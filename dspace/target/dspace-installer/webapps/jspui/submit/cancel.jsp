
<%--
  - Cancel or save submission page
  -
  - This page is displayed whenever the user clicks "cancel" during a
  - submission.  It asks them whether they want to delete the incomplete
  - item or leave it so they can continue later.
  -
  - Attributes to pass in:
  -    submission.info  - the SubmissionInfo object
  -    step             - the step the user was at when the cancelled
  -                       (as a String)
  -    display.step -   - this is the step to display in the progress bar
  -                       (i.e. the step from the user's perspective, rather
  -                       than the exact JSP the user clicked cancel on)
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
%>

<dspace:layout style="submission"
			   locbar="on"
               navbar="on"
               titlekey="jsp.submit.cancel.title"
               nocache="true">
	 <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">				
		        <h3 class="box-title">
    <form action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
        <jsp:include page="/submit/progressbar.jsp"/>		
                 </h3>
				 <h3><fmt:message key="jsp.submit.cancel.title"/></h3>
	    	<p><fmt:message key="jsp.submit.cancel.info"/></p>
				 </div>
				  <div class="box-body">
		<%-- Hidden fields needed for SubmissionController servlet to know which step is next--%>
        <%= SubmissionController.getSubmissionParameters(context, request) %>
        <input type="hidden" name="cancellation" value="true" />
		<div class="pull-right">
			<input class="btn btn-default" type="submit" name="submit_back" value="<fmt:message key="jsp.submit.cancel.continue.button"/>" />
			<input class="btn btn-danger" type="submit" name="submit_remove" value="<fmt:message key="jsp.submit.cancel.remove.button"/>" />
			<input class="btn btn-success" type="submit" name="submit_keep" value="<fmt:message key="jsp.submit.cancel.save.button"/>" />
        </div>
		  </div>
    </form>
 </div>
  </div>
   </div>
</dspace:layout>
