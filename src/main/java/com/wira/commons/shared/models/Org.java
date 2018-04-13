package com.wira.commons.shared.models;

import com.google.gwt.view.client.ProvidesKey;

public class Org extends SerializableObj implements Listable{

	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<Org> KEY_PROVIDER = new ProvidesKey<Org>() {
      @Override
      public Object getKey(Org item) {
        return item == null ? null : item.getRefId();
      }
    };
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
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
		return getDescription();
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
