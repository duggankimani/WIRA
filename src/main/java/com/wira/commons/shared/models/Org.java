package com.wira.commons.shared.models;


public class Org extends SerializableObj implements Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String county;
	
	public Org() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
}
