package org.dspace.administer;

public class DataTypesBean {

	private int datatype_id;
	private String field_name;
	private String datatype;
	private String displayvalue;
	private String storedvalue;
	
	
	public int getDatatype_id() {
		return datatype_id;
	}
	public void setDatatype_id(int datatype_id) {
		this.datatype_id = datatype_id;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getDisplayvalue() {
		return displayvalue;
	}
	public void setDisplayvalue(String displayvalue) {
		this.displayvalue = displayvalue;
	}
	public String getStoredvalue() {
		return storedvalue;
	}
	public void setStoredvalue(String storedvalue) {
		this.storedvalue = storedvalue;
	}
	
	
}
