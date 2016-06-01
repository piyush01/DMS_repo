

<%--
  - Display list of Groups, with 'edit' and 'delete' buttons next to them
  -
  - Attributes:
  -
  -   groups - Group [] of groups to work on
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
    
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.eperson.Group" %>
<%
    Group[] groups =(Group[]) request.getAttribute("groups");        
%>
<dspace:layout style="submission" titlekey="jsp.tools.group-list.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin"
               nocache="true">
	<div class="row">
			<div class="col-md-12">
			 <!-- general form elements -->
					  <div class="box box-primary">
						<div class="box-header with-border">
						<h3 class="box-title">Group Management</h3>
						<!--<p class="alert alert-info"><fmt:message key="jsp.tools.group-list.note1"/></p>	-->
	                  <div class="col-md-12">			
					<form method="post" action="">
						<div class="row col-md-offset-5">
							<input class="btn btn-success" type="submit" name="submit_add" value="<fmt:message key="jsp.tools.group-list.create.button"/>" />
						</div>
					</form>
				   </div>					  
   					</div>
					
			<div class="box-body"> 	            		
			   <table id="example1" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th width="30px"><strong>S.No</strong></th>
							<th width="30px"><strong><fmt:message key="jsp.tools.group-list.name"/></strong></th>
							<th>ACTION</th>
						</tr>
					</thead>
					<tbody>
							<%
								String row = "even";
								for (int i = 0; i < groups.length; i++)
								{
									if (groups[i].getID() > 0 )
								{
							%>
										<tr>
											<td width="20px"><%=i%></td>
											<td  width="30px">
												<%= groups[i].getName() %>
											</td>
											<td>
							<%
								// no edit button for group anonymous
								if (groups[i].getID() > 0 )
								{
							%>                  
												<form method="post" action="">
													<input type="hidden" name="group_id" value="<%= groups[i].getID() %>"/>
											<span class="icon-input-btn"><span class="glyphicon glyphicon-edit"></span><input class="btn btn-info btn-xs" type="submit" name="submit_edit" value="<fmt:message key="jsp.tools.general.edit"/>" />
											   
							<%
								}

								// no delete button for group Anonymous 0 and Administrator 1 to avoid accidental deletion
								if (groups[i].getID() > 1 )
								{
							%>   					
							<input type="hidden" name="group_id" value="<%= groups[i].getID() %>"/>
												<span class="icon-input-btn"><span class="glyphicon glyphicon-trash"></span><input class="btn btn-danger btn-xs" type="submit" name="submit_group_delete" value="<fmt:message key="jsp.tools.general.delete"/>" />
							<%
								}
							%>	                
												</form>
											</td>
										</tr>
										
							<%
									row = (row.equals("odd") ? "even" : "odd");
								}
								}
							%>
							</tbody>
    </table>
	</div>
	</div>
	</div>
	</div>
</dspace:layout>
