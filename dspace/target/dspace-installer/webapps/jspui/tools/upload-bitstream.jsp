<%--
  - Form to upload a bitstream
  -
  - Attributes:
  -    item - the item the bitstream will be added to
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.content.Item" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    Item item = (Item) request.getAttribute("item");
    request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="submission" titlekey="jsp.tools.upload-bitstream.title"
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
					<h5>
					  <%-- <h1>Upload Bitstream</h1> --%>
               <fmt:message key="jsp.tools.upload-bitstream.title"/></h5>			 
			   </div>			  
			   <form class="form-horizontal" method="post" enctype="multipart/form-data" action="">
				<div class="box-body">
				<div class="form-group">
				<div class="col-sm-3">   
					<label><fmt:message key="jsp.tools.upload-bitstream.info"/></label>	
					
				</div><div class="col-sm-5"> 
				<input class="form-control" type="file" size="40" name="file"/>
				</div>
				<div class="col-sm-4"> 
				</div>				
				<input type="hidden" name="item_id" value="<%= item.getID() %>"/>
				</div>
				<div class="form-group">
				<div class="col-sm-3">   
								
				</div>
				<div class="col-sm-5"> 
				<input class="btn btn-success col-md-4" type="submit" name="submit" value="<fmt:message key="jsp.tools.upload-bitstream.upload"/>" />
				</div>
				<div class="col-sm-4"> 
				</div>					
				</div>				
				</div>
			</form>
			</div>					
			</div>				
			</div>
    </section>
</dspace:layout>
