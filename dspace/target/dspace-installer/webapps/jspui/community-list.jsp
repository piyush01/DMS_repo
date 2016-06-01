

<%--
  - Display hierarchical list of communities and collections
  -
  - Attributes to be passed in:
  -    communities         - array of communities
  -    collections.map  - Map where a keys is a community IDs (Integers) and 
  -                      the value is the array of collections in that community
  -    subcommunities.map  - Map where a keys is a community IDs (Integers) and 
  -                      the value is the array of subcommunities in that community
  -    admin_button - Boolean, show admin 'Create Top-Level Community' button
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
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Map" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    Community[] communities = (Community[]) request.getAttribute("communities");
    Map collectionMap = (Map) request.getAttribute("collections.map");
    Map subcommunityMap = (Map) request.getAttribute("subcommunities.map");
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());
    ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));
%>

		<%!
			void showCommunity(Community c, JspWriter out, HttpServletRequest request, ItemCounter ic,
					Map collectionMap, Map subcommunityMap) throws ItemCountException, IOException, SQLException
			{
				boolean showLogos = ConfigurationManager.getBooleanProperty("jspui.community-list.logos", true);
				out.println( "<li>" );
				Bitstream logo = c.getLogo();
				if (showLogos && logo != null)
				{
					out.println("<a class=\"pull-left col-md-2\" href=\"" + request.getContextPath() + "/handle/" 
						+ c.getHandle() + "\"><img class=\"media-object img-responsive\" src=\"" + 
						request.getContextPath() + "/retrieve/" + logo.getID() + "\" alt=\"community logo\"></a>");
				}
				out.println( "<h3><a href=\"" + request.getContextPath() + "/handle/" 
					+ c.getHandle() + "\">" + c.getMetadata("name") + "</a>");
				if(ConfigurationManager.getBooleanProperty("webui.strengths.show"))
				{
					out.println(" <span class=\"badge\">" + ic.getCount(c) + "</span>");
				}
				out.println("</h3>");
				if (StringUtils.isNotBlank(c.getMetadata("short_description")))
				{
					out.println(c.getMetadata("short_description"));
				}
				//out.println("<br>");
				// Get the collections in this community
				Collection[] cols = (Collection[]) collectionMap.get(c.getID());
				if (cols != null && cols.length > 0)
				{
					out.println("<ul>");	
					for (int j = 0; j < cols.length; j++)
					{
						
						out.println("<li>");
						
						Bitstream logoCol = cols[j].getLogo();
						if (showLogos && logoCol != null)
						{
							out.println("<a class=\"pull-left col-md-2\" href=\"" + request.getContextPath() + "/handle/" 
								+ cols[j].getHandle() + "\"><img class=\"media-object img-responsive\" src=\"" + 
								request.getContextPath() + "/retrieve/" + logoCol.getID() + "\" alt=\"collection logo\"></a>");
						}
						out.println("<h4 ><a href=\"" + request.getContextPath() + "/handle/" + cols[j].getHandle() + "\">" + cols[j].getMetadata("name") +"</a>");
						if(ConfigurationManager.getBooleanProperty("webui.strengths.show"))
						{
							out.println(" [" + ic.getCount(cols[j]) + "]");
						}
						out.println("</h4>");
						if (StringUtils.isNotBlank(cols[j].getMetadata("short_description")))
						{
							out.println(cols[j].getMetadata("short_description"));
						}
						//out.println("</div>");
						out.println("</li>");
						
					}
					out.println("</ul>");
					
				}
					
					// Get the sub-communities in this community
					Community[] comms = (Community[]) subcommunityMap.get(c.getID());
					if (comms != null && comms.length > 0)
					{
						out.println("<ul>");
						for (int k = 0; k < comms.length; k++)
						{
						   showCommunity(comms[k], out, request, ic, collectionMap, subcommunityMap);
						}
						out.println("</ul>"); 
					}
				
				//out.println("</div>");
				out.println("</li>");
				
			}
		%>

<dspace:layout titlekey="jsp.community-list.title">
		<div class="row">
			<div class="col-md-12">
			 <!-- general form elements -->
					  <div class="box box-solid">
						<div class="box-header with-border">
							<h3 class="box-title"><fmt:message key="jsp.community-list.title"/></h3>
							<p><fmt:message key="jsp.community-list.text1"/></p>
						</div>
						<div class="box-body">
							
							<% if (communities.length != 0)
							{
							%>
								<ul>
							<%
									for (int i = 0; i < communities.length; i++)
									{
										showCommunity(communities[i], out, request, ic, collectionMap, subcommunityMap);
									}
							%>
								</ul>
							 
							<% }
							%>
						</div>
					</div>
			</div>
		</div>
</div>
</dspace:layout>
