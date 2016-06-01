<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@page import="java.util.ArrayList" %>	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.lang.String" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.MetadataField" %>
<%@ page import="org.dspace.content.MetadataSchema" %>
<%@ page import="org.dspace.content.Collection" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.dspace.eperson.EPerson" %>

<script type="text/javascript">      
	  $(document).ready(function()
      {   
			$('#update_fiels').submit(function() {
		if ($.trim($('#collection').val()) == 0) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Select Folder.</div>');			
		$('#collection').focus();
     return false;
	 }
     });
	 });	 
	</script>
<%
Collection [] collections =(Collection[]) request.getAttribute("collections");
boolean errormessage=(request.getAttribute("errormessage1")!=null);
%>


<dspace:layout style="submission" titlekey="jsp.dspace-admin.user-main.title"
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
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>					
					</div>
					<h3 class="box-title">
					Please enter the following information. Required fields are marked with a (<span class="star">*</span>)</p>
					</h3>
					<div id="error-alert"></div>	
					<%if(errormessage){ %>
					<p class="alert alert-warning">
						  Please Create new field to update files.
						   </p>
					<%} %>	 
			  </div>
					
<div  class="box-body">
    <form method="post" class="form-horizontal" action="" id="update_fiels" name="update_fiels">
 	     
			 <div class="form-group">
		      <div class="col-md-2">
				Document Type<span class="star">*</span>
			</div>
				<div class="col-md-3">
				<select class="form-control" name="collection_id" id="collection">
					<option value="0" seleceted>--Select Document Type--</option>
                        <%  for (int i = 0; i < collections.length; i++) { %>
                            <option value="<%= collections[i].getID()%>">
                                <%= collections[i].getMetadata("name")%>
                            </option>
                        <%  } %>
                    </select>
                  </div>
				  <div class="col-md-3"></div>
				</div>
     <div class="form-group">
		      <div class="col-md-2"></div>
			  <div class="col-md-3">
			<input class="btn btn-primary" id="inputform" type="submit" name="submit_update_input_form" value="Next"/>
			<!--<input class="btn btn-primary" type="submit" name="submit_update_browse" value="Update Browse Fields"/>
			<input class="btn btn-primary" type="submit" name="submit_update_searchFields" value="Update Search Field"/>--></td>
		</div>
      <div class="col-md-2"></div>
	</div>    
</form> 
</div>  
</div>
 </div>
 </div>

</dspace:layout>
