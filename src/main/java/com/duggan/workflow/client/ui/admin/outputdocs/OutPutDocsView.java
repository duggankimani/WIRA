package com.duggan.workflow.client.ui.admin.outputdocs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.events.EditOutputDocEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.OutputDocument;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class OutPutDocsView extends ViewImpl implements
		OutPutDocsPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNewDocument;
	@UiField FlexTable tblView;

	public interface Binder extends UiBinder<Widget, OutPutDocsView> {
	}

	@Inject
	public OutPutDocsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
//		setTable();
	}

//	private void setTable() {
//		tblView.setAutoNumber(true);
//		List<TableHeader> th = new ArrayList<TableHeader>();
//		th.add(new TableHeader("Name", 20.0,"title"));
//		th.add(new TableHeader("Template Id", 20.0));
//		th.add(new TableHeader("Path", 30.0));
//		th.add(new TableHeader("Attachment(s)", 20.0));
//		th.add(new TableHeader("Action(s)", 10.0));
//		
//		tblView.setTableHeaders(th);
//	}

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
		Collections.sort(documents, new Comparator<OutputDocument>(){
			@Override
			public int compare(OutputDocument o1, OutputDocument o2) {
			
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		int i = 1;
		for(OutputDocument doc: documents){
			ActionLink link = new ActionLink();
			if(doc.getAttachmentName()!=null){			
				link.setText(doc.getAttachmentName());
				UploadContext context = new UploadContext("getreport");
				context.setContext("attachmentId", doc.getAttachmentId()+"");
				context.setContext("ACTION", "GETATTACHMENT");
				link.setTarget("_blank");
				link.setHref(context.toUrl());
			}
			
			int j = 0;
			tblView.setWidget(i, j++ , new HTMLPanel(""+i));
			tblView.setWidget(i, j++, new HTMLPanel(doc.getCode()));
			tblView.setWidget(i, j++, new HTMLPanel(doc.getPath()));
			tblView.setWidget(i, j++, link);
			
			final Link edit = new Link("Edit",doc);
			edit.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					OutputDocument d = edit.getDoc();
					AppContext.fireEvent(new EditOutputDocEvent(d));
				}
			});
			
			final Link delete = new Link("Delete", doc);
			delete.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					OutputDocument d= delete.getDoc();
					d.setActive(false);
					AppContext.fireEvent(new EditOutputDocEvent(d));
				}
			});
			
			
			HTMLPanel panel = new HTMLPanel("");
			panel.add(edit);
			panel.add(new HTMLPanel(""));
			panel.add(delete);
			tblView.setWidget(i, j++, panel);
			
			++i;
		}
	}

	private void setUserHeaders(FlexTable flexTable) {
		int j = 0;
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "20px");
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>Template Id</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>Path</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>Attachments</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "150px");
		flexTable.setWidget(0, j++, new HTMLPanel("<strong>Actions</strong>"));
		flexTable.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
	}

//	private void createRow(final OutputDocument doc) {
//	
//		ActionLink link = new ActionLink();
//		if(doc.getAttachmentName()!=null){			
//			link.setText(doc.getAttachmentName());
//			UploadContext context = new UploadContext("getreport");
//			context.setContext("attachmentId", doc.getAttachmentId()+"");
//			context.setContext("ACTION", "GETATTACHMENT");
//			link.setTarget("_blank");
//			link.setHref(context.toUrl());
//		}
//		
//		
//		final Link edit = new Link("Edit",doc);
//		edit.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				OutputDocument d = edit.getDoc();
//				AppContext.fireEvent(new EditOutputDocEvent(d));
//			}
//		});
//		
//		final Link delete = new Link("Delete", doc);
//		delete.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				OutputDocument d= delete.getDoc();
//				d.setActive(false);
//				AppContext.fireEvent(new EditOutputDocEvent(d));
//			}
//		});
//		
//		HTMLPanel panel = new HTMLPanel("");
//		panel.add(edit);
//		panel.add(delete);
//		
////		tblView.addRow(new InlineLabel(doc.getName()), new InlineLabel(doc.getCode()), 
////				new InlineLabel(doc.getPath()),link, panel);
//	}
	
	class Link extends ActionLink{
		OutputDocument document;
		Link(String text,OutputDocument doc){
			this.document = doc;
			setText(text);
		}
		
		OutputDocument getDoc(){
			return document;
		}
	}
	
}
