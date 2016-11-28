package com.duggan.workflow.shared.events;

import com.duggan.workflow.shared.model.ProcessDef;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ProcessSelectedEvent extends
		GwtEvent<ProcessSelectedEvent.ProcessSelectedHandler> {

	public static Type<ProcessSelectedHandler> TYPE = new Type<ProcessSelectedHandler>();

	public interface ProcessSelectedHandler extends EventHandler {
		void onProcessSelected(ProcessSelectedEvent event);
	}

	private ProcessDef processDef;
	private boolean isSelected;
	
	public ProcessSelectedEvent(ProcessDef processDef, boolean isSelected) {
		this.processDef = processDef;
		this.isSelected = isSelected;
	}

	@Override
	protected void dispatch(ProcessSelectedHandler handler) {
		handler.onProcessSelected(this);
	}
	
	@Override
	public Type<ProcessSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProcessSelectedHandler> getType() {
		return TYPE;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public ProcessDef getProcessDef() {
		return processDef;
	}
	
	public static void fire(HasHandlers source,ProcessDef processDef, boolean selected) {
		source.fireEvent(new ProcessSelectedEvent(processDef,selected));
	}

}
