package com.duggan.workflow.server.security;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.codehaus.jackson.map.ObjectMapper;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.security.realm.google.GoogleAuthenticationToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wira.commons.shared.models.LogInResult;

@Singleton
public class GoogleLoginCallbackServlet extends BaseServlet {

	String app_page = null;

	@Inject UserDaoHelper usersHelper;
	
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
		

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}

	@Override
	protected void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		logger.info("Executing GoogleLoginCallback! -- ");
		
		// (Receive authCode via HTTPS POST)
		String authCode = req.getReader().readLine();
		
//		String REDIRECT_URI = "postmessage";
//
//		GoogleTokenResponse tokenResponse = null;
//
//		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//		String CLIENT_ID = clientSecrets.getDetails().getClientId();
//		String CLIENT_SECRET = clientSecrets.getDetails().getClientSecret();
//		
//		try {
//			tokenResponse = new GoogleAuthorizationCodeTokenRequest(
//					HTTP_TRANSPORT, JSON_FACTORY,
//					"https://www.googleapis.com/oauth2/v4/token",CLIENT_ID, CLIENT_SECRET, authCode,
//					REDIRECT_URI).execute();
//		} catch (TokenResponseException e) {
//			if (e.getDetails() != null) {
//				logger.warn("Error: " + e.getDetails().getError());
//				if (e.getDetails().getErrorDescription() != null) {
//					logger.warn(e.getDetails().getErrorDescription());
//				}
//				if (e.getDetails().getErrorUri() != null) {
//					logger.warn(e.getDetails().getErrorUri());
//				}
//			} else {
//				logger.warn(e.getMessage());
//			}
//
//			throw e;
//		}
//
//		String accessToken = tokenResponse.getAccessToken();
//
//		// Get profile info from ID token
//		GoogleIdToken idToken = tokenResponse.parseIdToken();
//		GoogleIdToken.Payload payload = idToken.getPayload();
//		String userId = payload.getSubject(); // Use this value as a key to
//												// identify a user.
		
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
		
		GoogleAuthenticationToken token = new GoogleAuthenticationToken(authCode);
		registerAndLoginUser(token, req, resp);
	}

	protected void registerAndLoginUser(AuthenticationToken authToken, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		LogInResult result = new LogInResult();
		
		boolean loggedIn = usersHelper.execLogin(authToken);
		usersHelper.wrapResult(loggedIn, result);
		
		ObjectMapper mapper = new ObjectMapper();
		//Object to JSON in file
		StringWriter w = new StringWriter();
		mapper.writeValue(w, result);

		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(result);
		resp.getWriter().write(jsonInString);
		resp.setContentType("application/json");
	}
}
