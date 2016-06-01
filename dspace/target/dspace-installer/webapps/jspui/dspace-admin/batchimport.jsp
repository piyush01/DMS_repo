<%--
  - Form to upload a metadata files
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.util.List"            %>
<%@ page import="java.util.ArrayList"            %>
<%@ page import="org.dspace.content.Collection"            %>

<%
	List<String> inputTypes = (List<String>)request.getAttribute("input-types");
	List<Collection> collections = (List<Collection>)request.getAttribute("collections");
	String hasErrorS = (String)request.getAttribute("has-error");
	boolean hasError = (hasErrorS==null) ? false : (Boolean.parseBoolean((String)request.getAttribute("has-error")));
	
	String uploadId = (String)request.getAttribute("uploadId");
	
    String message = (String)request.getAttribute("message");
    
	List<String> otherCollections = new ArrayList<String>();
	if (request.getAttribute("otherCollections")!=null) {
		otherCollections = (List<String>)request.getAttribute("otherCollections");
	}
		
	Integer owningCollectionID = null;
	if (request.getAttribute("owningCollection")!=null){
		owningCollectionID = (Integer)request.getAttribute("owningCollection");
	}
	
	String selectedInputType = null;
	if (request.getAttribute("inputType")!=null){
		selectedInputType = (String)request.getAttribute("inputType");
	}
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.batchimport.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin" 
               nocache="true">
			   
	<section class="content">
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
					<h3><fmt:message key="jsp.dspace-admin.batchimport.title"/></h3>
						<% if (uploadId != null) { %>
							<div style="color:red">-- <fmt:message key="jsp.dspace-admin.batchimport.resume.info"/> --</div>		
						<% } %>   
						
						<%
							if (hasErrorS == null){
							
							}
							else if (hasError && message!=null){
						%>
							<div class="alert alert-danger"><%= message %></div>
						<%  
							}
							else if (hasError && message==null){
						%>
								<div class="alert alert-danger"><fmt:message key="jsp.dspace-admin.batchmetadataimport.genericerror"/>
								</div>
						<%  
							}
							else {
						%>
								<div class="batchimport-info alert alert-info">
									<fmt:message key="jsp.dspace-admin.batchimport.info.success">
										<fmt:param><%= request.getContextPath() %>/mydspace</fmt:param>
									</fmt:message>
								</div>
						<%  
							}
						%>
					<div id="error-alert"></div>
					</div>
    		


			<form method="post" class="form-horizontal" action="<%= request.getContextPath() %>/dspace-admin/batchimport" enctype="multipart/form-data">
			<div class="box-body">
			<div class="form-group">
		      <div class="col-sm-4">
			<label for="inputType"><fmt:message key="jsp.dspace-admin.batchmetadataimport.selectinputfile"/></label>
			</div>
			   <div class="col-sm-5">
	        <select class="form-control" name="inputType" id="import-type">
					<%
						String safuploadSelected = ("safupload".equals(selectedInputType)) ? "selected" : "";
						String safSelected = ("saf".equals(selectedInputType)) ? "selected" : "";
					%>
						<option <%= safuploadSelected %> value="safupload"><fmt:message key="jsp.dspace-admin.batchimport.saf.upload"/></option>
						<option <%= safSelected %> value="saf"><fmt:message key="jsp.dspace-admin.batchimport.saf.remote"/></option>
			<% 
					for (String inputType : inputTypes){
						String selected = (inputType.equals(selectedInputType)) ? "selected" : "";
			%> 			
						<option <%= selected %> value="<%= inputType %>"><%= inputType %></option>	
			<%
					}
			%>      </select>
 		</div> 
		<div class="col-sm-3"></div>
		</div>
		
 		
		<% if (uploadId != null) { %>
			<input type="hidden" name=uploadId value="<%= uploadId %>"/>
		<% } %>
			<div class="form-group">
		    <div class="col-sm-4"  id="input-url">	
			 <label for="collection">
			 <fmt:message key="jsp.dspace-admin.batchmetadataimport.selecturl"/></label>
			 </div>
			<div class="col-sm-5">	
			<input type="text" name="zipurl" class="form-control"/>
           </div>		   
		   <div class="col-sm-3">
		   </div>
		</div>		   
        <div class="form-group">
        <div class="col-sm-4" id="input-file">
			<label for="file"><fmt:message key="jsp.dspace-admin.batchmetadataimport.selectfile"/></label>
	    </div>	
		<div class="col-sm-5">
            <input type="file" size="40" name="file" />
        </div>
		<div class="col-sm-3">
		   </div>
		</div>	 
        <div class="form-group">
			 <div class="col-sm-4">
			<label for="collection">
				<fmt:message key="jsp.dspace-admin.batchmetadataimport.selectowningcollection"/>
				<span id="owning-collection-optional"><fmt:message key="jsp.dspace-admin.batchmetadataimport.selectowningcollection.optional"/></span>
			</label>
			 </div>
			<div class="col-sm-5">
			<i for="collection"><fmt:message key="jsp.dspace-admin.batchmetadataimport.selectowningcollection.info"/></i>			
            <select class="form-control" name="collection" id="owning-collection-select">
				<option value="-1"><fmt:message key="jsp.dspace-admin.batchmetadataimport.select"/></option>
				 <% 
						for (Collection collection : collections){
								String selected = ((owningCollectionID != null) && (owningCollectionID == collection.getID())) ? "selected" : "";
				%> 			
								<option <%= selected %> value="<%= collection.getID() %>"><%= collection.getName() %></option>	
				 <%
						}
				 %>           	
            </select>
        </div>
        <div class="col-sm-3">
		   </div>
		</div>
		
		<% String displayValue = owningCollectionID != null ? "display:block" : "display:none"; %>
           <div class="form-group" id="other-collections-div" style="<%= displayValue %>">
			 <div class="col-sm-4">	
			<label for="collection"><fmt:message key="jsp.dspace-admin.batchmetadataimport.selectothercollections"/></label>
			</div>
			<div class="col-sm-5">
            <select class="form-control" name="collections" multiple style="height:100px" id="other-collections-select">
				 <% 
						for (Collection collection : collections){
							String selected = ((otherCollections != null) && (otherCollections.contains(""+collection.getID()))) ? "selected" : "";
				%> 				
							<option <%= selected %> value="<%= collection.getID() %>"><%= collection.getName() %></option>	
				 <%
						}
				 %>           	
            </select>
        </div>
		 <div class="col-sm-3">
		   </div>
		</div>
        <div class="form-group">
	     <div class="col-sm-4"> </div>
		  <div class="col-sm-5">
        <input class="btn btn-success" type="submit" name="submit" value="<fmt:message key="jsp.dspace-admin.general.upload"/>" />
          </div>
		 <div class="col-sm-3">
		  </div>
		</div>
		</div>		
    </form>
	 </div>
	</div>
  </div>
</section>
    
    <script>
	    $( "#import-type" ).change(function() {
	    	var index = $("#import-type").prop("selectedIndex");
	    	if (index == 1){
	    		$( "#input-file" ).hide();
	    		$( "#input-url" ).show();
	    		$( "#owning-collection-info" ).show();
	    		$( "#owning-collection-optional" ).show();
	    	}
	    	else {
	    		$( "#input-file" ).show();
	    		$( "#input-url" ).hide();
	    		$( "#owning-collection-info" ).hide();
	    		$( "#owning-collection-optional" ).hide();
	    	}
	    });
		
		$( "#owning-collection-select" ).change(function() {
	    	var index = $("#owning-collection-select").prop("selectedIndex");
	    	if (index == 0){
	    		$( "#other-collections-div" ).hide();
				$( "#other-collections-select > option" ).attr("selected",false);
	    	}
	    	else {
	    		$( "#other-collections-div" ).show();
	    	}
	    });
    </script>
    
    
</dspace:layout>