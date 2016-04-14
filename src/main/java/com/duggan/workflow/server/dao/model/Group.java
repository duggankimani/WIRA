package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

@Entity(name="BGroup")
@Table(uniqueConstraints={@UniqueConstraint(columnNames="name")})
@NamedQuery(name="Group.getGroupByGroupId", query="from BGroup p where p.name=:name")
public class Group extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	private String fullName;
	
	@ManyToMany(mappedBy="groups", fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,
			CascadeType.MERGE,CascadeType.REFRESH})
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Collection<User> members = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name="process_groupaccess", 
	joinColumns={@JoinColumn(name="groupid")},
	inverseJoinColumns={@JoinColumn(name="processid")})
	private Collection<ProcessDefModel> processDef = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.PERSIST })
	@JoinTable(name = "role_permissions", 
	joinColumns = @JoinColumn(name = "roleid"), inverseJoinColumns = @JoinColumn(name = "permissionid"))
	private Set<PermissionModel> permissions = new HashSet<>();

	
	private boolean isArchived;
	
	public Group(){
		this.isArchived=false;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public Collection<User> getMembers() {
		return members;
	}

	public void setMembers(Collection<User> members) {
		this.members = members;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Group)){
			return false;
		}
		
		Group other = (Group)obj;
		
		return name.equals(other.getName());
	}

	public void addProcessDef(ProcessDefModel processDefModel) {
		getProcessDef().add(processDefModel);
	}

	public Collection<ProcessDefModel> getProcessDef() {
		return processDef;
	}

	public void setProcessDef(Collection<ProcessDefModel> processDef) {
		this.processDef = processDef;
	}

	public void removeProcessDef(ProcessDefModel processDefModel) {
		processDef.remove(processDefModel);
	}
	
	public Set<PermissionModel> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionModel> permissions) {
		this.permissions.clear();
		this.permissions.addAll(permissions);
	}

}
