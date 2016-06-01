
<%--
  - Display hierarchical list of communities and collections
  -
  - Attributes to be passed in:
  -    communities         - array of communities
  -    collections.map  - Map where a keys is a community IDs (Integers) and 
  -                      the value is the array of collections in that community
  -    subcommunities.map  - Map where a keys is a community IDs (Integers) and 
  -                      the value is the array of subcommunities in that community
 
  --%>

<%@page import="org.dspace.content.Bitstream"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	
<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.browse.ItemCountException" %>
<%@ page import="org.dspace.browse.ItemCounter" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Map" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.util.Locale"%>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.dspace.core.I18nUtil" %>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.core.LogManager" %>
<%@ page import="org.dspace.core.PluginManager" %>
<%@ page import="org.dspace.plugin.SiteHomeProcessor" %>
<%@ page import="org.dspace.handle.HandleManager" %>
<%@ page import="org.dspace.content.DSpaceObject" %>
<%@ page import="org.dspace.core.Constants" %>
<%@ page import="org.dspace.content.Metadatum" %>
<%@ page import="org.dspace.content.ItemIterator" %>

<%
   Context context = null;
   try
   {
    // Obtain a context so that the location bar can display log in status
        context = UIUtil.obtainContext(request);
   try
        {
            SiteHomeProcessor[] chp = (SiteHomeProcessor[]) PluginManager.getPluginSequence(SiteHomeProcessor.class);
            for (int i = 0; i < chp.length; i++)
            {
                chp[i].process(context, request, response);
            }
        }
        catch (Exception e)
        {
            Logger log = Logger.getLogger("org.dspace.jsp");
            log.error("caught exception: ", e);
            throw new ServletException(e);
        }
   }
		catch (SQLException se)
    {
        
    }
	%>
<%
Locale sessionLocale = UIUtil.getSessionLocale(request);
    Config.set(request.getSession(), Config.FMT_LOCALE, sessionLocale);	
    Community[] communities = (Community[]) request.getAttribute("communities");
    Map collectionMap = (Map) request.getAttribute("collections.map");
    Map subcommunityMap = (Map) request.getAttribute("subcommunities.map");
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());  	
%>


<%!
   Integer name=0;
    void showCommunity(Community c, JspWriter out, HttpServletRequest request,Map collectionMap, Map subcommunityMap,String subcomm) throws ItemCountException, IOException, SQLException
    {
		
		 Context context = null;
		 context = UIUtil.obtainContext(request);
        Bitstream logo = c.getLogo();
       if(subcomm!=null && subcomm.equals("yes")){
        out.println( "<li><a  href=\"" + request.getContextPath() + "/handle/" 
        	+ c.getHandle() + "\"><i class=\"fa fa-folder-open\"></i>" + c.getMetadata("name")+"</a><a href=\"#\" id=\"leftarrow2\"><i class=\"fa fa-angle-left pull-right\"></i></a>");	
	   }else{
		    out.println( "<li><a  href=\"" + request.getContextPath() + "/handle/" 
        	+ c.getHandle() + "\"><i class=\"fa fa-database\"></i>" + c.getMetadata("name")+"</a><a href=\"#\" id=\"leftarrow2\"><i class=\"fa fa-angle-left pull-right\"></i></a>");
	   }
        // Get the collections in this community
		Collection[] cols=null;	
         cols = (Collection[]) collectionMap.get(c.getID());
		
		
        if (cols != null && cols.length > 0)
        {
			Collection myCollection = null;
            out.println("<ul class=\"treeview-menu\">");
            for (int j = 0; j < cols.length; j++)
            {
				if(cols[j].getMetadata("name")!=null && !cols[j].getMetadata("name").equals("")){
                out.println("<li>");
                out.println("<a id=\"myAnchor\" href=\"" + request.getContextPath() + "/handle/" + cols[j].getHandle() + "\"><i class=\"fa fa-list\"></i>" + cols[j].getMetadata("name")+"</a>");
				out.println("</li>");
				
				}
				}
            out.println("</ul>");
        }
		 // Get the sub-communities in this community
			Community[] comms = (Community[]) subcommunityMap.get(c.getID());
			 out.println("<ul class=\"treeview-menu\">");
			if (comms != null && comms.length > 0)
			{
			   
				for (int k = 0; k < comms.length; k++)
				{
				   showCommunity(comms[k], out, request,collectionMap, subcommunityMap,"yes");
				}
			   
			}	
              out.println("</ul>");	
	  out.println("</li>");   
	  
       
    }
%>
	
	<%
if (communities!=null && communities.length !=0)
{
        for (int i = 0; i < communities.length; i++)
        {
            showCommunity(communities[i], out, request,collectionMap, subcommunityMap,"no");
        }
}
%>
   

