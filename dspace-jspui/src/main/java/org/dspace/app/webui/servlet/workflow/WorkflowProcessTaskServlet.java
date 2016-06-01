package org.dspace.app.webui.servlet.workflow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.util.AuthorizeUtil;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dspace.app.webui.util.FileUploadRequest;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.FormatIdentifier;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataSchema;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.handle.HandleManager;
import org.dspace.license.CreativeCommons;
import org.dspace.util.CommonDateFormat;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.TaskMasterBean;
import org.dspace.workflowmanager.WorkflowStepBean;
import org.dspace.workflowmanager.WorkflowStepManager;
import org.dspace.workflowprocess.AdhocWorkflowManager;
import org.dspace.workflowprocess.WorkflowProcessManager;

public class WorkflowProcessTaskServlet extends DSpaceServlet{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4627741331258705624L;
	private static Logger log = Logger.getLogger(WorkflowProcessTaskServlet.class);
    private WorkflowProcessManager workflowProcessManager=new WorkflowProcessManager();
	private TaskManager taskManager=new TaskManager();
    private AdhocWorkflowManager adhocWorkflowManager=new AdhocWorkflowManager();
    private WorkflowStepBean workflowStepBean=new WorkflowStepBean();
    private WorkflowStepManager workflowStepManager=new WorkflowStepManager();
    
    
	public WorkflowStepBean getWorkflowStepBean() {
		return workflowStepBean;
	}

	public void setWorkflowStepBean(WorkflowStepBean workflowStepBean) {
		this.workflowStepBean = workflowStepBean;
	}

	public WorkflowStepManager getWorkflowStepManager() {
		return workflowStepManager;
	}

	public void setWorkflowStepManager(WorkflowStepManager workflowStepManager) {
		this.workflowStepManager = workflowStepManager;
	}

	public AdhocWorkflowManager getAdhocWorkflowManager() {
		return adhocWorkflowManager;
	}

	public void setAdhocWorkflowManager(AdhocWorkflowManager adhocWorkflowManager) {
		this.adhocWorkflowManager = adhocWorkflowManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public WorkflowProcessManager getWorkflowProcessManager() {
		return workflowProcessManager;
	}
	
	public void setWorkflowProcessManager(
			WorkflowProcessManager workflowProcessManager) {
		this.workflowProcessManager = workflowProcessManager;
	}
	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
            {	
		
		String action="";
		 if(request.getParameter("action")!=null){
		   action=request.getParameter("action");
		 }
		 
		 log.info("action:--------------------------"+action+"----"+request.getParameter("process_id"));
		 
		  if(action.equals("ajaxread")){
			  int rowid=0;
			  rowid= workflowProcessManager.readDocument(context,request);
			  context.complete();
			  log.info("document read:---"+rowid);
			return; 
		  }
		        request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		        
		        if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
		        {
		           request.setAttribute("role", "1");
		      	}
				showMain(context,request,response);
            }
	
	
	private void showMain(Context c, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
		request.setAttribute("pageMode", "doc_properties");
		JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
    }
	
	protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
            {	
		      Integer rowId=0;
		      String button=UIUtil.getSubmitButton(request, "submit");
		      EPerson eperson=context.getCurrentUser();
		      log.info("button:------------------"+button);
		      if(eperson!=null && eperson.getID()>0){
		    	  
		    	  if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
			        {
			           request.setAttribute("role", "1");
			      	}
		       if(request.getParameter("process_task_id")!=null && !request.getParameter("process_task_id").equals("")){
		    	   request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		      }
		     
		      request.setAttribute("process_id",request.getParameter("process_id"));
		      request.setAttribute("status",request.getParameter("status"));
		      request.setAttribute("task_id", request.getParameter("task_id"));
		      request.setAttribute("role", request.getParameter("role"));
		      if(button!=null && button.equals("submit_complete_task"))
		     {
		      request.setAttribute("pageMode", "complete_task");	  
		      request.setAttribute("process_id",request.getParameter("process_id"));
		      JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		     }
		      
		      else if(button!=null && button.equals("submit_task")){
		    	  String comments=request.getParameter("task_comment");
		    	  
		    	  if(comments!=null && !comments.equals("")){
		    		  String mode=request.getParameter("mode");
		    		if(mode!=null && mode.equals("a") || mode.equals("p")) { 
		    	     workflowProcessManager.approveTaskByUser(context, request);	
			         workflowProcessManager.approveDocument(context, request);
		    		}
		    	  rowId=workflowProcessManager.saveProcessTask(context,request,"task_comment",comments);
		    	  }
		    	  
		    	  if(rowId>0){
		    		  request.setAttribute("successMessage","Successfully complete your task.");
		    	  }
		    	  else{
		    		  request.setAttribute("errorMessage","Not  complete your task! Plz try agin.");
		    	  }
		    	  request.setAttribute("pageMode", "doc_properties");
		    	  request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		    	  context.complete();
		      }
		      
		      else if(button!=null && button.equals("submit_edit_comment")){
		    	  request.setAttribute("pageMode", "edit_comment");
		    	   request.setAttribute("commentdetails",workflowProcessManager.getTaskCommentDetails(context, request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_update_comment")){
		    	  int row=0;
		    	  row=workflowProcessManager.updateTaskComments(context,request);
		    	   if(row>0){
			    		  request.setAttribute("successMessage","Successfully complete your task.");
			    	  }
			    	  else{
			    		  request.setAttribute("errorMessage","Not  complete your task! Plz try agin.");
			    	  }
		    	  request.setAttribute("pageMode", "doc_properties");
		    	  request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		    	  context.complete();
		      }
		         
		      else if(button!=null && button.equals("submit_attachment_file"))
		      {
		    	  request.setAttribute("pageMode", "attachment_file");
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_file")){
		    	  String contentType = request.getContentType();

		    	  if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1))
		          {
		        	  // This is a multipart request, so it's a file upload
		              processUploadBitstream(context, request, response);
		          }
		    	  request.setAttribute("successMessage","Successfully upload your file.");
		    	  request.setAttribute("pageMode", "doc_properties");
		    	  request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_reassign")){		    	  
		    	  request.setAttribute("pageMode", "assign");
		    	  request.setAttribute("userlist", taskManager.getUser(context));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process.jsp");
		      }
		      else if(button!=null && button.equals("submit_comment_task")){		    	  
		    	  request.setAttribute("pageMode", "comment_task");
		    	  request.setAttribute("commentdetails",workflowProcessManager.getTaskCommentDetails(context, request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_postpone_task")){		    	  
		    	  request.setAttribute("pageMode", "postpone_task");
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_changetask_finished")){		    	  
		    	  request.setAttribute("pageMode", "changetask_finished");
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_show_workflow"))
		      {		
		    	  List<WorkflowStepBean> stepList=new ArrayList<>();
		    	  int workflow_id=Integer.parseInt(request.getParameter("workflow_id"));
		    	  stepList=workflowStepManager.getAllstepById(context, workflow_id);
		    	  request.setAttribute("stepList", stepList);
		    	  request.setAttribute("pageMode", "show_workflow");
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		    	  
		      }
		      else if(button!=null && button.equals("submit_doc_properties"))
		      {		    	  
		    	  request.setAttribute("pageMode", "doc_properties");
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      else if(button!=null && button.equals("submit_postpone")){	
		    	  String comments=request.getParameter("postpone_task_comment");
		    	  if(comments!=null && !comments.equals("")){
		    	  //rowId=workflowProcessManager.saveProcessTask(context,request,"postpone_task_reason_cmts",comments);
		    		  rowId=workflowProcessManager.postponeTask(context,request);
		    	  }
		    	  if(rowId>0){
		    		  request.setAttribute("successMessage","Successfully complete your task.");
		    	  }
		    	  else{
		    		  request.setAttribute("errorMessage","Not  complete your task! Plz try agin.");
		    	  }
		    	  request.setAttribute("pageMode", "doc_properties");
		    	  request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		    	  context.complete();
		      }
		      else if(button!=null && button.equals("submit_change_task"))
		      {	
		    	  String comments=request.getParameter("change_task_comment");
		    	  if(comments!=null && !comments.equals("")){
		    	  //rowId=workflowProcessManager.saveProcessTask(context,request,"change_taskfinished_reason_cmts",comments);
		    	  rowId=workflowProcessManager.changeTask(context,request);
		    	  }
		    	  if(rowId>0){
		    		  request.setAttribute("successMessage","Successfully complete your task.");
		    	  }
		    	  else{
		    		  request.setAttribute("errorMessage","Not  complete your task! Plz try agin.");
		    	  }
		    	  request.setAttribute("pageMode", "doc_properties");
		    	  request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		    	  context.complete();
		      }
		      else if(button!=null && button.equals("submit_associate_task")){
		    	  request.setAttribute("pageMode", "show_associate_task");
		    	  request.setAttribute("userlist", taskManager.getUser(context));
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		        else if(button!=null && button.equals("submit_task_save")){
		    	  int saveId=0;
		    	  log.info("save adhock workflow:-------------------------"+request.getParameter("workflowId"));
		    	  log.info("save adhock workflow:-------------------------"+request.getParameter("file_description"));
		    	  try{
		        		 saveId=adhocWorkflowManager.saveAdhocTask(context, request);
		        		}catch(ParseException pe){
		        			log.error(""+pe);
		        		}
		        		log.info("save adhock workflow:-------------------------"+rowId);
		        	 	if(saveId>0){
		        	request.setAttribute("successMessage", "Workflow has been successfully sent.");
		        	}else{
		        		request.setAttribute("errorMessage", "Workflow has not been successfully sent! Plz try again.");
		        	}
		        request.setAttribute("pageMode", "doc_properties");
		        request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
			    JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      context.complete();
		      }
		        else if(button!=null && button.equals("submit_reassign_task")){
			    	  request.setAttribute("pageMode", "show_reassign_task");
			    	  request.setAttribute("userlist", taskManager.getUser(context));
			    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
			      }
			      else if(button!=null && button.equals("submit_reassign_task_save"))
			      {
			    	  int	 saveId=0;
			    	  TaskMasterBean taskMasterBean=new TaskMasterBean();
			    	  try {
						taskMasterBean.setDeadline_day(new java.sql.Date(CommonDateFormat.getDateyyyymmdd(request.getParameter("deadline_day")).getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	  String user_assign[]=	request.getParameterValues("assign_user_id"); 
			    	  Integer task_id=Integer.parseInt(request.getParameter("task_id"));
			    	  for(int i=0; i<user_assign.length; i++)
		        	 	{
		        		 saveId=taskManager.saveTaskRole(context,task_id,Integer.valueOf(user_assign[i]),0,taskMasterBean);
		        		   log.info("tsave Task Role id:------------------------"+saveId);
		        	 	}
			    	  if(saveId>0){
				        	request.setAttribute("successMessage", "Workflow has been successfully reassigne.");
				        	}else{
				        		request.setAttribute("errorMessage", "Workflow has not been successfully reassigne! Plz try again.");
				        	}
			    	  request.setAttribute("pageMode", "doc_properties");
				      request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));
					  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
					  context.complete();
			      }
		      else
		      {		    	  
		    	JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		      }
		      }		      
		      else
		      {
		    	 response.sendRedirect(request.getContextPath()+"/password-login");
		      }
		     
            }
	
	
	 private Integer getItem_id(HttpServletRequest request)
		{
	    	String value="",field_name="";
	       	 FileItemFactory factory = new DiskFileItemFactory();
	       	ServletFileUpload upload = new ServletFileUpload( factory );
	       	List<FileItem> uploadItems;
	   		try {
	   			uploadItems = upload.parseRequest( request );
	   			for( FileItem uploadItem : uploadItems )
	   	    	{
	   	    	  if( uploadItem.isFormField() )
	   	    	  {
	   	    		field_name=uploadItem.getFieldName();
	   	    	     value = uploadItem.getString();
	   	    	    System.out.println("value:---"+field_name+"----value----"+value);
	   	    	  }
	   	    	}
	   		} catch (FileUploadException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	   
		return Integer.valueOf(value);	
		}
	
	private void processUploadBitstream(Context context,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        try {
            // Wrap multipart request to get the submission info
            FileUploadRequest wrapper = new FileUploadRequest(request);
            Bitstream b = null;
            Item item = Item.find(context, UIUtil.getIntParameter(wrapper, "item_id"));
            log.info("processUploadBitstream:-----------id-------------------"+item.getID());
            log.info("process_id:----UIUtil-----------"+UIUtil.getIntParameter(wrapper, "process_id"));
            File temp = wrapper.getFile("file");

            // Read the temp file as logo
            InputStream is = new BufferedInputStream(new FileInputStream(temp));

            // now check to see if person can edit item
            log.info("I am here:-------check for item authorization");
            checkEditAuthorization(context, item);
            log.info("I am here:-------check for item authorization after check:-----");
            // do we already have an ORIGINAL bundle?
            Bundle[] bundles = item.getBundles("ORIGINAL");

            if (bundles.length < 1)
            {
                // set bundle's name to ORIGINAL
                b = item.createSingleBitstream(is, "ORIGINAL");

                // set the permission as defined in the owning collection
                Collection owningCollection = item.getOwningCollection();
                if (owningCollection != null)
                {
                    Bundle bnd = b.getBundles()[0];
                    bnd.inheritCollectionDefaultPolicies(owningCollection);
                }
            } 
            else
            {
                // we have a bundle already, just add bitstream
                b = bundles[0].createBitstream(is);
            }

            // Strip all but the last filename. It would be nice
            // to know which OS the file came from.
            String noPath = wrapper.getFilesystemName("file");

            while (noPath.indexOf('/') > -1)
            {
                noPath = noPath.substring(noPath.indexOf('/') + 1);
            }

            while (noPath.indexOf('\\') > -1)
            {
                noPath = noPath.substring(noPath.indexOf('\\') + 1);
            }

            b.setName(noPath);
            b.setSource(wrapper.getFilesystemName("file"));

            // Identify the format
            BitstreamFormat bf = FormatIdentifier.guessFormat(context, b);
            b.setFormat(bf);
            b.update();

            item.update();

            // Back to edit form
            showEditForm(context, request, response, item);

            // Remove temp file
            if (!temp.delete())
            {
                log.error("Unable to delete temporary file");
            }

            // Update DB
            context.complete();
        } catch (FileSizeLimitExceededException ex)
        {
            log.warn("Upload exceeded upload.max");
            JSPManager.showFileSizeLimitExceededError(request, response, ex.getMessage(), ex.getActualSize(), ex.getPermittedSize());
        }
    }
	
	 private void checkEditAuthorization(Context c, Item item)
	            throws AuthorizeException, java.sql.SQLException
	    {
	        if (!item.canEdit())
	        {
	            int userID = 0;

	            // first, check if userid is set
	            if (c.getCurrentUser() != null)
	            {
	                userID = c.getCurrentUser().getID();
	            }

	            // show an error or throw an authorization exception
	            throw new AuthorizeException("EditItemServlet: User " + userID
	                    + " not authorized to edit item " + item.getID());
	        }
	    }
	 
	 /**
	     * Show the item edit form for a particular item
	     *
	     * @param context
	     *            DSpace context
	     * @param request
	     *            the HTTP request containing posted info
	     * @param response
	     *            the HTTP response
	     * @param item
	     *            the item
	     */
	    private void showEditForm(Context context, HttpServletRequest request,
	            HttpServletResponse response, Item item) throws ServletException,
	            IOException, SQLException, AuthorizeException
	    {
	        if ( request.getParameter("cc_license_url") != null )
	        {
	            // check authorization
	            AuthorizeUtil.authorizeManageCCLicense(context, item);
	            
	            // turn off auth system to allow replace also to user that can't
	            // remove/add bitstream to the item
	            context.turnOffAuthorisationSystem();
	                // set or replace existing CC license
	                CreativeCommons.setLicense( context, item,
	                   request.getParameter("cc_license_url") );
	                context.restoreAuthSystemState();
	                context.commit();
	        }
	  
	        // Get the handle, if any
	        String handle = HandleManager.findHandle(context, item);

	        // Collections
	        Collection[] collections = item.getCollections();

	        // All DC types in the registry
	        MetadataField[] types = MetadataField.findAll(context);
	        
	        // Get a HashMap of metadata field ids and a field name to display
	        Map<Integer, String> metadataFields = new HashMap<Integer, String>();
	        
	        // Get all existing Schemas
	        MetadataSchema[] schemas = MetadataSchema.findAll(context);
	        for (int i = 0; i < schemas.length; i++)
	        {
	            String schemaName = schemas[i].getName();
	            // Get all fields for the given schema
	            MetadataField[] fields = MetadataField.findAllInSchema(context, schemas[i].getSchemaID());
	            for (int j = 0; j < fields.length; j++)
	            {
	                Integer fieldID = Integer.valueOf(fields[j].getFieldID());
	                String displayName = "";
	                displayName = schemaName + "." + fields[j].getElement() + (fields[j].getQualifier() == null ? "" : "." + fields[j].getQualifier());
	                metadataFields.put(fieldID, displayName);
	            }
	        }

	        request.setAttribute("admin_button", AuthorizeManager.authorizeActionBoolean(context, item, Constants.ADMIN));
	        try
	        {
	            AuthorizeUtil.authorizeManageItemPolicy(context, item);
	            request.setAttribute("policy_button", Boolean.TRUE);
	        }
	        catch (AuthorizeException authex)
	        {
	            request.setAttribute("policy_button", Boolean.FALSE);
	        }
	        
	        if (AuthorizeManager.authorizeActionBoolean(context, item
	                .getParentObject(), Constants.REMOVE))
	        {
	            request.setAttribute("delete_button", Boolean.TRUE);
	        }
	        else
	        {
	            request.setAttribute("delete_button", Boolean.FALSE);
	        }
	        
	        try
	        {
	            AuthorizeManager.authorizeAction(context, item, Constants.ADD);
	            request.setAttribute("create_bitstream_button", Boolean.TRUE);
	        }
	        catch (AuthorizeException authex)
	        {
	            request.setAttribute("create_bitstream_button", Boolean.FALSE);
	        }
	        
	        try
	        {
	            AuthorizeManager.authorizeAction(context, item, Constants.REMOVE);
	            request.setAttribute("remove_bitstream_button", Boolean.TRUE);
	        }
	        catch (AuthorizeException authex)
	        {
	            request.setAttribute("remove_bitstream_button", Boolean.FALSE);
	        }
	        
	        try
	        {
	            AuthorizeUtil.authorizeManageCCLicense(context, item);
	            request.setAttribute("cclicense_button", Boolean.TRUE);
	        }
	        catch (AuthorizeException authex)
	        {
	            request.setAttribute("cclicense_button", Boolean.FALSE);
	        }
	        
	        try
	        {
	            if( 0 < item.getBundles("ORIGINAL").length){
	                AuthorizeUtil.authorizeManageBundlePolicy(context, item.getBundles("ORIGINAL")[0]);
	                request.setAttribute("reorder_bitstreams_button", Boolean.TRUE);
	            }
	        }
	        catch (AuthorizeException authex)
	        {
	            request.setAttribute("reorder_bitstreams_button", Boolean.FALSE);
	        }

	        if (!item.isWithdrawn())
	        {
	            try
	            {
	                AuthorizeUtil.authorizeWithdrawItem(context, item);
	                request.setAttribute("withdraw_button", Boolean.TRUE);
	            }
	            catch (AuthorizeException authex)
	            {
	                request.setAttribute("withdraw_button", Boolean.FALSE);
	            }
	        }
	        else
	        {
	            try
	            {
	                AuthorizeUtil.authorizeReinstateItem(context, item);
	                request.setAttribute("reinstate_button", Boolean.TRUE);
	            }
	            catch (AuthorizeException authex)
	            {
	                request.setAttribute("reinstate_button", Boolean.FALSE);
	            }
	        }

			if (item.isDiscoverable()) 
			{
				request.setAttribute("privating_button", AuthorizeManager
						.authorizeActionBoolean(context, item, Constants.WRITE));
			} 
			else 
			{
				request.setAttribute("publicize_button", AuthorizeManager
						.authorizeActionBoolean(context, item, Constants.WRITE));
			}
	        
	        request.setAttribute("item", item);
	        request.setAttribute("handle", handle);
	        request.setAttribute("collections", collections);
	        request.setAttribute("dc.types", types);
	        request.setAttribute("metadataFields", metadataFields);

	        JSPManager.showJSP(request, response, "/tools/edit-item-form.jsp");
	    }


}
