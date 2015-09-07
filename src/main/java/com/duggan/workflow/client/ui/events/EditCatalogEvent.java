package com.duggan.workflow.client.ui.events;

import java.util.List;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class EditCatalogEvent extends GwtEvent<EditCatalogEvent.EditCatalogHandler> {

	public static Type<EditCatalogHandler> TYPE = new Type<EditCatalogHandler>();
	private Catalog catalog;
	private List<DocumentLine> lines = null;
	private boolean isDelete=false;
	private boolean isEditData=false;
	

	public interface EditCatalogHandler extends EventHandler {
		void onEditCatalog(EditCatalogEvent event);
	}

	public EditCatalogEvent(Catalog catalog) {
		this.catalog = catalog;
	}
	
	public EditCatalogEvent(Catalog catalog, boolean isDelete) {
		this.catalog = catalog;
		this.isDelete = isDelete;
	}

	
	public EditCatalogEvent(Catalog catalog, boolean isDelete, boolean isEditData) {
		this.catalog = catalog;
		this.isDelete = isDelete;
		this.isEditData = isEditData;
	}

	@Override
	protected void dispatch(EditCatalogHandler handler) {
		handler.onEditCatalog(this);
	}

	@Override
	public Type<EditCatalogHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditCatalogHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,Catalog catalog, boolean isDelete) {
		source.fireEvent(new EditCatalogEvent(catalog, isDelete));
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public boolean isEditData() {
		return isEditData;
	}

	public List<DocumentLine> getLines() {
		return lines;
	}

	public void setLines(List<DocumentLine> lines) {
		this.lines = lines;
	}
}
