package com.duggan.workflow.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.duggan.workflow.client.ui.admin.outputdocs.OutPutDocsPresenter;

@GinModules({RpcDispatchAsyncModule.class,ClientModule.class})
public interface WiraGinjector extends Ginjector {
	//AsyncProvider<TaskPresenter> getTaskPresenter();

	//EventBus getEventBus();
//
	//PlaceManager getPlaceManager();

//	AsyncProvider<MainPagePresenter> getMainPagePresenter();
//
//	AsyncProvider<HomePresenter> getTaskListUIPresenter();
//
//	AsyncProvider<PersonnelReviewPresenter> getPersonnelReviewPresenter();
//	
//	AsyncProvider<ErrorPagePresenter> getErrorPagePresenter();
//
//	AsyncProvider<LoginPresenter> getLoginPresenter();
//	
//	Provider<AdminHomePresenter> getAdminHomePresenter();
//	
//	AsyncProvider<UserPresenter> getUserPresenter();
//	
//	AsyncProvider<DashboardPresenter> getDashboardPresenter();
//	
//	AsyncProvider<SettingsPresenter> getSettingsPresenter();
//	
//	AsyncProvider<ProcessPresenter> getProcessPresenter();
//	
//	AsyncProvider<FormBuilderPresenter> getFormBuilderPresenter();
//	
//	AsyncProvider<DataSourcePresenter> getDataSourcePresenter();
//	
//	LoginGateKeeper getLoggedInGateKeeper();
//	
//	AsyncProvider<NotfoundPresenter> getNotfoundPresenter();

}
