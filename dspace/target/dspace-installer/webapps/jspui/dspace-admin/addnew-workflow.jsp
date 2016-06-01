<%--
 - Workflow editor - for new workflow
  -
  - Attributes:
  -   workflow - workflow to be added
  -
  - Returns:
  -   submit_save   - admin wants to save 
  -   workflow_name		  - (String)
--%>

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport"%>
<%@ page import="org.dspace.core.ConfigurationManager"%>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace"%>

<dspace:layout style="submission"
	titlekey="jsp.dspace-admin.eperson-main.title" navbar="admin"
	locbar="link" parenttitlekey="jsp.administer"
	parentlink="/dspace-admin">
			
	 <script type="text/javascript">      
	  $(document).ready(function()
      {   
		$('#workflowform').submit(function() {
		if ($.trim($('#tworkflow_name').val()) == "") {
		$('#workflow-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter workflow name.</div>');			
		$('#tworkflow_name').focus();
     return false;
	 }
     });
	 });	 
	</script>
	<div class="col-md-3">
	<div class="panel panel-primary" style="height:420px;background:#428bca">
			<div class="panel-heading">Admin Tools</div>
			<div class="panel-body">
				<form method="post"  action="<%=request.getContextPath()%>/dspace-admin/new-workflow">
					<input type="hidden" name="community_id" value="2"> <input
						type="hidden" name="action" value="1"> 
						<a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/new-workflow">New workflow</a>
				</form>

				<form method="post" action="/jspui/tools/collection-wizard">
		     		<input type="hidden" name="community_id" value="2">
                   <a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/add-workflow">workflow List</a>
                </form>

				<form method="post" action="/jspui/mydspace">
					<input type="hidden" name="community_id" value="2"> 
						<a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/workflow-properties">properties</a>
				</form>

				<form method="post" action="/jspui/dspace-admin/metadataexport">
					<input type="hidden" name="handle" value="123456789/3">  
					<a class="btn btn-primary col-md-12" href="<%=request.getContextPath()%>/dspace-admin/workflow-step">Workflow Step</a>	
				</form>

			</div>
		</div>
		</div>
		<form id="workflowform" name="new-workflow" method="post" action="">   
<div class="container">
 
<h3><fmt:message key="jsp.dspace-admin.workflow-add.heading"/></h3>
<table style="width:67%">   
  <tr><th colspan="3"> 
<div id="workflow-alert"></div>
</th></tr></table>
		<div class="row">           	
            <label class="col-md-2" for="tworkflow_name"><fmt:message key="jsp.dspace-admin.workflow-add.name"/> <span class="star">*</span></label>
            <div class="col-md-5">
				<input class="form-control" name="workflow_name" value="<c:out value="${workflowMasterBean.workflow_name}"/>" id="tworkflow_name" size="15" />			
			</div><br>
			<br/><br/> <label class="col-md-2" for="tworkflow_name">Workflow Description</label>
			<div class="col-md-5">
			  <textarea class="form-control" id="task_instruction" name="workflow_description" size="10" col="5" row="20">
			  <c:out value="${workflowMasterBean.workflow_description}"/>
			  </textarea>
						
			</div>
			
			<label class="col-md-2" for="tworkflow_name"> </label>
			<div class="col-md-5 btn-group">
				</br>
			<label class="col-md-5" for="tworkflow_name"></label>
			<c:choose> 
			 <c:when test="${action == 'edit'}">			  
 <input type="hidden" name ="workflowId" value="<c:out value="${workflowMasterBean.workflow_id}"/>"/>			  
				 <input id="create" type="submit" class="btn btn-success" name="submit_update" value="Update Workflow "/>
			  </c:when>
			  <c:otherwise>
			<input id="create"  type="submit" class="btn btn-success" name="submit_add" value="Create Workflow "/>
			  </c:otherwise>
			</c:choose>	 		 						
		</div>
       </div>    
	</div>
	</form>
</dspace:layout>