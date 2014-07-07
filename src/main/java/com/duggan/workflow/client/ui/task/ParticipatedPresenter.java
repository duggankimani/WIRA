package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
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

public class ParticipatedPresenter extends AbstractTaskPresenter<ParticipatedPresenter.IParticipatedView, ParticipatedPresenter.INewTaskProxy>{

	public interface IParticipatedView extends com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView{}
	
	@ProxyCodeSplit
	@NameToken({NameTokens.participated})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface INewTaskProxy extends TabContentProxyPlace<ParticipatedPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new HomeTabData(TaskType.PARTICIPATED.name(),"Participated","",3, adminGatekeeper);
    }
	
	@Inject
	public ParticipatedPresenter(EventBus eventBus, IParticipatedView view,
			INewTaskProxy proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docViewProvider,
				dateGroupProvider);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		currentTaskType=TaskType.PARTICIPATED;
		getView().setTaskType(currentTaskType);
		super.prepareFromRequest(request);
	}

}
