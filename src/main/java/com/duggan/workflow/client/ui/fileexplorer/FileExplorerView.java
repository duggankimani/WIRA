package com.duggan.workflow.client.ui.fileexplorer;

import java.util.List;

import javax.inject.Inject;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class FileExplorerView extends ViewImpl implements FileExplorerPresenter.MyView {
	interface Binder extends UiBinder<Widget, FileExplorerView> {
	}

	@UiField
	HTMLPanel panelFiles;
	@UiField
	FlexTable fileTable;

	@Inject
	FileExplorerView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		bindSlot(FileExplorerPresenter.FILES_SLOT, panelFiles);
		setHeaders(fileTable);
	}

	@Override
	public void bindAttachments(List<Attachment> attachments) {
		fileTable.removeAllRows();
		setHeaders(fileTable);

		int i = 1;
		for (Attachment file : attachments) {
			int j = 0;
			Checkbox box = new Checkbox(file);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			fileTable.setWidget(i, j++, box);

			Image fileImg = new Image();
			fileImg.setResource(getImage(file.getName()));
			fileTable.setWidget(i, j++, fileImg);

			ActionLink link = new ActionLink(file);
			link.setText(file.getName());
			link.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Attachment file = (Attachment)((ActionLink)event.getSource()).getModel();
					String href = getHref(file);
					AppContext.fireEvent(new ShowAttachmentEvent(href, file.getName()));
				}
			});

			fileTable.setWidget(i, j++, link);
			fileTable.setWidget(i, j++, new InlineLabel(
					DateUtils.MONTHDAYFORMAT.format(file.getCreated())));
			fileTable.setWidget(i, j++, new InlineLabel(file.getCreatedBy()
					.getFullName()));
			fileTable
					.setWidget(i, j++, new InlineLabel(file.getDocumentType()));
			fileTable.setWidget(i, j++, new InlineLabel("Claim Authorization"));
			fileTable.setWidget(i, j++, new InlineLabel(file.getDocRefId()));

			++i;
		}
	}

	private ImageResource getImage(String name) {
		if (name.endsWith(".pdf")) {
			return ImageResources.IMAGES.pdf();
		} else if (name.endsWith(".docx") || name.endsWith(".doc")) {
			return ImageResources.IMAGES.doc();
		} else if (name.endsWith(".odt")) {
			return ImageResources.IMAGES.odt();
		} else if (name.endsWith(".ods")) {
			return ImageResources.IMAGES.ods();
		} else if (name.endsWith(".csv")) {
			return ImageResources.IMAGES.csv();
		} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
			return ImageResources.IMAGES.xls();
		} else if (name.endsWith(".png") || name.endsWith(".jpg")
				|| name.endsWith(".jpeg") || name.endsWith(".gif")) {
			return ImageResources.IMAGES.img();
		} else if (name.endsWith(".txt") || name.endsWith(".text")) {
			return ImageResources.IMAGES.txt();
		} else {
			return ImageResources.IMAGES.file();
		}
	}

	private void setHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");
		table.setWidget(0, j++, new HTMLPanel("<strong></strong>"));

		table.setWidget(0, j++, new HTMLPanel("<strong>File Name</strong>"));

		table.setWidget(0, j++, new HTMLPanel("<strong>Modified</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Owner</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Type</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Process</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Case</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "200px");
	}

	private String getHref(Attachment attachment) {

		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", attachment.getId() + "");
		context.setContext("ACTION", "GETATTACHMENT");
		String url = context.toUrl();

		String href = (AppContext.getBaseUrl() + "/" + url);

		return href;
	}

}