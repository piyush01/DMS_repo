
<%--
  - Confirm deletion of a community
  -
  - Attributes:
  -    community   - community we may delete
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.content.Community" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    Community community = (Community) request.getAttribute("community");
%>
 <dspace:layout locbar="commLink" title="Delete"  nocache="true">
     <%-- <h1>Delete Community: <%= community.getID() %></h1> --%>
	<div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
       
     <h3 class="box-title">Are you sure the <strong><%= community.getMetadata("name") %></strong>
    should be deleted?  This will delete:</h3>
   
        </div>
		<div class="box-body">
    <ul>
        <li><fmt:message key="jsp.tools.confirm-delete-community.info1"/></li>
        <li><fmt:message key="jsp.tools.confirm-delete-community.info2"/></li>
        <li><fmt:message key="jsp.tools.confirm-delete-community.info3"/></li>
        <li><fmt:message key="jsp.tools.confirm-delete-community.info4"/></li>
    </ul>
    
    <form method="post" action="">
        <input type="hidden" name="community_id" value="<%= community.getID() %>" />
        <input type="hidden" name="action" value="<%= EditCommunitiesServlet.CONFIRM_DELETE_COMMUNITY %>" />

		<input class="btn btn-default col-md-2 pull-right" type="submit" name="submit_cancel" value="<fmt:message key="jsp.tools.general.cancel"/>"/>
        <input class="btn btn-danger col-md-2 pull-right" type="submit" name="submit" value="<fmt:message key="jsp.tools.general.delete"/>"/>
    </form>
	</div>
	</div>
	</div>
	</div>
	</div>
</dspace:layout>
