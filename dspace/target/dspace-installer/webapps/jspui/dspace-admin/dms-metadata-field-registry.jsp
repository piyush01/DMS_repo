<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@page import="java.util.ArrayList" %>	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.lang.String" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.MetadataField" %>
<%@ page import="org.dspace.content.MetadataSchema" %>
<%@ page import="org.dspace.content.Collection" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.dspace.eperson.EPerson" %>
<%
/* Collection [] collections =(Collection[]) request.getAttribute("collections");
MetadataSchema schema =(MetadataSchema) request.getAttribute("schema");
MetadataSchema[] schemas =(MetadataSchema[]) request.getAttribute("schemas"); */
boolean errormessage = (request.getAttribute("checkmessage") != null);
boolean field_add=(request.getAttribute("field_add")!=null);
%>
<!--<script type="text/javascript">      
	  $(document).ready(function()
      { 
	  
	  $("#required_op").change(function(){
			if($(this).val()=="yes")
			{    
			$("#message").show();
			}
			else
			{
				$("#message").hide();
			}
			});
		  
		  });	 
	</script>
	-->
	<script type="text/javascript">      
	  $(document).ready(function()
      { 
	  
	  $("#tinputtype").change(function(){
			if($(this).val()=="dropdown")
			{    
			$("#dataTypeValue").show();
			}
			else
			{
				$("#dataTypeValue").hide();
			}
			});
		  
		  });	 
	</script>
	
	<script type="text/javascript">
$(function () {
    $("#btnAdd").bind("click", function () {
        var div = $("<div/>");
        div.html(GetDynamicTextBox(""));
        $("#TextBoxContainer").append(div);
    });
    $("#btnGet").bind("click", function () {
        var values = "";
        $("input[name=dropdown_value]").each(function () {
            values += $(this).val() + "\n";
        });
        alert(values);
    });
    $("body").on("click", "#remove", function () {
        $(this).closest("div").remove();
    });
});
function GetDynamicTextBox(value) {
    return '<input name = "dropdown_value" class="form-control" type="text" value = "' + value + '" />&nbsp;' +
            '<input type="button" class="btn btn-primary" value="Remove" id="remove" />'
}
</script>
	<script language="javascript">
function changeIt()
{
my_div.innerHTML = my_div.innerHTML +"<br><input type='text' class='form-control' name='dropdown_value'/>" 
}
</script>
<script type="text/javascript">      
	  $(document).ready(function()
      {   
			$('#add-field').click(function(e) {
				var letters = /^[0-9]+$/; 
		
	 if($.trim($('#text1').val())==0)
	 {
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Field Name.</div>');			
		$('#text1').focus();
		return false; 
	 }
	 else if($.trim($('#text1').val()).match(letters))
	 {
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Field Name should not be numeric character only.</div>');			
		$('#text1').focus();
		return false; 
	 }
	 else if($.trim($('#text1').val()).toLowerCase()=="date".toLowerCase())
	 {
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Date is reserved field, You can add date with other name.(Ex-joining Date)</div>');			
		$('#text1').focus();
		return false; 
	 }
	 else if($.trim($('#tinputtype').val())==0)
	 {
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Select Data Type.</div>');			
		$('#tinputtype').focus();
		return false; 
	 }
     });
	 });	 
	</script>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
	  <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<!--<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
					</div>-->
					<h3 class="box-title">
			        Required fields are marked with a (<span class="star">*</span>)
			      </h3>
				  <div id="error-alert"></div>
					<%
					if(errormessage)
					{
					%>	
					 <div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Field name alredy added!,you must change name.</div>
					<%}%>  
					<%
					if(field_add)
					{
					%>	
					 <div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Field has been successfully added.</div>
					<%}%>
					<%
					String error = (String)request.getAttribute("error");
					if (error!=null) {
					%>
						<p class="alert alert-danger">
							<%=error%>
						</p>
					<% } %>							  
				  </div>
			

	<form class="form-horizontal" method="post" action="" name="taddfield" id="taddfield">
		 <div  class="box-body">	     
			
	 	   <div class="form-group">
		      <div class="col-sm-2">
			  <label>
				Field Name<span class="star">*</span></label>
				 </div>
			<div class="col-sm-3">
				<input class="form-control" id="text1" type="text" name="fieldname" onkeypress="return IsAlphaNumeric(event);" ondrop="return false;"
        onpaste="return false;"/>
			</div>		
			<div class="col-sm-3">
			<span id="error" style="color: Red; display: none">* Special Characters not allowed</span>
			</div>
			<script type="text/javascript">
        var specialKeys = new Array();
        specialKeys.push(8); //Backspace
        specialKeys.push(9); //Tab
        specialKeys.push(46); //Delete
        specialKeys.push(36); //Home
        specialKeys.push(35); //End
        specialKeys.push(37); //Left
        specialKeys.push(39); //Right
        function IsAlphaNumeric(e) {
            var keyCode = e.keyCode == 0 ? e.charCode : e.keyCode;
            var ret = ((keyCode >= 48 && keyCode <= 57) ||(keyCode==32)|| (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122) || (specialKeys.indexOf(e.keyCode) != -1 && e.charCode != e.keyCode));
            document.getElementById("error").style.display = ret ? "none" : "inline";
            return ret;
        }
    </script>
	   </div>
	   
	   <div class="form-group">
		      <div class="col-sm-2">
			  <label>
				Data Type<span class="star">*</span></label>
			</div>
			<div class="col-sm-3">
				<select class="form-control"  name="inputtype_id" id="tinputtype" onchange="change()">
					<option value="0" selected>--Select Data Type--</option>
					<option value="onebox">Numeric</option> 
					<option value="name">Text</option>  
					<option value="date">Date</option>  
					<option value="textarea">Text Area</option> 
					<option value="dropdown">Drop Down</option> 					
			 </select>
			</div>
			 <input class="form-control" id="defaultvalue" type="hidden" name = "dropdown_value" value="--Please Select--"/>
			<div class="col-sm-3" id="dataTypeValue" style="display: none">
			<input class="btn btn-primary" type="button" value="Add TextBox" id="btnAdd"></div>
	   </div>
	   
	  
<div class="form-group">
<div class="col-sm-2">
			</div>
	    <div id="TextBoxContainer" class="col-sm-3">
    <!--Textboxes will be added here-->
		</div>
		</div>
	   <div class="form-group">
		      <div class="col-sm-2">
			  <label>
				Mandatory <span class="star">*</span>		 
			</label>
			</div>
			<div class="col-sm-3">
			 <!--<input class="form-control" id="trequired" type="text" name="required"/> -->
			 <select class="form-control"  name="required_field" id="required_op">
					<option value="no" selected="selected">No</option>
					<option value="yes">Yes</option>  
										
			 </select>
			</div>
			<div class="col-sm-5"></div>
		</div>
		<!-- <div class="form-group" id="message" style="display: none">
		      <div class="col-sm-2">
			  <label>
				Message	 
			</label>
			</div>
			<div class="col-sm-5">
			<input class="form-control" id="trequired" type="text" name="required"/>
			</div>
			<div class="col-sm-5"></div>
		</div>
		<div class="form-group">
		      <div class="col-sm-2">
			  <label>
			  <fmt:message key="jsp.dspace-admin.list-metadata-fields.scope"/>
			  </label>
			 </div>
			 <div class="col-sm-5">
			<textarea class="form-control" id="tscop_note" name="scope_note" rows="3" cols="40"></textarea>
		  </div>
		  <div class="col-sm-5"></div>
		</div>-->
		<div class="form-group">
		      <div class="col-sm-2"></div>
			 <div class="col-sm-5">
			 <input class="btn btn-primary" type="submit" name="submit_add" id="add-field" value="Add Field"/>
			 <input class="btn btn-danger" type="submit" name="submit_cancel"  value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
			 </div>
			  <div class="col-sm-5"></div>
		</div>
	
		</div>
</form> 
</div>
</div>
</div>

</dspace:layout>
