package com.duggan.workflow.client.ui.addDoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class DocTypesPresenter extends
		PresenterWidget<DocTypesPresenter.MyView> {
	
	public static final Object DOCITEM_SLOT = new Object();
	
	@Inject
	DispatchAsync requestHelper;

	public interface MyView extends View {

		void addSeparator(String category);

		void addContent(String string);

		void setData(ArrayList<ProcessCategory> categories);
	}
	
	@Inject
	public DocTypesPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}	
	
	public void load(){
		GetDocumentTypesRequest req=new GetDocumentTypesRequest();
		requestHelper.execute(req,new TaskServiceCallback<GetDocumentTypesResponse>() {
			@Override
			public void processResult(GetDocumentTypesResponse result) {
				ArrayList<DocumentType> types=result.getDocumentTypes();
				Collections.sort(types, new Comparator<DocumentType>() {
					@Override
					public int compare(DocumentType o1, DocumentType o2) {
						
						String o1Category = o1.getCategory();
						String o2Category = o2.getCategory();
						
						if(o1Category==null){
							return -1;
						}
						
						if(o2Category==null){
							return 1;
						}
						
						return o1Category.compareTo(o2Category);
					}
				});
				
				setDocumentItems(types);
			}
		});
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		load();
	}
	
	public void setDocumentItems(ArrayList<DocumentType> types){
		this.setInSlot(DOCITEM_SLOT, null);
		
		ArrayList<ProcessCategory> categories  = new ArrayList<ProcessCategory>();
		
		String category="";
		ProcessCategory processCat = new ProcessCategory();
		for(final DocumentType type : types) {
			if(type.getCategory()!=null && !category.equals(type.getCategory())){
				category = type.getCategory();
				//getView().addSeparator(category);
				processCat = new ProcessCategory();
				processCat.setName(category);
				categories.add(processCat);
			}else if(type.getCategory()==null && !category.equals("NULL")){
				category="General";
				//getView().addSeparator("General");
				processCat = new ProcessCategory();
				processCat.setName(category);
				categories.add(processCat);
			}
			
			processCat.addChild(type);
			
//			docTypeFactory.get(new ServiceCallback<DocTypeItemPresenter>() {
//				@Override
//				public void processResult(DocTypeItemPresenter result) {
//					result.setDocumentTypes(type);
//					DocumentPopupPresenter.this.addToSlot(DOCITEM_SLOT, result);
//				}
//			});
		}
		
		if(types.size()==0){
			getView().addContent("There are no processes assigned to you.");
		}else{
			getView().setData(categories);
		}
	}
	
//	public void setDocumentItems(ArrayList<DocumentType> types){
//		this.setInSlot(DOCITEM_SLOT, null);
//		
//		String category="";
//		for(final DocumentType type : types) {
//			if(type.getCategory()!=null && !category.equals(type.getCategory())){
//				category = type.getCategory();
//				getView().addSeparator(category);
//			}else if(type.getCategory()==null && !category.equals("NULL")){
//				category="NULL";
//				getView().addSeparator("General");
//			}
//			
//			docTypeFactory.get(new ServiceCallback<DocTypeItemPresenter>() {
//				@Override
//				public void processResult(DocTypeItemPresenter result) {
//					result.setDocumentTypes(type);
//					DocumentPopupPresenter.this.addToSlot(DOCITEM_SLOT, result);
//				}
//			});
//		}
//		
//		if(types.size()==0){
//			getView().addContent("There are no processes assigned to you.");
//		}
//	}
}
