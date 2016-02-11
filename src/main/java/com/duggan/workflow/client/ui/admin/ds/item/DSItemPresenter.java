package com.duggan.workflow.client.ui.admin.ds.item;

import java.util.Date;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.shared.events.EditDSConfigEvent;
import com.duggan.workflow.shared.events.ProcessingCompletedEvent;
import com.duggan.workflow.shared.events.ProcessingEvent;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.model.RDBMSType;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.requests.DeleteDSConfigurationEvent;
import com.duggan.workflow.shared.requests.GetDSStatusRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDSStatusResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DSItemPresenter extends
		PresenterWidget<DSItemPresenter.MyView> {

	public interface MyView extends View {

		HasClickHandlers getTestButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		void setValues(Long dsConfigId,RDBMSType rdbms,String name,
				String jndiName,String driverName,String url, 
				String user, Date lastModified, Status status);
	}
	
	@Inject DispatchAsync requestHelper;

	DSConfiguration configuration;
	
	@Inject
	public DSItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getTestButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new GetDSStatusRequest(configuration.getName()), 
						new TaskServiceCallback<GetDSStatusResponse>() {
					@Override
					public void processResult(
							GetDSStatusResponse aResponse) {
						setConfiguration(aResponse.getConfigs().get(0));
						fireEvent(new ProcessingCompletedEvent());
					}

				});
			}
		});
		

		getView().getEditButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditDSConfigEvent(configuration));
			}
		});
		
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Delete", 
						"Do you want to delete config \""+configuration.getName()+"\"?",
						new OnOptionSelected() {
							@Override
							public void onSelect(String name) {
								if(name.equals("Yes")){
									requestHelper.execute(new DeleteDSConfigurationEvent(configuration.getId()),
											new TaskServiceCallback<BaseResponse>() {
										public void processResult(BaseResponse aResponse) {
											getView().asWidget().removeFromParent();
										};
									});
								}
							}
				}, "Yes", "Cancel");
			}
		});
		
	}

	public void setConfiguration(DSConfiguration config) {
		this.configuration = config;
		getView().setValues(config.getId(),config.getRDBMS(),config.getName(),
				config.getJNDIName(), config.getDriver()
				,config.getURL(),config.getUser(),config.getLastModified(), config.getStatus());
	}
}
