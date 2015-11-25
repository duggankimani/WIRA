package com.duggan.workflow.shared.model.settings;

import com.duggan.workflow.shared.model.DataType;

public enum SETTINGNAME {

	ORGNAME("org.name", DataType.STRING),
	ORGLOGO("org.logo", DataType.INTEGER),
	SMTP_HOST("mail.smtp.host", DataType.STRING),
	SMTP_PORT("mail.smtp.port", DataType.INTEGER),
	SMTP_AUTH("mail.smtp.auth", DataType.BOOLEAN),
	SMTP_PROTOCOL("mail.transport.protocol", DataType.STRING),
	SMTP_STARTTLS("mail.smtp.starttls.enable", DataType.BOOLEAN),
	SMTP_ACCOUNT("mail.smtp.from", DataType.STRING),
	SMTP_PASSWORD("mail.smtp.password", DataType.STRING),
	RAZUNA_UPLOADS_FOLDERID("folder_id", DataType.STRING),
	REPORT_VIEW_IMPL("REPORT_VIEW_IMPL", DataType.STRING); //GOOGLE_DOCS, NEW_TAB, IFRAME
	
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
