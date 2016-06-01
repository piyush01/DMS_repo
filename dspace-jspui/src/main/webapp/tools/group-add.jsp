
<%--
  - Show contents of a group (name, epeople)
  -
  - Attributes:
  -   group - group to be edited
  -
  - Returns:
  -   cancel - if user wants to cancel
  -   add_eperson - go to group_eperson_select.jsp to choose eperson
  -   change_name - alter name & redisplay
  -   eperson_remove - remove eperson & redisplay
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.eperson.Group"   %>
<%@ page import="org.dspace.core.Utils" %>
<%
	request.setAttribute("LanguageSwitch", "hide");	
boolean error=(request.getAttribute("error")!=null);
%>
<script type="text/javascript">
$(document).ready(function() {
	$('#save_group').click(function(e) {
		var letters = /^[0-9]+$/; 
		if ($.trim($('#tgroup_name').val()) == 0) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Group Name.</div>');			
		$('#tgroup_name').focus();
     return false;
	 }
	 else if($.trim($('#tgroup_name').val()).match(letters))
	 {
		 $('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Group name Should not be numeric character only.</div>');			
		$('#tgroup_name').focus();
     return false;
	 }
	 
		   });
});

</script>
<dspace:layout style="submission" titlekey="jsp.tools.group-edit.title" navbar="admin" locbar="link"   parenttitlekey="jsp.administer"
               parentlink="/dspace-admin"
               nocache="true">

          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title">Add Group</h3>
				  <% if (error)
					{ %><p class="alert alert-danger">
						Group Name Already exits Please Try Again.
					   </p>
				<%  } %>
						   
	<div id="error-alert"></div> 
                </div><!-- /.box-header -->
                
				<!-- form start --> 
   <form class="form-horizontal" name="epersongroup" method="post" action="">
   <div class="box-body">
	  <div class="form-group">
     <label class="col-sm-2"><fmt:message key="jsp.tools.group-edit.name"/><span class="star">*</span></label>  
   <div class="col-sm-5"> 
   <input class="form-control" type="text" name="group_name" style="width: 100%;height: 170%" size="40" id="tgroup_name"/>
    </div>
     </div>
	 <div class="form-group">
	          <div class="col-sm-2">
						
						</div>
						<div class="col-sm-8">							
					<input class="btn btn-primary" type="submit" id="save_group" name="submit_group_add" value="Add Group" onclick="javascript:finishEPerson();finishGroups();"/>
							<input class="btn btn-danger" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />	
						</div>											
					</div>
</div>					
	 	   </form>
		   	</div>											
					</div>
					</div>
					
   </dspace:layout>