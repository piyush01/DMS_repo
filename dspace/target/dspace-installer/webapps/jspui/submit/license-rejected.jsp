
<%--
  - License rejected page
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<% request.setAttribute("LanguageSwitch", "hide"); %>

<dspace:layout style="submission" navbar="off" locbar="off" titlekey="jsp.submit.license-rejected.title">

    
	
			    <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
             <div class="box-header with-border">				
		     <h3 class="box-title"><fmt:message key="jsp.submit.license-rejected.heading"/></h3>
	      <p><fmt:message key="jsp.mydspace.remove-item.confirmation"/></p>
		  </div>
	 <div class="box-body">
     <div class="col-sm-12">	
	<p><fmt:message key="jsp.submit.license-rejected.info1"/></p>
    
    <%-- <p>If you wish to contact us to discuss the license, please use one
    of the methods below:</p> --%>
	<p><fmt:message key="jsp.submit.license-rejected.info2"/></p>

    <dspace:include page="/components/contact-info.jsp" />

    <%-- <p><a href="<%= request.getContextPath() %>/mydspace">Go to My DSpace</a></p> --%>
	<p><a class="btn btn-primary" href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.mydspace.general.goto-mydspace"/></a></p>
 </div>
 </div>
  </div>
 </div>
  </div>
</dspace:layout>
