package org.dspace.app.webui.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;

import org.dspace.util.EmailUploadThred;

public class EmailUploadServlet extends DSpaceServlet{
	 /** log4j category */
    private static Logger log = Logger.getLogger(EmailUploadServlet.class);
    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException, SQLException, AuthorizeException
{
    doDSGet(context, request, response);
}

	protected void doDSGet(Context context, HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException,
	        SQLException, AuthorizeException
	{
	    try 
	    { 	    
	    String commandArray[] = {"cmd", "/c", "C:\\blukupload\\uploadtest\\test.py -i C:\\blukupload\\metadataupload\\metadata1.csv -c 123456789 -p 859 -u C:\\hclupload859 -t C:\\blukupload\\metadataupload\\template1.csv -d D:\\CBSL\\DMS_5.4\\bin -e rajesh.yadav@cbsl-india.com -o map0.txt"};
	    	Process p = Runtime.getRuntime().exec(commandArray);
	      //  int v=  p.waitFor();
	        EmailUploadThred errorGlobber = new EmailUploadThred(p.getErrorStream(), "CMDTOOL-E");
	        EmailUploadThred outputGlobber = new EmailUploadThred(p.getInputStream(), "CMDTOOL-O");
	        errorGlobber.start();
	        outputGlobber.start();
	        int v=  p.waitFor();

	        /* BufferedReader reader=new BufferedReader(
	            new InputStreamReader(p.getInputStream())
	        ); 
	        String line; 
	        while((line = reader.readLine()) != null) 
	        { 
	            System.out.println(line);
	        } */

	    }
	    catch(IOException e1) {
	    	log.info("email upload error"+e1);
	    } 
	    catch (InterruptedException e) {
			// TODO Auto-generated catch block
	    	log.info("email upload error"+e);
		}
	    response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+ "/mydspace?msg=y"));
	    log.info("Done"); 

	}
}
