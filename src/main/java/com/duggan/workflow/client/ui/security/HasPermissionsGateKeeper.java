package com.duggan.workflow.client.ui.security;

import com.duggan.workflow.client.util.AppContext;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.GatekeeperWithParams;
import com.wira.commons.client.security.CurrentUser;

public class HasPermissionsGateKeeper implements GatekeeperWithParams {

	private final CurrentUser currentUser;
    private String[] requiredRoles;

    @Inject
    public HasPermissionsGateKeeper(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public GatekeeperWithParams withParams(String[] params) {
//    	Window.alert(this+" With Permissions = "+requiredRoles);
        requiredRoles = params;
        return this;
    }

    @Override
    public boolean canReveal() {
        return AppContext.isLoggedIn() && currentUser.hasPermissions(requiredRoles);
    }

	public String[] getRequiredRoles() {
		return requiredRoles;
	}

}
