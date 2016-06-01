<%--
  - News Edit Form JSP
  -
  - Attributes:
   --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.servlet.admin.NewsEditServlet" %>
<%@ page import="org.dspace.core.Constants" %>

<%
    String position = (String)request.getAttribute("position");

    //get the existing news
    String news = (String)request.getAttribute("news");

    if (news == null)
    {
        news = "";
    }

	request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.news-edit.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>			
					</div>
					<h3 class="box-title">
					 <fmt:message key="jsp.dspace-admin.news-edit.heading"/>
					</h3>  
				<p class="alert-info">
				<% if (position.contains("top"))
				   { %>
					
				<% }
				   else
				   { %>
					<fmt:message key="jsp.dspace-admin.news-edit.text.sidebar"/>
				<% } %>
				 </p>	 
				<p class="alert-info"><fmt:message key="jsp.dspace-admin.news-edit.text3"/>
	         </p>
              </div>
			
    <form class="form-horizontal" action="<%= request.getContextPath() %>/dspace-admin/news-edit" method="post">
	 <div class="box-body">
	   <div class="form-group">
		<div class="col-sm-2"> 	
		   <label><fmt:message key="jsp.dspace-admin.news-edit.news"/></label>
          </div>  		
		  <div class="col-sm-8">        
        <textarea class="form-control" name="news" rows="10" cols="50"><%= news %></textarea>
		 </div>
		 	<div class="col-sm-2">  </div>
    </div>  
	<div class="form-group">
		<div class="col-sm-2"> 	
		   </div>
		   <div class="col-sm-5"> 	
			<input type="hidden" name="position" value='<%= position %>'/>       
			<input class="btn btn-primary" type="submit" name="submit_save" value="<fmt:message key="jsp.dspace-admin.general.save"/>" />        
		<input class="btn btn-default" type="submit" name="cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
      </div>
	   <div class="col-sm-5">
	</div>
	</div> 
	 </div>	 
    </form>
	</div>
	</div> 
	 </div>	
</dspace:layout>
