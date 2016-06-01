package org.dspace.workflowmanager;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.util.SequenceGenerateManager;

public class WorkflowStepManager implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1342121464L;
	private static Logger log=Logger.getLogger(WorkflowStepManager.class);
private WorkflowStepBean workflowStepBean=new WorkflowStepBean();
	public WorkflowStepManager() {
		super();
	}

	public WorkflowStepBean getWorkflowStepBean() {
		return workflowStepBean;
	}

	public void setWorkflowStepBean(WorkflowStepBean workflowStepBean) {
		this.workflowStepBean = workflowStepBean;
	}
	
	public Integer addWorkflowStep(Context context,HttpServletRequest request) throws SQLException{
		int id=0;
		int generateid=0;
		generateid=SequenceGenerateManager.getGeneratedId(context,"workflow_step_seq");
		String name=request.getParameter("workflow_step_name");
		String step_des=request.getParameter("step_description");
		int workflowid=Integer.valueOf(request.getParameter("workflowId"));
		int step_no=getTotalStep(context,workflowid);
		EPerson eperson = context.getCurrentUser();
		int userid = eperson.getID();
		
		workflowStepBean.setWorkflow_step_id(generateid);
		workflowStepBean.setWorkflow_id(workflowid);
		workflowStepBean.setWorkflow_step_name(name);
		workflowStepBean.setStatus("A");
		workflowStepBean.setUpdate_date(new Date());
		workflowStepBean.setStep_no(step_no);
		PreparedStatement ps=null;
		if(name!=null && !name.equals("") && workflowid!=0)
		{
		try{
			ps=context.getDBConnection().prepareStatement(" insert into step_master (step_id,workflow_id,step_name,update_date,status,step_no,step_description,user_id) values (?, ?, ?, ? , ?, ?, ?, ?) ");
			ps.setInt(1, workflowStepBean.getWorkflow_step_id());
			ps.setInt(2, workflowStepBean.getWorkflow_id());
			ps.setString(3,workflowStepBean.getWorkflow_step_name());
			ps.setDate(4, new java.sql.Date(workflowStepBean.getUpdate_date().getTime()));
			ps.setString(5, workflowStepBean.getStatus());
			ps.setInt(6, workflowStepBean.getStep_no());
			ps.setString(7, step_des.trim());
			ps.setInt(8, userid);
		   id=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			log.info("Erroe in save workflow step==========>"+e);
		}
		 finally
	     {
			if (ps != null)
	         {
	             try { ps.close(); } catch (SQLException sqle) {
	            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                 throw sqle;
	             }
	         }
	     }
		}
		return id;
	}
	
	public Integer getTotalStep(Context context,Integer workflowid) throws SQLException{
		Statement statment=null;
		ResultSet rs=null;
		Integer totalNo=0;
		try{
				statment=context.getDBConnection().createStatement();
				rs=statment.executeQuery(" select count(*) from step_master where workflow_id="+workflowid+"  ");
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
					log.info("Error in get total workflow step:=========>"+ex);
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
	
	public Boolean updateWorkflowStep(Context context,HttpServletRequest request) throws SQLException{
		 PreparedStatement ps=null;
		 boolean isUpdate=false;
		 int workflow_step_id=Integer.valueOf(request.getParameter("workflow_step_id"));
		 int workflowid=Integer.valueOf(request.getParameter("workflowId"));
		 String workflowName=request.getParameter("workflow_step_name");
		 String step_des=request.getParameter("step_description");
		 if(workflowName!=null && !workflowName.equals("") && workflowid!=0)
			{
		 try{
			 ps=context.getDBConnection().prepareStatement(" update step_master set step_name=? ,workflow_id=?, step_description=? where step_id=?" );
			 ps.setString(1, workflowName);
			 ps.setInt(2, workflowid);
			 ps.setString(3, step_des.trim());
			 ps.setInt(4, workflow_step_id);
			int i= ps.executeUpdate();
			if(i>0){
				isUpdate=true;
			}
		 }catch(SQLException e){
			 log.info("Error in update step master==============>"+e);
			 
		 }
		 finally
	     {
			if (ps != null)
	         {
	             try { ps.close(); } catch (SQLException sqle) {
	            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                 throw sqle;
	             }
	         }
	     }
			}
		 return isUpdate;
	 }
	
	public Boolean deleteWorkflowStep(Context context,HttpServletRequest request) throws SQLException{
		 PreparedStatement ps=null;
		 boolean isUpdate=false;
		 int step_id=Integer.valueOf(request.getParameter("workflow_step_id"));
	
		 try{
			 ps=context.getDBConnection().prepareStatement(" update step_master set status=? where step_id=?" );
			 ps.setString(1, "D");
			 ps.setInt(2, step_id);
			int i= ps.executeUpdate();
			if(i>0){
				isUpdate=true;
			}
		 }catch(SQLException e){
			 log.info("Error in update step master==============>"+e);
			 
		 }
		 finally
	     {
			if (ps != null)
	         {
	             try { ps.close(); } catch (SQLException sqle) {
	            	 log.error("SQL doInsertPostgres statement close Error - ",sqle);
	                 throw sqle;
	             }
	         }
	     }
		 return isUpdate;
	 }
	
	public WorkflowStepBean getWorkflowstepById(Context context,HttpServletRequest request) throws SQLException{
		WorkflowStepBean workflowStepBean=new WorkflowStepBean();
		int id=Integer.valueOf(request.getParameter("workflow_step_id"));
		Statement statment=null;
		ResultSet rs=null;
		try{
				statment=context.getDBConnection().createStatement();
				rs=statment.executeQuery(" Select step_id,step_name,step_description from step_master where status='A' and step_id="+id+"  ");
				while(rs.next()){
				workflowStepBean.setWorkflow_step_id(rs.getInt("step_id"));
				workflowStepBean.setWorkflow_step_name(rs.getString("step_name"));
				workflowStepBean.setStep_description(rs.getString("step_description"));
				}
				}
				catch(SQLException ex){
					log.info("Error in get all workflow step list:=========>"+ex);
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
		return workflowStepBean;
	}
	
		
  public List<WorkflowStepBean> getAllWorkflowstep(Context context,Integer workflow_id) throws SQLException{
	 List<WorkflowStepBean> list=new ArrayList<WorkflowStepBean>();
	 Statement statment=null;
	 ResultSet rs=null;
	 try{
			statment=context.getDBConnection().createStatement();
			rs=statment.executeQuery(" Select workflow_id,step_id,step_name,step_no,step_description from step_master where status='A' and workflow_id="+workflow_id+" order by step_no ");
			while(rs.next()){
			workflowStepBean=new WorkflowStepBean();
			workflowStepBean.setWorkflow_id(rs.getInt("workflow_id"));
			workflowStepBean.setWorkflow_step_id(rs.getInt("step_id"));
			workflowStepBean.setWorkflow_step_name(rs.getString("step_name"));
			workflowStepBean.setStep_no(rs.getInt("step_no"));
			workflowStepBean.setStep_description("step_description");
			 list.add(workflowStepBean);
			}
			}
			catch(SQLException ex){
				System.out.println("-"+ex);
				log.error("Error in get all workflow step list:=========>"+ex);
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
	 
	 return list;
 }
  
  
  public List<WorkflowStepBean> getAllWorkflowstepById(Context context,Integer workflow_id) throws SQLException{
		 List<WorkflowStepBean> list=new ArrayList<WorkflowStepBean>();
		 Statement statment=null;
		 ResultSet rs=null;
		 try{
				 statment=context.getDBConnection().createStatement();
				 rs=statment.executeQuery("Select workflow_id,step_id,step_name,step_description from step_master where workflow_id="+workflow_id+" and status='A' ");
				while(rs.next()){
				WorkflowStepBean workflowStepBean=new WorkflowStepBean();
				workflowStepBean.setWorkflow_id(rs.getInt("workflow_id"));
				workflowStepBean.setWorkflow_step_id(rs.getInt("step_id"));
				workflowStepBean.setWorkflow_step_name(rs.getString("step_name"));
				workflowStepBean.setStep_description("step_description");
				 list.add(workflowStepBean);
				}
				}
				catch(SQLException ex){
					System.out.println("-"+ex);
					log.error("Error in get all workflow list:=========>"+ex);
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
		 return list;
	 }
  
  public List<WorkflowStepBean> getAllstepById(Context context,Integer workflow_id) throws SQLException{
		 List<WorkflowStepBean> list=new ArrayList<WorkflowStepBean>();
		 Statement statment=null;
		 ResultSet rs=null;
		 try{
			 statment=context.getDBConnection().createStatement();
			 rs=statment.executeQuery(" Select sm.step_id,sm.step_name,sm.step_no,tm.status from step_master sm left join task_master tm on sm.step_id=tm.step_id "
				 		+ " where sm.status='A' and sm.workflow_id="+workflow_id+" order by sm.step_no ");
				while(rs.next())
				{
					WorkflowStepBean workflowStepBean=new WorkflowStepBean();
					workflowStepBean.setWorkflow_step_id(rs.getInt("step_id"));
					workflowStepBean.setWorkflow_step_name(rs.getString("step_name"));
					workflowStepBean.setStep_no(rs.getInt("step_no"));
					workflowStepBean.setStatus(rs.getString("status"));
					list.add(workflowStepBean);
				}
				}
				catch(SQLException ex){
					System.out.println("-"+ex);
					log.error("Error in get all workflow list:=========>"+ex);
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
		 return list;
	 }
  
  public void setStepXml(HttpServletResponse response, List stepList) {
		try {
			StringBuilder content = new StringBuilder();
			response.setContentType("text/xml; charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");
			Iterator it=stepList.iterator();
			content.append("<main>");
			
			while (it.hasNext()){
				WorkflowStepBean ib=(WorkflowStepBean)it.next();
				content.append("<id>"+ib.getWorkflow_step_id()+"</id>");
				content.append("<name>"+convert(ib.getWorkflow_step_name())+"</name>");
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
  public static void main(String[] args) {
	  int id=0;
	  WorkflowStepManager workflowStepManager=new WorkflowStepManager();
	  //System.out.println("============>"+workflowStepManager.getWorkflowstepById());
}
}
