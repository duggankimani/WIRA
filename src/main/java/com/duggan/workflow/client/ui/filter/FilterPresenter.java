package com.duggan.workflow.client.ui.filter;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.home.HomeView;
import com.duggan.workflow.shared.events.SearchEvent;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class FilterPresenter extends PresenterWidget<FilterPresenter.MyView> {

	public interface MyView extends View {
		HasClickHandlers getCloseButton();
		HasClickHandlers getSearchButton();
		HasBlurHandlers getFilterDialog();
		SearchFilter getSearchFilter();
		void setDocTypes(ArrayList<DocumentType> documentTypes);
	}
	
	@Inject HomeView homeview;
	private String subject;
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public FilterPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				SearchFilter filter = getView().getSearchFilter();
				
				if(!filter.isEmpty())
				fireEvent(new SearchEvent(filter));
				subject=filter.getSubject();
				if(subject!=""){
					//homeview.setSearchBox(subject);
				}
			}
		});
		
		getView().getFilterDialog().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//homeview.hideFilterDialog();
			}
		});
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		
		loadDocTypes();
	}

	private void loadDocTypes() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetDocumentTypesRequest());
		
		requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				
				public void processResult(MultiRequestActionResult responses) {
				
					GetDocumentTypesResponse response = (GetDocumentTypesResponse)responses.get(0);
					getView().setDocTypes(response.getDocumentTypes());
				}
		});
	}

	public void addCloseHandler(ClickHandler clickHandler) {
		getView().getCloseButton().addClickHandler(clickHandler);
	}
}
