<%--
  - Version history table with functionalities
  -
  - Attributes:
   --%>

<%@page import="org.dspace.app.webui.util.UIUtil"%>
<%@page import="org.dspace.core.Context"%>
<%@page import="org.dspace.content.Item"%>
<%@page import="org.dspace.eperson.EPerson"%>
<%@page import="org.dspace.versioning.Version"%>
<%@page import="org.dspace.app.webui.util.VersionUtil"%>
<%@page import="org.dspace.versioning.VersionHistory"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Integer itemID = (Integer)request.getAttribute("itemID");
	String versionID = (String)request.getAttribute("versionID");
	Item item = (Item) request.getAttribute("item");
	Boolean removeok = UIUtil.getBoolParameter(request, "delete");
	Context context = UIUtil.obtainContext(request);
	
	request.setAttribute("LanguageSwitch", "hide");
%>
<c:set var="dspace.layout.head.last" scope="request">
<script type="text/javascript">
var j;
 j = jQuery.noConflict(true);

</script>
</c:set>
<dspace:layout style="default" titlekey="jsp.version.history.title">

 <div class="modal fade" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title"><fmt:message key="jsp.version.history.delete.warning.head1"/></h4>
      </div>
      <div class="modal-body">
        <p><fmt:message key="jsp.version.history.delete.warning.para1"/><br/><fmt:message key="jsp.version.history.delete.warning.para2"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="jsp.version.history.popup.close"/></button>
        <button type="button" class="btn btn-danger" 
		><fmt:message key="jsp.version.history.popup.delete"/></button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
	
	
	 <div class="row">
				<div class="col-lg-12 ">
				  <div class="box box-primary col-md-4">
					<div class="box-header">
					  <h3 class="box-title">File <fmt:message key="jsp.version.history.head2"/></h3>
					
					<% if(removeok) { %>
					<div class="alert alert-success"><fmt:message key="jsp.version.history.delete.success.message"/></div>
					<% } %>
					</div>	
			<div class="box-body">
			 <form action="<%= request.getContextPath() %>/tools/history" method="post">
				<input type="hidden" name="itemID" value="<%= itemID %>" />
				<input type="hidden" name="versionID" value="<%= versionID %>" />                
				<%-- Versioning table --%>
			<%                
				VersionHistory history = VersionUtil.retrieveVersionHistory(context, item);
									 
			%>
	
	<div id="versionHistory">
	<p class="alert alert-info"><fmt:message key="jsp.version.history.legend"/></p>
	<table id="example1"  class="table table-bordered table-striped">
		<thead><tr>
			<th id="t0"></th>			
			<th 			
				id="t2" class="oddRowOddCol"><fmt:message key="jsp.version.history.column1"/></th>
			<th 
				id="t3" class="oddRowEvenCol"><fmt:message key="jsp.version.history.column3"/></th>
			<th 				
				id="t4" class="oddRowOddCol"><fmt:message key="jsp.version.history.column4"/></th>
						
		</tr>
		</thead>
		<% for(Version versRow : history.getVersions()) {  
		
			EPerson versRowPerson = versRow.getEperson();
			String[] identifierPath = VersionUtil.addItemIdentifier(item, versRow);

		%>	
		<tbody>
		<tr>
			<td headers="t0"><input type="checkbox" class="remove" name="remove" value="<%=versRow.getVersionId()%>"/></td>	
			
			<td headers="t2" class="oddRowOddCol"><a href="<%= request.getContextPath() + identifierPath[0] %>"><%= versRow.getVersionNumber() %></a><%= item.getID()==versRow.getItemID()?"<span class=\"glyphicon glyphicon-asterisk\"></span>":""%></td>
			<td headers="t3" class="oddRowEvenCol"><a href="mailto:<%= versRowPerson.getEmail() %>">
			<%=versRowPerson.getFullName() %></a></td>
			<td headers="t4" class="oddRowOddCol"><%= versRow.getVersionDate() %></td>
			
		</tr>
		</tbody>
		<% } %>
	</table>
	 <div style="display: none">
	<input  data-toggle="modal" href="#myModal" class="btn btn-danger" type="button" id="fake_submit_delete" value="<fmt:message key="jsp.version.history.delete"/>"/>
	</div>
 <input type="submit" class="btn btn-danger" value="<fmt:message key="jsp.version.history.delete"/>" name="submit_delete" id="submit_delete"/>
 <input class="btn btn-default" type="submit" value="<fmt:message key="jsp.version.history.return"/>" name="submit_cancel"/>
	      
</form>
</div>  
</div>  
</div>  
</div>  
</div>  
</div>
</dspace:layout>
