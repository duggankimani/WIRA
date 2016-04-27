package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.TreeType;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FileSelectedEvent extends GwtEvent<FileSelectedEvent.FileSelectedHandler> {
    private static Type<FileSelectedHandler> TYPE = new Type<FileSelectedHandler>();
    
    public interface FileSelectedHandler extends EventHandler {
        void onFileSelected(FileSelectedEvent event);
    }
    
	private TreeType treeType;
	private String refId;
   
    public FileSelectedEvent(TreeType treeType, String refId) {
        this.treeType = treeType;
		this.refId = refId;
    }

    public static Type<FileSelectedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final FileSelectedHandler handler) {
        handler.onFileSelected(this);
    }

    @Override
    public Type<FileSelectedHandler> getAssociatedType() {
        return TYPE;
    }

	public TreeType getTreeType() {
		return treeType;
	}

	public void setTreeType(TreeType treeType) {
		this.treeType = treeType;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
    
}