/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.Authenticate;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authenticate.AuthenticationManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.I18nUtil;
import org.dspace.core.LogManager;
import org.dspace.eperson.AccountManager;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.EPersonDeletionException;
import org.dspace.eperson.Group;

import jdk.nashorn.internal.ir.BlockLexicalContext;

import org.dspace.administer.NewUserRegister;
import org.dspace.administer.UserBean;
/**
 * Servlet for editing and creating e-people
 * 
 * @author David Stuve
 * @version $Revision$
 */
public class NewUserServlet extends DSpaceServlet
{    
	
	 private static Logger log = Logger.getLogger(NewUserServlet.class); 
	
	List<UserBean> list=new ArrayList<UserBean>();
	NewUserRegister newuser=new NewUserRegister();
	 public NewUserRegister getNewuser() {
			return newuser;
		}

		public void setNewuser(NewUserRegister newuser) {
			this.newuser = newuser;
		}
	
	UserBean userbean=new UserBean();
    protected void doDSGet(Context context, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,SQLException, AuthorizeException
    {
    	
    	list=newuser.getUser(context);
    	log.info("User List===========================>"+list.size());
    	request.setAttribute("list",list);
    	JSPManager.showJSP(request, response,"/dspace-admin/self-user.jsp");
    	context.complete();
    }

    protected void doDSPost(Context context, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,SQLException, AuthorizeException
    {
    	
    	String button = UIUtil.getSubmitButton(request, "submit");
    	log.info("buttons value in NEwUserServlet====================>"+button);
    	Integer user_id=Integer.valueOf(request.getParameter("user_id"));
    	log.info("User ID  NEwUserServlet UserUpdate Method====================>"+user_id);
    	//String submit_approve=request.getParameter("submit_approve");
    	//String submit_disapprove=request.getParameter("submit_disapprove");
    	if(button.equals("submit_approve"))
    	{
    		
    		if(newuser.updateUser(context, user_id)>0)
    			{
    			newuser.sendApproveEmail(context, user_id);
    			list=newuser.getUser(context);
    	    	log.info("User List===========================>"+list.size());
    	    	request.setAttribute("list",list);
    			request.setAttribute("Userupdate", Boolean.TRUE);
    	    	JSPManager.showJSP(request, response,"/dspace-admin/self-user.jsp");
    	    	context.complete();
    			}
    		else
    		{
    			list=newuser.getUser(context);
    	    	log.info("User List===========================>"+list.size());
    	    	request.setAttribute("list",list);
    			request.setAttribute("Usernotupdate",Boolean.TRUE);
    	    	JSPManager.showJSP(request, response,"/dspace-admin/self-user.jsp");
    	    	context.complete();
    		}
    	}
    	else if(button.equals("submit_disapprove"))
    	{
    		if(newuser.UserDisApprove(context, user_id)>0)
    		{
    			newuser.sendDisApproveEmail(context, user_id);
    			list=newuser.getUser(context);
    			log.info("User List===========================>"+list.size());
    	    	request.setAttribute("list",list);
    			request.setAttribute("userdisapprov", Boolean.TRUE);
    	    	JSPManager.showJSP(request, response,"/dspace-admin/self-user.jsp");
    	    	context.complete();
    		}
    		else{
    			list=newuser.getUser(context);
    	    	log.info("User List===========================>"+list.size());
    	    	request.setAttribute("list",list);
    			request.setAttribute("Usernotupdate",Boolean.TRUE);
    	    	JSPManager.showJSP(request, response,"/dspace-admin/self-user.jsp");
    	    	context.complete();
    		}
    	}
    }
}
