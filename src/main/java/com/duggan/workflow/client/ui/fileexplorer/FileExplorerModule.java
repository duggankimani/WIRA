package com.duggan.workflow.client.ui.fileexplorer;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class FileExplorerModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(FileExplorerPresenter.class, FileExplorerPresenter.MyView.class, FileExplorerView.class, FileExplorerPresenter.MyProxy.class);
    }
}