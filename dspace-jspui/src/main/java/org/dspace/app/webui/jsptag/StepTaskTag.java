package org.dspace.app.webui.jsptag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.core.Context;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.TaskMasterBean;

public class StepTaskTag extends TagSupport {
	private transient TaskMasterBean taskMasterBean=new TaskMasterBean();

	private static Logger log = Logger.getLogger(StepTaskTag.class);
	  
private TaskManager taskManager=new TaskManager();
	private Integer workflowid;
	private Integer stepid;
	
	
	  public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public Integer getWorkflowid() {
		return workflowid;
	}

	public void setWorkflowid(Integer workflowid) {
		this.workflowid = workflowid;
	}

	public Integer getStepid() {
		return stepid;
	}

	public void setStepid(Integer stepid) {
		this.stepid = stepid;
	}

	public TaskMasterBean getTaskMasterBean() {
		return taskMasterBean;
	}

	public void setTaskMasterBean(TaskMasterBean taskMasterBean) {
		this.taskMasterBean = taskMasterBean;
	}

	public StepTaskTag() {
		super();
		
	}

public int doStartTag() throws JspException{
	try{
		getTaskDisplay();
	}catch(Exception e){
		log.info("Error in workflow step task::-----"+e);
	}
	 return SKIP_BODY;
}

private void getTaskDisplay() throws IOException
{
	log.info("workflowid:-----"+workflowid+"--:stepid:---"+stepid);
	
	JspWriter out = pageContext.getOut();
	HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();	
	try{
	Context context = UIUtil.obtainContext(request);
	Set<TaskMasterBean> tasklist=new TreeSet<TaskMasterBean>();;
	tasklist=taskManager.getAllTaskList(context, workflowid, stepid);
    for(TaskMasterBean   taskMasterBean:tasklist){
	   out.println("<tr>");	
		out.println("<td>");
	    out.println("<input type=\"checkbox\" class=\"chk\" id=\"gdRows_ct"+taskMasterBean.getStep_id()+"_chkDelete\" name=\"tid\" value=\""+taskMasterBean.getTask_id()+"\" </td> ");
		out.println("<td>"+taskMasterBean.getTask_name()+"</td>");
		out.println("<td>"+taskMasterBean.getPriorty()+"</td><td></td>");
		out.println("<td>"+taskMasterBean.getDeadline_day()+"Time:-"+taskMasterBean.getDeadline_time()+"</td>");		
		out.println("<td>"+taskManager.getUSerName(context,taskMasterBean.getTask_owner_id())+"</td>");		
		if(taskMasterBean.getTask_rule_id()==1){
		out.println("<td>One user is enough to complete the task</td>");
		}
		else if(taskMasterBean.getTask_rule_id()==1){
			out.println("<td>More than one user is to complete the task</td>");
		}
		else{
			out.println("<td>More than one user is to complete the task</td>");
		}
		out.println("<td>"+taskManager.getUSerName(context,String.valueOf(taskMasterBean.getSupervisor_id()))+"</td>");
		out.println("</tr>");		
		out.println("<tr>");	
		out.println("<td colspan=\"8\"><b>Task Instruction:--</b><a style=\"text-decoration:none\">"+taskMasterBean.getTask_instructions()+"</a></td>");
		out.println("</tr>");
   }
	}
	catch(Exception e){
		log.info("Error in Task tag class"+e);
	}
   
}

}
