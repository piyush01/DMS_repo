<%@page import="org.dspace.authorize.ResourcePolicy"%>
<%@page import="org.dspace.eperson.Group"%>
<%@page import="org.dspace.eperson.EPerson"%>
<%@page import="org.dspace.content.Community"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.dspace.core.Constants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.lang.String" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/containerscroll.css" type="text/css" />
<%
String message=(String)request.getParameter("message");
Community toplabelcommunities[]=(Community[])request.getAttribute("toplavelcommunities");
EPerson eperson[]=(EPerson[])request.getAttribute("epeople");
Group groups[]=(Group[])request.getAttribute("groups");
Community communities[]=(Community[])request.getAttribute("communities");
boolean createfolder=(request.getAttribute("folder.create")!=null); 
int resourceType      = 4;
    int resourceRelevance = 1 << resourceType;     
%>
<script type="text/javascript">      
	  $(document).ready(function()
      {   
			$('#create-folder').click(function() {
				var letters = /^[0-9]+$/; 
		if ($.trim($('#cabinename').val()) == 0) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Select Cabinet.</div>');			
		$('#cabinename').focus();
     return false;
   
	 }
	 else if($.trim($('#folder_id').val())==0){
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Folder Name.</div>');			
		$('#folder_id').focus();
     return false;
	 }
	 else if($.trim($('#folder_id').val()).match(letters)){
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Folder name should not be numeric character only.</div>');			
		$('#folder_id').focus();
     return false; 
	 }
     });
	 });	 
	</script>	
<dspace:layout style="submission" titlekey="jsp.tools.edit-community.title"
		       navbar="admin"
		       locbar="link"
		       parentlink="/dspace-admin"
		       parenttitlekey="jsp.administer" nocache="true">			
          <div class="row">
           		 <!-- left column -->
            		<div class="col-md-12">
              		<!-- general form elements -->
              				<div class="box box-primary">
                					<div class="box-header with-border">
             						<h2 class="box-title">Create Folder</h2>
             						 <% if(message!=null && !message.equals("") && message.equals("Y"))
									 {
										 %>
										 <div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Folder has been successfully created!</div>
										 <%
									 }
									 %>
						<div id="error-alert"></div>
                					</div>
     <form class="form-horizontal" method="post" action="<%= request.getContextPath() %>/dspace-admin/create-folder" name="createfolder" id="createfolder">
		<div  class="box-body">
			<%--<h4>Please enter the following information. Required fields are marked with a (<span class="star">*</span>)</h4>--%>
			<div class="form-group">
				<div class="col-sm-2">
			  		<label>	Select Cabinet<span class="star">*</span></label>
				</div>
			<div class="col-sm-3">
				<select class="form-control" name="cabinet" id="cabinename">
					<option value="0" selected>--Select Cabinet--</option>
						<%for (int i = 0; i <toplabelcommunities.length; i++) {%>
					<option value="<%=toplabelcommunities[i].getID()%>"><%=toplabelcommunities[i].getName()%></option>
					
					<%} %>    
				</select> 
			</div>		
			
			</div>
			<div class="form-group">
		      	<div class="col-sm-2">
			  		<label>	Folder Name<span class="star">*</span></label>
				 </div>
			<div class="col-sm-3">
			
				 <input class="form-control" id="folder_id" type="text" name="foldername"/>
			</div>		
			
			</div>
			<div class="form-group">
				<div class="col-sm-2">
			  		<label>	Select Group</label>
				</div>
			<div class="col-sm-3">
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
			<div class="col-sm-3">
				<select class="form-control" name="username" id="userid">
					<option value="0" selected>--Select User--</option>
						<%for (int i = 0; i <eperson.length; i++) {%>
					<option value="<%=eperson[i].getID()%>"><%=eperson[i].getFullName()%></option>
					
					<%} %>    
				</select> 
			</div>		
			
			</div> 
			<div class="form-group">
				<div class="col-md-2">
					<label for="exampleInputEmail1">Action</label>
				</div>
			<div class="col-sm-3">
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
						<input  type="checkbox" id="policyaction" name="actionname" value="<%= i %>"/> &nbsp;<%=Constants.actionText[i]%> 
						<td>
						</tr>
						<% } }} %>
				</table>			
			</div>
			</div>
			</div>

		
             	<div class="form-group">
		      	<div class="col-sm-2">	
				 </div>
			   <div class="col-sm-3">
			
				<input class="btn btn-primary" type="submit" name="submit_folder" id="create-folder" value="Submit"/>
				<input class="btn btn-danger" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
			</div>		
			</div>
     
		
		 </div>
		 </form>
                </div>
                </div>
                </div>               
</dspace:layout>
