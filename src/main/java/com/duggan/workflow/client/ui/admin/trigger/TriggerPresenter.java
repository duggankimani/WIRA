package com.duggan.workflow.client.ui.admin.trigger;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.trigger.save.SaveTriggerPresenter;
import com.duggan.workflow.client.ui.events.EditTriggerEvent;
import com.duggan.workflow.client.ui.events.EditTriggerEvent.EditTriggerHandler;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.Trigger;
import com.duggan.workflow.shared.requests.GetTriggersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveTriggerRequest;
import com.duggan.workflow.shared.responses.GetTriggersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class TriggerPresenter extends
		Presenter<TriggerPresenter.ITriggerView, TriggerPresenter.MyProxy>
implements EditTriggerHandler{

	public interface ITriggerView extends View {
		HasClickHandlers getAddTriggerLink();
		HasClickHandlers getCloneTriggerLink();
		void setTriggers(List<Trigger> triggeruments);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.triggers)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<TriggerPresenter>{
	}

	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
        return new TabDataExt("Triggers","icon-wrench",5, adminGatekeeper);
    }
	
	@Inject	SaveTriggerPresenter savePresenter;
	@Inject DispatchAsync requestHelper;
	List<Trigger> triggers;//For Cloning
	
	@Inject
	public TriggerPresenter(final EventBus eventBus, final ITriggerView view,
			final MyProxy proxy ) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditTriggerEvent.TYPE, this);
		getView().getAddTriggerLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showEditPopup(null);
			}
		});
		
		getView().getCloneTriggerLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showClonePopup();
			}
		});
	}
	
	protected void showClonePopup() {
		savePresenter.clear();
		savePresenter.setTriggers(triggers);
		AppManager.showPopUp("Copy Trigger",savePresenter.asWidget(),new OptionControl(){
			@Override
			public void onSelect(String name) {						
				if(name.equals("Save") && savePresenter.isValid()){
					Trigger trigger = savePresenter.getTrigger();
					save(trigger);
					hide();
				}
				
				if(name.equals("Cancel")){
					hide();
				}
			}

		},"Save", "Cancel");
	}

	protected void showEditPopup(Trigger trigger) {
		savePresenter.clear();
		savePresenter.setTrigger(trigger);
		AppManager.showPopUp("Create Trigger",savePresenter.asWidget(),new OptionControl(){
			@Override
			public void onSelect(String name) {						
				if(name.equals("Save") && savePresenter.isValid()){
					Trigger trigger = savePresenter.getTrigger();
					save(trigger);
					hide();
				}
				
				if(name.equals("Cancel")){
					hide();
				}
			}

		},"Save", "Cancel");

	}

	@Override
	protected void onReset() {
		super.onReset();
		requestHelper.execute(new GetTriggersRequest(), new TaskServiceCallback<GetTriggersResponse>() {
			@Override
			public void processResult(GetTriggersResponse aResponse) {
				getView().setTriggers(triggers = aResponse.getTriggers());
			}
		});
	}

	private void save(Trigger trigger) {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new SaveTriggerRequest(trigger));
		requests.addRequest(new GetTriggersRequest());
		requestHelper.execute(requests, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResult) {
				//SaveTriggerResponse aSaveResponse = (SaveTriggerResponse) aResult.get(0);
				GetTriggersResponse aGetTriggersResult = (GetTriggersResponse) aResult.get(1);
				getView().setTriggers(triggers = aGetTriggersResult.getTriggers());
			}
		});
	}

	@Override
	public void onEditTrigger(EditTriggerEvent event) {
		final Trigger trigger = event.getTrigger();
		if(!trigger.isActive()){
			//deleting
			AppManager.showPopUp("Delete '"+trigger.getName()+"'", "Do you want to delete this trigger?",
					new OnOptionSelected() {
						@Override
						public void onSelect(String name) {
							if(name.equals("Yes")){
								save(trigger);
							}
						}
					}, "Yes","Cancel");
			
		}else{
			showEditPopup(trigger);
		}
	}
}
