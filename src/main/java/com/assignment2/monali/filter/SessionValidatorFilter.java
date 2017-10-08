package com.assignment2.monali.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.assignment2.monali.Utils;
import com.assignment2.monali.models.LoggedInUser;

@WebFilter(filterName = "sessionValidatorFilter", urlPatterns = { "*.xhtml" })
public class SessionValidatorFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		filter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void filter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final LoggedInUser loggedInUser = (LoggedInUser) request.getSession().getAttribute("loggedInUser");
		final boolean loginRequest = isLoginRequest(request);
		final boolean registrationRequest = isRegistrationRequest(request);

		if (loggedInUser != null && (loginRequest || registrationRequest)) {
			response.sendRedirect(request.getServletContext().getContextPath() + "/faces/result.xhtml");
		}

		if (loggedInUser == null && !(loginRequest || registrationRequest)) {
			response.sendRedirect(request.getServletContext().getContextPath() + "/faces/login.xhtml");
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isLoginRequest(HttpServletRequest request) {
		return request.getRequestURI().equals(request.getServletContext().getContextPath() + "/faces/login.xhtml");
	}

	private boolean isRegistrationRequest(HttpServletRequest request) {
		return request.getRequestURI()
				.equals(request.getServletContext().getContextPath() + "/faces/registration.xhtml");
	}
}
