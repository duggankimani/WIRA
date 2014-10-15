package com.duggan.workflow.client.ui.admin.outputdocs.save;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.OutputDocument;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SaveOutPutDocsPresenter extends
		PresenterWidget<SaveOutPutDocsPresenter.IOutputDocView> {

	public interface IOutputDocView extends View{

		OutputDocument getOutputDocument();
		void clear();
		void setOutputDoc(OutputDocument doc);
		Uploader getUploader();
	}

	OutputDocument doc=new OutputDocument();
	
	@Inject
	public SaveOutPutDocsPresenter(final EventBus eventBus, final IOutputDocView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getUploader().addOnFinishUploaderHandler(new OnFinishUploaderHandler() {
			
			@Override
			public void onFinish(IUploader uploader) {
				String csv = uploader.getServerMessage().getMessage();
				//Window.alert(csv);
				String[]values = csv.split(",");
				for(String v: values){
					System.out.println(v);
				}
				//document.getId()+","+docName+","+description+","+code+","+attachment.getId()+","+attachment.getName();
				doc.setId(new Long(values[0]));
				doc.setName(values[1]);
				doc.setDescription(values[2]);
				doc.setCode(values[3]);
				doc.setAttachmentId(new Long(values[4]));
				doc.setAttachmentName(values[5]);
				getView().clear();
				getView().setOutputDoc(doc);
			}
		});
	}

	public OutputDocument getOutputDocument() {
		OutputDocument out = getView().getOutputDocument();
		if(doc!=null){
			out.setId(doc.getId());
		}
		return out;
	}

	public void clear() {
		getView().clear();
	}

	public void setOutputDoc(OutputDocument doc) {
		this.doc = doc;
		if(doc!=null){
			getView().setOutputDoc(doc);
		}
	}
	
	
}
