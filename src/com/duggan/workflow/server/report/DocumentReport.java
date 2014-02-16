package com.duggan.workflow.server.report;

import java.util.List;

import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Notification;

public class DocumentReport {

	public DocumentReport() {
	}
	
	Document document;
	List<Notification> notifications;

	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
}
