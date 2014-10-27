package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView;
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

public class DraftsPresenter extends AbstractTaskPresenter<DraftsPresenter.IDraftsView, DraftsPresenter.IDraftsProxy>{

	public interface IDraftsView extends ITaskView{}
	
	@ProxyCodeSplit
	@NameToken({NameTokens.drafts})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IDraftsProxy extends TabContentProxyPlace<DraftsPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new HomeTabData(TaskType.DRAFT.name(),"Drafts","",2, adminGatekeeper);
    }
	
	@Inject
	public DraftsPresenter(EventBus eventBus, IDraftsView view,
			IDraftsProxy proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docViewProvider,
				dateGroupProvider);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		currentTaskType=TaskType.DRAFT;
		getView().setTaskType(currentTaskType);
		super.prepareFromRequest(request);
	}

}
