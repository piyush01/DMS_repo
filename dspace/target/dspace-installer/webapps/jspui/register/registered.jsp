<%--
  - Registered OK message
  -
  - Displays a message indicating that the user has registered OK.
  - 
  - Attributes to pass in:
  -   eperson - eperson who's just registered
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.servlet.RegisterServlet" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.core.Utils" %>

<%
    EPerson eperson = (EPerson) request.getAttribute("eperson");
%>

<dspace:layout titlekey="jsp.register.registered.title">
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
					<%-- <h1>Registration Complete</h1> --%>
					<h3><fmt:message key="jsp.register.registered.title"/></h3>	
					
			   </div>	
			 <div class="box-body">	 
			 <center> 
				<p class="alert alert-success"><fmt:message key="jsp.register.registered.thank">
					<fmt:param><%= Utils.addEntities(eperson.getFirstName()) %></fmt:param>
					</fmt:message>
				<br/><fmt:message key="jsp.register.registered.info"/></p>   

	         <p><a href="<%= request.getContextPath() %>/"><fmt:message key="jsp.register.general.return-home"/></a></p>
		    </div>	
			 </div>	
			  </div>
 </div>		
</section> 
</div>	  
</dspace:layout>
