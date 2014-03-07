package com.duggan.workflow.client.ui.images;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {

	ImageResources IMAGES = GWT.create(ImageResources.class);
	
	@Source("excel.gif")
	ImageResource xls();
	
	@Source("pdf.gif")
	ImageResource pdf();
	
	@Source("txt.png")
	ImageResource txt();
	
	@Source("word.png")
	ImageResource doc();
	
	@Source("odt.png")
	ImageResource odt();

	@Source("ods.png")
	ImageResource ods();
	
	@Source("icon_csv.gif")
	ImageResource csv();
	
	@Source("file_extension_bmp.png")
	ImageResource img();
	
	@Source("file_icon.gif")
	ImageResource file();
	
	@Source("business_process.PNG")
	ImageResource business_process();
	
	@Source("document_action.PNG")
	ImageResource document_action();
	
	@Source("generic_display.PNG")
	ImageResource generic_display();
	
	@Source("LeaveApp.PNG")
	ImageResource leaveapp();
	
	@Source("notification.PNG")
	ImageResource notification();
	
	@Source("show_users.PNG")
	ImageResource show_users();
	
	@Source("AddDocScreenshot.png")
	ImageResource adddoc();
	
	@Source("tasks.PNG")
	ImageResource tasks();
	
	@Source("activity.png")
	ImageResource activity();
	
}
