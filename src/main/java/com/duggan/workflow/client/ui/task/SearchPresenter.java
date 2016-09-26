package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
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

public class SearchPresenter extends AbstractTaskPresenter<SearchPresenter.ISearchView, SearchPresenter.ISearchProxy>{

	public interface ISearchView extends com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView{}
	
	@ProxyCodeSplit
	@NameToken({NameTokens.search,NameTokens.searchTask})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ISearchProxy extends TabContentProxyPlace<SearchPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		HomeTabData data = new HomeTabData(TaskType.SEARCH.name(),"Search","",6, adminGatekeeper,false);
        return data;
    }
	
	@Inject
	public SearchPresenter(EventBus eventBus, ISearchView view,
			ISearchProxy proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docViewProvider,
				dateGroupProvider);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		currentTaskType=TaskType.SEARCH;
		getView().setTaskType(currentTaskType);
		String formMode = request.getParameter("mode", null);
		if(formMode!=null){
			try{
				mode = MODE.valueOf(formMode.toUpperCase());
			}catch(Exception e){}
			
		}
		
		super.prepareFromRequest(request);
	}

}
