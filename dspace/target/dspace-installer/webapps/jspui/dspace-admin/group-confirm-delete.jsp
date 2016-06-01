
<%--
  - Confirm deletion of a group
  -
  - Attributes:
  -    group   - group we may delete
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.dspace.eperson.Group" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    Group group = (Group) request.getAttribute("group");
%>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.group-confirm-delete.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">

			   <div class="row">
			<div class="col-md-12">
			 <!-- general form elements -->
					  <div class="box box-primary">
						<div class="box-header with-border">
						<h3 class="box-title">
						 <fmt:message key="jsp.dspace-admin.group-confirm-delete.heading">
							 <fmt:param><%= group.getName() %></fmt:param>
						 </fmt:message>
						</h3>
                   <!--<div class="alert alert-warning"><fmt:message key="jsp.tools.group-edit.heading"/></div>		-->		   <p class="alert alert-danger"><fmt:message key="jsp.dspace-admin.group-confirm-delete.confirm"/>    </p>
					 </div>	          
                <div class="box-body">					 
                    <form method="post" action="">
                      		<div class="btn-group col-md-offset-5">
								<input type="hidden" name="group_id" value="<%= group.getID() %>"/>
                    			<input class="btn btn-danger" type="submit" name="submit_confirm_delete" value="<fmt:message key="jsp.dspace-admin.general.delete"/>" />
                    			<input class="btn btn-default" type="submit" name="submit_cancel_delete" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
							</div>
                    </form>
					</div>	 
					</div>	 
					</div>	 
					</div>	 
</dspace:layout>

