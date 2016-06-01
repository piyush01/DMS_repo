
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%--
  - policy editor - for new or existing policies
  -
  - Attributes:
  -   policy - a ResourcePolicy to be edited
  -   groups - Group [] of groups to choose from
  -   epeople - EPerson [] of epeople to choose from (unused in first version)
  -   edit_title - title of the page ("Collection 13", etc.
  -   id_name - name of string to put in hidden arg (collection_id, etc.) 
  -   id - ID of the object policy relates to (collection.getID(), etc.)
  -   newpolicy - set to some string value if this is a new policy
  - Returns:
  -   save_policy   - user wants to save a policy
  -   cancel_policy - user wants to cancel, and return to policy list
  -   "id_name"     - name/value passed in from id_name/id above
  -   group_id      - set if user selected a group
  -   eperson_id    - set if user selected an eperson
  -   start_date    - start date of a policy (e.g. for embargo feature)
  -   end_date      - end date of a policy
  -   action_id     - set to whatever user chose
  -   (new policy)  - set to a the string passed in above if policy is a new one
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.authorize.ResourcePolicy" %>
<%@ page import="org.dspace.content.Collection"       %>
<%@ page import="org.dspace.core.Constants"           %>
<%@ page import="org.dspace.eperson.EPerson"          %>
<%@ page import="org.dspace.eperson.Group"            %>


<%
    ResourcePolicy policy = (ResourcePolicy) request.getAttribute("policy"    );
    Group   [] groups     = (Group  []     ) request.getAttribute("groups"    );
    EPerson [] epeople    = (EPerson[]     ) request.getAttribute("epeople"   );
    String edit_title     = (String        ) request.getAttribute("edit_title");
    String id_name        = (String        ) request.getAttribute("id_name"   );
    String id             = (String        ) request.getAttribute("id"        );
    String newpolicy      = (String        ) request.getAttribute("newpolicy" );    
    // calculate the resource type and its relevance ID
    // to check what actions to present
    int resourceType      = policy.getResourceType();
    int resourceRelevance = 1 << resourceType;    
    request.setAttribute("LanguageSwitch", "hide");  
%>


<dspace:layout style="submission" titlekey="jsp.dspace-admin.authorize-policy-edit.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin"
               nocache="true">
        
          <div class="row">
            <div class="col-xs-12">
              <!-- general form elements -->			  
			  <div class="box box-primary">
                <div class="box-header">
				<h4 class="box-title">
				 Edit Policy
					
               </h4>
            </div>
       <form class="form-horizontal" action="<%= request.getContextPath() %>/tools/authorize" method="post">
           <div class="box-body">
		   <div class="form-group">
		   <div class="col-sm-2">
                    <label for="tgroup_id"><fmt:message key="jsp.dspace-admin.general.group-colon"/></label>   
             </div>
         <div class="col-sm-3">			 
                <select class="form-control" size="5" name="group_id" id="tgroup_id">
				<option value="0">--Select Group--</option>
                    <%  for(int i = 0; i < groups.length; i++ ) { %>
                            <option value="<%= groups[i].getID() %>" <%= (groups[i].getID() == policy.getGroupID() ? "selected=\"selected\"" : "" ) %> >
                            <%= groups[i].getName()%>
                            </option>
                        <%  } %>
                </select>
              </div>
           <div class="col-sm-3">
		   </div>
           </div>
		   
		   		   	   <div class="form-group">
		   <div class="col-sm-2">
                    <label for="tgroup_id">Select User</label>   
             </div>
         <div class="col-sm-3">			 
                <select class="form-control" size="5" name="user_id" id="tuser_id">
				<option value="0">--Select User--</option>
                    <%  for(int i = 0; i < epeople.length; i++ ) { %>
                            <option value="<%= epeople[i].getID() %>" <%= (epeople[i].getID() == policy.getEPersonID()? "selected=\"selected\"" : "" ) %> >
                            <%= epeople[i].getFullName()%>
                            </option>
                        <%  } %>
                </select>
              </div>
           <div class="col-sm-3">
		   </div>
           </div>
        	<div class="form-group">
		   <div class="col-sm-2">
        		<label for="taction_id"><fmt:message key="jsp.dspace-admin.general.action-colon"/></label>
        	</div>
        	 <div class="col-sm-3">
                    <input type="hidden" name="<%=id_name%>" value="<%=id%>" />
                    <input type="hidden" name="policy_id" value="<%=policy.getID()%>" />
                    <select class="form-control" name="action_id" id="taction_id">
                        <%  for( int i = 0; i < Constants.actionText.length; i++ )
                                {
                                    // only display if action i is relevant
                                    //  to resource type resourceRelevance                             
                                    if( (Constants.actionTypeRelevance[i]&resourceRelevance) > 0)
                                    { %>
                                        <option value="<%= i %>"
                                        <%=(policy.getAction() == i ? "selected=\"selected\"" : "")%>>
                                        <%= Constants.actionText[i]%>
                                        </option>
                        <%          }
                                } %>
                    </select>
               </div>
			    <div class="col-sm-3"></div>
				</div>
				
                    <%
                    // start and end dates are used for Items and Bitstreams only.
                    if (resourceType == Constants.ITEM || resourceType == Constants.BITSTREAM)
                    {
                    %>
                       <div class="form-group">
		                <div class="col-sm-2">
                            <label for="t_start_date_id">
							<fmt:message key="jsp.dspace-admin.general.policy-start-date-colon"/></label>
                        </div>
                        <div class="col-sm-5">
                            <input class="form-control" name="policy_start_date" maxlength="10" size="10" type="text" 
                                   value="<%= policy.getStartDate() != null ? DateFormatUtils.format(policy.getStartDate(), "yyyy-MM-dd") : "" %>" />
                        </div>
						<div class="col-sm-5"> </div>
						 </div>
                        <!-- policy end date -->
                       <div class="form-group">
		                <div class="col-sm-2">
                             <label for="t_end_date_id"><fmt:message key="jsp.dspace-admin.general.policy-end-date-colon"/></label>
                        </div>
                        <div class="col-sm-5">
                            <input class="form-control" name="policy_end_date" maxlength="10" size="10" type="text" 
                                   value="<%= policy.getEndDate() != null ? DateFormatUtils.format(policy.getEndDate(), "yyyy-MM-dd") : "" %>" />
                        </div>
						 <div class="col-sm-5">  </div>
						  </div>
						
                    <%} // if Item||Bitstream%>
           
			
        <% if( newpolicy != null ) { %> <input name="newpolicy" type="hidden" value="<%=newpolicy%>"/> <% } %>
    
	    <div class="form-group">
				   <div class="col-sm-2">
				   </div>			
                    <div class="col-sm-3">
                    <input class="btn btn-primary" type="submit" name="submit_save_policy" value="<fmt:message key="jsp.dspace-admin.general.save"/>" />             
                   
                    <input class="btn btn-default" type="submit" name="submit_cancel_policy" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
				   </div>
				 <div class="col-sm-3"></div>
			</div>
		</div>
    </form>
	</div>
   </div>
   </div>
   </div>
</dspace:layout>
