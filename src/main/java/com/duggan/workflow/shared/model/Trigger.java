package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class Trigger implements Serializable, Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String imports;
	private String script;
	private boolean isActive=true;
	
	public Trigger() {
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public Trigger clone(){
		Trigger trigger = new Trigger();
		trigger.setActive(isActive);
		trigger.setId(id);
		trigger.setName(name);
		trigger.setScript(script);
		trigger.setImports(imports);
		
		return trigger;
	}

	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}

	@Override
	public String getDisplayName() {
		return name;
	}
}
