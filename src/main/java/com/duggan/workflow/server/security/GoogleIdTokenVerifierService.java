package com.duggan.workflow.server.security;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duggan.workflow.server.helper.session.SessionHelper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

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
		if (clientSecrets == null) {
			resp.setStatus(404);
			resp.getWriter().write("No Client Secrets File Found");
			return;
		}

		String CLIENT_ID = clientSecrets.getDetails().getClientId();
		String idTokenString = req.getReader().readLine();

		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				HTTP_TRANSPORT, JSON_FACTORY)
				.setAudience(Arrays.asList(CLIENT_ID))
				// If you retrieved the token on Android using the Play Services
				// 8.3 API or newer, set
				// the issuer to "https://accounts.google.com". Otherwise, set
				// the issuer to
				// "accounts.google.com". If you need to verify tokens from
				// multiple sources, build
				// a GoogleIdTokenVerifier for each issuer and try them both.
				.setIssuer("accounts.google.com").build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(idTokenString);
		} catch (GeneralSecurityException e) {
			resp.setStatus(403);
			writeOut(resp, "Invalid ID Token".getBytes());
			e.printStackTrace();
			return;
		}
		
		if (idToken != null) {
			
			Payload payload = idToken.getPayload();

			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");
			
			if(req.getSession(false)==null || SessionHelper.getCurrentUser(req)==null){
				registerAndLoginUser(payload, req, resp);
			}else if(!SessionHelper.getCurrentUser(req).getUserId().equals(email)){
				//Invalidate Session
				req.getSession().invalidate();
				registerAndLoginUser(payload,req, resp);
			}else{
				resp.sendRedirect(app_page);
			}
		} else {
			resp.setStatus(403);
			writeOut(resp, "Invalid ID Token".getBytes());
		}

	}
}
