package com.duggan.workflow.server.helper.jbpm;

import java.util.List;
import java.util.Map;

import com.duggan.workflow.server.dao.model.ADTaskNotification;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTUser;

public interface EmailHandler {

	public void sendNotification(ADTaskNotification template,Document doc, Map<String, Object> params);
	public void sendMail(String htmlTemplate, Doc doc,List<HTUser> receipients, Map<String, Object> params);
}
