<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Profile updated message
  -
  -   password.updated - a Boolean indicating whether the user updated their
  -                      password.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.eperson.EPerson" %>

<%
    boolean passwordUpdated =
        ((Boolean) request.getAttribute("password.updated")).booleanValue();
%>


<dspace:layout titlekey="jsp.register.profile-updated.title">
   <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title">
   <%-- <h1>Profile Updated</h1> --%>
	<fmt:message key="jsp.register.profile-updated.title"/></h3></div>
	<div class="box-body">
    <%-- <p>Thank you, your profile information
    <%= (passwordUpdated ? "and password have" : "has") %>
    been updated.</p> --%>   
	<p class="alert alert-info">
<% if(passwordUpdated)
   { %>
    <fmt:message key="jsp.register.profile-updated.info1"/>
<% }
   else
   { %>
    <fmt:message key="jsp.register.profile-updated.info2"/>
<% } %>
    </p>

    <%-- <p><a href="<%= request.getContextPath() %>/">Return to DSpace Home</a></p> --%>
	<p><a href="<%= request.getContextPath() %>/"><fmt:message key="jsp.register.general.return-home"/></a></p>
	</div>
	</div>
	</div>
	</div>
	</div>
</dspace:layout>
