<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@page import="java.util.ArrayList" %>	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>


<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.dspace.eperson.EPerson" %>
<%
boolean Approve = (request.getAttribute("Userupdate") != null);
boolean disapprove = (request.getAttribute("Usernotupdate") != null);
boolean userdisapprov = (request.getAttribute("userdisapprov") != null);
%>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
<%
if(userdisapprov)
{
%>	
 <p class="alert alert-warning">User Disapproved Successfully!</p>
<%}%>	
<%
if(Approve)
{
%>	
 <p class="alert alert-success">User Approved Successfully!</p>
<%}%>
<%
if(disapprove)
{
%>	
<p class="alert alert-warning">User Does Not Approved Successfully!</p>
<%}%>
     
	 
	  <!-- Main content -->
        <section class="content">
          <div class="row">
            <div class="col-xs-12">
              <!-- general form elements -->			  
			  <div class="box box-primary">
                <div class="box-header">
                  <h3 class="box-title">New User List</h3>
                </div><!-- /.box-header -->
                <div class="box-body">		
	 <table id="example1" class="table table-bordered table-striped">	  
         <thead> 	
			<tr>       
            <th>User ID</th>
			<th>Email ID</th>
			<th>First Name</th>
            <th>Last Name</th>
			 <th>Action</th>
       </tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="userBean">
		<tr>
		<td><c:out value="${userBean.user_id}"/></td>
		<td><c:out value="${userBean.email}"/></td>
		<td><c:out value="${userBean.first_name}"/></td>
		<td><c:out value="${userBean.last_name}"/></td>
		<form action="" method="post">
		<td>
		 <input type="hidden" name="user_id" value="${userBean.user_id}" />
		<input class="btn btn-success" name="submit_approve" value="Approve" type="submit">
		<input class="btn btn-danger" name="submit_disapprove" value="Disapprove" type="submit">
		</td>
		</form>     
		</c:forEach> 
		</tbody>
</table>  
				</div><!-- /.box-body -->
              </div><!-- /.box -->
            </div><!-- /.col -->
          </div><!-- /.row -->
        </section><!-- /.content -->      

</dspace:layout>
