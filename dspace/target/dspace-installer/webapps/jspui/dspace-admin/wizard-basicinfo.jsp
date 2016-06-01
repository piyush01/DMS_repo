
<%--
  - basic info for collection creation wizard
  -
  - attributes:
  -    collection - collection we're creating
  --%>

<%@page import="org.dspace.core.Constants"%>
<%@page import="org.dspace.eperson.Group"%>
<%@page import="org.dspace.eperson.EPerson"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.app.webui.servlet.admin.CollectionWizardServlet" %>
<%@ page import="org.dspace.content.Collection" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/containerscroll.css" type="text/css" />
<%  /* Collection collection = (Collection) request.getAttribute("collection");  */
	String communityID=(String)request.getParameter("community_id");
	EPerson eperson[]=(EPerson[])request.getAttribute("eperson");
	Group groups[]=(Group[])request.getAttribute("groups");
	int resourceType      = 3;
    int resourceRelevance = 1 << resourceType; 
%>
<script type="text/javascript">      
	  $(document).ready(function()
      {   
			$('#create_subfolder').click(function() {
				var letters = /^[0-9]+$/; 
		if ($.trim($('#subfolde_name').val()) == 0) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Sub Folder Name.</div>');			
		$('#subfolde_name').focus();
     return false;
   
	 }
	 else if($.trim($('#subfolde_name').val()).match(letters)){
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Sub Folder name should not be numeric character only.</div>');			
		$('#subfolde_name').focus();
     return false; 
	 }
     });
	 });	 
	</script>
<dspace:layout locbar="commLink" title="Edit"  nocache="true">
<div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<!--<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
					<h3 class="box-title">			
					Create Sub Folder
					
			       </h3>	
<div id="error-alert"></div>				   
			   </div>
              

    <form class="form-horizontal" action="<%= request.getContextPath() %>/tools/collection-wizard" method="post">
    
	   <div class="box-body">

				<div class="form-group">
				<div class="col-sm-2">
			  	<label for="short_description"><fmt:message key="jsp.dspace-admin.wizard-basicinfo.name"/><span class="star">*</span></label>
				</div>
				<div class="col-sm-3">
				<input class="form-control" id="subfolde_name" type="text" name="name" size="50" id="tname" />
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
						{ if( (Constants.actionTypeRelevance[i]&resourceRelevance) > 0)
                                    { 
						%>
						<tr>
						<td>  &nbsp;&nbsp;
						<input  type="checkbox" name="action_id" value="<%= i %>"/> &nbsp;<%=Constants.actionText[i]%> 
						<td>
						</tr>
						<% }} %>
				</table>			
			</div>
			</div>
			</div>
			

				   <input class="form-control" type="hidden" name="short_description" />
				   <input class="form-control" type="hidden" name="introductory_text" />
				   <input class="form-control" type="hidden" name="copyright_text" />
				    <input class="form-control" type="hidden" name="side_bar_text" />
					<input class="form-control" type="hidden" name="license" />
				    <input class="form-control" type="hidden" name="provenance_description" />
	    			
					<!-- modify by sanjeev kuamr -->
				<%-- <div class="form-group">
					<td><p class="submitFormLabel">Provenance:</p></td>
					<label class="col-sm-2" for="file"><fmt:message key="jsp.dspace-admin.wizard-basicinfo.logo"/></label>
					<span class="col-sm-5">
					<input class="form-control" type="file" size="40" name="file"/>
					</span>
					<span class="col-sm-5"></span>
				</div>            --%> 
				
				<!-- modify End by sanjeev kuamr -->    	
	    <div class="form-group">
		<div class="col-sm-2">
				</div>
				<div class="col-md-3">
					<%-- Hidden fields needed for servlet to know which collection and page to deal with --%>
					
					<input type="hidden" name="stage" value="<%= CollectionWizardServlet.BASIC_INFO %>" />
					<input type="hidden" name="community_id1" value="<%=communityID %>" />
					<%-- <input type="submit" name="submit_next" value="Next &gt;"> --%>
					<input class="btn btn-primary" type="submit" id="create_subfolder" name="submit_subfolder" value="Submit" />
					<input class="btn btn-danger" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
				</div>
				</div>  
				
				</div>        		
    </form>
	</div>
				</div>
				</div>
				</div>
</dspace:layout>
