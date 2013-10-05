package com.duggan.workflow.client.ui.admin.formbuilder;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.duggan.workflow.client.ui.component.ext.DragHandlerImpl;
import com.duggan.workflow.client.ui.component.ext.textfield.TextField;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FormBuilderView extends ViewImpl implements
		FormBuilderPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FormBuilderView> {
	}

	@UiField AbsolutePanel container;
	@UiField VerticalPanel vPanel;
	
	@UiField PalettePanel vPalettePanel;

	PickupDragController widgetDragController;
	
	@Inject
	public FormBuilderView(final Binder binder) {
		
		widget = binder.createAndBindUi(this);
		
		DragHandlerImpl dragHandler = new DragHandlerImpl(this.asWidget());
		
		widgetDragController = new PickupDragController(
				container, false);		
		widgetDragController.setBehaviorMultipleSelection(false);
		widgetDragController.addDragHandler(dragHandler);
		
		vPalettePanel.registerDragController(widgetDragController);
		
		//vPanel.getElement().getStyle().setBackgroundColor("blue");
		VerticalPanelDropController widgetDropController = new VerticalPanelDropController(
				vPanel);
		widgetDragController.registerDropController(widgetDropController);
		
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
}
