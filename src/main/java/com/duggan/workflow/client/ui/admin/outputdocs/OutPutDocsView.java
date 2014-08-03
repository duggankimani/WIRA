package com.duggan.workflow.client.ui.admin.outputdocs;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class OutPutDocsView extends ViewImpl implements
		OutPutDocsPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNewDocument;
	@UiField TableView tblView;

	public interface Binder extends UiBinder<Widget, OutPutDocsView> {
	}

	@Inject
	public OutPutDocsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		setTable();
	}

	private void setTable() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Name", 40.0,"title"));
		th.add(new TableHeader("Document Id", 10.0));
		th.add(new TableHeader("Attatchment(s)", 10.0));
		
		tblView.setTableHeaders(th);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getDocumentButton() {
		return aNewDocument;
	}
	
}
