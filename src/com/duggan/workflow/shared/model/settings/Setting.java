package com.duggan.workflow.shared.model.settings;

import java.io.Serializable;

import com.duggan.workflow.shared.model.Value;

public class Setting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SETTINGNAME name;
	
	private Value value;
	
	public Setting(){
		
	}
	
	public Setting(SETTINGNAME name, Value value){
		this.name = name;
		this.value = value;
	}
	
	public SETTINGNAME getName() {
		return name;
	}
	public void setName(SETTINGNAME name) {
		this.name = name;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	
	
}
