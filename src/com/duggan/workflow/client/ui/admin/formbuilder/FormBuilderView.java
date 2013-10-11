package com.duggan.workflow.client.ui.admin.formbuilder;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.duggan.workflow.client.ui.admin.formbuilder.component.DragHandlerImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class FormBuilderView extends ViewImpl implements
		FormBuilderPresenter.MyView {

	private final Widget widget;
	

	public interface Binder extends UiBinder<Widget, FormBuilderView> {
	}

	@UiField AbsolutePanel container;
	@UiField VerticalPanel vPanel;
	
	@UiField PalettePanel vTextInputPanel;
	@UiField PalettePanel vDatePanel;
	@UiField PalettePanel vTextAreaPanel;
	@UiField PalettePanel vInlineRadioPanel;
	@UiField PalettePanel vInlineCheckBoxPanel;
	@UiField PalettePanel vSelectBasicPanel;
	@UiField PalettePanel vSelectMultiplePanel;
	@UiField PalettePanel vSingleButtonPanel;
	@UiField PalettePanel vMultipleButtonPanel;
	

	PickupDragController widgetDragController;
	
	@Inject
	public FormBuilderView(final Binder binder) {
		
		widget = binder.createAndBindUi(this);
		
		DragHandlerImpl dragHandler = new DragHandlerImpl(this.asWidget());
		
		/*set up pick-up and move
		 * parameters: absolutePanel, boolean(whether items can be placed to any location)
		 *  */
		
		widgetDragController = new PickupDragController(
				container, false);	
		
		widgetDragController.setBehaviorMultipleSelection(false);
		widgetDragController.addDragHandler(dragHandler);  
		widgetDragController.setBehaviorBoundaryPanelDrop(false);
			
		
		vTextInputPanel.registerDragController(widgetDragController);
		vDatePanel.registerDragController(widgetDragController);
		vTextAreaPanel.registerDragController(widgetDragController);
		vInlineRadioPanel.registerDragController(widgetDragController);
		vInlineCheckBoxPanel.registerDragController(widgetDragController);
		vSelectBasicPanel.registerDragController(widgetDragController);
		vSelectMultiplePanel.registerDragController(widgetDragController);
		vSingleButtonPanel.registerDragController(widgetDragController);
		vMultipleButtonPanel.registerDragController(widgetDragController);
		
		VerticalPanelDropController widgetDropController = new VerticalPanelDropController(vPanel);
		widgetDragController.registerDropController(widgetDropController);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	
}
