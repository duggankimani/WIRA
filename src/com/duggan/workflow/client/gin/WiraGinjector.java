package com.duggan.workflow.client.gin;

import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.dashboard.DashboardPresenter;
import com.duggan.workflow.client.ui.admin.ds.DataSourcePresenter;
import com.duggan.workflow.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.duggan.workflow.client.ui.admin.processes.ProcessPresenter;
import com.duggan.workflow.client.ui.admin.settings.SettingsPresenter;
import com.duggan.workflow.client.ui.admin.users.UserPresenter;
import com.duggan.workflow.client.ui.error.ErrorPagePresenter;
import com.duggan.workflow.client.ui.error.NotfoundPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.ui.login.LoginPresenter;
import com.duggan.workflow.client.ui.task.perfomancereview.PersonnelReviewPresenter;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;

@GinModules({RpcDispatchAsyncModule.class,ClientModule.class})
public interface WiraGinjector extends Ginjector {

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
