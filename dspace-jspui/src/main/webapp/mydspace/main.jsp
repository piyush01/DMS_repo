
<%--
  - Main My DSpace page
  -
  -
  - Attributes:
  -    mydspace.user:    current user (EPerson)
  -    workspace.items:  WorkspaceItem[] array for this user
  -    workflow.items:   WorkflowItem[] array of submissions from this user in
  -                      workflow system
  -    workflow.owned:   WorkflowItem[] array of tasks owned
  -    workflow.pooled   WorkflowItem[] array of pooled tasks
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page  import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.app.webui.servlet.MyDSpaceServlet" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.Metadatum" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.content.SupervisedItem" %>
<%@ page import="org.dspace.content.WorkspaceItem" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.eperson.Group"   %>
<%@ page import="org.dspace.workflow.WorkflowItem" %>
<%@ page import="org.dspace.workflow.WorkflowManager" %>
<%@ page import="java.util.List" %>
<%@page import="org.dspace.app.itemimport.BatchUpload"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    EPerson user = (EPerson) request.getAttribute("mydspace.user");

    WorkspaceItem[] workspaceItems =
        (WorkspaceItem[]) request.getAttribute("workspace.items");

    WorkflowItem[] workflowItems =
        (WorkflowItem[]) request.getAttribute("workflow.items");

    WorkflowItem[] owned =
        (WorkflowItem[]) request.getAttribute("workflow.owned");

    WorkflowItem[] pooled =
        (WorkflowItem[]) request.getAttribute("workflow.pooled");
	
    Group [] groupMemberships =
        (Group []) request.getAttribute("group.memberships");

    SupervisedItem[] supervisedItems =
        (SupervisedItem[]) request.getAttribute("supervised.items");
    
    List<String> exportsAvailable = (List<String>)request.getAttribute("export.archives");
    
    List<BatchUpload> importsAvailable = (List<BatchUpload>)request.getAttribute("import.uploads");
    
    // Is the logged in user an admin
    Boolean displayMembership = (Boolean)request.getAttribute("display.groupmemberships");
    boolean displayGroupMembership = (displayMembership == null ? false : displayMembership.booleanValue());
    boolean submission_message=(request.getAttribute("submission.message")!=null);

//for microsoft office
String msword = request.getParameter("url");
session.setAttribute("fileurl",msword);
String ss=(String)session.getAttribute("fileurl");  
%>
<dspace:layout style="default" titlekey="jsp.mydspace" nocache="true">

 <!-- Info boxes -->
 				
				<%if(submission_message){ %>
					<h5 class="box-title"><p class="text-success">
						  Document has been successfully add!
					</p></h5>
					<%} %>
				
				
				
          <div class="row">
		  <div class="col-sm-12">
		  <div class="panel panel-primary col-sm-12" style="height:70px"><br/>			
		  <form action="<%= request.getContextPath() %>/mydspace" method="post">
		  <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>" />
            <div class="col-md-2 col-sm-4 col-xs-6">              
				 <input class="btn btn-primary" type="submit" name="submit_new" value="<fmt:message key="jsp.mydspace.main.start.button"/>" /><!-- /.info-box-content -->
            <!-- /.info-box -->
            </div><!-- /.col -->
			
            <div class="col-md-3 col-sm-4 col-xs-6">
             	<input class="btn btn-primary col-md-12" type="submit" name="submit_own" value="<fmt:message key="jsp.mydspace.main.view.button"/>" />				               
               
            </div>
			<!-- /.col -->  
			
           <!-- <div class="col-md-2 col-sm-4 col-xs-6">             
		      <input class="btn btn-primary col-md-12" type="submit" name="submit_view_submission" value="View Task Submission" />				  
            </div /.col -->			
            <!--<div class="col-md-2 col-sm-4 col-xs-6">              
			<input class="btn btn-primary col-md-12" type="submit" name="submit_view_doc" value="View Workflow Document" />	 
            </div> /.col -->
			<!--<c:if test="${totId >0}">
			<div class="col-md-2 col-sm-4 col-xs-6">            			  
				<input class="btn btn-primary col-md-12" type="submit" name="submit_supervisor_task" value="Supervisor Task" />
          </div> /.col 
			</c:if>-->
			<!--<div class="col-md-2 col-sm-4 col-xs-6">             
				  <input class="btn btn-primary col-md-12" type="submit" name="submit_task" value="My Workflow Task" />
            </div> /.col -->
			
			</form>
			</div>
			</div>
          </div><!-- /.row -->
		
		
<%-- Task list:  Only display if the user has any tasks --%>
<%
    if (owned.length > 0)
    {
%>
 <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        <h3 class="box-title">				
              <fmt:message key="jsp.mydspace.main.heading2"/>
			  </h3>
			<p class="submitFormHelp">
				<%-- Below are the current tasks that you have chosen to do. --%>
				<fmt:message key="jsp.mydspace.main.text1"/>
			</p>
        </div>
		 <div class="box-body">
    <table class="table" align="center" summary="Table listing owned tasks">
        <tr>
            <th id="t1" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.task"/></th>
            <th id="t2" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.item"/></th>
            <th id="t3" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.subto"/></th>
            <th id="t4" class="oddRowEvenCol"><fmt:message key="jsp.mydspace.main.subby"/></th>
            <th id="t5" class="oddRowEvenCol">&nbsp;</th>
        </tr>
<%
        // even or odd row:  Starts even since header row is odd (1).  Toggled
        // between "odd" and "even" so alternate rows are light and dark, for
        // easier reading.
        String row = "even";

        for (int i = 0; i < owned.length; i++)
        {
            Metadatum[] titleArray =
                owned[i].getItem().getDC("title", null, Item.ANY);
            String title = (titleArray.length > 0 ? titleArray[0].value
                                                  : LocaleSupport.getLocalizedMessage(pageContext,"jsp.general.untitled") );
            EPerson submitter = owned[i].getItem().getSubmitter();
%>
        <tr>
                <td headers="t1" class="<%= row %>RowOddCol">
<%
            switch (owned[i].getState())
            {

            //There was once some code...
            case WorkflowManager.WFSTATE_STEP1: %><fmt:message key="jsp.mydspace.main.sub1"/><% break;
            case WorkflowManager.WFSTATE_STEP2: %><fmt:message key="jsp.mydspace.main.sub2"/><% break;
            case WorkflowManager.WFSTATE_STEP3: %><fmt:message key="jsp.mydspace.main.sub3"/><% break;
            }
%>
                </td>
                <td headers="t2" class="<%= row %>RowEvenCol"><%= Utils.addEntities(title) %></td>
                <td headers="t3" class="<%= row %>RowOddCol"><%= owned[i].getCollection().getMetadata("name") %></td>
                <td headers="t4" class="<%= row %>RowEvenCol"><a href="mailto:<%= submitter.getEmail() %>"><%= Utils.addEntities(submitter.getFullName()) %></a></td>
                <!-- <td headers="t5" class="<%= row %>RowOddCol"></td> -->
                <td headers="t5" class="<%= row %>RowEvenCol">
                     <form action="<%= request.getContextPath() %>/mydspace" method="post">
                        <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>" />
                        <input type="hidden" name="workflow_id" value="<%= owned[i].getID() %>" />  
                        <input class="btn btn-primary" type="submit" name="submit_perform" value="<fmt:message key="jsp.mydspace.main.perform.button"/>" />  
                        <input class="btn btn-default" type="submit" name="submit_return" value="<fmt:message key="jsp.mydspace.main.return.button"/>" />
                     </form> 
                </td>
        </tr>
<%
            row = (row.equals("even") ? "odd" : "even" );
        }
%>
    </table>
	</div>
	 </div>
	  </div>
	   </div>
	
<%
    }

    // Pooled tasks - only show if there are any
    if (pooled.length > 0)
    {
%>

<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        <h3 class="box-title">				
             <fmt:message key="jsp.mydspace.main.heading3"/>
			  </h3>
			<p class="submitFormHelp">
			<%--Below are tasks in the task pool that have been assigned to you. --%>
			<fmt:message key="jsp.mydspace.main.text2"/>
         </p>
        </div>       
 <div class="box-body">
    <table class="table" align="center" summary="Table listing the tasks in the pool">
        <tr>
            <th id="t6" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.task"/></th>
            <th id="t7" class="oddRowEvenCol"><fmt:message key="jsp.mydspace.main.item"/></th>
            <th id="t8" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.subto"/></th>
            <th id="t9" class="oddRowEvenCol"><fmt:message key="jsp.mydspace.main.subby"/></th>
            <th class="oddRowOddCol"> </th>
        </tr>
<%
        // even or odd row:  Starts even since header row is odd (1).  Toggled
        // between "odd" and "even" so alternate rows are light and dark, for
        // easier reading.
        String row = "even";

        for (int i = 0; i < pooled.length; i++)
        {
            Metadatum[] titleArray =
                pooled[i].getItem().getDC("title", null, Item.ANY);
            String title = (titleArray.length > 0 ? titleArray[0].value
                    : LocaleSupport.getLocalizedMessage(pageContext,"jsp.general.untitled") );
            EPerson submitter = pooled[i].getItem().getSubmitter();
%>
        <tr>
                    <td headers="t6" class="<%= row %>RowOddCol">
<%
            switch (pooled[i].getState())
            {
            case WorkflowManager.WFSTATE_STEP1POOL: %><fmt:message key="jsp.mydspace.main.sub1"/><% break;
            case WorkflowManager.WFSTATE_STEP2POOL: %><fmt:message key="jsp.mydspace.main.sub2"/><% break;
            case WorkflowManager.WFSTATE_STEP3POOL: %><fmt:message key="jsp.mydspace.main.sub3"/><% break;
            }
%>
                    </td>
                    <td headers="t7" class="<%= row %>RowEvenCol"><%= Utils.addEntities(title) %></td>
                    <td headers="t8" class="<%= row %>RowOddCol"><%= pooled[i].getCollection().getMetadata("name") %></td>
                    <td headers="t9" class="<%= row %>RowEvenCol"><a href="mailto:<%= submitter.getEmail() %>"><%= Utils.addEntities(submitter.getFullName()) %></a></td>
                    <td class="<%= row %>RowOddCol">
                        <form action="<%= request.getContextPath() %>/mydspace" method="post">
                            <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>" />
                            <input type="hidden" name="workflow_id" value="<%= pooled[i].getID() %>" />
                            <input class="btn btn-default" type="submit" name="submit_claim" value="<fmt:message key="jsp.mydspace.main.take.button"/>" />
                        </form> 
                    </td>
        </tr>
<%
            row = (row.equals("even") ? "odd" : "even");
        }
%>
    </table>
	 </div>
	  </div>
	   </div>
	    </div>
<%
    }

    // Display workspace items (authoring or supervised), if any
    if (workspaceItems.length > 0 || supervisedItems.length > 0)
    {
        // even or odd row:  Starts even since header row is odd (1)
        String row = "even";
%>

<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        <h3 class="box-title">				
           <fmt:message key="jsp.mydspace.main.heading4"/>
			  </h3>
			<p class="submitFormHelp">
			<%--Below are tasks in the task pool that have been assigned to you. --%>
			<fmt:message key="jsp.mydspace.main.text4" />
         </p>
        </div>       
	<div class="box-body"> 
    <table class="table" align="center" summary="Table listing unfinished submissions">
        <tr>
            <th class="oddRowOddCol">&nbsp;</th>
            <th id="t10" class="oddRowEvenCol"><fmt:message key="jsp.mydspace.main.subby"/></th>
            <th id="t11" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.elem1"/></th>
            <th id="t12" class="oddRowEvenCol"><fmt:message key="jsp.mydspace.main.elem2"/></th>
            <th id="t13" class="oddRowOddCol">&nbsp;</th>
        </tr>
<%
        if (supervisedItems.length > 0 && workspaceItems.length > 0)
        {
%>
        <tr>
            <th colspan="5">
                <%-- Authoring --%>
                <fmt:message key="jsp.mydspace.main.authoring" />
            </th>
        </tr>
<%
        }

        for (int i = 0; i < workspaceItems.length; i++)
        {
            Metadatum[] titleArray =
                workspaceItems[i].getItem().getDC("title", null, Item.ANY);
            String title = (titleArray.length > 0 ? titleArray[0].value
                    : LocaleSupport.getLocalizedMessage(pageContext,"jsp.general.untitled") );
            EPerson submitter = workspaceItems[i].getItem().getSubmitter();
%>
        <tr>
            <td class="<%= row %>RowOddCol">
                <form action="<%= request.getContextPath() %>/workspace" method="post">
                    <input type="hidden" name="workspace_id" value="<%= workspaceItems[i].getID() %>"/>
                    <input class="btn btn-default" type="submit" name="submit_open" value="<fmt:message key="jsp.mydspace.general.open" />"/>
                </form>
            </td>
            <td headers="t10" class="<%= row %>RowEvenCol">
                <a href="mailto:<%= submitter.getEmail() %>"><%= Utils.addEntities(submitter.getFullName()) %></a>
            </td>
            <td headers="t11" class="<%= row %>RowOddCol"><%= Utils.addEntities(title) %></td>
            <td headers="t12" class="<%= row %>RowEvenCol"><%= workspaceItems[i].getCollection().getMetadata("name") %></td>
            <td headers="t13" class="<%= row %>RowOddCol">
                <form action="<%= request.getContextPath() %>/mydspace" method="post">
                    <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>"/>
                    <input type="hidden" name="workspace_id" value="<%= workspaceItems[i].getID() %>"/>
                    <input class="btn btn-danger" type="submit" name="submit_delete" value="<fmt:message key="jsp.mydspace.general.remove" />"/>
                </form> 
            </td>
        </tr>
<%
            row = (row.equals("even") ? "odd" : "even" );
        }
%>

<%-- Start of the Supervisors workspace list --%>
<%
        if (supervisedItems.length > 0)
        {
%>
        <tr>
            <th colspan="5">
                <fmt:message key="jsp.mydspace.main.supervising" />
            </th>
        </tr>
<%
        }

        for (int i = 0; i < supervisedItems.length; i++)
        {
            Metadatum[] titleArray =
                supervisedItems[i].getItem().getDC("title", null, Item.ANY);
            String title = (titleArray.length > 0 ? titleArray[0].value
                    : LocaleSupport.getLocalizedMessage(pageContext,"jsp.general.untitled") );
            EPerson submitter = supervisedItems[i].getItem().getSubmitter();
%>

        <tr>
            <td class="<%= row %>RowOddCol">
                <form action="<%= request.getContextPath() %>/workspace" method="post">
                    <input type="hidden" name="workspace_id" value="<%= supervisedItems[i].getID() %>"/>
                    <input class="btn btn-default" type="submit" name="submit_open" value="<fmt:message key="jsp.mydspace.general.open" />"/>
                </form>
            </td>
            <td class="<%= row %>RowEvenCol">
                <a href="mailto:<%= submitter.getEmail() %>"><%= Utils.addEntities(submitter.getFullName()) %></a>
            </td>
            <td class="<%= row %>RowOddCol"><%= Utils.addEntities(title) %></td>
            <td class="<%= row %>RowEvenCol"><%= supervisedItems[i].getCollection().getMetadata("name") %></td>
            <td class="<%= row %>RowOddCol">
                <form action="<%= request.getContextPath() %>/mydspace" method="post">
                    <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>"/>
                    <input type="hidden" name="workspace_id" value="<%= supervisedItems[i].getID() %>"/>
                    <input class="btn btn-default" type="submit" name="submit_delete" value="<fmt:message key="jsp.mydspace.general.remove" />"/>
					
                </form>  
            </td>
        </tr>
<%
            row = (row.equals("even") ? "odd" : "even" );
        }
%>
    </table>
	</div>
	  </div>
	   </div>
	    </div>
<%
    }
%>

<%
    // Display workflow items, if any
    if (workflowItems.length > 0)
    {
        // even or odd row:  Starts even since header row is odd (1)
        String row = "even";
%>

<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        <h3 class="box-title">				
          <fmt:message key="jsp.mydspace.main.heading5"/>
			  </h3>
			<p class="submitFormHelp">
			<%--Below are tasks in the task pool that have been assigned to you. --%>
			<fmt:message key="jsp.mydspace.main.text4" />
         </p>
        </div>       
	<div class="box-body">
	
    <table class="table" align="center" summary="Table listing submissions in workflow process">
        <tr>
            <th id="t14" class="oddRowOddCol"><fmt:message key="jsp.mydspace.main.elem1"/></th>
            <th id="t15" class="oddRowEvenCol"><fmt:message key="jsp.mydspace.main.elem2"/></th>
        </tr>
<%
        for (int i = 0; i < workflowItems.length; i++)
        {
            Metadatum[] titleArray =
                workflowItems[i].getItem().getDC("title", null, Item.ANY);
            String title = (titleArray.length > 0 ? titleArray[0].value
                    : LocaleSupport.getLocalizedMessage(pageContext,"jsp.general.untitled") );
%>
            <tr>
                <td headers="t14" class="<%= row %>RowOddCol"><%= Utils.addEntities(title) %></td>
                <td headers="t15" class="<%= row %>RowEvenCol">
                   <form action="<%= request.getContextPath() %>/mydspace" method="post">
                       <%= workflowItems[i].getCollection().getMetadata("name") %>
                       <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>" />
                       <input type="hidden" name="workflow_id" value="<%= workflowItems[i].getID() %>" />
                   </form>   
                </td>
            </tr>
<%
      row = (row.equals("even") ? "odd" : "even" );
    }
%>
    </table>
	</div>
	  </div>
	   </div>
	    </div>
		
				<%
				  }
				  if(displayGroupMembership && groupMemberships.length>0)
				  {
				%>
				
				<h3><fmt:message key="jsp.mydspace.main.heading6"/></h3>
					<ul>
				<%
					for(int i=0; i<groupMemberships.length; i++)
					{
				%>
					<li><%=groupMemberships[i].getName()%></li> 
				<%    
					}
				%>
					</ul>
				<%
				  }
				%>
	
	<div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<!--<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>-->
					</div>
		        	
             </div>				
			 <div class="box-body">
	<%if(exportsAvailable!=null && exportsAvailable.size()>0){ %>
	<h3><fmt:message key="jsp.mydspace.main.heading7"/></h3>
	
	<ol class="exportArchives">
		<%for(String fileName:exportsAvailable){%>
			<li><a href="<%=request.getContextPath()+"/exportdownload/"+fileName%>" title="<fmt:message key="jsp.mydspace.main.export.archive.title"><fmt:param><%= fileName %></fmt:param></fmt:message>"><%=fileName%></a></li> 
		<% } %>
	</ol>
	<%} %>
	
	<%if(importsAvailable!=null && importsAvailable.size()>0){ %>
	<h3><fmt:message key="jsp.mydspace.main.heading8"/></h3>
	<ul class="exportArchives" style="list-style-type: none;">
		<% int i=0;
			for(BatchUpload batchUpload : importsAvailable){
		%>
			<li style="padding-top:5px; margin-top:10px">
				<div style="float:left"><b><%= batchUpload.getDateFormatted() %></b></div>
				<% if (batchUpload.isSuccessful()){ %>
					<div style= "float:left">&nbsp;&nbsp;--> <span style="color:green"><fmt:message key="jsp.dspace-admin.batchimport.success"/></span></div>
				<% } else { %>
					<div style= "float:left;">&nbsp;&nbsp;--> <span style="color:red"><fmt:message key="jsp.dspace-admin.batchimport.failure"/></span></div>
				<% } %>
				<div style="float:left; padding-left:20px">
					<a id="a2_<%= i%>" style="display:none; font-size:12px" href="javascript:showMoreClicked(<%= i%>);"><i>(<fmt:message key="jsp.dspace-admin.batchimport.hide"/>)</i></a>
					<a id="a1_<%= i%>" style="font-size:12px" href="javascript:showMoreClicked(<%= i%>);"><i>(<fmt:message key="jsp.dspace-admin.batchimport.show"/>)</i></a>
				</div><br/>
				
				<div id="moreinfo_<%= i%>" style="clear:both; display:none; margin-top:15px; padding:10px; border:1px solid; border-radius:4px; border-color:#bbb">
					<div><fmt:message key="jsp.dspace-admin.batchimport.itemstobeimported"/>: <b><%= batchUpload.getTotalItems() %></b></div>
					<div style="float:left"><fmt:message key="jsp.dspace-admin.batchimport.itemsimported"/>: <b><%= batchUpload.getItemsImported() %></b></div>
					<div style="float:left; padding-left:20px">
					<a id="a4_<%= i%>" style="display:none; font-size:12px" href="javascript:showItemsClicked(<%= i%>);"><i>(<fmt:message key="jsp.dspace-admin.batchimport.hideitems"/>)</i></a>
					<a id="a3_<%= i%>" style="font-size:12px" href="javascript:showItemsClicked(<%= i%>);"><i>(<fmt:message key="jsp.dspace-admin.batchimport.showitems"/>)</i></a>
				</div>
				<br/>
					<div id="iteminfo_<%= i%>" style="clear:both; display:none; border:1px solid; background-color:#eeeeee; margin:30px 20px">
						<%
							for(String handle : batchUpload.getHandlesImported()){
						%>
							<div style="padding-left:10px"><a href="<%= request.getContextPath() %>/handle/<%= handle %>"><%= handle %></a></div>
						<%
							}
						%>
					</div>
					<div style="margin-top:10px">
						<form action="<%= request.getContextPath() %>/mydspace" method="post">
							<input type="hidden" name="step" value="7">
							<input type="hidden" name="uploadid" value="<%= batchUpload.getDir().getName() %>">
							<input class="btn btn-info" type="submit" name="submit_mapfile" value="<fmt:message key="jsp.dspace-admin.batchimport.downloadmapfile"/>">
							<% if (!batchUpload.isSuccessful()){ %>
								<input class="btn btn-warning" type="submit" name="submit_resume" value="<fmt:message key="jsp.dspace-admin.batchimport.resume"/>">
							<% } %>
							<input class="btn btn-danger" type="submit" name="submit_delete" value="<fmt:message key="jsp.dspace-admin.batchimport.deleteitems"/>">
						</form>
					<div>
					<% if (!batchUpload.getErrorMsgHTML().equals("")){ %>
						<div style="margin-top:20px; padding-left:20px; background-color:#eee">
							<div style="padding-top:10px; font-weight:bold">
								<fmt:message key="jsp.dspace-admin.batchimport.errormsg"/>
							</div>
							<div style="padding-top:20px">
								<%= batchUpload.getErrorMsgHTML() %>
							</div>
						</div>
					<% } %>
				</div>
				<br/>
			</li> 
		<% i++;
			} 
		%>
	</ul>
	
	<%} %>
	</div>
	</div>
	</div>
	</div>
	<script>
		function showMoreClicked(index){
			$('#moreinfo_'+index).toggle( "slow", function() {
				// Animation complete.
			  });
			$('#a1_'+index).toggle();
			$('#a2_'+index).toggle();
		}
		
		function showItemsClicked(index){
			$('#iteminfo_'+index).toggle( "slow", function() {
				// Animation complete.
			  });
			$('#a3_'+index).toggle();
			$('#a4_'+index).toggle();
		}
	</script>	
	</div>


	
</dspace:layout>
