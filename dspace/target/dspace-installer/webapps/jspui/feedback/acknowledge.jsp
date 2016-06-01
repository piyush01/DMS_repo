
<%--
  - Feedback received OK acknowledgement
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<dspace:layout titlekey="jsp.feedback.acknowledge.title">

<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
        <div class="box box-primary">
          <div class="box-header with-border">				
		   <h3 class="box-title"><fmt:message key="jsp.feedback.acknowledge.title"/></h3>		   
     <%-- <p>Your comments have been received.</p> --%>
         <p class="alert alert-success"><fmt:message key="jsp.feedback.acknowledge.text"/></p>
        </div>
	</div>
	</div> 
	</div>
	</div>
</dspace:layout>
