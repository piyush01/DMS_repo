package org.dspace.workflowmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class TaskMasterBean  implements Comparable<TaskMasterBean>
{
	private static Logger log = Logger.getLogger(TaskMasterBean.class);
private Integer task_id;
private Integer workflow_id;
private Integer step_id;
private String task_owner_id;
private Integer step_no;
private String task_name;
private Integer task_rule_id;
private String priorty;
private Date deadline_day;
private String deadline_time;
private String task_instructions;
private Integer supervisor_id;
private String task_permission_id;
private String task_requirment_id;
private String status;
private Date add_date;
private Date update_date;
private Integer task_no;
private String date;
private Integer user_id;
private String user_name;
private Integer task_user_id;
private String timemode;

private String task_comment;
private String postpone_task_comment;
private String change_taskfinished_comments;
private String senior_level_task_comment;

private List<String> reqList;
private List<String> perList;

private String r1;
private String r2;
private String r3;

private String p1;
private String p2;
private String p3;
private String p4;


public String getR1() {
	return r1;
}
public void setR1(String r1) {
	this.r1 = r1;
}
public String getR2() {
	return r2;
}
public void setR2(String r2) {
	this.r2 = r2;
}
public String getR3() {
	return r3;
}
public void setR3(String r3) {
	this.r3 = r3;
}
public String getP1() {
	return p1;
}
public void setP1(String p1) {
	this.p1 = p1;
}
public String getP2() {
	return p2;
}
public void setP2(String p2) {
	this.p2 = p2;
}
public String getP3() {
	return p3;
}
public void setP3(String p3) {
	this.p3 = p3;
}
public String getP4() {
	return p4;
}
public void setP4(String p4) {
	this.p4 = p4;
}
public String getTimemode() {
	return timemode;
}
public void setTimemode(String timemode) {
	this.timemode = timemode;
}
public List<String> getReqList() {
	return reqList;
}
public void setReqList(List<String> reqList) {
	this.reqList = reqList;
}
public List<String> getPerList() {
	return perList;
}
public void setPerList(List<String> perList) {
	this.perList = perList;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}

private List<TaskMasterBean> userList;


public List<TaskMasterBean> getUserList() {
	return userList;
}
public void setUserList(List<TaskMasterBean> userList) {
	this.userList = userList;
}
public String getTask_comment() {
	return task_comment;
}
public void setTask_comment(String task_comment) {
	this.task_comment = task_comment;
}
public String getPostpone_task_comment() {
	return postpone_task_comment;
}
public void setPostpone_task_comment(String postpone_task_comment) {
	this.postpone_task_comment = postpone_task_comment;
}
public String getChange_taskfinished_comments() {
	return change_taskfinished_comments;
}
public void setChange_taskfinished_comments(String change_taskfinished_comments) {
	this.change_taskfinished_comments = change_taskfinished_comments;
}
public String getSenior_level_task_comment() {
	return senior_level_task_comment;
}
public void setSenior_level_task_comment(String senior_level_task_comment) {
	this.senior_level_task_comment = senior_level_task_comment;
}
public Integer getTask_user_id() {
	return task_user_id;
}
public void setTask_user_id(Integer task_user_id) {
	this.task_user_id = task_user_id;
}

private String workflow_step;

public String getWorkflow_step() {
	return workflow_step;
}
public void setWorkflow_step(String workflow_step) {
	this.workflow_step = workflow_step;
}
public Integer getUser_id() {
	return user_id;
}
public void setUser_id(Integer user_id) {
	this.user_id = user_id;
}
public String getUser_name() {
	return user_name;
}
public void setUser_name(String user_name) {
	this.user_name = user_name;
}
public Integer getTask_id() {
	return task_id;
}
public void setTask_id(Integer task_id) {
	this.task_id = task_id;
}
public Integer getWorkflow_id() {
	return workflow_id;
}
public void setWorkflow_id(Integer workflow_id) {
	this.workflow_id = workflow_id;
}
public Integer getStep_id() {
	return step_id;
}
public void setStep_id(Integer step_id) {
	this.step_id = step_id;
}
public String getTask_owner_id() {
	return task_owner_id;
}
public void setTask_owner_id(String task_owner_id) {
	this.task_owner_id = task_owner_id;
}
public Integer getStep_no() {
	return step_no;
}
public void setStep_no(Integer step_no) {
	this.step_no = step_no;
}
public String getTask_name() {
	return task_name;
}
public void setTask_name(String task_name) {
	this.task_name = task_name;
}
public Integer getTask_rule_id() {
	return task_rule_id;
}
public void setTask_rule_id(Integer task_rule_id) {
	this.task_rule_id = task_rule_id;
}
public String getPriorty() {
	return priorty;
}
public void setPriorty(String priorty) {
	this.priorty = priorty;
}

public Date getDeadline_day() {
	return deadline_day;
}
public void setDeadline_day(Date deadline_day) {
	this.deadline_day = deadline_day;
}
public String getDeadline_time() {
	return deadline_time;
}
public void setDeadline_time(String deadline_time) {
	this.deadline_time = deadline_time;
}
public String getTask_instructions() {
	return task_instructions;
}
public void setTask_instructions(String task_instructions) {
	this.task_instructions = task_instructions;
}
public Integer getSupervisor_id() {
	return supervisor_id;
}
public void setSupervisor_id(Integer supervisor_id) {
	this.supervisor_id = supervisor_id;
}
public String getTask_permission_id() {
	return task_permission_id;
}
public void setTask_permission_id(String task_permission_id) {
	this.task_permission_id = task_permission_id;
}
public String getTask_requirment_id() {
	return task_requirment_id;
}
public void setTask_requirment_id(String task_requirment_id) {
	this.task_requirment_id = task_requirment_id;
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
public Date getUpdate_date() {
	return update_date;
}
public void setUpdate_date(Date update_date) {
	this.update_date = update_date;
}
public Integer getTask_no() {
	return task_no;
}
public void setTask_no(Integer task_no) {
	this.task_no = task_no;
}


@Override
public int compareTo(TaskMasterBean taskMasterBean) {
	
	return (this.task_id).compareTo(taskMasterBean.task_id);
}


}
