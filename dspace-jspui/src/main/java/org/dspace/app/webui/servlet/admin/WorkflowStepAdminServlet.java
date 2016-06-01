package org.dspace.app.webui.servlet.admin;
/**
 * Servlet for add step in new workflow
 * 
 * @author Mr. Rajesh Kumar
 * @version $Revision$
 */
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.WorkflowManager;
import org.dspace.workflowmanager.WorkflowMasterBean;
import org.dspace.workflowmanager.WorkflowStepBean;
import org.dspace.workflowmanager.WorkflowStepManager;
public class WorkflowStepAdminServlet extends DSpaceServlet
{
	/** Logger */
	private TaskManager taskManager=new TaskManager();
	public static Logger log=Logger.getLogger(WorkflowStepAdminServlet.class);
	private WorkflowStepBean workflowStepBean=new WorkflowStepBean();
	private WorkflowStepManager workflowStepManager=new WorkflowStepManager();
	private WorkflowManager workflowManager=new WorkflowManager();
	private WorkflowMasterBean workflowMasterBean=new WorkflowMasterBean();
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
	public TaskManager getTaskManager() {
		return taskManager;
	}
	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}
	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
            {
		   String mode=request.getParameter("action");
		   
		   Integer wid=Integer.valueOf(request.getParameter("workflowId"));
		  request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));		   
		    workflowMasterBean=workflowManager.getWorkflowById(context,request);
			request.setAttribute("workflowMasterBean",workflowMasterBean);
		  //request.setAttribute("taskMasterList", taskManager.getAllTaskList(context));
			 if(mode!=null && !mode.equals("") && mode.equals("status")){
			 JSPManager.showJSP(request, response, "/dspace-admin/workflow-status.jsp");
	          }
			 else{			 
			  showMain(context,request,response);
	          }
            }
         
	private void showMain(Context c, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
		  JSPManager.showJSP(request, response, "/dspace-admin/workflow-steplist.jsp"); 
        //JSPManager.showJSP(request, response, "/dspace-admin/workflow-step.jsp");
    }
	

}