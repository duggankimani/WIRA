package com.duggan.workflow.client.ui.filter;

import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.home.HomeView;
import com.duggan.workflow.shared.model.SearchFilter;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class FilterPresenter extends PresenterWidget<FilterPresenter.MyView> {

	public interface MyView extends View {
		HasClickHandlers getCloseButton();
		HasClickHandlers getSearchButton();
		HasBlurHandlers getFilterDialog();
		SearchFilter getSearchFilter();
	}
	
	@Inject HomeView homeview;
	private String subject;

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
					homeview.setSearchBox(subject);
				}
				homeview.hideFilterDialog();
			}
		});
		
		getView().getCloseButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				homeview.hideFilterDialog();
			}
		});
		
		getView().getFilterDialog().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//homeview.hideFilterDialog();
			}
		});
	}
}
