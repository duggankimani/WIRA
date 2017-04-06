package com.duggan.workflow.client.ui.admin.trigger;

import java.util.ArrayList;

import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.admin.trigger.save.SaveTriggerView;
import com.duggan.workflow.client.ui.events.EditTriggerEvent;
import com.duggan.workflow.client.ui.events.EditTriggerEvent.EditTriggerHandler;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
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
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class TriggerPresenter extends
		Presenter<TriggerPresenter.ITriggerView, TriggerPresenter.MyProxy>
		implements EditTriggerHandler, SearchHandler{

	public interface ITriggerView extends View {
		HasClickHandlers getAddTriggerLink();
		HasClickHandlers getCloneTriggerLink();
		void setTriggers(ArrayList<Trigger> triggeruments);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.triggers)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<TriggerPresenter> {
	}

	@Inject
	DispatchAsync requestHelper;
	ArrayList<Trigger> triggers;// For Cloning
	private String processRefId;

	@Inject
	public TriggerPresenter(final EventBus eventBus, final ITriggerView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, BaseProcessPresenter.CONTENT_SLOT);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditTriggerEvent.TYPE, this);
		addRegisteredHandler(SearchEvent.getType(), this);
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
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		fireEvent(new ProcessChildLoadedEvent(this));
		processRefId = request.getParameter("p",null);
		load(null);
	}

	protected void showClonePopup() {
		final SaveTriggerView view = new SaveTriggerView();
		view.setTriggers(triggers);
		AppManager.showPopUp("Copy Trigger", view,
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save") && view.isValid()) {
							Trigger trigger = view.getTrigger();
							trigger.setId(null);//Generate new
							save(trigger);
							hide();
						}

						if (name.equals("Cancel")) {
							hide();
						}
					}

				}, "Save", "Cancel");
	}

	protected void showEditPopup(final Trigger trigger) {
		final SaveTriggerView view = new SaveTriggerView(trigger);
		
		AppManager.showPopUp(trigger != null ? "Edit Trigger"
				: "Create Trigger", view,
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save") && view.isValid()) {
							Trigger trigger = view.getTrigger();
							save(trigger);
							hide();
						}

						if (name.equals("Cancel")) {
							hide();
						}
					}

				}, "Save", "Cancel");

	}

	protected void load(String searchTerm) {
		GetTriggersRequest request = new GetTriggersRequest(processRefId);
		request.setSearchTerm(searchTerm);
		requestHelper.execute(request,
				new TaskServiceCallback<GetTriggersResponse>() {
					@Override
					public void processResult(GetTriggersResponse aResponse) {
						getView().setTriggers(
								triggers = aResponse.getTriggers());
					}
				});
	}

	private void save(Trigger trigger) {
		trigger.setProcessRefId(processRefId);
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new SaveTriggerRequest(trigger));
		requests.addRequest(new GetTriggersRequest(processRefId));
		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResult) {
						// SaveTriggerResponse aSaveResponse =
						// (SaveTriggerResponse) aResult.get(0);
						GetTriggersResponse aGetTriggersResult = (GetTriggersResponse) aResult
								.get(1);
						getView().setTriggers(
								triggers = aGetTriggersResult.getTriggers());
					}
				});
	}

	@Override
	public void onEditTrigger(EditTriggerEvent event) {
		if(!isVisible()){
			return;
		}
		
		final Trigger trigger = event.getTrigger();
		if (!trigger.isActive()) {
			// deleting
			AppManager.showPopUp("Delete Trigger",
					"Do you want to delete trigger '"+trigger.getName()+"'?",
					new OnOptionSelected() {
						@Override
						public void onSelect(String name) {
							if (name.equals("Yes")) {
								save(trigger);
							}
						}
					}, "Yes", "Cancel");

		} else {
			showEditPopup(trigger);
		}
	}
	
	@Override
	public void onSearch(SearchEvent event) {
		if(isVisible()){
			load(event.getFilter().getPhrase());
		}
	}
}
