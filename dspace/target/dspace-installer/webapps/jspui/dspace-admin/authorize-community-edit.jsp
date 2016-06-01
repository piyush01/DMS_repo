
<%--
  - Show policies for a community, allowing you to modify, delete
  -  or add to them
  -
  - Attributes:
  -  community - Community being modified
  -  policies - ResourcePolicy [] of policies for the community
  - Returns:
  -  submit value community_addpolicy    to add a policy
  -  submit value community_editpolicy   to edit policy
  -  submit value community_deletepolicy to delete policy
  -
  -  policy_id - ID of policy to edit, delete
  -
  --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.util.List"     %>
<%@ page import="java.util.Iterator" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.authorize.ResourcePolicy" %>
<%@ page import="org.dspace.content.Community"        %>
<%@ page import="org.dspace.core.Constants"           %>
<%@ page import="org.dspace.eperson.EPerson"          %>
<%@ page import="org.dspace.eperson.Group"            %>

<%
   String name=(String)request.getParameter("action");
    Community community = (Community) request.getAttribute("community");
    List<ResourcePolicy> policies =
        (List<ResourcePolicy>) request.getAttribute("policies");
      
%>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.authorize-community-edit.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin"
               nocache="true">   
  
    <!-- Main content -->
        <section class="content">
          <div class="row">
            <div class="col-xs-12">
              <!-- general form elements -->			  
			  <div class="box box-primary">
                <div class="box-header">
				<h3>Policy List</h3>
                  	<div id="error-alert"></div> 
					  <form action="<%= request.getContextPath() %>/tools/authorize" method="post">
						<div class="row">    
						 <input type="hidden" name="community_id" value="<%=community.getID()%>" />
						 <input class="btn btn-success col-md-2 col-md-offset-5" type="submit" name="submit_community_add_policy" value="<fmt:message key="jsp.dspace-admin.general.addpolicy"/>" />
						</div>
					  </form>
                </div><!-- /.box-header -->
              <div class="box-body">  
          <table id="example1" class="table table-bordered table-striped">	  
         <thead>
          <tr>            
           <th><strong>
		   <% if(name!=null && name.equals("submit_folder")){ %>
		   Folder Name
		   <%} else {%>
		   Cabinet Name
		   <%}%>
		   </strong></th>
            <th><strong><fmt:message key="jsp.dspace-admin.general.action"/></strong></th>
			 <th><strong>User</strong></th>
            <th><strong><fmt:message key="jsp.dspace-admin.general.group"/></strong></th>
            <th></th>           
         </tr>
		</thead>
     <tbody>
<%
    String row = "even";
    for (ResourcePolicy rp : policies)
    {
%>
        <tr>
            <td><%= community.getName()%></td>
            <td><%= rp.getActionText() %></td>
			 <td><%=(rp.getEPerson()==null ? "...":rp.getEPerson().getFullName()) %></td>
            <td>
                 <%= (rp.getGroup()   == null ? "..." : rp.getGroup().getName() ) %>  
             </td>
             <td>
                <form action="<%= request.getContextPath() %>/tools/authorize" method="post">
                    <input type="hidden" name="policy_id" value="<%= rp.getID() %>" />
                    <input type="hidden" name="community_id" value="<%= community.getID() %>" />
                    <input class="btn btn-primary" type="submit" name="submit_community_edit_policy" value="<fmt:message key="jsp.dspace-admin.general.edit"/>" />
                    <input class="btn btn-danger" type="submit" name="submit_community_delete_policy" value="<fmt:message key="jsp.dspace-admin.general.delete"/>" />
			   </form>
             </td>            
         </tr>

<%
        row = (row.equals("odd") ? "even" : "odd");
    }
%>
   </tbody>
   </table>
   </div>
   </div>
   </div>
   </div>
</section>
<!-- DataTables -->
    <script src="<%= request.getContextPath() %>/static1/plugins/datatables/jquery.dataTables.min.js"></script>
    <script src="<%= request.getContextPath() %>/static1/plugins/datatables/dataTables.bootstrap.min.js"></script>
    <!-- SlimScroll -->
    <script src="<%= request.getContextPath() %>/static1/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <!-- FastClick -->
    <script src="<%= request.getContextPath() %>/static1/plugins/fastclick/fastclick.min.js"></script>
    <!-- AdminLTE App -->
   
    <!-- AdminLTE for demo purposes -->
    <script src="<%= request.getContextPath() %>/static1/dist/js/demo.js"></script>
    <!-- page script -->
    <script>
      $(function () {	
        $("#example1").DataTable();
        $('#example2').DataTable({
          "paging": true,
          "lengthChange": false,
          "searching": false,
          "ordering": true,
          "info": true,
          "autoWidth": false
        });
      });
    </script>
</dspace:layout>
