<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.workflowmanager.*" %>
<%@ page import="org.dspace.app.webui.util.UIUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
		 <div class="panel panel-primary" style="height:500px;background:#428bca">
             <div class="panel-heading">
             	Admin Tools
             	</div>
             <div class="panel-body">
	          <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/workflow-step?workflowId=<c:out value="${workflowMasterBean.workflow_id}"/>&&action=status">Workflow Status</a>
              <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-step?action=ddf&&workflowId=<c:out value="${workflowMasterBean.workflow_id}"/>">Add Step</a>
			  <a class="btn btn-primary col-md-12" id="edit" href="#">Edit Step</a>
			  <a class="btn btn-primary col-md-12" id="delete" href="#">Delete Step</a>				
              <a class="btn btn-primary col-md-12" id="task" href="#">Add Task</a>	
			  <a class="btn btn-primary col-md-12" id="task_edit" href="#">Edit Task</a>
			  <a class="btn btn-primary col-md-12" id="task_delete" href="#">Delete Task</a>				
			</div>
		</div> 
    </div>	
    
	<sql:setDataSource var="snapshot" driver="org.postgresql.Driver" url="jdbc:postgresql://localhost:5433/dspace" user="dspace"  password="dspace"/>
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
     <center><strong>Error!</strong> <c:out value="${errorMessage}"/></center>
   </div>
  </c:if>
  
  </th></tr></table>
  
   <table id="gdRows" border="0" class="table" style="width:75%">
          
	   <tr class="alert alert-success" >      
		<td colspan="7"><strong>Work flow Step and Tasks:-<c:out value="${workflowMasterBean.workflow_name}"/></strong></td>
		<td align="right"><a class="btn btn-primary" href="<%=request.getContextPath()%>/dspace-admin/add-workflow">Back</a></td>		
       </tr>
   <tr>
   <th></th>
   <th>Task Name</th>  
   <th>Priorty</th>
   <th></th>
   <th>Deadline</th>
   <th>Assign To</th>
   <th>Task Rule</th>
   <th>Supervisor</th>
   </tr>
   
    <c:forEach items="${workflowstep}" var="step">
	<input type="hidden" id="workflow_step_id" name="workflow_step_id" value="<c:out value="${step.workflow_step_id}"/>">
		<input type="hidden" id="workflowId" name="workflowId" value="<c:out value="${step.workflow_id}"/>">
      <tr>
      <td style="width: 1%;">
	  <input type="checkbox" class="chk" id="gdRows_ct<c:out value="${step.workflow_step_id}"/>_chkDelete" name="wid" value="<c:out value="${step.workflow_step_id}"/>"/></td> 
        <td colspan="8" style="width: 30%;"><p class="text-danger">Step &nbsp;<c:out value="${step.step_no}"/>&nbsp;:-&nbsp;&nbsp;
		<c:out value="${step.workflow_step_name}"/></p>
		</td></tr>
		<dspace:task workflowid="${step.workflow_id}" stepid="${step.workflow_step_id}"/>	
  </c:forEach>   
  </table>  
</div>

   <script>
   $(document).ready(function(){	 
	$("#delete").click(function(){	
	    if ($(".chk").is(":checked")==true) {
			var t=myFunction();
			if(t==false)
			  {	
			   return false;
			  }  
			$('input:checkbox[name=wid]').each(function(){    
				if($(this).is(':checked'))					
					window.location.href="<%=request.getContextPath()%>/dspace-admin/add-step?workflow_step_id="+$(this).val()+"&&workflowId="+$("#workflowId").val()+"&&action=submit_delete";				
				});
            return true;
        }
		else{
			alert("Please select checkbox!");
			return false;
		}
    })
 });
   
   $(document).ready(function(){	 
	$("#edit").click(function(){	
		if ($(".chk").is(":checked")==true) {
			$('input:checkbox[name=wid]').each(function(){    
				if($(this).is(':checked'))					
					window.location.href="<%=request.getContextPath()%>/dspace-admin/add-step?workflow_step_id="+$(this).val()+"&&workflowId="+$("#workflowId").val()+"&&action=submit_edit";				
				});
            return true;
        }
		else{
			alert("Please select checkbox!");
			return false;
		}
    })
 });
   
  $(document).ready(function(){	 
	$("#task").click(function(){	
		if ($(".chk").is(":checked")==true) {
			$('input:checkbox[name=wid]').each(function(){    
				if($(this).is(':checked'))	                				
				 window.location.href="<%=request.getContextPath()%>/dspace-admin/add-task?step-id="+$(this).val()+"&&workflowId="+$("#workflowId").val();				
				});
            return true;
        }
		else{
			alert("Please select checkbox!");
			return false;
		}
    })
 });

 $(document).ready(function(){	 
	$("#task_edit").click(function(){	
		if ($(".chk").is(":checked")==true) {
			$('input:checkbox[name=tid]').each(function(){    
			//alert($(this).val());
				if($(this).is(':checked'))	                				
               window.location.href="<%=request.getContextPath()%>/dspace-admin/add-task?task-id="+$(this).val()+"&&action=taskedit";				
				});
            return true;
        }
		else{
			alert("Please select checkbox!");
			return false;
		}
    })
 });
 
 $(document).ready(function(){	 
	$("#task_delete").click(function(){	
		if ($(".chk").is(":checked")==true) {
			var t=myFunction();
			if(t==false)
			  {	
			   return false;
			  } 
			$('input:checkbox[name=tid]').each(function(){    
				if($(this).is(':checked'))	                				
				window.location.href="<%=request.getContextPath()%>/dspace-admin/add-task?task-id="+$(this).val()+"-"+$("#workflowId").val()+"&&action=taskdelete";				
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
                "color": "GhostWhite"
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
</dspace:layout>