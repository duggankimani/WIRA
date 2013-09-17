package com.duggan.workflow.client.ui.admin.processes;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.admin.addprocess.AddProcessPresenter;
import com.duggan.workflow.client.ui.admin.processrow.ProcessColumnPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ProcessPresenter extends
		PresenterWidget<ProcessPresenter.MyView> {

	public interface MyView extends View {

		HasClickHandlers getaNewProcess();
	}
	
	public static final Object TABLE_SLOT = new Object();
	
	IndirectProvider<AddProcessPresenter> processFactory;
	IndirectProvider<ProcessColumnPresenter> columnFactory;

	@Inject
	public ProcessPresenter(final EventBus eventBus, final MyView view,
			Provider<AddProcessPresenter> addprocessProvider, Provider<ProcessColumnPresenter> columnProvider) {
		super(eventBus, view);
		processFactory = new StandardProvider<AddProcessPresenter>(addprocessProvider);
		columnFactory= new StandardProvider<ProcessColumnPresenter>(columnProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getaNewProcess().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				processFactory.get(new ServiceCallback<AddProcessPresenter>() {
					@Override
					public void processResult(AddProcessPresenter result) {
						addToPopupSlot(result);
					}
				});
			}
		});
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(TABLE_SLOT, null);
		for(int i=0; i<=5; i++){
			columnFactory.get(new AsyncCallback<ProcessColumnPresenter>() {
			
			@Override
			public void onSuccess(ProcessColumnPresenter result) {
				addToSlot(TABLE_SLOT, result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		   });
		}
	}
	
}
