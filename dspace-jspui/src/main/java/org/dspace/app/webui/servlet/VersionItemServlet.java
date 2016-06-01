/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.app.util.SubmissionInfo;
import org.dspace.app.util.SubmissionStepConfig;
import org.dspace.app.webui.util.FileUploadRequest;
import org.dspace.app.webui.util.JSONUploadResponse;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.app.webui.util.VersionUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.FormatIdentifier;
import org.dspace.content.Item;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.submit.step.UploadStep;
import org.dspace.workflow.WorkflowItem;

import com.google.gson.Gson;

/**
 * Servlet to handling the versioning of the item
 * 
 * @author Pascarelli Luigi Andrea
 * @version $Revision$
 */

public class VersionItemServlet extends DSpaceServlet
{

    /** log4j category */
    private static Logger log = Logger.getLogger(VersionItemServlet.class);
    private String tempDir = null;
    /** First step after "select collection" */
    public static final int FIRST_STEP = 1;
    
    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
    	       
        Integer itemID= UIUtil.getIntParameter(request,"itemID");	
        
        log.info("itemID:-----------"+itemID);
        
        Item item = Item.find(context,itemID);
        
        String submit = UIUtil.getSubmitButton(request,"submit");
        
        log.info("submit:-----------"+submit);
        String summary = "change";
        
        if (submit!=null && submit.equals("submit"))
        {
        	 // Integer wsid = VersionUtil.processCreateNewVersion(context, itemID, summary);            
              // response.sendRedirect(request.getContextPath()+"/submit?resume=" + wsid);
              // response.sendRedirect(request.getContextPath()+"/mydspace");
            //   context.complete();
        	Integer wsid = VersionUtil.processCreateNewVersion(context, itemID, summary);  
            request.setAttribute("wsid", wsid);
            request.setAttribute("itemID", itemID);
           // JSPManager.showJSP(request, response,"/tools/version-summary.jsp");            
                        
            response.sendRedirect(request.getContextPath()+"/submit?resume=" + wsid);        
            return;
        }
        
        if (submit!=null && submit.equals("submit_version"))
        {        	
          //  Integer wsid = VersionUtil.processCreateNewVersion(context, itemID, summary);            
          //   response.sendRedirect(request.getContextPath()+"/submit?resume=" + wsid);
            //response.sendRedirect(request.getContextPath()+"/mydspace");
            //need to find out what type of form we are dealing with
            String contentType = request.getContentType();
        	  if ((contentType != null)
                      && (contentType.indexOf("multipart/form-data") != -1))
              {
                  try
                  {
                          request = wrapMultipartRequest(request);                    
                          // check if the POST request was send by resumable.js
                          String resumableFilename = request.getParameter("resumableFilename");                    
                          if (!StringUtils.isEmpty(resumableFilename))
                          {
                              log.info("resumable Filename: '" + resumableFilename + "'.");
                              File completedFile = null;
                              try
                              {
                                  log.info("Starting doPostResumable method.");
                                  completedFile = doPostResumable(request);
                              } catch(IOException e){
                                  // we were unable to receive the complete chunk => initialize reupload
                                  response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                              }
                              
                              if (completedFile == null)
                              {
                                  // if a part/chunk was uploaded, but the file is not completly uploaded yet
                                  log.info("Got one file chunk, but the upload is not completed yet.");
                                  return;
                              }
                              else
                              {
                                  // We got the complete file. Assemble it and store
                                  // it in the repository.
                                  log.info("Going to assemble file chunks.");

                                  if (completedFile.length() > 0)
                                  {
                                  	
                                      String fileName = completedFile.getName();
                                      log.info("fileName:-"+fileName);
                                      String filePath = tempDir + File.separator + fileName;
                                      // Read the temporary file
                                      InputStream fileInputStream = 
                                              new BufferedInputStream(new FileInputStream(completedFile));
                                      
                                      // to safely store the file in the repository
                                      // we have to add it as a bitstream to the
                                      // appropriate item (or to be specific its
                                      // bundle). Instead of rewriting this code,
                                      // we should use the same code, that's used for
                                      // the "old" file upload (which is not using JS).
                                      SubmissionInfo si = getSubmissionInfo(context, request);
                                      UploadStep us = new UploadStep();
                                      request.setAttribute(fileName + "-path", filePath);
                                      request.setAttribute(fileName + "-inputstream", fileInputStream);
                                      request.setAttribute(fileName + "-description", request.getParameter("description"));
                                      int uploadResult = us.processUploadFile(context, request, response, si);

                                      // cleanup our temporary file
                                      if (!completedFile.delete())
                                      {
                                          log.info("Unable to delete temporary file " + filePath);
                                      }

                                      // We already assembled the complete file.
                                      // In case of any error it won't help to
                                      // reupload the last chunk. That makes the error
                                      // handling realy easy:
                                      if (uploadResult != UploadStep.STATUS_COMPLETE)
                                      {
                                      	 log.info("STATUS_COMPLETE");
                                          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                          return;
                                      }
                                      context.commit();
                                  }
                                  return;
                              }
                         }
                  }
                  catch (FileSizeLimitExceededException e)
                  {
                      log.warn("Upload exceeded upload.max");
                      if (ConfigurationManager.getBooleanProperty("webui.submit.upload.progressbar", true))
                      {
                          Gson gson = new Gson();
                          // old browser need to see this response as html to work            
                          response.setContentType("text/html");
                          JSONUploadResponse jsonResponse = new JSONUploadResponse();
                          jsonResponse.addUploadFileSizeLimitExceeded(
                                  e.getActualSize(), e.getPermittedSize());
                          response.getWriter().print(gson.toJson(jsonResponse));
                          response.flushBuffer();                    
                      }
                      else
                      {
                          JSPManager.showFileSizeLimitExceededError(request, response, e.getMessage(), e.getActualSize(), e.getPermittedSize());                    
                      }
                      return;
                  }
                  log.info("uploadFiles:--->");
                  //also, upload any files and save their contents to Request (for later processing by UploadStep)
                  uploadFiles(context, request);
              }
        	
        	
        	
            context.complete();
            
            return;
        }
        
        else if (submit!=null && submit.equals("submit_update_version")){
            String versionID = request.getParameter("versionID");
            request.setAttribute("itemID", itemID);
            request.setAttribute("versionID", versionID);
            JSPManager.showJSP(request, response,
                    "/tools/version-update-summary.jsp");
            return;
        }
        
        //Send us back to the item page if we cancel !
        response.sendRedirect(request.getContextPath() + "/handle/" + item.getHandle());
        context.complete();
        
    }

   
    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // If this is not overridden, we invoke the raw HttpServlet "doGet" to
        // indicate that POST is not supported by this servlet.
       // doDSGet(UIUtil.obtainContext(request), request, response);
    	
    	/*String contentType = request.getContentType();
    	log.info("contentType:-------"+contentType);
    	 if ((contentType != null)
                && (contentType.indexOf("multipart/form-data") != -1))
        {
            // This is a multipart request, so it's a file upload
           processUploadBitstream(context, request, response);
           
            return;
        }
    	 else
        {
        	doDSGet(UIUtil.obtainContext(request), request, response);
        }*/    	
    	
        //  Integer wsid = VersionUtil.processCreateNewVersion(context, itemID, summary);            
        //   response.sendRedirect(request.getContextPath()+"/submit?resume=" + wsid);
          //response.sendRedirect(request.getContextPath()+"/mydspace");
          //need to find out what type of form we are dealing with
    	
          String contentType = request.getContentType();
          
          log.info("contentType:-------------->>>"+contentType);
          
      	  if ((contentType != null)
                    && (contentType.indexOf("multipart/form-data") != -1))
            {
                try
                {
                        request = wrapMultipartRequest(request);                    
                        // check if the POST request was send by resumable.js
                        String resumableFilename = request.getParameter("resumableFilename");                    
                        if (!StringUtils.isEmpty(resumableFilename))
                        {
                            log.info("resumable Filename: '" + resumableFilename + "'.");
                            File completedFile = null;
                            try
                            {
                                log.info("Starting doPostResumable method.");
                                completedFile = doPostResumable(request);
                            } catch(IOException e){
                                // we were unable to receive the complete chunk => initialize reupload
                                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                            }
                            
                            if (completedFile == null)
                            {
                                // if a part/chunk was uploaded, but the file is not completly uploaded yet
                                log.info("Got one file chunk, but the upload is not completed yet.");
                                return;
                            }
                            else
                            {
                                // We got the complete file. Assemble it and store
                                // it in the repository.
                                log.info("Going to assemble file chunks.");

                                if (completedFile.length() > 0)
                                {
                                	
                                    String fileName = completedFile.getName();
                                    log.info("fileName:-"+fileName);
                                    String filePath = tempDir + File.separator + fileName;
                                    // Read the temporary file
                                    InputStream fileInputStream = 
                                            new BufferedInputStream(new FileInputStream(completedFile));
                                    
                                    // to safely store the file in the repository
                                    // we have to add it as a bitstream to the
                                    // appropriate item (or to be specific its
                                    // bundle). Instead of rewriting this code,
                                    // we should use the same code, that's used for
                                    // the "old" file upload (which is not using JS).
                                    SubmissionInfo si = getSubmissionInfo(context, request);
                                    UploadStep us = new UploadStep();
                                    request.setAttribute(fileName + "-path", filePath);
                                    request.setAttribute(fileName + "-inputstream", fileInputStream);
                                    request.setAttribute(fileName + "-description", request.getParameter("description"));
                                    int uploadResult = us.processUploadFile(context, request, response, si);

                                    // cleanup our temporary file
                                    if (!completedFile.delete())
                                    {
                                        log.info("Unable to delete temporary file " + filePath);
                                    }

                                    // We already assembled the complete file.
                                    // In case of any error it won't help to
                                    // reupload the last chunk. That makes the error
                                    // handling realy easy:
                                    if (uploadResult != UploadStep.STATUS_COMPLETE)
                                    {
                                    	 log.info("STATUS_COMPLETE");
                                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                        return;
                                    }
                                    context.commit();
                                }
                                return;
                            }
                       }
                }
                catch (FileSizeLimitExceededException e)
                {
                    log.warn("Upload exceeded upload.max");
                    if (ConfigurationManager.getBooleanProperty("webui.submit.upload.progressbar", true))
                    {
                        Gson gson = new Gson();
                        // old browser need to see this response as html to work            
                        response.setContentType("text/html");
                        JSONUploadResponse jsonResponse = new JSONUploadResponse();
                        jsonResponse.addUploadFileSizeLimitExceeded(
                                e.getActualSize(), e.getPermittedSize());
                        response.getWriter().print(gson.toJson(jsonResponse));
                        response.flushBuffer();                    
                    }
                    else
                    {
                        JSPManager.showFileSizeLimitExceededError(request, response, e.getMessage(), e.getActualSize(), e.getPermittedSize());                    
                    }
                    return;
                }
                log.info("uploadFiles:--->");
                //also, upload any files and save their contents to Request (for later processing by UploadStep)
                uploadFiles(context, request);
            }
      		
      	
          context.complete();
          JSPManager.showJSP(request, response,"/tools/version-summary.jsp");
    }

    /**
     * Process the input from the upload bitstream page
     *
     * @param context
     *            current DSpace context
     * @param request
     *            current servlet request object
     * @param response
     *            current servlet response object
     */
  
    private void processUploadBitstream(Context context,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        try 
        {
            // Wrap multipart request to get the submission info
            FileUploadRequest wrapper = new FileUploadRequest(request);
            Bitstream b = null;
            Item item = Item.find(context, UIUtil.getIntParameter(wrapper, "itemID"));
            File temp = wrapper.getFile("file");

            // Read the temp file as logo
            InputStream is = new BufferedInputStream(new FileInputStream(temp));

            // now check to see if person can edit item
            checkEditAuthorization(context, item);

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
           // showEditForm(context, request, response, item);

            // Remove temp file
            if (!temp.delete())
            {
                log.error("Unable to delete temporary file");
            }

            // Update DB
            context.complete();
            response.sendRedirect(request.getContextPath() + "/handle/" + item.getHandle());
            
        } catch (FileSizeLimitExceededException ex)
        {
            log.warn("Upload exceeded upload.max");
            JSPManager.showFileSizeLimitExceededError(request, response, ex.getMessage(), ex.getActualSize(), ex.getPermittedSize());
        }
        
        //Item item = Item.find(context,itemID);
   	   
    }
    
    
    
    /**
     * Wraps a multipart form request, so that its attributes and parameters can
     * still be accessed as normal.
     * 
     * @return wrapped multipart request object
     * 
     * @throws ServletException
     *             if there are no more pages in this step
     */
    private HttpServletRequest wrapMultipartRequest(HttpServletRequest request)
            throws ServletException, FileSizeLimitExceededException
    {
        HttpServletRequest wrappedRequest;

        try
        {
            // if not already wrapped
            if (!Class.forName("org.dspace.app.webui.util.FileUploadRequest")
                    .isInstance(request))
            {
                // Wrap multipart request
                wrappedRequest = new FileUploadRequest(request);

                return (HttpServletRequest) wrappedRequest;
            }
            else
            { // already wrapped
                return request;
            }
        }
        catch (FileSizeLimitExceededException e)
        {
            throw new FileSizeLimitExceededException(e.getMessage(),e.getActualSize(),e.getPermittedSize());
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    
    /**
     * Upload any files found on the Request or in assembledFiles, and save them back as 
     * Request attributes, for further processing by the appropriate user interface.
     * 
     * @param context
     *            current DSpace context
     * @param request
     *            current servlet request object
     */
    public void uploadFiles(Context context, HttpServletRequest request)
            throws ServletException
    {
        FileUploadRequest wrapper = null;
        String filePath = null;
        InputStream fileInputStream = null;

        try
        {
            // if we already have a FileUploadRequest, use it
            if (Class.forName("org.dspace.app.webui.util.FileUploadRequest")
                    .isInstance(request))
            {
                wrapper = (FileUploadRequest) request;
            }
            else
            {
                // Wrap multipart request to get the submission info
                wrapper = new FileUploadRequest(request);
            }
            
            log.info("Did not recoginze resumable upload, falling back to "
                    + "simple upload.");
            Enumeration fileParams = wrapper.getFileParameterNames();
            while (fileParams.hasMoreElements()) 
            {
                String fileName = (String) fileParams.nextElement();

                File temp = wrapper.getFile(fileName);

                //if file exists and has a size greater than zero
                if (temp != null && temp.length() > 0) 
                {
                    // Read the temp file into an inputstream
                    fileInputStream = new BufferedInputStream(
                            new FileInputStream(temp));

                    filePath = wrapper.getFilesystemName(fileName);

                    // cleanup our temp file
                    if (!temp.delete()) 
                    {
                        log.error("Unable to delete temporary file");
                    }

                    //save this file's info to request (for UploadStep class)
                    request.setAttribute(fileName + "-path", filePath);
                    request.setAttribute(fileName + "-inputstream", fileInputStream);
                    request.setAttribute(fileName + "-description", wrapper.getParameter("description"));
                }
            }
        }
        catch (Exception e)
        {
            // Problem with uploading
            log.warn(LogManager.getHeader(context, "upload_error", ""), e);
            throw new ServletException(e);
        }
    }
    
    
 // Resumable.js uses HTTP Get to recognize whether a specific part/chunk of 
    // a file was uploaded already. This method handles those requests.
    protected void DoGetResumable(HttpServletRequest request, HttpServletResponse response) 
        throws IOException
    {
    	log.info("uploda file:--DoGetResumable>");
        if (ConfigurationManager.getProperty("upload.temp.dir") != null)
        {
            tempDir = ConfigurationManager.getProperty("upload.temp.dir");
        }
        else
        {
            tempDir = System.getProperty("java.io.tmpdir");
        }

        String resumableIdentifier = request.getParameter("resumableIdentifier");
        String resumableChunkNumber = request.getParameter("resumableChunkNumber");
        long resumableCurrentChunkSize = 
                Long.valueOf(request.getParameter("resumableCurrentChunkSize"));

        tempDir = tempDir + File.separator + resumableIdentifier;

        File fileDir = new File(tempDir);

        // create a new directory for each resumableIdentifier
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        // use the String "part" and the chunkNumber as filename of a chunk
        String chunkPath = tempDir + File.separator + "part" + resumableChunkNumber;

        File chunkFile = new File(chunkPath);
        // if the chunk was uploaded already, we send a status code of 200
        if (chunkFile.exists()) {
            if (chunkFile.length() == resumableCurrentChunkSize) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            // The chunk file does not have the expected size, delete it and 
            // pretend that it wasn't uploaded already.
            chunkFile.delete();
        }
        // if we don't have the chunk send a http status code 404
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    // Resumable.js sends chunks of files using http post.
    // If a chunk was the last missing one, we have to assemble the file and
    // return it. If other chunks are missing, we just return null.
    protected File doPostResumable(HttpServletRequest request)
            throws FileSizeLimitExceededException, IOException, ServletException 
    {
        File completedFile = null;
        FileUploadRequest wrapper = null;
        log.info("uploda file:--doPostResumable>");
        if (ConfigurationManager.getProperty("upload.temp.dir") != null)
        {
            tempDir = ConfigurationManager.getProperty("upload.temp.dir");
        }
        else {
            tempDir = System.getProperty("java.io.tmpdir");
        }
        
        try
        {
            // if we already have a FileUploadRequest, use it
            if (Class.forName("org.dspace.app.webui.util.FileUploadRequest").isInstance(request))
            {
                wrapper = (FileUploadRequest) request;
            } 
            else // if not wrap the mulitpart request to get the submission info
            {
                wrapper = new FileUploadRequest(request);
            }
        }
        catch (ClassNotFoundException ex)
        {
            // Cannot find a class that is part of the JSPUI?
            log.fatal("Cannot find class org.dspace.app.webui.util.FileUploadRequest");
            throw new ServletException("Cannot find class org.dspace.app.webui.util.FileUploadRequest.", ex);
        }

        String resumableIdentifier = wrapper.getParameter("resumableIdentifier");
        long resumableTotalSize = Long.valueOf(wrapper.getParameter("resumableTotalSize"));
        int resumableTotalChunks = Integer.valueOf(wrapper.getParameter("resumableTotalChunks"));

        String chunkDirPath = tempDir + File.separator + resumableIdentifier;
        File chunkDirPathFile = new File(chunkDirPath);
        boolean foundAll = true;
        long currentSize = 0l;
        
        // check whether all chunks were received.
        if(chunkDirPathFile.exists())
        {
            for (int p = 1; p <= resumableTotalChunks; p++) 
            {
                File file = new File(chunkDirPath + File.separator + "part" + Integer.toString(p));

                if (!file.exists()) 
                {
                    foundAll = false;
                    break;
                }
                currentSize += file.length();
            }
        }
        
        if (foundAll && currentSize >= resumableTotalSize) 
        {
            try {
                // assemble the file from it chunks.
                File file = makeFileFromChunks(tempDir, chunkDirPathFile, wrapper);
            
                if (file != null) 
                {
                    completedFile = file;
                }
            } catch (IOException ex) {
                // if the assembling of a file results in an IOException a
                // retransmission has to be triggered. Throw the IOException
                // here and handle it above.
                throw ex;
            }
        }

        return completedFile;
    }
    
    // assembles a file from it chunks
    protected File makeFileFromChunks(String tmpDir, File chunkDirPath, HttpServletRequest request) 
            throws IOException
    {
    	  log.info("uploda file:--makeFileFromChunks>");
        int resumableTotalChunks = Integer.valueOf(request.getParameter("resumableTotalChunks"));
        String resumableFilename = request.getParameter("resumableFilename");
        String chunkPath = chunkDirPath.getAbsolutePath() + File.separator + "part";
        File destFile = null;

        String destFilePath = tmpDir + File.separator + resumableFilename;
        destFile = new File(destFilePath);
        InputStream is = null;
        OutputStream os = null;

        try {
            destFile.createNewFile();
            os = new FileOutputStream(destFile);

            for (int i = 1; i <= resumableTotalChunks; i++) 
            {
                File fi = new File(chunkPath.concat(Integer.toString(i)));
                try 
                {
                    is = new FileInputStream(fi);

                    byte[] buffer = new byte[1024];

                    int lenght;

                    while ((lenght = is.read(buffer)) > 0) 
                    {
                        os.write(buffer, 0, lenght);
                    }
                } 
                catch (IOException e) 
                {
                    // try to delete destination file, as we got an exception while writing it.
                    if(!destFile.delete())
                    {
                        log.warn("While writing an uploaded file an error occurred. "
                                + "We were unable to delete the damaged file: " 
                                + destFile.getAbsolutePath() + ".");
                    }
                    // throw IOException to handle it in the calling method
                    throw e;
                }
            }
        } 
        finally 
        {
            try 
            {
                if (is != null) 
                {
                    is.close();
                }
            } 
            catch (IOException ex) 
            {
                // nothing to do here
            }
            try 
            {
                if (os != null) 
                {
                    os.close();
                }
            } 
            catch (IOException ex) 
            {
                // nothing to do here
            }
            if (!deleteDirectory(chunkDirPath)) 
            {
                log.warn("Coudln't delete temporary upload path " + chunkDirPath.getAbsolutePath() + ", ignoring it.");
            }
        }
        return destFile;
    }

    /**
     * Reloads a filled-out submission info object from the parameters in the
     * current request. If there is a problem, <code>null</code> is returned.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            HTTP request
     * 
     * @return filled-out submission info, or null
     */
    public static SubmissionInfo getSubmissionInfo(Context context,
            HttpServletRequest request) throws SQLException, ServletException
    {
        SubmissionInfo info = null;
        log.info("uploda file:--SubmissionInfo>");
        // Is full Submission Info in Request Attribute?
        if (request.getAttribute("submission.info") != null)
        {
            // load from cache
            info = (SubmissionInfo) request.getAttribute("submission.info");
        }
        else
        {
            
            
            // Need to rebuild Submission Info from Request Parameters
            if (request.getParameter("workflow_id") != null)
            {
                int workflowID = UIUtil.getIntParameter(request, "workflow_id");
                
                info = SubmissionInfo.load(request, WorkflowItem.find(context, workflowID));
            }
            else if(request.getParameter("workspace_item_id") != null)
            {
                int workspaceID = UIUtil.getIntParameter(request,
                        "workspace_item_id");
                
                info = SubmissionInfo.load(request, WorkspaceItem.find(context, workspaceID));
            }
            else
            {
                //by default, initialize Submission Info with no item
                info = SubmissionInfo.load(request, null);
            }
            
            // We must have a submission object if after the first step,
            // otherwise something is wrong!
            if ((getStepReached(info) > FIRST_STEP)
                    && (info.getSubmissionItem() == null))
            {
                log.warn(LogManager.getHeader(context,
                        "cannot_load_submission_info",
                        "InProgressSubmission is null!"));
                return null;
            }
               

            if (request.getParameter("bundle_id") != null)
            {
                int bundleID = UIUtil.getIntParameter(request, "bundle_id");
                info.setBundle(Bundle.find(context, bundleID));
            }

            if (request.getParameter("bitstream_id") != null)
            {
                int bitstreamID = UIUtil.getIntParameter(request,
                        "bitstream_id");
                info.setBitstream(Bitstream.find(context, bitstreamID));
            }

            // save to Request Attribute
            saveSubmissionInfo(request, info);
        }// end if unable to load SubInfo from Request Attribute

        return info;
    }

    
    /**
     * Saves the submission info object to the current request.
     * 
     * @param request
     *            HTTP request
     * @param si
     *            the current submission info
     * 
     */
    public static void saveSubmissionInfo(HttpServletRequest request,
            SubmissionInfo si)
    {
    	  log.info("saveSubmissionInfo>");
        // save to request
        request.setAttribute("submission.info", si);
    }
    
    
    /**
     * Find out which step a user has reached in the submission process. If the
     * submission is in the workflow process, this returns -1.
     * 
     * @param subInfo
     *            submission info object
     * 
     * @return step reached
     */
    public static int getStepReached(SubmissionInfo subInfo)
    {
        if (subInfo == null || subInfo.isInWorkflow() || subInfo.getSubmissionItem() == null)
        {
            return -1;
        }
        else
        {
            WorkspaceItem wi = (WorkspaceItem) subInfo.getSubmissionItem();
            int i = wi.getStageReached();

            // Uninitialised workspace items give "-1" as the stage reached
            // this is a special value used by the progress bar, so we change
            // it to "FIRST_STEP"
            if (i == -1)
            {
                i = FIRST_STEP;
            }

            return i;
        }
    }
    
    public boolean deleteDirectory(File path) 
    {
        if (path.exists()) 
        {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) 
            {
                if (files[i].isDirectory()) 
                {
                    deleteDirectory(files[i]);
                } 
                else 
                {
                    files[i].delete();
                }
            }
        }
        
        return (path.delete());
    }
    
    /**
     * Throw an exception if user isn't authorized to edit this item
     *
     * @param c
     * @param item
     */
    
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
}
