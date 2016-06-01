<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@page import="java.util.ArrayList" %>	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.lang.String" %>
<%@ page import="org.dspace.content.DmsMetadaField" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%
	DmsMetadaField[] allfields=(DmsMetadaField[])request.getAttribute("fields");
%>



<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
			<div class="box box-primary">
                <div class="box-header">
                  <h3 class="box-title">Field List</h3>
                </div><!-- /.box-header -->
                <div class="box-body">   
		 <table id="example1" class="table table-bordered table-striped">	  
					 <thead>
					  <tr>
				   
						<th>Field Name</th>
						<th>Data Type</th>
					
				   </tr>
					</thead>
					<tbody>
					<%for(int i=0;i<allfields.length;i++){ %>
					<tr>
					<td><%=allfields[i].getFieldLevel() %></td>
					<td><%=allfields[i].getDataType() %></td>
					</tr>
			<%} %>
					</tbody>
		</table>
</div>
</div>		
</dspace:layout>
