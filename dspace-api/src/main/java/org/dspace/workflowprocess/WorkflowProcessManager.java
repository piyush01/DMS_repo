package org.dspace.workflowprocess;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.core.Email;
import org.dspace.eperson.EPerson;
import org.dspace.util.CommonDateFormat;
import org.dspace.util.SequenceGenerateManager;
import org.dspace.workflowmanager.TaskManager;
import org.dspace.workflowmanager.WorkflowManager;
import org.dspace.workflowmanager.WorkflowMasterBean;
import org.dspace.workflowmanager.WorkflowStepManager;

public class WorkflowProcessManager {
public static final Logger log=Logger.getLogger(WorkflowProcessManager.class);
private WorkflowManager workflowManager=new WorkflowManager();
private TaskManager taskManager=new TaskManager();
private WorkflowProcessBean workflowProcessBean=new WorkflowProcessBean();
private WorkflowStepManager workflowStepManager=new WorkflowStepManager();
private WorkflowMasterBean workflowMasterBean=new WorkflowMasterBean();

private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages");


public WorkflowMasterBean getWorkflowMasterBean() {
	return workflowMasterBean;
}

public void setWorkflowMasterBean(WorkflowMasterBean workflowMasterBean) {
	this.workflowMasterBean = workflowMasterBean;
}

public WorkflowStepManager getWorkflowStepManager() {
	return workflowStepManager;
}

public void setWorkflowStepManager(WorkflowStepManager workflowStepManager) {
	this.workflowStepManager = workflowStepManager;
}

public TaskManager getTaskManager() {
	return taskManager;
}

public void setTaskManager(TaskManager taskManager) {
	this.taskManager = taskManager;
}

public WorkflowManager getWorkflowManager() {
	return workflowManager;
}

public void setWorkflowManager(WorkflowManager workflowManager) {
	this.workflowManager = workflowManager;
}


public WorkflowProcessBean getWorkflowProcessBean() {
	return workflowProcessBean;
}

public void setWorkflowProcessBean(WorkflowProcessBean workflowProcessBean) {
	this.workflowProcessBean = workflowProcessBean;
}




	public Integer sendWorkflow(Context context, HttpServletRequest request)
			throws SQLException {
		int pId = 0, rowId = 0;
		List<WorkflowProcessBean> list = new ArrayList<WorkflowProcessBean>();
		Integer workflow_id = Integer.valueOf(request.getParameter("workflowId"));
		String[] file_descriptionArr=request.getParameterValues("file_description");
		String file_description="";
		
		String handle = request.getParameter("handle");
		Integer item_id = Integer.valueOf(request.getParameter("item_id"));
		EPerson e = context.getCurrentUser();
		Integer eid = e.getID();
		Integer stepid = 0;
		Integer task_id = 0;
	
		Date deadline_date = new Date();
		
		int count=0;
		
		pId=SequenceGenerateManager.getGeneratedId(context,"workprocess_role_seq");
		rowId = saveWorkprocessrole(context, request, pId, stepid, task_id,deadline_date);
		list = getWorkTaskDetails(context, workflow_id, eid);
		
		if (list != null && list.size() > 0) {
			for (WorkflowProcessBean workflowProcessBean : list) {
				stepid = workflowProcessBean.getWorkflow_stepid();
				task_id = workflowProcessBean.getWorkflow_taskid();
				deadline_date=workflowProcessBean.getDate();
				
			if(file_descriptionArr!=null && file_descriptionArr.length>0){
		for (int i = 0; i < file_descriptionArr.length; i++) {	
			log.info("i value:--"+i);
				
				file_description=file_descriptionArr[i];		
		 if(file_description!=null && !file_description.equals(""))
		 {
			 String[] file_description_Arr=file_description.split("#"); 
	
		 for (int s1 = 0; s1 < file_description_Arr.length; s1++) {
			 
			 if(count==0 && file_description_Arr[2]!=null && !file_description_Arr[2].equals(""))
			 { 
		       rowId = saveWorkflowprocessdocument(context,file_description_Arr[0], file_description_Arr[3], file_description_Arr[2],handle, workflow_id, pId, item_id, stepid, task_id,Integer.parseInt(file_description_Arr[1]));
			 }
			 count++;
		 }
		 
		 count=0;
	   }
		}
	}
  }
	}		
			
		if (rowId > 0) {
			log.info("Enter in send mail-------" + workflow_id);
			try {

				getSendMail(context, workflow_id);

			} catch (MessagingException me) {
				// TODO Auto-generated catch block
				me.printStackTrace();
			} catch (IOException ie) {
				// TODO Auto-generated catch block
				ie.printStackTrace();
			}
		}
		return rowId;
	}
	

public Integer saveWorkflowprocessdocument(Context context,String file_no,String file_link,String file_name,String handle,Integer workflow_id,Integer work_process_id,Integer item_id,Integer step_id,Integer task_id,Integer bitstreamId) throws SQLException{
	int nextId=0,rowId=0;
	
	if(file_link!=null && !file_link.equals("")){
		file_link=file_link+"?mode=1";
	}
	 PreparedStatement preparedStatement=null;
	 ResultSet rs = null;
	if(handle!=null && !handle.equals("")){
		 try {
			nextId=SequenceGenerateManager.getGeneratedId(context,"workflow_process_seq");
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

public  Integer saveWorkprocessrole(Context context,HttpServletRequest request,Integer nextId,Integer step_id,Integer task_id,Date deadline_date) throws SQLException {
	int rowId=0;
	 PreparedStatement preparedStatement=null;
	 ResultSet rs = null;
	Integer workflow_id=Integer.valueOf(request.getParameter("workflowId"));
	Integer item_id=Integer.valueOf(request.getParameter("item_id"));
 	String handle=request.getParameter("handle");
 	Integer userid=0;
 	EPerson e=context.getCurrentUser();
 	userid=e.getID(); 	
	 if(handle!=null && !handle.equals("")){
	 try {
		//nextId=workflowManager.getSendWorkflowNewId(context);
        preparedStatement=context.getDBConnection().prepareStatement(" insert into workprocess_role(work_process_id,user_id,task_id,community_id,collection_id,item_id,status,update_date,workflow_id,workflow_step_id,deadline_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );
        preparedStatement.setInt(1, nextId);  
        preparedStatement.setInt(2, userid);
        preparedStatement.setInt(3, task_id);
        preparedStatement.setInt(4, 0);
        preparedStatement.setInt(5, 0);
        preparedStatement.setInt(6,item_id);
        preparedStatement.setString(7, "P");
        preparedStatement.setDate(8, new java.sql.Date(new Date().getTime()));       
        preparedStatement.setInt(9, workflow_id);
        preparedStatement.setInt(10, step_id);
        preparedStatement.setDate(11, new java.sql.Date(deadline_date.getTime()));
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

	public String getSendMail(Context context, Integer workflow_id)
			throws MessagingException, IOException, SQLException {

		String emailid = "";
		String userid = getTaskOwnerId(context, workflow_id);
		String[] id = userid.split("#");
		if (userid != null && !userid.equals("")) {
			for (String uid : id) {
				try {
					emailid = userDetails(context, Integer.valueOf(uid));
					sendEmail(context, emailid, true);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		}

		return userid;
	}


	private static void sendEmail(Context context, String email1,
			boolean isRegister) throws MessagingException, IOException,
			SQLException {
		SimpleDateFormat formDate = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = formDate.format(new Date()); // option 2
		// String content =
		// (resourceBundle.getString("dms.dspace.mail.content"));
		String content = "Dear Member," + '\n';
		content = content + '\n' + "A document awaiting your approval at DMS.";
		content = content + '\n' + "Dated on " + strDate
				+ ".Kindly check at your end and update the Task List." + "\n";
		content = content + "\n" + "Regards" + '\n' + '\n' + '\n';
		content = content + "Technical Team" + '\n' + '\n' + '\n' + '\n' + '\n'
				+ '\n' + '\n';
		content = content
				+ "This is a system generated mailer. Please do not REPLY to this mail.";
		content = content.replace("[date]", strDate);
		log.info("html contentcontent------------------" + content);
		Email bean = new Email();
		bean.setSubject("DMS Document Workflow Assign");
		if (content != null && !content.equals("")) {
			bean.setContent(content);
		} else {
			bean.setContent("Note:------.");
		}
		bean.setCharset("UTF-8");
		bean.addRecipient(email1);
		bean.send();
	}

	public String userDetails(Context context, Integer userid)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = "";
		String email = "";
		try {
			ps = context
					.getDBConnection()
					.prepareStatement(
							"select e.eperson_id,m.text_value as first_name,ln.text_value as last_name,e.email from eperson"
									+ " e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124 "
									+ " left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 where e.eperson_id="
									+ userid + " order by m.text_value ");
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString(2) != null && !rs.getString(2).equals("")) {
					name = rs.getString(2);
				}
				if (rs.getString(3) != null && !rs.getString(3).equals("")) {
					name = name + " " + rs.getString(3);
				}
				email = rs.getString(4);
			}
		} catch (SQLException sqe) {
			log.info("Error in geting user name:--" + sqe);
		} finally {
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
		return email;
	}



public String getTaskOwnerId(Context context,Integer workflow_id) throws SQLException{
	String taskownerid="";
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;
	List<String> userid=new ArrayList<String>();
	try {
		preparedStatement = context.getDBConnection().prepareStatement(" select DISTINCT tr.task_owner_id from workflow_master wm left outer join step_master "
 			+" sm on wm.workflow_id=sm.workflow_id left outer join task_master tm on sm.workflow_id=tm.workflow_id " 
			+ " left outer join task_role tr on tr.task_id=tm.task_id " 
			+ " left outer join eperson ep on ep.eperson_id=tr.task_owner_id "
			+ " where tm.workflow_id="+workflow_id+" ");
		
		rs = preparedStatement.executeQuery();
		while(rs.next()) 
		{
			if(taskownerid!=null && !taskownerid.equals("")){
				 taskownerid=taskownerid+"#"+rs.getString(1);
			 }
			 else{
				 taskownerid=rs.getString(1);
			 }
		}
			}
	 catch (SQLException ex) {
			log.info("Error in get new id======>" + ex.getMessage());
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
			if (preparedStatement != null)
	        {
	            try { preparedStatement.close(); } catch (SQLException sqle) {
	           	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                throw sqle;
	            }
	        }
	    }
	return taskownerid;
}


public int getGenerateId(Context context) throws SQLException {
	int id = 0;
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;
	try {
		preparedStatement = context.getDBConnection().prepareStatement(" SELECT nextval('workflow_process_seq') ");
		rs = preparedStatement.executeQuery();
		if (rs.next()) // found
		{
			id = Integer.valueOf(rs.getString(1));
		}
	} catch (SQLException ex) {
		log.info("Error in get new id======>" + ex.getMessage());
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
		if (preparedStatement != null)
        {
            try { preparedStatement.close(); } catch (SQLException sqle) {
           	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
                throw sqle;
            }
        }
    }
	return id;
}


public List<WorkflowMasterBean> getSubmissionDocument(Context context)throws SQLException{
	List<WorkflowMasterBean> docList=new ArrayList<>();
	PreparedStatement ps=null;
	ResultSet rs=null;
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
 
	try{	
	ps=context.getDBConnection().prepareStatement("select distinct wp.workflow_id,wm.workflow_name from workprocess_role wr right join "
			    +" workflow_process wp on wr.work_process_id=wp.work_process_id right join workflow_master wm on wp.workflow_id=wm.workflow_id where wr.user_id="+userid+" ");	
	   rs=ps.executeQuery();
		  while(rs.next()){
			  workflowMasterBean=new WorkflowMasterBean();
			  workflowMasterBean.setWorkflow_id(rs.getInt(1));
			  workflowMasterBean.setWorkflow_name(rs.getString(2));
			  docList.add(workflowMasterBean);
		  }
		  
		  //workflowStepManager.getAllWorkflowstep(context, workflow_id);
				  
	}catch(SQLException sqe){
		log.info("Error in getting sumbition doc task list:--------------"+sqe);
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

 public List<WorkflowProcessBean> getSupervisorTaskList(Context context)throws SQLException{
	List<WorkflowProcessBean> taskList=new ArrayList<>();
	Map<String,String> docList=new HashMap<String,String>();
	Map<String,String> urlList=new HashMap<String,String>();
	
	PreparedStatement ps=null;
	ResultSet rs=null;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
     String date="";
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
	try{	
		
	ps=context.getDBConnection().prepareStatement(" select distinct wr.work_process_id,wp.workflow_id,wp.document_name,wp.status,wp.add_date,"
             + " wp.handle,wp.file_path,wr.user_id,tm.priorty,wr.status,tm.deadline_day,wr.deadline_date,tm.task_name,tm.task_no,tm.task_id from workflow_process wp right join task_master tm "
             + " on wp.workflow_id=tm.workflow_id right join task_role tr on tr.task_id=tm.task_id "
             + " right join workprocess_role wr on wr.work_process_id=wp.work_process_id "
             + " where tm.supervisors_id="+userid+" order by tm.task_name ");	
	   rs=ps.executeQuery();
		  while(rs.next()){
			workflowProcessBean=new WorkflowProcessBean();
			workflowProcessBean.setProcess_id(rs.getInt(1));
			workflowProcessBean.setWorkflow_id(rs.getInt(2));
			//workflowProcessBean.setDocument_name(rs.getString(3));
			workflowProcessBean.setStatus(rs.getString(4));
			date=sdf.format(rs.getDate(5));
			workflowProcessBean.setAdate(date);
			workflowProcessBean.setHandle(rs.getString(6));
			//workflowProcessBean.setFile_path(rs.getString(7));
			workflowProcessBean.setUser_name(taskManager.getUSerName(context,String.valueOf(rs.getInt(8))));
			workflowProcessBean.setPriorty(rs.getString(9));
			workflowProcessBean.setStatus(rs.getString(10));
			workflowProcessBean.setDue_Date(sdf.format(rs.getDate(12)));
			workflowProcessBean.setTask_name(rs.getString(13));
			workflowProcessBean.setTask_no(rs.getInt(14));
			workflowProcessBean.setTask_id(rs.getInt(15));
			docList.put(rs.getString(7),rs.getString(3));
			workflowProcessBean.setDocumentList(docList);
			taskList.add(workflowProcessBean);
		}
		//workflowProcessBean.setDocumentList(docList);
	    //taskList.add(workflowProcessBean);
		  
	}catch(SQLException sqe){
		log.info("Error in getting task list:--------------"+sqe);
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
	
	log.info("taskList:-----no of records---------------->"+taskList.size());
	
	return taskList;
}

public List<WorkflowProcessBean> getTaskList(Context context)throws SQLException{
	List<WorkflowProcessBean> taskList=new ArrayList<>();
	Map<String,String> docList=new HashMap<String,String>();
	Map<String,String> urlList=new HashMap<String,String>();
	
	PreparedStatement ps=null;
	ResultSet rs=null;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
     String date="";
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
	try{
	/*ps=context.getDBConnection().prepareStatement(" select wp.process_id,wp.workflow_id,wp.document_name,wp.status,"
			+ " wp.add_date,wp.handle,wp.file_path,wp.user_id from workflow_process wp left outer join task_master tm "
			+ "on wp.workflow_id=tm.workflow_id where tm.task_owner_id like '%"+userid+"%' ");*/
		
	ps=context.getDBConnection().prepareStatement(" select distinct wr.work_process_id,wp.workflow_id,wp.document_name,wp.status,wp.add_date,"
             + " wp.handle,wp.file_path,wr.user_id,tm.priorty,wr.status,tm.deadline_day,wr.deadline_date,tm.task_name,tm.task_no,tm.task_id,"
             + " tm.deadline_time,tm.task_instruction from workflow_process wp right join task_master tm "
             + " on wp.workflow_id=tm.workflow_id right join task_role tr on tr.task_id=tm.task_id "
             + " right join workprocess_role wr on wr.work_process_id=wp.work_process_id "
             + " where tr.task_owner_id="+userid+" order by tm.task_no ");	
	   rs=ps.executeQuery();
		  while(rs.next()){
			workflowProcessBean=new WorkflowProcessBean();
			workflowProcessBean.setProcess_id(rs.getInt(1));
			workflowProcessBean.setWorkflow_id(rs.getInt(2));
			//workflowProcessBean.setDocument_name(rs.getString(3));
			workflowProcessBean.setStatus(rs.getString(4));
			date=sdf.format(rs.getDate(5));
			workflowProcessBean.setAdate(date);
			workflowProcessBean.setHandle(rs.getString(6));
			//workflowProcessBean.setFile_path(rs.getString(7));
			workflowProcessBean.setUser_name(taskManager.getUSerName(context,String.valueOf(rs.getInt(8))));
			workflowProcessBean.setPriorty(rs.getString(9));
			workflowProcessBean.setStatus(rs.getString(10));
			workflowProcessBean.setDue_Date(sdf.format(rs.getDate(12)));
			workflowProcessBean.setTask_name(rs.getString(13));
			workflowProcessBean.setTask_no(rs.getInt(14));
			workflowProcessBean.setTask_id(rs.getInt(15));
			workflowProcessBean.setDeadline_time(rs.getString(16));
			workflowProcessBean.setTask_instruction(rs.getString(17));
			docList.put(rs.getString(7),rs.getString(3));
			workflowProcessBean.setDocumentList(docList);
			taskList.add(workflowProcessBean);
		}
		//workflowProcessBean.setDocumentList(docList);
	    //taskList.add(workflowProcessBean);
		  
	}catch(SQLException sqe){
		log.info("Error in getting task list:--------------"+sqe);
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
	
	return taskList;
}

public List<WorkflowProcessBean> getTaskDetails(Context context,HttpServletRequest request)throws SQLException{
	List<WorkflowProcessBean> taskList=new ArrayList<WorkflowProcessBean>();
	Map<String,String> docList=new HashMap<String,String>();
	PreparedStatement ps=null;
	ResultSet rs=null;
	Integer process_id=0,task_id=0;
	String processId="";
	String useridcolumn="";
	
	if(request.getParameter("process_task_id")!=null){
		processId=request.getParameter("process_task_id");
	}
	if(processId.equals("")){
		processId=(String)request.getAttribute("process_task_id");
	}
	log.info("processId:----------------->>"+processId);
	String p_id[]=processId.split("-");
	process_id=Integer.parseInt(p_id[0]);
	task_id=Integer.parseInt(p_id[1]);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
     String date="";
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
  	useridcolumn="tr.task_owner_id="+userid;
  	
  	//for superviosr code
  	if(request.getParameter("role")!=null && request.getParameter("role").equals("1")){
  		useridcolumn="tm.supervisors_id="+userid;
  	}
  	
  	log.info("useridcolumn:---->"+useridcolumn);
  	
	try{
		ps=context.getDBConnection().prepareStatement(" select distinct wr.work_process_id,wp.workflow_id,wp.document_name,tr.status,"
				  +"   wp.add_date,wp.handle,wp.file_path,wr.user_id,tm.priorty,wr.status,wm.workflow_name,"
				  +"  sm.step_name,tm.task_name,tm.task_instruction,tm.supervisors_id,tm.deadline_day,tr.task_owner_id,wr.item_id,"
				  +"  wp.workflow_step_id,wp.task_id,tr.task_comment,wr.deadline_date,tm.task_no,tm.task_id,tm.task_permission_id,"
				  +   "tm.task_requirements_id,wp.document_id,wp.bitstream_id from workflow_process wp left outer join task_master tm on wp.workflow_id=tm.workflow_id "
				  +"  left outer join task_role tr on tr.task_id=tm.task_id left outer join workprocess_role wr on wr.work_process_id=wp.work_process_id " 
				  +"  left outer join workflow_master wm on wm.workflow_id=tm.workflow_id "
				  +"  left outer join step_master sm on sm.step_id=tm.step_id where "+useridcolumn+" and "
		          +"  wr.work_process_id="+process_id+" and tm.task_id="+task_id+" order by tm.task_name ");	
		rs=ps.executeQuery();
		
		while(rs.next())
		{
				workflowProcessBean = new WorkflowProcessBean();
				workflowProcessBean.setProcess_id(rs.getInt(1));
				workflowProcessBean.setWorkflow_id(rs.getInt(2));
				workflowProcessBean.setDocument_name(rs.getString(3));
				workflowProcessBean.setStatus(rs.getString(4));
				workflowProcessBean.setAdate(sdf.format(rs.getDate(5)));
				workflowProcessBean.setHandle(rs.getString(6));
				workflowProcessBean.setFile_path(rs.getString(7));
				workflowProcessBean.setUser_name(taskManager.getUSerName(
						context, String.valueOf(rs.getInt(8))));
				workflowProcessBean.setPriorty(rs.getString(9));
				// workflowProcessBean.setStatus(rs.getString(10));
				workflowProcessBean.setWorkflow_name(rs.getString(11));
				workflowProcessBean.setStep_name(rs.getString(12));
				workflowProcessBean.setTask_name(rs.getString(13));
				workflowProcessBean.setTask_instruction(rs.getString(14));
				workflowProcessBean.setSupervisor_name(taskManager.getUSerName(
						context, String.valueOf(rs.getInt(15))));
				workflowProcessBean.setAssign_to_user(taskManager.getUSerName(
						context, String.valueOf(rs.getInt(17))));
				workflowProcessBean.setItem_id(rs.getInt(18));
				workflowProcessBean.setWorkflow_stepid(rs.getInt(19));
				workflowProcessBean.setWorkflow_taskid(rs.getInt(20));
				workflowProcessBean.setTask_comment(rs.getString(21));
				workflowProcessBean.setDue_Date(sdf.format(rs.getDate(22)));
				workflowProcessBean.setTask_no(rs.getInt(23));
				workflowProcessBean.setTask_id(rs.getInt(24));
				setTask_permisson(workflowProcessBean, rs.getString(25));
				setTask_requirement(workflowProcessBean, rs.getString(26));
				workflowProcessBean.setDocument_id(Integer.parseInt(rs.getString(27)));
				workflowProcessBean.setBitstream_id(rs.getInt(28));
				// workflowProcessBean.setTask_permisson_id(rs.getString(25));
				//workflowProcessBean.setTask_requirments_id(rs.getString(26));
				docList.put(rs.getString(7), rs.getString(3));
				workflowProcessBean.setDocumentList(docList);
				taskList.add(workflowProcessBean);
		}
//		taskList.add(workflowProcessBean);
	}
	catch(SQLException sqe){
		log.info("Error in getting task list:--------------"+sqe);
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
	log.info("taskList:----------------->>"+taskList.size());
	return taskList;
}

//for task_permisson requirement set

public void setTask_requirement(WorkflowProcessBean workflowProcessBean,String requirementid)
{
	String[] requirement=requirementid.split("#");
	 for(int i=0;i<requirement.length; i++)
	 {
		 if(requirement[i].equals("2")){
			 workflowProcessBean.setComment(Integer.valueOf(requirement[i]));
		 }
		 else  if(requirement[i].equals("3")){
			 workflowProcessBean.setApprove(Integer.valueOf(requirement[i]));
		 }    
	 }   
	
}

// for task_permisson role set

public void setTask_permisson(WorkflowProcessBean workflowProcessBean,String permissonid)
{
	String[] permisson=permissonid.split("#");
	 for(int i=0;i<permisson.length; i++)
	 {

		 if(permisson[i].equals("1")){
			 workflowProcessBean.setAllStep(Integer.valueOf(permisson[i]));
		 }
		 else  if(permisson[i].equals("2")){
			 workflowProcessBean.setEdit_comment(Integer.valueOf(permisson[i]));
		 }
     else  if(permisson[i].equals("3")){
    	 workflowProcessBean.setPostpone_task(Integer.valueOf(permisson[i]));
		 }
     else  if(permisson[i].equals("4")){
    	 workflowProcessBean.setChange_finished_task(Integer.valueOf(permisson[i]));
		 }
	 }
}

public List<WorkflowProcessBean> getSupervisorTaskDetails(Context context,HttpServletRequest request)throws SQLException{
	List<WorkflowProcessBean> taskList=new ArrayList<WorkflowProcessBean>();
	Map<String,String> docList=new HashMap<String,String>();
	PreparedStatement ps=null;
	ResultSet rs=null;
	Integer process_id=0,task_id=0;
	String processId="";
	log.info("processId:----------------->>"+request.getParameter("process_task_id"));
	if(request.getParameter("process_task_id")!=null){
		processId=request.getParameter("process_task_id");
	}
	if(processId.equals("")){
		processId=(String)request.getAttribute("process_task_id");
	}
	log.info("processId:----------------->>"+processId);
	String p_id[]=processId.split("-");
	process_id=Integer.parseInt(p_id[0]);
	task_id=Integer.parseInt(p_id[1]);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
     String date="";
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
	try{
		ps=context.getDBConnection().prepareStatement(" select distinct wr.work_process_id,wp.workflow_id,wp.document_name,wp.status,"
				  +"   wp.add_date,wp.handle,wp.file_path,wr.user_id,tm.priorty,wr.status,wm.workflow_name,"
				  +"  sm.step_name,tm.task_name,tm.task_instruction,tm.supervisors_id,tm.deadline_day,tr.task_owner_id,wr.item_id,"
				  +"  wp.workflow_step_id,wp.task_id,wr.task_comment,wr.deadline_date,tm.task_no,tm.task_id from workflow_process wp " 
				  +"  left outer join task_master tm on wp.workflow_id=tm.workflow_id "
				  +"  left outer join task_role tr on tr.task_id=tm.task_id "
				  +"  left outer join workprocess_role wr on wr.work_process_id=wp.work_process_id " 
				  +"  left outer join workflow_master wm on wm.workflow_id=tm.workflow_id "
				  +"  left outer join step_master sm on sm.step_id=tm.step_id where tr.task_owner_id="+userid+" and "
		          +"  wr.work_process_id="+process_id+" and tm.task_id="+task_id+" order by tm.task_name ");	
		rs=ps.executeQuery();
		
		while(rs.next())
		{
			workflowProcessBean=new WorkflowProcessBean();
			workflowProcessBean.setProcess_id(rs.getInt(1));
			workflowProcessBean.setWorkflow_id(rs.getInt(2));
			workflowProcessBean.setDocument_name(rs.getString(3));
			workflowProcessBean.setStatus(rs.getString(4));
			workflowProcessBean.setAdate(sdf.format(rs.getDate(5)));
			workflowProcessBean.setHandle(rs.getString(6));
			workflowProcessBean.setFile_path(rs.getString(7));
			workflowProcessBean.setUser_name(taskManager.getUSerName(context,String.valueOf(rs.getInt(8))));
			workflowProcessBean.setPriorty(rs.getString(9));
			workflowProcessBean.setStatus(rs.getString(10));
			workflowProcessBean.setWorkflow_name(rs.getString(11));
			workflowProcessBean.setStep_name(rs.getString(12));
			workflowProcessBean.setTask_name(rs.getString(13));
			workflowProcessBean.setTask_instruction(rs.getString(14));
			workflowProcessBean.setSupervisor_name(taskManager.getUSerName(context,String.valueOf(rs.getInt(15))));
			workflowProcessBean.setAssign_to_user(taskManager.getUSerName(context,String.valueOf(rs.getInt(17))));
			workflowProcessBean.setItem_id(rs.getInt(18));
			workflowProcessBean.setWorkflow_stepid(rs.getInt(19));
			workflowProcessBean.setWorkflow_taskid(rs.getInt(20));
			workflowProcessBean.setTask_comment(rs.getString(21));
			workflowProcessBean.setDue_Date(sdf.format(rs.getDate(22)));
			workflowProcessBean.setTask_no(rs.getInt(23));
			workflowProcessBean.setTask_id(rs.getInt(24));
			docList.put(rs.getString(7),rs.getString(3));
			workflowProcessBean.setDocumentList(docList);
			taskList.add(workflowProcessBean);
		}
//		taskList.add(workflowProcessBean);
	}
	catch(SQLException sqe){
		log.info("Error in getting task list:--------------"+sqe);
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
	
	return taskList;
}


public List<WorkflowProcessBean> getTaskCommentDetails(Context context,HttpServletRequest request)throws SQLException{
	List<WorkflowProcessBean> taskList=new ArrayList<WorkflowProcessBean>();
	Map<String,String> docList=new HashMap<String,String>();
	PreparedStatement ps=null;
	ResultSet rs=null;
	//Integer process_id=Integer.valueOf(request.getParameter("process_id"));
	Integer task_id=Integer.valueOf(request.getParameter("task_id"));
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
	try{
		/*ps=context.getDBConnection().prepareStatement("  Select distinct wr.work_process_id,wr.status,wr.user_id,tr.task_comment,wp.workflow_id,"
				   +" wp.workflow_step_id,wp.task_id from workflow_process wp "
				   +" left outer join task_master tm on wp.workflow_id=tm.workflow_id "
				   +" left outer join task_role tr on tr.task_id=tm.task_id "
				   +" left outer join workprocess_role wr on wr.work_process_id=wp.work_process_id " 
				   +" left outer join workflow_master wm on wm.workflow_id=tm.workflow_id "
				   +" left outer join step_master sm on sm.step_id=tm.step_id " 
		           +" where tr.task_owner_id="+userid+" or tm.supervisors_id="+userid+" and wr.work_process_id="+process_id+" ");		*/
		  if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
	        { 
			  //for supervisor approval
			  ps=context.getDBConnection().prepareStatement(" select distinct tm.task_id,tr.task_comment from task_master tm left outer join task_role tr"
						+ " on tm.task_id=tr.task_id left outer join workflow_process wp on wp.task_id=tm.task_id where tr.task_id="+task_id+" and tr.supervisor_id="+userid+" ");
	        }
		  else{
		  ps=context.getDBConnection().prepareStatement(" select distinct tm.task_id,tr.task_comment from task_master tm left outer join task_role tr"
				+ " on tm.task_id=tr.task_id left outer join workflow_process wp on wp.task_id=tm.task_id where tr.task_id="+task_id+" and tr.task_owner_id="+userid+" ");
		  }
		rs=ps.executeQuery();
		
		while(rs.next())
		{
			workflowProcessBean=new WorkflowProcessBean();
			workflowProcessBean.setTask_id(rs.getInt(1));
			workflowProcessBean.setTask_comment(rs.getString(2));
			//workflowProcessBean.setStatus(rs.getString(2));
			//workflowProcessBean.setWorkflow_id(rs.getInt(5));
			//workflowProcessBean.setWorkflow_stepid(rs.getInt(6));
			//workflowProcessBean.setWorkflow_taskid(rs.getInt(7));
			taskList.add(workflowProcessBean);
		}
		
	}catch(SQLException sqe){
		log.info("Error in getting task comment list:--------------"+sqe);
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
	
	return taskList;
}

public List<WorkflowProcessBean> getWorkTaskDetails(Context context,Integer workflow_id,Integer userId)throws SQLException{
	List<WorkflowProcessBean> taskList=new ArrayList<WorkflowProcessBean>();
	PreparedStatement ps=null;
	ResultSet rs=null;
     Integer userid=0;
  	EPerson e=context.getCurrentUser();
  	userid=e.getID(); 
	try{
		ps=context.getDBConnection().prepareStatement(" select tm.step_id,tm.task_id,tm.deadline_day from task_master tm left outer "
				+ " join task_role tr on tm.task_id=tr.task_id where tm.workflow_id="+workflow_id+" ");		
		rs=ps.executeQuery();
		
		while(rs.next())
		{
		workflowProcessBean=new WorkflowProcessBean();
		workflowProcessBean.setWorkflow_stepid(rs.getInt(1));
		workflowProcessBean.setWorkflow_taskid(rs.getInt(2));
		workflowProcessBean.setDate(rs.getDate(3));
		
		taskList.add(workflowProcessBean);
		}
		
	}catch(SQLException sqe){
		log.info("Error in getting task comment list:--------------"+sqe);
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
	
	return taskList;
}


	public Integer saveProcessTask(Context context,HttpServletRequest request,String column,String comments) throws SQLException
	{
		String status=request.getParameter("status");
		//String comment=request.getParameter("task_comment");
		String date=request.getParameter("deadline_date");
		Integer pid=Integer.valueOf(request.getParameter("process_id"));
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  String query="update workprocess_role set status=? , "+column+"=?, deadline_date=? where work_process_id=?";
		  try 
			 {
		      if(date!=null && column.equals("postpone_task_reason_cmts"))
		      {
		    	 preparedStatement=context.getDBConnection().prepareStatement(" update workprocess_role set status=? , "+column+"=?, deadline_date=? where work_process_id=? ");	  
		    	 preparedStatement.setString(1,status);
			     preparedStatement.setString(2,comments.trim());
			     preparedStatement.setDate(3,new java.sql.Date(CommonDateFormat.getDateyyyymmddAsString(date).getTime()));
			     preparedStatement.setInt(4,pid);
			     rowId=preparedStatement.executeUpdate();
		      }
		      else
		      {
				preparedStatement=context.getDBConnection().prepareStatement(" update workprocess_role set status=? , "+column+"=? where work_process_id=? ");
		        preparedStatement.setString(1,status);
		        preparedStatement.setString(2,comments.trim());
		        preparedStatement.setInt(3,pid);
		        rowId=preparedStatement.executeUpdate();		        
		      }
			 } 
			 catch (Exception ex) {
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
			 
		return rowId;
	}
	
	
	public Integer updateTaskComments(Context context,HttpServletRequest request) throws SQLException
	{
		String task_comments=request.getParameter("task_comment");
		Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		EPerson e=context.getCurrentUser();
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
			  if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
		        {
				  //for supervisor approval
				  preparedStatement=context.getDBConnection().prepareStatement(" update task_role set task_comment=? where task_id=? and supervisor_id=? ");
		        }
			  else
		         {
		        	//user level approval
			    preparedStatement=context.getDBConnection().prepareStatement(" update task_role set task_comment=? where task_id=? and task_owner_id=? ");
		        }
		        preparedStatement.setString(1, task_comments.trim());
		        preparedStatement.setInt(2,task_id);		        
		        preparedStatement.setInt(3,e.getID());
		        rowId=preparedStatement.executeUpdate();	
		        log.info("approve task cooments:-----------"+rowId);
		        
		  } 
			 catch (Exception ex) {
				 log.info("Error in update task cooments:-------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	
	public Integer changeTask(Context context,HttpServletRequest request) throws SQLException
	{
		String status=request.getParameter("status");
		String change_task_comment=request.getParameter("change_task_comment");
		Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		EPerson e=context.getCurrentUser();
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
			  if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
		        {
				  //for supervisor approval
				  preparedStatement=context.getDBConnection().prepareStatement(" update task_role set status=?, change_taskfinished_comments=? where task_id=? and supervisor_id=? ");
				  }else{
					  preparedStatement=context.getDBConnection().prepareStatement(" update task_role set status=?, change_taskfinished_comments=? where task_id=? and task_owner_id=? ");
				  } 
			  preparedStatement.setString(1, status);
		        preparedStatement.setString(2,change_task_comment.trim());		        
		        preparedStatement.setInt(3,task_id);
		        preparedStatement.setInt(4,e.getID());
		        rowId=preparedStatement.executeUpdate();	
		        log.info("approve changeTask:-----------"+rowId);
			 } 
			 catch (Exception ex) {
				 log.info("Error in changeTaskcooments:-------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	public Integer postponeTask(Context context,HttpServletRequest request) throws SQLException
	{
		
		String postpone_task_comment=request.getParameter("postpone_task_comment");
		Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		String date=request.getParameter("deadline_date");
		EPerson e=context.getCurrentUser();
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
			  if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
		        {
				  //for supervisor approval
				  preparedStatement=context.getDBConnection().prepareStatement(" update task_role set postpone_task_comment=?, deadline_day=? where task_id=? and supervisor_id=? ");
		        }else{
			    preparedStatement=context.getDBConnection().prepareStatement(" update task_role set postpone_task_comment=?, deadline_day=? where task_id=? and task_owner_id=? ");
		        }
		        preparedStatement.setString(1, postpone_task_comment.trim());
		        preparedStatement.setDate(2,new java.sql.Date(CommonDateFormat.getDateyyyymmddAsString(date).getTime()));		        
		        preparedStatement.setInt(3,task_id);
		        preparedStatement.setInt(4,e.getID());
		        rowId=preparedStatement.executeUpdate();	
		        log.info("approve postponeTask:-----------"+rowId);
			 } 
			 catch (Exception ex) {
				 log.info("Error in postponeTask cooments:-------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	public Integer approveTaskByUser(Context context,HttpServletRequest request) throws SQLException
	{
		String status=request.getParameter("status");
		String task_comments=request.getParameter("task_comment");
		Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		
		EPerson e=context.getCurrentUser();
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
			  if(request.getParameter("role")!=null && request.getParameter("role").equals("1"))
		        { 
				  //for supervisor approval
				  preparedStatement=context.getDBConnection().prepareStatement(" update task_role set status=?, task_comment=? where task_id=? and supervisor_id=? ");
		      	}else
		      	{
		      		//user level approval
		      		preparedStatement=context.getDBConnection().prepareStatement(" update task_role set status=?, task_comment=? where task_id=? and task_owner_id=? ");
		      	}
			    preparedStatement.setString(1,status);
		        preparedStatement.setString(2, task_comments.trim());
		        preparedStatement.setInt(3,task_id);		        
		        preparedStatement.setInt(4,e.getID());
		        rowId=preparedStatement.executeUpdate();	
		     		      
		        if(rowId>0)
		        {
		        	rowId=approveTask(context,request);
		        }		
		        log.info("after approve task:-----------"+rowId+"--status--"+status);
		      
		        if(status!=null && !status.equals("D"))
		        {
                  log.info("if condition"+request.getParameter(""));
		        	if(rowId>0){
			        	rowId=openTask(context,request);
			        }
		        }
		        
		      //  Integer step_id=Integer.valueOf(request.getParameter("workflow_stepid"));
		        
		       /* if(isCheckTaskClose(context,step_id)==true)
		        {
		        	rowId=openTaskByStep(context,request);
		        }*/
		        
		        log.info("openTask save:-----------"+rowId);
		  } 
			 catch (Exception ex) {
				 log.info("Error in update approveTaskByUser task role:-------------"+ex);
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
		  
		  return rowId;
	}
	
	public Integer approveTask(Context context,HttpServletRequest request) throws SQLException
	{
		Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		String status=request.getParameter("status");
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  log.info("approveTask:---status---------"+status+"---task_id--"+task_id);
		  try 
			 {
			 //for  approval
				preparedStatement=context.getDBConnection().prepareStatement(" update task_master set status=? where task_id=? and status='P' ");
		        preparedStatement.setString(1,status);
		        preparedStatement.setInt(2,task_id);		        
		        rowId=preparedStatement.executeUpdate();	
		     } 
			 catch (Exception ex) {
				 log.info("Error in update task role:-------------"+ex.getMessage());
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
		  
		  log.info("update task_master:-------------"+rowId+"---task_id--"+task_id);
		  
		  return rowId;
	}
	
	public Integer openTask(Context context,HttpServletRequest request) throws SQLException
	{
		//Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		 log.info("openTask--");
		EPerson e=context.getCurrentUser();		
		WorkflowProcessBean workflowProcessBean=getTaskNo(context,request);
		Integer task_no= workflowProcessBean.getTask_no();
		Integer step_id=workflowProcessBean.getStep_id();
		Integer task_id=workflowProcessBean.getTask_id();
		//Integer step_id=Integer.valueOf(request.getParameter("workflow_stepid"));
		
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
			  log.info("task_id--"+task_id+"--task_no--"+task_no+"--step_id--"+step_id);
				//preparedStatement=context.getDBConnection().prepareStatement(" update task_master set status=? ,task_start=?  where task_id=? ");
			  preparedStatement=context.getDBConnection().prepareStatement(" update task_master set task_start=? where task_no=? and step_id=? ");
		        preparedStatement.setString(1,"open");		       
		        preparedStatement.setInt(2,task_no);		        
		        preparedStatement.setInt(3,step_id);
		        rowId=preparedStatement.executeUpdate();	
		        log.info(" open Task:-----------"+rowId);
		  } 
			 catch (Exception ex) {
				 log.info("Error in openTask:-------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	
	public Integer openTaskByStep(Context context,HttpServletRequest request) throws SQLException
	{
		Integer task_id=Integer.valueOf(request.getParameter("task_id"));
		EPerson e=context.getCurrentUser();		
		Integer task_no= 0;
		Integer step_id=0;
		int count=0;
		
	   Integer s_id=Integer.valueOf(request.getParameter("workflow_stepid"));
		   
		List<WorkflowProcessBean> list=getStepTaskDetails(context,s_id);
		log.info("openTaskByStep list size:---------------"+list.size());
		for(WorkflowProcessBean WorkflowProcessBean:list){
			if(count==0){
			task_no= WorkflowProcessBean.getTask_no();
			step_id=WorkflowProcessBean.getWorkflow_stepid();
			log.info("for each loop :----------------"+step_id+"------------------"+task_no);
			}
			count++;
		 }
		
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
			  log.info("task_id--"+task_id+"--task_no--"+task_no+"--step_id--"+step_id);
				//preparedStatement=context.getDBConnection().prepareStatement(" update task_master set status=? ,task_start=?  where task_id=? ");
			  preparedStatement=context.getDBConnection().prepareStatement(" update task_master set task_start=? where task_no=? and step_id=? ");
		        preparedStatement.setString(1,"open");		       
		        preparedStatement.setInt(2,task_no);		        
		        preparedStatement.setInt(3,step_id);
		        rowId=preparedStatement.executeUpdate();	
		        log.info(" open Task:-----------"+rowId);
		  } 
			 catch (Exception ex) {
				 log.info("Error in openTask:-------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	public List<WorkflowProcessBean> getStepTaskDetails(Context context,Integer s_id)throws SQLException{
		List<WorkflowProcessBean> taskList=new ArrayList<WorkflowProcessBean>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			ps=context.getDBConnection().prepareStatement(" select step_id,task_no,task_id,task_start from task_master where task_start='close' and step_id!="+s_id+" order by step_id ");		
			rs=ps.executeQuery();
			
			while(rs.next())
			{
			workflowProcessBean=new WorkflowProcessBean();
			workflowProcessBean.setWorkflow_stepid(rs.getInt(1));
			workflowProcessBean.setTask_no(rs.getInt(2));
			workflowProcessBean.setTask_id(rs.getInt(3));			
			taskList.add(workflowProcessBean);
			}
			
		}catch(SQLException sqe){
			log.info("Error in getting task comment list:--------------"+sqe);
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
		
		return taskList;
	}
	
	public Boolean isCheckTaskClose(Context context,Integer step_id) throws SQLException
	{
		EPerson e=context.getCurrentUser();
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  boolean isClose=false;
		  try 
			 {
			  preparedStatement=context.getDBConnection().prepareStatement(" select task_id,task_start,status from task_master where step_id="+step_id+" order by task_start desc ");
			  rs=preparedStatement.executeQuery();
			  while(rs.next())
			  {
				  log.info("task_start:--------------------"+rs.getString("task_start")+"--"+rs.getString("status"));
				  
				  if(rs.getString("task_start").equals("open") && rs.getString("status").equals("A")){
					  isClose=true; 
				  }else{
					  isClose=false;
				  }
				//  rowId=rs.getInt(1);
			  }
			  
		  } 
			 catch (Exception ex) {
				 log.info("Error in update task role:-------------"+ex.getMessage());
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
		 	
		  log.info("rowId role:-------------"+isClose);
		  return isClose;
	}
	
	public WorkflowProcessBean getTaskNo(Context context,HttpServletRequest request) throws SQLException
	{
		WorkflowProcessBean workflowProcessBean=new WorkflowProcessBean();
		Integer workflow_id=Integer.valueOf(request.getParameter("workflow_id"));
		//Integer stepid=Integer.valueOf(request.getParameter("workflow_stepid"));	
		//EPerson e=context.getCurrentUser();
		log.info("get task no workflow_id:----------"+workflow_id);
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0,count=0;
		  try 
			 {
			  String query=" select distinct tm.workflow_id,tm.step_id,tm.task_id,tm.task_no from task_master tm right join task_role tr on tm.task_id=tr.task_id "
			  		+ " where tm.task_start='close' and workflow_id="+workflow_id+"  order by tm.workflow_id,tm.step_id,tm.task_no ";			  
			  preparedStatement=context.getDBConnection().prepareStatement(query);
			  rs=preparedStatement.executeQuery();
				log.info("count:---"+count);
			  while(rs.next()){
				 // workflowProcessBean=new WorkflowProcessBean();
				  if(count==0){
				  log.info("task_no:---"+rs.getInt("task_no"));
				  log.info("step_id:---"+rs.getInt("step_id"));
				  workflowProcessBean.setTask_no(rs.getInt("task_no"));
				  workflowProcessBean.setTask_id(rs.getInt("task_id"));
				  workflowProcessBean.setStep_id(rs.getInt("step_id"));
				  workflowProcessBean.setWorkflow_id(rs.getInt("workflow_id"));
				  count++;
			  }
			}
		        log.info("get task no:-----------"+rowId);
		  } 
			 catch (Exception ex) {
				 log.info("Error in update task role:-------------"+ex.getMessage());
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
		  
		  return workflowProcessBean;
	}
	
	public Integer readDocument(Context context,HttpServletRequest request) throws SQLException
	{
		Integer process_id=Integer.valueOf(request.getParameter("process_id"));
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
				preparedStatement=context.getDBConnection().prepareStatement(" update workflow_process set is_read=?  where process_id=? ");
		        preparedStatement.setInt(1,1);
		        preparedStatement.setInt(2,process_id);
		        rowId=preparedStatement.executeUpdate();
		        log.info("read Document:-----------"+rowId);
		  } 
			 catch (Exception ex) {
				 log.info("Error in read document------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	
	public Integer approveDocument(Context context,HttpServletRequest request) throws SQLException
	{
		String status=request.getParameter("status");
		Integer work_process_id=Integer.valueOf(request.getParameter("process_id"));
		Integer task_id=Integer.parseInt(request.getParameter("task_id"));
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
		  try 
			 {
				preparedStatement=context.getDBConnection().prepareStatement(" update workflow_process set status=?  where task_id=? and work_process_id=? ");
		        preparedStatement.setString(1,status);		       
		        preparedStatement.setInt(2,task_id);
		        preparedStatement.setInt(3,work_process_id);
		        rowId=preparedStatement.executeUpdate();
		        log.info("approve Document:-----------"+rowId);
		  } 
			 catch (Exception ex) {
				 log.info("Error in update task master:-------------"+ex.getMessage());
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
		  
		  return rowId;
	}
	
	public Integer updateProcessTask(Context context,HttpServletRequest request) throws SQLException
	{
		String st=request.getParameter("status");
		String comment=request.getParameter("task_comment");
		Integer pid=Integer.valueOf(request.getParameter("process_id"));
		PreparedStatement preparedStatement=null;
		 ResultSet rs = null;
		  Integer rowId=0;
			 try 
			 {
		        preparedStatement=context.getDBConnection().prepareStatement(" update workprocess_role set status=? ,task_comment=? where work_process_id=? ");
		        preparedStatement.setString(1,st);
		        preparedStatement.setString(2,comment.trim());
		        preparedStatement.setInt(3,pid);
		        rowId=preparedStatement.executeUpdate();
			 } 
			 catch (Exception ex) {
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
			 
		return rowId;
	}

	public Integer getSupervisorDetails(Context context)throws SQLException{
		PreparedStatement ps=null;
		ResultSet rs=null;
	     Integer userid=0,totId=0;
	  	EPerson e=context.getCurrentUser();
	  	userid=e.getID(); 
		try{
			ps=context.getDBConnection().prepareStatement(" Select count(task_master.supervisors_id) as tot_id from task_master,"
					+ " eperson  where eperson.eperson_id=task_master.supervisors_id and task_master.supervisors_id="+userid+" ");		
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				totId=rs.getInt(1);
			}
			
		log.info("Error in getting task comment list:--------------"+totId);
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
		
		return totId;
	}

	public Boolean getWorkflowMode(Context context,Integer bitstremid)throws SQLException{
		PreparedStatement ps=null;
		ResultSet rs=null;
		Boolean isWorkflowMode=false;
	     Integer userid=0,totId=0;
	  	EPerson e=context.getCurrentUser();
	  	userid=e.getID(); 
		try{
			ps=context.getDBConnection().prepareStatement("select count(workflow_process.bitstream_id) from workflow_process,bitstream where "
					+ " workflow_process.bitstream_id=bitstream.bitstream_id and  workflow_process.bitstream_id="+bitstremid+" and status='P' ;");		
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				totId=rs.getInt(1);
			}
			
			if(totId>0){
				isWorkflowMode=true;
			}
			
		log.info("Error in getting getWorkflowModet:--------------"+totId+"--"+isWorkflowMode);
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
		
		return isWorkflowMode;
	}
	
	public Boolean getItemWorkflowMode(Context context,Integer item_id)throws SQLException{
		PreparedStatement ps=null;
		ResultSet rs=null;
		Boolean isWorkflowMode=false;
	     Integer userid=0,totId=0;
	  	EPerson e=context.getCurrentUser();
	  	userid=e.getID(); 
		try{
			ps=context.getDBConnection().prepareStatement(" select workflow_process.item_id from workflow_process,item where"
					+ "  workflow_process.item_id=item.item_id and  workflow_process.item_id="+item_id+" and status='P' ;;");		
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				totId=rs.getInt(1);
			}
			
			if(totId>0){
				isWorkflowMode=true;
			}
			
		log.info("Error in getting getItemWorkflowMode:--------------"+totId+"--"+isWorkflowMode);
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
		
		return isWorkflowMode;
	}
	
	public List<WorkflowProcessBean> getDocumentList(Context context,Integer workflow_id) throws SQLException{
		List<WorkflowProcessBean> docList=new ArrayList<>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			ps=context.getDBConnection().prepareStatement(" select document_name,file_path from workflow_process where workflow_id="+workflow_id+" order by document_name ");		
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
	
public void setStepXml(HttpServletResponse response, Integer id) {
	try {
		StringBuilder content = new StringBuilder();
		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		content.append("<main>");
		if(id>0){
			content.append("<message>"+"Workflow has been successfully sent."+"</message>");
        	}else{
        		content.append("<message>"+"Workflow has not been successfully sent."+"</message>");
        	}
		content.append("</main>");
		response.getWriter().write(content.toString());
	} catch (Exception e) {
		e.printStackTrace();
		log.error("Exception in creating xml:" + e.getMessage());
	}
	
}

private String convert(String str) {
	str = "<![CDATA[" + str + "]]>";
	return str;
}

 public static void main(String[] args) throws SQLException {
	Context context=new Context();
	new WorkflowProcessManager().getGenerateId(context);
}
}
