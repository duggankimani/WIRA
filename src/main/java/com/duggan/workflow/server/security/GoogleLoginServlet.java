package com.duggan.workflow.server.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Singleton;

@Singleton
public class GoogleLoginServlet extends GoogleLoginCallbackServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}

	@Override
	protected void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		try {
			JSONObject json = new JSONObject();
			json.put("GOOGLE_CLIENT_STATUS", "ACTIVE");
			resp.getWriter().write(json.toString());
			resp.setContentType("application/json");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
