package com.duggan.workflow.shared.model;

public class DoubleValue implements Value {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double value;
	private String key;
	private Long id;
	
	public DoubleValue(){
	}

	public DoubleValue(Double val){
		value=val;
	}
	
	public DoubleValue(Long id, String key, Double value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (Double)value;
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
		
		return DataType.DOUBLE;
	}
	
	public DoubleValue clone(){
		DoubleValue dvalue = new DoubleValue(null, key, value);
		return dvalue;
	}
}
