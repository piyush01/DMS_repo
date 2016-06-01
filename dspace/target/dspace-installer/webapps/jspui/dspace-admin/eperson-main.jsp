
<%--
  - main page for eperson admin
  -
  - Attributes:
  -   no_eperson_selected - if a user tries to edit or delete an EPerson without
  -                         first selecting one
  -   reset_password - if a user tries to reset password of an EPerson and the email with token is
  -                    send successfull 
  -
  - Returns:
  -   submit_add    - admin wants to add an eperson
  -   submit_browse - admin wants to browse epeople
  -
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%--start for User list --%>

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.core.Utils" %>

<%

	int PAGESIZE = 4;
    EPerson[] epeople =
        (EPerson[]) request.getAttribute("epeople");
    int sortBy = ((Integer)request.getAttribute("sortby" )).intValue();
    int first = ((Integer)request.getAttribute("first")).intValue();
	boolean multiple = (request.getAttribute("multiple") != null);
	String search = (String) request.getAttribute("search");
	 String message=(String)request.getAttribute("message");
	if (search == null) search = "";
	int offset = ((Integer)request.getAttribute("offset")).intValue();

	// Make sure we won't run over end of list
	int last;
	if (search != null && !search.equals(""))
	{
		last = offset + PAGESIZE;	
	}
	else 
	{
	  last = first + PAGESIZE;
	}
	if (last >= epeople.length) last = epeople.length - 1;

	// Index of first eperson on last page
	int jumpEnd = ((epeople.length - 1) / PAGESIZE) * PAGESIZE;

	// Now work out values for next/prev page buttons
	int jumpFiveBack;
	if (search != null && !search.equals(""))
	{
	    jumpFiveBack = offset - PAGESIZE * 5;
	}
	else
	{
		jumpFiveBack = first - PAGESIZE * 5;
	}
	if (jumpFiveBack < 0) jumpFiveBack = 0;

	int jumpOneBack;
	if (search != null && !search.equals(""))
	{
		jumpOneBack = offset - PAGESIZE;		
	}
	else
	{
	   jumpOneBack = first - PAGESIZE;
	}
	if (jumpOneBack < 0) jumpOneBack = 0;
	
	int jumpOneForward;
	if (search != null && !search.equals(""))
	{
		jumpOneForward = offset + PAGESIZE;
	}
	else
	{
		jumpOneForward = first + PAGESIZE;
	}
	if (jumpOneForward > epeople.length) jumpOneForward = jumpEnd;

	int jumpFiveForward;
	if (search != null && !search.trim().equals(""))
	{
		jumpFiveForward = offset + PAGESIZE * 5;
	}
	else 
	{
		jumpFiveForward = first + PAGESIZE * 5;
	}
	if (jumpFiveForward > epeople.length) jumpFiveForward = jumpEnd;

	// What's the link?
	String sortByParam = "lastname";
	if (sortBy == EPerson.EMAIL) sortByParam = "email";
	if (sortBy == EPerson.ID) sortByParam = "id";
	if (sortBy == EPerson.LANGUAGE) sortByParam = "language";

	String jumpLink;
	if (search != null && !search.equals(""))
	{
		jumpLink = request.getContextPath() + "/dspace-admin/edit-epeople?multiple=" + multiple + "&sortby=" + sortByParam + "&first="+first+"&search="+search+"&offset=";
	}
	else
	{
		jumpLink = request.getContextPath() + "/dspace-admin/edit-epeople?multiple=" + multiple + "&sortby=" + sortByParam + "&first=";
	}
	String sortLink = request.getContextPath() + "/dspace-admin/edit-epeople?multiple=" + multiple + "&first=" + first + "&sortby=";
%>
<%--End for User list --%>



<%
   boolean noEPersonSelected = (request.getAttribute("no_eperson_selected") != null);
   boolean resetPassword = (request.getAttribute("reset_password") != null);
   boolean loginAs = ConfigurationManager.getBooleanProperty("webui.user.assumelogin", false);
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
	  
	 <%-- Will never actually be posted, it's just so buttons will appear --%>
	 
 <!-- Main content -->

          <div class="row">
            <div class="col-xs-12">
              <!-- general form elements -->			  
			 
			  
			  
			  <div class="box box-primary">
                <div class="box-header">
                  <h3 class="box-title">User List</h3>
				  <%
				  if(message!=null && !message.equals(""))
				  {
					 %>
                <p class="text-success"><%=message%></p>
					 <%
				  }
				  %>
                </div><!-- /.box-header -->
                <div class="box-body">	
				
				 <table id="example1" class="table table-bordered table-striped">	  
					 <thead>
					 
			  <% if (search != null && !search.equals(""))
			   {  %>
				  <tr>
				   
						<th><fmt:message key="jsp.tools.eperson-list.th.email" /></th>
						<th><fmt:message key="jsp.tools.eperson-list.th.firstname" /></th>
						<th><fmt:message key="jsp.tools.eperson-list.th.lastname" /></th>
						<th><fmt:message key="jsp.tools.eperson-list.th.action" /></th>
				   </tr>
				   <% }
			   else 
			   {  %>
				  <tr>           
						
						<th><%
							if (sortBy == EPerson.EMAIL)
							{
								%><fmt:message key="jsp.tools.eperson-list.th.email"/><%
							}
							else
							{
								%><a href="<%= sortLink %>email"><fmt:message key="jsp.tools.eperson-list.th.email" /></a><%
							}
						%></th>
					   
					   <th id="t5"><fmt:message key="jsp.tools.eperson-list.th.firstname"/></th>
					   <th id="t4"><%
							if (sortBy == EPerson.LASTNAME)
							{
								%><fmt:message key="jsp.tools.eperson-list.th.lastname"/><%
							}
							else
							{
								%><a href="<%= sortLink %>lastname"><fmt:message key="jsp.tools.eperson-list.th.lastname" /></a><%
							}
						%></th>
						 <th>Action</th>
						
					</tr>
					<%  }
					String row = "even";

				// If this is a dialogue to select a *single* e-person, we want
				// to clear any existing entry in the e-person list, and
				// to close this window when a 'select' button is clicked
				String clearList = (multiple ? "" : "clearEPeople();");
				String closeWindow = (multiple ? "" : "window.close();");


				for (int i = (search != null && !search.equals(""))?offset:first; i <= last; i++)
				{
					EPerson e = epeople[i];
					// Make sure no quotes in full name will mess up our Javascript
					String fullname = StringEscapeUtils.escapeXml(StringEscapeUtils.escapeJavaScript(e.getFullName()));
					String email = StringEscapeUtils.escapeXml(StringEscapeUtils.escapeJavaScript(e.getEmail()));
					
					
					%>
				</thead>
				<tbody>	
				<tr>
					<td headers="t3"><%= (e.getEmail() == null ? "" : Utils.addEntities(e.getEmail())) %></td>
						<td headers="t4">
						<%= (e.getFirstName() == null ? "" : Utils.addEntities(e.getFirstName())) %>                
						</td>
						<td headers="t5">
						   <%= (e.getLastName() == null ? "" : Utils.addEntities(e.getLastName())) %>
						</td>
						<td headers="t6">
						<form name="epersongroup" method="post" action=""> 
						 <input type="hidden" name="userupdate" value="update">
						   <span class="icon-input-btn"><span class="glyphicon glyphicon-edit"></span>
						   <input type="submit" class="btn btn-primary btn-xs" name="submit_edit" value="<fmt:message key="jsp.dspace-admin.general.edit"/>" onclick="javascript:finishEPerson();"/>						
					   <input type="hidden" name="eperson_id" value="<%= e.getID() %>">
					   
					   <% if(loginAs) { %>&nbsp;<input type="submit" class="btn btn-default" name="submit_login_as" value="<fmt:message key="jsp.dspace-admin.eperson-main.LoginAs.submit"/>" onclick="javascript:finishEPerson();"/> <% } %>          
					<span class="icon-input-btn"><span class="glyphicon glyphicon-trash"></span>
					<input type="submit" class="btn btn-danger btn-xs" name="submit_delete" value="<fmt:message key="jsp.dspace-admin.general.delete-w-confirm"/>" onclick="javascript:finishEPerson();"/>
						</form>
						</td>
					</tr>
					<%
					row = (row.equals("odd") ? "even" : "odd");
				}
			%>
			  </tbody>
			</table>

		<!--<div style="float:left">	
			<h5><fmt:message key="jsp.tools.eperson-list.heading">
				<fmt:param><%= ((search != null && !search.equals(""))?offset:first) + 1 %></fmt:param>
				<fmt:param><%= last + 1 %></fmt:param>
				<fmt:param><%= epeople.length %></fmt:param>
			</fmt:message>
			</h5>
			</div>
			<div style="float:right">
		<form method="get" action="">
			<ul class="pagination" style="text-align:center">
					<li><a href="<%= jumpLink %>0"><fmt:message key="jsp.tools.eperson-list.jump.first"/></a></li>
					<li><a href="<%= jumpLink %><%= jumpFiveBack %>"><fmt:message key="jsp.tools.eperson-list.jump.five-back"/></a></li>
					<li><a href="<%= jumpLink %><%= jumpOneBack %>"><fmt:message key="jsp.tools.eperson-list.jump.one-back"/></a></li>
					<li><a href="<%= jumpLink %><%= jumpOneForward %>"><fmt:message key="jsp.tools.eperson-list.jump.one-forward"/></a></li>
					<li><a href="<%= jumpLink %><%= jumpFiveForward %>"><fmt:message key="jsp.tools.eperson-list.jump.five-forward"/></a></li>
					<li><a href="<%= jumpLink %><%= jumpEnd %>"><fmt:message key="jsp.tools.eperson-list.jump.last"/></a></li>
			</ul>
			</form>
		</div>	-->

				</div><!-- /.box-body -->
              </div><!-- /.box -->
			  
			  
            </div><!-- /.col -->
          </div><!-- /.row -->
        
<%-- Controls for jumping around list--%>



</dspace:layout>


