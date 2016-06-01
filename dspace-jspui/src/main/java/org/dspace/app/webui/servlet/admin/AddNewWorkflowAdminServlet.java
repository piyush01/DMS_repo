package org.dspace.app.webui.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.workflowmanager.WorkflowManager;
import org.dspace.workflowmanager.WorkflowMasterBean;
/**
 * Servlet for editing and creating add new workflow
 * 
 * @author Mr. Rajesh Kumar
 * @version $Revision$
 */
public class AddNewWorkflowAdminServlet extends DSpaceServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 14545465455L;
	public static Logger log=Logger.getLogger(AddNewWorkflowAdminServlet.class);
	WorkflowManager weorkflowManager=new WorkflowManager();
	WorkflowMasterBean workflowMasterBean=new WorkflowMasterBean();
	
	public WorkflowMasterBean getWorkflowMasterBean() {
		return workflowMasterBean;
	}
	public void setWorkflowMasterBean(WorkflowMasterBean workflowMasterBean) {
		this.workflowMasterBean = workflowMasterBean;
	}
	public WorkflowManager getWeorkflowManager() {
		return weorkflowManager;
	}
	public void setWeorkflowManager(WorkflowManager weorkflowManager) {
		this.weorkflowManager = weorkflowManager;
	}
	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
            {
				showMain(context,request,response);
            }
	private void showMain(Context c, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
			  JSPManager.showJSP(request, response, "/dspace-admin/addnew-workflow.jsp");
    }

	 protected void doDSPost(Context context, HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException,
	            SQLException, AuthorizeException
	    {
		 int saveId=0;
		 String button=UIUtil.getSubmitButton(request, "submit");
		 	if(button.equals("submit_add")){
				try {
					saveId=weorkflowManager.addWorkflow(context,request);
					if(saveId>0){
						request.setAttribute("message","Workflow has been successfully saved.");
					}else{
						request.setAttribute("errorMessage", "Please try again! Workflow has not been successfully saved.");
					}
				} catch (Exception e) {
					log.info("error in add servlet======"+e);
				}
				request.setAttribute("workflows", weorkflowManager.getAllWorkflow(context));
				 JSPManager.showJSP(request, response,"/dspace-admin/workflow-main.jsp");
				 context.complete();
			}
			else if(button.equals("submit_delete"))
			{
				Boolean isUpdate=false;
				isUpdate=weorkflowManager.deleteWorkflow(context,request);
				if(isUpdate==true)
				{
					request.setAttribute("message", "Workflow has been successfully delete.");
				}else{
					request.setAttribute("errorMessage", "Workflow has not been successfully delete.");
				}
				request.setAttribute("workflows", weorkflowManager.getAllWorkflow(context));
				JSPManager.showJSP(request, response,"/dspace-admin/workflow-main.jsp");
				 context.complete();
			}
			else if(button.equals("submit_edit")){
				workflowMasterBean=weorkflowManager.getWorkflowById(context,request);
				request.setAttribute("workflowMasterBean",workflowMasterBean);
				request.setAttribute("action","edit");
				JSPManager.showJSP(request, response,"/dspace-admin/addnew-workflow.jsp");
				
			}
			else if(button.equals("submit_update")){
				Boolean isUpdate=false;
				isUpdate=weorkflowManager.updateWorkflow(context,request);
				if(isUpdate==true)
				{
					request.setAttribute("message", "Workflow has been successfully update.");
				}else{
					request.setAttribute("errorMessage", "Workflow has not been successfully update.");
				}
				request.setAttribute("workflows", weorkflowManager.getAllWorkflow(context));
				JSPManager.showJSP(request, response,"/dspace-admin/workflow-main.jsp");
				 context.complete();
			}
			else{
			showMain(context,request,response);
			}
	    }
	
}