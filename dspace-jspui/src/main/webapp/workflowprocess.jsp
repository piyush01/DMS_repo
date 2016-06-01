
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>
<%@ page import="org.dspace.content.Bundle" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.Metadatum" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%
    // Attributes
    
    Item item = (Item) request.getAttribute("item");
    Collection[] collections = (Collection[]) request.getAttribute("collections");  
    // get the handle if the item has one yet
    String handle = item.getHandle();   
    // Full title needs to be put into a string to use as tag argument
    String title = "";
    if (handle == null)
 	{
		title = "Workspace Item";
	}
	else 
	{
		Metadatum[] titleValue = item.getDC("title", null, Item.ANY);
		if (titleValue.length != 0)
		{
		 title = titleValue[0].value;
		}
		else
		{
			title = "Item " + handle;
		}
	}    
    
   
%>
	 
<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>

<dspace:layout title="<%= title %>">
<script type="text/javascript">
 function formvaliate()
     {   
		var chk=false;
		var x = document.forms["workflowform"]["workflowId"].value;
		var chks = document.getElementsByName('file_description');
		
		if(x=="0"){
			document.getElementById('errfn').innerHTML="<b>Please select workflow.</b>";
			document.forms["workflowform"]["workflow_id"].focus();
			return false;
		}	
		else if(x=="1"){
			 $("#hoc").show();
			 return false;
		}
		 else if(isCheck(chks)==false)
		 {
		 document.getElementById('errfn').innerHTML="";
		 document.getElementById('errChk').innerHTML="<b>Please check minimum one checkbox.</b>";
			 return false;
		 }
		else{
			 document.getElementById('errfn').innerHTML="";
		 document.getElementById('errChk').innerHTML="";
			return true;
		}
		return false;
	}
	
	function isCheck(chks)
	{
		var hasChecked = false;
		for (var i = 0; i < chks.length; i++)
		{
		if (chks[i].checked)
		{
		hasChecked = true;
		break;
		}
		}
		
		return hasChecked;
	}
	
	function checkAll(ele) {
		 var checkboxes = document.getElementsByTagName('input');
		 if (ele.checked) {
			 for (var i = 0; i < checkboxes.length; i++) {
				 if (checkboxes[i].type == 'checkbox') {
					 checkboxes[i].checked = true;
				 }
			 }
		 } else {
			 for (var i = 0; i < checkboxes.length; i++) {
				 console.log(i)
				 if (checkboxes[i].type == 'checkbox') {
					 checkboxes[i].checked = false;
				 }
			 }
		 }
	 }	
	
	jQuery(document).ready(function($)
	{ 
	
	  $("#hoc").hide();
		$( "#datepicker" ).datepicker();
				
		$("#w_id").click(function(){
			var ss=$("#w_id").val();			
			if(ss=="1"){
				  $("#hoc").show();
			}else{
				  $("#hoc").hide();
			}
			return false;
			
		})	
	 });
 
</script>

<form method="post" action="<%=request.getContextPath()%>/workflow-process" name="workflowform">
				
				<c:if test="${not empty message}">
		      <div class="alert alert-success">
			  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			   <c:out value="${message}" />
		        </div>
		   </c:if>
				
		
			<div class="row">
			
			<input type="hidden" name="item_id" value="<%= item.getID() %>"> 
			<input type="hidden" name="handle" value="<%= handle %>">
			<input type="hidden" name="status" value="dsfsd">
			<label class="col-md-2" for="tlastname">Select Workflow <span
				class="star">*</span></label>
			<div class="col-md-4">
					<select class="form-control" name="workflowId" id="w_id">
					<option value="0">-Select Workflow-</option>
					<c:forEach items="${workflowlist}" var="workflow">
						<option value="<c:out value="${workflow.workflow_id}"/>"><c:out
								value="${workflow.workflow_name}" /></option>
					</c:forEach>
				</select>
				</div>
				<div class="col-md-2"><span class="star" id="errfn"></span></div>
		</div>
		<div class="col-md-4"><span class="star" id="errChk"></span></div><br>
		
		<div id="hoc">
		<div class="row">
                <label class="col-md-2" for="tlastname">Task Name <span class="star">*</span></label>
            <div class="col-md-4">
				<input class="form-control" styles="width:5px;" name="task_name" id="task_name" size="5" />
			</div>
       </div></br>
	   <div class="row">
<label class="col-md-2" for="tlastname">User Assign <span class="star">*</span></label>
	<div class="col-xs-2">
	<h4 class="text-primary">User</h4>
		<select name="from[]" id="undo_redo"  onblur="return taskrule();" class="form-control" size="13" multiple="multiple">
			 <c:forEach items="${userlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
		</select>
	</div>
	
	<div class="col-xs-1">
	<h4 class="text-primary">&nbsp;</h4>
		<button type="button" id="undo_redo_undo" class="btn btn-primary btn-block">undo</button>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-forward"></i></button>
		<button type="button" onblur="return taskrule();" id="undo_redo_rightSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
		<button type="button" id="undo_redo_leftSelected" class="btn btn-default btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
		<button type="button" id="undo_redo_leftAll" class="btn btn-default btn-block"><i class="glyphicon glyphicon-backward"></i></button>
		<button type="button" id="undo_redo_redo" class="btn btn-warning btn-block">redo</button>
	</div>
	
	<div class="col-xs-2">
	<h4 class="text-primary">Assign to</h4>
		<select name="assign_user_id" id="undo_redo_to"  class="form-control" size="13"  multiple="true">
		</select>
	</div>
	</div>
	<script type="text/javascript">
jQuery(document).ready(function($) {
	$('#undo_redo').multiselect();
});

</script></br>
<div class="row">
			<label class="col-md-2" for="supervisor">Supervisors</label>
			<div class="col-md-5">
			<select class="form-control" name="supervisor_id" id="tlanguage">
			<option value="0">-Select Supervisor-</option>  
			 <c:forEach items="${userlist}" var="user">
			<option value="<c:out value="${user.user_id}"/>"><c:out value="${user.user_name}"/></option>
			</c:forEach>
			</select>
			</div>
			</div></br>
	    <div class="row">
                <label class="col-md-2" for="tlastname">Priorty</label>
            <div class="col-md-4">
				<select class="form-control" name="priorty" id="tpriorty">
          	 <option value="normal">Normal</option>
			<option value="medium">Medium</option>
          	<option value="urgent">Urgent</option>
          	
			</select>
			</div>
       </div></br>
	   <div class="row">
               <label class="col-md-2" for="lastname">Task Instructions <span class="star">*</span></label>
	   <div class="col-md-4">
	   <textarea class="form-control" id="task_instruction" name="task_instruction" size="10" col="5" row="20"></textarea>
			</div>
       </div></br>
	   <div class="row">
                <label class="col-md-2" for="tdeadline_day">Deadline Date<span class="star">*</span></label>
            <div class="col-md-2">
				<input class="form-control" onblur="dateValidate(this);" placeholder="MM/DD/YYYY" name="deadline_day" id="datepicker" />
			</div>
			<label class="col-md-1" for="tdeadline_time">Time:-</label>
			 <div class="col-md-1">			 
			<input class="form-control" style=" padding:0 0 0em 0.5em;" name="deadline_time" placeholder="00:00" id="tdeadline_time" size="5" />				 
			</div>
			<div class="col-md-1" style="width:100px">
			<select class="form-control" name="timemode" id="tpriorty">
          	 <option value="A.M">A.M</option>
			<option value="P.M">P.M</option>        	
			</select>
			</div>
			 <span style="color: Red"><label ID="lblError"></label></span>
       </div></br>
	   <div class="row">
			<label class="col-md-2" for="taskpermissions">Task permissions</label>
			<div class="col-md-2">
			<input type="checkbox" onclick="disableChk();" id="task_permission_0" name="task_permission_id" value="1"/> &nbsp; All Step
			</div>			
			<div class="col-md-2">
			<input type="checkbox" id="task_permission_2" name="task_permission_id" value="2"/> &nbsp; Edit Comment
			</div>
			<div class="col-md-2">
			<input type="checkbox" id="task_permission_3" name="task_permission_id" value="3"/> &nbsp; Postpone Task
			</div>	
			</div>			
			<div class="row">
			<label class="col-md-2" for="taskpermissions"></label>
				
			<div class="col-md-3">
			<input type="checkbox" id="task_permission_4" name="task_permission_id" value="4"/> &nbsp; Change Task Finished
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
			<input type="checkbox" name="task_requirement_id" value="2"/> &nbsp; Comment 
			</div>
			<div class="col-md-2">
			<input type="checkbox" name="task_requirement_id" value="3"/> &nbsp; Approve 
			</div>
		</div></br>
	</div>	
	<div class="table-responsive">
        <table class="table" >
            <tr>
			  <th id="t18" class="oddRowEvenCol"><span class="form-control">
			 <center>All &nbsp;<input onchange="checkAll(this)" type="checkbox" name="chk"></center>
			  </span></th>
               <th ><strong>File Name</strong></th>
                <th ><strong>File Format</strong></th>                                       
                <th id="t18" class="oddRowEvenCol">&nbsp;</th>
            </tr>
<%
    Bundle[] bundles = item.getBundles();
    for (int i = 0; i < bundles.length; i++)
    {
       Bitstream[] bitstreams = bundles[i].getBitstreams();
        for (int j = 0; j < bitstreams.length; j++)
        {
          ArrayList<Integer> bitstreamIdOrder = new ArrayList<Integer>();
           for (Bitstream bitstream : bitstreams) {
                bitstreamIdOrder.add(bitstream.getID());
            }
                BitstreamFormat bf = bitstreams[j].getFormat();
%>
                <tr >
            	 <td headers="t11" class="RowEvenCol" align="center">
                       <span class="form-control">
                       <input type="checkbox" name="file_description" value="<%= bitstreams[j].getSequenceID()%>#<%= bitstreams[j].getID() %>#<%= (bitstreams[j].getName() == null ? "" : Utils.addEntities(bitstreams[j].getName())) %>#<%= request.getContextPath() %>/bitstream/<%= item.getHandle()%>/<%= bitstreams[j].getSequenceID()%>/<%= (bitstreams[j].getName() == null ? "" : Utils.addEntities(bitstreams[j].getName())) %>"/>              
					   </span>
                </td>           	
                <td>
            <input class="form-control"  disabled type="text" name="bitstream_name" value="<%= (bitstreams[j].getName() == null ? "" : Utils.addEntities(bitstreams[j].getName())) %>"/>
                </td>               
               
                <td headers="t15" class="RowEvenCol">
				<%= Utils.addEntities(bf.getShortDescription()) %>
                 <!-- <input class="form-control" type="text" name="bitstream_format_id" value="<%= Utils.addEntities(bf.getShortDescription()) %>" size="20" /> !-->
                </td>         
				
                <td headers="t10" class="RowEvenCol" align="center">                
			<a class="btn btn-info" target="_blank" href="<%= request.getContextPath() %>/bitstream/<%= item.getHandle()%>/<%= bitstreams[j].getSequenceID()%>/<%= (bitstreams[j].getName() == null ? "" : Utils.addEntities(bitstreams[j].getName())) %>"><fmt:message key="jsp.tools.general.view"/></a>&nbsp;
				</td>
             				
            </tr>
<%
     }
	}
%>
    </table>
	</div> 
   <div class="row">
			<label class="col-md-2" for="tlastname"> </label>
			<div class="col-md-3">
				<input type="submit" onclick="return formvaliate();" id="submit_save" class="btn btn-success" name="submit_save"
					value="Send Workflow" />
			</div>
		</div>
	</form>


</dspace:layout>
