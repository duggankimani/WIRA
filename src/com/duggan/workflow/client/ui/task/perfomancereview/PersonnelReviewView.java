package com.duggan.workflow.client.ui.task.perfomancereview;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * 
 * @author duggan
 *
 */
public class PersonnelReviewView extends ViewImpl implements
		PersonnelReviewPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PersonnelReviewView> {
	}

	@UiField HTMLPanel pToobarContainer;
	
	@Inject
	public PersonnelReviewView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	
}
