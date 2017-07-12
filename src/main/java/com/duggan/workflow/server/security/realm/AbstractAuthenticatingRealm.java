package com.duggan.workflow.server.security.realm;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.UserDaoImpl;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.PermissionModel;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.security.realm.google.GoogleUserDetails;
import com.wira.commons.shared.models.HTUser;

public abstract class AbstractAuthenticatingRealm extends AuthorizingRealm{
	
	public AbstractAuthenticatingRealm(CredentialsMatcher matcher) {
		super(new MemoryConstrainedCacheManager(), matcher);
	}
	
	public AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Collection dbPrincipals = principals.fromRealm(getName());
		if(!dbPrincipals.iterator().hasNext()){
			return null;
		}
		
		UserDaoImpl userDao = DB.getUserDao();
		Object principal = dbPrincipals.iterator().next();
		User user = null;
		HTUser userDto = null;
		
		if(principal instanceof GoogleUserDetails){
			userDto = (HTUser) dbPrincipals.iterator().next();
			user =   userDao.findByRefId(userDto.getRefId(), User.class);
		}else{
			GoogleUserDetails googleUser = (GoogleUserDetails) dbPrincipals.iterator().next();
			userDto = googleUser;
			try{
				user = userDao.findUserByEmail(googleUser.getEmail());
			}catch(Exception e){}
		}
		
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			ArrayList<String> permissionList = new ArrayList<String>();
			ArrayList<String> roles = new ArrayList<String>();
			
			for (Group role : user.getGroups()) {
				roles.add(role.getName());
				Collection<PermissionModel> permissions = role.getPermissions();
				for(PermissionModel permission: permissions){
					if(!permissionList.contains(permission.getName().name())){
						permissionList.add(permission.getName().name());
					}
				}
				
			}
			
			info.addRoles(roles);
			info.addStringPermissions(permissionList);
			
			userDto.setRoles(roles);
			userDto.setPermissionList(permissionList);
			
			//Session Data?
			return info;
		} else {
			return null;
		}
	}

}
