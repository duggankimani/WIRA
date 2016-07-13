package com.duggan.workflow.client.ui.admin.ds.save;

import com.duggan.workflow.client.ui.events.LoadDSConfigsEvent;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.model.RDBMSType;
import com.duggan.workflow.shared.requests.SaveDSConfigRequest;
import com.duggan.workflow.shared.responses.SaveDSConfigResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class DSSavePresenter extends PresenterWidget<DSSavePresenter.IDSSaveView>{

	public interface IDSSaveView extends PopupView {
		HasClickHandlers getSave();
		boolean isValid();
		DSConfiguration getConfiguration();
		void setConfigurationId(Long id);
		void setValues(Long dsConfigId,
				RDBMSType rdbms,String name,String jndiName,String driverName,String url, 
				String user, String password, boolean isJNDI);

	}

	@Inject DispatchAsync requestHelper; 
	
	DSConfiguration config = null;
	
	@Inject
	public DSSavePresenter(final EventBus eventBus, final IDSSaveView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getSave().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					
					DSConfiguration configuration = getView().getConfiguration();
					assert configuration.getName()!=null;		
					assert configuration.getRDBMS()!=null;
					SaveDSConfigRequest request = new SaveDSConfigRequest(configuration);
					
					requestHelper.execute(request, new TaskServiceCallback<SaveDSConfigResponse>() {
						@Override
						public void processResult(SaveDSConfigResponse result) {
							DSConfiguration config = result.getConfiguration();
							DSSavePresenter.this.config = config;
							getView().setConfigurationId(config.getId());
							fireEvent(new LoadDSConfigsEvent());
							getView().hide();
						}
					});
				}
			}
		});		
		
	}

	public void setConfiguration(DSConfiguration config) {
		this.config = config;
		if(config!=null){
			getView().setValues(config.getId(), config.getRDBMS(),config.getName(), config.getJNDIName(),
				config.getDriver(), config.getURL(), config.getUser(), config.getPassword(), config.isJNDI());
		}
	}
	
}
