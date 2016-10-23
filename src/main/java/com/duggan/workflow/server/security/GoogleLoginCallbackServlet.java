package com.duggan.workflow.server.security;

import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.helper.jbpm.GoogleAuthCredentials;
import com.duggan.workflow.server.helper.jbpm.GoogleAuthenticationManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleLoginCallbackServlet extends HttpServlet {

	Logger logger = Logger.getLogger(GoogleLoginCallbackServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		GoogleAuthCredentials credentials = GoogleAuthenticationManager
				.getINSTANCE().getGoogleAuthCredentials();

		// (Receive authCode via HTTPS POST)
		String authCode = req.getParameter("code");
		String REDIRECT_URI = credentials.getRedirectUris()[0];
		

		// Set path to the Web application client_secret_*.json file you
		// downloaded from the
		// Google API Console:
		// https://console.developers.google.com/apis/credentials
		// You can also find your Web application client ID and client secret
		// from the
		// console and specify them directly when you create the
		// GoogleAuthorizationCodeTokenRequest
		// object.
		String CLIENT_SECRET_FILE = "client_secret.json";

		// Exchange auth code for access token
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new FileReader(
						CLIENT_SECRET_FILE));
		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
				new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
				"https://www.googleapis.com/oauth2/v4/token", clientSecrets
						.getDetails().getClientId(), clientSecrets.getDetails()
						.getClientSecret(), authCode, REDIRECT_URI) // Specify
																	// the same
																	// redirect
																	// URI that
																	// you use
																	// with your
																	// web
																	// app. If
																	// you don't
																	// have a
																	// web
																	// version
																	// of your
																	// app, you
																	// can
																	// specify
																	// an empty
																	// string.
				.execute();

		String accessToken = tokenResponse.getAccessToken();

		// Use access token to call API
		GoogleCredential credential = new GoogleCredential()
				.setAccessToken(accessToken);
//		Drive drive = new Drive.Builder(new NetHttpTransport(),
//				JacksonFactory.getDefaultInstance(), credential)
//				.setApplicationName("Auth Code Exchange Demo").build();
//		File file = drive.files().get("appfolder").execute();

		// Get profile info from ID token
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();
		String userId = payload.getSubject(); // Use this value as a key to
												// identify a user.
		String email = payload.getEmail();
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get("name");
		String pictureUrl = (String) payload.get("picture");
		String locale = (String) payload.get("locale");
		String familyName = (String) payload.get("family_name");
		String givenName = (String) payload.get("given_name");
		
		logger.info("##User Found - name = "+name+", PictureUrl = "+pictureUrl);
	}
}
