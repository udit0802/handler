package com.spring.security.entity;

/**
 * @author B0073698
 *
 */
public class User {

	private String olmId;
	private String name;
	private String mobile;
	private String email;
	private String role;
	private String password;	
	private User addedBy;
	
	
	public User() {
		super();
	}


	public User(String olmId, String name, String mobile, String email, String role, String password, User addedBy) {
		super();
		this.olmId = olmId;
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.role = role;
		this.password = password;
		this.addedBy = addedBy;
	}


	public String getOlmId() {
		return olmId;
	}


	public void setOlmId(String olmId) {
		this.olmId = olmId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public User getAddedBy() {
		return addedBy;
	}


	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}


	@Override
	public String toString() {
		return "User [olmId=" + olmId + ", name=" + name + ", mobile=" + mobile + ", email=" + email + ", role=" + role
				+ ", password=" + password + ", addedBy=" + addedBy + "]";
	}



}
