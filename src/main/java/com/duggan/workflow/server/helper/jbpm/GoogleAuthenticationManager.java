package com.duggan.workflow.server.helper.jbpm;

import java.io.FileReader;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleAuthenticationManager {

	private GoogleAuthCredentials googleAuthCredentials;
	private static GoogleAuthenticationManager INSTANCE;
	public static final String Google_OAuth_Client_Secret_File = "Google_OAuth_Client_Secret_File";

	public GoogleAuthenticationManager() {
		try {
			String path = System.getProperty(Google_OAuth_Client_Secret_File);
			if(path==null){
				return;
			}
			StringWriter writer = new StringWriter();
			IOUtils.copy(new FileReader(path), writer);

			String json = writer.getBuffer().toString();

			JSONObject client = new JSONObject(json);
			JSONObject web = client.getJSONObject("web");
			String clientId = web.getString("client_id");
			String projectId = web.getString("project_id");
			String authUri = web.getString("auth_uri");
			String tokenUri = web.getString("token_uri");
//			JSONArray redirect_uris = web.getJSONArray("redirect_uris");
			JSONArray javascript_origins = web
					.getJSONArray("javascript_origins");

//			String[] redirectUris = new String[redirect_uris.length()];
//			for (int i = 0; i < redirect_uris.length(); i++) {
//				String uri = redirect_uris.getString(i);
//				redirectUris[i] = uri;
//			}

			String[] javascriptOrigins = new String[javascript_origins.length()];
			for (int i = 0; i < javascript_origins.length(); i++) {
				String uri = javascript_origins.getString(i);
				javascriptOrigins[i] = uri;
			}

			googleAuthCredentials = new GoogleAuthCredentials();
			googleAuthCredentials.setAuthUri(authUri);
			googleAuthCredentials.setClientId(clientId);
			googleAuthCredentials.setJavascriptOrigins(javascriptOrigins);
			googleAuthCredentials.setProjectId(projectId);
//			googleAuthCredentials.setRedirectUris(redirectUris);
			googleAuthCredentials.setTokenUri(tokenUri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GoogleAuthCredentials getGoogleAuthCredentials() {
		return googleAuthCredentials;
	}

	public void setGoogleAuthCredentials(
			GoogleAuthCredentials googleAuthCredentials) {
		this.googleAuthCredentials = googleAuthCredentials;
	}

	public static GoogleAuthenticationManager getINSTANCE() {
		if (INSTANCE == null) {
			synchronized (GoogleAuthenticationManager.class) {
				if (INSTANCE == null) {
					INSTANCE = new GoogleAuthenticationManager();
				}
			}
		}

		return INSTANCE;
	}
}
