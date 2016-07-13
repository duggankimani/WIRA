package com.wira.commons.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.wira.commons.client.security.SecurityModule;

public class CommonsClientModule extends AbstractGinModule{

	@Override
	protected void configure() {
		install(new SecurityModule());
	}
}
