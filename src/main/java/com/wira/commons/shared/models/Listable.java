package com.wira.commons.shared.models;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This interface defines an object/POJO that can be 
 * added into a dropdown ArrayList/ Combobox
 * @author duggan
 *
 */
public interface Listable extends IsSerializable,Serializable{

	/**
	 * This is the Key Value of the Object
	 * @return
	 */
	String getName();
	
	/**
	 * This is the value to be displayed
	 * @return
	 */
	String getDisplayName();
}
