package com.duggan.workflow.server.dao.model;


import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class ADTrigger extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String imports;
	
	@Column(length=5000)
	private String script;

	@OneToMany(fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.DELETE, CascadeType.DELETE_ORPHAN, CascadeType.REMOVE})
	Collection<ADTaskStepTrigger> steps = new HashSet<>();
	
	public ADTrigger() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	@Override
	public boolean equals(Object obj) {
		ADTrigger other = (ADTrigger)obj;
		
		return other.name.equals(name);
	}

	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}
	
}
