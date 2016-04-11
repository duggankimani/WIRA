package com.duggan.workflow.server.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ADProcessCategory extends PO{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String index;
	
	@OneToMany
	private Set<ADDocType> docTypes = new HashSet<>();
	
	@OneToMany
	private Set<CatalogModel> catalogs = new HashSet<>(); 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public Long getId() {
		
		return id;
	}
	
	
}
