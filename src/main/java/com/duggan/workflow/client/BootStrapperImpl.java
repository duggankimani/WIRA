package com.duggan.workflow.client;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class BootStrapperImpl implements Bootstrapper {

	
	private final PlaceManager placeManager;

    @Inject
    public BootStrapperImpl(PlaceManager placeManager) {
        this.placeManager = placeManager;
    }

    @Override
    public void onBootstrap() {
        doSomeCustomLogic();
        placeManager.revealCurrentPlace();
    }

    private void doSomeCustomLogic() {
       // ...
    }
}
