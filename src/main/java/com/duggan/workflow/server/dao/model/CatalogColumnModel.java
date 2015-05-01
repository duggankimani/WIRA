package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.DBType;



@Entity
@Table(name="catalogcolumn")
public class CatalogColumnModel extends PO{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String label;
	@Enumerated(EnumType.STRING)
	private DBType type;
	private Integer size;
	private boolean isNullable;
	private boolean isPrimaryKey;
	private boolean isAutoIncrement;
	
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="catalogid", referencedColumnName="id")
	private CatalogModel catalog;
	
	public CatalogColumnModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DBType getType() {
		return type;
	}

	public void setType(DBType type) {
		this.type = type;
	}

	public CatalogModel getCatalog() {
		return catalog;
	}

	public void setCatalog(CatalogModel catalog) {
		this.catalog = catalog;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CatalogColumnModel){
			return this.name.equals(((CatalogColumnModel)obj).name);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
	
		return 7*name.hashCode();
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
