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

}
