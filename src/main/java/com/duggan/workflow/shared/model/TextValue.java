package com.duggan.workflow.shared.model;

public class TextValue extends StringValue{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TextValue() {
		
	}
	
	public TextValue(Long id, String key, String value){
		super(id,key,value);
	}
	
	public TextValue(String value) {
		super(value);
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRINGLONG;
	}
}
