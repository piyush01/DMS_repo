/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.discovery;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.app.bulkedit.DSpaceCSV;
import org.dspace.app.bulkedit.MetadataExport;
import org.dspace.app.util.OpenSearch;
import org.dspace.app.util.SyndicationFeed;
import org.dspace.app.webui.search.SearchProcessorException;
import org.dspace.app.webui.search.SearchRequestProcessor;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.DmsMetadaField;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.I18nUtil;
import org.dspace.core.LogManager;
import org.dspace.discovery.DiscoverQuery;
import org.dspace.discovery.DiscoverResult;
import org.dspace.discovery.SearchServiceException;
import org.dspace.discovery.SearchUtils;
import org.dspace.discovery.configuration.DiscoveryConfiguration;
import org.dspace.discovery.configuration.DiscoverySearchFilter;
import org.dspace.discovery.configuration.DiscoverySearchFilterFacet;
import org.dspace.discovery.configuration.DiscoverySortFieldConfiguration;
import org.dspace.handle.HandleManager;
import org.dspace.search.DSQuery;
import org.dspace.search.QueryArgs;
import org.dspace.search.QueryResults;
import org.dspace.sort.SortOption;
import org.dspace.usage.UsageEvent;
import org.dspace.usage.UsageSearchEvent;
import org.dspace.utils.DSpace;
import org.w3c.dom.Document;

public class DiscoverySearchRequestProcessor implements SearchRequestProcessor
{
    private static final int ITEMMAP_RESULT_PAGE_SIZE = 50;

    private static String msgKey = "org.dspace.app.webui.servlet.FeedServlet";

    /** log4j category */
    private static Logger log = Logger.getLogger(DiscoverySearchRequestProcessor.class);

    // locale-sensitive metadata labels
    private Map<String, Map<String, String>> localeLabels = null;

    private List<String> searchIndices = null;
    
    public synchronized void init()
    {
        if (localeLabels == null)
        {
            localeLabels = new HashMap<String, Map<String, String>>();
        }
        
        if (searchIndices == null)
        {
            searchIndices = new ArrayList<String>();
            DiscoveryConfiguration discoveryConfiguration = SearchUtils
                    .getDiscoveryConfiguration();
            searchIndices.add("any");
            for (DiscoverySearchFilter sFilter : discoveryConfiguration.getSearchFilters())
            {
                searchIndices.add(sFilter.getIndexFieldName());
            }
        }
    }

    public void doOpenSearch(Context context, HttpServletRequest request,
            HttpServletResponse response) throws SearchProcessorException,
            IOException, ServletException
    {
        init();
        
        // dispense with simple service document requests
        String scope = request.getParameter("scope");
        if (scope != null && "".equals(scope))
        {
            scope = null;
        }
        String path = request.getPathInfo();
        if (path != null && path.endsWith("description.xml"))
        {
            String svcDescrip = OpenSearch.getDescription(scope);
            response.setContentType(OpenSearch
                    .getContentType("opensearchdescription"));
            response.setContentLength(svcDescrip.length());
            response.getWriter().write(svcDescrip);
            return;
        }

        // get enough request parameters to decide on action to take
        String format = request.getParameter("format");
        if (format == null || "".equals(format))
        {
            // default to atom
            format = "atom";
        }

        // do some sanity checking
        if (!OpenSearch.getFormats().contains(format))
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // then the rest - we are processing the query
        DSpaceObject container;
        try
        {
            container = DiscoverUtility.getSearchScope(context,
                    request);
        }
        catch (Exception e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }
        DiscoverQuery queryArgs = DiscoverUtility.getDiscoverQuery(context,
                request, container, false);
        String query = queryArgs.getQuery();

        // Perform the search
        DiscoverResult qResults = null;
        try
        {
            qResults = SearchUtils.getSearchService().search(context,
                    container, queryArgs);
        }
        catch (SearchServiceException e)
        {
            log.error(
                    LogManager.getHeader(context, "opensearch", "query="
                            + queryArgs.getQuery() + ",scope=" + scope
                            + ",error=" + e.getMessage()), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        // Log
        log.info(LogManager.getHeader(context, "opensearch",
                "scope=" + scope + ",query=\"" + query + "\",results=("
                        + qResults.getTotalSearchResults() + ")"));

        // format and return results
        Map<String, String> labelMap = getLabels(request);
        DSpaceObject[] dsoResults = new DSpaceObject[qResults
                .getDspaceObjects().size()];
        qResults.getDspaceObjects().toArray(dsoResults);
        Document resultsDoc = OpenSearch.getResultsDoc(format, query,
                (int)qResults.getTotalSearchResults(), qResults.getStart(),
                qResults.getMaxResults(), container, dsoResults, labelMap);
        try
        {
            Transformer xf = TransformerFactory.newInstance().newTransformer();
            response.setContentType(OpenSearch.getContentType(format));
            xf.transform(new DOMSource(resultsDoc),
                    new StreamResult(response.getWriter()));
        }
        catch (TransformerException e)
        {
            log.error(e);
            throw new ServletException(e.toString());
        }
    }

    private Map<String, String> getLabels(HttpServletRequest request)
    {
        // Get access to the localized resource bundle
        Locale locale = UIUtil.getSessionLocale(request);
        Map<String, String> labelMap = localeLabels.get(locale.toString());
        if (labelMap == null)
        {
            labelMap = getLocaleLabels(locale);
            localeLabels.put(locale.toString(), labelMap);
        }
        return labelMap;
    }

    private Map<String, String> getLocaleLabels(Locale locale)
    {
        Map<String, String> labelMap = new HashMap<String, String>();
        labelMap.put(SyndicationFeed.MSG_UNTITLED, I18nUtil.getMessage(msgKey + ".notitle", locale));
        labelMap.put(SyndicationFeed.MSG_LOGO_TITLE, I18nUtil.getMessage(msgKey + ".logo.title", locale));
        labelMap.put(SyndicationFeed.MSG_FEED_DESCRIPTION, I18nUtil.getMessage(msgKey + ".general-feed.description", locale));
        labelMap.put(SyndicationFeed.MSG_UITYPE, SyndicationFeed.UITYPE_JSPUI);
        for (String selector : SyndicationFeed.getDescriptionSelectors())
        {
            labelMap.put("metadata." + selector, I18nUtil.getMessage(SyndicationFeed.MSG_METADATA + selector, locale));
        }
        return labelMap;
    }
    
    public void doSimpleSearch(Context context, HttpServletRequest request,
            HttpServletResponse response) throws SearchProcessorException,
            IOException, ServletException
    {
    	

       /* try
        {
            // Get the query
            String query = request.getParameter("query");
            int start = UIUtil.getIntParameter(request, "start");
            String advanced = request.getParameter("advanced");
            String fromAdvanced = request.getParameter("from_advanced");
            int sortBy = UIUtil.getIntParameter(request, "sort_by");
            String order = request.getParameter("order");
            int rpp = UIUtil.getIntParameter(request, "rpp");
            String advancedQuery = "";

            // can't start earlier than 0 in the results!
            if (start < 0)
            {
                start = 0;
            }

            int collCount = 0;
            int commCount = 0;
            int itemCount = 0;

            Item[] resultsItems;
            Collection[] resultsCollections;
            Community[] resultsCommunities;

            QueryResults qResults = null;
            QueryArgs qArgs = new QueryArgs();
            SortOption sortOption = null;

            if (request.getParameter("etal") != null)
            {
                qArgs.setEtAl(UIUtil.getIntParameter(request, "etal"));
            }

            try
            {
                if (sortBy > 0)
                {
                    sortOption = SortOption.getSortOption(sortBy);
                    qArgs.setSortOption(sortOption);
                }

                if (SortOption.ASCENDING.equalsIgnoreCase(order))
                {
                    qArgs.setSortOrder(SortOption.ASCENDING);
                }
                else
                {
                    qArgs.setSortOrder(SortOption.DESCENDING);
                }
            }
            catch (Exception e)
            {
            }

            // Override the page setting if exporting metadata
            if ("submit_export_metadata".equals(UIUtil.getSubmitButton(request, "submit")))
            {
                qArgs.setPageSize(Integer.MAX_VALUE);
            }
            else if (rpp > 0)
            {
                qArgs.setPageSize(rpp);
            }
            
            // if the "advanced" flag is set, build the query string from the
            // multiple query fields
            if (advanced != null)
            {
                query = qArgs.buildQuery(request);
                advancedQuery = qArgs.buildHTTPQuery(request);
            }

            // Ensure the query is non-null
            if (query == null)
            {
                query = "";
            }

            // Get the location parameter, if any
            String location = request.getParameter("location");

            // If there is a location parameter, we should redirect to
            // do the search with the correct location.
            if ((location != null) && !location.equals(""))
            {
                String url = "";

                if (!location.equals("/"))
                {
                    // Location is a Handle
                    url = "/handle/" + location;
                }

                // Encode the query
                query = URLEncoder.encode(query, Constants.DEFAULT_ENCODING);

                if (advancedQuery.length() > 0)
                {
                    query = query + "&from_advanced=true&" + advancedQuery;
                }

                // Do the redirect
                response.sendRedirect(response.encodeRedirectURL(request
                        .getContextPath()
                        + url + "/simple-search?query=" + query));

                return;
            }

            // Build log information
            String logInfo = "";

            // Get our location
            Community community = UIUtil.getCommunityLocation(request);
            Collection collection = UIUtil.getCollectionLocation(request);

            // get the start of the query results page
            //        List resultObjects = null;
            qArgs.setQuery(query);
            qArgs.setStart(start);

            // Perform the search
            if (collection != null)
            {
                logInfo = "collection_id=" + collection.getID() + ",";

                // Values for drop-down box
                request.setAttribute("community", community);
                request.setAttribute("collection", collection);

                qResults = DSQuery.doQuery(context, qArgs, collection);
            }
            else if (community != null)
            {
                logInfo = "community_id=" + community.getID() + ",";

                request.setAttribute("community", community);

                // Get the collections within the community for the dropdown box
                request
                        .setAttribute("collection.array", community
                                .getCollections());

                qResults = DSQuery.doQuery(context, qArgs, community);
            }
            else
            {
                // Get all communities for dropdown box
                Community[] communities = Community.findAll(context);
                request.setAttribute("community.array", communities);

                qResults = DSQuery.doQuery(context, qArgs);
            }

            // now instantiate the results and put them in their buckets
            for (int i = 0; i < qResults.getHitTypes().size(); i++)
            {
                Integer myType = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (myType.intValue())
                {
                case Constants.ITEM:
                    itemCount++;
                    break;

                case Constants.COLLECTION:
                    collCount++;
                    break;

                case Constants.COMMUNITY:
                    commCount++;
                    break;
                }
            }

            // Make objects from the handles - make arrays, fill them out
            resultsCommunities = new Community[commCount];
            resultsCollections = new Collection[collCount];
            resultsItems = new Item[itemCount];

            collCount = 0;
            commCount = 0;
            itemCount = 0;

            for (int i = 0; i < qResults.getHitTypes().size(); i++)
            {
                Integer myId    = qResults.getHitIds().get(i);
                String myHandle = qResults.getHitHandles().get(i);
                Integer myType  = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (myType.intValue())
                {
                case Constants.ITEM:
                    if (myId != null)
                    {
                        resultsItems[itemCount] = Item.find(context, myId);
                    }
                    else
                    {
                        resultsItems[itemCount] = (Item)HandleManager.resolveToObject(context, myHandle);
                    }

                    if (resultsItems[itemCount] == null)
                    {
                        throw new SQLException("Query \"" + query
                                + "\" returned unresolvable item");
                    }
                    itemCount++;
                    break;

                case Constants.COLLECTION:
                    if (myId != null)
                    {
                        resultsCollections[collCount] = Collection.find(context, myId);
                    }
                    else
                    {
                        resultsCollections[collCount] = (Collection)HandleManager.resolveToObject(context, myHandle);
                    }

                    if (resultsCollections[collCount] == null)
                    {
                        throw new SQLException("Query \"" + query
                                + "\" returned unresolvable collection");
                    }

                    collCount++;
                    break;

                case Constants.COMMUNITY:
                    if (myId != null)
                    {
                        resultsCommunities[commCount] = Community.find(context, myId);
                    }
                    else
                    {
                        resultsCommunities[commCount] = (Community)HandleManager.resolveToObject(context, myHandle);
                    }

                    if (resultsCommunities[commCount] == null)
                    {
                        throw new SQLException("Query \"" + query
                                + "\" returned unresolvable community");
                    }

                    commCount++;
                    break;
                }
            }

            // Log
            log.info(LogManager.getHeader(context, "search", logInfo + "query=\""
                    + query + "\",results=(" + resultsCommunities.length + ","
                    + resultsCollections.length + "," + resultsItems.length + ")"));

            // Pass in some page qualities
            // total number of pages
            int pageTotal = 1 + ((qResults.getHitCount() - 1) / qResults
                    .getPageSize());

            // current page being displayed
            int pageCurrent = 1 + (qResults.getStart() / qResults.getPageSize());

            // pageLast = min(pageCurrent+9,pageTotal)
            int pageLast = ((pageCurrent + 9) > pageTotal) ? pageTotal
                    : (pageCurrent + 9);

            // pageFirst = max(1,pageCurrent-9)
            int pageFirst = ((pageCurrent - 9) > 1) ? (pageCurrent - 9) : 1;


            //Fire an event to log our search
            DSpaceObject scope = null;
            if(collection != null){
                scope = collection;
            }else
            if(community != null){
                scope = community;
            }
            logSearch(context, request, query, pageCurrent, scope);


            // Pass the results to the display JSP
            request.setAttribute("items", resultsItems);
            request.setAttribute("communities", resultsCommunities);
            request.setAttribute("collections", resultsCollections);

            request.setAttribute("pagetotal", Integer.valueOf(pageTotal));
            request.setAttribute("pagecurrent", Integer.valueOf(pageCurrent));
            request.setAttribute("pagelast", Integer.valueOf(pageLast));
            request.setAttribute("pagefirst", Integer.valueOf(pageFirst));

            request.setAttribute("queryresults", qResults);

            // And the original query string
            request.setAttribute("query", query);

            request.setAttribute("order",  qArgs.getSortOrder());
            request.setAttribute("sortedBy", sortOption);

            if (AuthorizeManager.isAdmin(context))
            {
                // Set a variable to create admin buttons
                request.setAttribute("admin_button", Boolean.TRUE);
            }
            
            if ((fromAdvanced != null) && (qResults.getHitCount() == 0))
            {
                // send back to advanced form if no results
                Community[] communities = Community.findAll(context);
                request.setAttribute("communities", communities);
                request.setAttribute("no_results", "yes");

                Map<String, String> queryHash = qArgs.buildQueryMap(request);

                if (queryHash != null)
                {
                    for (Map.Entry<String, String> entry : queryHash.entrySet())
                    {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                }

                JSPManager.showJSP(request, response, "/search/advanced.jsp");
            }
            else if ("submit_export_metadata".equals(UIUtil.getSubmitButton(request, "submit")))
            {
                exportMetadata(context, response, resultsItems);
            }
            else
            {
                JSPManager.showJSP(request, response, "/search/results.jsp");
            }
        }
        catch (IllegalStateException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }
        catch (SQLException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }*/
    
    	
        Item[] resultsItems;
        Collection[] resultsCollections;
        Community[] resultsCommunities;
        DSpaceObject scope;
        String folderscope="";
        String subfoldescope="";
        
        try
        {
            DmsMetadaField searchfield[]=DmsMetadaField.getAllSearchFields(context,1);
             request.setAttribute("searchfield",searchfield);
            scope = DiscoverUtility.getSearchScope(context, request);
        }
        catch (IllegalStateException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }
        catch (SQLException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }

        DiscoveryConfiguration discoveryConfiguration = SearchUtils
                .getDiscoveryConfiguration(scope);
        List<DiscoverySortFieldConfiguration> sortFields = discoveryConfiguration
                .getSearchSortConfiguration().getSortFields();
        List<String> sortOptions = new ArrayList<String>();
        for (DiscoverySortFieldConfiguration sortFieldConfiguration : sortFields)
        {
            String sortField = SearchUtils.getSearchService().toSortFieldIndex(
                    sortFieldConfiguration.getMetadataField(),
                    sortFieldConfiguration.getType());
            sortOptions.add(sortField);
        }
        request.setAttribute("sortOptions", sortOptions);
        
        DiscoverQuery queryArgs = DiscoverUtility.getDiscoverQuery(context,
                request, scope, true);

        queryArgs.setSpellCheck(discoveryConfiguration.isSpellCheckEnabled()); 
        
        List<DiscoverySearchFilterFacet> availableFacet = discoveryConfiguration
                .getSidebarFacets();
        
        request.setAttribute("facetsConfig",
                availableFacet != null ? availableFacet
                        : new ArrayList<DiscoverySearchFilterFacet>());
        int etal = UIUtil.getIntParameter(request, "etal");
        if (etal == -1)
        {
            etal = ConfigurationManager
                    .getIntProperty("webui.itemlist.author-limit");
        }

        request.setAttribute("etal", etal);

        String query = queryArgs.getQuery();
        log.info("Query=====================>"+query);
        request.setAttribute("query", query);
        request.setAttribute("queryArgs", queryArgs);
        List<DiscoverySearchFilter> availableFilters = discoveryConfiguration
                .getSearchFilters();
        request.setAttribute("availableFilters", availableFilters);

        List<String[]> appliedFilters = DiscoverUtility.getFilters(request);
        request.setAttribute("appliedFilters", appliedFilters);
        List<String> appliedFilterQueries = new ArrayList<String>();
        for (String[] filter : appliedFilters)
        {
            appliedFilterQueries.add(filter[0] + "::" + filter[1] + "::"
                    + filter[2]);
        }
        request.setAttribute("appliedFilterQueries", appliedFilterQueries);
        List<DSpaceObject> scopes = new ArrayList<DSpaceObject>();
        List<DSpaceObject> folderscopes = new ArrayList<DSpaceObject>();
        List<DSpaceObject> subfolderscopes = new ArrayList<DSpaceObject>();
        if (scope == null)
        {
            Community[] topCommunities;
            try
            {
                topCommunities = Community.findAllTop(context);
            }
            catch (SQLException e)
            {
                throw new SearchProcessorException(e.getMessage(), e);
            }
            for (Community com : topCommunities)
            {
                scopes.add(com);
            }
        }
        else
        {
            try
            {
                DSpaceObject pDso = scope.getParentObject();
                while (pDso != null)
                {
                    // add to the available scopes in reverse order
                    scopes.add(0, pDso);
                    pDso = pDso.getParentObject();
                }
                scopes.add(scope);
                if (scope instanceof Community)
                {
                    Community[] comms = ((Community) scope).getSubcommunities();
                    for (Community com : comms)
                    {
                    	folderscope="folder";
                    	folderscopes.add(com);
                    	//scopes.add(com);
                    }
                    Collection[] colls = ((Community) scope).getCollections();
                    for (Collection col : colls)
                    {
                    	subfoldescope="subfolder";
                    	subfolderscopes.add(col);
                       // scopes.add(col);
                    }
                }
            }
            catch (SQLException e)
            {
                throw new SearchProcessorException(e.getMessage(), e);
            }
        }
        request.setAttribute("scope", scope);
        request.setAttribute("scopes", scopes);
        request.setAttribute("folderscope", folderscope);
        request.setAttribute("subfoldescope", subfoldescope);
        request.setAttribute("folderscopes", folderscopes);
        request.setAttribute("subfolderscopes", subfolderscopes);

        // Perform the search
        DiscoverResult qResults = null;
        try
        {
            qResults = SearchUtils.getSearchService().search(context, scope,
                    queryArgs);
            
            List<Community> resultsListComm = new ArrayList<Community>();
            List<Collection> resultsListColl = new ArrayList<Collection>();
            List<Item> resultsListItem = new ArrayList<Item>();

            for (DSpaceObject dso : qResults.getDspaceObjects())
            {
                if (dso instanceof Item)
                {
                    resultsListItem.add((Item) dso);
                }
                else if (dso instanceof Collection)
                {
                    resultsListColl.add((Collection) dso);

                }
                else if (dso instanceof Community)
                {
                    resultsListComm.add((Community) dso);
                }
            }

            // Make objects from the handles - make arrays, fill them out
            resultsCommunities = new Community[resultsListComm.size()];
            resultsCollections = new Collection[resultsListColl.size()];
            resultsItems = new Item[resultsListItem.size()];

            resultsCommunities = resultsListComm.toArray(resultsCommunities);
            resultsCollections = resultsListColl.toArray(resultsCollections);
            resultsItems = resultsListItem.toArray(resultsItems);

            // Log
            log.info(LogManager.getHeader(context, "search", "scope=" + scope
                    + ",query=\"" + query + "\",results=("
                    + resultsCommunities.length + ","
                    + resultsCollections.length + "," + resultsItems.length
                    + ")"));

            // Pass in some page qualities
            // total number of pages
            long pageTotal = 1 + ((qResults.getTotalSearchResults() - 1) / qResults
                    .getMaxResults());

            // current page being displayed
            long pageCurrent = 1 + (qResults.getStart() / qResults
                    .getMaxResults());

            // pageLast = min(pageCurrent+3,pageTotal)
            long pageLast = ((pageCurrent + 3) > pageTotal) ? pageTotal
                    : (pageCurrent + 3);

            // pageFirst = max(1,pageCurrent-3)
            long pageFirst = ((pageCurrent - 3) > 1) ? (pageCurrent - 3) : 1;

            // Pass the results to the display JSP
            request.setAttribute("items", resultsItems);
            request.setAttribute("communities", resultsCommunities);
            request.setAttribute("collections", resultsCollections);

            request.setAttribute("pagetotal", new Long(pageTotal));
            request.setAttribute("pagecurrent", new Long(pageCurrent));
            request.setAttribute("pagelast", new Long(pageLast));
            request.setAttribute("pagefirst", new Long(pageFirst));
            request.setAttribute("spellcheck", qResults.getSpellCheckQuery());
            
            request.setAttribute("queryresults", qResults);

            try
            {
                if (AuthorizeManager.isAdmin(context))
                {
                    // Set a variable to create admin buttons
                    request.setAttribute("admin_button", new Boolean(true));
                }
            }
            catch (SQLException e)
            {
                throw new SearchProcessorException(e.getMessage(), e);            }

            if ("submit_export_metadata".equals(UIUtil.getSubmitButton(request,
                    "submit")))
            {
                exportMetadata(context, response, resultsItems);
            }
        }
        catch (SearchServiceException e)
        {
            log.error(
                    LogManager.getHeader(context, "search", "query="
                            + queryArgs.getQuery() + ",scope=" + scope
                            + ",error=" + e.getMessage()), e);
            request.setAttribute("search.error", true);
            request.setAttribute("search.error.message", e.getMessage());
        }

        JSPManager.showJSP(request, response, "/search/discovery.jsp");
    }
    protected void logSearch(Context context, HttpServletRequest request, String query, int start, DSpaceObject scope) {
        UsageSearchEvent searchEvent = new UsageSearchEvent(
                UsageEvent.Action.SEARCH,
                request,
                context,
                null, Arrays.asList(query), scope);


        if(!StringUtils.isBlank(request.getParameter("rpp"))){
            searchEvent.setRpp(Integer.parseInt(request.getParameter("rpp")));
        }
        if(!StringUtils.isBlank(request.getParameter("sort_by"))){
            searchEvent.setSortBy(request.getParameter("sort_by"));
        }
        if(!StringUtils.isBlank(request.getParameter("order"))){
            searchEvent.setSortOrder(request.getParameter("order"));
        }
        if(!StringUtils.isBlank(request.getParameter("start"))){
            searchEvent.setPage(start);
        }

        //Fire our event
        new DSpace().getEventService().fireEvent(searchEvent);
    }
    /**
     * Export the search results as a csv file
     * 
     * @param context
     *            The DSpace context
     * @param response
     *            The request object
     * @param items
     *            The result items
     * @throws IOException
     * @throws ServletException
     */
    protected void exportMetadata(Context context,
            HttpServletResponse response, Item[] items) throws IOException,
            ServletException
    {
        // Log the attempt
        log.info(LogManager.getHeader(context, "metadataexport",
                "exporting_search"));

        // Export a search view
        ArrayList iids = new ArrayList();
        for (Item item : items)
        {
            iids.add(item.getID());
        }
        ItemIterator ii = new ItemIterator(context, iids);
        MetadataExport exporter = new MetadataExport(context, ii, false);

        // Perform the export
        DSpaceCSV csv = exporter.export();

        // Return the csv file
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=search-results.csv");
        PrintWriter out = response.getWriter();
        out.write(csv.toString());
        out.flush();
        out.close();
        log.info(LogManager.getHeader(context, "metadataexport",
                "exported_file:search-results.csv"));
        return;
    }

    /**
     * Method for constructing the discovery advanced search form
     * 
     * author: Andrea Bollini
     */
    @Override
    public void doAdvancedSearch(Context context, HttpServletRequest request,
            HttpServletResponse response) throws SearchProcessorException,
            IOException, ServletException
            {    
                // just redirect to the simple search servlet.
                // The advanced form is always displayed with Discovery togheter with
                // the search result
                // the first access to the advanced form performs a search for
                // "anythings" (SOLR *:*)
               /* response.sendRedirect(request.getContextPath() + "/simple-search");*/
    	 // just build a list of top-level communities and pass along to the jsp
      Community[] communities;
        try
        {
        	 DmsMetadaField searchfield[]=DmsMetadaField.getAllSearchFields(context,1);
             request.setAttribute("searchfield",searchfield);
            communities = Community.findAllTop(context);
        }
        catch (SQLException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }

        request.setAttribute("communities", communities);

        JSPManager.showJSP(request, response, "/search/advanced.jsp");
    	

      
            }
  

    /**
     * Method for searching authors in item map
     * 
     * author: gam
     */
    @Override
    public void doItemMapSearch(Context context, HttpServletRequest request,
            HttpServletResponse response) throws SearchProcessorException, ServletException, IOException
    {
        String queryString = (String) request.getParameter("query");
        Collection collection = (Collection) request.getAttribute("collection");
        int page = UIUtil.getIntParameter(request, "page")-1;
        int offset = page > 0? page * ITEMMAP_RESULT_PAGE_SIZE:0;
        String idx = (String) request.getParameter("index");
        if (StringUtils.isNotBlank(idx) && !idx.equalsIgnoreCase("any"))
        {
            queryString = idx + ":(" + queryString + ")";
        }
        DiscoverQuery query = new DiscoverQuery();
        query.setQuery(queryString);
        query.addFilterQueries("-location:l"+collection.getID());
        query.setMaxResults(ITEMMAP_RESULT_PAGE_SIZE);
        query.setStart(offset);

        DiscoverResult results = null;
        try
        {
            results = SearchUtils.getSearchService().search(context, query);
        }
        catch (SearchServiceException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }

        Map<Integer, Item> items = new HashMap<Integer, Item>();

        List<DSpaceObject> resultDSOs = results.getDspaceObjects();
        for (DSpaceObject dso : resultDSOs)
        {
            if (dso != null && dso.getType() == Constants.ITEM)
            {
                // no authorization check is required as discovery is right aware
                Item item = (Item) dso;
                items.put(Integer.valueOf(item.getID()), item);
            }
        }

        request.setAttribute("browsetext", queryString);
        request.setAttribute("items", items);
        request.setAttribute("more", results.getTotalSearchResults() > offset + ITEMMAP_RESULT_PAGE_SIZE);
        request.setAttribute("browsetype", "Add");
        request.setAttribute("page", page > 0 ? page + 1 : 1);
        
        JSPManager.showJSP(request, response, "itemmap-browse.jsp");
    }
    
    @Override
    public String getI18NKeyPrefix()
    {
        return "jsp.search.filter.";
    }
    
    @Override
    public List<String> getSearchIndices()
    {
        init();
        return searchIndices;
    }
  /*
     * 
     * code add by Sanjeev Kumar
     * 
     * */
	@Override
	public void doEzeeAdvancedSearch(Context context, HttpServletRequest request, HttpServletResponse response)
			throws SearchProcessorException, IOException, ServletException {

        try
        {
            // Get the query
            String query = request.getParameter("query");
            int start = UIUtil.getIntParameter(request, "start");
            String advanced = request.getParameter("advanced");
            String fromAdvanced = request.getParameter("from_advanced");
            int sortBy = UIUtil.getIntParameter(request, "sort_by");
            String order = request.getParameter("order");
            int rpp = UIUtil.getIntParameter(request, "rpp");
            String advancedQuery = "";

            DmsMetadaField searchfield[]=DmsMetadaField.getAllSearchFields(context,1);
            request.setAttribute("searchfield",searchfield);
            // can't start earlier than 0 in the results!
            if (start < 0)
            {
                start = 0;
            }

            int collCount = 0;
            int commCount = 0;
            int itemCount = 0;

            Item[] resultsItems;
            Collection[] resultsCollections;
            Community[] resultsCommunities;

            QueryResults qResults = null;
            QueryArgs qArgs = new QueryArgs();
            SortOption sortOption = null;

            if (request.getParameter("etal") != null)
            {
                qArgs.setEtAl(UIUtil.getIntParameter(request, "etal"));
            }

            try
            {
                if (sortBy > 0)
                {
                    sortOption = SortOption.getSortOption(sortBy);
                    qArgs.setSortOption(sortOption);
                }

                if (SortOption.ASCENDING.equalsIgnoreCase(order))
                {
                    qArgs.setSortOrder(SortOption.ASCENDING);
                }
                else
                {
                    qArgs.setSortOrder(SortOption.DESCENDING);
                }
            }
            catch (Exception e)
            {
            }

            // Override the page setting if exporting metadata
            if ("submit_export_metadata".equals(UIUtil.getSubmitButton(request, "submit")))
            {
                qArgs.setPageSize(Integer.MAX_VALUE);
            }
            else if (rpp > 0)
            {
                qArgs.setPageSize(rpp);
            }
            
            // if the "advanced" flag is set, build the query string from the
            // multiple query fields
            if (advanced != null)
            {
                query = qArgs.buildQuery(request);
                advancedQuery = qArgs.buildHTTPQuery(request);
            }

            // Ensure the query is non-null
            if (query == null)
            {
                query = "";
            }

            // Get the location parameter, if any
            String location = request.getParameter("location");

            // If there is a location parameter, we should redirect to
            // do the search with the correct location.
            if ((location != null) && !location.equals(""))
            {
                String url = "";

                if (!location.equals("/"))
                {
                    // Location is a Handle
                   // url = "/handle/" + location;
                }

                // Encode the query
                query = URLEncoder.encode(query, Constants.DEFAULT_ENCODING);

                if (advancedQuery.length() > 0)
                {
                    query = query + "&from_advanced=true&" + advancedQuery;
                }

                // Do the redirect
                response.sendRedirect(response.encodeRedirectURL(request
                        .getContextPath()
                        + url + "/eZeeAdvance-search?query=" + query));

                return;
            }

            // Build log information
            String logInfo = "";

            // Get our location
            Community community = UIUtil.getCommunityLocation(request);
            Collection collection = UIUtil.getCollectionLocation(request);

            // get the start of the query results page
            //        List resultObjects = null;
            qArgs.setQuery(query);
            qArgs.setStart(start);

            // Perform the search
            if (collection != null)
            {
                logInfo = "collection_id=" + collection.getID() + ",";

                // Values for drop-down box
                request.setAttribute("community", community);
                request.setAttribute("collection", collection);

                qResults = DSQuery.doQuery(context, qArgs, collection);
            }
            else if (community != null)
            {
                logInfo = "community_id=" + community.getID() + ",";

                request.setAttribute("community", community);

                // Get the collections within the community for the dropdown box
                request
                        .setAttribute("collection.array", community
                                .getCollections());

                qResults = DSQuery.doQuery(context, qArgs, community);
            }
            else
            {
                // Get all communities for dropdown box
                Community[] communities = Community.findAll(context);
                request.setAttribute("community.array", communities);

                qResults = DSQuery.doQuery(context, qArgs);
            }

            // now instantiate the results and put them in their buckets
            for (int i = 0; i < qResults.getHitTypes().size(); i++)
            {
                Integer myType = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (myType.intValue())
                {
                case Constants.ITEM:
                    itemCount++;
                    break;

                case Constants.COLLECTION:
                    collCount++;
                    break;

                case Constants.COMMUNITY:
                    commCount++;
                    break;
                }
            }

            // Make objects from the handles - make arrays, fill them out
            resultsCommunities = new Community[commCount];
            resultsCollections = new Collection[collCount];
            resultsItems = new Item[itemCount];

            collCount = 0;
            commCount = 0;
            itemCount = 0;

            for (int i = 0; i < qResults.getHitTypes().size(); i++)
            {
                Integer myId    = qResults.getHitIds().get(i);
                String myHandle = qResults.getHitHandles().get(i);
                Integer myType  = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (myType.intValue())
                {
                case Constants.ITEM:
                    if (myId != null)
                    {
                        resultsItems[itemCount] = Item.find(context, myId);
                    }
                    else
                    {
                        resultsItems[itemCount] = (Item)HandleManager.resolveToObject(context, myHandle);
                    }

                    if (resultsItems[itemCount] == null)
                    {
                        throw new SQLException("Query \"" + query
                                + "\" returned unresolvable item");
                    }
                    itemCount++;
                    break;

                case Constants.COLLECTION:
                    if (myId != null)
                    {
                        resultsCollections[collCount] = Collection.find(context, myId);
                    }
                    else
                    {
                        resultsCollections[collCount] = (Collection)HandleManager.resolveToObject(context, myHandle);
                    }

                    if (resultsCollections[collCount] == null)
                    {
                        throw new SQLException("Query \"" + query
                                + "\" returned unresolvable collection");
                    }

                    collCount++;
                    break;

                case Constants.COMMUNITY:
                    if (myId != null)
                    {
                        resultsCommunities[commCount] = Community.find(context, myId);
                    }
                    else
                    {
                        resultsCommunities[commCount] = (Community)HandleManager.resolveToObject(context, myHandle);
                    }

                    if (resultsCommunities[commCount] == null)
                    {
                        throw new SQLException("Query \"" + query
                                + "\" returned unresolvable community");
                    }

                    commCount++;
                    break;
                }
            }

            // Log
            log.info(LogManager.getHeader(context, "search", logInfo + "query=\""
                    + query + "\",results=(" + resultsCommunities.length + ","
                    + resultsCollections.length + "," + resultsItems.length + ")"));

            // Pass in some page qualities
            // total number of pages
            int pageTotal = 1 + ((qResults.getHitCount() - 1) / qResults
                    .getPageSize());

            // current page being displayed
            int pageCurrent = 1 + (qResults.getStart() / qResults.getPageSize());

            // pageLast = min(pageCurrent+9,pageTotal)
            int pageLast = ((pageCurrent + 9) > pageTotal) ? pageTotal
                    : (pageCurrent + 9);

            // pageFirst = max(1,pageCurrent-9)
            int pageFirst = ((pageCurrent - 9) > 1) ? (pageCurrent - 9) : 1;


            //Fire an event to log our search
            DSpaceObject scope = null;
            if(collection != null){
                scope = collection;
            }else
            if(community != null){
                scope = community;
            }
            logSearch(context, request, query, pageCurrent, scope);


            // Pass the results to the display JSP
            request.setAttribute("items", resultsItems);
            request.setAttribute("communities", resultsCommunities);
            request.setAttribute("collections", resultsCollections);

            request.setAttribute("pagetotal", Integer.valueOf(pageTotal));
            request.setAttribute("pagecurrent", Integer.valueOf(pageCurrent));
            request.setAttribute("pagelast", Integer.valueOf(pageLast));
            request.setAttribute("pagefirst", Integer.valueOf(pageFirst));

            request.setAttribute("queryresults", qResults);

            // And the original query string
            request.setAttribute("query", query);

            request.setAttribute("order",  qArgs.getSortOrder());
            request.setAttribute("sortedBy", sortOption);

            if (AuthorizeManager.isAdmin(context))
            {
                // Set a variable to create admin buttons
                request.setAttribute("admin_button", Boolean.TRUE);
            }
            
            if ((fromAdvanced != null) && (qResults.getHitCount() == 0))
            {
                // send back to advanced form if no results
                Community[] communities = Community.findAll(context);
                request.setAttribute("communities", communities);
                request.setAttribute("no_results", "yes");

                Map<String, String> queryHash = qArgs.buildQueryMap(request);

                if (queryHash != null)
                {
                    for (Map.Entry<String, String> entry : queryHash.entrySet())
                    {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                }

                JSPManager.showJSP(request, response, "/search/advanced.jsp");
            }
            else if ("submit_export_metadata".equals(UIUtil.getSubmitButton(request, "submit")))
            {
                exportMetadata(context, response, resultsItems);
            }
            else
            {
                JSPManager.showJSP(request, response, "/search/results.jsp");
            }
        }
        catch (IllegalStateException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }
        catch (SQLException e)
        {
            throw new SearchProcessorException(e.getMessage(), e);
        }
    
		
	}
	 /*
     * 
     * code End by sanjeev kumar
     * 
     * */
}
