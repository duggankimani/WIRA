package com.duggan.workflow.server.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class ADProcessCategory extends PO{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String index;
	
	@XmlTransient
	@OneToMany
	private Set<ADDocType> docTypes = new HashSet<>();
	
	@XmlTransient
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
