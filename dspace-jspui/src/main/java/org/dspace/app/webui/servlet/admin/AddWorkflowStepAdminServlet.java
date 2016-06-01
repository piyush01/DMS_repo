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
import org.dspace.workflowmanager.WorkflowStepBean;
import org.dspace.workflowmanager.WorkflowStepManager;
/**
 * Servlet for editing and creating add new workflow
 * 
 * @author Mr. Rajesh Kumar
 * @version $Revision$
 */
public class AddWorkflowStepAdminServlet extends DSpaceServlet
{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1546456456L;
	public static Logger log=Logger.getLogger(AddWorkflowStepAdminServlet.class);
	private WorkflowManager workflowManager=new WorkflowManager();
	private WorkflowStepManager workflowStepManager=new WorkflowStepManager();
	private WorkflowStepBean workflowStepBean=new WorkflowStepBean();
	private WorkflowMasterBean workflowMasterBean=new WorkflowMasterBean();
	
	public WorkflowMasterBean getWorkflowMasterBean() {
		return workflowMasterBean;
	}
	public void setWorkflowMasterBean(WorkflowMasterBean workflowMasterBean) {
		this.workflowMasterBean = workflowMasterBean;
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
	public WorkflowManager getWorkflowManager() {
		return workflowManager;
	}
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}
	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
            {
		  String action=request.getParameter("action");	
		   
		  Integer wid=Integer.valueOf(request.getParameter("workflowId"));
		  request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));
		   
		    workflowMasterBean=workflowManager.getWorkflowById(context,request);
			request.setAttribute("workflowMasterBean",workflowMasterBean);
			
		  if(action!=null && action.equals("list")){
			  request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,44));
			  JSPManager.showJSP(request, response, "/dspace-admin/workflow-steplist.jsp");  
		   }
		   else if(action!=null && action.equals("submit_delete"))
			{
				Boolean isUpdate=false;
				isUpdate=workflowStepManager.deleteWorkflowStep(context,request);
				if(isUpdate==true){
					request.setAttribute("message","Step has been successfully deleted.");
				}else{
					request.setAttribute("errorMessage", "Please try again! Step has not been successfully delete.");
				}
				 request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));				  
				 workflowMasterBean=workflowManager.getWorkflowById(context,request);
				request.setAttribute("workflowMasterBean",workflowMasterBean);
				context.complete();
				response.sendRedirect(request.getContextPath()+"/dspace-admin/workflow-step?workflowId="+request.getParameter("workflowId"));	
				//JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp");
				
			}
			else if(action!=null && action.equals("submit_edit")){
				log.info("I m here submit edit");
				Integer workflowId=Integer.valueOf(request.getParameter("workflowId"));
				workflowStepBean=workflowStepManager.getWorkflowstepById(context,request);
				workflowMasterBean=workflowManager.getWorkflowById(context,request);
				request.setAttribute("workflowMasterBean",workflowMasterBean);
				request.setAttribute("action","edit");
				request.setAttribute("workflowStepBean",workflowStepBean);		
				//request.setAttribute("workflows", workflowManager.getNoAllWorkflow(context,request));
				request.setAttribute("workflowId", workflowId);
				JSPManager.showJSP(request, response,"/dspace-admin/add-workflowstep.jsp");				
			}
		     		   
		   else{
			   Integer worfkflow_id=Integer.valueOf(request.getParameter("workflowId"));
			   request.setAttribute("workflowId", worfkflow_id);
			   workflowMasterBean=workflowManager.getWorkflowById(context,request);
			   request.setAttribute("workflowMasterBean",workflowMasterBean);
			  showMain(context,request,response);
		   }
		 	   
            }
	
	private void showMain(Context c, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
		//request.setAttribute("workflows", workflowManager.getAllWorkflow(c));
		
        JSPManager.showJSP(request, response, "/dspace-admin/add-workflowstep.jsp");
    }
	
	 protected void doDSPost(Context context, HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException,
	            SQLException, AuthorizeException
	    {
		 int saveId=0;
		 String button=UIUtil.getSubmitButton(request, "submit");

			Integer wid=Integer.valueOf(request.getParameter("workflowId"));
			request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));		   
			workflowMasterBean=workflowManager.getWorkflowById(context,request);
			request.setAttribute("workflowMasterBean",workflowMasterBean);
			
		 if(button!=null && button.equals("submit_add")){
				try
				{
					saveId=workflowStepManager.addWorkflowStep(context,request);
					if(saveId>0){
						request.setAttribute("message","Step has been successfully saved.");
					}else{
						request.setAttribute("errorMessage", "Please try again! Step has not been successfully saved.");
					}
				}
				catch (Exception e)
				{
					log.info("error in add servlet add workflow step======"+e);
				}	
				 request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));				  
				 workflowMasterBean=workflowManager.getWorkflowById(context,request);
				request.setAttribute("workflowMasterBean",workflowMasterBean);
				context.complete();
				response.sendRedirect(request.getContextPath()+"/dspace-admin/workflow-step?workflowId="+request.getParameter("workflowId"));	
				//request.setAttribute("workflows", workflowManager.getAllWorkflow(context));
				//JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp");				
		 	}
			
			else if(button!=null && button.equals("submit_update")){
				Boolean isUpdate=false;
				isUpdate=workflowStepManager.updateWorkflowStep(context,request);
				if(isUpdate==true){
					request.setAttribute("message","Step has been successfully updated.");
				}else{
					request.setAttribute("errorMessage", "Please try again! Step has not been successfully update.");
				}
				 request.setAttribute("workflowstep", workflowStepManager.getAllWorkflowstep(context,wid));				  
				 workflowMasterBean=workflowManager.getWorkflowById(context,request);
				request.setAttribute("workflowMasterBean",workflowMasterBean);
				context.complete();
				response.sendRedirect(request.getContextPath()+"/dspace-admin/workflow-step?workflowId="+request.getParameter("workflowId"));	
				//JSPManager.showJSP(request, response,"/dspace-admin/workflow-steplist.jsp");				
			}
			else{
			showMain(context,request,response);
			}
	    }
}