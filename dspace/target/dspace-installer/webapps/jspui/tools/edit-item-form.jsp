<%--
  - Show form allowing edit of collection metadata
  -
  - Attributes:
  -    item        - item to edit
  -    collections - collections the item is in, if any
  -    handle      - item's Handle, if any (String)
  -    dc.types    - MetadataField[] - all metadata fields in the registry
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
    prefix="c" %>

<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="javax.servlet.jsp.PageContext" %>

<%@ page import="org.dspace.content.MetadataField" %>
<%@ page import="org.dspace.app.webui.servlet.admin.AuthorizeAdminServlet" %>
<%@ page import="org.dspace.app.webui.servlet.admin.EditItemServlet" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>
<%@ page import="org.dspace.content.Bundle" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.Metadatum" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.content.authority.MetadataAuthorityManager" %>
<%@ page import="org.dspace.content.authority.ChoiceAuthorityManager" %>
<%@ page import="org.dspace.content.authority.Choices" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.dspace.workflowprocess.WorkflowProcessManager" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%
    Item item = (Item) request.getAttribute("item");
    String handle = (String) request.getAttribute("handle");
    Collection[] collections = (Collection[]) request.getAttribute("collections");
    MetadataField[] dcTypes = (MetadataField[])  request.getAttribute("dc.types");
    HashMap metadataFields = (HashMap) request.getAttribute("metadataFields");
    request.setAttribute("LanguageSwitch", "hide");

    // Is anyone logged in?
    EPerson user = (EPerson) request.getAttribute("dspace.current.user");
    
    // Is the logged in user an admin of the item
    Boolean itemAdmin = (Boolean)request.getAttribute("admin_button");
    boolean isItemAdmin = (itemAdmin == null ? false : itemAdmin.booleanValue());
    
    Boolean policy = (Boolean)request.getAttribute("policy_button");
    boolean bPolicy = (policy == null ? false : policy.booleanValue());
    
    Boolean delete = (Boolean)request.getAttribute("delete_button");
    boolean bDelete = (delete == null ? false : delete.booleanValue());

    Boolean createBits = (Boolean)request.getAttribute("create_bitstream_button");
    boolean bCreateBits = (createBits == null ? false : createBits.booleanValue());

    Boolean removeBits = (Boolean)request.getAttribute("remove_bitstream_button");
    boolean bRemoveBits = (removeBits == null ? false : removeBits.booleanValue());

    Boolean ccLicense = (Boolean)request.getAttribute("cclicense_button");
    boolean bccLicense = (ccLicense == null ? false : ccLicense.booleanValue());
    
    Boolean withdraw = (Boolean)request.getAttribute("withdraw_button");
    boolean bWithdraw = (withdraw == null ? false : withdraw.booleanValue());
    
    Boolean reinstate = (Boolean)request.getAttribute("reinstate_button");
    boolean bReinstate = (reinstate == null ? false : reinstate.booleanValue());

    Boolean privating = (Boolean)request.getAttribute("privating_button");
    boolean bPrivating = (privating == null ? false : privating.booleanValue());
    
    Boolean publicize = (Boolean)request.getAttribute("publicize_button");
    boolean bPublicize = (publicize == null ? false : publicize.booleanValue());

    Boolean reOrderBitstreams = (Boolean)request.getAttribute("reorder_bitstreams_button");
    boolean breOrderBitstreams = (reOrderBitstreams != null && reOrderBitstreams);
    WorkflowProcessManager workflowProcessManager=new WorkflowProcessManager();
	Context context = UIUtil.obtainContext(request);
	Boolean isWorkflowMode= workflowProcessManager.getItemWorkflowMode(context,item.getID());
    // owning Collection ID for choice authority calls
    int collectionID = -1;
	String message=(String)(request.getAttribute("msg")==null?"":request.getAttribute("msg"));
    if (collections.length > 0)
        collectionID = collections[0].getID();
	
%>


<dspace:layout style="default" locbar="commLink" titlekey="jsp.tools.edit-item-form.title" nocache="true">
		  
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>					
					</div>
					<%
					 if(message!=null && !message.equals(""))
					 {	
                   %>
				   <p class="text-success box-title"><%=message%></p>
				   <%				 
					 }					
					%>
					
					<h3 class="box-title">
					<%
					if (item.isWithdrawn())
							{
						%>
							<%-- <p align="center"><strong>This item was withdrawn from DSpace</strong></p> --%>
								<p class="alert-primary"><strong><fmt:message key="jsp.tools.edit-item-form.msg"/></strong></p>
						<%
							}
						%></h3>
					</div> 
			<div class="box-body">
			<form class="form-horizontal" id="edit_metadata" name="edit_metadata" method="post" action="<%= request.getContextPath() %>/tools/edit-item">
			
			<div class="form-group">
				<div class="col-sm-3"> 
				<strong><fmt:message key="jsp.tools.edit-item-form.elem1"/></strong>
				</div>
				<div class="col-sm-3">
				<strong><fmt:message key="jsp.tools.edit-item-form.elem3"/></strong>
				</div>
				</div>
				
				<%
					MetadataAuthorityManager mam = MetadataAuthorityManager.getManager();
					ChoiceAuthorityManager cam = ChoiceAuthorityManager.getManager();
					Metadatum[] dcv = item.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
					String row = "even";    
					// Keep a count of the number of values of each element+qualifier
					// key is "element" or "element_qualifier" (String)
					// values are Integers - number of values that element/qualifier so far
					Map<String, Integer> dcCounter = new HashMap<String, Integer>();
					
					for (int i = 0; i < dcv.length; i++)
					{
						// Find out how many values with this element/qualifier we've found

						String key = ChoiceAuthorityManager.makeFieldKey(dcv[i].schema, dcv[i].element, dcv[i].qualifier);

						Integer count = dcCounter.get(key);
						if (count == null)
						{
							count = new Integer(0);
						}
						
						// Increment counter in map
						dcCounter.put(key, new Integer(count.intValue() + 1));

						// We will use two digits to represent the counter number in the parameter names.
						// This means a string sort can be used to put things in the correct order even
						// if there are >= 10 values for a particular element/qualifier.  Increase this to
						// 3 digits if there are ever >= 100 for a single element/qualifer! :)
						String sequenceNumber = count.toString();
						
						while (sequenceNumber.length() < 2)
						{
							sequenceNumber = "0" + sequenceNumber;
						}
				 %>
				       <%
					    if(dcv[i].element.equals("description") || dcv[i].element.equals("identifier") || dcv[i].element.equals("date") )
						{
						}
						else{						
					   %>
							<div class="form-group">
							<div class="col-sm-3"> 
							 <%
							 if(dcv[i].element.equals("contributor"))
							 {
							  %>
							  Owner Name
                            <%							  
							 } 
							  else if(dcv[i].element.equals("ownersno")){
							%>
							No of owners
							<%							
							  }
							   else if(dcv[i].element.equals("plotno") || dcv[i].element.equals("title")){
							%>
							Plot No							
							<%}
							 else if(dcv[i].element.equals("allotmentdate")){
							%>
							 Date of allotment
							 <%
							 }
							  else{
							 %>							 
							<%= dcv[i].element%>
							<%
							  }
							%>
							
							</div>               
							<div class="col-sm-3">
							<input type="text" value="<%= dcv[i].value %>" class="form-control" id="value_<%= key %>_<%= sequenceNumber %>" name="value_<%= key %>_<%= sequenceNumber %>" >
							</div> 
							</div> 
							
					<% 					
					}
					} 
					%>           
       
		<div class="form-group">
				<div class="col-sm-3"> </div>
				<div class="col-sm-3">
        <input type="hidden" name="item_id" value="<%= item.getID() %>"/>
            <input type="hidden" name="action" value="<%= EditItemServlet.UPDATE_ITEM %>"/>
                        <input class="btn btn-primary" type="submit" name="submit" value="<fmt:message key="jsp.tools.general.update"/>" />
						<input class="btn btn-default" type="submit" name="submit_cancel" value="<fmt:message key="jsp.tools.general.cancel"/>" />
				</div>
				</div>
			 </form>	
      </div>
</div>
</div> 
</div>		
</div>
</dspace:layout>
