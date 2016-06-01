
<%--
  - Submission cancelled and removed page - displayed whenever the user has
  - clicked "cancel/save" during a submission and elected to remove the item.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<% request.setAttribute("LanguageSwitch", "hide"); %>

<dspace:layout locbar="on" titlekey="jsp.submit.cancelled-removed.title">
	 <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
             <div class="box-header with-border">				
		       
	      </div>
	 <div class="box-body">
     <div class="col-sm-12">
    <h3 class="box-title">
				<fmt:message key="jsp.submit.cancelled-removed.title"/>   
	</h3>
	<p><fmt:message key="jsp.submit.cancelled-removed.info"/></p>
    
   
	<p><a class="btn btn-info pull-right" href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.mydspace.general.goto-mydspace"/></a></p>
    </div>
	
	</div>
</div>
</div>
</div>
</div>
</dspace:layout>
