package com.duggan.workflow.client.ui.admin.msgs;

import java.util.ArrayList;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.component.Emailer;
import com.duggan.workflow.client.ui.component.Grid;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.shared.model.RequestInfoDto;
import com.duggan.workflow.shared.requests.GetMessagesRequest;
import com.duggan.workflow.shared.requests.SendMessageRequest;
import com.duggan.workflow.shared.responses.GetMessagesResponse;
import com.duggan.workflow.shared.responses.SendMessageResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class MessagesPresenter
		extends
		Presenter<MessagesPresenter.IMessagesView, MessagesPresenter.IMessagesProxy> {

	public interface IMessagesView extends View {
		Grid<RequestInfoDto> getGrid();

		void setData(ArrayList<RequestInfoDto> data, int totalCount);

		Anchor getViewLink();
		
		Anchor getViewErrorLink();

		Anchor getNewLink();
	}

	public static final String MAILLOG_CAN_VIEW_MAILLOG = "MAILLOG_CAN_VIEW_MAILLOG";
	
	@ProxyCodeSplit
	@NameToken(NameTokens.messages)
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({MAILLOG_CAN_VIEW_MAILLOG})
	public interface IMessagesProxy extends
			TabContentProxyPlace<MessagesPresenter> {
	}

	@TabInfo(container = AdminHomePresenter.class)
	static TabData getTabLabel(HasPermissionsGateKeeper gateKeeper) {
		/**
		 * Manually calling gateKeeper.withParams Method.
		 * 
		 * HACK NECESSITATED BY THE FACT THAT Gin injects to different instances of this GateKeeper in 
		 * Presenter.MyProxy->UseGateKeeper & 
		 * getTabLabel(GateKeeper);
		 * 
		 * Test -> 
		 * Window.alert in GateKeeper.canReveal(this+" Params = "+params) Vs 
		 * Window.alert here in getTabLabel.canReveal(this+" Params = "+params) Vs
		 * Window.alert in AbstractTabPanel.refreshTabs(tab.getTabData.getGateKeeper()+" Params = "+params) Vs
		 * 
		 */
		gateKeeper.withParams(new String[]{MAILLOG_CAN_VIEW_MAILLOG});
		TabDataExt ext = new TabDataExt(TABLABEL, "icon-th", 8,
				gateKeeper);
		return ext;
	}

	public static final Object TABLE_SLOT = new Object();

	public static final String TABLABEL = "Email & SMS Messages";

	@Inject
	DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;

	@Inject
	public MessagesPresenter(final EventBus eventBus, final IMessagesView view,
			IMessagesProxy proxy) {
		super(eventBus, view, proxy, AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getNewLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Email", new Emailer(), "htmlEditorPopup",
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {

							}
						}, "Send", "Cancel");
			}
		});

		getView().getViewLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Emailer e = new Emailer(getView().getGrid()
						.getSelectedValue());
				AppManager.showPopUp("Email", e, "htmlEditorPopup",
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								RequestInfoDto dto = e.getRequestInfo();
								if (name.equals("ReSend")) {
									resend(dto);
								}
							}
						}, "ReSend", "Cancel");
			}
		});
		
		getView().getViewErrorLink().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				RequestInfoDto dto = getView().getGrid().getSelectedValue();
				Long errorId= null;
				if(dto.getErrorInfo()!=null && dto.getErrorInfo().size()>0){
					errorId = dto.getErrorInfo().get(dto.getErrorInfo().size()-1).getId();
				}else{
					return;
				}
				placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.error)
						.with("errorid",errorId+"").build());
			}
		});

//		getView().getRetryLink().addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//
//			}
//		});

		getView().getGrid().setDataProvider(
				new AsyncDataProvider<RequestInfoDto>() {
					@Override
					protected void onRangeChanged(
							final HasData<RequestInfoDto> display) {
						final Range range = getView().getGrid()
								.getVisibleRange();
						loadData(range.getStart(), range.getLength());
						
					}

				});

	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadData(0, 10);
	}
	
	public void loadData(int offset, int limit){
		GetMessagesRequest request = new GetMessagesRequest();
		request.setOffset(offset);
		request.setLength(limit);

		requestHelper.execute(request,
				new TaskServiceCallback<GetMessagesResponse>() {
					@Override
					public void processResult(
							GetMessagesResponse aResponse) {
						getView().setData(
								aResponse.getRequests(),
								aResponse.getTotalCount());
					}
				});
	}

	private void resend(RequestInfoDto dto) {
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new SendMessageRequest(dto),
				new TaskServiceCallback<SendMessageResponse>() {
					@Override
					public void processResult(SendMessageResponse aResponse) {
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

}