
<%--
  - Display list of collections, with continue and cancel buttons
  -  post method invoked with collection_select or collection_select_cancel
  -     (collection_id contains ID of selected collection)
  -
  - Attributes:
  -   collections - a Collection [] containing all collections in the system
  - Returns:
  -   submit set to collection_select, user has selected a collection
  -   submit set to collection_select_cancel, return user to main page
  -   collection_id - set if user has selected one

  --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="org.dspace.content.Collection" %>

<%
    Collection [] collections =(Collection[]) request.getAttribute("collections");       
    request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.collection-select.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">   
<section class="content">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title">Sub Folder Policy</h3>				  
                </div><!-- /.box-header -->                
				<!-- form start --> 
 <form class="form-horizontal" method="post" action="">	
	   <div class="box-body">
	 <div class="form-group">
		<div class="col-sm-2"> 
		 <label>Select Sub Folder<label>
		 </div>
		 <div class="col-sm-3">
	            <select class="form-control" size="#" name="collection_id">
					<option value="0">--Select--</option>
                        <%  for (int i = 0; i < collections.length; i++) { %>
                            <option value="<%= collections[i].getID()%>">
                                <%= collections[i].getMetadata("name")%>
                            </option>
                        <%  } %>
                    </select>
	   </div>
	   <div class="col-sm-5"> </div>
	   </div>
	 <div class="form-group">
		<div class="col-sm-2"> 		
		 </div>
		 <div class="col-sm-3">	
             <input class="btn btn-primary" type="submit" name="submit_collection_select" value="<fmt:message key="jsp.dspace-admin.general.editpolicy"/>" />	
           <input class="btn btn-danger" type="reset" name="submit_cancel_policy" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />					 
         </div>
		<div class="col-sm-5"> 		
		 </div>
		</div>
		</div>
    </form>
	</div>
   </div>
  </div>
  </section>
</dspace:layout>
