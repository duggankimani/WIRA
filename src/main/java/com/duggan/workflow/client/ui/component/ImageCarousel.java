package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Based on this: <a href="http://bootsnipp.com/snippets/featured/thumbnail-carousel">Thumbnail Carousel</a>
 * 
 * @author duggan
 *
 */
public class ImageCarousel extends Composite {

	private static ImageCarouselUiBinder uiBinder = GWT
			.create(ImageCarouselUiBinder.class);

	interface ImageCarouselUiBinder extends UiBinder<Widget, ImageCarousel> {
	}

	@UiField
	HTMLPanel carouselPanel;
	HTMLPanel item = null;
	HTMLPanel row = null;
	List<Attachment> attachments = new ArrayList<Attachment>();
	
	public ImageCarousel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	protected void load() {
		int count = attachments.size();
		int itemsPerRow = 4;
		if(count<4){
			itemsPerRow = attachments.size();
		}
		
		for (int i = 0; i < attachments.size(); i++) {
			final Attachment attachment  = attachments.get(i);
			if (i == 0 || i % 4 == 0) {
				item = new HTMLPanel("");
				item.addStyleName("item");
				if (i == 0) {
					item.addStyleName("active");
				}

				row = new HTMLPanel("");
				row.addStyleName("row-fluid");
				item.add(row);
				carouselPanel.add(item);
			}

			HTMLPanel span3 = new HTMLPanel("");
			span3.addStyleName("span"+getSize());
			Anchor a = new ActionLink(attachment);
			a.addStyleName("thumbnail");
			a.setTitle(attachment.getName());
			span3.add(a);
			registerImage(a);

			a.getElement().appendChild(getImage(attachment).getElement());
			row.add(span3);
		}
	}

	private Image getImage(Attachment attachment) {
		Image image = new Image(getUrl(attachment));
		image.setWidth("100%");
		image.setHeight("100%");
		return image;
	}

	protected void download(Attachment attachment) {
		AppContext.fireEvent(
				new ShowAttachmentEvent(getUrl(attachment), attachment.getName()));
	}

	private String getSize() {
		if(attachments.size()>3){
			return "3";
		}
		
		if(attachments.size()==3){
			return "4";
		}
		
		return "6";
	}

	public void addAttachment(Attachment attachment) {
		attachments.add(attachment);
		timer.cancel();
		timer.schedule(500);
	}

	public static String getUrl(Attachment attachment) {

		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", attachment.getId() + "");
		context.setContext("ACTION", "GETATTACHMENT");
		final String fullUrl = AppContext.getBaseUrl() + "/" + context.toUrl();
		return fullUrl;
	}

	public void clear() {
		item = null;
		row = null;
		carouselPanel.clear();
		attachments.clear();
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


	Timer timer = new Timer() {

		@Override
		public void run() {
			load();
		}
	};

	public void registerImage(HasClickHandlers obj) {
		obj.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ActionLink link = (ActionLink) event.getSource();
				download((Attachment)link.getModel());
			}
		});
	}
}
