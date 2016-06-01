
<%--
  - Feedback form JSP
  -
  - Attributes:
  -    feedback.problem  - if present, report that all fields weren't filled out
  -    authenticated.email - email of authenticated user, if any
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    boolean problem = (request.getParameter("feedback.problem") != null);
    String email = request.getParameter("email");

    if (email == null || email.equals(""))
    {
        email = (String) request.getAttribute("authenticated.email");
    }

    if (email == null)
    {
        email = "";
    }

    String feedback = request.getParameter("feedback");
    if (feedback == null)
    {
        feedback = "";
    }

    String fromPage = request.getParameter("fromPage");
    if (fromPage == null)
    {
		fromPage = "";
    }
%>

<dspace:layout titlekey="jsp.feedback.form.title">
    <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
             <div class="box-header with-border">				
		     <h3 class="box-title"><fmt:message key="jsp.feedback.form.title"/></h3>
	      <p><fmt:message key="jsp.feedback.form.text1"/></p>
		 	
<%
    if (problem)
    {
%>
        <%-- <p><strong>Please fill out all of the information below.</strong></p> --%>
        <p><strong><fmt:message key="jsp.feedback.form.text2"/></strong></p>
<%
    }
%>
 </div>
	 
    <form class="form-horizontal" action="<%= request.getContextPath() %>/feedback" method="post">
        <div class="box-body">
		<div class="form-group">
       <div class="col-sm-2">
          <label for="temail"><fmt:message key="jsp.feedback.form.email"/></label>
		  </div>
		   <div class="col-sm-5">
		  <input type="text" name="email" id="temail" size="50" value="<%=StringEscapeUtils.escapeHtml(email)%>" />
		  </div>
		   </div>
		 <div class="form-group">
       <div class="col-sm-2">
                   <label for="tfeedback"><fmt:message key="jsp.feedback.form.comment"/></label>
				    </div>
			<div class="col-sm-5">
			<textarea name="feedback" id="tfeedback" rows="6" cols="50"><%=StringEscapeUtils.escapeHtml(feedback)%></textarea>
			 </div>
			</div> 
			
                <div class="form-group">
       <div class="col-sm-2"></div> 
	  <div class="col-sm-5">
	  <input class="btn btn-primary" type="submit" name="submit" value="<fmt:message key="jsp.feedback.form.send"/>" />
                    </div> 
                </div> 
            </div>
      
    </form>
</div>
</div>
</div>
</div>
</dspace:layout>
