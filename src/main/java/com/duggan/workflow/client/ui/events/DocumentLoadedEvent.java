package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Doc;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DocumentLoadedEvent extends GwtEvent<DocumentLoadedEvent.DocumentLoadedHandler> {
    private static Type<DocumentLoadedHandler> TYPE = new Type<DocumentLoadedHandler>();
    
    public interface DocumentLoadedHandler extends EventHandler {
        void onDocumentLoaded(DocumentLoadedEvent event);
    }

    private final Doc doc;
   
    public DocumentLoadedEvent(Doc doc) {
		this.doc = doc;
    }

    public static Type<DocumentLoadedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final DocumentLoadedHandler handler) {
        handler.onDocumentLoaded(this);
    }

    @Override
    public Type<DocumentLoadedHandler> getAssociatedType() {
        return TYPE;
    }

	public Doc getDoc() {
		return doc;
	}
}