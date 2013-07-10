package com.duggan.workflow.client.ui.task.personalreview;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.duggan.workflow.client.ui.task.personalreview.PersonalReviewPresenter.*;

public class PersonalReviewView extends ViewImpl implements
		PersonalReviewPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PersonalReviewView> {
	}

	@UiField HTMLPanel pToobarContainer;
	
	@UiField TextArea txtWorkAssignment;
	
	@UiField TextArea txtFutureObjectives;
	
	@UiField TextArea txtPerfomanceDiscussion;
	
	@UiField TextArea txtPerformanceDifficulties;
	
	@UiField TextArea txtSuggestions;
	
	@UiField TextArea txtComments;
	
	
	@Inject
	public PersonalReviewView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot==TOOLBAR_content){
			pToobarContainer.clear();
			
			if(content!=null){
				pToobarContainer.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
	}
	

	void setEnabled(boolean enabled){
		txtWorkAssignment.setReadOnly(enabled);
		
		txtFutureObjectives.setReadOnly(enabled);
		
		txtPerfomanceDiscussion.setReadOnly(enabled);
		
		txtPerformanceDifficulties.setReadOnly(enabled);
		
		txtSuggestions.setReadOnly(enabled);
		
		txtComments.setReadOnly(enabled);
	}
}
