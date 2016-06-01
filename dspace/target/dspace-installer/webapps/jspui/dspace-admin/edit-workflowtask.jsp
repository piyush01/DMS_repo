
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
				<script>
				
				function dateValidate(deadline) {
				var re=/^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\d\d+$/;
				document.getElementById('lblError').innerHTML = '';
				if (deadline.value != '') {

				if(re.test(deadline.value ))
				{
				birthdayDate = new Date(deadline.value);
				dateNow = new Date();
				}
				else
				{
				document.getElementById('lblError').innerHTML = 'Date must be mm/dd/yyyy format';
				$('#datepicker').val("");
				$('#datepicker').focus();
				return false;
				}
				}
				}
				
				
				
			function disableChk(){				
			
			if($("#task_permission_0").prop('checked') == true){
				//$("input[name='task_permission_id']").prop("disabled", true);
				$("#task_permission_1").prop("disabled", true);
                $("#task_permission_2").prop("disabled", true);	
                $("#task_permission_3").prop("disabled", true);	
                $("#task_permission_4").prop("disabled", true);					
				return true;
			}else{
			   $("#task_permission_1").prop("disabled", false);
                $("#task_permission_2").prop("disabled", false);	
                $("#task_permission_3").prop("disabled", false);	
                $("#task_permission_4").prop("disabled", false);		
			}
				
			}
				function taskrule(){
					var str=$("#undo_redo_to").val();					
					 var abc=str.toString().split(",");
					 var len=abc.length
					 if(len==1){
						 $("#task_rule option[value='2']").remove();
						 $('#task_rule').append( new Option("One user is enough to complete the task","1") );						
					 }else if(len>=2){
						 $("#task_rule option[value='1']").remove();
						 $('#task_rule').append( new Option("More than one user is to complete the task","2") );
					 }else{						 
						   $("#task_rule option[value='1']").remove();
						    $("#task_rule option[value='2']").remove();
						 $('#task_rule').append( new Option("Not Define","0") );
					 }
					
				return true;	
				}
				
				
	function sendRequest(url, target) {			
	$('#' + target).empty();
	$.ajax({
		url : url,
		success : function(result) {
			
			var name = result.getElementsByTagName("name");
			var id = result.getElementsByTagName("id");
			var length = name.length;
			
			$('#' + target).append('<option value="0">--Select--</option>');
			for ( var i = 0; i < length; i++) {
				$('#' + target).append(
						'<option value="' + id[i].childNodes[0].nodeValue
								+ '">' + name[i].childNodes[0].nodeValue
								+ '</option>');
			}
		}
	});
}
				</script>
				 <script type="text/javascript">      
	  $(document).ready(function()
      {   
		$('#taskform').submit(function() {		
		if ($.trim($('#task_name').val()) == "") {
		$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter task name.</div>');			
		$('#task_name').focus();
     return false;
	 }		
	 /*else if($('#undo_redo_to').val()==null || $('#undo_redo_to').val()==""){
				$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please assign user.</div>');			
		$('#undo_redo_to').focus();
		return false;
			}	*/
			
	else if($('#datepicker').val()== ""){
				$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter dead date.</div>');			
		$('#datepicker').focus();
		return false;
			}
			
		else if($('#task_instruction').val()== ""){
				$('#taskalert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter task instruction.</div>');			
		$('#task_instruction').focus();
		return false;
			}			
     });
	 });	 
	 
	 //-----------------datepicker---------------
  $(function() {
    $( "#datepicker" ).datepicker();
  }); 
</script>

	<div align="right">
<a class="btn btn-primary" href="<%=request.getContextPath()%>/dspace-admin/
workflow-step?workflowId=${taskMasterBean.workflow_id}">Back</a>
	</div>
<div style="margin-left:10%; class="container">  
<table style="width:100%">   
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
<form id="taskform" action="<%=request.getContextPath()%>/dspace-admin/add-task" name="workflow_task" method="post"> 
	<div class="alert alert-info"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter the following information.  The fields marked with a * are required.</div>
	<div id="taskalert"></div>
	<input type="hidden" name="workflowId" value="<c:out value="${taskMasterBean.workflow_id}"/>"/>
	<input type="hidden" name="step_id" value="<c:out value="${taskMasterBean.step_id}"/>"/>
<input type="hidden" name="task_id" value="<c:out value="${taskMasterBean.task_id}"/>"/>
	
	<!--
	
	<div class="row">
                <label class="col-md-2" for="tlastname">Select workflow <span class="star">*</span></label>
            		<div class="col-md-5">
	<select onchange="sendRequest('<%=request.getContextPath()%>/dspace-admin/add-task?action=ajaxlist&workflow_id='+this.value, 'step_id');" class="form-control" name="workflowId" id="workflow_id">
			<option value="0">-Select Workflow-</option>
			<c:forEach items="${workflows}" var="workflow">
			<option value="<c:out value="${workflow.workflow_id}"/>"><c:out value="${workflow.workflow_name}"/></option>
			</c:forEach>
			</select>
			 </div>
 <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div></br>
 </div>
 		<div class="row">
                <label class="col-md-2" for="tlastname">Select step <span class="star">*</span></label>
            <div class="col-md-5">
				<select class="form-control" name="step_id" id="step_id">
			 <option value="0">-Select step-</option>
          	 <c:forEach items="${workflowstep}" var="step">
			<option value="<c:out value="${step.workflow_step_id}"/>"><c:out value="${step.workflow_step_name}"/></option>
			</c:forEach>	
			</select>			 
			</div>
			</div>!-->
			
	   <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
	   </br>
	   <div class="row">
                <label class="col-md-2" for="tlastname">Task Name <span class="star">*</span></label>
            <div class="col-md-5">
<input class="form-control" value="<c:out value="${taskMasterBean.task_name}"/>" name="task_name" id="task_name" size="12" />
			</div>
       </div>
	   <br>
	   <div class="row">
                <label class="col-md-2" for="tlastname">Step Number</label>
            <div class="col-md-5">
				<input class="form-control" type="hidden" name="step_no" value="<c:out value="${taskMasterBean.step_no}"/>" /><b><c:out value="${taskMasterBean.step_no}"/></b>			
			
			</div>
       </div>  
	   		<div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
		<div class="row">
<label class="col-md-2" for="tlastname">User Assign <span class="star">*</span></label>
	<div class="col-xs-2">
	<h4 class="text-primary">User</h4>
		<select disabled="true" name="from[]" id="undo_redo"  onblur="return taskrule();" class="form-control" size="13" multiple="multiple">
			 <c:forEach items="${userlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
		</select>
	</div>
	
	<div class="col-xs-1">
	<h4 class="text-primary">&nbsp;</h4>
		<button type="button" id="undo_redo_undo" class="btn btn-primary btn-block">undo</button>
		<button type="button" disabled="true"  onblur="return taskrule();" id="undo_redo_rightAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-forward"></i></button>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
		<button type="button" id="undo_redo_leftSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
		<button type="button" disabled="true"  id="undo_redo_leftAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-backward"></i></button>
		<button type="button" id="undo_redo_redo" class="btn btn-warning btn-block">redo</button>
	</div>
	
	<div class="col-xs-2">
	<h4 class="text-primary">Assign to</h4>
		<select disabled="true" name="assign_user_id" id="undo_redo_to"  class="form-control" size="13"  multiple="true">
		<c:forEach items="${taskuserlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
		</select>
	</div>
	</div>
	<script type="text/javascript">
jQuery(document).ready(function($) {
	$('#undo_redo').multiselect();
});

</script>
	
	
 <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
 </br>
	
	    <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
		</br>
	    <div class="row">
                <label class="col-md-2" for="ttask_rule_id">Task Rule <span class="star">*</span></label>
            <div class="col-md-5">
			<!--<select class="form-control" name="task_rule_id" id="task_rule">			        	
          	</select>!-->
		
			 
			<select class="form-control" name="task_rule_id" id="task_rule">
			
			<c:if test="${taskMasterBean.task_rule_id == 1}">
			<option value="1">One user is enough to complete the task</option>
			</c:if>
			<c:if test="${taskMasterBean.task_rule_id == 2}">
			<option value="2">More than one user is to complete the task</option>
			</c:if>
          	</select>
			
			
			</div>
       </div>
	    <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
		</br>
	   <div class="row">
                <label class="col-md-2" for="tlastname">Priorty</label>
            <div class="col-md-5">
				<select class="form-control" name="priorty" id="tpriorty">				
				<c:if test="${taskMasterBean.priorty == 'normal'}">
				 <option value="normal">Normal</option>
				  <option value="medium">Medium</option>
				<option value="urgent">Urgent</option>
				 </c:if>
				 <c:if test="${taskMasterBean.priorty == 'medium'}">
				<option value="medium">Medium</option>
				<option value="normal">Normal</option>			  
				<option value="urgent">Urgent</option>
				 </c:if>
				 <c:if test="${taskMasterBean.priorty == 'urgent'}">
				 <option value="urgent">Urgent</option>
				 <option value="normal">Normal</option>
				  <option value="medium">Medium</option>          	
				 </c:if>          	
			</select>
			</div>
       </div>
	    <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
		</br>
	    <div class="row">
                <label class="col-md-2" for="tdeadline_day">Deadline Date<span class="star">*</span></label>
            <div class="col-md-2">
				<input class="form-control" value="${taskMasterBean.date}" onblur="dateValidate(this);" placeholder="MM/DD/YYYY" name="deadline_day" id="datepicker" />
			</div>
			<label class="col-md-1" for="tdeadline_time">Time:-</label>
			 <div class="col-md-1">			 
				<input class="form-control" value="${taskMasterBean.deadline_time}" style=" padding:0 0 0em 0.5em;" name="deadline_time" placeholder="00:00" id="tdeadline_time" size="5" />				 
			</div>
			<div class="col-md-1" style="width:100px">
			<select class="form-control" name="timemode" id="tpriorty">
			 <option value="<c:out value="${taskMasterBean.timemode}"/>"><c:out value="${taskMasterBean.timemode}"/></option>
			<option value="P.M">P.M</option>        	
			</select>
			</div>
			 <span style="color: Red"><label ID="lblError"></label></span>
       </div><br>
	    <div class="row"><label class="col-md-2" for="tdeadline_time"></label><div class="col-md-5"></div></div>
		</br>
	   <div class="row">
	   <label class="col-md-2" for="lastname">Task Instructions <span class="star">*</span></label>
	   <div class="col-md-5">
	   <textarea class="form-control" id="task_instruction" name="task_instruction" size="10" col="5" row="20">
	   <c:out value="${taskMasterBean.task_instructions}"/>
	   </textarea>
			</div>
			</div><br>
			 <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
			 </br>
			<div class="row">
			<label class="col-md-2" for="supervisor">Supervisors</label>
			<div class="col-md-5">
			<select class="form-control" name="supervisor_id" id="tlanguage">
			<option value="<c:out value="${taskMasterBean.supervisor_id}"/>">
			<c:out value="${taskMasterBean.user_name}"/>
			</option>  
			 <c:forEach items="${supervisorList}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
			</select>
			</div>
			</div>
			
		</br>
			<div class="row">
			<label class="col-md-2" for="taskpermissions">Task permissions</label>
			<div class="col-md-2">
			<c:choose>
			<c:when test="${taskMasterBean.p1 == '1'}">
			<input type="checkbox" checked onclick="disableChk();" id="task_permission_0" name="task_permission_id" value="1"/>
			</c:when>
			<c:otherwise>
			<input type="checkbox" onclick="disableChk();" id="task_permission_0" name="task_permission_id" value="1"/>
			</c:otherwise>
		  </c:choose>
			&nbsp; All Step
			</div>			
			<div class="col-md-2">
			<c:choose>
			<c:when test="${taskMasterBean.p2 == '2'}">
			  <input type="checkbox" checked id="task_permission_2" name="task_permission_id" value="2"/> 
			</c:when>
			<c:otherwise>
			<input type="checkbox" id="task_permission_2" name="task_permission_id" value="2"/> 			
			</c:otherwise>
		  </c:choose>
		  	&nbsp; Edit Comment
			</div>
			<div class="col-md-2">
			<c:choose>
			<c:when test="${taskMasterBean.p3 == '3'}">
			  <input type="checkbox" checked id="task_permission_3" name="task_permission_id" value="3"/>	
			</c:when>
			<c:otherwise>
			<input type="checkbox" id="task_permission_3" name="task_permission_id" value="3"/>	
			</c:otherwise>
		  </c:choose>
			 &nbsp; Postpone Task
			</div>	
			</div>			
			<div class="row">
			<label class="col-md-2" for="taskpermissions"></label>
				
			<div class="col-md-3">
			<c:choose>
			<c:when test="${taskMasterBean.p4 == '4'}">
			  <input type="checkbox" id="task_permission_4" checked name="task_permission_id" value="4"/>	
			</c:when>
			<c:otherwise>
			<input type="checkbox"  name="task_permission_id" value="4"/>	
			</c:otherwise>
		  </c:choose>
			&nbsp; Change Task Finished
			</div>
			</div>
			</br>
			 <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
				<div class="row">
			<label class="col-md-2" for="taskpermissions">Task Requirement</label>
			
			<div class="col-md-2">
			<input type="hidden" name="task_requirement_id" value="1"/>
			<input type="checkbox" disabled="true" checked name="task_requirement_id" value="1"/>&nbsp; Read 
			</div>
			<div class="col-md-2">
			<c:choose>
			<c:when test="${taskMasterBean.r2 == '2'}">
			  <input type="checkbox" checked name="task_requirement_id" value="2"/>	
			</c:when>
			<c:otherwise>
			<input type="checkbox" name="task_requirement_id" value="2"/>	
			</c:otherwise>
		  </c:choose>
			&nbsp; Comment 
			</div>
			<div class="col-md-2">
			<c:choose>
			<c:when test="${taskMasterBean.r3 == '3'}">
			 <input type="checkbox" checked name="task_requirement_id" value="3"/>
			</c:when>
			<c:otherwise>
			<input type="checkbox" name="task_requirement_id" value="3"/>	
			</c:otherwise>
		  </c:choose>
			 &nbsp; Approve 
			</div>		
			<!-- <div class="col-md-2">
			<input type="checkbox" name="task_requirement_id" value="3"/>Edit
			</div>
			<div class="col-md-2">
			<input type="checkbox" name="task_requirement_id" value="4"/>Active
			</div>
			-->
			</div>
			 <div class="row"><label class="col-md-2" for="tlastname"></label><div class="col-md-5"></div></div>
			<div class="row">
			<label class="col-md-2" for="taskpermissions"></label>
			  <div class="col-md-5">
			  <input class="btn btn-success" type="submit" name="submit_update" value="Update Task">
			  </div>
			</div>
			</form>
	   </div>
</dspace:layout>