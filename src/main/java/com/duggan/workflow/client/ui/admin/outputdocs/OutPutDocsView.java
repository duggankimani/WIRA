package com.duggan.workflow.client.ui.admin.outputdocs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.OutputDocument;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class OutPutDocsView extends ViewImpl implements
		OutPutDocsPresenter.MyView {

	private final Widget widget;
	@UiField
	Anchor aNewDocument;
	@UiField
	FlexTable tblView;

	@UiField
	Anchor aEdit;
	@UiField
	Anchor aDelete;
	@UiField
	Anchor aEditDoc;

	@UiField
	Element divGeneralActions;
	@UiField
	Element divModelActions;

	public interface Binder extends UiBinder<Widget, OutPutDocsView> {
	}

	@Inject
	public OutPutDocsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getDocumentButton() {
		return aNewDocument;
	}

	@Override
	public void setOutputDocuments(List<OutputDocument> documents) {
		tblView.removeAllRows();
		setUserHeaders(tblView);
		Collections.sort(documents, new Comparator<OutputDocument>() {
			@Override
			public int compare(OutputDocument o1, OutputDocument o2) {

				return o1.getName().compareTo(o2.getName());
			}
		});

		int i = 1;
		for (OutputDocument doc : documents) {
			int j = 0;
			Checkbox box = new Checkbox(doc);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});
			tblView.setWidget(i, j++, box);

			ActionLink link = new ActionLink();
			if (doc.getAttachmentName() != null) {
				link.setText(doc.getAttachmentName());
				UploadContext context = new UploadContext("getreport");
				context.setContext("attachmentId", doc.getAttachmentId() + "");
				context.setContext("ACTION", "GETATTACHMENT");
				link.setTarget("_blank");
				link.setHref(context.toUrl());
			}
			//tblView.setWidget(i, j++, new HTMLPanel("" + i));
			tblView.setWidget(i, j++, new HTMLPanel(doc.getCode()));
			tblView.setWidget(i, j++, new HTMLPanel(doc.getPath()));
			tblView.setWidget(i, j++, link);

			++i;
		}
	}

	private void setUserHeaders(FlexTable flexTable) {
		int j = 0;
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "20px");
//		flexTable.setWidget(0, j++, new HTMLPanel(
//				"<strong>Template Id</strong>"));
//		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>Path</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
		flexTable.setWidget(0, j++, new HTMLPanel(
				"<strong>Attachments</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "150px");
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>Actions</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
	}

	class Link extends ActionLink {
		OutputDocument document;

		Link(String text, OutputDocument doc) {
			this.document = doc;
			setText(text);
		}

		OutputDocument getDoc() {
			return document;
		}
	}

	@Override
	public HasClickHandlers getEdit() {
		return aEdit;
	}

	@Override
	public HasClickHandlers getDelete() {
		return aDelete;
	}

	@Override
	public HasClickHandlers getEditDoc() {
		return aEditDoc;
	}

	@Override
	public void setModelSelected(boolean selected) {
		if (selected) {
			divGeneralActions.addClassName("hide");
			divModelActions.removeClassName("hide");
		}else{
			divGeneralActions.removeClassName("hide");
			divModelActions.addClassName("hide");
		}
	}

}
