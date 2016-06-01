package org.dspace.workflowmanager;

import java.io.Serializable;
import java.util.Date;

public class WorkflowStepBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 13243242343L;
	private Integer workflow_step_id;
	private Integer workflow_id;
	private Integer community_id;
	private Integer collection_id;
    private Integer item_id;
	private Integer sub_community_id;
	private Integer eperson_id;
	private String workflow_step_name;
	private Date add_date;
    private Date update_date;
	private String status;
	private Integer step_no;
	private String step_description;
	
	public String getStep_description() {
		return step_description;
	}
	public void setStep_description(String step_description) {
		this.step_description = step_description;
	}
	public Integer getWorkflow_step_id() {
		return workflow_step_id;
	}
	public void setWorkflow_step_id(Integer workflow_step_id) {
		this.workflow_step_id = workflow_step_id;
	}
	public Integer getWorkflow_id() {
		return workflow_id;
	}
	public void setWorkflow_id(Integer workflow_id) {
		this.workflow_id = workflow_id;
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
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public Integer getSub_community_id() {
		return sub_community_id;
	}
	public void setSub_community_id(Integer sub_community_id) {
		this.sub_community_id = sub_community_id;
	}
	public Integer getEperson_id() {
		return eperson_id;
	}
	public void setEperson_id(Integer eperson_id) {
		this.eperson_id = eperson_id;
	}
	public String getWorkflow_step_name() {
		return workflow_step_name;
	}
	public void setWorkflow_step_name(String workflow_step_name) {
		this.workflow_step_name = workflow_step_name;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getStep_no() {
		return step_no;
	}
	public void setStep_no(Integer step_no) {
		this.step_no = step_no;
	} 
	
}
