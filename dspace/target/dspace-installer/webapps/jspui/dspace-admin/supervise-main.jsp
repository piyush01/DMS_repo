<%--
   - This page provides the options for administering supervisor settings
   --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<dspace:layout style="submission"
			   titlekey="jsp.dspace-admin.supervise-main.title"
               navbar="admin"
               locbar="link"
               parentlink="/dspace-admin"
               parenttitlekey="jsp.administer">
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
					<h5><fmt:message key="jsp.dspace-admin.supervise-main.heading"/>
						<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#supervision\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup>				
					</h5>
					 <h5><fmt:message key="jsp.dspace-admin.supervise-main.subheading"/></h5>
					</div>
					
					
					<%-- form to navigate to any of the three options available --%>
					<form class="form-horizontal" method="post" action="">
					<div class="box-body">
			        <div class="form-group">
						<div class="row">
							<input class="btn btn-primary col-md-3 col-md-offset-3" type="submit" name="submit_add" value="<fmt:message key="jsp.dspace-admin.supervise-main.add.button"/>"/>
						</div>
					</div>	
					<div class="form-group">
						<div class="row">
							<input class="btn btn-info col-md-3 col-md-offset-3" type="submit" name="submit_view" value="<fmt:message key="jsp.dspace-admin.supervise-main.view.button"/>"/>
						</div>
						</div>
						<div class="form-group">						  
							<input class="btn btn-warning col-md-3 col-md-offset-3" type="submit" name="submit_clean" value="<fmt:message key="jsp.dspace-admin.supervise-main.clean.button"/>"/>
						</div>
						</div>
						</div>
					</form>
					</div>
					</div>
					</div>
					</section>					
</dspace:layout>
