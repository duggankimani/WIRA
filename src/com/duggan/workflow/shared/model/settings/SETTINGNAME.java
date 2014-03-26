package com.duggan.workflow.shared.model.settings;

import com.duggan.workflow.shared.model.DataType;

public enum SETTINGNAME {

	ORGNAME("org.name", DataType.STRING),
	ORGLOGO("org.logo", DataType.INTEGER),
	HOST("mail.smtp.host", DataType.STRING),
	PORT("mail.smtp.port", DataType.INTEGER),
	AUTH("mail.smtp.auth", DataType.BOOLEAN),
	PROTOCOL("mail.transport.protocol", DataType.STRING),
	STARTTLS("mail.smtp.starttls.enable", DataType.BOOLEAN),
	ACCOUNT("mail.smtp.from", DataType.STRING),
	PASSWORD("mail.smtp.password", DataType.STRING),
	VVV("Test1",DataType.STRING);
	
	String key;
	DataType type;
	
	private SETTINGNAME(String key, DataType type) {
		this.key = key;
		this.type = type;
	}
	
	public String getKey(){
		return key;
	}

	public DataType getType() {
		return type;
	}
	
}
