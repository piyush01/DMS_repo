<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.workflowmanager.WorkflowManager" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.eperson-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
                  
              <script>
function myFunction() {
  var r= confirm("Are you sure?");
 if(r==true)
  {	
	  return true;
  }else{	 
	  return false;
  }  
 }
</script>
<div class="col-md-3">
		 <div class="panel panel-primary" style="height:420px;background:#428bca">
             <div class="panel-heading">
             	Admin Tools
             	</div>
             <div class="panel-body">

         <form method="post" action="<%=request.getContextPath()%>/dspace-admin/new-workflow">
		          <input type="hidden" name="community_id" value="2">
		          <input type="hidden" name="action" value="1">
                  <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/new-workflow">New workflow</a>
                </form>
				<form method="post" action="/jspui/tools/collection-wizard">
		     		<input type="hidden" name="community_id" value="2">
                   <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-workflow">workflow List</a>
                </form>
                <form method="post" action="/jspui/mydspace">
                  <input type="hidden" name="community_id" value="2">
                  <input type="hidden" name="step" value="5">
            <a class="btn btn-primary col-md-12" href="#">Properties</a>
                </form>
             
                  <input type="hidden" name="handle" value="123456789/3">
				  <button class="btn btn-primary col-md-12" id="step" >Workflow Step</button>				
			  
			
			</div>
		</div>
  
    </div>
<div class="container">  

<table style="width:65%">   
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
     <strong>Error!</strong> <c:out value="${errorMessage}"/>.
   </div>
  </c:if>
  </th></tr></table>
  
<div class="table-responsive">      
 <table id="gdRows" class="table table-bordered" style="width:75%">
        <tr class="success" >
  <th scope="col"> </th><!--<th scope="col">Workflow ID</th>!--><th scope="col">Workflow Name</th><th scope="col">Action</th>
        </tr>     	
    <c:forEach items="${workflows}" var="workflow">
      <tr >
	  <td>
	  <c:choose>
	  <c:when test="${workflow.workflow_id=='1'}" >
	  
	  </c:when>
	  <c:otherwise>
	  <input type="checkbox" class="chk" id="gdRows_ct<c:out value="${workflow.workflow_id}"/>_chkDelete" value="<c:out value="${workflow.workflow_id}"/>" name="id" />
	  </c:otherwise>
	  </c:choose>
	  </td>
      <!--  <td><c:out value="${workflow.workflow_id}"/></td> !-->
        <td><c:out value="${workflow.workflow_name}"/></td>
        <td>
		<form action="<%=request.getContextPath()%>/dspace-admin/new-workflow" method="post">
		<input type="submit" class="btn btn-primary" name="submit_edit" value="Edit" >
		<input type="submit" class="btn btn-danger" onclick="return myFunction();" name="submit_delete" value="Delete" >
		<input type="hidden" name="workflowId" value="<c:out value="${workflow.workflow_id}"/>">	
		</form>
		</td>
        </tr>
        </c:forEach>    
  </table>
  <style>
  td
{
    padding:5px;
}
th
{
    padding:10px;
    font-weight:bold;
}
</style>

  </table>
  
   <script>
  $(document).ready(function(){
	 
	$("#step").click(function(){
	
		if ($(".chk").is(":checked")==true) {
			$('input:checkbox[name=id]').each(function() 
				{    
				if($(this).is(':checked'))	
				
				  window.location.href="<%=request.getContextPath()%>/dspace-admin/workflow-step?workflowId="+$(this).val();
				
				});	
				  
            return true;
        }
		else{
		alert("Please select checkbox!");
			return false;
		} 
    })
 });

$( document ).ready(function() {  	
      $('#gdRows').find('input:checkbox[id$="chkDelete"]').click(function() {
        var isChecked = $(this).prop("checked");
        var $selectedRow = $(this).parent("td").parent("tr");
        var selectedIndex = $selectedRow[0].rowIndex;
        var sColor = '';        
		var theCheckboxes = $("input[type='checkbox']"); 
		if (theCheckboxes.filter(":checked").length > 1) {
        $(this).removeAttr("checked");
        alert( "Please selected one user at a time for selecting." );
        return false;
		}		
        if (selectedIndex % 2 == 0) 
            sColor = '';
        else 
            sColor = '';

        if (isChecked) 
        {
            $selectedRow.css({
                "background-color": "DodgerBlue",
                "color": ""
            });
        }
        else 
        {
            $selectedRow.css({
                "background-color": sColor,
                "color": "black"
            });
        }
    });
});
  </script>
  
</div>
 </div>
</dspace:layout>