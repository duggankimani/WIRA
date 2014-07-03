package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.save.form.GenericFormPresenter;
import com.duggan.workflow.client.ui.task.AbstractTaskPresenter.ITaskView;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class InboxPresenter extends AbstractTaskPresenter<InboxPresenter.IInboxView, InboxPresenter.InboxTaskProxy>{

	public interface IInboxView extends ITaskView{}
	
	@ProxyCodeSplit
	@NameToken({NameTokens.inbox})
	@UseGatekeeper(LoginGateKeeper.class)
	public interface InboxTaskProxy extends TabContentProxyPlace<InboxPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new TabDataExt("Inbox","",1, adminGatekeeper);
    }
	
	@Inject
	public InboxPresenter(EventBus eventBus, IInboxView view,
			InboxTaskProxy proxy, Provider<CreateDocPresenter> docProvider,
			Provider<GenericFormPresenter> formProvider,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, docProvider, formProvider, docViewProvider,
				dateGroupProvider);
		currentTaskType=TaskType.APPROVALREQUESTNEW;
		getView().setTaskType(currentTaskType);
	}

}
