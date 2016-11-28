package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent.AlertLoadHandler;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.shared.model.TaskType;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class InboxPresenter
		extends
		AbstractTaskPresenter<InboxPresenter.IInboxView, InboxPresenter.InboxTaskProxy>
		implements AlertLoadHandler {

	public interface IInboxView extends
			com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView {
	}

	@ProxyCodeSplit
	@NameToken({ NameTokens.inbox, NameTokens.inboxwithparams,NameTokens.inboxwithparamsPerProcess})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface InboxTaskProxy extends
			TabContentProxyPlace<InboxPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	public static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		return new HomeTabData(TaskType.INBOX.name(), "Inbox", "", 1,
				adminGatekeeper);
	}

	@Inject
	public InboxPresenter(EventBus eventBus, IInboxView view,
			InboxTaskProxy proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docViewProvider, dateGroupProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(AlertLoadEvent.getType(), this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		currentTaskType = TaskType.INBOX;
		String filter = request.getParameter("filter", "all");
		if (filter != null) {
			currentTaskType = TaskType.valueOf(filter.toUpperCase());
		}
		getView().setTaskType(currentTaskType);
		super.prepareFromRequest(request);
	}

	@Override
	public void onAlertLoad(AlertLoadEvent event) {
		getView().bindAlerts(event.getAlerts());
	}

}
