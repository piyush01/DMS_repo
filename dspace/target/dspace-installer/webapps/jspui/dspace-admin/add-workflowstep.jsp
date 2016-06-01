<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.eperson-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
			   
			   <script type="text/javascript">      
	  $(document).ready(function()
      {   
		$('#workflowstepform').submit(function() {
			 if($('#workflowId').val()=="0"){
		$('#workflowstep-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please select workflow.</div>');			 
		$('#workflowId').focus();
     return false;
	 }
		else if ($.trim($('#workflow_step_name').val()) == "") {
		$('#workflowstep-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter workflow name.</div>');			
		$('#workflow_step_name').focus();
     return false;
	 }	 
     });
	 });	 
	</script>
			   
<div align="right"><a class="btn btn-warning" href="<%=request.getContextPath()%>/dspace-admin/workflow-step?workflowId=<c:out value="${workflowId}"/>">Back</a></div>

<div class="container">   

<table style="width:67%">   
  <tr><th colspan="3"> 
<div id="workflowstep-alert"></div>
</th></tr></table>
<form id="workflowstepform" method="post" action="<%=request.getContextPath()%>/dspace-admin/add-step">
<div class="row">
<input type="hidden" name ="workflowId" value="<c:out value="${workflowId}"/>"/>
 <label class="col-md-2">Step name <span class="star">*</span></label>  
 <div class="col-md-4">
<input class="form-control" size="10" id="workflow_step_name" type="text" name="workflow_step_name" value="<c:out value="${workflowStepBean.workflow_step_name}"/>"/>
</div>
</div>
<br/>
<div class="row">
 <label class="col-md-2">Step Description </label>  
 <div class="col-md-4">
  <textarea class="form-control" id="step_description" name="step_description" size="10" col="5" row="20">	 
			  <c:out value="${workflowStepBean.step_description}"/>
			  </textarea>
</div>
</div>
</div>
<div class="row">
<label class="col-md-2" for="tworkflow_name"></label>
 <div class="col-md-6">
 <br/>
 
 	<c:choose> 
			  <c:when test="${action == 'edit'}">		
      <input type="hidden" name ="workflow_step_id" value="<c:out value="${workflowStepBean.workflow_step_id}"/>"/>			  
				 <input  type="submit" required="true" class="btn btn-success" name="submit_update" value="Update Step "/>
			  </c:when>
			  <c:otherwise>
				<input  type="submit" class="btn btn-success" name="submit_add" value="Add Step "/>
			  </c:otherwise>
			</c:choose>	
 </br></br>
</div>
</form>

</div>
</dspace:layout>