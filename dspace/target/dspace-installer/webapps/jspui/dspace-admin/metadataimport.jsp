
<%--
  - Form to upload a csv metadata file
--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    String message = (String)request.getAttribute("message");
    if (message == null)
    {
        message = "";
    }
    else
    {
        message = "<p><b>" + message + "</b></p>";
    }
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.metadataimport.title"
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
                    <h3><fmt:message key="jsp.dspace-admin.metadataimport.title"/></h3>
					<%= message %>
                </div>
				
             <form class="form-horizontal" method="post" enctype="multipart/form-data" action="">
				<div class="box-body">
			    <div class="form-group">
				<div class="col-sm-2">
				Select File
				 </div>
		        <div class="col-sm-5">
                   <input type="file" size="40" name="file"/>
               </div>
			    <div class="col-sm-5">
				</div>
				</div>
				 <div class="form-group">
				<div class="col-sm-2">				
				 </div>
		        <div class="col-sm-5">
                 <input class="btn btn-info" type="submit" name="submit" value="<fmt:message key="jsp.dspace-admin.general.upload"/>" />
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
