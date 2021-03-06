package com.assignment2.monali.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@ManagedBean(name = "loginFormBean")
@RequestScoped
public class LoginFormBean implements Serializable {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
