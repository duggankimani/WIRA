package com.duggan.workflow.server.security.realm.google;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

public class AuthUtil {
	
	private static final Logger LOG = Logger.getLogger(AuthUtil.class
			.getSimpleName());

	/**
	 * Creates and returns a new {@link AuthorizationCodeFlow} for this app.
	 * @param SCOPEs 
	 */
	public static AuthorizationCodeFlow newAuthorizationCodeFlow(List<String> SCOPEs)
			throws IOException {
		
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(),
				new InputStreamReader(AuthUtil.class.getClassLoader()
						.getResourceAsStream(
								"client_secret.apps.google.json")));

		return new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
				new JacksonFactory(), clientSecrets.getDetails().getClientId(), 
				clientSecrets.getDetails().getClientSecret(),SCOPEs)
		.setCredentialDataStore(
				StoredCredential.getDefaultDataStore(
				new FileDataStoreFactory(new File(OauthServlet.getBASEPATH())))
				) //Credential Store - Handles token refresh automatically
		.setAccessType("offline").build();
	}

	/**
	 * Get the current user's ID from the session
	 * 
	 * @return string user id or null if no one is logged in
	 */
	public static String getUserId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute("userId");
	}

	public static void setUserId(HttpServletRequest request, String userId) {

		if(request!=null){
			HttpSession session = request.getSession();
			
			if(session!=null){
				session.setAttribute("userId", userId);
			}
		}
	}

	public static void clearUserId(HttpServletRequest request)
			throws IOException {
		// Delete the credential in the credential store
		String userId = getUserId(request);
//		store.delete(userId, getCredential(userId));

		// Remove their ID from the local session
		request.getSession().removeAttribute("userId");
	}

	public static Credential getCredential(String userId,List<String> SCOPEs) throws IOException {
		if (userId == null) {
			return null;
		} else {
			return AuthUtil.newAuthorizationCodeFlow(SCOPEs).loadCredential(userId);
		}
	}

	public static Credential getCredential(HttpServletRequest req, List<String> SCOPEs)
			throws IOException {
		return AuthUtil.newAuthorizationCodeFlow(SCOPEs).loadCredential(
				getUserId(req));
	}

	public static List<String> getAllUserIds() {
//		return store.listAllUsers();
		return new ArrayList<String>();
	}
}