package com.duggan.workflow.client.ui.admin.outputdocs;

import org.apache.directory.server.core.subtree.SubentryInterceptor.HideEntriesFilter;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.outputdocs.save.OutPutDocsSavePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class OutPutDocsPresenter extends
		Presenter<OutPutDocsPresenter.MyView, OutPutDocsPresenter.MyProxy> {

	public interface MyView extends View {
		HasClickHandlers getDocumentButton();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.outputdocs)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<OutPutDocsPresenter>{
	}

	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new TabDataExt("Output Documents","icon-cogs",5, adminGatekeeper);
    }
	
	@Inject
	OutPutDocsSavePresenter saveProvider;
	
	@Inject
	public OutPutDocsPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy ) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getDocumentButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Create Output Document",saveProvider.asWidget(),new OptionControl(){
					@Override
					public void onSelect(String name) {
						hide();
						if(name.equals("Save")){
						}
					}
				},"Save", "Cancel");
			}
		});
	}
}
