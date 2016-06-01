<%@page import="org.dspace.content.DmsMetadaField"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@page import="java.util.ArrayList" %>	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.lang.String" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/containerscroll.css" type="text/css" />
<script src="<%= request.getContextPath() %>/static/js/formvalidation/jquery-1.11.1.js" type="text/javascript"></script>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.MetadataField" %>
<%@ page import="org.dspace.content.MetadataSchema" %>
<%@ page import="org.dspace.content.Collection" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.dspace.eperson.EPerson" %>
<%
boolean message=(request.getAttribute("message")!=null);
Collection collection = (Collection) request.getAttribute("collection");
%>
<script type="text/javascript">      
	  $(document).ready(function()
      {   
			$('#save').click(function(e) {
		if ($.trim($('#fieldname').val()) == 0) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Select Field Name.</div>');			
		$('#fieldname').focus();
     return false;
	 }
     });
	 });	 
	</script>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
           <section class="content">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
					</div>
					<h3 class="box-title">Add Input Form Field</h3>
					<%if(message){ %>
					<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
						 Input-form.xml file Updated.
					</div>
					<%} %>	   
					<div id="error-alert"></div>
					</div>
			
	   
				<form class="form-horizontal" method="post" action="" name="addfields" id="addfields">
			<div  class="box-body">   
			
			<div class="form-group">
		      <div class="col-sm-2">			  
				<label>Select Field Name<span class="star">*</span></label>
				</div>
				<div class="col-sm-3">
				 <select class="form-control" name="fieldname" id="fieldname">
					<option value="0" selected>--Select Field Name--</option>
					<c:forEach items="${dmsfields}" var="dmsf">
					<option value="${dmsf.field_name}"><c:out value="${dmsf.field_name}"/></option>
				</c:forEach>     
				</select> 
				</div>
				<div class="col-sm-3">
				</div>
				</div>
			
			<div class="form-group">
		      <div class="col-sm-2">			  
				</div>
				<div class="col-sm-3">				
				 <input type="hidden" value="${collection.getID()}"  name="collectionid" />
				  <input class="btn btn-primary" type="submit" name="submit_add_InputForm" id="save" value="Add"/>
				<input class="btn btn-danger" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
					</div>
				<div class="col-sm-3">
				</div>
				</div>
				</div>
				</form>
				</div>
				</div>
				</div>
				</section>
				
				
				<!--Fields added of this Sub-Folder-->
				 <section class="content">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
					</div>
					<h3 class="box-title">Fields in this Sub-Folder</h3>
					</div>
	<div class="box-body">   
		 <table id="example1" class="table table-bordered table-striped">	  
					 <thead>
					  <tr>
				   
						<th>Field Name</th>
						<th>Data Type</th>
					
				   </tr>
					</thead>
					<tbody>
					<c:forEach items="${fields}" var="field">
					<tr>
					<td><c:out value="${field.field_name}"/></td>
					<td><c:out value="${field.datatype}"/></td>
					</tr>
				</c:forEach>    
					</tbody>
		</table>
</div>
	   
				</div>
				</div>
				</div>
				</section>
</dspace:layout>
