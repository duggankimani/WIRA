package com.duggan.workflow.shared.model;

public class ProcessCategory extends SerializableObj implements Listable{

	private Long id;
	private String name;
	private String index;
	
	public ProcessCategory() {
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public String getDisplayName() {
		return name;
	}
}
