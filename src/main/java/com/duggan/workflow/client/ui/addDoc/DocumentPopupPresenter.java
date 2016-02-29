package com.duggan.workflow.client.ui.addDoc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.addDoc.doctypeitem.DocTypeItemPresenter;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DocumentPopupPresenter extends
		PresenterWidget<DocumentPopupPresenter.MyView> {
	
	public static final Object DOCITEM_SLOT = new Object();
	
	IndirectProvider<DocTypeItemPresenter> docTypeFactory;
	
	@Inject
	DispatchAsync requestHelper;

	public interface MyView extends View {

		void addSeparator(String category);

		void addContent(String string);
	}
	
	@Inject
	public DocumentPopupPresenter(final EventBus eventBus, final MyView view, Provider<DocTypeItemPresenter> docTypeProvider) {
		super(eventBus, view);
		docTypeFactory = new StandardProvider<DocTypeItemPresenter>(docTypeProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		GetDocumentTypesRequest req=new GetDocumentTypesRequest();
		requestHelper.execute(req,new TaskServiceCallback<GetDocumentTypesResponse>() {
			@Override
			public void processResult(GetDocumentTypesResponse result) {
				List<DocumentType> types=result.getDocumentTypes();
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
	
	public void setDocumentItems(List<DocumentType> types){
		this.setInSlot(DOCITEM_SLOT, null);
		
		String category="";
		for(final DocumentType type : types) {
			if(type.getCategory()!=null && !category.equals(type.getCategory())){
				category = type.getCategory();
				getView().addSeparator(category);
			}else if(type.getCategory()==null && !category.equals("NULL")){
				category="NULL";
				getView().addSeparator("General");
			}
			
			docTypeFactory.get(new ServiceCallback<DocTypeItemPresenter>() {
				@Override
				public void processResult(DocTypeItemPresenter result) {
					result.setDocumentTypes(type);
					DocumentPopupPresenter.this.addToSlot(DOCITEM_SLOT, result);
				}
			});
		}
		
		if(types.size()==0){
			getView().addContent("There are no processes assigned to you.");
		}
	}
}
