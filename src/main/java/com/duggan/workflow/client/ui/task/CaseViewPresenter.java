package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.save.form.GenericFormPresenter;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.shared.model.MODE;
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

public class CaseViewPresenter extends AbstractTaskPresenter<CaseViewPresenter.ICaseView, CaseViewPresenter.ICaseViewProxy>{

	public interface ICaseView extends com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView{}
	
	@ProxyCodeSplit
	@NameToken({NameTokens.caseview})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ICaseViewProxy extends TabContentProxyPlace<CaseViewPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new HomeTabData("caseview","Case View","",1, adminGatekeeper,false);
    }
	
	@Inject
	public CaseViewPresenter(EventBus eventBus, ICaseView view,
			ICaseViewProxy proxy, Provider<CreateDocPresenter> docProvider,
			Provider<GenericFormPresenter> formProvider,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docViewProvider,
				dateGroupProvider);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		currentTaskType=TaskType.SEARCH;
		getView().setTaskType(currentTaskType);
		mode = MODE.VIEW;
		super.prepareFromRequest(request);
	}
	
	boolean isLoadAsAdmin(){
		return true;
	}

}
