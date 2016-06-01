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
<%@ page import="org.dspace.content.DmsMetadaField" %>
<%
boolean message=(request.getAttribute("message")!=null);
DmsMetadaField[] dmsfiels=(DmsMetadaField[])request.getAttribute("all_field");
DmsMetadaField[] allsearchfield=(DmsMetadaField[])request.getAttribute("allsearchfield");
%>


<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
         
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>			
					</div>
					<!-- <h3 class="box-title">Document Type</h3> -->
					<%if(message){ %>
					<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
						 Search Field add
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
					<%for (int i = 0; i <dmsfiels.length; i++) {
							int x=0;
						 for (int j = 0; j < allsearchfield.length; j++) {
							 if (allsearchfield[j].getID()==dmsfiels[i].getID()) {
			                       x=allsearchfield[j].getID();
			                    	
			                        break;
			                    }
						 }
						 if (dmsfiels[i].getID()!=x) {
						%>
					<option value="<%=dmsfiels[i].getFieldName()%>"><%=dmsfiels[i].getFieldName() %></option>
					
					<%}} %>    
				</select> 
				</div>
				<div class="col-sm-3">
				</div>
				</div>
			
			<div class="form-group">
		      <div class="col-sm-2">			  
				</div>
				<div class="col-sm-3">	
				  <input class="btn btn-primary" type="submit" name="submit_SearchField" id="save" value="Add"/>
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
			<!--Fields added of this Search-->				
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>				
					</div>
					<h3 class="box-title">Fields in Search</h3>
					</div>
	<div class="box-body">   
		 <table id="example1" class="table table-bordered table-striped">	  
					 <thead>
					  <tr>
						<th>S.No</th>
						<th>Field Name</th>
						<th>Data Type</th>
					
				   </tr>
					</thead>
					<tbody>
					<%for(int i=0;i<allsearchfield.length;i++){ %>
					
					<tr>
					<td><%=i+1%></td>
					<td><%=allsearchfield[i].getFieldLevel() %></td>
					<td>
					<% if(allsearchfield[i].getDataType().equals("name")){ %>
						Text
					<%} else if(allsearchfield[i].getDataType().equals("onebox")){%>
					 Numeric
					<%}else{%>				
					 <%=allsearchfield[i].getDataType()%>
					<%}%>
					</td>
					</tr>
				<%} %>  
					</tbody>
		</table>
</div>
	   
				</div>
				</div>
				</div>			
				
</dspace:layout>
