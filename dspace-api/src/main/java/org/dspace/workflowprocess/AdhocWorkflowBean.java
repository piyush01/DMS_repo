package org.dspace.workflowprocess;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class AdhocWorkflowBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1324324L;
	private Integer process_id;
	private Integer workflow_id;
	private Integer user_id;
	private Integer community_id;
	private Integer collection_id;
	private Integer document_id;
	private String  document_name;
	private String status;
	private Date add_date;
	private Date date;
	private String handle;
	private String file_path;
    private String user_name;
    private String adate;
    private String priorty;
    private String due_Date;
    private String workflow_name;
    private String step_name;
    private String task_name;
    private String task_instruction;
    private String supervisor_name;
    private String assign_to_user;
    private String task_comment;
    private Integer item_id;
    private Map documentList;
    private Integer task_no;
    private Integer task_id;
    private String deadline_time;
    private String task_permisson_id;
    private String task_requirments_id;
    private Integer allStep;
    private Integer edit_comment;
    private Integer postpone_task;
    private Integer change_finished_task;    
    private Integer comment;
    private Integer approve;
    private Integer step_id;
    
	public Integer getProcess_id() {
		return process_id;
	}
	public void setProcess_id(Integer process_id) {
		this.process_id = process_id;
	}
	public Integer getWorkflow_id() {
		return workflow_id;
	}
	public void setWorkflow_id(Integer workflow_id) {
		this.workflow_id = workflow_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getCommunity_id() {
		return community_id;
	}
	public void setCommunity_id(Integer community_id) {
		this.community_id = community_id;
	}
	public Integer getCollection_id() {
		return collection_id;
	}
	public void setCollection_id(Integer collection_id) {
		this.collection_id = collection_id;
	}
	public Integer getDocument_id() {
		return document_id;
	}
	public void setDocument_id(Integer document_id) {
		this.document_id = document_id;
	}
	public String getDocument_name() {
		return document_name;
	}
	public void setDocument_name(String document_name) {
		this.document_name = document_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getAdd_date() {
		return add_date;
	}
	public void setAdd_date(Date add_date) {
		this.add_date = add_date;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAdate() {
		return adate;
	}
	public void setAdate(String adate) {
		this.adate = adate;
	}
	public String getPriorty() {
		return priorty;
	}
	public void setPriorty(String priorty) {
		this.priorty = priorty;
	}
	public String getDue_Date() {
		return due_Date;
	}
	public void setDue_Date(String due_Date) {
		this.due_Date = due_Date;
	}
	public String getWorkflow_name() {
		return workflow_name;
	}
	public void setWorkflow_name(String workflow_name) {
		this.workflow_name = workflow_name;
	}
	public String getStep_name() {
		return step_name;
	}
	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_instruction() {
		return task_instruction;
	}
	public void setTask_instruction(String task_instruction) {
		this.task_instruction = task_instruction;
	}
	public String getSupervisor_name() {
		return supervisor_name;
	}
	public void setSupervisor_name(String supervisor_name) {
		this.supervisor_name = supervisor_name;
	}
	public String getAssign_to_user() {
		return assign_to_user;
	}
	public void setAssign_to_user(String assign_to_user) {
		this.assign_to_user = assign_to_user;
	}
	public String getTask_comment() {
		return task_comment;
	}
	public void setTask_comment(String task_comment) {
		this.task_comment = task_comment;
	}
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public Map getDocumentList() {
		return documentList;
	}
	public void setDocumentList(Map documentList) {
		this.documentList = documentList;
	}
	public Integer getTask_no() {
		return task_no;
	}
	public void setTask_no(Integer task_no) {
		this.task_no = task_no;
	}
	public Integer getTask_id() {
		return task_id;
	}
	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	public String getDeadline_time() {
		return deadline_time;
	}
	public void setDeadline_time(String deadline_time) {
		this.deadline_time = deadline_time;
	}
	public String getTask_permisson_id() {
		return task_permisson_id;
	}
	public void setTask_permisson_id(String task_permisson_id) {
		this.task_permisson_id = task_permisson_id;
	}
	public String getTask_requirments_id() {
		return task_requirments_id;
	}
	public void setTask_requirments_id(String task_requirments_id) {
		this.task_requirments_id = task_requirments_id;
	}
	public Integer getAllStep() {
		return allStep;
	}
	public void setAllStep(Integer allStep) {
		this.allStep = allStep;
	}
	public Integer getEdit_comment() {
		return edit_comment;
	}
	public void setEdit_comment(Integer edit_comment) {
		this.edit_comment = edit_comment;
	}
	public Integer getPostpone_task() {
		return postpone_task;
	}
	public void setPostpone_task(Integer postpone_task) {
		this.postpone_task = postpone_task;
	}
	public Integer getChange_finished_task() {
		return change_finished_task;
	}
	public void setChange_finished_task(Integer change_finished_task) {
		this.change_finished_task = change_finished_task;
	}
	public Integer getComment() {
		return comment;
	}
	public void setComment(Integer comment) {
		this.comment = comment;
	}
	public Integer getApprove() {
		return approve;
	}
	public void setApprove(Integer approve) {
		this.approve = approve;
	}
	public Integer getStep_id() {
		return step_id;
	}
	public void setStep_id(Integer step_id) {
		this.step_id = step_id;
	}
    
    
}
