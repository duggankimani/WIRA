package com.duggan.workflow.shared.model;

/**
 * This interface defines an object/POJO that can be 
 * added into a dropdown list/ Combobox
 * @author duggan
 *
 */
public interface Listable {

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
