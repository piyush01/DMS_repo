
<%--
  - Remove Item page
  -
  -  Attributes:
  -      workspace.item - the workspace item the user wishes to delete
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.servlet.MyDSpaceServlet" %>
<%@ page import="org.dspace.content.WorkspaceItem" %>

<%
    WorkspaceItem wi = (WorkspaceItem) request.getAttribute("workspace.item");
%>

<dspace:layout locbar="link"
               parentlink="/mydspace"
               parenttitlekey="jsp.mydspace"
               titlekey="jsp.mydspace.remove-item.title"
               nocache="true">
			   
			    <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
             <div class="box-header with-border">				
		     <h3 class="box-title">  <fmt:message key="jsp.mydspace.remove-item.title"/></h3>
	      <p><fmt:message key="jsp.mydspace.remove-item.confirmation"/></p>
		  </div>
	 <div class="box-body">
     <div class="col-sm-12">    
    <dspace:item item="<%= wi.getItem() %>"/>
    <form action="<%= request.getContextPath() %>/mydspace" method="post">
        <input type="hidden" name="workspace_id" value="<%= wi.getID() %>"/>
        <input type="hidden" name="step" value="<%= MyDSpaceServlet.REMOVE_ITEM_PAGE %>"/>

		<div class="pull-right">
			<input class="btn btn-danger" type="submit" name="submit_delete" value="<fmt:message key="jsp.mydspace.remove-item.remove.button"/>" />
			<input class="btn btn-success" type="submit" name="submit_cancel" value="<fmt:message key="jsp.mydspace.remove-item.cancel.button"/>" />
		</div>
    </form>
    </div>
	
	</div>
</div>
</div>
</div>
</div>
			   

    
    <%-- <p>Are you sure you want to remove the following incomplete item?</p> --%>
    

</dspace:layout>
