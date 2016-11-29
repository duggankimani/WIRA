package com.duggan.workflow.client.ui.admin.process;

import java.util.ArrayList;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.ProcessDef;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProcessView extends ViewImpl implements
		ProcessPresenter.IProcessView {

	private final Widget widget;

	@UiField
	HTMLPanel processStepsPanel;
	@UiField
	HTMLPanel processConfigPanel;
	
	@UiField Image imgProcess;

	@UiField IFrameElement guvnorIFrame;
	
	private String actionPreview;
	
	@UiField AnchorElement aDownloadBPN;
	@UiField AnchorElement aDownloadIMG;

	public interface Binder extends UiBinder<Widget, ProcessView> {
	}

	@Inject
	public ProcessView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		imgProcess.setAltText("Could not load process image. Kindly ensure the svg image for this process has been uploaded");
		/**
		 * http://stackoverflow.com/questions/28960628/embedding-workbench-jbpm-in-application
		 * 
		 * http://localhost:9090/kie-wb/kie-wb.html?standalone=&perspective=org.kie.workbench.common.screens.home.client.perspectives.HomePerspective#org.kie.workbench.common.screens.messageconsole.MessageConsole
		 */
		
		guvnorIFrame.setSrc("http://localhost:9090/kie-wb?standalone=&path=default://master@jbpm-playground/Evaluation/src/main/resources/evaluation.bpmn2");
		
		//using erspectives instead of path_url
		//http://localhost:9090/kie-wb/kie-wb.html?standalone=&perspective=org.kie.workbench.common.screens.home.client.perspectives.HomePerspective
		//guvnorIFrame.setSrc("http://localhost:9090/kie-wb/kie-wb.html?standalone=&perspective=org.kie.workbench.common.screens.home.client.perspectives.HomePerspective#org.kie.workbench.common.screens.messageconsole.MessageConsole");
		bindSlot(ProcessPresenter.TABLE_SLOT, processStepsPanel); 
	}

//	@Override
//	public void setInSlot(Object slot, IsWidget content) {
//		if (slot == ProcessPresenter.TABLE_SLOT) {
//			processStepsPanel.clear();
//			if (content != null) {
//				processStepsPanel.add(content);
//			}
//		} else {
//			super.setInSlot(slot, content);
//		}
//	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setProcess(ProcessDef processDef) {
//		spnProcessName.setInnerText(processDef.getName());
		String url = AppContext.getBaseUrl()+"/getreport?attachmentId="+processDef.getImageId()+"&action=getattachment";
		aDownloadIMG.setHref(url);
		imgProcess.setUrl(url);
		if(actionPreview==null || actionPreview.equals(ProcessPresenter.ACTION_PREVIEW)){
			imgProcess.removeStyleName("hide");
		}
		
		ArrayList<Attachment> attachments = processDef.getFiles();
		if(!attachments.isEmpty()){
			String bpmnUrl = AppContext.getBaseUrl()+"/getreport?attachmentId="+attachments.get(0).getId()+"&action=getattachment";
			aDownloadBPN.setHref(bpmnUrl);
		}
		
	}
	
	@Override
	public void clear() {
		imgProcess.addStyleName("hide");
	}

	@Override
	public void setConfig(String actionPreview) {
		this.actionPreview = actionPreview;
		if(actionPreview.equals(ProcessPresenter.ACTION_CONFIG)){
			processConfigPanel.removeStyleName("hide");
			imgProcess.addStyleName("hide");
		}else{
			processConfigPanel.addStyleName("hide");
			imgProcess.removeStyleName("hide");
		}
	}
}
