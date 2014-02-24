package com.duggan.workflow.shared.model;

public class LongValue implements Value{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long value;
	private String key;
	private Long id;
	
	public LongValue() {
	}
	
	public LongValue(Long value){
		this();
		this.value = value;
	}
	
	public LongValue(Long id, String key, Long value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	@Override
	public void setValue(Object value) {
		this.value = (Long)value;
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

	public LongValue clone(boolean fullClone){
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		LongValue lvalue = new LongValue(identify, key, value);
		return lvalue;
	}
}
