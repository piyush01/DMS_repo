<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Insert summary for versionable item JSP
  -
  - Attributes:
   --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ page import="org.dspace.core.Context" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="org.dspace.submit.step.UploadStep" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%
request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
    request.setAttribute("submission.info", subInfo);
	
    Integer itemID = (Integer)request.getAttribute("itemID");	
	Integer wsid=(Integer)request.getAttribute("wsid");
	
	request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="default" titlekey="jsp.dspace-admin.version-summary.title">
 <div class="row">
				<div class="col-lg-12 ">
				  <div class="box box-primary col-md-4">
					<div class="box-header">
					  <h3 class="box-title"><fmt:message key="jsp.dspace-admin.version-summary.heading"/></h3>
					</div>	-----------<%=wsid%>
            <div class="box-body">
				 <form class="form-horizontal" action="<%= request.getContextPath() %>/submit" enctype="multipart/form-data" method="post">
				 
				   <input type="hidden" name="workspace_item_id" value="<%=wsid%>"/>
				   <input type="hidden" name="step" value="3"/>
				   <input type="hidden" name="page" value="1"/>				  
           <input type="hidden" name="jsp" value="/submit/show-uploaded-file.jsp"/>
					<input type="hidden" name="itemID" value="<%= itemID %>" />
					<p><fmt:message key="jsp.dspace-admin.version-summary.text3"><fmt:param><%= itemID%></fmt:param></fmt:message></p>
							
                   <%--  <td class="submitFormLabel">News:</td> --%>
                   <div class="form-group">
                    	<div class="col-md-3">
						<label for="summary">
						<fmt:message key="jsp.dspace-admin.version-summary.text"/></label>
                    	</div>
						<div class="col-md-4">
						<input class="btn btn-primary" type="file" size="40" name="file" id="tfile" />
						<input class="form-control" type="hidden" name="description" id="tdescription" size="40"/>
						<!--<textarea class="form-control" name="summary" rows="5" cols="5"></textarea> -->
                   </div>
				   </div>
				   
				   <div class="form-group">
                    <div class="col-md-3"> </div>
				   <div class="col-md-3">
				   <input class="btn btn-primary" type="submit" name="<%=UploadStep.SUBMIT_UPLOAD_BUTTON%>" value="<fmt:message key="jsp.submit.general.next"/>" />
				   
					<input class="btn btn-success" type="submit" name="submit_version" value="<fmt:message key="jsp.version.version-summary.submit_version"/>" />
                   
                    <input class="btn btn-default" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
					</div>
					</div>
</form>
</div>
</div>
</div>
</div>
</div>
</dspace:layout>
