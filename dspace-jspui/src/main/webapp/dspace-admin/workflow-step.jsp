<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.eperson-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
<div class="col-md-3">
                    
		 <div class="panel panel-primary">
             <div class="panel-heading">
             	Admin Tools
             	</div>
             <div class="panel-body">

           <form method="post" action="<%=request.getContextPath()%>/dspace-admin/new-workflow">
		          <input type="hidden" name="community_id" value="2">
		          <input type="hidden" name="action" value="1">
                  <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-step">Add Step</a>
                </form>	
                 <form method="post" action="<%=request.getContextPath()%>/dspace-admin/new-workflow">
		          <input type="hidden" name="community_id" value="2">
		          <input type="hidden" name="action" value="1">
                  <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-step?action=list">Step List</a>
                </form>		
              <form method="post" action="/jspui/dspace-admin/metadataexport">
                 <input type="hidden" name="handle" value="123456789/3">
				  <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-task">Add Task</a>				
			  </form>
			      <form method="post" action="/jspui/dspace-admin/metadataexport">
                 <input type="hidden" name="handle" value="123456789/3">
				  <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/delete-task">Graphical View</a>				
			  </form>
			</div>
		</div>
  
    </div>
<div class="container">   
<table style="width:75%">   
  <tr>
    <th colspan="3"> 
   <c:if test="${not empty message}">
   <div class="alert alert-success">
     <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
   <center><strong>Success!</strong> <c:out value="${message}"/></center>
   </div>
</c:if>
	<c:if test="${not empty errorMessage}">
   <div class="alert alert-danger">
     <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <center> <strong>Error!</strong> <c:out value="${errorMessage}"/></center>
   </div>
  </c:if>
  </th></tr></table>
<div class="table-responsive">      
  <table id="example" class="table table-bordered" style="width:100%">
    <thead>
  <tr>
    <th class="btn-primary" colspan="7">Workflow Steps and Tasks - Invoice Read</th>
  </tr>
      <tr class="success">
	  <th>Task ID</th>
	  <th>Step Name</th>
        <th>Task Name</th>
        <th>Priority</th>
		<th>Deadline</th>		
		<th>Task Rule</th>
       </tr>
    </thead>
    <tbody>
	<c:forEach items="${taskMasterList}" var="task">
	   <tr>
        <td><c:out value="${task.task_id}"/></td>	
		<td><a href="#"><c:out value="${task.workflow_step}"/></a></td>			
         <td><c:out value="${task.task_name}"/></td>
		  <td><c:out value="${task.priorty}"/></td>
		 <td>Day:-<c:out value="${task.deadline_day}"/>&nbsp;&nbsp;Time:- <c:out value="${task.deadline_time}"/></td>
			<td>
		<c:if test="${task.task_rule_id == 1}">
        <p>One user is enough to complete the task<p>
       </c:if>
	   <c:if test="${task.task_rule_id == 2}">
		More than one user is to complete the task
		</c:if>
		</th>
		
        </tr>
	</c:forEach>
    </tbody>
  </table>
  <script>
   $(document).ready(function() {
    $('#example').DataTable( {
        "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "All"]]
    } );
} );
$( document ).ready(function() {
    $(".dataTables_wrapper").css("width","77%");	
	 $(".dataTables_wrapper").css("bottom","210px");	
	});

  </script>
</div>
 </div>
</dspace:layout>