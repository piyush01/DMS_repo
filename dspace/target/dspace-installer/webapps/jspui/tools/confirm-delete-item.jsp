
<%--
  - Confirm deletion of a item
  -
  - Attributes:
  -    item   - item we may delete
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditItemServlet" %>
<%@ page import="org.dspace.content.Item" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    String handle = (String) request.getAttribute("handle");
    Item item = (Item) request.getAttribute("item");
    request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="default" locbar="commLink" titlekey="jsp.tools.confirm-delete-item.title" nocache="true">

   <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">				
		        <h3 class="box-title">
				<fmt:message key="jsp.tools.confirm-delete-item.title"/>: <%= (handle == null ? String.valueOf(item.getID()) : handle) %></h3>

				<%-- <p>Are you sure this item should be completely deleted?  Caution:
				At present, no tombstone would be left.</p> --%>
				<p><fmt:message key="jsp.tools.confirm-delete-item.info"/></p>
			  </div>
			 <div  class="box-body">
    <dspace:item item="<%= item %>" style="full" />

    <form method="post" action="<%= request.getContextPath() %>/tools/edit-item">
        <input type="hidden" name="item_id" value="<%= item.getID() %>"/>
        <input type="hidden" name="action" value="<%= EditItemServlet.CONFIRM_DELETE %>"/>
             <div class="form-group">
			 <div class="col-md-3">
                        <%-- <input type="submit" name="submit" value="Delete" /> --%>
						<input class="btn btn-danger" type="submit" name="submit" value="<fmt:message key="jsp.tools.general.delete"/>" />
                                   
                        <%-- <input type="submit" name="submit_cancel" value="Cancel"> --%>
				<input class="btn btn-success" type="submit" name="submit_cancel" value="<fmt:message key="jsp.tools.general.cancel"/>" />
			 </div>
		</div>
                    
    </form>
	 </div>
	  </div>
	   </div>
	    </div>
		 </div>
</dspace:layout>
