<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Show form allowing edit of collection metadata
  -
  - Attributes:
  -    community    - community to create new collection in, if creating one
  -    collection   - collection to edit, if editing an existing one.  If this
  -                  is null, we are creating one.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.eperson.Group" %>
<%@ page import="org.dspace.harvest.HarvestedCollection" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="java.util.Enumeration" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    Collection collection = (Collection) request.getAttribute("collection");
    Community community = (Community) request.getAttribute("community");

    Boolean adminCollection = (Boolean)request.getAttribute("admin_collection");
    boolean bAdminCollection = (adminCollection == null ? false : adminCollection.booleanValue());
    
    Boolean adminCreateGroup = (Boolean)request.getAttribute("admin_create_button");
    boolean bAdminCreateGroup = (adminCreateGroup == null ? false : adminCreateGroup.booleanValue());

    Boolean adminRemoveGroup = (Boolean)request.getAttribute("admin_remove_button");
    boolean bAdminRemoveGroup = (adminRemoveGroup == null ? false : adminRemoveGroup.booleanValue());
    
    Boolean workflowsButton = (Boolean)request.getAttribute("workflows_button");
    boolean bWorkflowsButton = (workflowsButton == null ? false : workflowsButton.booleanValue());
    
    Boolean submittersButton = (Boolean)request.getAttribute("submitters_button");
    boolean bSubmittersButton = (submittersButton == null ? false : submittersButton.booleanValue());
    
    Boolean templateButton = (Boolean)request.getAttribute("template_button");
    boolean bTemplateButton = (templateButton == null ? false : templateButton.booleanValue());

    Boolean policyButton = (Boolean)request.getAttribute("policy_button");
    boolean bPolicyButton = (policyButton == null ? false : policyButton.booleanValue());
    
    Boolean deleteButton = (Boolean)request.getAttribute("delete_button");
    boolean bDeleteButton = (deleteButton == null ? false : deleteButton.booleanValue());
    
    // Is the logged in user a sys admin
    Boolean admin = (Boolean)request.getAttribute("is.admin");
    boolean isAdmin = (admin == null ? false : admin.booleanValue());
    
    HarvestedCollection hc = (HarvestedCollection) request.getAttribute("harvestInstance");
    
    String name = "";
    String shortDesc = "";
    String intro = "";
    String copy = "";
    String side = "";
    String license = "";
    String provenance = "";
    
    String oaiProviderValue= "";
	String oaiSetIdValue= "";
	String metadataFormatValue= "";
	String lastHarvestMsg= "";
	int harvestLevelValue=0;
	int harvestStatus= 0;
	
    Group[] wfGroups = new Group[3];
    wfGroups[0] = null;
    wfGroups[1] = null;
    wfGroups[2] = null;

    Group admins     = null;
    Group submitters = null;

    Item template = null;

    Bitstream logo = null;
    
    if (collection != null)
    {
        name = collection.getMetadata("name");
        shortDesc = collection.getMetadata("short_description");
        intro = collection.getMetadata("introductory_text");
        copy = collection.getMetadata("copyright_text");
        side = collection.getMetadata("side_bar_text");
        provenance = collection.getMetadata("provenance_description");

        if (collection.hasCustomLicense())
        {
            license = collection.getLicense();
        }
        
        wfGroups[0] = collection.getWorkflowGroup(1);
        wfGroups[1] = collection.getWorkflowGroup(2);
        wfGroups[2] = collection.getWorkflowGroup(3);

        admins     = collection.getAdministrators();
        submitters = collection.getSubmitters();

        template = collection.getTemplateItem();

        logo = collection.getLogo();
                
        /* Harvesting stuff */
        if (hc != null) {
			oaiProviderValue = hc.getOaiSource();
			oaiSetIdValue = hc.getOaiSetId();
			metadataFormatValue = hc.getHarvestMetadataConfig();
			harvestLevelValue = hc.getHarvestType();
			lastHarvestMsg= hc.getHarvestMessage();
			harvestStatus = hc.getHarvestStatus();
		}
        
    }
%>
<dspace:layout locbar="commLink" title="Edit"  nocache="true">

			   
		<div class="row">
<h3 class="col-md-8"></h3>    
<% if(bDeleteButton) { %>
              <form class="col-md-4" method="post" action="">
                <input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_DELETE_COLLECTION %>" />
                <input type="hidden" name="community_id" value="<%= community.getID() %>" />
                <input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
                <input class="btn btn-danger col-md-12" type="submit" name="submit_delete" value="<fmt:message key="jsp.tools.edit-collection.button.delete"/>" />
              </form>
<% } %>
</div>
<div class="row">
<form class="form-group" method="post" action="<%= request.getContextPath() %>/tools/edit-communities">
	<div class="col-md-8">
    
<%-- ===========================================================
     Basic metadata
     =========================================================== --%>
     <div class="box box-primary">
     	<div class="box-header"><h3 class="box-title"><strong>Sub Folder</strong>
		<span>		
	</span></h3>
		</div>
     	<div class="box-body">
        	<div class="row">        
                <label class="col-sm-1" for="name"><fmt:message key="jsp.tools.edit-collection.form.label1"/></label>
                <span class="col-md-5">
                	<input class="form-control" type="text" name="name" value="<%= Utils.addEntities(name) %>" />
                </span>
            </div><br/>    
            <br/>
             <div class="btn-group col-md-6">
			 <label class="col-md-3" for=""></label>
			<%
				if (collection == null)
				{
			%>
									<input type="hidden" name="community_id" value="<%= community.getID() %>" />
									<input type="hidden" name="create" value="true" />
									<input class="btn btn-success col-md-3" type="submit" name="submit" value="<fmt:message key="jsp.tools.edit-collection.form.button.create2"/>" />
			<%
				}
				else
				{
			%>
									<input type="hidden" name="community_id" value="<%= community.getID() %>" />
									<input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
									<input type="hidden" name="create" value="false" />
									<input class="btn btn-success col-md-3" type="submit" name="submit" value="<fmt:message key="jsp.tools.edit-collection.form.button.update"/>" />
			<% 
				}
			%>
									<input type="hidden" name="community_id" value="<%= community.getID() %>" />
									<input type="hidden" name="action" value="<%= EditCommunitiesServlet.CONFIRM_EDIT_COLLECTION %>" /> 
									<input class="btn btn-warning col-md-3" type="submit" name="submit_cancel" value="<fmt:message key="jsp.tools.edit-collection.form.button.cancel"/>" />
			</div>  


			
		</div>
	</div>	
</div>
<div class="col-md-4">
<div class="row">
 <div class="box box-primary">
 <div class="box-header">
 <h3 class="box-title"><strong>Sub Folder Settings</strong></h3>
  </div>
            
<% if(bSubmittersButton || bWorkflowsButton || bAdminCreateGroup || (admins != null && bAdminRemoveGroup)) { %>
           

<% }
	
   if(bSubmittersButton) { %>
<%-- ===========================================================
     Collection Submitters
     =========================================================== --%>
            <div class="row">     
                <p class="col-md-6" for="submit_submitters_create"><fmt:message key="jsp.tools.edit-collection.form.label10"/></p>
                <span class="col-md-6 btn-group">
<%  if (submitters == null) {%>
                    <input class="btn btn-success col-md-12" type="submit" name="submit_submitters_create" value="<fmt:message key="jsp.tools.edit-collection.form.button.create"/>" />
<%  } else { %>
                    <input class="btn btn-default col-md-6"  type="submit" name="submit_submitters_edit" value="<fmt:message key="jsp.tools.edit-collection.form.button.edit"/>" />
                    <input class="btn btn-danger col-md-6"  type="submit" name="submit_submitters_delete" value="<fmt:message key="jsp.tools.edit-collection.form.button.delete"/>" />
<%  } %>
				</span>
			</div>              
<%  } %>           

     
 
<% if(bAdminCreateGroup || (admins != null && bAdminRemoveGroup)) { %>


	 
            <div class="row">    
                <p class="col-md-6" for="submit_admins_create"><fmt:message key="jsp.tools.edit-collection.form.label12"/></p>
                <span class="col-md-6 btn-group">
<%  if (admins == null) {
		if (bAdminCreateGroup) {
%>
                    <input class="btn btn-success col-md-12" type="submit" name="submit_admins_create" value="<fmt:message key="jsp.tools.edit-collection.form.button.create"/>" />
<%  	} 
	} 
	else { 
		if (bAdminCreateGroup) {
	%>
                    <input class="btn btn-default" type="submit" name="submit_admins_edit" value="<fmt:message key="jsp.tools.edit-collection.form.button.edit"/>" />
	<%  }
		if (bAdminRemoveGroup) { 
		%>
                    <input class="btn btn-danger" type="submit" name="submit_admins_delete" value="<fmt:message key="jsp.tools.edit-collection.form.button.delete"/>" />
<%  	}
	}	%>        
				</span>
			</div>
<% } %>

  
<% if(bPolicyButton) { %>
     		
			<div class="row">
                <p class="col-md-6" for="submit_authorization_edit"><fmt:message key="jsp.tools.edit-collection.form.label14"/></p>
                <span class="col-md-6 btn-group">
                    <input class="btn btn-success col-md-12" type="submit" name="submit_authorization_edit" value="<fmt:message key="jsp.tools.edit-collection.form.button.edit"/>" />
                </span>
        	</div> 
<%  } %>

<div class="row">
                <p class="col-md-6" for="submit_authorization_edit"></p>
                <span class="col-md-6 btn-group">
                   
                </span>
        	</div> 
		</div>
   </div>
   
	<% if(bAdminCollection) { %>	
	
	  <!--<div class="box box-default" >
       	<div class="box-header"><h2 class="box-title"><fmt:message key="jsp.tools.edit-collection.form.label15"/></h2></div>
		<div class="box-body" style="height: 500px;">
     
     		<div class="row">				
				 <label class="col-lg-12" for="source_normal">Content Source				
				 </label>
				 
					<div class="radio">
                        <label>
                          <input type="radio" value="source_normal" <% if (harvestLevelValue == 0) { %> checked="checked" <% } %> name="source" />
                          <fmt:message key="jsp.tools.edit-collection.form.label17"/>
                        </label>
                      </div>
					  
					<div class="radio">
                        <label>
                          <input type="radio" value="source_normal" <% if (harvestLevelValue == 0) { %> checked="checked" <% } %> name="source" />
                          <fmt:message key="jsp.tools.edit-collection.form.label18"/>
                        </label>
                      </div>  
				 
				
			</div>	
				
				
                <div class="row">
                	<label class="col-lg-12" for="oai_provider"><fmt:message key="jsp.tools.edit-collection.form.label19"/></label>
                	<span class="col-lg-12">
                		<input class="form-control" type="text" name="oai_provider" value="<%= oaiProviderValue %>" size="50" />
                	</span>	
                </div>
				
				
                <div class="row">
                	<label class="col-lg-12" for="oai_setid"><fmt:message key="jsp.tools.edit-collection.form.label20"/></label>
                	<span class="col-lg-12">
                		<input class="form-control" type="text" name="oai_setid" value="<%= oaiSetIdValue %>" size="50" />
                	</span>
                </div>
				
                <div class="row">
                	<label class="col-lg-12" for="metadata_format"><fmt:message key="jsp.tools.edit-collection.form.label21"/></label>
                	<span class="col-lg-12">
                	<select class="form-control" name="metadata_format" >
	                	<%
		                // Add an entry for each instance of ingestion crosswalks configured for harvesting 
			            String metaString = "harvester.oai.metadataformats.";
			            Enumeration pe = ConfigurationManager.propertyNames("oai");
			            while (pe.hasMoreElements())
			            {
			                String key = (String)pe.nextElement();
							
							
			                if (key.startsWith(metaString)) {
			                	String metadataString = ConfigurationManager.getProperty("oai", key);
			                	String metadataKey = key.substring(metaString.length());
								String label = "jsp.tools.edit-collection.form.label21.select." + metadataKey;
		                	
	                	%>
			                	<option value="<%= metadataKey %>" 
			                	<% if(metadataKey.equalsIgnoreCase(metadataFormatValue)) { %> 
			                	selected="selected" <% } %> >
								<fmt:message key="<%=label%>"/>
								</option>
			                	<% 
			                }
			            }
		                %>
					</select>
					</span>
				</div>
				
				<div class="row">
				<br>
					<label class="col-lg-12" for="harvest_level">Content Being Harvested </label>				
				 
					<div class="radio">
                        <label>
                          <input class="col-lg-12" type="radio" value="1" <% if (harvestLevelValue != 2 && harvestLevelValue != 3) { %> checked="checked" <% } %> name="harvest_level" />
                          <fmt:message key="jsp.tools.edit-collection.form.label23"/>
                        </label>
                      </div>					  
					<div class="radio">
                        <label>
                          <input class="col-lg-12" type="radio" value="2" <% if (harvestLevelValue == 2) { %> checked="checked" <% } %> name="harvest_level" />
                          <fmt:message key="jsp.tools.edit-collection.form.label24"/>
                        </label>
                      </div>					
					<div class="radio">
                        <label>
                          <input class="col-lg-12" type="radio" value="3" <% if (harvestLevelValue == 3) { %> checked="checked" <% } %> name="harvest_level" />
                          <fmt:message key="jsp.tools.edit-collection.form.label25"/>
                        </label>
                      </div>					 
			</div>	
				
				
				
                <div class="row">
                <label class="col-lg-12"><fmt:message key="jsp.tools.edit-collection.form.label26"/></label>
                <span class="col-lg-12"><%= lastHarvestMsg %></span>
                </div>	
           				
		</div>
	</div>	-->                
<%  } %>
</div>
                      
    </form>
    </div>
	 </div>
	  </div>
</dspace:layout>
