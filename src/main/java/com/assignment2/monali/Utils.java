package com.assignment2.monali;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.assignment2.monali.models.LoggedInUser;

public class Utils {

	public static HttpSession getSession() {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		if(currentInstance == null) {
			return null;
		}
		
		return (HttpSession) currentInstance.getExternalContext().getSession(true);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static LoggedInUser getLoggedInUser() {
		return (LoggedInUser) getSession().getAttribute("loggedInUser");
	}

	public static void setLoggedInUser(LoggedInUser loggedInUser) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.setAttribute("loggedInUser", loggedInUser);
	}
	
	public static void logout() {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
}
