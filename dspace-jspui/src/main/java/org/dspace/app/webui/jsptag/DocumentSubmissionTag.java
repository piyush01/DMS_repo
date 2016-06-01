package org.dspace.app.webui.jsptag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.dspace.core.Utils;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.TaskMasterBean;
import org.dspace.workflowmanager.WorkflowMasterBean;
import org.dspace.workflowmanager.WorkflowStepBean;
import org.dspace.workflowmanager.WorkflowStepManager;
import org.dspace.workflowprocess.WorkflowProcessBean;
import org.dspace.workflowprocess.WorkflowProcessManager;
import org.dspace.app.webui.util.UIUtil;

import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.JspWriter;

import org.dspace.app.util.DCInputsReaderException;
public class DocumentSubmissionTag extends TagSupport{
	 /** log4j logger */
    private static Logger log = Logger.getLogger(DocumentSubmissionTag.class);
	private WorkflowProcessManager workflowProcessManager=new WorkflowProcessManager();
	private WorkflowMasterBean workflowMasterBean=new WorkflowMasterBean();
	private WorkflowStepManager workflowStepManager=new WorkflowStepManager();
	private TaskManager taskManager=new TaskManager();
	private Integer stepid;
	private String status;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public WorkflowStepManager getWorkflowStepManager() {
		return workflowStepManager;
	}
	public void setWorkflowStepManager(WorkflowStepManager workflowStepManager) {
		this.workflowStepManager = workflowStepManager;
	}
	public TaskManager getTaskManager() {
		return taskManager;
	}
	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}
	public Integer getStepid() {
		return stepid;
	}
	public void setStepid(Integer stepid) {
		this.stepid = stepid;
	}
	public WorkflowMasterBean getWorkflowMasterBean() {
		return workflowMasterBean;
	}
	public void setWorkflowMasterBean(WorkflowMasterBean workflowMasterBean) {
		this.workflowMasterBean = workflowMasterBean;
	}
	public WorkflowProcessManager getWorkflowProcessManager() {
		return workflowProcessManager;
	}
	public void setWorkflowProcessManager(
			WorkflowProcessManager workflowProcessManager) {
		this.workflowProcessManager = workflowProcessManager;
	}
	
	public DocumentSubmissionTag(){
		
	}
	 public int doStartTag() throws JspException
	    {
	        try
	        {
	        	log.info("i here do start tag:----------------"+status);
	        	if(status.equals("document")){
	        		showWorkflowDocument();
	        	}
	        	else
	        	{
	             showSubmissonDocument();
	           }
	        }
	        catch (Exception e)
	        {
	            throw new JspException(e);
	        }
	       
	        return SKIP_BODY;
	    }
	 
	 public void showSubmissonDocument()
	 {
		 log.info("i here do showSubmissonDocument:----------------");
		  List<WorkflowMasterBean> workflowlist=new ArrayList<>();
		  List<WorkflowStepBean> stepList=new ArrayList<>();
		  Set<TaskMasterBean> taskList=new TreeSet<>();
		  List<TaskMasterBean> userList=new ArrayList<>();
		  List<WorkflowProcessBean> docList=new ArrayList<>();
		  JspWriter out = pageContext.getOut();
	        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	        try
	        {
	           Context context = UIUtil.obtainContext(request);
	           EPerson e=context.getCurrentUser();
	           Integer eid=e.getID();
	           String samedoc="";
	           log.info("i here do eid:----------------"+eid);
	            if(e!=null && eid>0){
	            	log.info("i here do eid:----------------"+eid);
	            	 workflowlist=workflowProcessManager.getSubmissionDocument(context);
	            	 log.info("workflowlist:--size tag handler classs"+workflowlist.size());
	            	  	 for(WorkflowMasterBean workflowMasterBean:workflowlist){
	            		 out.print("<tr><td colspan=\"8\"> <p class=\"text-danger\" ><b>Workflow Name:-");
	            		 out.print(workflowMasterBean.getWorkflow_name());
	            		 out.print("</b></p></td></tr>");
	            		 stepList=workflowStepManager.getAllWorkflowstep(context, workflowMasterBean.getWorkflow_id());	            		 
	            		 for(WorkflowStepBean workflowStepBean:stepList){
	            		
	            			if(workflowStepBean.getWorkflow_step_name()!=null){
	            			 out.print("<tr><td colspan=\"7\"> <p class=\"text-warning\" >Step:-"); 
	            			 out.print(workflowStepBean.getStep_no()+" &nbsp;"+workflowStepBean.getWorkflow_step_name());
	            			 out.print("</p></td></tr>");
	            			 }
	            			 
	            			 taskList=taskManager.getAllTaskList(context, workflowMasterBean.getWorkflow_id(), workflowStepBean.getWorkflow_step_id());
	            			
	            			 for(TaskMasterBean taskMasterBean:taskList)
	            			 {
	            				 log.info("task name:--------------"+taskMasterBean.getTask_name());
	            				 out.print("<tr><td>"); 
	            				 out.print(taskMasterBean.getTask_name());
	            				 out.print("</td>"); 
	            				/* docList=workflowProcessManager.getDocumentList(context,taskMasterBean.getWorkflow_id());
	            				 log.info("docList:----------------------"+docList.size());
	            				
	            				 for(WorkflowProcessBean workflowProcessBean: docList)
	            				 {
	            					 
	            				if(!samedoc.equals(workflowProcessBean.getDocument_name())){
	            					out.print("<li><a href=\""+workflowProcessBean.getFile_path()+"\">"+workflowProcessBean.getDocument_name()+"</a></li>");
	            				}
	            					samedoc=workflowProcessBean.getDocument_name();
	            				 }
	            				 samedoc="";*/
	            				 
	            				 
	            				 userList=taskManager.getUserRoleList(context,taskMasterBean.getTask_id());
	            				 
	            				 log.info("nameList:----------------------"+userList.size());
	            				 out.print("<td>");
	            				 for(TaskMasterBean taskMaster:userList){
	            					 out.print(taskManager.userName(context, Integer.parseInt(taskMaster.getTask_owner_id())));
	            				 }
	            				out.print("</td><td>");
	            				
	            				for(TaskMasterBean taskMaster:userList){
	            					 if(taskMaster.getStatus().equals("A")){
	            						 out.print("<p class=\"text-success\" >Completed</p>");
	            					 }
	            					 else if(taskMaster.getStatus().equals("P")){
	            						 out.print("<p class=\"text-warning\" >Pending</p>");
	            					 }
	            					 else if(taskMaster.getStatus().equals("D")){
	            						 out.print("<p class=\"text-danger\" >Disapproved</p>");
	            					 }	            					
	               				 }	            				 
	            				out.print("</td><td>");
	            				 for(TaskMasterBean taskMaster:userList){
	            					 if(taskMaster.getTask_comment()!=null){
	            					 out.print("/Comments:-"+taskMaster.getTask_comment());}
	            				 }
	            				out.print("</td><td>");
	            				out.print(taskManager.userName(context,taskMasterBean.getSupervisor_id()));
	            				out.print("</td>"); 
	            				out.print("<td>");
	            				out.print(taskMasterBean.getDeadline_day()+"&nbsp;"+taskMasterBean.getDeadline_time());
	            			    out.print("</td></tr>");
	            			 }
	            		 }
	            	 }
	            }
	            	 
	        }
	        catch(Exception e)
	        {
	        	log.error(e);
	        }
		 
	 }
	 
	 public void showWorkflowDocument()
	 {
		 log.info("i here do showWorkflowDocument:----------------");
		  List<WorkflowMasterBean> workflowlist=new ArrayList<>();
		  List<WorkflowStepBean> stepList=new ArrayList<>();
		  Set<TaskMasterBean> taskList=new TreeSet<>();
		  List<TaskMasterBean> userList=new ArrayList<>();
		  List<WorkflowProcessBean> docList=new ArrayList<>();
		  
		  JspWriter out = pageContext.getOut();
	        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	        try
	        {
	           Context context = UIUtil.obtainContext(request);
	           EPerson e=context.getCurrentUser();
	           Integer eid=e.getID();
	           String samedoc="";
	            if(e!=null && eid>0){
	            	 workflowlist=workflowProcessManager.getSubmissionDocument(context);
	            	 for(WorkflowMasterBean workflowMasterBean:workflowlist){
	            		 out.print("<tr><td colspan=\"4\"> <p class=\"text-danger\" ><b>Workflow Name:-");
	            		 out.print(workflowMasterBean.getWorkflow_name());
	            		 out.print("</b></p></td></tr>");
	            		 out.print("<tr><td>"); 
        				 out.print("Document:-");
        				 out.print("</td><td><ol>"); 
        				 docList=workflowProcessManager.getDocumentList(context,workflowMasterBean.getWorkflow_id());
        				 for(WorkflowProcessBean workflowProcessBean: docList)
        				 {	
	        				 if(!samedoc.equals(workflowProcessBean.getDocument_name())){
	        					 out.print("<li><a href=\""+workflowProcessBean.getFile_path()+"\">"+workflowProcessBean.getDocument_name()+"</a></li>");
	        				 }
	        				 samedoc=workflowProcessBean.getDocument_name();
        				 }
        				 samedoc="";
        				 out.print("</ol></td></tr>");
	            	 }
	            }
	            	 
	        }
	        catch(Exception e)
	        {
	        	log.error(e);
	        }
		 
	 }
	 
	 public List getTaskList(){
		 List taskList=new ArrayList();
		 
		return taskList; 
	 }
}
