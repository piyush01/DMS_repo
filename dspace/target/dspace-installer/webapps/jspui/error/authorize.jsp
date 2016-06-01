
<%--
  - Page representing an authorization error
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
	
<%@ page isErrorPage="true" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%

String message=(String)request.getParameter("message");

%>

<dspace:layout titlekey="jsp.error.authorize.title">

    <%-- <h1>Authorization Required</h1> --%>
	<div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">	
                <% if(message==null || message.equals("")) {%>
				
				 <h3 class="box-title"><strong><fmt:message key="jsp.error.authorize.title"/></strong></h3>   
				<p><fmt:message key="jsp.error.authorize.text1"/></p>
				<%}%>
  </div>
  <div>
	  <div class="box-body"> 
		<% if(message==null || message.equals("")) {%>
		<p><fmt:message key="jsp.error.authorize.text2"/></p>

		<dspace:include page="/components/contact-info.jsp" />

		<p align="center">
			
			<a href="<%= request.getContextPath() %>/"><fmt:message key="jsp.general.gohome"/></a>
		</p>
		<%}%>
		
		<% if(message!=null && !message.equals("") && message.equals("Y"))
									 {
										 %>
										 <div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Cabinet/Folder/Sub Folder has been successfully updated!</div>
										 <%
									 }
			 %>
		
		</div>
	
	</div>
	</div>
	</div>
	</div>
	</div>
</dspace:layout>
