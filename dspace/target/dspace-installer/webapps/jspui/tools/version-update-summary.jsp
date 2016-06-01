
<%--
  - Insert summary for versionable item JSP
  -
  - Attributes:
   --%>

<%@page import="org.dspace.core.Context"%>
<%@page import="org.dspace.app.webui.util.UIUtil"%>
<%@page import="org.dspace.app.webui.util.VersionUtil"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>


<%
    Integer itemID = (Integer)request.getAttribute("itemID");	
	String versionID = (String)request.getAttribute("versionID");
	
	Context context = UIUtil.obtainContext(request);
	
	request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="default" titlekey="jsp.dspace-admin.version-summary.title">
   
<div class="row">
				<div class="col-lg-12 ">
				  <div class="box box-primary col-md-4">
					<div class="box-header">
					  <h3 class="box-title"><fmt:message key="jsp.dspace-admin.version-summary.heading"/></h3>
					</div>	
					<div class="box-body">

 <form class="form-horizontal" action="<%= request.getContextPath() %>/tools/history" method="post">
		<input type="hidden" name="itemID" value="<%= itemID %>" />
		<input type="hidden" name="versionID" value="<%= versionID %>" />
        <p><fmt:message key="jsp.dspace-admin.version-summary.text3"><fmt:param><%= itemID%></fmt:param></fmt:message></p>
                   <%--  <td class="submitFormLabel">News:</td> --%>
                   	<div class="form-group"> 
					<div class="col-md-3">
                    	<label for="summary"><fmt:message key="jsp.dspace-admin.version-summary.text"/></label>
						</div>
						<div class="col-md-4">
                    	<textarea class="form-control" name="summary" rows="5" cols="5"><%= VersionUtil.getSummary(context, versionID) %></textarea>
						</div>
					</div>
					<div class="form-group"> 
					<div class="col-md-3"></div>
					 <div class="col-md-3">
                    <%-- <input type="submit" name="submit_save" value="Save"> --%>
                    <input class="btn btn-success" type="submit" name="submit_update" value="<fmt:message key="jsp.version.history.update"/>" />
                    <%-- <input type="submit" name="cancel" value="Cancel"> --%>
                    <input class="btn btn-default" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
					</div>
					</div>
</form>
</div>
</div>
</div>
</div>
</div>
</dspace:layout>
