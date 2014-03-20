package com.duggan.workflow.client.ui.admin.dashboard;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import static com.duggan.workflow.client.ui.admin.dashboard.DashboardPresenter.*;

public class DashboardView extends ViewImpl implements
		DashboardPresenter.MyView {

	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	private Widget widget;

	@UiField HTMLPanel panelTurnAroundTime;
	@UiField HTMLPanel panelTotalRequestsPerDoc;
	@UiField HTMLPanel panelRequestsApprovalsComp;
	
	@Inject
	public DashboardView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		
		if(slot == OVERALLTURNAROUND_SLOT){
			panelTurnAroundTime.clear();
			if(content!=null){
				panelTurnAroundTime.add(content);
			}
		}else if(slot == REQUESTSPERDOC_SLOT){
			panelTotalRequestsPerDoc.clear();
			if(content!=null){
				panelTotalRequestsPerDoc.add(content);
			}
		}else if(slot == LINEGRAPH_SLOT){
			panelRequestsApprovalsComp.clear();
			if(content!=null){
				panelRequestsApprovalsComp.add(content);
			}
		}else
			super.setInSlot(slot, content);
	}

}
