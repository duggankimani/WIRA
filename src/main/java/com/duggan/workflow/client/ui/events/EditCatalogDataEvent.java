package com.duggan.workflow.client.ui.events;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class EditCatalogDataEvent extends GwtEvent<EditCatalogDataEvent.EditCatalogDataHandler> {

	public static Type<EditCatalogDataHandler> TYPE = new Type<EditCatalogDataHandler>();
	private Catalog catalog;
	private ArrayList<DocumentLine> lines = null;
	private boolean isDelete=false;
	private boolean isEditData=false;
	

	public interface EditCatalogDataHandler extends EventHandler {
		void onEditCatalogData(EditCatalogDataEvent event);
	}

	public EditCatalogDataEvent(Catalog catalog) {
		this.catalog = catalog;
	}
	
	public EditCatalogDataEvent(Catalog catalog, boolean isDelete) {
		this.catalog = catalog;
		this.isDelete = isDelete;
	}

	
	public EditCatalogDataEvent(Catalog catalog, boolean isDelete, boolean isEditData) {
		this.catalog = catalog;
		this.isDelete = isDelete;
		this.isEditData = isEditData;
	}

	@Override
	protected void dispatch(EditCatalogDataHandler handler) {
		handler.onEditCatalogData(this);
	}

	@Override
	public Type<EditCatalogDataHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditCatalogDataHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,Catalog catalog, boolean isDelete) {
		source.fireEvent(new EditCatalogDataEvent(catalog, isDelete));
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

	public ArrayList<DocumentLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<DocumentLine> lines) {
		this.lines = lines;
	}
}
