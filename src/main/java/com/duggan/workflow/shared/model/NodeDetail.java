package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ArrayList;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class NodeDetail implements Serializable {

	private static final long serialVersionUID = 8764691316633514474L;
	private Integer processInstanceId;
	private String name;
	private boolean isStartNode;
	private boolean isEndNode;
	private boolean isCurrentNode;
	private Long nodeId;
	private ArrayList<HTUser> actors = new ArrayList<HTUser>(); //Comma separated ArrayList of actor ids
	private ArrayList<UserGroup> groups = new ArrayList<UserGroup>(); //Comma Separated ArrayList of group ids
	
	public NodeDetail() {
	}

	public NodeDetail(String name, boolean isFirstNode, boolean isLastNode) {
		this.name = name;
		this.isStartNode=isFirstNode;
		this.isEndNode = isLastNode;		
	}

	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProcessInstanceId() {
		return processInstanceId;
	}

	public String getName() {
		return name;
	}

	public boolean isStartNode() {
		return isStartNode;
	}

	public void setStartNode(boolean isStartNode) {
		this.isStartNode = isStartNode;
	}

	public boolean isEndNode() {
		return isEndNode;
	}

	public void setEndNode(boolean isEndNode) {
		if(isEndNode &&  name==null)
			setName("End");
		
		this.isEndNode = isEndNode;
	}

	public boolean isCurrentNode() {
		return isCurrentNode;
	}

	public void setCurrentNode(boolean isCurrentNode) {
		this.isCurrentNode = isCurrentNode;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void addUser(HTUser user) {
		if(!actors.contains(user)){
			actors.add(user);
		}
	}

	public void addGroup(UserGroup group) {
		groups.add(group);
	}

	public void addAllUsers(ArrayList<HTUser> lst) {
		assert lst!=null;
		
		for(HTUser user:lst){
			addUser(user);
		}
	}

	public ArrayList<HTUser> getActors() {
		return actors;
	}

	public void setActors(ArrayList<HTUser> actors) {
		this.actors = actors;
	}

	public ArrayList<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<UserGroup> groups) {
		this.groups = groups;
	}

	
}
