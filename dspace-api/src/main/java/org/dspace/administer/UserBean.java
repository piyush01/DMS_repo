package org.dspace.administer;

public class UserBean {

	
	private int user_id;
	private String email;
	private boolean can_log_in;
	private boolean self_registered;
	private String first_name;
	private String last_name;
	private String mobileno;
	private String password;
	private String language;
	private String superiorname;
	private String superioremail;
	private String userdesignation;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSuperiorname() {
		return superiorname;
	}
	public void setSuperiorname(String superiorname) {
		this.superiorname = superiorname;
	}
	public String getSuperioremail() {
		return superioremail;
	}
	public void setSuperioremail(String superioremail) {
		this.superioremail = superioremail;
	}
	public String getUserdesignation() {
		return userdesignation;
	}
	public void setUserdesignation(String userdesignation) {
		this.userdesignation = userdesignation;
	}
	public UserBean()
	{
		
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isCan_log_in() {
		return can_log_in;
	}
	public void setCan_log_in(boolean can_log_in) {
		this.can_log_in = can_log_in;
	}
	public boolean isSelf_registered() {
		return self_registered;
	}
	public void setSelf_registered(boolean self_registered) {
		this.self_registered = self_registered;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
}
