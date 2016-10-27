package com.duggan.workflow.server.security;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.jbpm.GoogleAuthenticationManager;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;
import com.wira.login.shared.model.ActionType;
import com.wira.login.shared.request.LoginRequest;
import com.wira.login.shared.response.LoginRequestResult;

public class GoogleLoginCallbackServlet extends BaseServlet {

	Logger logger = Logger.getLogger(GoogleLoginCallbackServlet.class);
	String app_page = null;
	GoogleClientSecrets clientSecrets = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config != null) {
			app_page = config.getInitParameter("app_page");
		}
		String CLIENT_SECRET_FILE = System
				.getProperty(GoogleAuthenticationManager.Google_OAuth_Client_Secret_File);

		// Exchange auth code for access token
		try {
			clientSecrets = GoogleClientSecrets.load(
					JacksonFactory.getDefaultInstance(), new FileReader(
							CLIENT_SECRET_FILE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}

	@Override
	protected void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		if(clientSecrets==null){
			resp.setStatus(404);
			writeOut(resp, "No Client Secrets File Found".getBytes());
		}
		
		// (Receive authCode via HTTPS POST)
		String authCode = req.getReader().readLine();
		String REDIRECT_URI = "postmessage";

		GoogleTokenResponse tokenResponse = null;

		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		String CLIENT_ID = clientSecrets.getDetails().getClientId();
		String CLIENT_SECRET = clientSecrets.getDetails().getClientSecret();
		
		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(
					HTTP_TRANSPORT, JSON_FACTORY,
					"https://www.googleapis.com/oauth2/v4/token",CLIENT_ID, CLIENT_SECRET, authCode,
					REDIRECT_URI).execute();
		} catch (TokenResponseException e) {
			if (e.getDetails() != null) {
				logger.warn("Error: " + e.getDetails().getError());
				if (e.getDetails().getErrorDescription() != null) {
					logger.warn(e.getDetails().getErrorDescription());
				}
				if (e.getDetails().getErrorUri() != null) {
					logger.warn(e.getDetails().getErrorUri());
				}
			} else {
				logger.warn(e.getMessage());
			}

			throw e;
		}

		String accessToken = tokenResponse.getAccessToken();

		// Get profile info from ID token
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();
		String userId = payload.getSubject(); // Use this value as a key to
												// identify a user.
		
//		Collection<String> SCOPE = Arrays.asList("profile", "email");
//		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//				HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).setAccessType("offline")
//				.setApprovalPrompt("force")
//				.build();
//		Credential credential = flow.loadCredential(payload.getEmail());
//		
//		if(credential==null){
//			credential = flow.createAndStoreCredential(tokenResponse, userId);
//		}
		
		registerAndLoginUser(payload, req, resp);
	}

	protected void registerAndLoginUser(Payload payload, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String email = payload.getEmail();
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get("name");
		String pictureUrl = (String) payload.get("picture");
		String locale = (String) payload.get("locale");
		String familyName = (String) payload.get("family_name");
		String givenName = (String) payload.get("given_name");

		UserDaoHelper helper = new UserDaoHelper();
		HTUser user = helper.getUser(email);
		if (user == null) {
			user = new HTUser();
		}
		user.setEmail(email);
		user.setUserId(email);
		user.setName(givenName);
		user.setSurname(familyName);
		user.setPictureUrl(pictureUrl);
		user.setEmailVerified(emailVerified);
		if(user.getRefId()==null){
			//new group
			UserGroup admin = helper.getGroupById("ADMIN");
			if(admin!=null){
				ArrayList<UserGroup> groups = new ArrayList<UserGroup>();
				groups.add(admin);
				user.setGroups(groups);
			}
		}

		LoginRequest request = new LoginRequest(ActionType.VIA_GOOGLE_OAUTH,
				user);
		LoginRequestResult response = (LoginRequestResult) request
				.createDefaultActionResponse();
		helper.login(request, response);
		
		CurrentUserDto currentUserDto = response.getCurrentUserDto();
		if (currentUserDto != null
				&& currentUserDto.isLoggedIn()) {
			resp.sendRedirect(app_page);
		}
	}
}
