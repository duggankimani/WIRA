package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
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
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS})
	public interface IUnAssignedProxy extends TabContentProxyPlace<UnAssignedPresenter> {
	}
	
	public static final String UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS = "UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS";
	
	@TabInfo(container = HomePresenter.class)
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
		gateKeeper.withParams(new String[]{UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS});
        return new HomeTabData(TaskType.UNASSIGNED.name(),"UnAssigned","",5, gateKeeper);
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
