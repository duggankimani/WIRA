package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
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

public class UnAssignedPresenter extends AbstractTaskPresenter<UnAssignedPresenter.IUnAssignedView, UnAssignedPresenter.IUnAssignedProxy>{

	public interface IUnAssignedView extends com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView{}
	
	@ProxyCodeSplit
	@NameToken({NameTokens.unassigned})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IUnAssignedProxy extends TabContentProxyPlace<UnAssignedPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new HomeTabData(TaskType.UNASSIGNED.name(),"UnAssigned","",5, adminGatekeeper);
    }
	
	@Inject
	public UnAssignedPresenter(EventBus eventBus, IUnAssignedView view,
			IUnAssignedProxy proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docViewProvider,
				dateGroupProvider);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		currentTaskType=TaskType.UNASSIGNED;
		getView().setTaskType(currentTaskType);
		super.prepareFromRequest(request);
	}

}
