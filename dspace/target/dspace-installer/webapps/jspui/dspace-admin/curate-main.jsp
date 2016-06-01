
<%--
  - Form requesting a Handle or internal item ID for item editing
  -
  - Attributes:
  -     curate_group_options - options string of gropu selection. 
  -         "" unless ui.taskgroups is set
  -     curate_task_options - options string of task selection.
  -     handle - handle of the DSpaceObject
  -     task_result - result of the curation task
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.app.webui.util.CurateTaskResult" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%!
    private static final String TASK_QUEUE_NAME = ConfigurationManager.getProperty("curate", "ui.queuename");
%>
<%
    String handle  = (String) request.getAttribute("handle");
    if (handle == null)
    {
        handle = "";
    }
    String groupOptions = (String)request.getAttribute("curate_group_options");
    String taskOptions = (String)request.getAttribute("curate_task_options");
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.curate.main.title" navbar="admin" locbar="link"   parenttitlekey="jsp.administer" parentlink="/dspace-admin">


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
					<h3><fmt:message key="jsp.dspace-admin.curate.main.heading"/></h3>
					<%@ include file="/tools/curate-message.jsp" %>
					</h4>
					 
					</div>

         <form  class="form-horizontal" action="<%=request.getContextPath()%>/dspace-admin/curate" method="post">
               <div class="box-body">
			    <div class="form-group">
				<div class="col-sm-3">            
	              	<label><fmt:message key="jsp.dspace-admin.curate.main.info1"/>:</label>
				</div>	
				<div class="col-sm-5">     
       	         <input class="form-control" type="text" name="handle" value="<%= handle %>" size="20"/>
         	     
	          </div>
			  	<div class="col-sm-4">
				<span class="col-md-10"><fmt:message key="jsp.dspace-admin.curate.main.info2"/></span>
				</div>
				</div>				
	
	
    
<%
    if (groupOptions != null && !"".equals(groupOptions))
    {
%>
 	 <div class="form-group">
			<div class="col-sm-3">  
            <label><fmt:message key="jsp.tools.curate.select-group.tag"/>:</label>
  	     </div>	
		 <div class="col-sm-5">
        <select class="form-control" name="select_curate_group" id="select_curate_group" onchange="this.form.submit();">
          <%= groupOptions %>
        </select>
    </div>
	<div class="col-sm-4"></div>
	</div>
<%
    }
%>
   <div class="form-group">
			<div class="col-sm-3"> 
             <label><fmt:message key="jsp.tools.curate.select-task.tag"/>:</label>
           </div>
		    <div class="col-sm-5">
        <select class="form-control" name="curate_task" id="curate_task">
          <%= taskOptions %>
        </select>
   </div>
	<div class="col-sm-4"></div>
	</div>
     <div class="form-group">
	<div class="col-sm-3"> </div>
	   <div class="col-sm-5">
	<input type="hidden" name="handle" value="<%= handle %>"/>
    <input class="btn btn-info" type="submit" name="submit_main_curate" value="<fmt:message key="jsp.tools.curate.perform.button"/>" />
    <input class="btn btn-info" type="submit" name="submit_main_queue" value="<fmt:message key="jsp.tools.curate.queue.button"/>" />
    <input class="btn btn-info" type="submit" name="submit_main_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
    </div>
	<div class="col-sm-4"> </div>
   </div>
</div>
</form>
 </div>
  </div> 
  </div>
  </section>
</dspace:layout>
