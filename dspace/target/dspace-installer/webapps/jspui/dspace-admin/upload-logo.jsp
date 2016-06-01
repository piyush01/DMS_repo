
<%--
  - Form to upload a logo
  -
  - Attributes:
  -    community    - community to upload logo for
  -    collection   - collection to upload logo for - "overrides" community
  -                   if this isn't null
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.content.Collection" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    Collection collection = (Collection) request.getAttribute("collection");
    Community community = (Community) request.getAttribute("community");
    
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.upload-logo.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin" 
               nocache="true">

   <div class="row">
     <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
               <h3 class="box-title"><fmt:message key="jsp.dspace-admin.upload-logo.title"/></h3>   
   
						<p>
							<%
							if (collection != null){
						%>
								<fmt:message key="jsp.dspace-admin.upload-logo.select.col">
									<fmt:param><%= collection.getMetadata("name")%></fmt:param>
								</fmt:message>
						<%	
							}
							else{
						%>
								<fmt:message key="jsp.dspace-admin.upload-logo.select.com">
									<fmt:param><%= community.getMetadata("name")%></fmt:param>
								</fmt:message>
						<%
							}
						%>
						</p>
    	</div>
	
    <form class="form-horizontal" method="post" enctype="multipart/form-data" action="">
       <div class="box-body">	
	  <div class="form-group">
	  <div class="col-sm-2">
	  <label>Select File</label>
	  </div>
	  <div class="col-sm-5">
	     <input type="file" size="40" name="file"/>					  
		 <input type="hidden" name="community_id" value="<%= community.getID() %>" />
		<%  if (collection != null) { %>
				<input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
		<%  } %>
				<%-- <p align="center"><input type="submit" name="submit" value="Upload"/></p> --%>			
		</div>
		</div>
		 <div class="form-group">
	  <div class="col-sm-2">
	  </div>
	  <div class="col-sm-5">
	  <input type="submit" class="btn btn-success" name="submit" value="<fmt:message key="jsp.dspace-admin.general.upload"/>" />
	  </div>
	  </div>
	  
		</div>
    </form>
	</div>
	</div>
	</div>
	</div>
</dspace:layout>
