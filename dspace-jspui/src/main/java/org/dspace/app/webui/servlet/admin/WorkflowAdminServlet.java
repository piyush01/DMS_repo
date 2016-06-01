package org.dspace.app.webui.servlet.admin;

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
import org.dspace.workflowmanager.WorkflowManager;
/**
 * Servlet for editing and creating e-people
 * 
 * @author Mr. Rajesh Kumar
 * @version $Revision$
 */
public class WorkflowAdminServlet extends DSpaceServlet
{
	/** Logger */
    private static Logger log = Logger.getLogger(EPersonAdminServlet.class);
    WorkflowManager workflowManager=new WorkflowManager();
	
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
	        showMain(context, request, response);
	    }
	  
	private void showMain(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
		request.setAttribute("workflows", workflowManager.getAllWorkflow(context));
        JSPManager.showJSP(request, response, "/dspace-admin/workflow-main.jsp");
    }
	
	 protected void doDSPost(Context context, HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException,
	            SQLException, AuthorizeException
	    {
		 
	    }
}
