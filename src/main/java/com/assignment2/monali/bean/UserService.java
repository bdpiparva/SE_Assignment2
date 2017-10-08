package com.assignment2.monali.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.assignment2.monali.Utils;
import com.assignment2.monali.models.LoggedInUser;

@ManagedBean(name = "userService")
@ApplicationScoped
public class UserService {

	public String login(LoginFormBean loginFormBean) throws SQLException {
		Connection connection = Database.getInstance().getConnection();
		try {
			final PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM user WHERE username= ? and password= ?");
			statement.setString(1, loginFormBean.getUsername());
			statement.setString(2, loginFormBean.getPassword());
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				LoggedInUser loggedInUser = new LoggedInUser(result.getString("username"), result.getString("email"),
						result.getString("name"));
				Utils.setLoggedInUser(loggedInUser);

				return "faces/result.xhtml";
			}

			FacesContext.getCurrentInstance().addMessage("loginFrom:username", errorMessage(MessageFormat.format("User {0} does not exist.", loginFormBean.getUsername())));
			return "faces/login.xhtml";
		} finally {
			Database.closeSilently(connection);
		}
	}

	public boolean isEmailExists(String email) throws SQLException {
		Connection connection = Database.getInstance().getConnection();
		try {
			final PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE email= ?");
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();

			return result.next();
		} finally {
			Database.closeSilently(connection);
		}
	}

	public String register(UserRegistrationBean userRegistrationBean) throws SQLException {
		try {
			if (isEmailExists(userRegistrationBean.getEmail())) {
				String message = MessageFormat.format("Email `{0}` is already taken.", userRegistrationBean.getEmail());
				FacesContext.getCurrentInstance().addMessage("userRegistrationBean:email", errorMessage(message));
				return "registration.xhtml";
			}

			Connection connection = Database.getInstance().getConnection();

			final PreparedStatement statement = connection
					.prepareStatement("INSERT INTO user (username, name, email, password) VALUES(?,?,?,?)");
			statement.setString(1, userRegistrationBean.getUsername());
			statement.setString(2, userRegistrationBean.getName());
			statement.setString(3, userRegistrationBean.getEmail());
			statement.setString(4, userRegistrationBean.getPassword());

			long result = statement.executeLargeUpdate();

			if (result == 1) {
				return "faces/login.xhtml";
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			String message = MessageFormat.format("Username `{0}` is already exist in database",
					userRegistrationBean.getUsername());
			FacesContext.getCurrentInstance().addMessage("userRegistrationBean:username", errorMessage(message));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("userRegistrationBean:username", errorMessage(e.getMessage()));
		}
		return "faces/registration.xhtml";
	}

	private FacesMessage errorMessage(String message) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		facesMessage.setSummary(message);
		facesMessage.setDetail(message);
		return facesMessage;
	}

	public String logout() {
		Utils.logout();
		return "faces/login.xhtml";
	}
}
