<%--
  - Navigation bar for admin pages
  --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page import="org.dspace.sort.SortOption" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/menu.css" type="text/css" />
<%
	// Is anyone logged in?
	EPerson user = (EPerson) request.getAttribute("dspace.current.user");

    // Get the current page, minus query string
    String currentPage = UIUtil.getOriginalURL(request);    
    int c = currentPage.indexOf( '?' );
    if( c > -1 )
    {
        currentPage = currentPage.substring(0, c);
    }
    
    // E-mail may have to be truncated
    String navbarEmail = null;
    if (user != null)
    {
        navbarEmail = user.getEmail();
    }

%>
		<nav class="navbar navbar-static-top" role="navigation">
		<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>     
	   	   <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
			 <!-- User Account Menu -->
              <li class="dropdown user user-menu">         
		<!-- Menu Toggle Button -->
							   <%
					if (user != null)
					{
						%>
						<!-- Menu Toggle Button -->
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								  <!-- The user image in the navbar-->
								  <i class="fa fa-user"></i>
								  <!-- hidden-xs hides the username on small devices so only the image appears. -->
								  <span class="hidden-xs">
								  <fmt:message key="jsp.layout.navbar-default.loggedin">
								  <fmt:param><%= StringUtils.abbreviate(navbarEmail, 20) %></fmt:param> </fmt:message> </span>
								</a>
						<%
					} else {
						%>
							 <a href="#" class="dropdown-toggle" data-toggle="dropdown">
								  <!-- The user image in the navbar-->
								  <i class="fa fa-user"></i>
								  <!-- hidden-xs hides the username on small devices so only the image appears. -->
								  <span class="hidden-xs"> <fmt:message key="jsp.layout.navbar-default.sign"/> </span>
								  <i class="fa fa-angle-down pull-right"></i>
								</a>
					<% } %>
		             
             <ul class="dropdown-menu"><!-- Menu Body -->
                  <li class="user-body">
                    <div class="col-xs-4 text-center">
                      <a href="/jspui/mydspace">My DMS</a></div>
                    <div class="col-xs-4 text-center">
                      <a href="/jspui/subscribe">Receive email Updates</a></div>
                    <div class="col-xs-4 text-center">
                      <a href="/jspui/profile">Edit Profile</a></div>
					</li> 
				<li class="user-footer">
                    <div class="pull-left">
                      <a href="/jspui/dspace-admin" class="btn btn-default btn-flat">Administer</a>
                    </div>               
				 <div class="pull-right">
                      <a href="/jspui/logout" class="btn btn-default btn-flat">Logout</a>
                    </div>		     		
              </li>
			  </ul>
			</li>
			
            </ul>
          </div>	   	   
	   
</nav>
