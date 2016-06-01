<%--
  - UI page for selection of collection.
  -
  - Required attributes:
  -    collections - Array of collection objects to show in the drop-down.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Collection" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    //get collections to choose from
    Collection[] collections =
        (Collection[]) request.getAttribute("collections");

	//check if we need to display the "no collection selected" error
    Boolean noCollection = (Boolean) request.getAttribute("no.collection");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);
%>

<dspace:layout style="default" titlekey="jsp.submit.select-collection.title"  nocache="true">
<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        <h3 class="box-title"> Choose SubFolder      </h3>   
		<p>Select the SubFolder you wish to submit an document to from the list below,then click "Next".</p>
	</div>	
<%  if (collections.length > 0)
    {
%>
 <div class="box-body">
    <form action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
	  
<%
		//if no collection was selected, display an error
		if((noCollection != null) && (noCollection.booleanValue()==true))
		{
%>
	<div class="alert alert-danger"><fmt:message key="jsp.submit.select-collection.no-collection"/></div>
<%
		}
%>            
            
					<div class="input-group col-sm-9">
					<label for="tcollection" class="input-group-addon">
						<fmt:message key="jsp.submit.select-collection.collection"/>
					</label>
          <dspace:selectcollection klass="form-control" id="tcollection" collection="-1" name="collection"/>
					</div><br/>
            <%-- Hidden fields needed for SubmissionController servlet to know which step is next--%>
            <%= SubmissionController.getSubmissionParameters(context, request) %>

				<div class="row">
					<div class="col-md-4 pull-right btn-group">
						<input class="btn btn-default col-md-6" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>" value="<fmt:message key="jsp.submit.select-collection.cancel"/>" />
						<input class="btn btn-primary col-md-6" type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>" value="<fmt:message key="jsp.submit.general.next"/>" />
					</div>
				</div>
          				
    </form><br/><br/>
	<div class="form-group">
	<div class="col-sm-4 btn-group">
<%  } else { %>
	<p class="alert alert-danger"><fmt:message key="jsp.submit.select-collection.none-authorized"/></p>
<%  } %>	
	   <p class="btn btn-default"><strong><fmt:message key="jsp.general.goto"/></strong>&nbsp;&nbsp;
	   <a class="btn btn-primary" href="<%= request.getContextPath() %>"><fmt:message key="jsp.general.home"/></a>
	   <a class="btn btn-primary" href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.general.mydspace" /></a>
	   </p>	
	    </div>
	   </div>
	    </div>
	   </div>
	</div>
   </div>	
    </div>
</dspace:layout>
