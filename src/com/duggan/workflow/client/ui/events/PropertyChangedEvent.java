package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class PropertyChangedEvent extends
		GwtEvent<PropertyChangedEvent.PropertyChangedHandler> {

	public static Type<PropertyChangedHandler> TYPE = new Type<PropertyChangedHandler>();
	private Long componentId;
	private String propertyName;
	private String propertyValue;

	public interface PropertyChangedHandler extends EventHandler {
		void onPropertyChanged(PropertyChangedEvent event);
	}

	public PropertyChangedEvent(Long componentId,String propertyName, String propertyValue ) {
		this.componentId = componentId;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public Long getComponentId() {
		return componentId;
	}

	@Override
	protected void dispatch(PropertyChangedHandler handler) {
		handler.onPropertyChanged(this);
	}

	@Override
	public Type<PropertyChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PropertyChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long componentId,String propertyName, String propertyValue) {
		source.fireEvent(new PropertyChangedEvent(componentId,propertyName, propertyValue));
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}
}
