<%--
  - Show form allowing edit of community metadata
  -
  - Attributes:
  -    community   - community to edit, if editing an existing one.  If this
  -                  is null, we are creating one.
  --%>

<%@page import="org.dspace.eperson.EPerson"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.Constants"%>
<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.eperson.Group" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/containerscroll.css" type="text/css" />
<%
 String message=(String)request.getParameter("message");
Group groups[]=(Group[])request.getAttribute("groups");
EPerson epeople[]=(EPerson[])request.getAttribute("epeople");
  Community topcommunity[]=(Community[])request.getAttribute("topcommunity");
Community[] subcommunities =(Community[]) request.getAttribute("subcommunities");
    Community community = (Community) request.getAttribute("community");
    int parentID = UIUtil.getIntParameter(request, "parent_community_id");	
    // Is the logged in user a sys admin
    Boolean admin = (Boolean)request.getAttribute("is.admin");
    boolean isAdmin = (admin == null ? false : admin.booleanValue());
 
    Boolean adminCreateGroup = (Boolean)request.getAttribute("admin_create_button");
    boolean bAdminCreateGroup = (adminCreateGroup == null ? false : adminCreateGroup.booleanValue());

    Boolean adminRemoveGroup = (Boolean)request.getAttribute("admin_remove_button");
    boolean bAdminRemoveGroup = (adminRemoveGroup == null ? false : adminRemoveGroup.booleanValue());
    
    Boolean policy = (Boolean)request.getAttribute("policy_button");
    boolean bPolicy = (policy == null ? false : policy.booleanValue());

    Boolean delete = (Boolean)request.getAttribute("delete_button");
    boolean bDelete = (delete == null ? false : delete.booleanValue());

    Boolean adminCommunity = (Boolean)request.getAttribute("admin_community");
    boolean bAdminCommunity = (adminCommunity == null ? false : adminCommunity.booleanValue());
    String name = "";
    String shortDesc = "";
    String intro = "";
    String copy = "";
    String side = "";
    Group admins = null;

    Bitstream logo = null;
    int resourceType      = 4;
    int resourceRelevance = 1 << resourceType;  
	
    if (community != null)
    {
        name = community.getMetadata("name");
        shortDesc = community.getMetadata("short_description");
        intro = community.getMetadata("introductory_text");
        copy = community.getMetadata("copyright_text");
        side = community.getMetadata("side_bar_text");
        logo = community.getLogo();
        admins = community.getAdministrators();
    }
	 
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
  <dspace:layout locbar="commLink" title="Edit"  nocache="true">		
  sdfssdfsdfsdf
 <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">			
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
					
						<%
							if (community == null)
							{
						%>
							 <% if( parentID!=-1){
								 %>
							<h3 class="box-title"><strong>Create Folder	 </strong>
							<% } else {%>
							<h3 class="box-title"><strong><fmt:message key="jsp.tools.edit-community.heading1"/></strong>
							<%}%>						
							</h3>
						<%
							}
							else
							{
						%>
							<h3 class="box-title">							
							
							<% if( parentID!=-1){
								 %>
							<h3 class="box-title">Edit Folder <%= community.getHandle() %>
							<% } else {%>
							<fmt:message key="jsp.tools.edit-community.heading2">
								
								</fmt:message>
							<%}%>							
							</h3>
									
							<!--<% if(bDelete) { %>
							<div class="box-tools pull-right">
									  <form method="post" action="">
										<input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_DELETE_COMMUNITY %>" />
										<input type="hidden" name="community_id" value="<%= community.getID() %>" />
										
										
										 <% if( parentID!=0 && parentID>0){
								 %>
							     <input class="btn btn-danger" type="submit" name="submit_delete" value="Folder this delete" /> 
									<% } else {%>
									<input class="btn btn-danger" type="submit" name="submit_delete" value="<fmt:message key="jsp.tools.edit-community.button.delete"/>" />
									<%}%>&nbsp;&nbsp;
										<button style="padding-top:0px;" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
									    </form>
							 </div>
							<% } %>-->
						<%
							}
						%>
						
						 <% if(message!=null && !message.equals("") && message.equals("Y"))
									 {
										 %>
										  <div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Cabinet has been successfully created!</div>
										 <%
									 }
									 %>
				</div>
  
  
					<div class="box-body">			
						<form class="form-horizontal" method="post" action="<%= request.getContextPath() %>/tools/edit-communities"> 						
						<div class="col-md-<%= community != null?"8":"12" %>">
	<c:choose>
    <c:when test="${default_modify=='modify'}">
						<div class="form-group">
							<div class="col-sm-2">
								<label>	Name<span class="star">*</span></label>
							</div>
						<div class="col-md-4">
							<input class="form-control" type="text" name="name" value="<%= Utils.addEntities(name) %>"/>
						</div>		
			
						</div>
	</c:when>    
    <c:otherwise>
    <div class="form-group">
							<div class="col-sm-2">
								<label>	Name<span class="star">*</span></label>
							</div>
						<div class="col-md-4">
							<input class="form-control" type="text" name="name" value="<%= Utils.addEntities(name) %>"/>
						</div>		
			
						</div>
						<div class="form-group">
							<div class="col-sm-2">
								<label>	Select Group</label>
							</div>
						<div class="col-md-4">
							<select class="form-control" name="group" id="group_id">
								<option value="0" selected>--Select Group--</option>
								<%for (int i = 0; i <groups.length; i++) {%>
								<option value="<%=groups[i].getID()%>"><%=groups[i].getName()%></option>
					
							<%} %>    
							</select> 
						</div>
						</div>
						<div class="form-group">
				<div class="col-sm-2">
			  		<label>	Select User</label>
				</div>
			<div class="col-md-4">
				<select class="form-control" name="username" id="userid">
					<option value="0" selected>--Select User--</option>
						<%for (int i = 0; i <epeople.length; i++) {%>
					<option value="<%=epeople[i].getID()%>"><%=epeople[i].getFullName()%></option>
					
					<%} %>    
				</select> 
			</div>		
			
			</div> 
			<div class="form-group">
				<div class="col-md-2">
					<label for="exampleInputEmail1">Action</label>
				</div>
			<div class="col-md-4">
			<div class="containerscroll"size="40">
				<table>
					<tr><td>&nbsp;&nbsp;<b>--Select Action--</b></td></tr>               
						<%  
						for(int i = 0; i < Constants.actionText.length; i++ ) 
						{ 
					   if( (Constants.actionTypeRelevance[i]&resourceRelevance) > 0)
                                    { 
								if(!Constants.actionText[i].equals("REMOVE")){ 
								%>
						
						<tr>
						<td>  &nbsp;&nbsp;
						<input  type="checkbox" name="actionname" value="<%= i %>"/> &nbsp;<%=Constants.actionText[i]%> 
						<td>
						</tr>
								<% }}} %>
				</table>			
			</div>
			</div>
			</div>
			 </c:otherwise>
</c:choose>
						
									<input class="form-control" type="hidden" name="introductory_text" value=""/>
									<input class="form-control" type="hidden" name="copyright_text" value=""/>
									 <input class="form-control" type="hidden" name="side_bar_text" value=""/>
									 
					<div class="form-group"> 
					<div class="col-sm-2"></div>
						<div class="col-md-4">					
										<%if (community == null)
											{
										%>
											<input type="hidden" name="parent_community_id" value="<%= parentID %>" />
												<input type="hidden" name="create" value="true" />
												<input class="btn btn-primary" type="submit" name="submit" value="<fmt:message key="jsp.tools.edit-community.form.button.create"/>" />
												
												<input type="hidden" name="parent_community_id" value="<%= parentID %>" />
												<input type="hidden" name="action" value="<%= EditCommunitiesServlet.CONFIRM_EDIT_COMMUNITY %>" />
												<input class="btn btn-warning" type="submit" name="submit_cancel" value="<fmt:message key="jsp.tools.edit-community.form.button.cancel"/>" />
						<%}else{%>
												<input type="hidden" name="community_id" value="<%= community.getID() %>" />
												<input type="hidden" name="create" value="false" />
												<input class="btn btn-primary" type="submit" name="submit" value="<fmt:message key="jsp.tools.edit-community.form.button.update"/>" />

												<input type="hidden" name="community_id" value="<%= community.getID() %>" />
												<input type="hidden" name="action" value="<%= EditCommunitiesServlet.CONFIRM_EDIT_COMMUNITY %>" />
												<input class="btn btn-warning" type="submit" name="submit_cancel" value="<fmt:message key="jsp.tools.edit-community.form.button.cancel"/>" />
						<%}%>
					</div>
					</div>
							 
						 </div>
						 <% if (community != null) { %>
						 <div class="col-md-4">
							<div class="panel panel-default">
								<div class="panel-heading">
								
								 <% 
								 int topcommunityid=checkTopCommunity(community.getID(), topcommunity);
								 if( community.getID()==topcommunityid)
								 {
								 %>
							      <fmt:message key="jsp.tools.edit-community.form.community-settings" /> 
									<% } else {%>
									Folder settings	
									<%}%>
									</div>
								<div class="panel-body">
						<% if(bAdminCreateGroup || (admins != null && bAdminRemoveGroup)) { %>
						 <%-- ===========================================================
							 Community Administrators
							 =========================================================== --%>
									<div class="row">
										<label class="col-md-6" for="submit_admins_create">
										 <% 
										if( community.getID()==topcommunityid)
										 {
										 %>
										  <fmt:message key="jsp.tools.edit-community.form.label8"/>	 
											<% } else {%>
											
											Folder Administrators:
											
											<%}%>
									</label>
										<span class="col-md-6 btn-group">
									<%  if (admins == null) {
											if (bAdminCreateGroup) {
									%>
											<input class="btn btn-success col-md-12" type="submit" name="submit_admins_create" value="<fmt:message key="jsp.tools.edit-community.form.button.create"/>" />
									<%  	}
										} 
										else 
										{ 
											if (bAdminCreateGroup) { %>
											<input class="btn btn-default col-md-6" type="submit" name="submit_admins_edit" value="<fmt:message key="jsp.tools.edit-community.form.button.edit"/>" />
										<%  }
											if (bAdminRemoveGroup) { %>
											<input class="btn btn-danger col-md-6" type="submit" name="submit_admins_remove" value="<fmt:message key="jsp.tools.edit-community.form.button.remove"/>" />
									<%  	}
										}
									%>                    
										</span>
									</div>   
							
							<% }
								
							if (bPolicy) { 
							
							%>

						<%-- ===========================================================
							 Edit community's policies
							 =========================================================== --%>
									<div class="row">
										<label class="col-md-6" for="submit_authorization_edit">
										 <% 
										 if( community.getID()==topcommunityid){
										 %>
										<fmt:message key="jsp.tools.edit-community.form.label7"/> 
											<% } else {%>
											  Folder's Authorizations:	
											<%}%>
										</label>
										<span class="col-md-6 btn-group">
											<input class="col-md-12 btn btn-success" type="submit" name="submit_authorization_edit" value="<fmt:message key="jsp.tools.edit-community.form.button.edit"/>" />
										</span>
									</div>   
							<% }

							if (bAdminCommunity) {
						%> 
						
							<% } %>
							
							
							
							</form>
							</div>
							<% if(bDelete) { %>
							<div class="row">
							<label class="col-md-6" for="submit_admins_create">
							
							 <%if( community.getID()==topcommunityid){
										 %>
										  Delete Cabinet:	 
											<% } else {%>
											Delete Folder:
											<%}%>
							</label>
							
							<span class="col-md-6 btn-group">
									  <form method="post" action="">
										<input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_DELETE_COMMUNITY %>" />
										<input type="hidden" name="community_id" value="<%= community.getID() %>" />
										
										
										 <% if( community.getID()==topcommunityid){
								 %>
							     <input class="btn btn-danger col-md-12" type="submit" name="submit_delete" value="<fmt:message key="jsp.tools.edit-community.button.delete"/>" />
									<% } else {%>
									
									<input class="btn btn-danger col-md-12" type="submit" name="submit_delete" value="Delete this Folder" /> 
									<%}%>
									    </form>
							</span>
								</div>
							<% } %>
						
							
							</div>
							</div>
						</div>
						<% } %>
						</div>	
					</div>
					</div>
					</div>
					</div>
</dspace:layout>
