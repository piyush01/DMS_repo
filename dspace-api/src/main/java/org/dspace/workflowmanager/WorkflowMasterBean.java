package org.dspace.workflowmanager;

import java.util.Date;

public class WorkflowMasterBean{
	
	/**
	 * 
	 */
	private Integer workflow_id;
	private Integer  community_id;
	private Integer  collection_id;
	private Integer  item_id ;
	private Integer  sub_community_id;
	private Integer  eperson_id;
	private String  workflow_name;
	private String workflow_description;
	private Date   add_date;
	private Date   update_date;
	private String status;
	
	public String getWorkflow_description() {
		return workflow_description;
	}
	public void setWorkflow_description(String workflow_description) {
		this.workflow_description = workflow_description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getWorkflow_name() {
		return workflow_name;
	}
	public void setWorkflow_name(String workflow_name) {
		this.workflow_name = workflow_name;
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
	
}
