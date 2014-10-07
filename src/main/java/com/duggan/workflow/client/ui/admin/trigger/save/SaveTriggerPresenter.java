package com.duggan.workflow.client.ui.admin.trigger.save;

import com.duggan.workflow.shared.model.Trigger;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SaveTriggerPresenter extends
		PresenterWidget<SaveTriggerPresenter.ISaveTriggerView> {

	public interface ISaveTriggerView extends View{

		Trigger getTrigger();
		void clear();
		void setTrigger(Trigger trigger);
		boolean isValid();
	}

	Trigger trigger=null;
	
	@Inject
	public SaveTriggerPresenter(final EventBus eventBus, final ISaveTriggerView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public Trigger getTrigger() {
		Trigger out = getView().getTrigger();
		if(trigger!=null){
			out.setId(trigger.getId());
		}
		return out;
	}

	public void clear() {
		getView().clear();
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
		if(trigger!=null){
			getView().setTrigger(trigger);
		}
	}
	
	public boolean isValid(){
		return getView().isValid();
	}
	
	
}
