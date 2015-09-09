package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ResetFieldValueEvent extends
		GwtEvent<ResetFieldValueEvent.ResetFieldValueHandler> {

	public static Type<ResetFieldValueHandler> TYPE = new Type<ResetFieldValueHandler>();

	public interface ResetFieldValueHandler extends EventHandler {
		void onResetFieldValue(ResetFieldValueEvent event);
	}
	
	private String fieldName;
	private String value;
	
	public ResetFieldValueEvent(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	protected void dispatch(ResetFieldValueHandler handler) {
		handler.onResetFieldValue(this);
	}
	

	@Override
	public Type<ResetFieldValueHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ResetFieldValueHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String fieldName, String value) {
		source.fireEvent(new ResetFieldValueEvent(fieldName,value));
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
