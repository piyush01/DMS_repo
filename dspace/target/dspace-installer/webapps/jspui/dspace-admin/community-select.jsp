
<%--
  - Display list of communities, with continue and cancel buttons
  -  post method invoked with community_select or community_select_cancel
  -     (community_id contains ID of selected community)
  -
  - Attributes:
  -   communities - a Community [] containing all communities in the system
  - Returns:
  -   submit set to community_select, user has selected a community
  -   submit set to community_select_cancel, return user to main page
  -   community_id - set if user has selected one

  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.content.Community" %>



<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%
    Community [] communities =
        (Community[]) request.getAttribute("communities");      
		
%>
<script type="text/javascript">
$(document).ready(function() {
	$('#editpolicy').click(function(e) {
		var tcommunity_id = $('#tcommunity_id').val();
		if (tcommunity_id==0) {		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please select cabinet!</div>');			
			$('#temail').focus();
			return false;
		}else {
			return true;
		}
	})
});
		</script>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.community-select.title"
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
                  <h3 class="box-title">Cabinet Policy</h3>	
					<div id="error-alert"></div>				  
                </div><!-- /.box-header -->                
				<!-- form start --> 
    <form class="form-horizontal" method="post" action="">
	<div class="box-body">
	<div class="form-group">
		<div class="col-sm-2"> 
		 <label><fmt:message key="jsp.dspace-admin.community-select.com"/></label>  
	   </div>
		  <div class="col-sm-3">    
						<select class="form-control" size="#" name="community_id" id="tcommunity_id">
						<option value="0"> --Select-- </option>
						<%  for (int i = 0; i < communities.length; i++) 
							{ 
							   %>
								<option value="<%= communities[i].getID()%>">
									<%= communities[i].getMetadata("name")%>
								</option>
							   <% 
							} 
						%>
						</select>  
		</div>
    </div>
			<div class="form-footer">
			<div class="col-sm-2"></div>   
			<div class="col-sm-3">   
				<input class="btn btn-primary" id="editpolicy" type="submit" name="submit_community_select" value="<fmt:message key="jsp.dspace-admin.general.editpolicy"/>" />	
				<input class="btn btn-danger" type="reset" name="submit_cancel_policy" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />				   
				
			</div>
	</div>
	</div>		
   </form>
    </div>											
	</div>
	</div>
  
</dspace:layout>
