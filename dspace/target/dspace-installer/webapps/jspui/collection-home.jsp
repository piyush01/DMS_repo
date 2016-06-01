
<%--
  - Collection home JSP
  -
  - Attributes required:
  -    collection  - Collection to render home page for
  -    community   - Community this collection is in
  -    last.submitted.titles - String[], titles of recent submissions
  -    last.submitted.urls   - String[], corresponding URLs
  -    logged.in  - Boolean, true if a user is logged in
  -    subscribed - Boolean, true if user is subscribed to this collection
  -    admin_button - Boolean, show admin 'edit' button
  -    editor_button - Boolean, show collection editor (edit submitters, item mapping) buttons
  -    show.items - Boolean, show item list
  -    browse.info - BrowseInfo, item list
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.components.RecentSubmissions" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page import="org.dspace.browse.ItemCounter"%>
<%@ page import="org.dspace.content.*"%>
<%@ page import="org.dspace.core.ConfigurationManager"%>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.eperson.Group"     %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="java.net.URLEncoder" %>

<%
    // Retrieve attributes
    Collection collection = (Collection) request.getAttribute("collection");
    Community  community  = (Community) request.getAttribute("community");
    Group      submitters = (Group) request.getAttribute("submitters");

    RecentSubmissions rs = (RecentSubmissions) request.getAttribute("recently.submitted");
    
    boolean loggedIn =
        ((Boolean) request.getAttribute("logged.in")).booleanValue();
    boolean subscribed =
        ((Boolean) request.getAttribute("subscribed")).booleanValue();
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());

    Boolean editor_b      = (Boolean)request.getAttribute("editor_button");
    boolean editor_button = (editor_b == null ? false : editor_b.booleanValue());

    Boolean submit_b      = (Boolean)request.getAttribute("can_submit_button");
    boolean submit_button = (submit_b == null ? false : submit_b.booleanValue());

	// get the browse indices
    BrowseIndex[] bis = BrowseIndex.getBrowseIndices();

    // Put the metadata values into guaranteed non-null variables
    String name = collection.getMetadata("name");
    String intro = collection.getMetadata("introductory_text");
    if (intro == null)
    {
        intro = "";
    }
    String copyright = collection.getMetadata("copyright_text");
    if (copyright == null)
    {
        copyright = "";
    }
    String sidebar = collection.getMetadata("side_bar_text");
    if(sidebar == null)
    {
        sidebar = "";
    }

    String communityName = community.getMetadata("name");
    String communityLink = "/handle/" + community.getHandle();

    Bitstream logo = collection.getLogo();
    
    boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
    String feedData = "NONE";
    if (feedEnabled)
    {
        feedData = "coll:" + ConfigurationManager.getProperty("webui.feed.formats");
    }
    
    ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));
    Boolean showItems = (Boolean)request.getAttribute("show.items");
    boolean show_items = showItems != null ? showItems.booleanValue() : false;
	
	 String path = collection.getHandle();	
	 session.removeAttribute( "handleid" );
	session.setAttribute("handleid",path);
	String message=(String)request.getParameter("message");
%>
<style type="text/css">
    .bs-example{
    	margin: 20px;
    }
    .icon-input-btn{
        display: inline-block;
        position: relative;
    }
    .icon-input-btn input[type="submit"]{
        padding-left: 2em;
    }
    .icon-input-btn .glyphicon{
        display: inline-block;
        position: absolute;
        left: 0.65em;
        top: 30%;
    }
</style>
<script type="text/javascript">
$(document).ready(function(){
	$(".icon-input-btn").each(function(){
        var btnFont = $(this).find(".btn").css("font-size");
        var btnColor = $(this).find(".btn").css("color");
		$(this).find(".glyphicon").css("font-size", btnFont);
        $(this).find(".glyphicon").css("color", btnColor);
        if($(this).find(".btn-xs").length){
            $(this).find(".glyphicon").css("top", "24%");
        }
	}); 
});
</script>
<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>
<dspace:layout locbar="commLink" title="<%= name %>" feedData="<%= feedData %>">
		<div class="row">
            <!-- left column -->			
			<div class="col-md-12">
			
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header">
				<%
					    if(message!=null && !message.equals("")){
							%>
						<center><p class="text-success"><%=message%></p></center>
							<%
						}
					  %>
				 <h2 class="box-title"><%= name %>
						<%
									if(ConfigurationManager.getBooleanProperty("webui.strengths.show"))
									{
						%>
										: [<%= ic.getCount(collection) %>]
						<%
									}
						%>
								<small>: Sub Folder home page</small>
							
							  </h2>
							  
							  
							  
						<% if(admin_button || editor_button ) { %> 
		
		      <div class="btn-group pull-right">
			    <div class="btn-group pull-right">
                      <button type="button" class="btn btn-info">Admin Tools</button>
                      <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                      </button>
                      <ul class="dropdown-menu" role="menu" style="min-width:122px !important; border:1px solid #00c0ef !important; margin:0px !important; padding:0px;">

					  
				<% if( editor_button ) { %>
				<li>
				<form method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
					 <input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
					 <input type="hidden" name="community_id" value="<%= community.getID() %>" />
					  <input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_EDIT_COLLECTION %>" />
					 <input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" value="Modify" />
				</form>
				</li>
				<% } %>
				<% if(submitters != null) { %>
						<li>
					   <form method="get" action="<%=request.getContextPath()%>/tools/group-edit">
								<input type="hidden" name="group_id" value="<%=submitters.getID()%>" />
								<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit_edit" value="<fmt:message key="jsp.collection-home.editsub.button"/>" />
					</form>
					 </li>
				<% } %>

			<% if( admin_button ) { %>				

			<% if( editor_button || admin_button) { %>
						  <li>
						  <form method="post" action="<%=request.getContextPath()%>/mydspace">
							  <input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
							  <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_EXPORT_ARCHIVE %>" />
							  <input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" value="<fmt:message key="jsp.mydspace.request.export.collection"/>" />
							</form>
							</li>
						<li>							
						   <form method="post" action="<%=request.getContextPath()%>/dspace-admin/metadataexport">
							 <input type="hidden" name="handle" value="<%= collection.getHandle() %>" />
							 <input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" value="<fmt:message key="jsp.general.metadataexport.button"/>" />
						   </form>
						   </li>
						   
			<% } %>
							 
			<% } %>
			<%  if (submit_button)
				{ %>
					 <li><form  action="<%= request.getContextPath() %>/submit" method="post">
						<br/> <br/> <input type="hidden" name="collection" value="<%= collection.getID() %>" />
						<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit" value="Add Document" />
					  </form></li>
			<%  } %> 
			    </ul>
				</div>					
				</div>				
				<% } %> 	  
							  
							  
							  
							  
							  
				</div><!-- /.box-header -->
				<div class="box-body">
					<div class="col-sm-3">
						<%  if (logo != null) { %>
								
									<img class="img-responsive pull-right" alt="Logo" src="<%= request.getContextPath() %>/retrieve/<%= logo.getID() %>" />
								
						<% 	} %>
					</div>		
                 </div>
                
			</div>
		</div>
		
	</div>	
				
				
			           	 
	<% if (show_items)
   {
        BrowseInfo bi = (BrowseInfo) request.getAttribute("browse.info");
        BrowseIndex bix = bi.getBrowseIndex();

        // prepare the next and previous links
        String linkBase = request.getContextPath() + "/handle/" + collection.getHandle();
        
        String next = linkBase;
        String prev = linkBase;
        
        if (bi.hasNextPage())
        {
            next = next + "?offset=" + bi.getNextOffset();
        }
        
        if (bi.hasPrevPage())
        {
            prev = prev + "?offset=" + bi.getPrevOffset();
        }

        String bi_name_key = "browse.menu." + bi.getSortOption().getName();
        String so_name_key = "browse.order." + (bi.isAscending() ? "asc" : "desc");
%>
   
	<div class="row">
            <!-- left column -->        
			<div class="col-md-12">		
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
				  <h3 class="box-title">Document List</h3>						
                 </div>             
					 <div class="box-body"> 
					<%-- output the results using the browselist tag --%>
						 <div class="form-group">
							<dspace:browselist collection_id="<%= collection.getID() %>" browseInfo="<%= bi %>" emphcolumn="<%= bix.getSortOption().getMetadata() %>" />
						 </div>
					 </div>					
			</div>
		</div>
	</div> 
<%
   } 
%>
	

</div>
</dspace:layout>
