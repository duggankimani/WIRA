package com.wira.login.client;

import com.duggan.workflow.client.place.NameTokens;
import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;
import com.wira.login.client.activateaccount.ActivateAccountModule;
import com.wira.login.client.signin.LoginModule;

public class LoginClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new RpcDispatchAsyncModule.Builder().build());
		install(new DefaultModule.Builder()
				.tokenFormatter(RouteTokenFormatter.class).build());
		
		install(new LoginModule());
		install(new ActivateAccountModule());
		
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.signin);
		bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.signin);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(
				NameTokens.signin);
	}
	
}
