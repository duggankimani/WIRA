package com.duggan.workflow.server.security;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duggan.workflow.server.security.realm.google.GoogleAuthenticationToken;
import com.google.inject.Singleton;

@Singleton
public class GoogleIdTokenVerifierService extends GoogleLoginCallbackServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}
	
	@Override
	protected void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String idTokenString = req.getReader().readLine();
		
		GoogleAuthenticationToken token = new GoogleAuthenticationToken();
		token.setIdToken(idTokenString);

		registerAndLoginUser(token, req, resp);
	}
}
