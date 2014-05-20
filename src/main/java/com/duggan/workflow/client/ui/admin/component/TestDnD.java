package com.duggan.workflow.client.ui.admin.component;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestDnD extends Composite {

	private static TestDnDUiBinder uiBinder = GWT.create(TestDnDUiBinder.class);

	interface TestDnDUiBinder extends UiBinder<Widget, TestDnD> {
	}
	
	@UiField AbsolutePanel testabsolutepanel;
	@UiField HTMLPanel dropTarget;
	@UiField HTMLPanel sampleSubject;

	public TestDnD() {
		initWidget(uiBinder.createAndBindUi(this));
		PickupDragController dragController =new PickupDragController(testabsolutepanel, true);
		
		SimpleDropController dropController = new SimpleDropController(dropTarget);
        dragController.registerDropController(dropController);
		   
	    dragController.makeDraggable(sampleSubject);
	}

}
