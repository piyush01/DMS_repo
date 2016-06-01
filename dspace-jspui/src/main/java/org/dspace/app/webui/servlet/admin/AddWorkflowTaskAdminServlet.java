package org.dspace.app.webui.servlet.admin;

import java.awt.Desktop.Action;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.TaskMasterBean;
import org.dspace.workflowmanager.WorkflowManager;
import org.dspace.workflowmanager.WorkflowMasterBean;
import org.dspace.workflowmanager.WorkflowStepBean;
import org.dspace.workflowmanager.WorkflowStepManager;

/**
 * Servlet for editing and creating add new workflow
 * 
 * @author Mr. Rajesh Kumar
 * @version $Revision$
 */
public class AddWorkflowTaskAdminServlet extends DSpaceServlet

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 15646549844L;
	public static Logger log = Logger
			.getLogger(AddWorkflowTaskAdminServlet.class);
	private WorkflowManager workflowManager = new WorkflowManager();
	private WorkflowStepManager workflowStepManager = new WorkflowStepManager();
	private WorkflowStepBean workflowStepBean = new WorkflowStepBean();
	private WorkflowMasterBean workflowMasterBean = new WorkflowMasterBean();
	private TaskManager taskManager = new TaskManager();
    private TaskMasterBean taskMasterBean=new TaskMasterBean();
    
	public TaskMasterBean getTaskMasterBean() {
		return taskMasterBean;
	}

	public void setTaskMasterBean(TaskMasterBean taskMasterBean) {
		this.taskMasterBean = taskMasterBean;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public WorkflowManager getWorkflowManager() {
		return workflowManager;
	}

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public WorkflowStepManager getWorkflowStepManager() {
		return workflowStepManager;
	}

	public void setWorkflowStepManager(WorkflowStepManager workflowStepManager) {
		this.workflowStepManager = workflowStepManager;
	}

	public WorkflowStepBean getWorkflowStepBean() {
		return workflowStepBean;
	}

	public void setWorkflowStepBean(WorkflowStepBean workflowStepBean) {
		this.workflowStepBean = workflowStepBean;
	}

	public WorkflowMasterBean getWorkflowMasterBean() {
		return workflowMasterBean;
	}

	public void setWorkflowMasterBean(WorkflowMasterBean workflowMasterBean) {
		this.workflowMasterBean = workflowMasterBean;
	}

	protected void doDSGet(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		
		List<WorkflowStepBean> stepList = new ArrayList<WorkflowStepBean>();
		String action = request.getParameter("action");
		
		TaskMasterBean taskMasterBean=new TaskMasterBean();
		log.info("action---"+action);
		 if(action!=null && action.equals("taskedit"))
		 {
				Integer task_id = Integer.valueOf(request.getParameter("task-id"));
				log.info("servlet task id------------------------------task_id---"+task_id);
				
				taskMasterBean=taskManager.getTaskById(context, task_id);
				request.setAttribute("userlist", taskManager.getUser(context));
				request.setAttribute("supervisorList", taskManager.getSupervisorUser(context,taskMasterBean.getSupervisor_id()));
				request.setAttribute("taskuserlist", taskManager.getTaskUserList(context,task_id));
				request.setAttribute("taskMasterBean",taskMasterBean);
				JSPManager.showJSP(request, response,"/dspace-admin/edit-workflowtask.jsp");
		 }
		 
		 else if(action!=null && action.equals("taskdelete"))
		 {
			 String task = request.getParameter("task-id");
			 String tid[]=task.split("-");
			 Integer task_id=Integer.valueOf(tid[0]);
			 Integer workflowId=Integer.valueOf(tid[1]);
			 log.info("servlet task id------------------------------task_id---"+task_id+"--"+action+"--"+workflowId);
			 try {
				int saveId=taskManager.deleteTask(context, task_id);
				if(saveId>0){
					log.info("task delete servlet===============>>>>" + saveId);
					request.setAttribute("message","Task has been successfully deleted.");
					
				}else{
					
					request.setAttribute("errorMessage", "Please try again! Task do not successfully deleted.");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,workflowId));	
				JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp?workflowId=");
				context.complete();
		 }
		 else
		 {
		Integer workflowId = Integer.valueOf(request.getParameter("workflowId"));
		Integer step_id = Integer.valueOf(request.getParameter("step-id"));
		request.setAttribute("workflowId", workflowId);
		request.setAttribute("step_id", step_id);
		request.setAttribute("stepno", taskManager.getStepNo(context,step_id));
		request.setAttribute("userlist", taskManager.getUser(context));
		showMain(context, request, response);
		context.commit();
		 }
		/*if (action != null && action.equals("ajaxlist")) {
			stepList = workflowStepManager.getAllWorkflowstepById(context,
					workflow_id);
			workflowStepManager.setStepXml(response, stepList);
		} else {
			
		}*/

	}

	protected void doDSPost(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		int saveId=0;
		String button = UIUtil.getSubmitButton(request, "submit");
		log.info("button:-------------------"+button);
		if (button.equals("submit_add")) {
			try {
				saveId=taskManager.saveTask(context,request);
				if(saveId>0){
					log.info("task save servlet===============>>>>" + saveId);
					request.setAttribute("message","Task has been successfully saved.");
					
				}else{
					
					request.setAttribute("errorMessage", "Please try again! Task do not successfully saved.");
				}
			} catch (Exception e) {
				log.info("Error in save add task===============>>>>" + e);
			}
			
			Integer wid=Integer.valueOf(request.getParameter("workflowId"));
			 request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));	
		   workflowMasterBean=workflowManager.getWorkflowById(context,request);
		   request.setAttribute("workflowMasterBean",workflowMasterBean);	
		   
		   Integer workflowId = Integer.valueOf(request.getParameter("workflowId"));
			Integer step_id = Integer.valueOf(request.getParameter("step_id"));
			request.setAttribute("workflowId", workflowId);
			request.setAttribute("step_id", step_id);
			request.setAttribute("stepno", taskManager.getStepNo(context,step_id));
			request.setAttribute("userlist", taskManager.getUser(context));
			showMain(context, request, response);
		  // JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp?workflowId="+request.getParameter("workflowId"));
		   context.complete();
		   
		   /*request.setAttribute("taskMasterList", taskManager.getAllTaskList(context));*/
			//JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp");
			//response.sendRedirect(request.getContextPath()+"/dspace-admin/workflow-step?workflowId="+request.getParameter("workflowId"));	
			//JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp?workflowId="+request.getParameter("workflowId"));	
			//<%=request.getContextPath()%>/dspace-admin/workflow-step?workflowId
			
		}
		else if(button.equals("submit_update")){
			Integer workflowId = Integer.valueOf(request.getParameter("workflowId"));
			Integer step_id = Integer.valueOf(request.getParameter("step_id"));
				
			try {
				saveId=taskManager.updateTask(context,request);
				
				if(saveId>0){
					log.info("task update servlet===============>>>>" + saveId);
					request.setAttribute("message","Task has been successfully update.");
					
				}else{
					
					request.setAttribute("errorMessage", "Please try again! Task do not successfully update.");
				}
			} catch (Exception e) {
				log.info("Error in update  task===============>>>>" + e);
			}
			
			log.info("step_id:-----------"+workflowId+"---"+step_id);
			request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,workflowId));	
			 workflowMasterBean=workflowManager.getWorkflowById(context,request);
			  request.setAttribute("workflowMasterBean",workflowMasterBean);
			JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp?workflowId="+workflowId);
			//JSPManager.showJSP(request, response,"/dspace-admin/edit-workflowtask.jsp");
			  context.complete();
		}
		
	}

	private void showMain(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		
		JSPManager.showJSP(request, response,"/dspace-admin/add-workflowtask.jsp");
	}
}