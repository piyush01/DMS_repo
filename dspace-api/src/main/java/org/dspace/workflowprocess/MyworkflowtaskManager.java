package org.dspace.workflowprocess;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dspace.core.Context;

public class MyworkflowtaskManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12345345345L;
	private WorkflowProcessBean workflowProcessBean=new WorkflowProcessBean();
	private static Logger log = Logger.getLogger(MyworkflowtaskManager.class);
	public WorkflowProcessBean getWorkflowProcessBean() {
		return workflowProcessBean;
	}

	public void setWorkflowProcessBean(WorkflowProcessBean workflowProcessBean) {
		this.workflowProcessBean = workflowProcessBean;
	}

	public List<WorkflowProcessBean> getMyTaskDetais(Context context,Integer user_id){
		log.info("user_id:------------------"+user_id);
		List<WorkflowProcessBean> mytaskList=new ArrayList<>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		try{
			ps=context.getDBConnection().prepareStatement(" select wm.workflow_name,tm.task_id,sm.step_name,tm.task_name,tm.task_no,tm.deadline_time,"
			+" tm.task_instruction,tm.add_date,tr.deadline_day,tr.status,tm.task_start,wm.workflow_id from task_master tm right  join task_role tr on tr.task_id=tm.task_id " 
			+" right  join step_master sm on sm.step_id=tm.step_id right  join workflow_master wm on wm.workflow_id=tm.workflow_id "
			+" where tr.task_owner_id="+user_id+" and tm.is_deleted='NO' order by task_no ");
			rs=ps.executeQuery();
			
			while(rs.next())
			{
			 log.info("data:---------"+rs.getString(1)+""+rs.getInt(2)+"---"+rs.getString(3)+"--"+rs.getString(4));	
			 log.info("data:---------"+rs.getDate(8));	
			 workflowProcessBean=new WorkflowProcessBean();
			 workflowProcessBean.setWorkflow_name(rs.getString(1));
			 workflowProcessBean.setTask_id(rs.getInt(2));
			 workflowProcessBean.setStep_name(rs.getString(3));
			 workflowProcessBean.setTask_name(rs.getString(4));
			 workflowProcessBean.setTask_no(rs.getInt(5));
			 workflowProcessBean.setDeadline_time(rs.getString(6));
			 workflowProcessBean.setTask_instruction(rs.getString(7));
			 workflowProcessBean.setAdate(sdf.format(rs.getDate(8)));
			 workflowProcessBean.setDue_Date(sdf.format(rs.getDate(9)));
			 workflowProcessBean.setStatus(rs.getString(10));
			 workflowProcessBean.setTask_start(rs.getString(11));
			 workflowProcessBean.setWorkflow_id(rs.getInt(12));
			 mytaskList.add(workflowProcessBean);
			}
		}
		catch(Exception ex){
		log.info("Getting getMyTaskDetais :------------------"+ex);
		}
		
		return mytaskList;
	}
	
	public List<WorkflowProcessBean> getMyDocumentList(Context context,Integer workflow_id){
		log.info("task_id:------------------"+workflow_id);
		List<WorkflowProcessBean> mydocList=new ArrayList<>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		try{
			ps=context.getDBConnection().prepareStatement(" select distinct wp.process_id,wp.work_process_id,wp.document_name,wp.file_path,wr.user_id,date(wr.add_date) "
					+ " as add_date,wp.is_read from workflow_process wp right join workprocess_role wr on wr.work_process_id=wp.work_process_id where wp.workflow_id="+workflow_id+" order by wp.document_name ");
			rs=ps.executeQuery();
			
			while(rs.next())
			{
			 workflowProcessBean=new WorkflowProcessBean();
			 workflowProcessBean.setProcess_id(rs.getInt(1));
			 workflowProcessBean.setWork_process_id(rs.getInt(2));
			 workflowProcessBean.setDocument_name(rs.getString(3));
			 workflowProcessBean.setFile_path(rs.getString(4));
			 workflowProcessBean.setUser_id(rs.getInt(5));
			 workflowProcessBean.setAdate(sdf.format(rs.getDate(6)));
			 workflowProcessBean.setIs_read(rs.getInt(7));
			 mydocList.add(workflowProcessBean);
			}
		}
		catch(Exception ex){
			log.error("Getting getMyDocumentList :------------------"+ex);
		}
		return mydocList;
	}
	
	public List<WorkflowProcessBean> getSupervisorTaskDetais(Context context,Integer user_id){
		log.info("user_id:------------------"+user_id);
		List<WorkflowProcessBean> mytaskList=new ArrayList<>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		try{
			ps=context.getDBConnection().prepareStatement(" select wm.workflow_name,tm.task_id,sm.step_name,tm.task_name,tm.task_no,tm.deadline_time,"
					+ " tm.task_instruction,tm.add_date,tm.deadline_day,tr.status,tm.task_start,wm.workflow_id from task_master tm	right  join task_role tr on tr.task_id=tm.task_id "
			        + " right  join step_master sm on sm.step_id=tm.step_id right  join workflow_master wm on wm.workflow_id=tm.workflow_id "
			        + " where tm.supervisors_id="+user_id+" order by task_no ");
			rs=ps.executeQuery();
			
			while(rs.next())
			{
			 log.info("data:---------"+rs.getString(1)+""+rs.getInt(2)+"---"+rs.getString(3)+"--"+rs.getString(4));	
			 log.info("data:---------"+rs.getDate(8));	
			 workflowProcessBean=new WorkflowProcessBean();
			 workflowProcessBean.setWorkflow_name(rs.getString(1));
			 workflowProcessBean.setTask_id(rs.getInt(2));
			 workflowProcessBean.setStep_name(rs.getString(3));
			 workflowProcessBean.setTask_name(rs.getString(4));
			 workflowProcessBean.setTask_no(rs.getInt(5));
			 workflowProcessBean.setDeadline_time(rs.getString(6));
			 workflowProcessBean.setTask_instruction(rs.getString(7));
			 workflowProcessBean.setAdate(sdf.format(rs.getDate(8)));
			 workflowProcessBean.setDue_Date(sdf.format(rs.getDate(9)));
			 workflowProcessBean.setStatus(rs.getString(10));
			 workflowProcessBean.setTask_start(rs.getString(11));
			 workflowProcessBean.setWorkflow_id(rs.getInt(12));
			 mytaskList.add(workflowProcessBean);
			}
		}
		catch(Exception ex){
		log.info("Getting getMyTaskDetais :------------------"+ex);
		}
		
		return mytaskList;
	}
}
