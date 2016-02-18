package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.LinkedHashSet;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;

import com.duggan.workflow.shared.model.catalog.CatalogType;
import com.duggan.workflow.shared.model.catalog.FieldSource;

@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(CatalogColumnModel.class)
@Entity
@Table(name = "catalog")
public class CatalogModel extends PO {

	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlAttribute(required = true)
	@Column(nullable = false, length = 125)
	private String name;

	@XmlAttribute
	@Column(nullable = false, length = 255)
	private String description;

	@XmlTransient
	private String recordCount;
	
	private Long processDefId;
	
	private CatalogType type = CatalogType.DATATABLE;
	
	private String gridName;
	
	private FieldSource fieldSource = FieldSource.FORM;// Field Source
	
	public CatalogType getType() {
		return type;
	}

	public void setType(CatalogType type) {
		this.type = type;
	}

	@XmlTransient
	private String process;

	@XmlElement(name="column")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalog", cascade = {
			CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE })
	@Cascade({ org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private Set<CatalogColumnModel> columns = new LinkedHashSet<>();

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
		for (CatalogColumnModel model : cols) {
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

	public Long getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}

	public FieldSource getFieldSource() {
		return fieldSource;
	}

	public void setFieldSource(FieldSource fieldSource) {
		this.fieldSource = fieldSource;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

}
