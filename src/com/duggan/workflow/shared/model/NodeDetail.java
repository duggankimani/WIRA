package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class NodeDetail implements Serializable {

	private static final long serialVersionUID = 8764691316633514474L;
	private Integer processInstanceId;
	private String name;
	private boolean isStartNode;
	private boolean isEndNode;
	private boolean isCurrentNode;
	private Long nodeId;
	private List<HTUser> actors = new ArrayList<HTUser>(); //Comma separated list of actor ids
	private List<UserGroup> groups = new ArrayList<UserGroup>(); //Comma Separated list of group ids
	
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

	public void addAllUsers(List<HTUser> lst) {
		assert lst!=null;
		
		for(HTUser user:lst){
			addUser(user);
		}
	}

	public List<HTUser> getActors() {
		return actors;
	}

	public void setActors(List<HTUser> actors) {
		this.actors = actors;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}

	
}
