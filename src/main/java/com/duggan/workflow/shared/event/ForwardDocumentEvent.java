package com.duggan.workflow.shared.event;

import com.duggan.workflow.shared.model.Doc;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ForwardDocumentEvent extends GwtEvent<ForwardDocumentEvent.ForwardDocumentHandler> {
    private static Type<ForwardDocumentHandler> TYPE = new Type<ForwardDocumentHandler>();
    
    public interface ForwardDocumentHandler extends EventHandler {
        void onForwardDocument(ForwardDocumentEvent event);
    }
    
    
    private final Doc doc;

	public ForwardDocumentEvent(final Doc doc) {
        this.doc = doc;
    }

    public static Type<ForwardDocumentHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ForwardDocumentHandler handler) {
        handler.onForwardDocument(this);
    }

    @Override
    public Type<ForwardDocumentHandler> getAssociatedType() {
        return TYPE;
    }
    
    public Doc getDoc() {
		return doc;
	}
}