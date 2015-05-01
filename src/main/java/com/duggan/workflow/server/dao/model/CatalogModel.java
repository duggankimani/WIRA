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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;


@Entity
@Table(name="catalog")
public class CatalogModel extends PO{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, length=125)
	private String name;
	
	@Column(nullable=false, length=255)
	private String description;
	
	private String recordCount;
	private String process;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="catalog",
			cascade={CascadeType.PERSIST,CascadeType.REMOVE, CascadeType.MERGE})
	@Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private Set<CatalogColumnModel> columns = new HashSet<>();
	
	public CatalogModel() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Collection<CatalogColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(Collection<CatalogColumnModel> cols) {
		columns.clear();
		for(CatalogColumnModel model: cols){
			columns.add(model);
			model.setCatalog(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
