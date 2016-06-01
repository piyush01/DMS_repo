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
import org.dspace.workflowprocess.MyworkflowtaskManager;
import org.dspace.workflowprocess.WorkflowProcessBean;

public class MyTaskTag extends TagSupport {
	private transient TaskMasterBean taskMasterBean=new TaskMasterBean();
    private WorkflowProcessBean   workflowProcessBean=new WorkflowProcessBean();
	private static Logger log = Logger.getLogger(MyTaskTag.class);
    private MyworkflowtaskManager myworkflowtaskManager=new MyworkflowtaskManager();
    private TaskManager taskManager=new TaskManager();

	private Integer userid;
    private String status;    
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TaskMasterBean getTaskMasterBean() {
		return taskMasterBean;
	}

	public void setTaskMasterBean(TaskMasterBean taskMasterBean) {
		this.taskMasterBean = taskMasterBean;
	}

	public WorkflowProcessBean getWorkflowProcessBean() {
		return workflowProcessBean;
	}

	public void setWorkflowProcessBean(WorkflowProcessBean workflowProcessBean) {
		this.workflowProcessBean = workflowProcessBean;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public MyworkflowtaskManager getMyworkflowtaskManager() {
		return myworkflowtaskManager;
	}

	public void setMyworkflowtaskManager(MyworkflowtaskManager myworkflowtaskManager) {
		this.myworkflowtaskManager = myworkflowtaskManager;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public MyTaskTag() {
		super();
		
	}

public int doStartTag() throws JspException{
	try{
		log.info("doStartTag-----"+status);
		if(status!=null && status.equals("user")){
			getMyTaskDisplay();
			}
		else if(status!=null && status.equals("supervisor")) {
				getSupervisorTaskDisplay();
			}
			else{
				log.info("nothing");
			}
		
	}catch(Exception e){
		log.info("Error in workflow step task::-----"+e);
	}
	 return SKIP_BODY;
}

private void getMyTaskDisplay() throws IOException
{
	String samedoc="";
	int user_id=0,count=0;
	log.info("workflowid:--------"+userid);
	
	JspWriter out = pageContext.getOut();
	HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();	
	try{
	Context context = UIUtil.obtainContext(request);
	List<WorkflowProcessBean> mytaskList=new ArrayList<>();
	List<WorkflowProcessBean> mydocList=new ArrayList<>();
	mytaskList= myworkflowtaskManager.getMyTaskDetais(context, userid);
	
	log.info("mytaskList:--------------"+mytaskList.size());
	
    for(WorkflowProcessBean   workflowProcessBean:mytaskList){
    	log.info("mytaskList:--------------"+mytaskList.size()+workflowProcessBean.getTask_id());
    	log.info("work_id:-----"+workflowProcessBean.getWorkflow_id());
    	mydocList=myworkflowtaskManager.getMyDocumentList(context,workflowProcessBean.getWorkflow_id());
    	out.println("<tr style=\"background-color:\">");	
	   out.println("<td class=\"oddRowEvenCol\">");
	   for(WorkflowProcessBean   workflowProcess:mydocList){
		   if(count==1){
			   break;
		   }
		   log.info("mytaskList:--------------"+mydocList.size()+"--"+workflowProcess.getWork_process_id()+"--"+workflowProcessBean.getTask_start());
		   if(workflowProcessBean.getTask_start()!=null && workflowProcessBean.getTask_start().equals("open")){
			   out.println("<a id=\"mylink\" onclick=\"return read("+workflowProcess.getIs_read()+");\" href=\""+request.getContextPath()+"/mytask?process_task_id="+workflowProcess.getWork_process_id()+"-"+workflowProcessBean.getTask_id()+"\">");
			   out.println("<img height=\"40\" alt=\"Start Task\" src=\""+request.getContextPath()+"/image/start.jpg\"></img></a>");
		   }else
		   {
			   out.println("<a id=\"mylink\" onclick=\"start();\" href=\"#\"> ");
			   out.println("<img  height=\"40\" alt=\"Stop Task\" src=\""+request.getContextPath()+"/image/stop.png\"></img></a>");
			  
		   }
		   count++;
	   }
	   count=0;
		out.println("</td>");
		out.println("<td>"+workflowProcessBean.getTask_no()+"</td>");
		out.println("<td>"+workflowProcessBean.getTask_name()+"</td>");	
		out.println("<td>");
		 for(WorkflowProcessBean   workflowProcess:mydocList){
			 user_id=workflowProcess.getUser_id();
			 log.info("file path:--------------"+workflowProcess.getFile_path()+"user_id-----------"+user_id);
			 if(!samedoc.equals(workflowProcess.getDocument_name()))
			 {
			out.println("<li style=\"list-style-type:none\">");	 
			out.println("<a class=\"checked\" id=\""+workflowProcess.getProcess_id()+"\" href=\""+workflowProcess.getFile_path()+"\">"+workflowProcess.getDocument_name()+"</a>");
			out.println("</li>");	
			 }
			 samedoc=workflowProcess.getDocument_name();
		 }
		 samedoc="";
		 out.println("</td>");
		 out.println("<td>");	 
		 if(workflowProcessBean.getStatus()!=null && workflowProcessBean.getStatus().equals("P"))
		 {
			out.println("<p class=\"text-warning\">Pending</p>");
		 }
		 else if(workflowProcessBean.getStatus()!=null && workflowProcessBean.getStatus().equals("A"))
		 {
			out.println("<p class=\"text-success\">Approved</p>");
		 }
		 else if(workflowProcessBean.getStatus()!=null && workflowProcessBean.getStatus().equals("D"))
		 {
			out.println("<p class=\"text-danger\">Disapproved</p>");
		 }
		 log.info("date-------------"+workflowProcessBean.getAdate());
		 out.println("</td>");
		out.println("<td class=\"oddRowEvenCol\">");
		 out.println(""+workflowProcessBean.getAdate()+"");
		 out.println("</td>");
		 out.println("<td class=\"oddRowEvenCol\">");
		 out.println(""+workflowProcessBean.getDue_Date()+"&nbsp;"+workflowProcessBean.getDeadline_time()+"");
		 out.println("</td>");
		out.println("<td class=\"oddRowEvenCol\">"+taskManager.userName(context,user_id)+"</td>");	
		out.println("</tr>");
		user_id=0;
   }
	}
	catch(Exception e){
		log.info("Error in Task tag class"+e);
	}
   
}
private void getSupervisorTaskDisplay() throws IOException
{
	int user_id=0,count=0;
	log.info("workflowid:--------"+userid);
	String samedoc="";
	JspWriter out = pageContext.getOut();
	HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();	
	try{
	Context context = UIUtil.obtainContext(request);
	List<WorkflowProcessBean> mytaskList=new ArrayList<>();
	List<WorkflowProcessBean> mydocList=new ArrayList<>();
	mytaskList= myworkflowtaskManager.getSupervisorTaskDetais(context, userid);
	log.info("mytaskList:--------------"+mytaskList.size());
	
    for(WorkflowProcessBean   workflowProcessBean:mytaskList)
    {
    	log.info("mytaskList:--------------"+mytaskList.size()+workflowProcessBean.getTask_id());
    	log.info("work_id:-----"+workflowProcessBean.getWorkflow_id());
    	mydocList=myworkflowtaskManager.getMyDocumentList(context,workflowProcessBean.getWorkflow_id());
    	out.println("<tr style=\"background-color:\">");	
	   out.println("<td class=\"oddRowEvenCol\">");
	   for(WorkflowProcessBean   workflowProcess:mydocList){
		  
		   if(count==1){
			   break;
		   }
		   log.info("mytaskList:--------------"+mydocList.size()+"--"+workflowProcess.getWork_process_id()+"--"+workflowProcessBean.getTask_start());
		   if(workflowProcessBean.getTask_start()!=null && workflowProcessBean.getTask_start().equals("open")){
			   out.println("<a id=\"mylink\" onclick=\"return read("+workflowProcess.getIs_read()+");\" href=\""+request.getContextPath()+"/mytask?process_task_id="+workflowProcess.getWork_process_id()+"-"+workflowProcessBean.getTask_id()+"&&role=1\">");
			   out.println("<img height=\"40\" alt=\"Start Task\" src=\""+request.getContextPath()+"/image/start.jpg\"></img></a>");
		   }else
		   {
			   out.println("<a id=\"mylink\" onclick=\"start();\" href=\"#\"> ");
			   out.println("<img  height=\"40\" alt=\"Stop Task\" src=\""+request.getContextPath()+"/image/stop.png\"></img></a>");
		   }
		   count++;
		   
	   }
	   count=0;
		out.println("</td>");
		out.println("<td>"+workflowProcessBean.getTask_no()+"</td>");
		out.println("<td>"+workflowProcessBean.getTask_name()+"</td>");	
		out.println("<td>");
		 for(WorkflowProcessBean   workflowProcess:mydocList){
			 user_id=workflowProcess.getUser_id();
			 if(!samedoc.equals(workflowProcess.getDocument_name()))
			 {
				out.println("<li style=\"list-style-type:none\">");	 
				out.println("<a class=\"checked\" id=\""+workflowProcess.getProcess_id()+"\" href=\""+workflowProcess.getFile_path()+"\">"+workflowProcess.getDocument_name()+"</a>");
				out.println("</li>");	
			 }
			samedoc=workflowProcess.getDocument_name();		
		 }
		 samedoc="";
		 out.println("</td>");
		 out.println("<td>");	 
		 if(workflowProcessBean.getStatus()!=null && workflowProcessBean.getStatus().equals("P"))
		 {
			out.println("<p class=\"text-warning\">Pending</p>");
		 }
		 else if(workflowProcessBean.getStatus()!=null && workflowProcessBean.getStatus().equals("A"))
		 {
			out.println("<p class=\"text-success\">Approved</p>");
		 }
		 else if(workflowProcessBean.getStatus()!=null && workflowProcessBean.getStatus().equals("D"))
		 {
			out.println("<p class=\"text-danger\">Disapproved</p>");
		 }
		 log.info("date-------------"+workflowProcessBean.getAdate());
		 out.println("</td>");
		out.println("<td class=\"oddRowEvenCol\">");
		 out.println(""+workflowProcessBean.getAdate()+"");
		 out.println("</td>");
		 out.println("<td class=\"oddRowEvenCol\">");
		 out.println(""+workflowProcessBean.getDue_Date()+"&nbsp;"+workflowProcessBean.getDeadline_time()+"");
		 out.println("</td>");
		out.println("<td class=\"oddRowEvenCol\">"+taskManager.userName(context,user_id)+"</td>");	
		out.println("</tr>");
		user_id=0;
   }
	}
	catch(Exception e){
		log.info("Error in Task tag class"+e);
	}
   
}
}
