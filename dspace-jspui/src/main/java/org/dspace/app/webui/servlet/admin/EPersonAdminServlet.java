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
import org.dspace.administer.GroupDms;
import org.dspace.administer.NewUserRegister;
import org.dspace.administer.UserBean;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.servlet.EditProfileServlet;
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

/**
 * Servlet for editing and creating e-people
 * 
 * @author David Stuve
 * @version $Revision$
 */
public class EPersonAdminServlet extends DSpaceServlet
{
        
    /** Logger */
    private static Logger log = Logger.getLogger(EPersonAdminServlet.class);
    NewUserRegister newuser=new NewUserRegister();
    List<UserBean> list=new ArrayList<UserBean>();

    public NewUserRegister getNewuser() {
		return newuser;
	}

	public void setNewuser(NewUserRegister newuser) {
		this.newuser = newuser;
	}

	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        String action=request.getParameter("action");
        log.info("action message==================>"+action);
        if(action!=null && action.equals("add")){
        	log.info("eperson add page open");
            //EPerson e = EPerson.create(context);
        
            int sortBy = Group.ID;
            String sbParam = request.getParameter("sortby");

    		if (sbParam != null && sbParam.equals("id"))
    		{
    			sortBy = Group.ID;
    		}
    		
    		// What's the index of the first group to show?  Default is 0
    		int first = UIUtil.getIntParameter(request, "first");
    		if (first == -1)
            {
                first = 0;
            }

    		// Retrieve the e-people in the specified order
    		Group[] groups = Group.findAll(context, sortBy);
    		
    		// Set attributes for JSP
    		request.setAttribute("sortby", Integer.valueOf(sortBy));
    		request.setAttribute("first",  Integer.valueOf(first));
    		request.setAttribute("groups", groups);
            //request.setAttribute("eperson", e);
            JSPManager.showJSP(request, response,
                    "/dspace-admin/eperson-add.jsp");
        	log.info("eperson i m here:---");
            context.complete();
        }
       
        else
        {
        	showMain(context, request, response);	
        }
         
    }

    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        String button = UIUtil.getSubmitButton(request, "submit");
        String userupdate=request.getParameter("userupdate");
        String[] department =request.getParameterValues("group_id");
        
        /*if(request.getParameter("submit_cancel")!=null)
        {
        	 showMain(context, request, response);
        	 context.complete();
        }*/
        
        if (button.equals("submit_add"))
        {
            // add an EPerson, then jump user to edit page
            EPerson e = EPerson.create(context);

            // create clever name and do update before continuing
            e.setEmail("newuser" + e.getID());
            e.update();

            request.setAttribute("eperson", e);

            JSPManager.showJSP(request, response,
                    "/dspace-admin/eperson-edit.jsp");

            context.complete();
        }
        else if (button.equals("submit_edit"))
        {
            // edit an eperson
            EPerson e = EPerson.find(context, UIUtil.getIntParameter(request,
                    "eperson_id"));
            
            // Check the EPerson exists
            if (e == null)
            {
            	request.setAttribute("no_eperson_selected", Boolean.TRUE);
            	showMain(context, request, response);
            }
            else 
            {            
	            // what groups is this person a member of?
	            Group[] groupMemberships = Group.allMemberGroups(context, e);
	            request.setAttribute("eperson", e);
	            request.setAttribute("group.memberships", groupMemberships);
	            if(userupdate.equals("update"))
            	{           		
            		request.setAttribute("userupdate","update");
            	}
	            
	            
	            /*================================================================================================================*/
	            int sortBy = Group.ID;
	            String sbParam = request.getParameter("sortby");

	    		if (sbParam != null && sbParam.equals("id"))
	    		{
	    			sortBy = Group.ID;
	    		}
	    		
	    		// What's the index of the first group to show?  Default is 0
	    		int first = UIUtil.getIntParameter(request, "first");
	    		if (first == -1)
	            {
	                first = 0;
	            }

	    		// Retrieve the e-people in the specified order
	    		Group[] groups = Group.findAll(context, sortBy);
	    		
	    		// Set attributes for JSP
	    		request.setAttribute("sortby", Integer.valueOf(sortBy));
	    		request.setAttribute("first",  Integer.valueOf(first));
	    		request.setAttribute("groups", groups);
	            /*==================================================================================================================*/
	            JSPManager.showJSP(request, response,
	                    "/dspace-admin/eperson-edit.jsp");
	
	            context.complete();
            }
        }
        else if(button.equals("submit_save_user"))
        {
        	String email=request.getParameter("email");
        	int sortBy = Group.ID;
            String sbParam = request.getParameter("sortby");
        	Group[] groups = Group.findAll(context, sortBy);
        	if (sbParam != null && sbParam.equals("id"))
    		{
    			sortBy = Group.ID;
    		}
    		
    		// What's the index of the first group to show?  Default is 0
    		int first = UIUtil.getIntParameter(request, "first");
    		if (first == -1)
            {
                first = 0;
            }

    		
        	if(EPerson.findByEmail(context,email) == null){
        		
        		String password=request.getParameter("password");
        		String fname=request.getParameter("firstname");
        		String lname=request.getParameter("lastname");
        		String mobile=request.getParameter("phone");
        		String User_Designation=request.getParameter("designation");
        		String superiorename =request.getParameter("superiorename");
        		String superioremailid=request.getParameter("superioremailid");
        		EPerson eperson=EPerson.create(context);
        		eperson.setEmail(email);
        		eperson.setPassword(password);
        		eperson.setFirstName(fname);
        		eperson.setLastName(lname);
        		eperson.setMetadata("phone", mobile);
        		eperson.setUserDesignation(User_Designation);
        		eperson.setSuperiorEmail(superioremailid);
        		eperson.setSuperiorName(superiorename);
        		eperson.setStatus("a");
        		eperson.setMetadata("language","en");
        		eperson.setCanLogIn(true);
        		eperson.setSelfRegistered(false);
        		eperson.update();
        		 newuser.sendWelcomeEmail(context, eperson.getID());
        		 if(department!=null && department.length>0)
                 {
        			 
                 for(int i=0;i<department.length;i++)
                 {
                 eperson.setepersongroup2eperson(eperson.getID(),Integer.parseInt(department[i]));
                 log.info("group id insert group==================>"+department[i]);
                 }
                 }
        		 request.setAttribute("message", "Successfully create user.");
        		showMain(context, request, response);
        		context.complete();
        	   
        	}
        	else{

        		request.setAttribute("emailexists",Boolean.TRUE);
        		request.setAttribute("sortby", Integer.valueOf(sortBy));
        		request.setAttribute("first",  Integer.valueOf(first));
        		request.setAttribute("groups", groups);
        		JSPManager.showJSP(request, response,"/dspace-admin/eperson-add.jsp");
                context.complete();
        	}
        	
        }
        else if (button.equals("submit_save") || button.equals("submit_resetpassword"))
        {
            // Update the metadata for an e-person
            EPerson e = EPerson.find(context, UIUtil.getIntParameter(request,"eperson_id"));

            // see if the user changed the email - if so, make sure
            // the new email is unique
            String oldEmail = e.getEmail();
            log.info("action message==================>"+oldEmail);
            String newEmail = request.getParameter("email").trim();
            //String password =request.getParameter("password");
            String netid = request.getParameter("netid");

            if (!newEmail.equals(oldEmail))
            {
                // change to email, now see if it's unique
                if (EPerson.findByEmail(context, newEmail) == null)
                {
                    // it's unique - proceed!
                    e.setEmail(newEmail);
                    
                    e.setStatus("a");
                    e.setPassword(request.getParameter("password"));
                    e.setSuperiorName(request.getParameter("superiorename"));
                    e.setSuperiorEmail(request.getParameter("superioremailid"));
                    e.setUserDesignation(request.getParameter("designation"));
                   newuser.sendWelcomeEmail(context, e.getID());
                                                                                                  
                    e.setFirstName(request.getParameter("firstname") .equals("") ? null : request.getParameter("firstname"));

                    e.setLastName(request.getParameter("lastname").equals("") ? null : request.getParameter("lastname"));

                    if (netid != null)
                    {
                        e.setNetid(netid.equals("") ? null : netid.toLowerCase());
                    }
                    else
                    {
                        e.setNetid(null);
                    }

                    // FIXME: More data-driven?
                    e.setMetadata("phone", request.getParameter("phone").equals("") ? null : request.getParameter("phone"));
 
                    e.setMetadata("language", request.getParameter("language").equals("") ? null : request.getParameter("language"));
                    
                    e.setCanLogIn((request.getParameter("can_log_in") != null)&& request.getParameter("can_log_in").equals("true"));

                    e.setRequireCertificate((request.getParameter("require_certificate") != null)&& request.getParameter("require_certificate").equals("true"));

                    e.update();
                    request.setAttribute("message", "Successfully update user.");
                    if(department!=null && department.length>0)
                    {
                    for(int i=0;i<department.length;i++)
                    {
                    e.setepersongroup2eperson(e.getID(),Integer.parseInt(department[i]));
                    log.info("group id insert group==================>"+department[i]);
                    }
                    }
                    if (button.equals("submit_resetpassword"))
                    {                        
                        try
                        {
                            resetPassword(context, request, response, e);
                        }
                        catch (MessagingException e1)
                        {
                            JSPManager.showJSP(request, response,"/dspace-admin/eperson-resetpassword-error.jsp");
                            return;
                        }
                    }
                    showMain(context, request, response);
                    context.complete();
                }
                else
                {
                	int sortBy = Group.ID;
                    // not unique - send error message & let try again
                    request.setAttribute("eperson", e);
                    request.setAttribute("email_exists", Boolean.TRUE);
                    Group[] groups = Group.findAll(context, sortBy);
    	    		
    	    		
    	    		request.setAttribute("groups", groups);
                    JSPManager.showJSP(request, response,"/dspace-admin/eperson-edit.jsp");

                    context.complete();
                }
            }
            else
            {
                // no change to email
                if (netid != null)
                {
                    e.setNetid(netid.equals("") ? null : netid.toLowerCase());
                }
                else
                {
                    e.setNetid(null);
                }
                /*========================================================*/
                
                e.setPassword(request.getParameter("password"));
                e.setSuperiorName(request.getParameter("superiorename"));
                e.setSuperiorEmail(request.getParameter("superioremailid"));
                e.setUserDesignation(request.getParameter("designation"));
                /*===============================================================*/
                e.setFirstName(request.getParameter("firstname").equals("") ? null : request.getParameter("firstname"));

                e.setLastName(request.getParameter("lastname").equals("") ? null : request.getParameter("lastname"));

                // FIXME: More data-driven?
                e.setMetadata("phone", request.getParameter("phone").equals("") ? null: request.getParameter("phone"));
                
                e.setMetadata("language", request.getParameter("language").equals("") ? null : request.getParameter("language"));
                         
                e.setCanLogIn((request.getParameter("can_log_in") != null)&& request.getParameter("can_log_in").equals("true"));

                e.setRequireCertificate((request.getParameter("require_certificate") != null)&& request.getParameter("require_certificate").equals("true"));

                e.update();
                newuser.deletegroupID(context, e.getID());
                if(department!=null && department.length>0)
                {
                for(int i=0;i<department.length;i++)
                {
                e.setepersongroup2eperson(e.getID(),Integer.parseInt(department[i]));
                log.info("group id update group==================>"+department[i]);
                }
                }
                
                if (button.equals("submit_resetpassword"))
                {
                    try
                    {
                        resetPassword(context, request, response, e);
                    }
                    catch (MessagingException e1)
                    {
                       
                     JSPManager.showJSP(request, response,
                                        "/dspace-admin/eperson-resetpassword-error.jsp");
                        return;
                    }                   
                }
                
                showMain(context, request, response);
                context.complete();
            }
            


        }
              else if(button.equals("submit_add_cancel"))
        {
        	 // User confirms deletion of type
            EPerson e = EPerson.find(context, UIUtil.getIntParameter(request,
                    "eperson_id"));

            try
            {
                e.delete();
            }
            catch (EPersonDeletionException ex)
            {
                request.setAttribute("eperson", e);
                request.setAttribute("tableList", ex.getTables());
                JSPManager.showJSP(request, response,
                        "/dspace-admin/eperson-deletion-error.jsp");
            }

            showMain(context, request, response);
            context.complete();
        	
        }
        else if (button.equals("submit_delete"))
        {
            // Start delete process - go through verification step
            EPerson e = EPerson.find(context, UIUtil.getIntParameter(request,
                    "eperson_id"));
            
            // Check the EPerson exists
            if (e == null)
            {
            	request.setAttribute("no_eperson_selected", Boolean.TRUE);
            	showMain(context, request, response);
            }
            else 
            {       
	            request.setAttribute("eperson", e);
	
	            JSPManager.showJSP(request, response,
	                    "/dspace-admin/eperson-confirm-delete.jsp");
            }
        }
        else if (button.equals("submit_confirm_delete"))
        {
            // User confirms deletion of type
            EPerson e = EPerson.find(context, UIUtil.getIntParameter(request,
                    "eperson_id"));

            try
            {
                e.delete();
            }
            catch (EPersonDeletionException ex)
            {
                request.setAttribute("eperson", e);
                request.setAttribute("tableList", ex.getTables());
                JSPManager.showJSP(request, response,
                        "/dspace-admin/eperson-deletion-error.jsp");
            }

            showMain(context, request, response);
            context.complete();
        }
        /*======================================================================*/
        else if(button.equals("submit_cancel"))
        {
        	showMain(context, request, response);
        	  context.complete();
        }
      
        /*======================================================================*/
        else if (button.equals("submit_login_as"))
        {
            if (!ConfigurationManager.getBooleanProperty("webui.user.assumelogin", false))
            {
                throw new AuthorizeException("Turn on webui.user.assumelogin to activate Login As feature");                
            }
            EPerson e = EPerson.find(context, UIUtil.getIntParameter(request,
                    "eperson_id"));
            // Check the EPerson exists
            if (e == null)
            {
                request.setAttribute("no_eperson_selected", new Boolean(true));
                showMain(context, request, response);
            }
            // Only super administrators can login as someone else.
            else if (!AuthorizeManager.isAdmin(context))
            {                
                throw new AuthorizeException("Only site administrators may assume login as another user.");
            }
            else
            {
                
                log.info(LogManager.getHeader(context, "login-as",
                        "current_eperson="
                                + context.getCurrentUser().getFullName()
                                + ", id=" + context.getCurrentUser().getID()
                                + ", as_eperson=" + e.getFullName() + ", id="
                                + e.getID()));
                
                // Just to be double be sure, make sure the administrator
                // is the one who actually authenticated himself.
                HttpSession session = request.getSession(false);
                Integer authenticatedID = (Integer) session.getAttribute("dspace.current.user.id"); 
                if (context.getCurrentUser().getID() != authenticatedID)
                {                                         
                    throw new AuthorizeException("Only authenticated users who are administrators may assume the login as another user.");                    
                }
                
                // You may not assume the login of another super administrator
                Group administrators = Group.find(context,1);
                if (administrators.isMember(e))
                {                    
                    JSPManager.showJSP(request, response,
                            "/dspace-admin/eperson-loginas-error.jsp");
                    return;
                }
                               
                // store a reference to the authenticated admin
                session.setAttribute("dspace.previous.user.id", authenticatedID);
                
                // Logged in OK.
                Authenticate.loggedIn(context, request, e);

                // Set the Locale according to user preferences
                Locale epersonLocale = I18nUtil.getEPersonLocale(context
                        .getCurrentUser());
                context.setCurrentLocale(epersonLocale);
                Config.set(request.getSession(), Config.FMT_LOCALE,
                        epersonLocale);

                // Set any special groups - invoke the authentication mgr.
                int[] groupIDs = AuthenticationManager.getSpecialGroups(
                        context, request);

                for (int i = 0; i < groupIDs.length; i++)
                {
                    context.setSpecialGroup(groupIDs[i]);
                    log.debug("Adding Special Group id="
                            + String.valueOf(groupIDs[i]));
                }

                response.sendRedirect(request.getContextPath() + "/mydspace");
            }
        }
        else
        {
            // Cancel etc. pressed - show list again
            showMain(context, request, response);
        }
    }

    private void resetPassword(Context context, HttpServletRequest request,
            HttpServletResponse response, EPerson e) throws SQLException,
            IOException, AuthorizeException, ServletException,
            MessagingException
    {
        // Note, this may throw an error is the email is bad.
        AccountManager.sendForgotPasswordInfo(context, e.getEmail());
        request.setAttribute("reset_password", Boolean.TRUE);
    }

    private void showMain(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {


        // Are we for selecting a single or multiple epeople?
        boolean multiple = Boolean.FALSE;// UIUtil.getBoolParameter(request, "multiple");

        // What are we sorting by. Lastname is default
        //int sortBy = EPerson.LASTNAME;
        int sortBy = EPerson.ID;
        String sbParam = request.getParameter("sortby");

        if ((sbParam != null) && sbParam.equals("lastname"))
        {
            sortBy = EPerson.LASTNAME;
        }
        else if ((sbParam != null) && sbParam.equals("email"))
        {
            sortBy = EPerson.EMAIL;
        }
        else if ((sbParam != null) && sbParam.equals("id"))
        {
            sortBy = EPerson.ID;
        }
        else if ((sbParam != null) && sbParam.equals("language"))
        {
            sortBy = EPerson.LANGUAGE;
        }

        // What's the index of the first eperson to show? Default is 0
        int first = UIUtil.getIntParameter(request, "first");
        int offset = UIUtil.getIntParameter(request, "offset");
        if (first == -1)
        {
            first = 0;
        }
        if (offset == -1)
        {
            offset = 0;
        }
        

        EPerson[] epeople;
        String search = request.getParameter("search");
        if (search != null && !search.equals(""))
        {
            epeople = EPerson.search(context, search);
            request.setAttribute("offset", Integer.valueOf(offset));
        }
        else
        {
            // Retrieve the e-people in the specified order
            epeople = EPerson.findAll(context, sortBy);
            request.setAttribute("offset", Integer.valueOf(0));
        }        
        
        // Set attributes for JSP
        request.setAttribute("sortby", Integer.valueOf(sortBy));
        request.setAttribute("first", Integer.valueOf(first));
        request.setAttribute("epeople", epeople);
        request.setAttribute("search", search);
        
        if (multiple)
        {
            request.setAttribute("multiple", Boolean.TRUE);
        }
        JSPManager.showJSP(request, response, "/dspace-admin/eperson-main.jsp");
    }
}
