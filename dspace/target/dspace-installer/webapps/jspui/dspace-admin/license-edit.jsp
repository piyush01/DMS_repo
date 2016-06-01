
<%--
  - License Edit Form JSP
  -
  - Attributes:
  -  license - The license to edit
   --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@page import="javax.servlet.jsp.jstl.fmt.LocaleSupport"%>
<%
    // Get the existing license
    String license = (String)request.getAttribute("license");
    if (license == null)
    {
    	license = "";
    }

    // Are there any messages to show?
    String message = (String)request.getAttribute("edited");
    boolean edited = false;
    if ((message != null) && (message.equals("true")))
    {
    	edited = true;
    }
    message = (String)request.getAttribute("empty");
    boolean empty = false;
    if ((message != null) && (message.equals("true")))
    {
    	empty = true;
    }
    
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.license-edit.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
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
					<h3 class="box-title">
					 <fmt:message key="jsp.dspace-admin.license-edit.heading"/>
					<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#editlicense\"%>"><fmt:message key="jsp.help"/></dspace:popup>
				   </h3>
				   <%
						if (edited)
						{
							%>
								<p class="alert-warning">
									<strong><fmt:message key="jsp.dspace-admin.license-edit.edited"/></strong>
								</p>
							<%
						}
					%>
					<%
						if (empty)
						{
							%>
								<p class="alert-warning">
									<strong><fmt:message key="jsp.dspace-admin.license-edit.empty"/></strong>
								</p>
							<%
						}
					%>    
            <p class="alert-info"><fmt:message key="jsp.dspace-admin.license-edit.description"/></p>
              </div>
			  
    <form class="form-horizontal" action="<%= request.getContextPath() %>/dspace-admin/license-edit" method="post">
   <div class="box-body">
	   <div class="form-group">
		<div class="col-sm-2">    
       </div>
	   <div class="col-sm-8"> 
       <textarea class="form-control" name="license" rows="15" cols="70"><%= license %></textarea>
	    </div>
        <div class="col-sm-2">    
       </div>
     </div>
	 <div class="form-group">
		<div class="col-sm-2">    
       </div>
	  <div class="col-sm-5"> 
   <input class="btn btn-primary" type="submit" name="submit_save" value="<fmt:message key="jsp.dspace-admin.general.save"/>" />
    <input class="btn btn-default" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
     </div>
	  <div class="col-sm-5">
	 </div>
	 </div> 
	  </div>
    </form>
	</div>
	 </div> 
	  </div>
	  </section>
</dspace:layout>
