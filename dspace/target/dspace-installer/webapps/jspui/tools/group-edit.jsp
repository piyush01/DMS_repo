<%--
  - Show contents of a group (name, epeople)
  -
  - Attributes:
  -   group - group to be edited
  -
  - Returns:
  -   cancel - if user wants to cancel
  -   add_eperson - go to group_eperson_select.jsp to choose eperson
  -   change_name - alter name & redisplay
  -   eperson_remove - remove eperson & redisplay
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.eperson.Group"   %>
<%@ page import="org.dspace.core.Utils" %>

<%
    Group group = (Group) request.getAttribute("group");
    EPerson [] epeople = (EPerson []) request.getAttribute("members");
    
	Group   [] groups  = (Group   []) request.getAttribute("membergroups");
	request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="submission" titlekey="jsp.tools.group-edit.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin"
               nocache="true">
<div class="row">
			<div class="col-md-12">
			 <!-- general form elements -->
					  <div class="box box-primary">
						<div class="box-header with-border">
						<h3 class="box-title">
						Create/Edit Group
							
						</h3>
                   <!--<div class="alert alert-warning"><fmt:message key="jsp.tools.group-edit.heading"/></div>		-->				
   					  </div>

    <form class="form-horizontal" name="epersongroup" method="post" action="">
	 <div class="box-body">
	 <div class="row">
	 <label for="tgroup_name" class="col-md-2">
		<fmt:message key="jsp.tools.group-edit.name"/></label>
	<span class="col-md-3">
		<input class="form-control" name="group_name" id="tgroup_name" value="<%= Utils.addEntities(group.getName()) %>"/>
	</span>
	</div>
	
    <input type="hidden" name="group_id" value="<%=group.getID()%>"/>
    
    <div class="row"><br/>
	 <div class="col-md-2"></div>
    <div class="col-md-4"> 
	    <label for="eperson_id">User<!--<fmt:message key="jsp.tools.group-edit.eperson"/>--></label>
	    <dspace:selecteperson multiple="true" selected="<%= epeople %>"/> 
    </div>
    
    <div class="col-md-3">
	    <label for="eperson_id">Group User<!--<fmt:message key="jsp.tools.group-edit.group"/>--></label>
	    <dspace:selectgroup   multiple="true" selected="<%= groups  %>"/>
	</div>
	</div>
	<br/>
    <div class="row"><input class="btn btn-success  col-md-offset-5" type="submit" name="submit_group_update" value="Save" onclick="javascript:finishEPerson();finishGroups();"/></div>
  </div>
  </form>
  </div>
  </div>
  </div>
</dspace:layout>
