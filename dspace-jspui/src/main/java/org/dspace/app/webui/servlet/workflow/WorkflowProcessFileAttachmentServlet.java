package org.dspace.app.webui.servlet.workflow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.dspace.app.util.AuthorizeUtil;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.servlet.workflow.WorkflowProcessTaskServlet;
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
import org.dspace.handle.HandleManager;
import org.dspace.license.CreativeCommons;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.workflowprocess.WorkflowProcessManager;

public class WorkflowProcessFileAttachmentServlet extends DSpaceServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5085478870818340098L;
	private static Logger log = Logger.getLogger(WorkflowProcessFileAttachmentServlet.class);
	 private WorkflowProcessManager workflowProcessManager=new WorkflowProcessManager();
		
	
	 
		public WorkflowProcessManager getWorkflowProcessManager() {
			return workflowProcessManager;
		}
		
		public void setWorkflowProcessManager(
				WorkflowProcessManager workflowProcessManager) {
			this.workflowProcessManager = workflowProcessManager;
		}
		
	protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
            {	
		     String contentType = request.getContentType();
		          if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1))
		          {
		        	 // This is a multipart request, so it's a file upload
		            int rowId= processUploadBitstream(context, request, response);
		            if(rowId>0){
			    		  request.setAttribute("successMessage","Successfully upload your file.");
			    	  }
			    	  else{
			    		  request.setAttribute("errorMessage","Not upload your file! Plz try agin.");
			    	  }
		          }		      
		         
		          request.setAttribute("taskdetails",workflowProcessManager.getTaskDetails(context,request));		             
		    	  JSPManager.showJSP(request, response, "/mydspace/task-process-main.jsp");
		    	  context.complete();
		      }
           
	
	
	private int processUploadBitstream(Context context,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
		int saveID=0;
		String docName="";
        try
        {
           // Wrap multipart request to get the submission info
            FileUploadRequest wrapper = new FileUploadRequest(request);
            Bitstream b = null;
            Item item = Item.find(context, UIUtil.getIntParameter(wrapper, "item_id"));
            Integer process_id=UIUtil.getIntParameter(wrapper, "process_id");
            Integer task_id=UIUtil.getIntParameter(wrapper, "task_id");
            request.setAttribute("process_task_id", process_id+"-"+task_id);
            Integer workflow_id= UIUtil.getIntParameter(wrapper, "workflow_id");
            Integer filemode=UIUtil.getIntParameter(wrapper, "filemode");
            String handle= item.getHandle();
            Integer item_id=item.getID();            
            File temp = wrapper.getFile("file");     
            Integer step_id=UIUtil.getIntParameter(wrapper, "workflow_stepid");        
            
            // Read the temp file as logo
            InputStream is = new BufferedInputStream(new FileInputStream(temp));
	             // now check to see if person can edit item
	            // checkEditAuthorization(context, item);
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
                	log.info("i m owningCollection block:----------------->>");
                    Bundle bnd = b.getBundles()[0];
                    bnd.inheritCollectionDefaultPolicies(owningCollection);
                }
            } 
            else
            {
            	log.info("file temp:-----7----------");
                // we have a bundle already, just add bitstream
                //b = bundles[0].createBitstream(is);
                b = bundles[0].createBitstreamUnauthorise(is);
                log.info("file temp:-----8----------");
            }

            // Strip all but the last filename. It would be nice
            // to know which OS the file came from.
            String noPath = wrapper.getFilesystemName("file");
            
            log.info("noPath::------------"+noPath);
            while (noPath.indexOf('/') > -1)
            {
                noPath = noPath.substring(noPath.indexOf('/') + 1);
            }
            while (noPath.indexOf('\\') > -1)
            {
                noPath = noPath.substring(noPath.indexOf('\\') + 1);
                docName=noPath;
            }
            b.setName(noPath);
            b.setSource(wrapper.getFilesystemName("file"));

            // Identify the format
            BitstreamFormat bf = FormatIdentifier.guessFormat(context, b);
            b.setFormat(bf);
           // b.update();
            b.updateunauthorize();
            log.error("bitstream update:------------------------------------>>");
           // item.update();
            item.updateunauthorise();
            log.error("item update:------------------------------------>>");
            // Remove temp file
            if (!temp.delete())
            {
                log.error("Unable to delete temporary file");
            }
            int bitstream_id=b.getID();
            String file_link=request.getContextPath()+"/bitstream/"+handle+"/"+b.getSequenceID()+"/"+UIUtil.encodeBitstreamName(docName,Constants.DEFAULT_ENCODING);
            saveID=workflowProcessManager.saveWorkflowprocessdocument(context,""+b.getSequenceID(),file_link,docName,handle,workflow_id,process_id,item_id,step_id,task_id,bitstream_id);
         if(saveID>0){
        	 request.setAttribute("doc_name", UIUtil.encodeBitstreamName(docName,Constants.DEFAULT_ENCODING));
        	 request.setAttribute("path", file_link);
         }
         if(filemode==1){
        request.setAttribute("pageMode", "doc_properties");
         }else{
         request.setAttribute("pageMode", "complete_task");
         }
            // Update DB              
        } catch (FileSizeLimitExceededException ex)
        {
            log.warn("Upload exceeded upload.max");
            JSPManager.showFileSizeLimitExceededError(request, response, ex.getMessage(), ex.getActualSize(), ex.getPermittedSize());
            return 0;
        }
        
        return saveID;
    }
	
	 /*private void checkEditAuthorization(Context c, Item item)
	            throws AuthorizeException, java.sql.SQLException
	    {
	        if (!item.canEdit())
	        {
	        	log.info("i m canEdit block:----------------->>");
	            int userID = 0;
	            // first, check if userid is set
	            if (c.getCurrentUser() != null)
	            {
	            	log.info("i m getCurrentUser block:----------------->>");
	                userID = c.getCurrentUser().getID();
	            }
	            // show an error or throw an authorization exception
	            throw new AuthorizeException("EditItemServlet: User " + userID + " not authorized to edit item " + item.getID());
	        }
	    }*/
	 
	
}
