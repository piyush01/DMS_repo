package org.dspace.workflowprocess;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.util.CommonDateFormat;
import org.dspace.util.SequenceGenerateManager;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.TaskMasterBean;
import org.dspace.workflowmanager.WorkflowManager;

public class AdhocWorkflowManager implements Serializable{
	private static Logger log = Logger.getLogger(AdhocWorkflowManager.class);
	private TaskMasterBean taskMasterBean=new TaskMasterBean();
	private WorkflowManager workflowManager=new WorkflowManager();
	private WorkflowProcessManager workflowProcessManager=new WorkflowProcessManager();
	private WorkflowProcessBean workflowProcessBean=new WorkflowProcessBean();
	private TaskManager taskManager=new TaskManager();
	
	
	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public WorkflowProcessBean getWorkflowProcessBean() {
		return workflowProcessBean;
	}

	public void setWorkflowProcessBean(WorkflowProcessBean workflowProcessBean) {
		this.workflowProcessBean = workflowProcessBean;
	}

	public WorkflowProcessManager getWorkflowProcessManager() {
		return workflowProcessManager;
	}

	public void setWorkflowProcessManager(
			WorkflowProcessManager workflowProcessManager) {
		this.workflowProcessManager = workflowProcessManager;
	}

	public TaskMasterBean getTaskMasterBean() {
		return taskMasterBean;
	}

	public void setTaskMasterBean(TaskMasterBean taskMasterBean) {
		this.taskMasterBean = taskMasterBean;
	}

	public WorkflowManager getWorkflowManager() {
		return workflowManager;
	}

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1212656565L;
	
	public Integer saveAdhocTask(Context context,HttpServletRequest request) throws ParseException, SQLException
	{
		
		 Integer saveId=0;
		 
		 saveId=taskManager.saveTask(context, request);
		
		 /*  String permission[]=	request.getParameterValues("task_permission_id");
		   String requirement[]=	request.getParameterValues("task_requirement_id");
		   String user_assign[]=	request.getParameterValues("assign_user_id");   
		   String task_permission="";
		   String task_req="1";
		   String user_assign_id="";
		  
		   EPerson e=context.getCurrentUser();
		   Integer userid=e.getID();
		   String deadline_time=request.getParameter("deadline_time");
		   deadline_time=deadline_time+" "+request.getParameter("timemode");
		 //  assign_user_id
		   
			  for(int i=0; i<permission.length; i++){
				  if(task_permission!=null && task_permission.equals(""))
				  {
					  task_permission=task_permission+permission[i];
				  }
				  else{
				  task_permission=task_permission+"#"+permission[i];
				  }
			  }
			  
			  log.info("task_permission:------------------------"+task_permission);
			  for(int i=0; i<requirement.length; i++){
				  if(task_req!=null && task_req.equals("")){
					  task_req=task_req+requirement[i];
				  }else{
					    task_req=task_req+"#"+requirement[i];
				  }
			  }
			  log.info("task_req:------------------------"+task_req);  
			 	
			taskMasterBean.setWorkflow_id(Integer.valueOf(request.getParameter("workflow_id")==null ? "0" :request.getParameter("workflow_id")));
		    taskMasterBean.setTask_name(request.getParameter("task_name"));
		    taskMasterBean.setPriorty(request.getParameter("priorty"));
		    taskMasterBean.setDeadline_day(new java.sql.Date(CommonDateFormat.getDateyyyymmddAsString(request.getParameter("deadline_day")).getTime()));
		    taskMasterBean.setDeadline_time(deadline_time);
		    taskMasterBean.setTask_instructions(request.getParameter("task_instruction"));
		    taskMasterBean.setSupervisor_id(Integer.valueOf(request.getParameter("supervisor_id")== null ? "0" :request.getParameter("supervisor_id")));
		    taskMasterBean.setTask_permission_id(task_permission.trim());
		    taskMasterBean.setTask_requirment_id(task_req.trim());
		    taskMasterBean.setStatus("P");
		    taskMasterBean.setTask_user_id(userid);
		    taskMasterBean.setUpdate_date(new Date());
		   // int task_id=getGeneratedNewId(context);	
		    int task_id=SequenceGenerateManager.getGeneratedId(context,"adhock_task_seq");
		    taskMasterBean.setTask_id(task_id);
		    saveId=saveAdTask(context,taskMasterBean);
		    
		    if(saveId>0)
		    {
			    for(int i=0; i<user_assign.length; i++)
	    	 	{
			     saveId=saveAdhocTaskRole(context,task_id,Integer.valueOf(user_assign[i]),taskMasterBean.getSupervisor_id(),taskMasterBean);
			     log.info("save adhoc task id:---------------------------"+saveId);
	    	 	}
		    }*/
		    
		    if(saveId>0){
		    	saveId=	saveWorkflowDocument(context,request,saveId);
		    	log.info("save workflow document:------------------------->>>"+saveId);
		    }
		    
	return saveId;
}

	public Integer saveAdhocTaskRole(Context context,Integer task_id,Integer user_id,Integer s_id,TaskMasterBean taskMasterBean) throws SQLException{
		PreparedStatement ps=null;
		
         Integer saveId=0;
		//int id=getGeneratedRolenewId(context);
		int id=SequenceGenerateManager.getGeneratedId(context,"adhock_task_role_seq");
		try{
			ps=context.getDBConnection().prepareStatement(" insert into adhoc_task_role (task_role_id,task_id,task_owner_id,status,update_date,supervisor_id,deadline_day) values(?, ?, ?, ?, ?, ?, ?) ");
	        ps.setInt(1, id);
	        ps.setInt(2, task_id);
	        ps.setInt(3, user_id);
	        ps.setString(4, "P");
	        ps.setDate(5, new java.sql.Date(new Date().getTime()));  
	        ps.setInt(6, s_id);
	        ps.setDate(7,  new java.sql.Date(taskMasterBean.getDeadline_day().getTime())); 
	        saveId=ps.executeUpdate();
	        
		}catch(SQLException e){
			log.info("Error in save adhoc task role"+e);
		}	
			finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException s) {
						log.error("SQL QueryTable close Error - ", s);
						throw s;
					}
				}
			}
		return saveId;
	}
	
	public Integer saveAdTask(Context context,TaskMasterBean taskMasterBean) throws ParseException
	{
		   PreparedStatement ps=null;
		   Integer saveId=0;
		   
			try{
				ps=context.getDBConnection().prepareStatement("insert into adhock_task (adhock_task_id,workflow_id,task_name,task_owner_id,"
						+ " priorty,task_instruction,deadline_day,deadline_time,task_permission_id,task_requirements_id,status,update_date,supervisor_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		        ps.setInt(1, taskMasterBean.getTask_id());
		        ps.setInt(2, taskMasterBean.getWorkflow_id());
		        ps.setString(3, taskMasterBean.getTask_name());
		        ps.setInt(4,taskMasterBean.getTask_user_id());
		        ps.setString(5, taskMasterBean.getPriorty());
		        ps.setString(6, taskMasterBean.getTask_instructions());
		        ps.setDate(7, new java.sql.Date(taskMasterBean.getDeadline_day().getTime()));
		        ps.setString(8, taskMasterBean.getDeadline_time());
		        ps.setString(9, taskMasterBean.getTask_permission_id());
		        ps.setString(10, taskMasterBean.getTask_requirment_id());
		        ps.setString(11, taskMasterBean.getStatus());
		        ps.setDate(12, new java.sql.Date(taskMasterBean.getUpdate_date().getTime()));
		        ps.setInt(13, taskMasterBean.getSupervisor_id());
		        saveId=ps.executeUpdate();
		        log.info("task save row id:------------------------"+saveId);
		        
			}catch(SQLException e)
			{
				log.info("Error in save task master"+e);
			}	
				finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
						}
					}
				}
			
			return saveId;
}
	
	public Integer saveWorkflowDocument(Context context, HttpServletRequest request,Integer ad_task_id)
			throws SQLException {
		int pId = 0, rowId = 0;
		
		Integer workflow_id = Integer.valueOf(request.getParameter("workflowId"));
		String[] file_descriptionArr=request.getParameterValues("file_description");
		Integer item_id = Integer.valueOf(request.getParameter("item_id"));
		String file_description="";
		String handle = request.getParameter("handle");
		EPerson e = context.getCurrentUser();
		Integer eid = e.getID();
		Integer stepid = 0;
		Integer task_id = 0;
		Date deadline_date = new Date();
		int count=0;
	//	pId = workflowProcessManager.getGenerateProcessId(context);
		pId=SequenceGenerateManager.getGeneratedId(context, "workprocess_role_seq");
		rowId = workflowProcessManager.saveWorkprocessrole(context, request, pId, stepid, ad_task_id,deadline_date);
		
			if(file_descriptionArr!=null && file_descriptionArr.length>0){
		for (int i = 0; i < file_descriptionArr.length; i++) {	
			log.info("i value:--"+i);
			file_description=file_descriptionArr[i];		
		 if(file_description!=null && !file_description.equals(""))
		 {
			 String[] file_description_Arr=file_description.split("#"); 
		 for (int s1 = 0; s1 < file_description_Arr.length; s1++)
		 {
			 
			 if(count==0 && file_description_Arr[2]!=null && !file_description_Arr[2].equals(""))
			 { 
		       rowId = saveWorkflowprocessdocument(context,file_description_Arr[0], file_description_Arr[3], file_description_Arr[2],handle, workflow_id, pId, item_id, stepid, ad_task_id,Integer.parseInt(file_description_Arr[1]));
			 }
			 count++;
		 }
		 
		 count=0;
	   }
		}
	}		
		return rowId;
	}
	

public Integer saveWorkflowprocessdocument(Context context,String file_no,String file_link,String file_name,String handle,Integer workflow_id,Integer work_process_id,Integer item_id,Integer step_id,Integer task_id,Integer bitstreamId) throws SQLException{
	int nextId=0,rowId=0;
	 PreparedStatement preparedStatement=null;
	 ResultSet rs = null;
	if(handle!=null && !handle.equals("")){
		 try {
			//nextId=workflowManager.getSendWorkflowNewId(context);
			nextId=SequenceGenerateManager.getGeneratedId(context, "workflow_process_seq");
	        preparedStatement=context.getDBConnection().prepareStatement(" insert into workflow_process(process_id,workflow_id,document_id,handle,update_date,status,file_path,document_name,work_process_id,item_id,workflow_step_id,task_id,bitstream_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " );
	        preparedStatement.setInt(1,nextId);  
	        preparedStatement.setInt(2,workflow_id);
	        preparedStatement.setString(3,file_no);
	        preparedStatement.setString(4,handle);
	        preparedStatement.setDate(5, new java.sql.Date(new Date().getTime()));
	        preparedStatement.setString(6,"P");
	        preparedStatement.setString(7, file_link);
	        preparedStatement.setString(8, file_name);
	        preparedStatement.setInt(9, work_process_id);
	        preparedStatement.setInt(10, item_id);
	        preparedStatement.setInt(11, step_id);
	        preparedStatement.setInt(12, task_id);
	        preparedStatement.setInt(13, bitstreamId);
	        rowId=preparedStatement.executeUpdate();
		 } catch (Exception ex) {
			 log.info("Error in add work flow:-------------"+ex.getMessage());
		}
		 finally
	     {
			 if (rs != null)
	         {
	             try { rs.close(); } catch (SQLException sqle) { }
	         }
			 if (preparedStatement != null)
	         {
	             try { preparedStatement.close(); } catch (SQLException sqle) {
	            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                 throw sqle;
	             }
	         }
	     }
	 }
		 
return rowId;
}

public List<TaskMasterBean> getAdhocTaskList(Context context,Integer user_id) throws SQLException{
	List<TaskMasterBean> tasklist=new ArrayList<TaskMasterBean>();;
	PreparedStatement ps=null;
	ResultSet rs=null;

	try{
		ps=context.getDBConnection().prepareStatement(" select adt.adhock_task_id,adt.workflow_id,adt.task_name,adt.priorty,"
				+ " adt.task_instruction,adt.deadline_day,adt.deadline_time,adt.status,ar.task_owner_id,ar.supervisor_id "
         +" from adhock_task adt right join adhoc_task_role ar on adt.adhock_task_id=ar.task_id where ar.task_owner_id="+user_id+" ");
		
		rs=ps.executeQuery();
		
				while(rs.next())
				{
					log.info("while execute in geeting list adhoc:------------------->>"+rs.getInt(1));
					taskMasterBean=new TaskMasterBean();
					taskMasterBean.setTask_id(rs.getInt(1));
					taskMasterBean.setWorkflow_id(rs.getInt(2));
					taskMasterBean.setTask_name(rs.getString(3));
					taskMasterBean.setPriorty(rs.getString(4));
					taskMasterBean.setTask_instructions(rs.getString(5));
					taskMasterBean.setDeadline_day(rs.getDate(6));
					taskMasterBean.setDeadline_time(rs.getString(7));
					taskMasterBean.setStatus(rs.getString(8));
					taskMasterBean.setTask_owner_id(rs.getString(9));
					taskMasterBean.setSupervisor_id(rs.getInt(10));
					tasklist.add(taskMasterBean);
			   }
		
	}catch(SQLException e){
		log.info("Error in getting all task=============>>>>>>"+e);
	}
	finally {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException s) {
				log.error("SQL QueryTable close Error - ", s);
				throw s;
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException s) {
				log.error("SQL QueryTable close Error - ", s);
				throw s;
			}
		}
		
	}
	
	log.info("adhoc task list:------------->"+tasklist.size());
	
	return tasklist;
}

public List<WorkflowProcessBean> getAdhocDocumentList(Context context,Integer workflowid,Integer task_id) throws SQLException{
	List<WorkflowProcessBean> docList=new ArrayList<>();
	PreparedStatement ps=null;
	ResultSet rs=null;
	try{
		ps=context.getDBConnection().prepareStatement(" select document_name,file_path from workflow_process where workflow_id="+workflowid+" and task_id="+task_id+" ");		
		rs=ps.executeQuery();
		while(rs.next())
		{
			workflowProcessBean=new WorkflowProcessBean();
			workflowProcessBean.setDocument_name(rs.getString(1));
			workflowProcessBean.setFile_path(rs.getString(2));
			docList.add(workflowProcessBean);
		}
	}catch(SQLException sqe){
		log.info("Error in getting supervisor exits or not:--------------"+sqe);
	}
	finally
    {
	 if (rs != null)
        {
            try { rs.close(); } catch (SQLException sqle) {
           	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
                throw sqle;
            }
        }
		if (ps != null)
        {
            try { ps.close(); } catch (SQLException sqle) {
           	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
                throw sqle;
            }
        }
    }
	
	return docList;
}
	

}
