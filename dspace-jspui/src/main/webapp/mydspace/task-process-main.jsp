

<link rel="stylesheet" href="/jspui/css/style.css">


<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<dspace:layout style="submission" titlekey="jsp.mydspace" nocache="true">
<style> 
 p{    
    font: bold;
}
</style>
<script>

$(document).ready(function(){
    $("#submit_task1").click(function(){
		
		if ($.trim($('#astatus').val()) =="0") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please select task results.</div>');			
		$('#astatus').focus();
     return false;
	 }	
	 else if ($.trim($('#task_comment1').val()) =="") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please write task comments.</div>');		
		$('#task_comment1').focus();
     return false;
	 }
      return true;
    });
});
$(document).ready(function(){
    $("#submit_postpone").click(function(){
		
		if ($.trim($('#datepicker').val()) =="") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please select datepicker.</div>');			
		$('#astatus').focus();
     return false;
	 }	
	 else if ($.trim($('#postpone_task_comment').val()) =="") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please write task comments.</div>');		
		$('#task_comment1').focus();
     return false;
	 }
      return true;
    });
});



$(document).ready(function(){
    $("#submit_change_task").click(function(){
		
	if ($.trim($('#change_task_comment').val()) =="") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please write task comments.</div>');		
		$('#change_task_comment').focus();
     return false;
	 }
      return true;
    });
});

$(document).ready(function(){
    $("#update_submit_task").click(function(){
		
	if ($.trim($('#update_task_comment').val()) =="") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please write task comments.</div>');		
		$('#update_task_comment').focus();
     return false;
	 }
      return true;
    });
});

 
 
 function completeTask(){
	 var status=$("#st").val();
	 if(status=="A"){
		 $('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Task is already completed.</div>');	
		 //alert("Task is already completed.");
	 return false;
	 }
	 else if(status=="D"){
		 $('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Task is already completed.</div>');	
		 //alert("Task is already completed.");
		  return false;
	 }
	 else{
		 return true;
	 }
	 return false;
 }
 
 function commentTask(){
	 var comments=$("#task_comment").val();	
	 if(comments !== null && comments !== undefined && comments !=''){
		 //alert("You have already commented.");
		  $('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>You have already commented.</div>');	
	 return false;
	 }
	 else{
		 return true;
	 }
	 return false;
 }
 
 function completeEdit(){
	 var status=$("#st").val();
	 if(status=="P"){
		  $('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Task is already completed.</div>');
		// alert("Task is already completed.");
	 return false;
	 }
	 else if(status=="D"){
		 $('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Task is already completed.</div>');
		// alert("Task is already completed.");
		  return false;
	 }
	 else{
		 return true;
	 }
	 return false;
 }
 
 
  $(function() {
    $( "#datepicker" ).datepicker();
  });
 
</script>

<div style="padding:0px 80px 0px 0px;" class="col-md-3">
<div class="panel panel-primary" style="height:490px;background:#428bca">
			<div class="panel-heading">Control Panel</div>
			<div class="panel-body">
						<form action="<%=request.getContextPath()%>/mytask" method="post">
							<c:forEach items="${taskdetails}" var="task" begin="0" end="0">
						<input class="btn btn-primary col-md-12" type="submit" value="Document Properties" name="submit_doc_properties"/>
						<c:if test="${task.approve== '3'}">
						<input id="task" onclick="return completeTask();" class="btn btn-primary col-md-12" type="submit" value="Approve Task" name="submit_complete_task"/>
						</c:if>
						<c:if test="${task.comment== '2'}">
						<input id="task" class="btn btn-primary col-md-12" onclick="return commentTask();" type="submit" value="Comment Task" name="submit_comment_task"/>
						</c:if>
						
						<c:if test="${task.allStep== '1'}">
						<input class="btn btn-primary col-md-12"  type="submit" value="Edit Comment" name="submit_edit_comment"/>
						<input class="btn btn-primary col-md-12"  onclick="return completeTask();" type="submit" value="Postpone Task" name="submit_postpone_task"/>
						<input class="btn btn-primary col-md-12" type="submit" value="Change Task Finished" name="submit_changetask_finished"/>						
						</c:if>
						<c:if test="${task.edit_comment== '2'}">
						<input class="btn btn-primary col-md-12"  type="submit" value="Edit Comment" name="submit_edit_comment"/>
						</c:if>
						
						<c:if test="${task.postpone_task== '3'}">
						<input class="btn btn-primary col-md-12"  onclick="return completeTask();" type="submit" value="Postpone Task" name="submit_postpone_task"/>
						</c:if>
						
						<c:if test="${task.change_finished_task== '4'}">
						<input class="btn btn-primary col-md-12" type="submit" value="Change Task Finished" name="submit_changetask_finished"/>
						</c:if>
						<input class="btn btn-primary col-md-12" type="submit" value="Show Workflow" name="submit_show_workflow" />	  
						<input class="btn btn-primary col-md-12" type="submit" value="Create Associate Task" name="submit_associate_task" />	  
						<input class="btn btn-primary col-md-12" type="submit" value="Reassign Task" name="submit_reassign_task" />
						<input class="btn btn-primary col-md-12" type="submit" value="Add Attachment" name="submit_attachment_file"/>
						<input type="hidden" name="process_task_id" value="${task.process_id}-${task.task_id}">
						<input type="hidden" id ="st" name="status" value="${task.status}">
						<input type="hidden" id ="item_id" name="status" value="${task.item_id}">
						<input type="hidden" id="workflow_id" name="workflow_id" value="${task.workflow_id}">
						<input type="hidden" id="workflow_stepid" name="workflow_stepid" value="${task.workflow_stepid}">
				        <input type="hidden" id="workflow_taskid" name="workflow_taskid" value="${task.workflow_taskid}">		
						<input type="hidden" id="task_comment" name="task_comment" value="${task.task_comment}">
                         <input type="hidden" name="process_id" value="${task.process_id}">
	                    <input type="hidden" name="task_id" value="${task.task_id}">
					    <input type="hidden" name="role" value="${role}">
					   <c:set var="stepid" value="${task.workflow_stepid}"/>  
					</c:forEach>	
					
				<!--<input class="btn btn-primary col-md-12" type="submit" value="Add Attachment" name="submit_attachment_file"/>
						
						<input class="btn btn-primary col-md-12" type="submit" value="Add Attachment" name="submit_attachment_file"/>
						<input class="btn btn-primary col-md-12" type="submit" value="Create Associated Task" name="submit_complete_task"/>
						<input class="btn btn-primary col-md-12" type="submit" value="Reassign To" name="submit_reassign"/>
						-->					
						</form>
				</div>
		</div>
		</div> 	
		<c:if test="${not empty successMessage}">
		    <div class="col-md-9 alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			 <strong>Success! :- </strong><c:out value="${successMessage}"/>
			 </div>
			 </c:if>
		   <c:if test="${not empty errorMessage}">
		     <div class="col-md-9 alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			<strong>Error! :- </strong>
			  <c:out value="${errorMessage}"/>
			 </div>
		    </c:if>
			<div class="col-md-9" id="taskalert"></div>
			
		<c:if test="${pageMode == 'doc_properties'}">
		<table class="table" style="width:70%">
		
			<c:forEach items="${taskdetails}" var="task">
			<c:if test="${same != task.task_name}">		
		  <c:set var="same" value="${task.task_name}"/>
			<tr>
			<td class="gtext"><p>Priorty</p></td><td>:-</td>
			<td><c:out value="${task.priorty}"/></td>
			</tr>
			<tr>
			<td class="gtext"><P>Task Results</p></td><td>:-</td>
			<td>
			<c:if test="${task.status == 'P'}">
		    Pending
		    </c:if>
		    <c:if test="${task.status == 'A'}">
		     Approved
		    </c:if>
			 <c:if test="${task.status == 'D'}">
		     Disapproved
		    </c:if>
			</td>
			</tr>
			<tr>
			<td class="gtext"><p>Due Date<p></td><td>:-</td>
			<td><c:out value="${task.due_Date}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Workflow name</p></td><td>:-</td>
			<td><c:out value="${task.workflow_name}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Step Name</p></td><td>:-</td>
			<td><c:out value="${task.step_name}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Task Name</p></td><td>:-</td>
			<td>
			<c:out value="${task.task_name}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Document</p></td><td>:-</td>
			<td>
			<c:forEach items="${task.documentList}" var="document" varStatus="documentStatus">
         <li style="list-style-type:none"><c:out value="${documentStatus.count}."/>			 
		<a id="doc" onclick="return documentread();" href="<c:out value="${document.key}"/>"><c:out value="${document.value}"/></a>
		</li>
		
		</c:forEach>
			</td>
			</tr>
			<tr>
			<td class="gtext"><p>Assigned By</p></td><td>:-</td>
			<td><c:out value="${task.user_name}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Assigned To</p></td><td>:-</td>
			<td><c:out value="${task.assign_to_user}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Superviosor</p></td><td>:-</td>
			<td><c:out value="${task.supervisor_name}"/></td>
			</tr>
			<tr>
			<td class="gtext"><p>Instruction</p></td><td>:-</td>
			<td><c:out value="${task.task_instruction}"/></td>
			</tr>
			</c:if>
		</c:forEach>
		</table>		
		</c:if>
		<c:if test="${pageMode == 'complete_task'}">				  
		<table class="table " style="width:75%">		
		 <form enctype="multipart/form-data" action="<%=request.getContextPath()%>/attachment" method="post">
	                  <c:forEach items="${taskdetails}" var="task">
					  <c:if test="${same != task.task_name}">		
		              <c:set var="same" value="${task.task_name}"/>
				<input type="hidden" name="process_task_id" value="<c:out value="${task.process_id}-${task.task_id}"/>"/>	
					   	<input type="hidden" id="task_id" name="task_id" value="${task.task_id}">
						<input type="hidden" id="process_id" name="process_id" value="${task.process_id}">
						<input type="hidden" id="st" name="status" value="${task.status}">
						<input type="hidden" id="item_id" name="item_id" value="${task.item_id}">
						<input type="hidden" id="workflow_id" name="workflow_id" value="${task.workflow_id}">
						<input type="hidden" id="handle" name="handle" value="${task.handle}">					
						<input type="hidden" id="workflow_stepid" name="workflow_stepid" value="${task.workflow_stepid}">
				        <input type="hidden" id="workflow_taskid" name="workflow_taskid" value="${task.workflow_taskid}">	
					  </c:if>
					  </c:forEach>
					  <tr><td>
						Select File
					</td>
					<td><div class="col-md-8">				
					<input class="form-control" type="file" size="40" name="file"/>
					</div> 
					<div class="col-md-2">
					<input class="btn btn-success" type="submit" name="submit_file" value="Upload" />
					</div>					
					</td>
					
		</tr>	
		<tr><td></td><td><div class="col-md-8"><a href="${path}">${doc_name}</a></div></td></td></tr>
    </form>		
		<form action="<%=request.getContextPath()%>/mytask" method="post">
		
		 <c:forEach items="${taskdetails}" var="task">
					  <c:if test="${data != task.task_name}">		
		              <c:set var="data" value="${task.task_name}"/>
			<input type="hidden" name="process_task_id" value="<c:out value="${task.process_id}-${task.task_id}"/>"/>	
					   	<input type="hidden" id="process_id" name="process_id" value="${task.process_id}">
						<input type="hidden" id="task_id" name="task_id" value="${task.task_id}">
						<input type="hidden" name="mode" value="a"/>
						<input type="hidden" id="workflow_stepid" name="workflow_stepid" value="${task.workflow_stepid}">
						<input type="hidden" name="role" value="${role}">	
						<input type="hidden" id="workflow_id" name="workflow_id" value="${task.workflow_id}">						
					  </c:if>
					  </c:forEach>
		
		<!--<input type="hidden" name="process_task_id" value="<c:out value="${process_id}-${task_id}"/>"/>	
		<input type="hidden" name="process_id" value="<c:out value="${process_id}"/>"/>	
		<input type="hidden" name="task_id" value="<c:out value="${task_id}"/>"/>	
		<input type="hidden" name="mode" value="a"/>
		<input type="hidden" id="workflow_stepid" name="workflow_stepid" value="${stepid}">
		<input type="hidden" name="role" value="${role}"> -->
		 <tr><td>
		Task Results
		</td><td>	
		<div class="col-md-8">
			<select name="status" id="astatus" class="form-control">
			<option value="0">--Select--</option>
			<option value="A">Approved</option>
			<option value="D">Disapproved</option>
			</select>
			</div>
			</td>
			</tr>
			<tr>
			<td></td><td></td>
			</tr>
					<tr>
			<td>
			Task Comment
			</td>	
			<td>
			<div class="col-md-8">
			<textarea name="task_comment" id="task_comment1" class="form-control" rows="5" cols="75"/></textarea>
			</div>
			</td>
			</tr> 
			<tr>
			<td>		
			</td>	
			<td>
			<div class="col-md-3">
             <input type="submit" name="submit_task" id="submit_task1" value="Complete Task" class="btn btn-primary">
			</div>
			</td>
			</tr>
		</table>
</form>
 </c:if>
		<c:if test="${pageMode == 'edit_comment'}">
		 
		 <form action="<%=request.getContextPath()%>/mytask" method="post">
			<input type="hidden" name="mode" value="">
			 <input type="hidden" name="role" value="${role}">
				<c:forEach items="${commentdetails}" var="comments">
					
				<table class="table " style="width:75%"> 
			<tr>
			<td>
			Task Comment
			</td>	
			<td>
			<div class="col-md-8">
			<input type="hidden" name="process_task_id" value="<c:out value="${process_id}-${task_id}"/>"/>	
				<input type="hidden" name="process_id" value="${comments.process_id}">
				<input type="hidden" name="status" value="${comments.status}">	
                 <input type="hidden" name="task_id" value="${task_id}">					
				<textarea id="update_task_comment" name="task_comment" class="form-control" rows="5" cols="75">
					<c:out value="${comments.task_comment}"/>
					</textarea>
					</div>
			</td>
			</tr>
			<tr>
			<td>		
			</td>	
			<td>
			<div class="col-md-3">
             <input type="submit" id="update_submit_task" name="submit_update_comment" value="Update Comments" class="btn btn-primary">
			</div>
			</td>
			</tr>
		</table>
		
				</c:forEach>
			
			
</form>
	</c:if>
	
		<c:if test="${pageMode == 'postpone_task'}">
		
		<form action="<%=request.getContextPath()%>/mytask" method="post">
		<input type="hidden" name="process_task_id" value="<c:out value="${process_id}-${task_id}"/>"/>	
		<input type="hidden" name="process_id" value="${process_id}">
	   <input type="hidden" name="status" value="${status}">	
	   <input type="hidden" name="task_id" value="${task_id}">
	    <input type="hidden" name="role" value="${role}">
		<table class="table " style="width:75%">
	   <tr><td>
		Expected Date
		</td><td>	
		<div class="col-md-8">
			<input class="form-control" style="width:260px;" id="datepicker" type="text" name="deadline_date">
			</div>
			</td>
			</tr>
			<tr>
			<td></td><td></td>
			</tr>
			<tr>
			<td>
			Reason Comments
			</td>	
			<td>
			<div class="col-md-8">
			<textarea id="postpone_task_comment" name="postpone_task_comment" class="form-control" rows="5" cols="75"/></textarea>
			</div>
			</td>
			</tr>
			<tr> 
			<td>		
			</td>	
			<td>
			<div class="col-md-3">
             <input type="submit" id="submit_postpone" name="submit_postpone" value="Postpone Task" class="btn btn-primary">
			</div>
			</td>
			</tr>
		</table>
</form>
		</c:if>
		
		<c:if test="${pageMode == 'changetask_finished'}">
		
		<form action="<%=request.getContextPath()%>/mytask" method="post">
	     <input type="hidden" name="role" value="${role}">
			<input type="hidden" name="process_task_id" value="${process_id}-${task_id}"/>	
				<input type="hidden" name="process_id" value="${process_id}">
			  <!-- <input type="hidden" name="status" value="${status}">	!-->
		   <input type="hidden" name="task_id" value="<c:out value="${task_id}"/>"/>
		   <input type="hidden" name="mode" value="p"/>
		<table class="table " style="width:75%">
	   <tr><td>
		Change Task Results
		</td><td>	
		<div class="col-md-8">
			<select name="status" class="form-control">
			<option value="P">Pending</option>
			</select>
			</div>
			</td>
			</tr>
			<tr>
			<td></td><td></td>
			</tr>
			<tr>
			<td>
			Reason Comment
			</td>	
			<td>
			<div class="col-md-8">
			<textarea name="change_task_comment" id="change_task_comment" class="form-control" rows="5" cols="75"/></textarea>
			</div>
			</td>
			</tr>
			<tr>
			<td>		
			</td>	
			<td>
			<div class="col-md-3">
             <input type="submit" name="submit_change_task" id="submit_change_task" value="Change Complete Task" class="btn btn-primary">
			</div>
			</td>
			</tr>
		</table>
</form>
		</c:if>
		<c:if test="${pageMode == 'show_workflow'}">
		<h1 class="text-danger">Workflow progress tracking step wise</h1>		
		<div id="container">		
			<div class="checkout-wrap">
			<ul class="checkout-bar">
			<c:forEach items="${stepList}" var="step">
			<c:choose> 
			<c:when test="${step.status == 'A'}">			
			<li class="visited first">${step.workflow_step_name}</li>
			</c:when>
			<c:when test="${step.status == 'D'}">			
			<li class="deactive">${step.workflow_step_name}</li>
			</c:when>
			<c:otherwise>
			<li class="next">${step.workflow_step_name}</li>
			</c:otherwise>
			</c:choose>
			</c:forEach>
		</ul>			
		</div><br/><br/><br/><br/><br/><br/>	
		<ul style="list-style-type:square">
		<li>Note:-</li>
		<li style="color:#4682B4;">Complete</li>
		<li style="color:#9CD353;">Process</li>
		<li style="color:#ECECEC;">Not Start</li>
		<li style="color:red;">Disapproved</li>
		</ul>
</div>
		</c:if>
		<c:if test="${pageMode == 'comment_task'}">
		<form action="<%=request.getContextPath()%>/mytask" method="post">
			<table class="table " style="width:75%"> 
			<tr>
			<td>
			Task Comment
			</td>	
			<td>
			<div class="col-md-8">
			<c:forEach items="${commentdetails}" var="comments">
			<input type="hidden" name="process_task_id" value="<c:out value="${process_id}-${task_id}"/>"/>	
			 <input type="hidden" name="role" value="${role}">
			<input type="hidden" name="process_id" value="${comments.process_id}">
			<input type="hidden" name="status" value="${comments.status}">	
			<input type="hidden" name="mode" value="">	
			<input type="hidden" name="task_id" value="${task_id}">	
           </c:forEach>			 
			<textarea name="task_comment" class="form-control" rows="5" cols="75"></textarea>				
			</div>
			</td>
			</tr>
			<tr>
			<td>		
			</td>	
			<td>
			<div class="col-md-3">
             <input type="submit" name="submit_update_comment" value="Comments Task" class="btn btn-primary">
			</div>
			</td>
			</tr>
		</table>
   </form>
		</c:if>
		<c:if test="${pageMode == 'attachment_file'}">
	   <form enctype="multipart/form-data" action="<%=request.getContextPath()%>/attachment" method="post">
	                  <c:forEach items="${taskdetails}" var="task">
					  <c:if test="${same != task.task_name}">		
		              <c:set var="same" value="${task.task_name}"/>
					   <input type="hidden" name="process_task_id" value="<c:out value="${process_id}-${task_id}"/>"/>	
					   	<input type="hidden" id="task_id" name="task_id" value="${task.task_id}">
						<input type="hidden" id="process_id" name="process_id" value="${task.process_id}">
						<input type="hidden" id="st" name="status" value="${task.status}">
						<input type="hidden" id="item_id" name="item_id" value="${task.item_id}">
						<input type="hidden" id="workflow_id" name="workflow_id" value="${task.workflow_id}">
						<input type="hidden" id="handle" name="handle" value="${task.handle}">					
						<input type="hidden" id="workflow_stepid" name="workflow_stepid" value="${task.workflow_stepid}">
				        <input type="hidden" id="workflow_taskid" name="workflow_taskid" value="${task.workflow_taskid}">	
					  </c:if>
					  </c:forEach>
					  <input type="hidden" id="filemode" name="filemode" value="1">
	 <br/><br/>
        <table class="table " style="width:75%">
		<tr>
		<td>
    <label>Select File</label>
			</td>
		<td>
		<div class="container row col-md-6">        	
            <input class="form-control" type="file" size="40" name="file"/>
        </div>
        </td>
		</tr>
		<tr>
			<td>     			
			</td>	
			<td>
			<div class="container row col-md-4">
		    <input class="btn btn-success" type="submit" name="submit_file" value="Attachment File" />
		</div>
		</td>
		</tr>
		</table>
    </form>
	 </c:if>
	 <c:if test="${pageMode =='show_associate_task'}">
	 
	 <c:if test="${not empty successMessage}">
		    <div class="col-md-9 alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			 <strong>Success! :- </strong><c:out value="${successMessage}"/>
			 </div>
			 </c:if>
		   <c:if test="${not empty errorMessage}">
		     <div class="col-md-9 alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			<strong>Error! :- </strong>
			  <c:out value="${errorMessage}"/>
			 </div>
		    </c:if>
			
	  <form action="<%=request.getContextPath()%>/mytask" method="post">
	   <table  class="table " style="width:75%">
	   <tr><td><label>Document Name</label></td>
		<td>
		<div class="col-md-6">   
		<c:forEach items="${taskdetails}" var="task"> 
			<c:if test="${same != task.task_name}">		
		  <c:set var="same" value="${task.task_name}"/>    	
          <c:forEach items="${task.documentList}" var="document" varStatus="documentStatus">
         <li style="list-style-type:none"><c:out value="${documentStatus.count}."/>			 
		<a id="doc" target="_blank" onclick="return documentread();" href="<c:out value="${document.key}"/>"><c:out value="${document.value}"/></a>
		</li>
		 <input type="hidden" name="file_description" value="<c:out value="${task.document_id}"/>#<c:out value="${task.bitstream_id}"/>#<c:out value="${document.value}"/>#<c:out value="${document.key}"/>"/> 
		 <input type="hidden" name="process_task_id" value="${task.process_id}-${task.task_id}">
		<input type="hidden" name="item_id" value="<c:out value="${task.item_id}"/>"> 
		<input type="hidden" name="handle" value="<c:out value="${task.handle}"/>">
		<input type="hidden" name="status" value="none">
		</c:forEach>
		</c:if>
		   </c:forEach>
        </div>
		</td></tr>
		<tr><td><label>Select Workflow</label></td>
		<td>
		<div class="col-md-6">        	
            <select class="form-control" name="workflowId" id="w_id">
					<option value="1">Adhoc Workflow</option>					
				</select>
        </div>
		</td></tr>
		
		<tr><td><label>Task Name</label></td>
		<td>
		<div class="col-md-6">        	
           <input class="form-control" styles="width:5px;" name="task_name" id="task_name" size="5" />
        </div>
		</td></tr>
		
		<tr><td><label>User Assign</label></td>
		<td>
		<div class="col-md-10">        	
           <div class="col-xs-4">
	<h4 class="text-primary">User</h4>
		<select name="from[]" id="undo_redo"  onblur="return taskrule();" class="form-control" size="9" multiple="multiple">
			 <c:forEach items="${userlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
		</select>
	</div>
	
	<div class="col-xs-2">
	<h4 class="text-primary">&nbsp;</h4>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-forward"></i></button>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
		<button type="button" id="undo_redo_leftSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
		<button type="button" id="undo_redo_leftAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-backward"></i></button>		
	</div>
	
	<div class="col-xs-4">
	<h4 class="text-primary">Assign to</h4>
		<select name="assign_user_id" id="undo_redo_to"  class="form-control" size="9"  multiple="true">
		</select>
	</div>
        </div>
		<script type="text/javascript">
jQuery(document).ready(function($) {
	$('#undo_redo').multiselect();
});

</script>
		</td></tr>
		
		<tr><td><label>Supervisors</label></td>
		<td>
		<div class="col-md-6">        	
           <select class="form-control" name="supervisor_id" id="tlanguage">
			<option value="0">-Select Supervisor-</option>  
			<c:forEach items="${userlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
			
			</select>
        </div>
		</td></tr>
		<tr><td><label>Priorty</label></td>
		<td>
		<div class="col-md-6">        	
           <select class="form-control" name="priorty" id="tpriorty">
          	 <option value="normal">Normal</option>
			<option value="medium">Medium</option>
          	<option value="urgent">Urgent</option>          	
			</select>
        </div>
		</td></tr>
		<tr><td><label>Task Instructions</label></td>
		<td>
		<div class="col-md-6">        	
            <textarea class="form-control" id="task_instruction" name="task_instruction" size="10" col="5" row="20"></textarea>
        </div>
		</td></tr>
		<tr><td><label>Deadline Date</label></td>
		<td>
		<div class="col-md-6">        	
       <input class="form-control" onblur="dateValidate(this);" placeholder="MM/DD/YYYY" name="deadline_day" id="datepicker" />
       
	   </div>
		</td></tr>
		<tr><td><label>Deadline Time</label></td>
		<td>		   
     <div class="col-md-2">		     	
       <input class="form-control" style=" padding:0 0 0em 0.5em;" name="deadline_time" placeholder="00:00" id="tdeadline_time" size="5" />
	    </div>
	   <div class="col-md-5" style="width:100px">
       <select class="form-control" name="timemode" id="tpriorty">
          	 <option value="A.M">A.M</option>
			<option value="P.M">P.M</option>        	
			</select>
			</div>	   
		</td></tr>
		
		<tr><td><label>Task permissions</label></td>
		<td>
		<div class="col-md-2">
			<input type="checkbox" onclick="disableChk();" id="task_permission_0" name="task_permission_id" value="1"/> &nbsp; All Step
			</div>			
			<div class="col-md-3">
			<input type="checkbox" id="task_permission_2" name="task_permission_id" value="2"/> &nbsp; Edit Comment
			</div>
			<div class="col-md-3">
			<input type="checkbox" id="task_permission_3" name="task_permission_id" value="3"/> &nbsp; Postpone Task
			</div>	
			<div class="col-md-4">
			<input type="checkbox" id="task_permission_4" name="task_permission_id" value="4"/> &nbsp; Change Task Finished
			</div>
		</td></tr>
		
		<tr><td><label>Task Requirement</label></td>
		<td>
		<div class="col-md-2">
			<input type="hidden" name="task_requirement_id" value="1"/>
			<input type="checkbox" disabled="true" checked name="task_requirement_id" value="1"/>&nbsp; Read 
			</div>
			<div class="col-md-3">
			<input type="checkbox" name="task_requirement_id" value="2"/> &nbsp; Comment 
			</div>
			<div class="col-md-2">
			<input type="checkbox" name="task_requirement_id" value="3"/> &nbsp; Approve 
			</div>
		</td></tr>
		
		<tr>
			<td>     			
			</td>	
			<td>
			<div class="container row col-md-4">
		    <input class="btn btn-success" type="submit" name="submit_task_save" value="Create Task" />
		</div>
		</td>
		</tr>
		</table>
		</form>
	 </c:if>
	  <c:if test="${pageMode =='show_reassign_task'}">
	   <form action="<%=request.getContextPath()%>/mytask" method="post">
	   <c:forEach items="${taskdetails}" var="task" begin="0" end="0">
	   <input type="hidden" name="task_id" value="${task.task_id}">
	   <input type="hidden" name="deadline_day" value="${task.due_Date}">
	    <input type="hidden" name="process_task_id" value="${task.process_id}-${task.task_id}">
	   </c:forEach>
	  <table class="table" style="width:75%"> 
		<tr><td><label>User Assign</label></td>
		<td>
		<div class="col-md-10">        	
           <div class="col-xs-4">
	<h4 class="text-primary">User</h4>
		<select name="from[]" id="undo_redo"  onblur="return taskrule();" class="form-control" size="9" multiple="multiple">
			 <c:forEach items="${userlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
		</select>
	</div>
	
	<div class="col-xs-2">
	<h4 class="text-primary">&nbsp;</h4>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-forward"></i></button>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
		<button type="button" id="undo_redo_leftSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
		<button type="button" id="undo_redo_leftAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-backward"></i></button>		
	</div>
	
	<div class="col-xs-4">
	<h4 class="text-primary">Assign to</h4>
		<select name="assign_user_id" id="undo_redo_to"  class="form-control" size="9"  multiple="true">
		</select>
	</div>
        </div>
		<script type="text/javascript">
jQuery(document).ready(function($) {
	$('#undo_redo').multiselect();
});

</script>
		</td></tr>
		<tr>
			<td>     			
			</td>	
			<td>
			<div class="container row col-md-4">
		    <input class="btn btn-success" type="submit" name="submit_reassign_task_save" value="Reassign Task" />
		</div>
		</td>
		</tr>
		</table>
		</form>
	   </c:if>	 
	 
</dspace:layout>