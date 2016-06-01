<%--
  - Display list of DC types
  -
  - Attributes:
  -
  -   formats - the DC formats in the system (MetadataValue[])
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="java.lang.String" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.MetadataField" %>
<%@ page import="org.dspace.content.MetadataSchema" %>


<%
    MetadataField[] types =
        (MetadataField[]) request.getAttribute("types");
    MetadataSchema schema =
        (MetadataSchema) request.getAttribute("schema");
    MetadataSchema[] schemas =
        (MetadataSchema[]) request.getAttribute("schemas");
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.list-metadata-fields.title"
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
				 <h3><fmt:message key="jsp.dspace-admin.list-metadata-fields.addfield"/></h3>
                <p class="alert alert-info"><fmt:message key="jsp.dspace-admin.list-metadata-fields.addfieldnote"/></p>		
				<div class="box-tools pull-right">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>               
               </div>
				<p class="alert alert-info">
					<fmt:message key="jsp.dspace-admin.list-metadata-fields.note"/>
				</p>			   
			 </div>
	   <form class="form-horizontal" method="post" action="">
	   <div class="box-body">
			<div class="form-group">
		      <div class="col-sm-2">			
              <input type="hidden" name="dc_schema_id" value="<%= schema.getSchemaID() %>"/>                  
			  <label><fmt:message key="jsp.dspace-admin.list-metadata-fields.element"/>:</label>
			 </div>
                 <div class="col-sm-5">
				 <input class="form-control" type="text" name="element"/>
               </div>
			   <div class="col-sm-5"> </div>
			 </div>
			 <div class="form-group">
		      <div class="col-sm-2">
                <label><fmt:message key="jsp.dspace-admin.list-metadata-fields.qualifier"/>:</label>
				 </div>
                     <div class="col-sm-5">   
					 <input class="form-control" type="text" name="qualifier"/>
              </div>
			  <div class="col-sm-5">
			  </div>
			 </div> 
			<!-- <div class="form-group">
		      <div class="col-sm-2">
                      <label><fmt:message key="jsp.dspace-admin.list-metadata-fields.scope"/>:</label>
			 </div>
			  <div class="col-sm-5">
                <textarea class="form-control" name="scope_note" rows="3" cols="40"></textarea>
			 </div><div class="col-sm-5">
			 </div>
			 </div> -->
               <div class="form-group">
		      <div class="col-sm-2"> </div>
			   <div class="col-sm-5">
            <input class="btn btn-primary" type="submit" name="submit_add" value="<fmt:message key="jsp.dspace-admin.general.addnew"/>"/>
			</div><div class="col-sm-5">
			</div>
			 </div> 
         </div> 
      </form>
		</div>
		 </div> 
         </div> 
			   
			   
			   
			   
           <div class="row">
            <!-- left column -->
             <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<h4><fmt:message key="jsp.dspace-admin.list-metadata-fields.title"/>
				 <a href="<%=request.getContextPath()%>/dspace-admin/metadata-schema-registry">
					<fmt:message key="jsp.dspace-admin.list-metadata-fields.schemas"/>
				</a> |
				 <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#dublincore\"%>"><fmt:message key="jsp.help"/></dspace:popup>
			   </h4>	   
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
               </div>
				<p class="alert alert-info">
					<fmt:message key="jsp.dspace-admin.list-metadata-fields.note"/>
				</p>			   
			 </div>

	<div class="box-body"> 
		<table class="table" summary="Dublic Core Type Registry data table">
           <tr>
              <th class="oddRowOddCol">
                 <strong>
                            <fmt:message key="jsp.general.id" /> 
                            / <fmt:message key="jsp.dspace-admin.list-metadata-fields.element"/> 
                            / <fmt:message key="jsp.dspace-admin.list-metadata-fields.qualifier"/> 
                            / <fmt:message key="jsp.dspace-admin.list-metadata-fields.scope"/>
                 </strong>
              </th>
           </tr>
           
<%
    String row = "even";
    for (int i = 0; i < types.length; i++)
    {
%>
      <tr>
         <td>
             <form class="form-inline" method="post" action="">
                 <span class="col-md-1"><%= types[i].getFieldID() %></span>

                    <div class="form-group">
                    	<label class="sr-only" for="element"><fmt:message key="jsp.dspace-admin.list-metadata-fields.element"/></label>
                		<input class="form-control" type="text" name="element" value="<%= types[i].getElement() %>" size="12" placeholder="<fmt:message key="jsp.dspace-admin.list-metadata-fields.element"/>"/>
                	</div>
                    <div class="form-group">
                    	<label class="sr-only" for="qualifier"><fmt:message key="jsp.dspace-admin.list-metadata-fields.qualifier"/></label>
                		<input class="form-control" type="text" name="qualifier" value="<%= (types[i].getQualifier() == null ? "" : types[i].getQualifier()) %>" size="12" placeholder="<fmt:message key="jsp.dspace-admin.list-metadata-fields.qualifier"/>"/>
                	</div>                         
                    <div class="form-group">
                    	<label class="sr-only" for="scope_note"><fmt:message key="jsp.dspace-admin.list-metadata-fields.scope"/></label>
                		<textarea class="form-control" name="scope_note" rows="3" cols="40"><%= (types[i].getScopeNote() == null ? "" : types[i].getScopeNote()) %></textarea>
                	</div>                             
                         
					<div class="btn-group pull-right">                             
                         
                            <input type="hidden" name="dc_type_id" value="<%= types[i].getFieldID() %>"/>
                            <input class="btn btn-primary" type="submit" name="submit_update" value="<fmt:message key="jsp.dspace-admin.general.update"/>"/>             
                         
                            <input class="btn btn-danger" type="submit" name="submit_delete" value="<fmt:message key="jsp.dspace-admin.general.delete-w-confirm"/>"/>
                    </div>     
             </form>
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
				  <h3><fmt:message key="jsp.dspace-admin.list-metadata-fields.move"/></h3>
               	
				<div class="box-tools pull-right">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>              
               </div>
				<% if (schemas.length > 1) { %>
					<p class="alert-info">
					<fmt:message key="jsp.dspace-admin.list-metadata-fields.movenote"/>
					</p>					  
				  <% } else { %>
				  <p class="alert alert-info"><fmt:message key="jsp.dspace-admin.list-metadata-fields.moveformnote"/></p>				  
			<% } %> 
			 </div>
	   <form class="form-horizontal" method="post" action="">
	    
			<% if (schemas.length > 1) { %>
					<div class="box-body">
			<div class="form-group">
		      <div class="col-sm-2">				  
				   <label><fmt:message key="jsp.dspace-admin.list-metadata-fields.element"/>:</label>
				 </div>
               <div class="col-sm-5">				 
				  <select class="form-control" name="dc_field_id" multiple="multiple" size="5">
			<%
				for (int i = 0; i < types.length; i++)
				{
				  String qualifier = (types[i].getQualifier() == null ? "" : "."+types[i].getQualifier());
			%>     <option value="<%= types[i].getFieldID() %>"><%= types[i].getElement()+qualifier %></option>
			<%  }
			%>
				  </select>
				  </div> <div class="col-sm-5">	</div>
				 </div>  
				 <div class="form-group">
		          <div class="col-sm-2">
				  <label><fmt:message key="jsp.dspace-admin.list-metadata-fields.schema"/>: </label>
				  </div>  
				  <div class="col-sm-5">
				  <select class="form-control" name="dc_dest_schema_id">
			<%
				for (int i = 0; i < schemas.length; i++)
				{
						  if (schemas[i].getSchemaID() != schema.getSchemaID())
						  {
			%>      <option value="<%= schemas[i].getSchemaID() %>"><%= schemas[i].getNamespace() %></option>
			<%            }
				}
			%>
				  </select>
				  </div> 
				  <div class="col-sm-5">	</div>
				 </div> 
				  <div class="form-group">
		          <div class="col-sm-2"></div>
				   <div class="col-sm-5">
					<input class="btn btn-primary" type="submit" name="submit_move" value="<fmt:message key="jsp.dspace-admin.list-metadata-fields.movesubmit"/>"/>
					</div>
				<div class="col-sm-5">
					</div>
				 </div> 					
					
			<% }%>			  
		</div>    
	   </form>
	   </div>
	   </div>
	   </div>

</dspace:layout>
