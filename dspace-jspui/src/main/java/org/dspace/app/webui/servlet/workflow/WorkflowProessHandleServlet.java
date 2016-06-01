package org.dspace.app.webui.servlet.workflow;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.app.util.AuthorizeUtil;
import org.dspace.app.util.GoogleMetadata;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.servlet.HandleServlet;
import org.dspace.app.webui.util.Authenticate;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataSchema;
import org.dspace.content.crosswalk.CrosswalkException;
import org.dspace.content.crosswalk.DisseminationCrosswalk;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.PluginManager;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.eperson.Subscribe;
import org.dspace.handle.HandleManager;
import org.dspace.license.CreativeCommons;
import org.dspace.plugin.CollectionHomeProcessor;
import org.dspace.plugin.CommunityHomeProcessor;
import org.dspace.plugin.ItemHomeProcessor;
import org.dspace.usage.UsageEvent;
import org.dspace.utils.DSpace;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.XMLOutputter;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.WorkflowManager;
import org.dspace.workflowmanager.WorkflowMasterBean;
import org.dspace.workflowprocess.AdhocWorkflowManager;
import org.dspace.workflowprocess.WorkflowProcessManager;

public class WorkflowProessHandleServlet extends DSpaceServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3251380672099299921L;
	/** log4j category */
    private static Logger log = Logger.getLogger(WorkflowProessHandleServlet.class);
    private WorkflowProcessManager workflowProcessManager=new WorkflowProcessManager();
    private WorkflowManager workflowManager=new WorkflowManager();
    private WorkflowMasterBean workflowMasterBean=new WorkflowMasterBean();
    private TaskManager taskManager=new TaskManager();
    private AdhocWorkflowManager adhocWorkflowManager=new AdhocWorkflowManager();
    
    /** log4j category */

    /** For obtaining &lt;meta&gt; elements to put in the &lt;head&gt; */
    private DisseminationCrosswalk xHTMLHeadCrosswalk;

    public WorkflowProessHandleServlet()
    {
        super();
        xHTMLHeadCrosswalk = (DisseminationCrosswalk) PluginManager
                .getNamedPlugin(DisseminationCrosswalk.class, "XHTML_HEAD_ITEM");
    }   
    
    
    public TaskManager getTaskManager() {
		return taskManager;
	}


	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}


	public WorkflowManager getWorkflowManager() {
		return workflowManager;
	}



	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}



	public WorkflowMasterBean getWorkflowMasterBean() {
		return workflowMasterBean;
	}



	public void setWorkflowMasterBean(WorkflowMasterBean workflowMasterBean) {
		this.workflowMasterBean = workflowMasterBean;
	}



	public WorkflowProcessManager getWorkflowProcessManager() {
		return workflowProcessManager;
	}



	public void setWorkflowProcessManager(
			WorkflowProcessManager workflowProcessManager) {
		this.workflowProcessManager = workflowProcessManager;
	}



	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
    
    	String file_no=request.getParameter("file_no");
    	String handle1=request.getParameter("handle");
    	String file_link=request.getParameter("file_link");
    	String file_name=request.getParameter("file_name");
    	String status=request.getParameter("status");
    	String item_id=request.getParameter("item_id");
        request.setAttribute("file_no", file_no);
        request.setAttribute("handle", handle1);
        request.setAttribute("file_link", file_link);
        request.setAttribute("file_name", file_name);
        request.setAttribute("item_id", item_id);
        
        if(status!=null && handle1!=null && status.equals("all")){

            String handle = null;
            String extraPathInfo = null;
            DSpaceObject dso = null;

            // Original path info, of the form "1721.x/1234"
            // or "1721.x/1234/extra/stuff"
            String path ="/"+handle1; 
            if (path != null)
            {
                // substring(1) is to remove initial '/'
                path = path.substring(1);
                try
                {
                    // Extract the Handle
                    int firstSlash = path.indexOf('/');
                    int secondSlash = path.indexOf('/', firstSlash + 1);
                    if (secondSlash != -1)
                    {
                        // We have extra path info
                        handle = path.substring(0, secondSlash);
                        extraPathInfo = path.substring(secondSlash);
                    }
                    else
                    {                    	
                        handle = path;
                    }
                }
                catch (NumberFormatException nfe)
                {
                    // Leave handle as null
                }
            }

            // Find out what the handle relates to
            if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);
            }

            if (dso == null)
            {
                log.info(LogManager.getHeader(context, "invalid_id", "path=" + path));
                JSPManager.showInvalidIDError(request, response, StringEscapeUtils.escapeHtml(path), -1);

                return;
            }

           
            
            // OK, we have a valid Handle. What is it?
            if (dso.getType() == Constants.ITEM)
            {
            	List<WorkflowMasterBean> list=new ArrayList<WorkflowMasterBean>();
       	     list=workflowManager.getAllWorkflow(context);
       	     request.setAttribute("workflowlist", list);
             // do we actually want to display the item, or forward to another page?
                if ((extraPathInfo == null) || (extraPathInfo.equals("/")))
                {
                    Item item = (Item) dso;

                    // Only use last-modified if this is an anonymous access
                    // - caching content that may be generated under authorisation
                    //   is a security problem

                    if (context.getCurrentUser() == null)
                    {
                        response.setDateHeader("Last-Modified", item
                                .getLastModified().getTime());

                        // Check for if-modified-since header

                        long modSince = request.getDateHeader("If-Modified-Since");

                        if (modSince != -1 && item.getLastModified().getTime() < modSince)
                        {
                            // Item has not been modified since requested date,
                            // hence bitstream has not been, either; return 304
                            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        }
                        else
                        {
                            // Display the item page
                            displayItem(context, request, response, item, handle);
                        }
                    }
                    else
                    {
                        // Display the item page
                        displayItem(context, request, response, item, handle);
                    }
                }
                else
                {
                    log.debug("Found Item with extraPathInfo => Error.");
                    JSPManager.showInvalidIDError(request, response, StringEscapeUtils.escapeHtml(path), -1);
                    return;
                }

            }     
           
            else
            {
                // Shouldn't happen. Log and treat as invalid ID
                log.info(LogManager.getHeader(context,
                        "Handle not an item, collection or community", "handle="
                                + handle));
                JSPManager.showInvalidIDError(request, response, StringEscapeUtils.escapeHtml(path), -1);

                return;
            }
        	
        }else{
        	List<WorkflowMasterBean> list=new ArrayList<WorkflowMasterBean>();
    	     list=workflowManager.getAllWorkflow(context);
    	     request.setAttribute("workflowlist", list);
       	   JSPManager.showJSP(request, response, "/workflow-singleprocess.jsp");
        }
         
       
    }
	
	protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {		
    	
		String handle=request.getParameter("handle");
        String status=request.getParameter("status");	
        int internalID = UIUtil.getIntParameter(request, "item_id");
        request.setAttribute("userlist", taskManager.getUser(context));
        List<WorkflowMasterBean> list=new ArrayList<WorkflowMasterBean>();
	     list=workflowManager.getAllWorkflow(context);
	     request.setAttribute("workflowlist", list);
	     
      if(status.equals("bitstream"))
      { 
    	  showEditFromProcess(context,request,response,internalID,handle);
      }
      
      else{
    	if(handle!=null && !handle.equals("")){
        	Integer rowId=0;
        	Integer workflow_id = Integer.valueOf(request.getParameter("workflowId"));
        	
        	if(workflow_id==1){
        		log.info("Ad-hoc workflow");
        		try{
        		rowId=adhocWorkflowManager.saveAdhocTask(context, request);
        		}catch(ParseException pe){
        			log.error(""+pe);
        		}
        		log.info("save adhock workflow:-------------------------"+rowId);
        	}
        	else{
        	rowId=workflowProcessManager.sendWorkflow(context, request);
        	}
        	if(rowId>0){
        	request.setAttribute("message", "Workflow has been successfully sent.");
        	}else{
        		request.setAttribute("message", "Workflow has not been successfully sent! Plz try again.");
        	}
        	 List<WorkflowMasterBean> workflowlist=new ArrayList<WorkflowMasterBean>();
     	     list=workflowManager.getAllWorkflow(context);
     	     request.setAttribute("workflowlist", list);
     	    showEditFromProcess(context,request,response,internalID,handle);
             context.complete();
           }
      }
    }
	
	
	private void showEditFromProcess(Context context, HttpServletRequest request,HttpServletResponse response,Integer internalID,String handle) throws ServletException,
    IOException, SQLException, AuthorizeException
	{
		 boolean showError = false;
	        // See if an item ID or Handle was passed in
	        Item itemToEdit = null;

	        if (internalID > 0)
	        {
	            itemToEdit = Item.find(context, internalID);

	            showError = (itemToEdit == null);
	        }
	        else if ((handle != null) && !handle.equals(""))
	        {
	            // resolve handle
	            DSpaceObject dso = HandleManager.resolveToObject(context, handle.trim());

	            // make sure it's an ITEM
	            if ((dso != null) && (dso.getType() == Constants.ITEM))
	            {
	                itemToEdit = (Item) dso;
	                showError = false;
	            }
	            else
	            {
	                showError = true;
	            }
	        }

	        // Show edit form if appropriate
	        if (itemToEdit != null)
	        {
	           
	            showEditForm(context, request, response, itemToEdit);
	        }
	}
	
	private void showEditForm(Context context, HttpServletRequest request,
            HttpServletResponse response, Item item) throws ServletException,
            IOException, SQLException, AuthorizeException
    {
        String handle = HandleManager.findHandle(context, item);
        // Collections
        Collection[] collections = item.getCollections();
        request.setAttribute("item", item);
        request.setAttribute("handle", handle);
        request.setAttribute("collections", collections);      
        JSPManager.showJSP(request, response, "/workflowprocess.jsp");
    }
	
	private void displayItem(Context context, HttpServletRequest request,
            HttpServletResponse response, Item item, String handle)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        // perform any necessary pre-processing
        preProcessItemHome(context, request, response, item);
        
        

        // Ensure the user has authorisation
        AuthorizeManager.authorizeAction(context, item, Constants.READ);

        log
                .info(LogManager.getHeader(context, "view_item", "handle="
                        + handle));

       

        // Get the collections
        Collection[] collections = item.getCollections();

        // For the breadcrumbs, get the first collection and the first community
        // that is in. FIXME: Not multiple-inclusion friendly--should be
        // smarter, context-sensitive
        request.setAttribute("dspace.collection", item.getOwningCollection());

        Community[] comms = item.getOwningCollection().getCommunities();
        request.setAttribute("dspace.community", comms[0]);

        /*
         * Find all the "parent" communities for the collection
         */
        request.setAttribute("dspace.communities", getParents(comms[0], true));

        // Full or simple display?
        boolean displayAll = false;
        String modeParam = request.getParameter("mode");

        if ((modeParam != null) && modeParam.equalsIgnoreCase("full"))
        {
            displayAll = true;
        }

        String headMetadata = "";

        // Produce <meta> elements for header from crosswalk
        try
        {
            List<Element> l = xHTMLHeadCrosswalk.disseminateList(item);
            StringWriter sw = new StringWriter();

            XMLOutputter xmlo = new XMLOutputter();
            xmlo.output(new Text("\n"), sw);
            for (Element e : l)
            {
                // FIXME: we unset the Namespace so it's not printed.
                // This is fairly yucky, but means the same crosswalk should
                // work for Manakin as well as the JSP-based UI.
                e.setNamespace(null);
                xmlo.output(e, sw);
                xmlo.output(new Text("\n"), sw);
            }
            boolean googleEnabled = ConfigurationManager.getBooleanProperty("google-metadata.enable", false);
            if (googleEnabled)
            {
                // Add Google metadata field names & values
                GoogleMetadata gmd = new GoogleMetadata(context, item);
                xmlo.output(new Text("\n"), sw);

                for (Element e: gmd.disseminateList())
                {
                    xmlo.output(e, sw);
                    xmlo.output(new Text("\n"), sw);
                }
            }
            headMetadata = sw.toString();
        }
        catch (CrosswalkException ce)
        {
            throw new ServletException(ce);
        }

        // Enable suggest link or not
        boolean suggestEnable = false;
        if (!ConfigurationManager.getBooleanProperty("webui.suggest.enable"))
        {
            // do nothing, the suggestLink is already set to false 
        }
        else
        {
            // it is in general enabled
            suggestEnable= true;
            
            // check for the enable only for logged in users option
            if(!ConfigurationManager.getBooleanProperty("webui.suggest.loggedinusers.only"))
            {
                // do nothing, the suggestLink stays as it is
            }
            else
            {
                // check whether there is a logged in user
                suggestEnable = (context.getCurrentUser() == null ? false : true);
            }
        }

        // Fire usage event.
        new DSpace().getEventService().fireEvent(
            		new UsageEvent(
            				UsageEvent.Action.VIEW,
            				request,
            				context,
            				item));

        // Set attributes and display
        request.setAttribute("suggest.enable", Boolean.valueOf(suggestEnable));
        request.setAttribute("display.all", Boolean.valueOf(displayAll));
        request.setAttribute("item", item);
        request.setAttribute("collections", collections);
        request.setAttribute("dspace.layout.head", headMetadata);
        JSPManager.showJSP(request, response, "/workflowprocess.jsp");
    }
    
    private void preProcessItemHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Item item)
        throws ServletException, IOException, SQLException
    {
        try
        {
            ItemHomeProcessor[] chp = (ItemHomeProcessor[]) PluginManager.getPluginSequence(ItemHomeProcessor.class);
            for (int i = 0; i < chp.length; i++)
            {
                chp[i].process(context, request, response, item);
            }
        }
        catch (Exception e)
        {
            log.error("caught exception: ", e);
            throw new ServletException(e);
        }
    }

    /**
     * Show a community home page, or deal with button press on home page
     * 
     * @param context
     *            Context object
     * @param request
     *            the HTTP request
     * @param response
     *            the HTTP response
     * @param community
     *            the community
     */
    private void communityHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Community community)
            throws ServletException, IOException, SQLException
    {
        // Handle click on a browse or search button
        if (!handleButton(request, response, community.getHandle()))
        {
            // No button pressed, display community home page
            log.info(LogManager.getHeader(context, "view_community",
                    "community_id=" + community.getID()));

            // Get the collections within the community
            Collection[] collections = community.getCollections();

            // get any subcommunities of the community
            Community[] subcommunities = community.getSubcommunities();

            // perform any necessary pre-processing
            preProcessCommunityHome(context, request, response, community);

            // is the user a COMMUNITY_EDITOR?
            if (community.canEditBoolean())
            {
                // set a variable to create an edit button
                request.setAttribute("editor_button", Boolean.TRUE);
            }

            // can they add to this community?
            if (AuthorizeManager.authorizeActionBoolean(context, community,
                    Constants.ADD))
            {
                // set a variable to create an edit button
                request.setAttribute("add_button", Boolean.TRUE);
            }

            // can they remove from this community?
            if (AuthorizeManager.authorizeActionBoolean(context, community,
                    Constants.REMOVE))
            {
                // set a variable to create an edit button
                request.setAttribute("remove_button", Boolean.TRUE);
            }

            // Fire usage event.
            new DSpace().getEventService().fireEvent(
            		new UsageEvent(
            				UsageEvent.Action.VIEW,
            				request,
            				context,
            				community));

            // Forward to community home page
            request.setAttribute("community", community);
            request.setAttribute("collections", collections);
            request.setAttribute("subcommunities", subcommunities);
            JSPManager.showJSP(request, response, "/community-home.jsp");
        }
    }

    private void preProcessCommunityHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Community community)
    	throws ServletException, IOException, SQLException
    {
    	try
    	{
    		CommunityHomeProcessor[] chp = (CommunityHomeProcessor[]) PluginManager.getPluginSequence(CommunityHomeProcessor.class);
    		for (int i = 0; i < chp.length; i++)
    		{
    			chp[i].process(context, request, response, community);
    		}
    	}
    	catch (Exception e)
    	{
    		log.error("caught exception: ", e);
    		throw new ServletException(e);
    	}
    }
    
    
    /**
     * Show a collection home page, or deal with button press on home page
     * 
     * @param context
     *            Context object
     * @param request
     *            the HTTP request
     * @param response
     *            the HTTP response
     * @param community
     *            the community
     * @param collection
     *            the collection
     */
    private void collectionHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Community community,
            Collection collection) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // Handle click on a browse or search button
        if (!handleButton(request, response, collection.getHandle()))
        {
            // Will need to know whether to commit to DB
            boolean updated = false;

            // No search or browse button pressed, check for
            if (request.getParameter("submit_subscribe") != null)
            {
                // Subscribe button pressed.
                // Only registered can subscribe, so redirect unless logged in.
                if (context.getCurrentUser() == null &&
                    !Authenticate
                            .startAuthentication(context, request, response))

                {
                    return;
                }
                else
                {
                    Subscribe.subscribe(context, context.getCurrentUser(),
                            collection);
                    updated = true;
                }
            }
            else if (request.getParameter("submit_unsubscribe") != null)
            {
                Subscribe.unsubscribe(context, context.getCurrentUser(),
                        collection);
                updated = true;
            }

            // display collection home page
            log.info(LogManager.getHeader(context, "view_collection",
                    "collection_id=" + collection.getID()));

            // perform any necessary pre-processing
            preProcessCollectionHome(context, request, response, collection);
            
            // Is the user logged in/subscribed?
            EPerson e = context.getCurrentUser();
            boolean subscribed = false;

            if (e != null)
            {
                subscribed = Subscribe.isSubscribed(context, e, collection);

                // is the user a COLLECTION_EDITOR?
                if (collection.canEditBoolean(true))
                {
                    // set a variable to create an edit button
                    request.setAttribute("editor_button", Boolean.TRUE);
                }

                // can they admin this collection?
                if (AuthorizeManager.authorizeActionBoolean(context,
                        collection, Constants.COLLECTION_ADMIN))
                {
                    request.setAttribute("admin_button", Boolean.TRUE);

                    // give them a button to manage submitter list
                    // what group is the submitter?
                    Group group = collection.getSubmitters();

                    if (group != null)
                    {
                        request.setAttribute("submitters", group);
                    }
                }

                // can they submit to this collection?
                if (AuthorizeManager.authorizeActionBoolean(context,
                        collection, Constants.ADD))
                {
                    request
                            .setAttribute("can_submit_button",
                                    Boolean.TRUE);

                }
                else
                {
                    request.setAttribute("can_submit_button",
                            Boolean.FALSE);
                }
            }

            // Fire usage event.
            new DSpace().getEventService().fireEvent(
            		new UsageEvent(
            				UsageEvent.Action.VIEW,
            				request,
            				context,
            				collection));

            // Forward to collection home page
            request.setAttribute("collection", collection);
            request.setAttribute("community", community);
            request.setAttribute("logged.in", Boolean.valueOf(e != null));
            request.setAttribute("subscribed", Boolean.valueOf(subscribed));
            JSPManager.showJSP(request, response, "/collection-home.jsp");

            if (updated)
            {
                context.complete();
            }
        }
    }

    private void preProcessCollectionHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Collection collection)
    	throws ServletException, IOException, SQLException
    {
    	try
    	{
    		CollectionHomeProcessor[] chp = (CollectionHomeProcessor[]) PluginManager.getPluginSequence(CollectionHomeProcessor.class);
    		for (int i = 0; i < chp.length; i++)
    		{
    			chp[i].process(context, request, response, collection);
    		}
    	}
    	catch (Exception e)
    	{
    		log.error("caught exception: ", e);
    		throw new ServletException(e);
    	}
    }
    
    /**
     * Check to see if a browse or search button has been pressed on a community
     * or collection home page. If so, redirect to the appropriate URL.
     * 
     * @param request
     *            HTTP request
     * @param response
     *            HTTP response
     * @param handle
     *            Handle of the community/collection home page
     * 
     * @return true if a browse/search button was pressed and the user was
     *         redirected
     */
    private boolean handleButton(HttpServletRequest request,
            HttpServletResponse response, String handle) throws IOException
    {
        String button = UIUtil.getSubmitButton(request, "");
        String location = request.getParameter("location");
        String prefix = "/";
        String url = null;

        if (location == null)
        {
            return false;
        }

        /*
         * Work out the "prefix" to which to redirect If "/", scope is all of
         * DSpace, so prefix is "/" If prefix is a handle, scope is a community
         * or collection, so "/handle/1721.x/xxxx/" is the prefix.
         */
        if (!location.equals("/"))
        {
            prefix = "/handle/" + location + "/";
        }

        if (button.equals("submit_search")
                || (request.getParameter("query") != null))
        {
            /*
             * Have to check for search button and query - in some browsers,
             * typing a query into the box and hitting return doesn't produce a
             * submit button parameter. Redirect to appropriate search page
             */
            url = request.getContextPath()
                    + prefix
                    + "simple-search?query="
                    + URLEncoder.encode(request.getParameter("query"),
                            Constants.DEFAULT_ENCODING);
        }

        // If a button was pressed, redirect to appropriate page
        if (url != null)
        {
            response.sendRedirect(response.encodeRedirectURL(url));

            return true;
        }

        return false;
    }

    /**
     * Utility method to produce a list of parent communities for a given
     * community, ending with the passed community, if include is true. If
     * commmunity is top-level, the array will be empty, or contain only the
     * passed community, if include is true. The array is ordered highest level
     * to lowest
     */
    private Community[] getParents(Community c, boolean include)
            throws SQLException
    {
        // Find all the "parent" communities for the community
        Community[] parents = c.getAllParents();

        // put into an array in reverse order
        int revLength = include ? (parents.length + 1) : parents.length;
        Community[] reversedParents = new Community[revLength];
        int index = parents.length - 1;

        for (int i = 0; i < parents.length; i++)
        {
            reversedParents[i] = parents[index - i];
        }

        if (include)
        {
            reversedParents[revLength - 1] = c;
        }

        return reversedParents;
    }
	
	
    
}
