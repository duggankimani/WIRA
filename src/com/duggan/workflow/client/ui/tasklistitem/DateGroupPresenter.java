package com.duggan.workflow.client.ui.tasklistitem;


import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.events.PresentTaskEvent;
import com.duggan.workflow.client.ui.events.PresentTaskEvent.PresentTaskHandler;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DateGroupPresenter extends
		PresenterWidget<DateGroupPresenter.MyView> implements PresentTaskHandler{

	public interface MyView extends View {
		void setDate(String date);
	}

	public static final Object ITEM_SLOT = new Object();
	
	private IndirectProvider<TaskItemPresenter> presenterProvider;
	
	String date;
	
	@Inject
	public DateGroupPresenter(final EventBus eventBus, final MyView view, Provider<TaskItemPresenter> provider) {
		super(eventBus, view);
		presenterProvider = new StandardProvider<TaskItemPresenter>(provider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(PresentTaskEvent.TYPE, this);
		//addRegisteredHandler(ClearTasksEvent.TYPE, this);
	}
	
	public void setDate(String dt){
		this.date = dt;
		getView().setDate(dt);
	}
	
	@Override
	public void onPresentTask(final PresentTaskEvent event) {
		String dateFormatted = DateUtils.DATEFORMAT.format(event.getDoc().getCreated());
		
		if(!dateFormatted.equals(date)){
			return;
		}
				
		System.err.println("Added item ## "+event.getDoc().getSubject()+" :: "+DateGroupPresenter.this.toString());
		presenterProvider.get(new ServiceCallback<TaskItemPresenter>() {
			
			@Override
			public void processResult(TaskItemPresenter result) {
				result.setDocSummary(event.getDoc());
				DateGroupPresenter.this.addToSlot(ITEM_SLOT, result);
			}
			
		});			
	}
		
	
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}
	
}
