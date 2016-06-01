
<%--
  - Message indicating that there are no entries in the browse index.
  -
  - Attributes required:
  -    community      - community the browse was in, or null
  -    collection     - collection the browse was in, or null
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page  import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.content.Collection" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

	String layoutNavbar = "default";
	if (request.getAttribute("browseWithdrawn") != null || request.getAttribute("browsePrivate") != null)
	{
	    layoutNavbar = "admin";
	}

	// get the BrowseInfo object
	BrowseInfo bi = (BrowseInfo) request.getAttribute("browse.info");

    // Retrieve community and collection if necessary
    Community community = null;
    Collection collection = null;

    if (bi.inCommunity())
    {
    	community = (Community) bi.getBrowseContainer();
    }
    
    if (bi.inCollection())
    {
    	collection = (Collection) bi.getBrowseContainer();
    }
    
    // FIXME: this is not using the i18n
    // Description of what the user is actually browsing, and where link back
    String linkText = LocaleSupport.getLocalizedMessage(pageContext, "jsp.general.home");
    String linkBack = "/";

    if (collection != null)
    {
        linkText = collection.getMetadata("name");
        linkBack = "/handle/" + collection.getHandle();
    }
    else if (community != null)
    {
        linkText = community.getMetadata("name");
        linkBack = "/handle/" + community.getHandle();
    }
%>

<dspace:layout style="submission" titlekey="browse.no-results.title" navbar="<%= layoutNavbar %>">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					
					</div>
			    	<h1><fmt:message key="browse.no-results.title"/></h1>
				</div>
				<div class="box-body">		
				<p>
				<%
					if (collection != null)
					{
			   %>
							<fmt:message key="browse.no-results.col">
								<fmt:param><%= collection.getMetadata("name")%></fmt:param>
							</fmt:message>
			   <%
					}
					else if (community != null)
					{
			   %>
					<fmt:message key="browse.no-results.com">
						<fmt:param><%= community.getMetadata("name")%></fmt:param>
					</fmt:message>
			   <%
					}
					else
					{
			   %>
					<fmt:message key="browse.no-results.genericScope"/>
			   <%
					}
			   %>
			 </p>
    		<p><a href="<%= request.getContextPath() %><%= linkBack %>"><%= linkText %></a></p>
		</div>
		</div>
		</div>
		</div>
	</div>
</dspace:layout>
