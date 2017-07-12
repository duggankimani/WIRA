package com.duggan.workflow.server.security.realm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.duggan.workflow.server.dao.UserDaoImpl;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.PermissionModel;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.security.realm.google.AuthUtil;
import com.duggan.workflow.server.security.realm.google.GoogleAuthenticationInfo;
import com.duggan.workflow.server.security.realm.google.GoogleAuthenticationToken;
import com.duggan.workflow.server.security.realm.google.GoogleCredentialsMatcher;
import com.duggan.workflow.server.security.realm.google.GoogleUserDetails;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class GoogleOAuthAuthentingRealm extends AbstractAuthenticatingRealm{

	GoogleClientSecrets clientSecrets = null;
	Logger logger = Logger.getLogger(getClass());
	
	public GoogleOAuthAuthentingRealm() {
		super(new GoogleCredentialsMatcher());
		setName("GoogleOAuthRealm");
		try {
			clientSecrets = GoogleClientSecrets.load(
					JacksonFactory.getDefaultInstance(), new InputStreamReader(
							getClass().getClassLoader().getResourceAsStream("client_secret.apps.google.json")));
		} catch (IOException e) {
		}
	}
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken aToken) throws AuthenticationException {
		GoogleAuthenticationToken token = (GoogleAuthenticationToken)aToken;
		
		String authCode = token.getAuthCode();
		String idTokenStr = token.getIdToken();
		
		try {
			GoogleIdToken idToken = null;
			GoogleTokenResponse tokenResponse = null;
			if(authCode!=null){
				tokenResponse = requestAuthorizationCodeTokenRequest(authCode);
				idToken = tokenResponse.parseIdToken();
			}else{
				idToken = requestVerificationForAccessToken(idTokenStr);
			}
			
			
			GoogleIdToken.Payload payload = idToken.getPayload();
			String userId = idToken.getPayload().getUserId();
			AuthUtil.setUserId(SessionHelper.getHttpRequest(), userId);
			
			if(tokenResponse!=null){
				
			}
			
			GoogleUserDetails googleUserDetails = new GoogleUserDetails(payload);
			googleUserDetails.setAuthCode(authCode);
			googleUserDetails.setIdToken(idTokenStr);
			
			UserDaoImpl userDao = DB.getUserDao();
			//User must exist in local db
			boolean exists = userDao.checkEmailExistsAndIsUnique(payload.getEmail());
			if(exists){
				//Merge with DB Data for completeness
				User dbUser = userDao.findUserByEmail(payload.getEmail());
				if(!dbUser.isSame(googleUserDetails.getEmail(), googleUserDetails.getSurname(), 
						googleUserDetails.getName(), 1)){
					dbUser.setFirstName(googleUserDetails.getName());
					dbUser.setLastName(googleUserDetails.getSurname());
					dbUser.setFullName(googleUserDetails.getFullName());
					userDao.save(dbUser);
				}
				dbUser.setPictureUrl(googleUserDetails.getPictureUrl());
				dbUser.toDto(googleUserDetails);
				dbUser.toDto(googleUserDetails);
				logger.info("GoogleOAuthAuthentingRealm - Google Auth Info created");
				return new GoogleAuthenticationInfo(googleUserDetails, idToken.getPayload().getAccessTokenHash(), getName());
			}
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Class<?> getAuthenticationTokenClass() {
		return GoogleAuthenticationToken.class;
	}
	
	/**
	 * Verify existing access token
	 * 
	 * @param idTokenStr
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private GoogleIdToken requestVerificationForAccessToken(String idTokenStr) throws GeneralSecurityException, IOException {
		String CLIENT_ID = clientSecrets.getDetails().getClientId();
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

		GoogleIdToken idToken = verifier.verify(idTokenStr);
		
		return idToken;
	}
	
	/**
	 * Request Authorization for AuthCode
	 * @param authCode
	 * @return
	 * @throws IOException
	 */
	private GoogleTokenResponse requestAuthorizationCodeTokenRequest(String authCode) throws IOException {
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

			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return tokenResponse;
	}
	
}
