<%--
  - Renders a page containing a statistical summary of the repository usage
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String navbar = (String) request.getAttribute("navbar");
%>
<dspace:layout style="submission" navbar="<%=  navbar %>" titlekey="jsp.statistics.no-report.title">



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
					<h3>					 
					Reports
			        </h3>			 
			   </div>			  
			 
				<div class="box-body">
				<p><fmt:message key="jsp.statistics.no-report.info1"/></p>
							
				</div>
			
			</div>					
			</div>				
			</div>
    </section>
</dspace:layout>
