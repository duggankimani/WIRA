package com.duggan.workflow.server.security.realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.duggan.workflow.server.dao.UserDaoImpl;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionPOJO;

public class DBAuthenticatingRealm extends AuthorizingRealm {

	Logger logger = Logger.getLogger(DBAuthenticatingRealm.class);
	
	public DBAuthenticatingRealm() {
		super(new MemoryConstrainedCacheManager(), new Sha256CredentialsMatcher());
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
			logger.info("Simple Auth Info created");
			return new SimpleAuthenticationInfo(user.toDto(),
					user.getPassword(), getName());
		}
		return null;

	}
	
	
	@Override
	public AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Collection dbPrincipals = principals.fromRealm(getName());
		if(!dbPrincipals.iterator().hasNext()){
			return null;
		}
		
		HTUser userDto = (HTUser) dbPrincipals.iterator().next();
//		User user = getUserDao().findByUserId(userDto.getRefId());
		User user =   getUserDao().findByRefId(userDto.getRefId(), User.class);
		logger.info("User == "+user);

		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			ArrayList<String> permissionList = new ArrayList<String>();
			ArrayList<String> roles = new ArrayList<String>();
			
			for (Group role : user.getGroups()) {
				roles.add(role.getName());
				List<PermissionPOJO> permissions = DB.getPermissionDao().getPermissionsForRole(role.getName());
				
				for(PermissionPOJO permission: permissions){
					if(!permissionList.contains(permission.getName().name())){
						permissionList.add(permission.getName().name());
					}
				}
				
			}
			
			info.addRoles(roles);
			info.addStringPermissions(permissionList);
			
			userDto.setRoles(roles);
			userDto.setPermissionList(permissionList);
			return info;
		} else {
			return null;
		}
	}
	
	@Override
	public Class<?> getAuthenticationTokenClass() {
		return UsernamePasswordToken.class;
	}

	
}
