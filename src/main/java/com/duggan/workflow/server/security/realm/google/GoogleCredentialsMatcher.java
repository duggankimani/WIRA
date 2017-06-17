package com.duggan.workflow.server.security.realm.google;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import com.google.inject.Singleton;

@Singleton
public class GoogleCredentialsMatcher implements CredentialsMatcher{

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		if(info instanceof GoogleAuthenticationInfo){
			//Check exists
			return true;
		}
		return false;
	}
}
