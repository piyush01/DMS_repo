
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.io.IOException" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="javax.servlet.jsp.PageContext" %>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.util.DCInputSet" %>
<%@ page import="org.dspace.app.util.DCInput" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.DCLanguage" %>
<%@ page import="org.dspace.content.Metadatum" %>
<%@ page import="org.dspace.content.InProgressSubmission" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.core.Utils" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
  String version=(String)session.getAttribute("version");
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
    
	//get list of review JSPs from VerifyServlet
	HashMap reviewJSPs = (HashMap) request.getAttribute("submission.review");

	//get an iterator to loop through the review JSPs to load
	Iterator reviewIterator = reviewJSPs.keySet().iterator();
%> 

<dspace:layout style="default" locbar="commLink" titlekey="jsp.submit.review.title" nocache="true">

  <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        <h3 class="box-title">				 
		
    <form action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
   
        <jsp:include page="/submit/progressbar.jsp" />
			Submition								
				</h3>
						

     <!--   <p><fmt:message key="jsp.submit.review.info1"/></p>
        <div class="alert alert-info"><fmt:message key="jsp.submit.review.info2"/></div>-->
          <br/>
        <p><b>If everything is OK,</b> please click the "Submit" button at the bottom of the page.</p>       
		</div>
		<div class="box-body">  
		  <div class="form-group">
   <div class="col-sm-12">   		
<%
		//loop through the list of review JSPs
		while(reviewIterator.hasNext())
     {
            //remember, the keys of the reviewJSPs hashmap is in the
            //format: stepNumber.pageNumber
            String stepAndPage = (String) reviewIterator.next();

			//finally get the path to the review JSP (the value)
			String reviewJSP = (String) reviewJSPs.get(stepAndPage);
	%>
		    <div class="well row">
				<%--Load the review JSP and pass it step & page info--%>
				<jsp:include page="<%=reviewJSP%>">
					<jsp:param name="submission.jump" value="<%=stepAndPage%>" />	
				</jsp:include>
			</div>	
<%
    }

%>
		
       
        <%= SubmissionController.getSubmissionParameters(context, request) %>
</div>
    </div>
        <div class="col-md-6 pull-right btn-group">
			<input class="btn btn-default col-md-4" type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>" value="<fmt:message key="jsp.submit.review.button.previous"/>" />
          	<input class="btn btn-default col-md-4" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>" value="<fmt:message key="jsp.submit.review.button.cancelsave"/>" />
          	<input class="btn btn-primary col-md-4" type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>" value="Submit" />
        </div>
		
</div>
    </form>
</div>
</div>
</div>
</div>
</dspace:layout>
