package com.duggan.workflow.client.ui.admin.settings;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SettingsPresenter extends
		PresenterWidget<SettingsPresenter.ISettingsView> {

	public interface ISettingsView extends View {
		public void setValues(List<Setting> settings);
		public List<Setting> getSettings();
		boolean isValid();
		HasClickHandlers getSaveLink();
	}
	
	@Inject DispatchAsync requestHelper;

	@Inject
	public SettingsPresenter(final EventBus eventBus, final ISettingsView view) {
		super(eventBus, view);
	}


	@Override
	protected void onBind() {
		super.onBind();
		getView().getSaveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					
					requestHelper.execute(new SaveSettingsRequest(getView().getSettings()),
							new TaskServiceCallback<SaveSettingsResponse>() {
						@Override
						public void processResult(
								SaveSettingsResponse aResponse) {
							getView().setValues(aResponse.getSettings());
						}
					}); 
				}
			}
		});
		
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadSettings();
	}

	boolean loaded = false;
	private void loadSettings() {
		if(loaded){
			return;
		}
		loaded=true;
		requestHelper.execute(new GetSettingsRequest(), new TaskServiceCallback<GetSettingsResponse>() {
			@Override
			public void processResult(GetSettingsResponse aResponse) {
				getView().setValues(aResponse.getSettings());
			}
		});
	}
}
