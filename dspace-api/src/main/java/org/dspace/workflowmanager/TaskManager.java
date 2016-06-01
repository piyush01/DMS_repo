package org.dspace.workflowmanager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dspace.core.Context;

import org.dspace.util.CommonDateFormat;
import org.dspace.util.SequenceGenerateManager;

public class TaskManager implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1324435345L;
private static Logger log=Logger.getLogger(TaskManager.class);
private TaskMasterBean taskMasterBean=new TaskMasterBean();


public TaskManager() {

	}


public TaskMasterBean getTaskMasterBean() {
	return taskMasterBean;
}


public void setTaskMasterBean(TaskMasterBean taskMasterBean) {
	this.taskMasterBean = taskMasterBean;
}




 public Integer saveTask(Context context,HttpServletRequest request) throws SQLException, ParseException
 {
   
   PreparedStatement ps=null;
   String permission[]=	request.getParameterValues("task_permission_id");
   String requirement[]=	request.getParameterValues("task_requirement_id");
   String user_assign[]=	request.getParameterValues("assign_user_id");   
   String task_permission="";
   String task_req="";
   Integer saveId=0;
   String deadline_time=request.getParameter("deadline_time");
   deadline_time=deadline_time+" "+request.getParameter("timemode");
   Integer workflow_id=Integer.valueOf(request.getParameter("workflowId"));
   String task_start="";
   
   if(permission!=null && permission.length>0){
	  for(int i=0; i<permission.length; i++){
		  if(task_permission!=null && task_permission.equals(""))
		  {
			  task_permission=task_permission+permission[i];
		  }
		  else{
		  task_permission=task_permission+"#"+permission[i];
		  }
	  }
   }
	  log.info("task_permission:------------------------"+task_permission);
	  if(requirement!=null && requirement.length>0){
		  for(int i=0; i<requirement.length; i++){
			  if(task_req!=null && task_req.equals("")){
				  task_req=task_req+requirement[i];
			  }else{
				    task_req=task_req+"#"+requirement[i];
			  }
		  }
	  }
	  
	  log.info("task_req:------------------------"+task_req);  
	  
	int id=SequenceGenerateManager.getGeneratedId(context,"workflow_step_task");
	int taskno=getTotalTask(context,Integer.valueOf(request.getParameter("step_id")== null ? "0" :request.getParameter("step_id")));
	int tasknoByworkflow=getTotalTaskWorkflowId(context,Integer.valueOf(request.getParameter("workflowId")== null ? "0" :request.getParameter("workflowId")));
	
	log.info("taskno:---------------tasknoByworkflow--------"+taskno+"-----"+tasknoByworkflow);  
	
	 if(taskno==1 && tasknoByworkflow==1){
		 task_start="open";
	 }else{
		 task_start="close";
	 }
	 
	 if(workflow_id==1){
		 taskno=1;
		 task_start="open";
	 }
	 	
	taskMasterBean.setTask_id(id);
	taskMasterBean.setWorkflow_id(Integer.valueOf(request.getParameter("workflowId")==null ? "0" :request.getParameter("workflowId")));
    taskMasterBean.setStep_id(Integer.valueOf(request.getParameter("step_id")==null ? "0" :request.getParameter("step_id")));
    taskMasterBean.setTask_owner_id("0");
    taskMasterBean.setStep_no(Integer.valueOf(request.getParameter("step_no")== null ? "0" :request.getParameter("step_no")));
    taskMasterBean.setTask_name(request.getParameter("task_name"));
    taskMasterBean.setTask_rule_id(Integer.valueOf(request.getParameter("task_rule_id")==null ? "0" :request.getParameter("task_rule_id")));
    taskMasterBean.setPriorty(request.getParameter("priorty"));
    taskMasterBean.setDeadline_day(new java.sql.Date(CommonDateFormat.getDateyyyymmddAsString(request.getParameter("deadline_day")).getTime()));
    taskMasterBean.setDeadline_time(deadline_time);
    taskMasterBean.setTask_instructions(request.getParameter("task_instruction").trim());
    taskMasterBean.setSupervisor_id(Integer.valueOf(request.getParameter("supervisor_id")== null ? "0" :request.getParameter("supervisor_id")));
    taskMasterBean.setTask_permission_id(task_permission.trim());
    taskMasterBean.setTask_requirment_id(task_req.trim());
    taskMasterBean.setStatus("P");
    taskMasterBean.setUpdate_date(new Date());
    taskMasterBean.setTask_no(taskno);
    
	try{
		ps=context.getDBConnection().prepareStatement("insert into task_master (task_id,workflow_id,step_id,task_owner_id,step_no,task_name,task_rule_id,priorty,deadline_day,deadline_time,task_start,task_instruction,supervisors_id,task_permission_id,task_requirements_id,status,update_date,task_no) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
        ps.setInt(1, taskMasterBean.getTask_id());
        ps.setInt(2, taskMasterBean.getWorkflow_id());
        ps.setInt(3, taskMasterBean.getStep_id());
        ps.setString(4, taskMasterBean.getTask_owner_id());
        ps.setInt(5, taskMasterBean.getStep_no());
        ps.setString(6, taskMasterBean.getTask_name());
        ps.setInt(7, taskMasterBean.getTask_rule_id());
        ps.setString(8, taskMasterBean.getPriorty());
        ps.setDate(9, new java.sql.Date(taskMasterBean.getDeadline_day().getTime()));
        ps.setString(10, taskMasterBean.getDeadline_time());
        ps.setString(11, task_start);
        ps.setString(12, taskMasterBean.getTask_instructions());
        ps.setInt(13, taskMasterBean.getSupervisor_id());
        ps.setString(14, taskMasterBean.getTask_permission_id());
        ps.setString(15, taskMasterBean.getTask_requirment_id());
        ps.setString(16, taskMasterBean.getStatus());
        ps.setDate(17, new java.sql.Date(taskMasterBean.getUpdate_date().getTime()));   
        ps.setInt(18, taskMasterBean.getTask_no());
        saveId=ps.executeUpdate();
        log.info("task save row id:------------------------"+saveId);
        
        if(saveId>0){
        	 for(int i=0; i<user_assign.length; i++)
        	 	{
        		 saveId=saveTaskRole(context,id,Integer.valueOf(user_assign[i]),taskMasterBean.getSupervisor_id(),taskMasterBean);
        		   log.info("tsave Task Role id:------------------------"+saveId);
        	 	}
        	 
        	 if(saveId>0){
        		 saveId=id;}
        }
        
	}catch(SQLException e){
		log.info("Error in save task master"+e);
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
 
 public Integer updateTask(Context context,HttpServletRequest request) throws SQLException, ParseException{
	 PreparedStatement ps=null;
	   String permission[]=	request.getParameterValues("task_permission_id");
	   String requirement[]=	request.getParameterValues("task_requirement_id");
	   String user_assign[]=	request.getParameterValues("assign_user_id");   
	   String task_permission="";
	   String task_req="1";
	   Integer saveId=0;
	   String deadline_time=request.getParameter("deadline_time");
	   deadline_time=deadline_time+" "+request.getParameter("timemode");
	   
	   Integer task_id=Integer.valueOf(request.getParameter("task_id")==null ? "0" :request.getParameter("task_id"));
	   
	   log.info("task_id:------------------------"+task_id);
	   if(permission!=null && permission.length>0){
		  for(int i=0; i<permission.length; i++){
			  if(task_permission!=null && task_permission.equals(""))
			  {
				  task_permission=task_permission+permission[i];
			  }
			  else
			  {
			  task_permission=task_permission+"#"+permission[i];
			  }
		  }
	   }
		  log.info("task_permission:------------------------"+request.getParameter("task_rule_id"));
		  if(requirement!=null && requirement.length>0){
		  for(int i=0; i<requirement.length; i++){
			  if(task_req!=null && task_req.equals("")){
				  task_req=task_req+requirement[i];
			  }else{
				    task_req=task_req+"#"+requirement[i];
			  }
		  }
		  }
		    taskMasterBean.setTask_id(task_id);
			taskMasterBean.setWorkflow_id(Integer.valueOf(request.getParameter("workflowId")==null ? "0" :request.getParameter("workflowId")));
		    taskMasterBean.setStep_id(Integer.valueOf(request.getParameter("step_id")==null ? "0" :request.getParameter("step_id")));
		    taskMasterBean.setTask_owner_id("0");
		    taskMasterBean.setStep_no(Integer.valueOf(request.getParameter("step_no")== null ? "0" :request.getParameter("step_no")));
		    taskMasterBean.setTask_name(request.getParameter("task_name"));
		    taskMasterBean.setTask_rule_id(Integer.valueOf(request.getParameter("task_rule_id")==null ? "0" :request.getParameter("task_rule_id")));
		    taskMasterBean.setPriorty(request.getParameter("priorty"));
		    taskMasterBean.setDeadline_day(new java.sql.Date(CommonDateFormat.getDateyyyymmddAsString(request.getParameter("deadline_day")).getTime()));
		    taskMasterBean.setDeadline_time(deadline_time);
		    taskMasterBean.setTask_instructions(request.getParameter("task_instruction").trim());
		    taskMasterBean.setSupervisor_id(Integer.valueOf(request.getParameter("supervisor_id")== null ? "0" :request.getParameter("supervisor_id")));
		    taskMasterBean.setTask_permission_id(task_permission.trim());
		    taskMasterBean.setTask_requirment_id(task_req.trim());
		    taskMasterBean.setStatus("P");
		    taskMasterBean.setUpdate_date(new Date());
		    
			try{
				ps=context.getDBConnection().prepareStatement(" update task_master set workflow_id=?,step_id=?,step_no=?,task_name=?,"
						+ " task_rule_id=?, priorty=?, deadline_day=?, deadline_time=?, task_instruction=?, supervisors_id=?, task_permission_id=?,"
						+ " task_requirements_id=?, update_date=? where task_id=? ");
				
				 log.info("task_id:------------------------"+taskMasterBean.getTask_rule_id());
				 
		        ps.setInt(1, taskMasterBean.getWorkflow_id());
		        ps.setInt(2, taskMasterBean.getStep_id());
		        ps.setInt(3, taskMasterBean.getStep_no());
		        ps.setString(4, taskMasterBean.getTask_name());
		        ps.setInt(5, taskMasterBean.getTask_rule_id());
		        ps.setString(6, taskMasterBean.getPriorty());
		        ps.setDate(7, new java.sql.Date(CommonDateFormat.getDateyyyymmddAsString(request.getParameter("deadline_day")).getTime()));
		        ps.setString(8, taskMasterBean.getDeadline_time());
		        ps.setString(9, taskMasterBean.getTask_instructions());
		        ps.setInt(10, taskMasterBean.getSupervisor_id());
		        ps.setString(11, taskMasterBean.getTask_permission_id());
		        ps.setString(12, taskMasterBean.getTask_requirment_id());
		        ps.setDate(13, new java.sql.Date(taskMasterBean.getUpdate_date().getTime()));
		        ps.setInt(14, taskMasterBean.getTask_id());
		        
		        saveId=ps.executeUpdate();
		        
		        log.info("task save row id:------------------------"+saveId);
		        
		        /*if(saveId>0){
		        	 for(int i=0; i<user_assign.length; i++)
		        	 	{
		        		// saveId=saveTaskRole(context,id,Integer.valueOf(user_assign[i]),taskMasterBean.getSupervisor_id(),taskMasterBean);
		        		   log.info("tsave Task Role id:------------------------"+saveId);
		        	 	}
		        	 
		        	 if(saveId>0){
		        		 saveId=id;}
		        }*/
		        
			}catch(SQLException e){
				log.info("Error in update task master"+e);
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
 
 public Integer deleteTask(Context context,Integer task_id) throws SQLException, ParseException{
	 PreparedStatement ps=null;
	   
	   Integer saveId=0;
	     
	   log.info("task_id:------------------------"+task_id);
	     
			try{
				ps=context.getDBConnection().prepareStatement(" update task_master set is_deleted=?,update_date=? where task_id=? ");
				 log.info("task_id:------------------------"+taskMasterBean.getTask_rule_id());
		        ps.setString(1, "YES");		        
		        ps.setDate(2,new java.sql.Date(new Date().getTime()));
		        ps.setInt(3, task_id);
		        saveId=ps.executeUpdate();
		        log.info("task save row id:------------------------"+saveId);
		        
		        if(saveId>0){
		        	saveId=deleteTaskRole(context,task_id);
		        }
			}catch(SQLException e){
				log.info("Error in update task master"+e);
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
 
 public Integer deleteTaskRole(Context context,Integer task_id) throws SQLException, ParseException{
	 PreparedStatement ps=null;
	   Integer saveId=0;
	   log.info("task_id:------------------------"+task_id);
	     
			try{
				ps=context.getDBConnection().prepareStatement(" update task_role set is_deleted=?,update_date=? where task_id=? ");
				 log.info("task_id:------------------------"+taskMasterBean.getTask_rule_id());
		        ps.setString(1, "YES");		        
		        ps.setDate(2,new java.sql.Date(new Date().getTime()));
		        ps.setInt(3, task_id);
		        saveId=ps.executeUpdate();
		        log.info("task save row id:------------------------"+saveId);
			}catch(SQLException e){
				log.info("Error in delete task role"+e);
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
 
 public Integer saveTaskRole(Context context,Integer task_id,Integer user_id,Integer s_id,TaskMasterBean taskMasterBean) throws SQLException{
		PreparedStatement ps=null;
		
         Integer saveId=0;
		int id=SequenceGenerateManager.getGeneratedId(context,"task_role_seq");
		try{
			ps=context.getDBConnection().prepareStatement(" insert into task_role (task_role_id,task_id,task_owner_id,status,update_date,supervisor_id,deadline_day) values(?, ?, ?, ?, ?, ?, ?) ");
	        ps.setInt(1, id);
	        ps.setInt(2, task_id);
	        ps.setInt(3, user_id);
	        ps.setString(4, "P");
	        ps.setDate(5, new java.sql.Date(new Date().getTime()));  
	        ps.setInt(6, s_id);
	        ps.setDate(7,  new java.sql.Date(taskMasterBean.getDeadline_day().getTime())); 
	        saveId=ps.executeUpdate();
	        
		}catch(SQLException e){
			log.info("Error in save task master"+e);
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

	public Integer getTotalTask(Context context,Integer stepid) throws SQLException{
		Statement statment=null;
		ResultSet rs=null;
		Integer totalNo=0;
		try{
				statment=context.getDBConnection().createStatement();
				rs=statment.executeQuery(" select count(*) as tot from task_master where step_id="+stepid+"  ");
				while(rs.next()){
					totalNo=rs.getInt(1);	
				}
				
				if(totalNo>0){
					totalNo=totalNo+1;
				}else{
					totalNo=1;
				}
		}
				catch(SQLException ex){
					log.info("Error in get total task no:=========>"+ex);
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
			if (statment != null)
	         {
	             try { statment.close(); } catch (SQLException sqle) {
	            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                 throw sqle;
	             }
	         }
	     }
		return totalNo;
	}
	
	public Integer getTotalTaskWorkflowId(Context context,Integer workflow_id) throws SQLException{
		Statement statment=null;
		ResultSet rs=null;
		Integer totalNo=0;
		try{
				statment=context.getDBConnection().createStatement();
				rs=statment.executeQuery(" select count(*) as tot from task_master where workflow_id="+workflow_id+"  ");
				while(rs.next()){
					totalNo=rs.getInt(1);	
				}
				
				if(totalNo>0){
					totalNo=totalNo+1;
				}else{
					totalNo=1;
				}
		}
				catch(SQLException ex){
					log.info("Error in get total task no:=========>"+ex);
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
			if (statment != null)
	         {
	             try { statment.close(); } catch (SQLException sqle) {
	            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                 throw sqle;
	             }
	         }
	     }
		return totalNo;
	}


public Integer getStepNo(Context context,Integer stepid) throws SQLException{
	Statement statment=null;
	ResultSet rs=null;
	Integer stepNo=0;
	try{
			statment=context.getDBConnection().createStatement();
			rs=statment.executeQuery(" select step_no from step_master where step_id="+stepid+"  ");
			while(rs.next()){
				stepNo=rs.getInt(1);
			}
	}
			catch(SQLException ex){
				log.info("Error in get total task no:=========>"+ex);
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
		if (statment != null)
         {
             try { statment.close(); } catch (SQLException sqle) {
            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
                 throw sqle;
             }
         }
     }
	
	return stepNo;
}

public List<TaskMasterBean> getUserById(Context context,Integer userId) throws SQLException{
	List<TaskMasterBean> list=new ArrayList<TaskMasterBean>();;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try{
		ps=context.getDBConnection().prepareStatement("select e.eperson_id,m.text_value as first_name,ln.text_value as last_name from eperson"
		+ " e left join metadatavalue m on m.resource_id = e.eperson_id "+ 
       " left join metadatavalue ln on ln.resource_id=e.eperson_id where m.metadata_field_id =124  and ln.metadata_field_id=125 and e.eperson_id="+userId+" order by m.text_value ");
		rs=ps.executeQuery();
			while(rs.next()){
				taskMasterBean=new TaskMasterBean();
				log.info("user id by db:----------"+rs.getInt(1));
				log.info("user id by function:----------"+userId);
				taskMasterBean.setUser_id(rs.getInt(1));
				taskMasterBean.setUser_name(rs.getString(2)+" "+rs.getString(3));
				list.add(taskMasterBean);
			}
	}catch(SQLException e){
		log.info("Error in getting all user"+e);
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
	
	return list;
}

public List<TaskMasterBean> getUser(Context context) throws SQLException{
	List<TaskMasterBean> list=new ArrayList<TaskMasterBean>();;
	PreparedStatement ps=null;
	ResultSet rs=null;
	String name="";
	try{
		ps=context.getDBConnection().prepareStatement("select e.eperson_id,m.text_value as first_name,ln.text_value as last_name from eperson"
		+ " e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124 "+ 
       " left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 order by m.text_value ");
		rs=ps.executeQuery();
			while(rs.next()){
				taskMasterBean=new TaskMasterBean();
				taskMasterBean.setUser_id(rs.getInt(1));
				taskMasterBean.setUser_name(rs.getString(2)+" "+rs.getString(3));
				list.add(taskMasterBean);
			}
	}catch(SQLException e){
		log.info("Error in getting all user"+e);
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
	
	return list;
}

public List<TaskMasterBean> getSupervisorUser(Context context,Integer user_id) throws SQLException{
	List<TaskMasterBean> list=new ArrayList<TaskMasterBean>();;
	PreparedStatement ps=null;
	ResultSet rs=null;
	String name="";
	try{
		ps=context.getDBConnection().prepareStatement("select e.eperson_id,m.text_value as first_name,ln.text_value as last_name from eperson"
		+ " e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124 "+ 
       " left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 where e.eperson_id!="+user_id+" order by m.text_value ");
		rs=ps.executeQuery();
			while(rs.next()){
				taskMasterBean=new TaskMasterBean();
				taskMasterBean.setUser_id(rs.getInt(1));
				taskMasterBean.setUser_name(rs.getString(2)+" "+rs.getString(3));
				list.add(taskMasterBean);
			}
	}catch(SQLException e){
		log.info("Error in getting all user"+e);
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
	
	return list;
}


public List getAllTaskList(Context context) throws SQLException{
	List<TaskMasterBean> list=new ArrayList<TaskMasterBean>();;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try{
		ps=context.getDBConnection().prepareStatement("select t.task_id,t.task_name,t.priorty,t.deadline_day,t.deadline_time,t.task_rule_id,"
				+ "t.task_owner_id,s.step_name from task_master t left join step_master s on t.step_id=s.step_id order by t.task_id");
		rs=ps.executeQuery();
		if(rs.next()){
			while(rs.next()){
				TaskMasterBean taskMasterBean=new TaskMasterBean();
				taskMasterBean.setTask_id(rs.getInt(1));
				taskMasterBean.setTask_name(rs.getString(2));
				taskMasterBean.setPriorty(rs.getString(3));
				taskMasterBean.setDeadline_day(rs.getDate(4));
				taskMasterBean.setDeadline_time(rs.getString(5));
				taskMasterBean.setTask_rule_id(rs.getInt(6));
				taskMasterBean.setTask_owner_id(rs.getString(7));
				taskMasterBean.setWorkflow_step(rs.getString(8));
				list.add(taskMasterBean);
			}
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
	
	return list;
}


public Set<TaskMasterBean> getAllTaskList(Context context,Integer workflow_id,Integer step_id) throws SQLException{
	Set<TaskMasterBean> tasklist=new TreeSet<TaskMasterBean>();;
	PreparedStatement ps=null;
	ResultSet rs=null;
	try{
		
	    ps=context.getDBConnection().prepareStatement("select t.task_id,s.step_id,t.task_name,t.status,t.priorty,t.deadline_day,t.deadline_time,t.task_rule_id,"
	    +" tr.task_owner_id,s.step_name,t.supervisors_id,t.task_instruction from task_master t left join step_master s on s.step_id=t.step_id "
	    + " left join workflow_master wm on wm.workflow_id=t.workflow_id left join task_role tr on tr.task_id=t.task_id where"
	    + " t.workflow_id="+workflow_id+" and s.step_id="+step_id+" and t.is_deleted='NO' order by t.task_id ");
	    		  rs=ps.executeQuery();
	    		  
				while(rs.next())
				{
				taskMasterBean=new TaskMasterBean();
				taskMasterBean.setTask_id(rs.getInt(1));	
				taskMasterBean.setStep_id(rs.getInt(2));
				taskMasterBean.setTask_name(rs.getString(3));		
				taskMasterBean.setStatus(rs.getString(4));
				taskMasterBean.setPriorty(rs.getString(5));
				taskMasterBean.setDeadline_day(rs.getDate(6));
				taskMasterBean.setDeadline_time(rs.getString(7));				
				taskMasterBean.setTask_rule_id(rs.getInt(8));
				taskMasterBean.setTask_owner_id(rs.getString(9));				
				taskMasterBean.setSupervisor_id(rs.getInt(11));
				taskMasterBean.setTask_instructions(rs.getString(12));
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
	System.out.println(tasklist.size());
	return tasklist;
}


public TaskMasterBean getTaskById(Context context,Integer task_id) throws SQLException{
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	List<String> reqList=new ArrayList<>();
	List<String> perList=new ArrayList<>();
	try{
		log.info("task_id---------------------->"+task_id);
		
		ps=context.getDBConnection().prepareStatement("select task_id ,workflow_id,step_id,step_no ,task_name,task_rule_id ,"
				+ " priorty,deadline_time ,task_instruction,supervisors_id ,task_permission_id,task_requirements_id,add_date,"
				+ " deadline_day from task_master where task_id="+task_id+" ");
				rs=ps.executeQuery();
				
				while(rs.next())
				{		
					log.info("task"+rs.getString(5));
					log.info("pp"+rs.getString(8));
				
				taskMasterBean=new TaskMasterBean();				
				taskMasterBean.setTask_id(rs.getInt(1));
				taskMasterBean.setWorkflow_id(rs.getInt(2));
				taskMasterBean.setStep_id(rs.getInt(3));
				taskMasterBean.setStep_no(rs.getInt(4));				
				taskMasterBean.setTask_name(rs.getString(5));
				taskMasterBean.setTask_rule_id(rs.getInt(6));
				taskMasterBean.setPriorty(rs.getString(7));
				taskMasterBean.setDeadline_time(rs.getString(8));
				taskMasterBean.setTask_instructions(rs.getString(9));
				taskMasterBean.setSupervisor_id(rs.getInt(10));
				taskMasterBean.setUser_name(userName(context,rs.getInt(10)));
				taskMasterBean.setTask_permission_id(rs.getString(11));
				taskMasterBean.setTask_requirment_id(rs.getString(12));
				taskMasterBean.setAdd_date(rs.getDate(13));
				taskMasterBean.setDeadline_day(rs.getDate(14));
			}
				
			taskMasterBean.setDate(CommonDateFormat.getTodayDateddmmyyyyString(taskMasterBean.getDeadline_day()));

			String dt[]=taskMasterBean.getDeadline_time().split(" ");
			taskMasterBean.setDeadline_time(dt[0]);
			taskMasterBean.setTimemode(dt[1]);
				String req[]=taskMasterBean.getTask_requirment_id().split("#");
				for(String s:req){
					if(s.equals("1")){
						taskMasterBean.setR1(s);
					}
				else if(s.equals("2")){
						taskMasterBean.setR2(s);
					}else if(s.equals("3")){
						taskMasterBean.setR3(s);
					}
					log.info("requrement id:------------>>"+s);
					//reqList.add(s);
				}
			
				String permisson[]=taskMasterBean.getTask_permission_id().split("#");
				for(String s:permisson){
					if(s.equals("1")){
						taskMasterBean.setP1(s);
					}
					else if(s.equals("2")){
						taskMasterBean.setP2(s);
					}
					else if(s.equals("3")){
						taskMasterBean.setP3(s);
					}
					else if(s.equals("4")){
						taskMasterBean.setP4(s);
					}
					log.info("permisson id:------------>>"+s);
					//perList.add(s);
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
	
	log.info("task name"+taskMasterBean.getTask_name());
	log.info("task name"+taskMasterBean.getDate());
	log.info("task name"+taskMasterBean.getStep_no());
	
	return taskMasterBean;
}

public List<TaskMasterBean> getTaskUserList(Context context,Integer task_id) throws SQLException{
	List<TaskMasterBean> roleList=new ArrayList<>();
	List<TaskMasterBean> userList=new ArrayList<>();
	roleList=getUserRoleList(context,task_id);
	
	for(TaskMasterBean taskMaster:roleList)
	{
	  log.info("user id---------------------->"+taskMaster.getTask_owner_id());
	  userList.addAll((getUserById(context,Integer.valueOf(taskMaster.getTask_owner_id()))));
	}
	log.info("userList:----------"+userList.size());
	return userList;
}

public String getUSerName(Context context,String userid)
{
	String name="";
	String[] id=userid.split("#");
	if(userid!=null && !userid.equals("")){
    for(String uid:id){
    	System.out.println("split string:--"+Integer.valueOf(uid));
    	try {
    		if(name.equals("")){
    			 name=userName(context,Integer.valueOf(uid));
    		}else{
    		  name=name+","+userName(context,Integer.valueOf(uid));
    		}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	}
	
	return name;
}

public String userName(Context context,Integer userid)throws SQLException{
	PreparedStatement ps=null;
	ResultSet rs=null;
	String name="";
	try{
		ps=context.getDBConnection().prepareStatement("select e.eperson_id,m.text_value as first_name,ln.text_value as last_name from eperson"
				+ " e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124 "+ 
		       " left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 where e.eperson_id="+userid+" order by m.text_value ");
	 rs=ps.executeQuery();
	 while(rs.next()){
			if(rs.getString(2)!=null && !rs.getString(2).equals("")){
				name=rs.getString(2);
			}
			if(rs.getString(3)!=null && !rs.getString(3).equals("")){
				name=name+" "+rs.getString(3);
			}	
	 }
	}catch(SQLException sqe){
		log.info("Error in geting user name:--"+sqe);
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
	return name;
}


public List<TaskMasterBean> getUserRoleList(Context context,Integer task_id)throws SQLException{
	List<TaskMasterBean> roleList=new ArrayList<>();
	PreparedStatement ps=null;
	ResultSet rs=null;
	String name="";
	try{
		ps=context.getDBConnection().prepareStatement("select task_owner_id,status,task_comment,postpone_task_comment,"
				+ " change_taskfinished_comments,senior_level_task_comment from task_role where task_id="+task_id+" ");
	 rs=ps.executeQuery();
	 while(rs.next()){
		 taskMasterBean=new TaskMasterBean();
		 taskMasterBean.setTask_owner_id(String.valueOf(rs.getInt(1)));
		 taskMasterBean.setStatus(rs.getString(2));
		 taskMasterBean.setTask_comment(rs.getString(3));
		taskMasterBean.setPostpone_task_comment(rs.getString(4));
		taskMasterBean.setChange_taskfinished_comments(rs.getString(5));
		taskMasterBean.setSenior_level_task_comment(rs.getString(6));
		roleList.add(taskMasterBean);
	 }
	}catch(SQLException sqe){
		log.info("Error in geting user name:--"+sqe);
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
	
	return roleList;
}

public List<TaskMasterBean> getAdhocUserList(Context context,Integer task_id)throws SQLException{
	List<TaskMasterBean> roleList=new ArrayList<>();
	PreparedStatement ps=null;
	ResultSet rs=null;
	try{
		ps=context.getDBConnection().prepareStatement(" select task_owner_id,status from adhoc_task_role where task_id="+task_id+" ");
	 rs=ps.executeQuery();
	 
	 while(rs.next()){
		 taskMasterBean=new TaskMasterBean();
		 taskMasterBean.setTask_owner_id(String.valueOf(rs.getInt(1)));
		 taskMasterBean.setStatus(rs.getString(2));
		roleList.add(taskMasterBean);
	 }
	}catch(SQLException sqe){
		log.info("Error in geting user name:--"+sqe);
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
	return roleList;
}

public static void main(String[] args) {
	TaskManager tm=new TaskManager();
	String userid="0";
	String[] id=userid.split("#");
	    for(String uid:id){
	    	System.out.println("split string:--"+Integer.valueOf(uid));
	    }
	 
	//	tm.getAllTaskList(30, 53);

}
}
