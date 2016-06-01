<%--
  - Display list of DC schemas
  -
  - Attributes:
  -
  -   formats - the DC formats in the system (MetadataValue[])
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.content.MetadataSchema" %>
<%
    MetadataSchema[] schemas =
        (MetadataSchema[]) request.getAttribute("schemas");
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.list-metadata-schemas.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
           <div class="row">
            <!-- left column -->
             <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<h1 class="box-title"><fmt:message key="jsp.dspace-admin.list-metadata-schemas.title"/>
				<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#dublincore\"%>"><fmt:message key="jsp.help"/></dspace:popup>
				</h1>
				<%
				String error = (String)request.getAttribute("error");
				if (error!=null) { 
				%>
					<p class="alert alert-danger">
						<%=error%>
					</p>
				<% } %>
				<div class="box-tools pull-right">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
               </div>	
			 </div>
  
   <div class="box-body">
    <table class="table table-bordered">
        <tr>
            <th class="oddRowOddCol"><strong><fmt:message key="jsp.general.id" /></strong></th>
            <th class="oddRowEvenCol"><strong><fmt:message key="jsp.dspace-admin.list-metadata-schemas.namespace"/></strong></th> 
            <th class="oddRowOddCol"><strong><fmt:message key="jsp.dspace-admin.list-metadata-schemas.name"/></strong></th> 
            <th class="oddRowOddCol">&nbsp;</th>
        </tr>

<%
    String row = "even";
    for (int i = 0; i < schemas.length; i++)
    {
%>
        <tr>
            <td class="<%= row %>RowOddCol"><%= schemas[i].getSchemaID() %></td>            
			 <td class="<%= row %>RowEvenCol">
                <a href="<%=request.getContextPath()%>/dspace-admin/Dms-metadata-field-registry?dc_schema_id=<%= schemas[i].getSchemaID() %>"><%= schemas[i].getNamespace() %></a>
            </td>
            <td class="<%= row %>RowOddCol">
                <%= schemas[i].getName() %>
            </td>
            <td class="<%= row %>RowOddCol">
		<% if ( schemas[i].getSchemaID() != 1 ) { %>
                <form method="post" action="">
                    <input type="hidden" name="dc_schema_id" value="<%= schemas[i].getSchemaID() %>"/>
                    <input class="btn btn-primary" type="button" name="submit_update" value="<fmt:message key="jsp.dspace-admin.general.update"/>" onclick="javascript:document.schema.namespace.value='<%= schemas[i].getNamespace() %>';document.schema.short_name.value='<%= schemas[i].getName() %>';document.schema.dc_schema_id.value='<%= schemas[i].getSchemaID() %>';return null;"/>
                    <input class="btn btn-danger" type="submit" name="submit_delete" value="<fmt:message key="jsp.dspace-admin.general.delete-w-confirm"/>"/>
                </form>
		    <% } %>
                </td>
            </tr>
<%
        row = (row.equals("odd") ? "even" : "odd");
    }
%>
    </table>
    </div>
    </div>
	</div>
	</div>

          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
                </div>
				</div>
				  <div class="box-header with-border">
                  <h6 class="box-title">				
                  <fmt:message key="jsp.dspace-admin.list-metadata-schemas.instruction"/>                
				</h5>					
				</div>				
		 <form class="form-horizontal" method="post" name="schema" action="">
			 <div  class="box-body">	     
			 <div class="form-group">
		      <div class="col-sm-2">
			   <input type="hidden" name="dc_schema_id" value=""/>  
			   <label><fmt:message key="jsp.dspace-admin.list-metadata-schemas.namespace"/>:</label>
		 	</div>
			 <div class="col-sm-5">
          	<input class="form-control" type="text" name="namespace" value=""/>
		    </div> 
			<div class="col-sm-5"> </div>
		   </div>
		   
       <div class="form-group">
		   <div class="col-sm-2">
       			<label><fmt:message key="jsp.dspace-admin.list-metadata-schemas.name"/>:</label>
    	   </div>	
       <div class="col-sm-5">		   
       		<input class="form-control" type="text" name="short_name" value=""/>
	    </div>
        <div class="col-sm-5">
		</div>
		</div>
		 <div class="form-group">
		   <div class="col-sm-2">
		   </div>
       <div class="col-sm-5">
       		<input class="btn btn-success col-md-3" type="submit" name="submit_add" value="<fmt:message key="jsp.dspace-admin.general.save"/>"/>
       </div>
	    <div class="col-sm-5">  </div>
	  </div>
	 </div>
  </form>
  </div>
	</div>
	</div>
</dspace:layout>
