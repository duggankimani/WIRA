package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.duggan.workflow.server.security.realm.google.GoogleUserDetails;
import com.wira.commons.shared.models.HTUser;

@Entity(name = "BUser")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "userId") })
@NamedQuery(name = "User.getUserByUserId", query = "SELECT u from BUser u where u.userId=:userId")
public class User extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	private String password;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String firstName;
	
	private String fullName;

	@Column(length = 100)
	private String email;
	
	private String pictureUrl;
	
	private String refreshToken;

	private boolean isArchived;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "UserGroup", joinColumns = { @JoinColumn(name = "userid") }, inverseJoinColumns = { @JoinColumn(name = "groupid") })
	// @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.PERSIST,org.hibernate.annotations.CascadeType.MERGE})
	private Collection<Group> groups = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "process_useraccess", joinColumns = { @JoinColumn(name = "userid") }, inverseJoinColumns = { @JoinColumn(name = "processid") })
	private Collection<ProcessDefModel> processDef = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orgid")
	private OrgModel org;

	public User() {
		this.isArchived = false;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Group> getGroups() {
		return groups;
	}

	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}

	public void addProcessDef(ProcessDefModel processDefModel) {
		processDef.add(processDefModel);
	}

	public void removeProcessDef(ProcessDefModel processDefModel) {
		processDef.remove(processDefModel);
	}

	public HTUser toDto() {
		HTUser htuser = new HTUser();
		return toDto(htuser);
	}
	
	public HTUser toDto(HTUser htuser) {
		htuser.setEmail(this.getEmail());
		htuser.setUserId(this.getUserId());
		htuser.setName(this.getFirstName());
		htuser.setPassword(this.getPassword());
		htuser.setSurname(this.getLastName());
		htuser.setId(this.getId());
		htuser.setRefId(this.getRefId());

		return htuser;
	}

	public String getFullNames() {
		return (lastName == null ? "" : lastName) + " "
				+ (firstName == null ? "" : firstName);

	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public boolean isSame(String email, String familyName, String givenName,
			int status) {

		if (isSame(familyName, this.lastName)
				&& isSame(givenName, this.firstName)
				&& isSame(this.getIsActive(), status)) {
			return true;
		}

		return false;
	}
	
	private boolean isSame(String str1, String str2) {
		if (str1 == null ^ str2 == null) {
			//xor
			return false;
		}

		if (str1 != null) {
			return str1.equals(str2);
		}
		
		return false;
	}

	private boolean isSame(int isActive, int status) {
		return isActive == status;
	}

}
