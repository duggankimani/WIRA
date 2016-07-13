package com.wira.login.client.activateaccount;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ActivateAccountModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ActivateAccountPresenter.class, ActivateAccountPresenter.IActivateAccountView.class, ActivateAccountView.class, ActivateAccountPresenter.IActivateAccountProxy.class);
    }
}