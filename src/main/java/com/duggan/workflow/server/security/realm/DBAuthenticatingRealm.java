package com.duggan.workflow.server.security.realm;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;

import com.duggan.workflow.server.dao.UserDaoImpl;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;

public class DBAuthenticatingRealm extends AbstractAuthenticatingRealm {

	Logger logger = Logger.getLogger(DBAuthenticatingRealm.class);
	
	public DBAuthenticatingRealm() {
		super(new Sha256CredentialsMatcher());
		setName("DBAuthRealm");
	}

	UserDaoImpl getUserDao() {
		return DB.getUserDao();
	}

	@Override
	public AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user =   getUserDao().getUser(token.getUsername());
		if (user != null) {
			logger.info("DBAuthenticatingRealm - Simple Auth Info created");
			return new SimpleAuthenticationInfo(user.toDto(),
					user.getPassword(), getName());
		}
		return null;

	}
	
	@Override
	public Class<?> getAuthenticationTokenClass() {
		return UsernamePasswordToken.class;
	}

	
}
