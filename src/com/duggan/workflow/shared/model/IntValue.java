package com.duggan.workflow.shared.model;

public class IntValue implements Value{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer value;
	private String key;
	private Long id;
	
	public IntValue() {
	}
	
	public IntValue(Integer value){
		this();
		this.value = value;
	}
	
	public IntValue(Long id, String key, Integer value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	@Override
	public void setValue(Object value) {
		this.value = (Integer)value;
	}

	@Override
	public Object getValue() {
		
		return value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public DataType getDataType() {
		return DataType.INTEGER;
	}

	public IntValue clone(){
		IntValue ivalue = new IntValue(null, key, value);
		return ivalue;
	}
}
