package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

import com.google.gwt.user.client.Cookies;

/**
 * Session User
 * @author duggan
 *
 */
public class CurrentUser implements Serializable {

	private static final long serialVersionUID = 3817696672368275188L;
	private String userId;
	private String fullName;
	private String firstName;
	private String sirName;
	private List<UserGroup> groups;
	private boolean isValid;

	public CurrentUser() {
		this.isValid=false;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		//Cookies.setCookie("userId", fullName);
		
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
		//Cookies.setCookie("fullName", fullName);
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setSirName(String sirName) {
		this.sirName = sirName;
	}

	public String getUserId() {
		return userId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSirName() {
		return sirName;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;		
	}

	public List<UserGroup> getGroups() {
		return groups;
	}
	
	public String getGroupsAsString(){
		StringBuffer out = new StringBuffer();
		if(groups!=null){
			for(UserGroup group: groups){
				out.append(group.getName()+",");
			}
		}
		
		if(out.length()>0){
			return out.substring(0, out.length()-1);
		}
		
		return "";
	}

	/**
	 * Client Side Auth
	 * 
	 * @param user
	 * @param fullName
	 * @return
	 */
//	public boolean validate(String user, String fullName) {
//		
//		if(user==null && fullName==null){
//			return false;
//		}
//		
//		if(user!=null && fullName!=null){
//			if(this.userId==null){
//				this.userId=user;
//			}
//			if(this.fullName==null){
//				this.fullName=fullName;
//			}
//			
//			return true;
//		}
//		
//		return false;
//	}
}
