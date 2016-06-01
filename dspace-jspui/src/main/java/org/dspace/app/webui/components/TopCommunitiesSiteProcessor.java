/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.components;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.AdvancedSearchServlet;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.content.Metadatum;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.Utils;
import org.dspace.plugin.PluginException;
import org.dspace.plugin.SiteHomeProcessor;

/**
 * This class add top communities object to the request attributes to use in
 * the site home page implementing the SiteHomeProcessor.
 * 
 * @author Andrea Bollini
 * 
 */
public class TopCommunitiesSiteProcessor implements SiteHomeProcessor
{
	 private static Logger log = Logger.getLogger(TopCommunitiesSiteProcessor.class);
	 
	 // This will map community IDs to arrays of collections
	    private Map<Integer, Collection[]> colMap;

	    // This will map communityIDs to arrays of sub-communities
	    private Map<Integer, Community[]> commMap;
	    private static final Object staticLock = new Object();
    /**
     * blank constructor - does nothing.
     * 
     */
    public TopCommunitiesSiteProcessor()
    {

    }

    @Override
    public void process(Context context, HttpServletRequest request,
            HttpServletResponse response) throws PluginException,
            AuthorizeException
    {
    	log.info("i ma here call home page");
        // Get the top communities to shows in the community list
        Community[] communities;
        
        try
        {
           // communities = Community.findAllTop(context);
            synchronized (staticLock) 
            {
             colMap = new HashMap<Integer, Collection[]>();
             commMap = new HashMap<Integer, Community[]>();
             log.info(LogManager.getHeader(context, "view_community_list", ""));

            communities = Community.findAllTop(context);
             for (int com = 0; com < communities.length; com++) 
             {
                 build(communities[com]);
             }
             // can they admin communities?
             if (AuthorizeManager.isAdmin(context)) 
             {
                // set a variable to create an edit button
                request.setAttribute("admin_button", Boolean.TRUE);
             }
             
             Map subcommunityMap = (Map)colMap;
             
             if (communities!=null && communities.length != 0)
             {
                     for (int i = 0; i < communities.length; i++)
                     {
                    	 showItem(communities[i],subcommunityMap,context);
                     }
             }
             
             
             request.setAttribute("communities", communities);
             request.setAttribute("collections.map", colMap);
             request.setAttribute("subcommunities.map", commMap);
            // JSPManager.showJSP(request, response, "/community-list.jsp");
            }
        }
        catch (SQLException e)
        {
            throw new PluginException(e.getMessage(), e);
        }
        request.setAttribute("communities", communities);
    }
    
    
    void showItem(Community c, Map collectionMap,Context context) throws SQLException
    {
    	Collection myCollection = null;
    	int colid=0;
    	Item item=null;
        // Get the collections in this community
        Collection[] cols = (Collection[]) collectionMap.get(c.getID());
        if (cols != null && cols.length > 0)
        {
           for (int j = 0; j < cols.length; j++)
            {
        	  myCollection = Collection.find(context, cols[j].getID());
        	  myCollection.getItems();
        	  ItemIterator i = myCollection.getItems();
       		try
               {
                   // iterate through the items in this collection, and count how many
                   // are native, and how many are imports, and which collections they
                   // came from
                   while (i.hasNext())
                   {
                       Item myItem = i.next();
                       Metadatum[] metadataArray;                      
                       metadataArray = myItem.getMetadata("dc", "title", Item.ANY, Item.ANY);                     
                       if (metadataArray.length > 0)
                       {
                       }
                     
                   }
               }
               finally
               {
                   if (i != null)
                   {
                       i.close();
                   }
               }
			}
            }
           
      }       
    
    /*
     * Get all subcommunities and collections from a community
     */
    private void build(Community c) throws SQLException {

        Integer comID = Integer.valueOf(c.getID());

        // Find collections in community
        Collection[] colls = c.getCollections();
        colMap.put(comID, colls);

        // Find subcommunties in community
        Community[] comms = c.getSubcommunities();
        
        // Get all subcommunities for each communities if they have some
        if (comms.length > 0) 
        {
            commMap.put(comID, comms);
            
            for (int sub = 0; sub < comms.length; sub++) {
                
                build(comms[sub]);
            }
        }
    }

}
