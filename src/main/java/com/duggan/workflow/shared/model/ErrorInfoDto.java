package com.duggan.workflow.shared.model;



public class ErrorInfoDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String message;
	private String stacktrace;
	
	public ErrorInfoDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}
}
