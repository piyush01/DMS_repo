
<%--
  - Submission complete message
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.content.InProgressSubmission" %>
<%@ page import="org.dspace.content.Collection"%>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>


<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

	//get collection
    Collection collection = subInfo.getSubmissionItem().getCollection();
%>

<dspace:layout style="default" locbar="commLink" titlekey="jsp.submit.complete.title" nocache="true">

<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">				
		        <h3 class="box-title">	
    <jsp:include page="/submit/progressbar.jsp"/>
 </h3> 
 <br/><br/>
 <h3 class="box-title">	
    <%-- <h1>Submit: Submission Complete!</h1> --%>
	<fmt:message key="jsp.submit.complete.heading"/>
    </h3>
   
	<p class="alert alert-info"><fmt:message key="jsp.submit.complete.info"/></p> 
    <p><a href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.submit.complete.link"/></a></p>
     
    <p><a href="<%= request.getContextPath() %>/community-list"><fmt:message key="jsp.community-list.title"/></a></p>
     </div>
	  <div class="box-body">
    <form action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
        <input type="hidden" name="collection" value="<%= collection.getID() %>"/>
	    <input class="btn btn-success pull-right" type="submit" name="submit" value="<fmt:message key="jsp.submit.complete.again"/>"/>
    </form>
      </div>
	   </div>
	    </div>
		 </div>
		  </div>
</dspace:layout>
