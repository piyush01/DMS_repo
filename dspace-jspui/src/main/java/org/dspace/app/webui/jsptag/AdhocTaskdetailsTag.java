package org.dspace.app.webui.jsptag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.dspace.core.Utils;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.TaskMasterBean;
import org.dspace.workflowprocess.AdhocWorkflowManager;
import org.dspace.workflowprocess.WorkflowProcessBean;
import org.dspace.app.webui.util.UIUtil;

import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.JspWriter;

public class AdhocTaskdetailsTag extends TagSupport{
private static Logger log = Logger.getLogger(AdhocTaskdetailsTag.class);
private AdhocWorkflowManager adhocWorkflowManager=new AdhocWorkflowManager();
private TaskManager taskManager=new TaskManager();


public TaskManager getTaskManager() {
	return taskManager;
}

public void setTaskManager(TaskManager taskManager) {
	this.taskManager = taskManager;
}

public AdhocWorkflowManager getAdhocWorkflowManager() {
	return adhocWorkflowManager;
}

public void setAdhocWorkflowManager(AdhocWorkflowManager adhocWorkflowManager) {
	this.adhocWorkflowManager = adhocWorkflowManager;
}

public int doStartTag() throws JspException
{
    try
    {
    	showAdhocTaskdetails();
    }
    catch (Exception e)
    {
        throw new JspException(e);
    }
   
    return SKIP_BODY;
}

public void showAdhocTaskdetails()
	{
	  List<TaskMasterBean> taskList=new ArrayList<>();
	  List<TaskMasterBean> userList=new ArrayList<>();
	  List<WorkflowProcessBean> docList=new ArrayList<>();
	  
		  JspWriter out = pageContext.getOut();
	      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	      try
	      {
	         Context context = UIUtil.obtainContext(request);
	         EPerson e=context.getCurrentUser();
	         Integer eid=e.getID();
	         taskList=adhocWorkflowManager.getAdhocTaskList(context,eid);
	         String path=request.getContextPath();
	         log.info("i here do eid:----------------"+eid);
	          if(e!=null && eid>0)
	          {
	        	  for(TaskMasterBean taskMasterBean:taskList)
     			 {
     				 log.info("task name:--------------"+taskMasterBean.getTask_name());
     				 out.print("<tr><td>"); 
                     out.print("<a  id=\"mylink\" onclick=\"read();\" href=\"#\"><img  height=\"40\" alt=\"Start Task\" src=\""+path+"/image/start.jpg\"></img></a>");
     				 out.print("</td><td>");
     				 out.print(taskMasterBean.getTask_name());
     				 out.print("</td><td><ul>"); 
     				 docList=adhocWorkflowManager.getAdhocDocumentList(context,taskMasterBean.getWorkflow_id(),taskMasterBean.getTask_id());
     				 log.info("docList:----------------------"+docList.size());
     				 for(WorkflowProcessBean workflowProcessBean: docList)
     				 {
     				 out.print("<li><a href=\""+workflowProcessBean.getFile_path()+"\">"+workflowProcessBean.getDocument_name()+"</a></li>");
     				 }
     				 out.print("</ul></td><td>");
     				 out.print(taskMasterBean.getDeadline_day()+"&nbsp;"+taskMasterBean.getDeadline_time());
     				 out.print("</td><td>");
     				 if(taskMasterBean.getStatus().equals("A"))
     				 {
     				out.print("<p class=\"text-success\" >Completed</p>");
     				 }
     				 else if(taskMasterBean.getStatus().equals("A"))
     				 {
     				out.print("<p class=\"text-warning\" >Pending</p>");
     				 }
     				 else if(taskMasterBean.getStatus().equals("A"))
     				 {
     				out.print("<p class=\"text-danger\" >Disapproved</p>");
     				 }
     				 
     				 out.print("</td><td colspan=\"2\">");
     				 userList=taskManager.getUserRoleList(context,taskMasterBean.getTask_id());
     				 log.info("nameList:----------------------"+userList.size());
     				 for(TaskMasterBean taskMaster:userList){
     					 if(taskMaster.getStatus().equals("A")){
     						 out.print(taskManager.userName(context, Integer.parseInt(taskMaster.getTask_owner_id()))+"/&nbsp;"+"<p class=\"text-success\" >Completed</p>");
     					 }
     					 else if(taskMaster.getStatus().equals("P")){
     						 out.print(taskManager.userName(context, Integer.parseInt(taskMaster.getTask_owner_id()))+"/&nbsp;"+"<p class=\"text-warning\" >Pending</p>");
     					 }
     					 else if(taskMaster.getStatus().equals("D")){
     						 out.print(taskManager.userName(context, Integer.parseInt(taskMaster.getTask_owner_id()))+"/&nbsp;"+"<p class=\"text-danger\" >Disapproved</p>");
     					 }else{
     					 out.print(taskManager.userName(context, Integer.parseInt(taskMaster.getTask_owner_id()))+"&nbsp;");
     					 }
     				 }
     				out.print("</td><td>ff");
     				out.print(taskManager.userName(context,taskMasterBean.getSupervisor_id()));
     				out.print("</td></tr>"); 
     				 
     			 }
	          }
	}
	catch(Exception e){
		log.error("Error in adhoc task tag:-------------->"+e);
	}
}

}
