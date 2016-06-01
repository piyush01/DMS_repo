
<%--
  - Display list of Groups, with 'edit' and 'delete' buttons next to them
  -
  - Attributes:
  -
  -   groups - Group [] of groups to work on
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Constants" %>


<%
    String news = (String)request.getAttribute("news");

    if (news == null)
    {
        news = "";
    }

%>

<dspace:layout style="submission" titlekey ="jsp.dspace-admin.news-main.title"
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
                  <h3 class="box-title"><fmt:message key="jsp.dspace-admin.item-select.heading"/>
					<fmt:message key="jsp.dspace-admin.news-main.heading"/>					
				</h3>
				</div>                			
		<form class="form-horizontal" action="<%= request.getContextPath() %>/dspace-admin/news-edit" method="post">		<div class="box-body">
	   <div class="form-group">
		<div class="col-sm-2"> 	
          </div>  		
		  <div class="col-sm-3">
			<select class="form-control" name="position" size="2">
				<option value="<fmt:message key="news-top.html"/>"><fmt:message key="jsp.dspace-admin.news-main.news.top"/></option>			
			</select>
			</div>
			  <div class="col-sm-5"></div>
	     </div>
		 <div class="form-group">
		<div class="col-sm-2"> 	
          </div>  		
		  <div class="col-sm-5">
			<input class="btn btn-primary" type="submit" name="submit_edit" value="<fmt:message key="jsp.dspace-admin.general.edit"/>" />
			</div> 
			 <div class="col-sm-5"></div>
	     </div>
		 </div>
 		</form>	
			</div>
	   </div>
	  </div>
	</section>
</dspace:layout>
