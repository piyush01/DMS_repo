package org.dspace.workflowmanager;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.util.SequenceGenerateManager;

public class WorkflowManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 124567568L;
	private WorkflowMasterBean workflow = new WorkflowMasterBean();
	private static Logger log = Logger.getLogger(WorkflowManager.class);

	public WorkflowMasterBean getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowMasterBean workflow) {
		this.workflow = workflow;
	}

	public WorkflowManager() {
		super();
	}

	public Integer addWorkflow(Context context, HttpServletRequest request)
			throws SQLException {
		int nextId = 0, rowId = 0;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String workflowName = request.getParameter("workflow_name");
		String workflow_des = request.getParameter("workflow_description");
		EPerson eperson = context.getCurrentUser();
		int userid = eperson.getID();
		if (workflowName != null && !workflowName.equals("")) {
			try {
				nextId = SequenceGenerateManager.getGeneratedId(context,
						"workflow_seq");
				workflow.setWorkflow_id(nextId);
				workflow.setWorkflow_name(workflowName);
				workflow.setUpdate_date(new Date());
				preparedStatement = context
						.getDBConnection()
						.prepareStatement(
								" insert into workflow_master(workflow_id, workflow_name, update_date, status, workflow_description,user_id) values (?, ?, ?, ?, ? ,?)");
				preparedStatement.setInt(1, workflow.getWorkflow_id());
				preparedStatement.setString(2, workflow.getWorkflow_name());
				preparedStatement.setDate(3, new java.sql.Date(workflow
						.getUpdate_date().getTime()));
				preparedStatement.setString(4, "A");
				preparedStatement.setString(5, workflow_des.trim());
				preparedStatement.setInt(6, userid);
				rowId = preparedStatement.executeUpdate();

			} catch (Exception e) {
				log.info("Error in add work flow:-------------"
						+ e.getMessage());
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqle) {
					}
				}
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException sqle) {
						log.error(
								"SQL doInsertPostgres statement close Error - ",
								sqle);
						throw sqle;
					}
				}
			}
		}

		return rowId;
	}

	public List<WorkflowMasterBean> getAllWorkflow(Context context)
			throws SQLException {
		List<WorkflowMasterBean> list = new ArrayList<WorkflowMasterBean>();
		ResultSet rs = null;
		Statement statment = null;
		try {
			statment = context.getDBConnection().createStatement();
			rs = statment
					.executeQuery("Select workflow_id,workflow_name,workflow_description from workflow_master where status='A' order by workflow_id ");
			while (rs.next()) {
				WorkflowMasterBean workflowMasterBean = new WorkflowMasterBean();
				workflowMasterBean.setWorkflow_id(rs.getInt("workflow_id"));
				workflowMasterBean.setWorkflow_name(rs
						.getString("workflow_name"));
				workflowMasterBean.setWorkflow_description(rs
						.getString("workflow_description"));
				list.add(workflowMasterBean);
			}
		} catch (SQLException ex) {
			log.info("Error in get all workflow list:=========>" + ex);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
			}
			if (statment != null) {
				try {
					statment.close();
				} catch (SQLException sqle) {
					log.error("SQL doInsertPostgres statement close Error - ",
							sqle);
					throw sqle;
				}
			}
		}
		return list;
	}

	public List<WorkflowMasterBean> getNoAllWorkflow(Context context,
			HttpServletRequest request) throws SQLException {
		List<WorkflowMasterBean> list = new ArrayList<WorkflowMasterBean>();
		int id = 0;
		id = Integer.valueOf(request.getParameter("workflowId"));
		ResultSet rs = null;
		Statement statment = null;
		try {
			statment = context.getDBConnection().createStatement();
			rs = statment
					.executeQuery("Select workflow_id,workflow_name,workflow_description from workflow_master where status='A' and workflow_id!="
							+ id + " ");
			while (rs.next()) {
				WorkflowMasterBean workflowMasterBean = new WorkflowMasterBean();
				workflowMasterBean.setWorkflow_id(rs.getInt("workflow_id"));
				workflowMasterBean.setWorkflow_name(rs
						.getString("workflow_name"));
				workflowMasterBean.setWorkflow_description(rs
						.getString("workflow_description"));
				list.add(workflowMasterBean);
			}
		} catch (SQLException ex) {
			log.info("Error in get all workflow list:=========>" + ex);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
			}
			if (statment != null) {
				try {
					statment.close();
				} catch (SQLException sqle) {
					log.error("SQL doInsertPostgres statement close Error - ",
							sqle);
					throw sqle;
				}
			}
		}
		return list;
	}

	public Boolean deleteWorkflow(Context context, HttpServletRequest request)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		int id = Integer.valueOf(request.getParameter("workflowId"));
		boolean isUpdate = false;
		if (id != 0 && id > 0) {
			try {
				preparedStatement = context
						.getDBConnection()
						.prepareStatement(
								"update workflow_master set status=? where workflow_id=? ");
				preparedStatement.setString(1, "P");
				preparedStatement.setInt(2, id);
				int i = preparedStatement.executeUpdate();
				if (i > 0) {
					isUpdate = true;
				}

			} catch (SQLException e) {
				log.info("Error in delete workflow========>" + e);
			}

			finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException sqle) {
						log.error(
								"SQL doInsertPostgres statement close Error - ",
								sqle);
						throw sqle;
					}
				}
			}
		}
		return isUpdate;
	}

	public Boolean updateWorkflow(Context context, HttpServletRequest request)
			throws SQLException {
		PreparedStatement ps = null;
		boolean isUpdate = false;
		int id = Integer.valueOf(request.getParameter("workflowId"));
		String workflowName = request.getParameter("workflow_name");
		String workflow_des = request.getParameter("workflow_description");
		if (workflowName != null && !workflowName.equals("")) {
			try {
				ps = context
						.getDBConnection()
						.prepareStatement(
								"update workflow_master set workflow_name=? ,workflow_description=? where workflow_id=?");
				ps.setString(1, workflowName);
				ps.setString(2, workflow_des.trim());
				ps.setInt(3, id);
				int i = ps.executeUpdate();
				if (i > 0) {
					isUpdate = true;
				}
			} catch (SQLException e) {
				log.info("Error in update workflow==============>" + e);

			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException sqle) {
						log.error(
								"SQL doInsertPostgres statement close Error - ",
								sqle);
						throw sqle;
					}
				}
			}
		}
		return isUpdate;
	}

	public WorkflowMasterBean getWorkflowById(Context context,
			HttpServletRequest request) throws SQLException {
		WorkflowMasterBean workflowMasterBean = new WorkflowMasterBean();
		int id = Integer.valueOf(request.getParameter("workflowId"));
		Statement statment = null;
		ResultSet rs = null;
		try {
			statment = context.getDBConnection().createStatement();
			rs = statment
					.executeQuery("Select workflow_id,workflow_name,workflow_description from workflow_master where status='A' and workflow_id="
							+ id + " ");
			while (rs.next()) {
				workflowMasterBean.setWorkflow_id(rs.getInt("workflow_id"));
				workflowMasterBean.setWorkflow_name(rs
						.getString("workflow_name"));
				workflowMasterBean.setWorkflow_description(rs
						.getString("workflow_description"));
			}
		} catch (Exception e) {
			log.info("Error in get workflow by id===========>" + e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqle) {
					log.error("SQL doInsertPostgres statement close Error - ",
							sqle);
					throw sqle;
				}
			}
			if (statment != null) {
				try {
					statment.close();
				} catch (SQLException sqle) {
					log.error("SQL doInsertPostgres statement close Error - ",
							sqle);
					throw sqle;
				}
			}
		}
		return workflowMasterBean;
	}

	public static void main(String[] args) {
		int i = 0;

		WorkflowManager workflowManager = new WorkflowManager();
		// i=workflowManager.getNewId();
		// workflowManager.updateWorkflow();
		System.out.println(i + "==================");
	}
}
