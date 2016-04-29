package com.duggan.workflow.client.ui.security;

import com.duggan.workflow.client.security.CurrentUser;
import com.duggan.workflow.client.util.AppContext;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.GatekeeperWithParams;

public class HasAllPermissionsGateKeeper implements GatekeeperWithParams {

	private final CurrentUser currentUser;
    private String[] requiredRoles;

    @Inject
    public HasAllPermissionsGateKeeper(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public GatekeeperWithParams withParams(String[] params) {
        requiredRoles = params;
        return this;
    }

    @Override
    public boolean canReveal() {
        return AppContext.isLoggedIn() && currentUser.hasPermissions(requiredRoles);
    }

}
