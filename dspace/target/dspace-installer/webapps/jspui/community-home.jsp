
<%--
  - Community home JSP
  -
  - Attributes required:
  -    community             - Community to render home page for
  -    collections           - array of Collections in this community
  -    subcommunities        - array of Sub-communities in this community
  -    last.submitted.titles - String[] of titles of recently submitted items
  -    last.submitted.urls   - String[] of URLs of recently submitted items
  -    admin_button - Boolean, show admin 'edit' button
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.components.RecentSubmissions" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.browse.ItemCounter" %>
<%@ page import="org.dspace.content.*" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%
    // Retrieve attributes
	int parentID = UIUtil.getIntParameter(request, "parent_community_id");	
    Community topcommunity[]=(Community[])request.getAttribute("topcommunity");
    Community community = (Community) request.getAttribute( "community" );
    Collection[] collections =
        (Collection[]) request.getAttribute("collections");
    Community[] subcommunities =
        (Community[]) request.getAttribute("subcommunities");
     
    RecentSubmissions rs = (RecentSubmissions) request.getAttribute("recently.submitted");
    
    Boolean editor_b = (Boolean)request.getAttribute("editor_button");
    boolean editor_button = (editor_b == null ? false : editor_b.booleanValue());
    Boolean add_b = (Boolean)request.getAttribute("add_button");
    boolean add_button = (add_b == null ? false : add_b.booleanValue());
    Boolean remove_b = (Boolean)request.getAttribute("remove_button");
    boolean remove_button = (remove_b == null ? false : remove_b.booleanValue());

	// get the browse indices
    BrowseIndex[] bis = BrowseIndex.getBrowseIndices();

    // Put the metadata values into guaranteed non-null variables
    String name = community.getMetadata("name");
    String intro = community.getMetadata("introductory_text");
    String copyright = community.getMetadata("copyright_text");
    String sidebar = community.getMetadata("side_bar_text");
    Bitstream logo = community.getLogo();
    
    boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
    String feedData = "NONE";
    if (feedEnabled)
    {
        feedData = "comm:" + ConfigurationManager.getProperty("webui.feed.formats");
    }
    
    ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));
	int topcommunityid=checkTopCommunity(community.getID(), topcommunity);
%>
<%!
int checkTopCommunity(int community_id,Community topcommunity_id[])
{
	int c_id = 0;
	for(int i=0;i<topcommunity_id.length;i++)
	{
		if(community_id==topcommunity_id[i].getID())
		{
			c_id=topcommunity_id[i].getID();
			break;
		}
	}
	return c_id;
	}

int checkSubCommunity(int community_id,Community subcommunity_id[])
{
	int c_id = 0;
	for(int i=0;i<subcommunity_id.length;i++)
	{
		if(community_id==subcommunity_id[i].getID())
		{
			c_id=subcommunity_id[i].getID();
			break;
		}
	}
	return c_id;
	}
%>
<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>
<dspace:layout locbar="commLink" title="<%= name %>" feedData="<%= feedData %>">


<div class="row">
 <div class="col-lg-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
										
						<% if(editor_button || add_button)  // edit button(s)
						{ %>

					<div class="btn-group pull-right">
                      <button type="button" class="btn btn-info">Admin Tools</button>
                      <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                      </button>
                      <ul class="dropdown-menu" role="menu" style="min-width:122px !important; border:1px solid #00c0ef !important; margin:0px !important; padding:0px;">
						
						<% if(editor_button) { 
						 %>	
                        <li>
							<form method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
							  <input type="hidden" name="community_id" value="<%= community.getID() %>" />
							  <input type="hidden" name="action" value="<%=EditCommunitiesServlet.START_EDIT_COMMUNITY%>" />
							  <%--<input type="submit" value="Edit..." />--%>
							 <input type="hidden" name="modify" value="modify" />
							 
							  <input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" value="Modify" />
							</form>
						</li>
						 <% } %>
						 <% if(add_button) {  
							 
							if(community.getID()==topcommunityid){
						 %>
						 
                        <li>
							<form method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
								<input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_CREATE_COMMUNITY%>" />
								<input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
								
								
								<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit" value="Create Folder" />
							 </form>
						</li>
						
						 <% }else {%>
                        <li>
						     <form method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
								<input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_CREATE_COMMUNITY%>" />
								<input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
								
								
								<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit" value="Create Folder" />
							 </form>
							 <form method="post" action="<%=request.getContextPath()%>/tools/collection-wizard">
								<input type="hidden" name="community_id" value="<%= community.getID() %>" />
								<input type="hidden" name="action" value="subfolder" />
								<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" value="Create Sub-folder" />
							</form>
						</li>
                        
						<%}}%> 					
                      </ul>
                    </div>
				<% } %>					
						
					
				
					<h3 class="box-title"><strong><%= name %></strong></h3>
					<div class="box-tools pull-right">
						<%  if (logo != null) { %>
						
							<div class="col-lg-1 pull-right" >
				            <img class="img-responsive" alt="Logo" src="<%= request.getContextPath() %>/retrieve/<%= logo.getID() %>" />
							 </div>
						<% } %>
					  </div>
				</div>
				<div class="box-body">
			<%
			 
			 boolean showLogos = ConfigurationManager.getBooleanProperty("jspui.community-home.logos", true);
              	if(community.getID()==topcommunityid){
					
				%>
				<p>Folder(s) within this Cabinet </p>
				<%
				if (subcommunities.length != 0)
				{
					for (int j = 0; j < subcommunities.length; j++)
						{
					%>
				
					<div class="row" >
					
					
						<div class="col-md-3 col-sm-6 col-xs-12">
						  <div class="info-box">
							<span class="info-box-icon "><i class="fa fa-folder-open-o"></i></span>
							<div class="info-box-content">
							  <a href="<%= request.getContextPath() %>/handle/<%= subcommunities[j].getHandle() %>">
								<span class="info-box-text"><%= subcommunities[j].getMetadata("name") %></span>
							  </a>	
							</div><!-- /.info-box-content -->
						  </div><!-- /.info-box -->
						</div>		
						
						<!--<% if (remove_button) { %>
									
										<form class="col-sm-2" method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
										  <input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
										  <input type="hidden" name="community_id" value="<%= subcommunities[j].getID() %>" />
										  <input type="hidden" name="action" value="<%=EditCommunitiesServlet.START_DELETE_COMMUNITY%>" />
										  <button type="submit" class="btn btn-block btn-danger">Delete </button>
										</form>									
										
									<% } %>	-->
									</div>
							
				<%}}}else{
					
					%>
					
				<% if (collections.length != 0)
					{ %>	
				<p>Sub-Folder(s) within this Folder</p>
				
				<%
					}
				 if (collections.length != 0)
					{
						for (int i = 0; i < collections.length; i++)
						{%>
					
				<div class="row" >
				
					<div class="col-md-3 col-sm-6 col-xs-12">
					  <div class="info-box">
						<span class="info-box-icon "><i class="fa fa-folder-open-o"></i></span>
						<div class="info-box-content">
						  <a href="<%= request.getContextPath() %>/handle/<%= collections[i].getHandle() %>">
							<span class="info-box-text"><%= collections[i].getMetadata("name") %></span>
						  </a>	
						</div><!-- /.info-box-content -->
					  </div><!-- /.info-box -->
					</div>
				
				
				
				
				
			
			
		<!--	<div class="col-sm-5">
			<% if (remove_button) { %>
	      <form class="btn-group" method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
	          <input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
	          <input type="hidden" name="community_id" value="<%= community.getID() %>" />
	          <input type="hidden" name="collection_id" value="<%= collections[i].getID() %>" />
	          <input type="hidden" name="action" value="<%=EditCommunitiesServlet.START_DELETE_COLLECTION%>" />
	          <button type="submit" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> &nbsp; Delete</button>			 
	      </form>
	    <% } %>	
			</div>-->
			</div>			
				<%
				}
				}}
				%>
		
				 </div>
			</div>
  </div>
  	 
 
  </div>
</div>
</div>
</dspace:layout>
