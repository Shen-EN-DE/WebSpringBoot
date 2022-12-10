package com.example.demo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
	
	private Integer userIdInteger;
	private String email;
	
	@JsonIgnore
	private String password;
	private Date createdDate;
	private Date lastModifiedDate;
	public Integer getUserIdInteger() {
		return userIdInteger;
	}
	public void setUserIdInteger(Integer userIdInteger) {
		this.userIdInteger = userIdInteger;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
	

}
