
<%--
  - Renders a whole HTML page for displaying item metadata.  Simply includes
  - the relevant item display component in a standard HTML page.
  -
  - Attributes:
  -    display.all - Boolean - if true, display full metadata record
  -    item        - the Item to display
  -    collections - Array of Collections this item appears in.  This must be
  -                  passed in for two reasons: 1) item.getCollections() could
  -                  fail, and we're already committed to JSP display, and
  -                  2) the item might be in the process of being submitted and
  -                  a mapping between the item and collection might not
  -                  appear yet.  If this is omitted, the item display won't
  -                  display any collections.
  -    admin_button - Boolean, show admin 'edit' button
  --%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.Metadatum" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.handle.HandleManager" %>
<%@ page import="org.dspace.license.CreativeCommons" %>
<%@page import="javax.servlet.jsp.jstl.fmt.LocaleSupport"%>
<%@page import="org.dspace.versioning.Version"%>
<%@page import="org.dspace.core.Context"%>
<%@page import="org.dspace.app.webui.util.VersionUtil"%>
<%@page import="org.dspace.app.webui.util.UIUtil"%>
<%@page import="org.dspace.authorize.AuthorizeManager"%>
<%@page import="java.util.List"%>
<%@page import="org.dspace.core.Constants"%>
<%@page import="org.dspace.eperson.EPerson"%>
<%@page import="org.dspace.versioning.VersionHistory"%>
<%@page language="java" session="true" %>
<%
    // Attributes
    Boolean displayAllBoolean = (Boolean) request.getAttribute("display.all");
    boolean displayAll = (displayAllBoolean != null && displayAllBoolean.booleanValue());
    Boolean suggest = (Boolean)request.getAttribute("suggest.enable");
    boolean suggestLink = (suggest == null ? false : suggest.booleanValue());
    Item item = (Item) request.getAttribute("item");
    Collection[] collections = (Collection[]) request.getAttribute("collections");
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());
    
    // get the workspace id if one has been passed
    Integer workspace_id = (Integer) request.getAttribute("workspace_id");

    // get the handle if the item has one yet
    String handle = item.getHandle();
   /*  String parentpath=request.getAttribute("path");
    // CC URL & RDF */
    String cc_url = CreativeCommons.getLicenseURL(item);
    String cc_rdf = CreativeCommons.getLicenseRDF(item);

    // Full title needs to be put into a string to use as tag argument
    String title = "";
    if (handle == null)
 	{
		title = "Workspace Item";
	}
	else 
	{
		Metadatum[] titleValue = item.getDC("title", null, Item.ANY);
		if (titleValue.length != 0)
		{
			title ="Document:"+ titleValue[0].value;
		}
		else
		{
			title = "Document";
		}
	}
    
    Boolean versioningEnabledBool = (Boolean)request.getAttribute("versioning.enabled");
    boolean versioningEnabled = (versioningEnabledBool!=null && versioningEnabledBool.booleanValue());
    Boolean hasVersionButtonBool = (Boolean)request.getAttribute("versioning.hasversionbutton");
    Boolean hasVersionHistoryBool = (Boolean)request.getAttribute("versioning.hasversionhistory");
    boolean hasVersionButton = (hasVersionButtonBool!=null && hasVersionButtonBool.booleanValue());
    boolean hasVersionHistory = (hasVersionHistoryBool!=null && hasVersionHistoryBool.booleanValue());   
    Boolean newversionavailableBool = (Boolean)request.getAttribute("versioning.newversionavailable");
    boolean newVersionAvailable = (newversionavailableBool!=null && newversionavailableBool.booleanValue());
    Boolean showVersionWorkflowAvailableBool = (Boolean)request.getAttribute("versioning.showversionwfavailable");
    boolean showVersionWorkflowAvailable = (showVersionWorkflowAvailableBool!=null && showVersionWorkflowAvailableBool.booleanValue());
    
    String latestVersionHandle = (String)request.getAttribute("versioning.latestversionhandle");
    String latestVersionURL = (String)request.getAttribute("versioning.latestversionurl");
    
    VersionHistory history = (VersionHistory)request.getAttribute("versioning.history");
    List<Version> historyVersions = (List<Version>)request.getAttribute("versioning.historyversions");
	session.setAttribute("handleid", handle);
	session.setAttribute("version", "yes");
	String message=(String)request.getParameter("message");
	
%>

<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
        google.load("jquery", "1.4");
        google.setOnLoadCallback(function() {
			$( document ).ready(function() {
			
			})
            // Place init code here instead of $(document).ready()
        });
		
		function pageReload()
		{
		 location.reload();
		}
</script> 

<dspace:layout title="<%= title %>">	
			
<%
        if (admin_button)  // admin edit button
        { %>
		
     
			  <div class="row">
				<div class="col-lg-12 ">
				  <div class="box box-primary col-md-4">
					<div class="box-header">										
					  <%
					    if(message!=null && !message.equals("")){
							%>
							<p class="text-success"><%=message%></p>
							<%
						}
					  %>
					 <%
							if (handle != null)
							{
						%>

								<%		
								if (newVersionAvailable)
								   {
								%>
								<h3 class="box-title text-warning"><b><fmt:message key="jsp.version.notice.new_version_head"/></b>		
								Notice	This is not the latest version of this document. </a>
								</h3>
								<%
									}
								%>
								
								<%		
								if (showVersionWorkflowAvailable)
								   {
								%>
								<h3 class="box-title text-warning"><b><fmt:message key="jsp.version.notice.workflow_version_head"/></b>	
								<fmt:message key="jsp.version.notice.workflow_version_help"/>
								</h3>
								<%
									}
								%>					 
					<div class="btn-group pull-right">
			       <div class="btn-group pull-right">
                      <button type="button" class="btn btn-info">Admin Tools</button>
                      <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                      </button>
                        <ul class="dropdown-menu" role="menu" style="min-width:122px !important; border:1px solid #00c0ef !important; margin:0px !important; padding:0px;">					  
					 <li>
					<form method="get" action="<%= request.getContextPath() %>/tools/edit-item">
						<input type="hidden" name="item_id" value="<%= item.getID() %>" />
						<%--<input type="submit" name="submit" value="Edit...">--%>
						<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit" value="Modify" />
					</form>
					</li>
					<li>
					<form method="post" action="<%= request.getContextPath() %>/mydspace">
						<input type="hidden" name="item_id" value="<%= item.getID() %>" />
						<input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_EXPORT_ARCHIVE %>" />
						<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit" value="<fmt:message key="jsp.mydspace.request.export.item"/>" />
					</form>   
					</li>					
					<li>
					<form method="post" action="<%= request.getContextPath() %>/dspace-admin/metadataexport">
						<input type="hidden" name="handle" value="<%= item.getHandle() %>" />
						<input class="btn btn-default col-md-12" style="text-align:left !important;" type="submit" name="submit" value="<fmt:message key="jsp.general.metadataexport.button"/>" />
					</form>  
					</li>
									
					<% if(true) { %>   
					<li>						
                	<form method="get" action="<%= request.getContextPath() %>/tools/version">
                    	<input type="hidden" name="itemID" value="<%= item.getID() %>" />                
                    	<input class="btn btn-default col-md-12"  style="text-align:left !important;" type="submit" name="submit" value="Check In" />
                	</form>
					</li>
                	<% } %>                 	
					
					</ul>					
            </div>
	</div>
	</div>					
	</div>
	</div>	
	</div>	
    
<%      } %>



<%
    }

    String displayStyle = (displayAll ? "full" : "");
%>
	
			  <div class="row">
				<div class="col-xs-12">
				  <div class="box box-primary">
					<div class="box-header">
					  <h3 class="box-title"><fmt:message key="org.dspace.app.webui.jsptag.ItemTag.files"/></h3>
					</div><!-- /.box-header 
					 <div class="box-header">
					<form method="post" action="<%=request.getContextPath()%>/workflow-process">
					<input type="hidden" name="item_id" value="<%=item.getID()%>" />
					<input type="hidden" name="handle" value="<%=item.getHandle()%>" />
					<input type="hidden" name="status" value="bitstream" />
					<input class="btn btn-primary" type="submit" name="submit" value="Move To Workflowmode"/>
					</form>
					</div>-->
					<div class="box-body">
					<dspace:item item="<%= item %>" collections="<%= collections %>" style="<%= displayStyle %>" />
					 </div>
					</div>
					</div>
					</div>
					
			  <div class="row">
				<div class="col-xs-12">
				  <div class="box box-primary">
					<div class="box-header">
					  <h3 class="box-title"></h3>
					  </div>
			<div class="container row">
			<%
				String locationLink = request.getContextPath() + "/handle/" + handle;

				if (displayAll)
				{
			%>
			<%
					if (workspace_id != null)
					{
			%>
				<form class="col-md-2" method="post" action="<%= request.getContextPath() %>/view-workspaceitem">
					<input type="hidden" name="workspace_id" value="<%= workspace_id.intValue() %>" />
					<input class="btn btn-default" type="submit" name="submit_simple" value="<fmt:message key="jsp.display-item.text1"/>" />
				</form>
			<%
					}
					else
					{
			%>
				<a class="btn btn-default" href="<%=locationLink %>?mode=simple">
					<fmt:message key="jsp.display-item.text1"/>
				</a>
			<%
					}
			%>
			<%
				}
				else
				{
			%>
			<%
					if (workspace_id != null)
					{
			%>
				<form class="col-md-2" method="post" action="<%= request.getContextPath() %>/view-workspaceitem">
					<input type="hidden" name="workspace_id" value="<%= workspace_id.intValue() %>" />				
				</form>
			<%
					}
					else
					{
			%>
			   <!-- <a class="btn btn-default" href="<%=locationLink %>?mode=full">
					<fmt:message key="jsp.display-item.text2"/>Show Full Item Record
				</a>-->
			<%
					}
				}

				if (workspace_id != null)
				{
			%>
			   <form class="col-md-2" method="post" action="<%= request.getContextPath() %>/workspace">
					<input type="hidden" name="workspace_id" value="<%= workspace_id.intValue() %>"/>
					<input class="btn btn-primary" type="submit" name="submit_open" value="<fmt:message key="jsp.display-item.back_to_workspace"/>"/>
				</form>
			<%
				} else {

					if (suggestLink)
					{
			%>
				<a class="btn btn-success" href="<%= request.getContextPath() %>/suggest?handle=<%= handle %>" target="new_window">
				   <fmt:message key="jsp.display-item.suggest"/></a>
			<%
					}
			%>
			  <!--  
			  <a class="statisticsLink  btn btn-primary" href="<%= request.getContextPath() %>/handle/<%= handle %>/statistics"><fmt:message key="jsp.display-item.display-statistics"/></a>
			-->
				<%-- SFX Link --%>
			<%
				if (ConfigurationManager.getProperty("sfx.server.url") != null)
				{
					String sfximage = ConfigurationManager.getProperty("sfx.server.image_url");
					if (sfximage == null)
					{
						sfximage = request.getContextPath() + "/image/sfx-link.gif";
					}
			%>
				 <a class="btn btn-default" href="<dspace:sfxlink item="<%= item %>"/>" />
				 <img src="<%= sfximage %>" border="0" alt="SFX Query" /></a>
			<%
				}
				}
			%>
			</div>
			
				<%-- Versioning table --%>
			<%
				if (versioningEnabled && hasVersionHistory)
				{
					boolean item_history_view_admin = ConfigurationManager
							.getBooleanProperty("versioning", "item.history.view.admin");
					if(!item_history_view_admin || admin_button) {         
			%>
	
	 <div class="box-body">
	<fmt:message key="jsp.version.history.head2" />		
	 <table id="example2" class="table table-bordered table-hover">
		<thead>
		<tr>
			<!--<th id="tt1" class="oddRowEvenCol"><fmt:message key="jsp.version.history.column1"/></th>-->
			 			
				<th id="tt2" ><fmt:message key="jsp.version.history.column1"/></th>
			 
				<th id="tt3"><fmt:message key="jsp.version.history.column3"/></th>
			 
				
		    <th id="tt4" ><fmt:message key="jsp.version.history.column4"/></th>
			<!--<th 
				 id="tt5" class="oddRowEvenCol"><fmt:message key="jsp.version.history.column5"/> </th>-->
		</tr>
		</thead>
		<tbody>
		<% for(Version versRow : historyVersions) 
		{  		
			EPerson versRowPerson = versRow.getEperson();
			String[] identifierPath = VersionUtil.addItemIdentifier(item, versRow);
		%>	
		
		<tr>	
			<!--
			<td headers="tt1" class="oddRowEvenCol"><%= versRow.getVersionNumber() %></td>
			<a href="<%= request.getContextPath() + identifierPath[0] %>"><%= identifierPath[1] %>
			</a>
			-->
			<td headers="tt2" >
			<a href="<%= request.getContextPath() + identifierPath[0] %>"><%= versRow.getVersionNumber() %></a>
			<%= item.getID()==versRow.getItemID()?"<span class=\"glyphicon glyphicon-asterisk\"></span>":""%>
			</td>
			<td headers="tt3" ><% if(admin_button) { %><a
				href="mailto:<%= versRowPerson.getEmail() %>"><%=versRowPerson.getFullName() %></a><% } else { %><%=versRowPerson.getFullName() %><% } %></td>
			<td headers="tt4" ><%= versRow.getVersionDate() %></td>
			<!--<td headers="tt5" class="oddRowEvenCol"><%= versRow.getSummary() %></td>-->
		</tr>
		<% } %>
	
		</tbody>
	</table>
	
	<div class="panel-footer"><fmt:message key="jsp.version.history.legend"/></div>
	 </div>
	 
		<%
				}
			}
		%>
		
			<%-- Create Commons Link --%>
		<%
			if (cc_url != null)
			{
		%>
		  <p class="submitFormHelp alert alert-info"><fmt:message key="jsp.display-item.text3"/> <a href="<%= cc_url %>"><fmt:message key="jsp.display-item.license"/></a>
			<a href="<%= cc_url %>"><img src="<%= request.getContextPath() %>/image/cc-somerights.gif" border="0" alt="Creative Commons" style="margin-top: -5px;" class="pull-right"/></a>
			</p>
			<!--
			<%= cc_rdf %>
			-->
		<%
			} else {
		%>
			<!-- <p class="submitFormHelp alert alert-info"><fmt:message key="jsp.display-item.copyright"/></p> -->
		<%
			} 
		%>   
		</div>
		</div>
		</div> 
		</div>
		</dspace:layout>
 