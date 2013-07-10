package com.duggan.workflow.client.ui.toolbar;

import java.util.List;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.HTStatus;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ToolbarView extends ViewImpl implements ToolbarPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ToolbarView> {
	}

	@UiField HasClickHandlers aClaim;
	@UiField HasClickHandlers aStart;
	@UiField HasClickHandlers aSuspend;
	@UiField HasClickHandlers aResume;
	@UiField HasClickHandlers aComplete;
	@UiField HasClickHandlers aDelegate;
	@UiField HasClickHandlers aRevoke;
	@UiField HasClickHandlers aStop;
	@UiField HasClickHandlers aForward;
	
	@Inject
	public ToolbarView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		clearAll();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setTaskStatus(HTStatus status) {
		String style="gwt-Anchor";
		
		List<Actions> actions = status.getValidActions();
		
		if(actions!=null)
		for(Actions action : actions){
			HasClickHandlers target=null;
			
			switch(action){
			case CLAIM:
				target=aClaim;
				break;
			case COMPLETE:
				target=aComplete;
				break;
			case DELEGATE:
				target=aDelegate;
				break;
			case FORWARD:
				target=aForward;
				break;
			case RESUME:
				target=aResume;
				break;
			case REVOKE:
				target=aRevoke;
				break;
			case START:
				target=aStart;
				break;
			case STOP:
				target=aStop;
				break;
			case SUSPEND:
				target=aSuspend;
				break;
			}
			
			if(target!=null){
				((Widget)target).setStyleName(style);
			}
		}
	}

	public HasClickHandlers getClaimLink(){
		return aClaim;
	}
	
	public HasClickHandlers getStartLink(){
		return aStart;
	}
	
	public HasClickHandlers getSuspendLink(){
		return aSuspend;
	}
	
	public HasClickHandlers getResumeLink(){
		return aResume;
	}
	
	public HasClickHandlers getCompleteLink(){
		return aComplete;
	}
	
	public HasClickHandlers getDelegateLink(){
		return aDelegate;
	}
	
	public HasClickHandlers getRevokeLink(){
		return aRevoke;
	}
	
	public HasClickHandlers getStopLink(){
		return aStop;
	}
	
	public HasClickHandlers getForwardLink(){
		return aForward;
	}
	

	private void clearAll() {
		((Widget)aClaim).setStyleName("hidden");
		((Widget)aStart).setStyleName("hidden");
		((Widget)aSuspend).setStyleName("hidden");
		((Widget)aResume).setStyleName("hidden");
		((Widget)aComplete).setStyleName("hidden");
		((Widget)aDelegate).setStyleName("hidden");
		((Widget)aRevoke).setStyleName("hidden");
		((Widget)aStop).setStyleName("hidden");
		((Widget)aForward).setStyleName("hidden");
	}
}
