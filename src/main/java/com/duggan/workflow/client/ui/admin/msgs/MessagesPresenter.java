package com.duggan.workflow.client.ui.admin.msgs;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.component.Grid;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.RequestInfoDto;
import com.duggan.workflow.shared.requests.GetMessagesRequest;
import com.duggan.workflow.shared.responses.GetMessagesResponse;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
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

public class MessagesPresenter
		extends
		Presenter<MessagesPresenter.IMessagesView, MessagesPresenter.IMessagesProxy> {

	public interface IMessagesView extends View {
		Grid<RequestInfoDto> getGrid();
		void setData(List<RequestInfoDto> data, int totalCount);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.messages)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IMessagesProxy extends
			TabContentProxyPlace<MessagesPresenter> {
	}

	@TabInfo(container = AdminHomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt ext = new TabDataExt("Email & SMS Messages", "icon-th", 8,
				adminGatekeeper);
		return ext;
	}

	public static final Object TABLE_SLOT = new Object();

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public MessagesPresenter(final EventBus eventBus, final IMessagesView view,
			IMessagesProxy proxy) {
		super(eventBus, view, proxy, AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getGrid().setDataProvider(
				new AsyncDataProvider<RequestInfoDto>() {
					@Override
					protected void onRangeChanged(
							final HasData<RequestInfoDto> display) {
						final Range range = getView().getGrid()
								.getVisibleRange();
						
						GetMessagesRequest request  = new GetMessagesRequest();
						request.setOffset(range.getStart());
						request.setLength(range.getLength());
						
						requestHelper.execute(request, new TaskServiceCallback<GetMessagesResponse>() {
							@Override
							public void processResult(
									GetMessagesResponse aResponse) {
								getView().setData(aResponse.getRequests(), aResponse.getTotalCount());
							}
						});
					}

				});

	}

}