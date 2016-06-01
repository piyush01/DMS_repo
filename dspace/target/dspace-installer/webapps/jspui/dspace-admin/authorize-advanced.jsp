
<%--
  - Advanced policy tool - a bit dangerous, but powerful
  -
  - Attributes:
  -  collections - Collection [] all DSpace collections
  -  groups      - Group      [] all DSpace groups for select box
  - Returns:
  -  submit_advanced_clear - erase all policies for set of objects
  -  submit_advanced_add   - add a policy to a set of objects
  -  collection_id         - ID of collection containing objects
  -  resource_type         - type, "bitstream" or "item"
  -  group_id              - group that policy relates to
  -  action_id             - action that policy allows
  -
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@page import="org.dspace.eperson.EPerson"%>

<%@ page import="java.util.List"     %>
<%@ page import="java.util.Iterator" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.Collection"       %>
<%@ page import="org.dspace.core.Constants"           %>
<%@ page import="org.dspace.eperson.Group"            %>

<%
    Group      [] groups     = (Group      []) request.getAttribute("groups"     );
	EPerson[] epersons=(EPerson[])request.getAttribute("epersons");
    Collection [] collections= (Collection []) request.getAttribute("collections");
    request.setAttribute("LanguageSwitch", "hide");
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.authorize-advanced.advanced"
               navbar="admin"
               locbar="link"
               parentlink="/dspace-admin"
               parenttitlekey="jsp.administer">

	<section class="content">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title">Sub Folder Policy</h3>				  
                </div><!-- /.box-header -->                
				<!-- form start -->
     <form class="form-horizontal" method="post" action="">
          <div class="box-body">
	 <div class="form-group">
		<div class="col-sm-2"> 
		 <label>
                      <%--   <fmt:message key="jsp.dspace-admin.authorize-advanced.col"/> --%>
          Select Sub Folder
			</label>
			 </div>
			 <div class="col-sm-3">
						<select class="form-control" size="#" name="collection_id" id="tcollection">
						<option value="0">--Select--</option>
						<%  for(int i = 0; i < collections.length; i++ ) { %>
								<option value="<%= collections[i].getID() %>"> <%= collections[i].getMetadata("name")%>
								</option>
							<%  } %>
					</select>
			</div>
			<div class="col-sm-3"></div>
		</div>
		 <div class="form-group">
		<div class="col-sm-2"> 
           <label for="tresource_type"><fmt:message key="jsp.dspace-admin.authorize-advanced.type"/></label>
		</div>
		<div class="col-sm-3">
                <select class="form-control" name="resource_type" id="tresource_type">   
					<option value="0">--Select--</option>				
                    <option value="<%=Constants.ITEM%>"><fmt:message key="jsp.dspace-admin.authorize-advanced.type1"/></option>
                    <option value="<%=Constants.BITSTREAM%>"><fmt:message key="jsp.dspace-admin.authorize-advanced.type2"/></option>
                </select>
     		</div>
         	<div class="col-sm-3"></div>
		</div>
		
             <div class="form-group">
		<div class="col-sm-2"> 
			<label for="tgroup_id"><fmt:message key="jsp.dspace-admin.general.group-colon"/></label>
		</div>
            <div class="col-sm-3">
            	<select class="form-control" size="#" name="group_id" id="tgroup_id">
					<option value="0">--Select--</option>
                    <%  for(int i = 0; i < groups.length; i++ ) { %>
                            <option value="<%= groups[i].getID() %>"> <%= groups[i].getName()%>
                            </option>
                        <%  } %>
                </select>
            </div>
          <div class="col-sm-3"></div>
		</div>
				             <div class="form-group">
		<div class="col-sm-2"> 
			<label for="tgroup_id">User</label>
		</div>
            <div class="col-sm-3">
            	<select class="form-control" size="#" name="user_id" id="tuser_id">
					<option value="0">--Select--</option>
                    <%  for(int i = 0; i < epersons.length; i++ ) { %>
                            <option value="<%= epersons[i].getID() %>"> <%= epersons[i].getFullName()%>
                            </option>
                        <%  } %>
                </select>
            </div>
          <div class="col-sm-3"></div>
		</div>
              <div class="form-group">
		       <div class="col-sm-2">               
				<label for="taction_id"><fmt:message key="jsp.dspace-admin.general.action-colon"/></label>
			   </div>
			 <div class="col-sm-3">
                <select class="form-control" name="action_id" id="taction_id">
					<option value="0">--Select--</option>
                    <%  for( int i = 0; i < Constants.actionText.length; i++ ) { %>
                        <option value="<%= i %>">
                            <%= Constants.actionText[i]%>
                        </option>
                    <%  } %>
                </select>
            </div>
        <div class="col-sm-3"></div>
		</div>
		
	 <div class="form-group">
		       <div class="col-sm-2"> 
            
     	  </div>
   <div class="col-sm-3">
	 <input class="btn btn-primary" type="submit" name="submit_advanced_add" value="<fmt:message key="jsp.dspace-admin.authorize-advanced.add"/>" />
	  <input class="btn btn-danger" type="reset" name="submit_advanced_add" value="Cancel" />
		</div>	   
	<div class="col-sm-3"></div>
		</div>	   
		</div>
    </form>
	</div>	   
	</div>
	</div>
	</section>
</dspace:layout>

