package com.wira.commons.shared.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HTUser extends SerializableObj implements Listable,HasKey,Serializable,IsSerializable {

	private static final long serialVersionUID = -5249516544970187459L;
	private Long id;
	private String name;
	private String userId;
	private String email;
	private String surname;
	private String password;
	private ArrayList<UserGroup> groups ;
	private int participated;
	private int inbox;
	private int drafts;
	private ArrayList<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
	private Org org;
	
	public HTUser() {
	}

	public HTUser(String id) {
		this.userId = id;
	}
	
	public HTUser(String id, String email) {
		this.userId = id;
		this.email = email;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public String getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFullName(){
		return surname+" "+name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getFullName();
	}

	public ArrayList<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<UserGroup> groups) {
		this.groups = groups;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean hasGroup(String groupName) {
		if(groups!=null)
		for(UserGroup group:groups){
			if(group.getName().equalsIgnoreCase(groupName)){
				return true;
			}
		}
		return false;
	}

	public boolean isAdmin() {

		return hasGroup("admin") || hasGroup("Administrator");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof HTUser)){
			return false;
		}
		
		HTUser other =  (HTUser)obj;
		
		if(userId==null){
			return false;
		}
		
		return userId.equals(other.userId);
	}

	public int getInbox() {
		return inbox;
	}

	public void setInbox(int inbox) {
		this.inbox = inbox;
	}

	public int getParticipated() {
		return participated;
	}

	public void setParticipated(int participated) {
		this.participated = participated;
	}

	public int getTotal() {
		
		return participated+inbox;
	}

	@Override
	public String getDisplayName() {
		return getFullName();
	}

	@Override
	public String getKey() {
		return userId;
	}

	public int getDrafts() {
		return drafts;
	}

	public void setDrafts(int drafts) {
		this.drafts = drafts;
	}

	public ArrayList<PermissionPOJO> getPermissions() {
		return permissions;
	}

	public void setPermissions(ArrayList<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}
}