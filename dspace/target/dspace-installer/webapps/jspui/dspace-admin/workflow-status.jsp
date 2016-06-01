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
	          
        <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-step?action=ddf&&workflowId=<c:out value="${workflowMasterBean.workflow_id}"/>">Add Step</a>
              
				 <a class="btn btn-primary col-md-12" id="edit" href="#">Edit Step</a>
			    <a class="btn btn-primary col-md-12" id="delete" href="#">Delete Step</a>
			
				  <a class="btn btn-primary col-md-12" id="task" href="#">Add Task</a>	
				<!--<a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/workflow-step">Task List</a>					  !-->
			 <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/workflow-step?workflowId=<c:out value="${workflowMasterBean.workflow_id}"/>">Step and Task</a>	
				  <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/workflow-step?workflowId=<c:out value="${workflowMasterBean.workflow_id}"/>&&action=status">Workflow Status</a>			 
			</div>
		</div> 
    </div>	
	<sql:setDataSource var="snapshot" driver="org.postgresql.Driver"
     url="jdbc:postgresql://localhost:5432/dspace"
     user="dspace"  password="dspace"/>


	
<div class="container">   
<!--
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
<div class="table-responsive">      
  <table id="gdRows" class="table table-bordered" style="width:75%">
        <tr class="success" >      
		<th colspan="7">Work flow Step and Tasks:-<c:out value="${workflowMasterBean.workflow_name}"/></th>
       </tr>
   <tr>
   <th></th>
   <th>Task Name</th>
   <th>Priorty</th>
   <th>Deadline</th>
   <th>Assign To</th>
   <th>Task Rule</th>
   <th>Supervisor</th>
   </tr>
    <c:forEach items="${workflowstep}" var="step">
      <tr>
      <td style="width: 1%;">
	  <input type="checkbox" class="chk" id="gdRows_ct<c:out value="${step.workflow_step_id}"/>_chkDelete" name="wid" value="<c:out value="${step.workflow_step_id}"/>"/></td> 
        <td colspan="6" style="width: 30%;"><b><c:out value="${step.workflow_step_name}"/></b>
		</td>
		<sql:query dataSource="${snapshot}" var="result">
			select t.task_id,s.step_id,t.task_name,t.priorty,t.deadline_day,t.deadline_time,t.task_rule_id,
	t.task_owner_id,s.step_name,t.task_instruction from task_master t left join step_master s on s.step_id=t.step_id  left join workflow_master wm on
	 wm.workflow_id=t.workflow_id where t.workflow_id=? and s.step_id=? order by t.task_id
			<sql:param value="${step.workflow_id}" />
			<sql:param value="${step.workflow_step_id}" />
		
		</sql:query>
		
		<dspace:task stepid="${step.workflow_step_id}" workflowid="${step.workflow_id}" />
		<input type="hidden" id="workflow_step_id" name="workflow_step_id" value="<c:out value="${step.workflow_step_id}"/>">		
		<input type="hidden" id="workflowId" name="workflowId" value="<c:out value="${step.workflow_id}"/>">
	 </tr>
    </c:forEach>   
  </table>
  !-->
   <table id="gdRows" border="0" class="table" style="width:75%">
          
	   <tr class="success" >      
		<td colspan="7">Work flow Step and Tasks:-<c:out value="${workflowMasterBean.workflow_name}"/></td>
		<td align="right"><a class="btn btn-primary" href="<%=request.getContextPath()%>/dspace-admin/add-workflow">Back</a></td>		
       </tr>
   <tr>   
   <th>Task Name</th>
   <th>Document</th>  
   <th>Task Status</th>
   <th>Deadline</th>
   <th>Assign To</th>  
   <th>Supervisor</th>
    <th colspan="2">Task Rule</th>
	
   </tr>
    <c:forEach items="${workflowstep}" var="step">
      <tr>      
        <td colspan="8" style="width: 30%;"><p class="text-danger">Step &nbsp;<c:out value="${step.step_no}"/>&nbsp;:-&nbsp;&nbsp;
		<c:out value="${step.workflow_step_name}"/></p>
		</td>
		<sql:query dataSource="${snapshot}" var="result">
			select t.task_id,s.step_id,t.task_name,tr.status,t.deadline_day,t.deadline_time,t.task_rule_id,
	     t.task_owner_id,s.step_name,t.supervisors_id,t.task_instruction from task_master t left join step_master s
		 on s.step_id=t.step_id  left join workflow_master wm on wm.workflow_id=t.workflow_id left join task_role tr on t.task_id=tr.task_id where t.workflow_id=? and s.step_id=? order by t.task_id
			<sql:param value="${step.workflow_id}" />
			<sql:param value="${step.workflow_step_id}" />		
		</sql:query>
			<c:forEach var="row" items="${result.rows}">
			<c:choose> 			 
			<c:when test="${row.status == 'A'}">	
			<tr>
			</c:when>
			 <c:otherwise>			
			<tr bgcolor="#F0F0E2">	
			</c:otherwise>
			</c:choose>							
			<td><b><c:out value="${row.task_name}"/></b></td>
			<td style="width: 15%;">
			<sql:query dataSource="${snapshot}" var="rs11">
		     	select document_name,file_path from workflow_process where task_id=${row.task_id}     		
		    </sql:query>
			<c:forEach var="doc" items="${rs11.rows}">	
				<a href="<c:out value="${doc.file_path}"/>"><c:out value="${doc.document_name}"/></a>
			</c:forEach>
			</td>			
			<td>
			<c:if test="${row.status == 'P'}">	
			<p class="text-warning">Pending</p>
			</c:if>
			<c:if test="${row.status == 'A'}">	
			<p class="text-success">Approved</p>
			</c:if>
			<c:if test="${row.status == 'D'}">	
			<p class="text-danger">Disapproved</p>
			</c:if>	
			</td>			
			<td><!--<img alt="Deadline" height="30" src="<%=request.getContextPath()%>/image/timer.jpg"></img>Deadline Date: !--><c:out value="${row.deadline_day}"/> &nbsp;<c:out value="${row.deadline_time}"/></td>
				<td>				
				<sql:query dataSource="${snapshot}" var="rs1">
		     	Select task_owner_id from task_role where task_id=${row.task_id}     		
		    </sql:query>
					 
			<c:forEach var="r" items="${rs1.rows}">			
				<sql:query dataSource="${snapshot}" var="rs">
			select e.eperson_id,m.text_value as first_name,ln.text_value as last_name from eperson
			e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124
		    left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 
			where e.eperson_id=${r.task_owner_id} order by m.text_value	       		
		</sql:query>
	    	<c:forEach var="ro" items="${rs.rows}">		
				<c:out value="${ro.first_name}"/>&nbsp;<c:out value="${ro.last_name}"/>,
			</c:forEach>		
			</c:forEach>			
			</td>		
			<td>
			<c:set var="str" value="${row.supervisors_id}" />			 
			<c:forEach var="item" items="${fn:split(str,'#')}">			
				<sql:query dataSource="${snapshot}" var="rs">
			select e.eperson_id,m.text_value as first_name,ln.text_value as last_name from eperson
			e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124
		    left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 where e.eperson_id=${item} order by m.text_value	       		
		</sql:query>
			<c:forEach var="ro" items="${rs.rows}">		
				<c:out value="${ro.first_name}"/>&nbsp;<c:out value="${ro.last_name}"/>
			</c:forEach>		
			</c:forEach>			
			</td>
			<td colspan="2">
			<c:choose> 			 
			<c:when test="${row.task_rule_id == '1'}">	
			One user is enough to complete the task.
			</c:when>
			 <c:when test="${row.task_rule_id == '2'}">			
			More than one user is to complete the task.
			</c:when>
			 <c:otherwise>			
			Not define rule.
			</c:otherwise>
			</c:choose>
			</td>			
		</tr>
       		<tr>
			<td colspan="8"><b>Task Instruction:-</b><a style="text-decoration:none"><c:out value="${row.task_instruction}"/></a> </td>					
		</tr>
		</c:forEach>
				
		<!-- <dspace:task stepid="${step.workflow_step_id}" workflowid="${step.workflow_id}" /> !-->
		<input type="hidden" id="workflow_step_id" name="workflow_step_id" value="<c:out value="${step.workflow_step_id}"/>">		
		<input type="hidden" id="workflowId" name="workflowId" value="<c:out value="${step.workflow_id}"/>">
	 </tr>
    </c:forEach>   
  </table>
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
            sColor = 'LightGoldenrodYellow';

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
</div>
</div>
</dspace:layout>