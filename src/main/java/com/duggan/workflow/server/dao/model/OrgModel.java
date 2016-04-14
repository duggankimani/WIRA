package com.duggan.workflow.server.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.Org;

@Entity
@Table(name = "orgmodel", indexes={@Index(columnList="refId",name="idx_ref_id"),
		@Index(columnList="name",name="idx_org_name"),
		@Index(columnList="county",name="idx_org_county")
} )
public class OrgModel extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false)
	private String name;
	
	private String county;
	
	@OneToMany(mappedBy="org")
	private Set<User> users = new HashSet<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public Org toDto(){
		Org org = new Org();
		org.setRefId(getRefId());
		org.setName(getName());
		org.setCounty(county);
		return org;
	}
	
	public void copyFrom(Org org){
		setName(org.getName());
		setCounty(org.getCounty());
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
