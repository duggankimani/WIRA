package com.duggan.workflow.shared.events;

import java.util.List;

import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class EditCatalogSchemaEvent extends GwtEvent<EditCatalogSchemaEvent.EditCatalogSchemaHandler> {

	public static Type<EditCatalogSchemaHandler> TYPE = new Type<EditCatalogSchemaHandler>();
	private Catalog catalog;
	private List<CatalogColumn> lines = null;
	private boolean isDelete=false;

	public interface EditCatalogSchemaHandler extends EventHandler {
		void onEditCatalogSchema(EditCatalogSchemaEvent event);
	}

	public EditCatalogSchemaEvent(Catalog catalog) {
		this.catalog = catalog;
	}
	
	public EditCatalogSchemaEvent(Catalog catalog, boolean isDelete) {
		this.catalog = catalog;
		this.isDelete = isDelete;
	}

	
	@Override
	protected void dispatch(EditCatalogSchemaHandler handler) {
		handler.onEditCatalogSchema(this);
	}

	@Override
	public Type<EditCatalogSchemaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditCatalogSchemaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,Catalog catalog, boolean isDelete) {
		source.fireEvent(new EditCatalogSchemaEvent(catalog, isDelete));
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public List<CatalogColumn> getLines() {
		return lines;
	}

	public void setLines(List<CatalogColumn> lines) {
		this.lines = lines;
	}
}
