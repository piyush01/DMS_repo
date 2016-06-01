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
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;

import com.google.gson.Gson;

public class FolderCreationServlet extends DSpaceServlet {

	
	 private static Logger log = Logger.getLogger(FolderCreationServlet.class);
	 
	 protected void doDSGet(Context context, HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException,
	            SQLException, AuthorizeException
	    {
		 Community toplavelcommunities[]=Community.findAllTop(context);
		 Community communities[]=Community.findAll(context);
		 Group[] groups = Group.findAll(context, Group.ID);
	     EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);
	   
	    
		 String action= request.getParameter("action");
		 request.setAttribute("toplavelcommunities",toplavelcommunities);
		 request.setAttribute("communities",communities);
		 request.setAttribute("groups", groups);
	     request.setAttribute("epeople", epeople);
	     
	     if(action!=null && action.equals("cabinet"))
	     {
	    	 JSPManager.showJSP(request, response,"/tools/create-cabinet.jsp");
	     }
	     if(action!=null && action.equals("folder"))
		 {
			
			
			//request.setAttribute("action","createfolder");
			 JSPManager.showJSP(request, response,"/tools/create-folder.jsp");
		 }
		 else if(action!=null && action.equals("subfolder"))
		 {
		
			 JSPManager.showJSP(request, response,"/tools/create-subfolder.jsp");
		 }
		
		 else if(action!=null && action.equals("ajaxlist"))
		 {
			
			 log.info("Action========>"+action);
			String CabinetID=request.getParameter("cabine_id");
			log.info("Cabinet Id=====>"+CabinetID);
			  Community community1=Community.find(context,Integer.parseInt(CabinetID));
			  Community[] subcommunities = community1.getSubcommunities();
			
			  for(int i=0;i<subcommunities.length;i++)
			  {
				  log.info("Sub comunities=========>"+subcommunities[i].getName());
			  }
			 /* List<Community> list=new ArrayList<Community>();
			  for(Community c:subcommunities)
			  {
				  list.add(c);
			  }*/
			  log.info("CAll method setFolderXml....");
			  Community.setFolderXml(response, subcommunities);
			  log.info("call after setFolderxml====");
			  return;
		 }
		
			 
	    }
	 
	 protected void doDSPost(Context context, HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException,
	            SQLException, AuthorizeException
	    {
		 Community toplavelcommunities[]=Community.findAllTop(context);
		 Community communities[]=Community.findAll(context);
		 Group[] groups1 = Group.findAll(context, Group.ID);
	     EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);
	     request.setAttribute("toplavelcommunities",toplavelcommunities);
		 request.setAttribute("communities",communities);
		 request.setAttribute("groups", groups1);
	     request.setAttribute("epeople", epeople);
		 String action= request.getParameter("action");
		 if(action!=null && action.equals("cabinet"))
	     {
			/* if(request.getParameter("c_button").equals("default"))
	    	 {
	    		 request.setAttribute("c_Button","default");
	    	 }*/
	    	 JSPManager.showJSP(request, response,"/tools/create-cabinet.jsp");
	     }
			 
		 Community community = Community.find(context, UIUtil.getIntParameter(
	                request, "community_id"));
	      
		  String button = UIUtil.getSubmitButton(request, "submit");
		  String cabinetid=request.getParameter("cabinet");
		 String foldername=request.getParameter("foldername");
		 String cabinetname=request.getParameter("cabinetname");
		  String subfoldername=request.getParameter("subfoldername");
		  int  group=Integer.parseInt(request.getParameter("group"));
		  String actionname[]=request.getParameterValues("actionname");
		 int userid=Integer.parseInt(request.getParameter("username"));
		  
		  Group groups = null;
		  EPerson eperson = null;

		  if(button.equals("submit_folder"))
		  {
			  int cabinet_id=Integer.parseInt(cabinetid);
			  
			  if (cabinet_id != -1)
	            {
				  Community cabinet=Community.find(context,cabinet_id);
				  if(cabinet!=null)
				  {
					  community=cabinet.createSubcommunity();
				  }
				  
	            }
			  community.setMetadata("name",foldername);
			  
			  community.update();
			 
			if(userid!=0)
			{
				eperson=EPerson.find(context,userid);
			}
			if(group!=0)
			{
				 groups=Group.find(context,group);
			}
			/*if(userid!=0 && group!=0 && actionname.length!=0)
			{
				for(int i=0;i<actionname.length;i++)
				  {
					  ResourcePolicy myPolicy = ResourcePolicy.create(context);
					  log.info("Action name In FolderCreationServlet folder =================>"+actionname[i]);
					  myPolicy.setResource(community);
					  myPolicy.setAction(Integer.parseInt(actionname[i]));
					  myPolicy.setGroup(groups); 
				      myPolicy.setEPerson(eperson);
				      myPolicy.update();
				  }	
			}*/
		
			  if(actionname!=null && actionname.length>0)
			  {
			  for(int i=0;i<actionname.length;i++)
			  {
				  ResourcePolicy myPolicy = ResourcePolicy.create(context);
				  log.info("Action name In FolderCreationServlet folder =================>"+actionname[i]);
				  myPolicy.setResource(community);
				  myPolicy.setAction(Integer.parseInt(actionname[i]));
				  myPolicy.setGroup(groups); 
			      myPolicy.setEPerson(eperson);
			      myPolicy.update();
			  }
			  }
		       
		        context.complete();
			  request.setAttribute("folder.create",Boolean.TRUE);
			  String msg="";
			  //JSPManager.showJSP(request, response,"/tools/completefolder.jsp");
			    msg="Y";        	
	        	 response.sendRedirect(response.encodeRedirectURL(request
	                     .getContextPath()+ "/dspace-admin/create-folder?action=folder&message="+msg));
	        	 
			 /* response.sendRedirect(response.encodeRedirectURL(request
		               .getContextPath()
		               + "/handle/" + community.getHandle()));*/
		  }
		  else if(button.equals("submit_subfolder"))
		  {
			  Collection newCollection = null;
			  int folder_id=Integer.parseInt(foldername);
			  if(folder_id > -1)
			  {
				  Community c = Community.find(context, folder_id);
				  if(c==null)
				  {
					  log.warn(LogManager.getHeader(context, "integrity_error",
		                        UIUtil.getRequestLogInfo(request)));
		                JSPManager.showIntegrityError(request, response);

		                return;
				  }
				  newCollection = c.createCollection();
				  newCollection.setMetadata("name",subfoldername);
				  newCollection.update();
				  if(userid!=0)
					{
					  	eperson=EPerson.find(context,userid);
					}
					if(group!=0)
					{

						 groups=Group.find(context,group);
					}
					/*
					if(userid!=0 && group!=0 && actionname.length!=0)
					{
						 for(int i=0;i<actionname.length;i++)
						  {
							  ResourcePolicy myPolicy = ResourcePolicy.create(context);
							  log.info("Action name In FolderCreationServlet subfolder =================>"+actionname[i]);
							  myPolicy.setResource(newCollection);
							  myPolicy.setAction(Integer.parseInt(actionname[i]));
					
								  myPolicy.setGroup(groups);  
			
						      myPolicy.setEPerson(eperson);
						      myPolicy.update(); 
						  }
					}*/
					
				  if(actionname!=null && actionname.length>0)
				  {
				  for(int i=0;i<actionname.length;i++)
				  {
					  ResourcePolicy myPolicy = ResourcePolicy.create(context);
					  log.info("Action name In FolderCreationServlet subfolder =================>"+actionname[i]);
					  myPolicy.setResource(newCollection);
					  myPolicy.setAction(Integer.parseInt(actionname[i]));
			
						  myPolicy.setGroup(groups);  
	
				      myPolicy.setEPerson(eperson);
				      myPolicy.update(); 
				  }
				  } 
			       
			       
			  }
			  
			  context.complete();
			  
			  
			  request.setAttribute("subfolder.create", Boolean.TRUE);
			 
			  String msg="Y";        	
	        	 response.sendRedirect(response.encodeRedirectURL(request
	                     .getContextPath()+ "/dspace-admin/create-folder?action=subfolder&message="+msg));
	        	 
			  //JSPManager.showJSP(request, response,"/tools/completefolder.jsp");
			 /* response.sendRedirect(response.encodeRedirectURL(request
		               .getContextPath()
		               + "/handle/" + newCollection.getHandle()));*/
		  }
		
		  else if(button.equals("submit_cabinet"))
		  {
				 Community community1 = Community.find(context, UIUtil.getIntParameter(
			                request, "community_id"));
      		int parent_community_ID=Integer.parseInt(request.getParameter("parent_community_id"));
      	
      		if (parent_community_ID != -1)
              {
                  Community parent = Community.find(context, parent_community_ID);

                  if (parent != null)
                  {
                  	community1= parent.createSubcommunity();
                  }
                  request.setAttribute("parent_community_id", "-2");
                  log.info("folder");
              }
              else
              {
                 community1=Community.create(null, context);
                  log.info("cabinet");
              }
      	community1.setMetadata("name",foldername);
      	community1.update();
      	 if(userid!=0)
 		  {
 			  eperson=EPerson.find(context,userid);
 		  }
 		  if(group!=0)
 		  {
 			  groups=Group.find(context,group);
 		  }
 		  
 		 /* if(userid!=0 && group!=0 && actionname.length!=0)
 		  {
 			 for(int i=0;i<actionname.length;i++)
 	 		  {
 	 			  ResourcePolicy myPolicy = ResourcePolicy.create(context);
 	 			  log.info("Action name In EditCommunityServlet folder =================>"+actionname[i]);
 	 			  myPolicy.setResource(community1);
 	 			  myPolicy.setAction(Integer.parseInt(actionname[i]));
 	 		      myPolicy.setGroup(groups);
 	 		      myPolicy.setEPerson(eperson);
 	 		      myPolicy.update();
 	 		  }  
 		  }*/
 		  if(actionname!=null && actionname.length>0)
 		  {
 		  for(int i=0;i<actionname.length;i++)
 		  {
 			  ResourcePolicy myPolicy = ResourcePolicy.create(context);
 			  log.info("Action name In EditCommunityServlet folder =================>"+actionname[i]);
 			  myPolicy.setResource(community1);
 			  myPolicy.setAction(Integer.parseInt(actionname[i]));
 		      myPolicy.setGroup(groups);
 		      myPolicy.setEPerson(eperson);
 		      myPolicy.update();
 		  }
 		  }
 	        context.complete();
 	     response.sendRedirect(response.encodeRedirectURL(request
               .getContextPath()
               + "/handle/" + community1.getHandle()));
      	
		  }
		  
		  else if(button.equals("submit_default_cancel"))
		  {
			  int parent_community_ID=Integer.parseInt(request.getParameter("parent_community_id"));
			  Community com=Community.find(context,parent_community_ID);
			  if(com!=null)
			  {
				  response.sendRedirect(response.encodeRedirectURL(request
	                        .getContextPath()
	                        + "/handle/" +com.getHandle()));
			  }
			  else
			  {
				  response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+ "/dspace-admin/"));
			  }
		  }
		  else if(button.equals("submit_cancel"))
		  {
			  response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+ "/dspace-admin/"));
		  }
	    }
}
