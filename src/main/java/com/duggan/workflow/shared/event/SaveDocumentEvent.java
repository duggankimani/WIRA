package com.duggan.workflow.shared.event;

import com.duggan.workflow.shared.model.Doc;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SaveDocumentEvent extends GwtEvent<SaveDocumentEvent.SaveDocumentHandler> {
    private static Type<SaveDocumentHandler> TYPE = new Type<SaveDocumentHandler>();
    
    public interface SaveDocumentHandler extends EventHandler {
        void onSaveDocument(SaveDocumentEvent event);
    }
    
    
    private final Doc doc;

	public SaveDocumentEvent(final Doc doc) {
        this.doc = doc;
    }

    public static Type<SaveDocumentHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final SaveDocumentHandler handler) {
        handler.onSaveDocument(this);
    }

    @Override
    public Type<SaveDocumentHandler> getAssociatedType() {
        return TYPE;
    }
    
    public Doc getDoc() {
		return doc;
	}
}