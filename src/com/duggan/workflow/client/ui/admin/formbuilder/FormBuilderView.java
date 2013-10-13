package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.duggan.workflow.client.ui.admin.formbuilder.component.DragHandlerImpl;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * Form builder class
 * 
 * @author duggan
 *
 */
public class FormBuilderView extends ViewImpl implements
		FormBuilderPresenter.MyView {

	private final Widget widget;
	

	public interface Binder extends UiBinder<Widget, FormBuilderView> {
	}
	
	@UiField Anchor aSaveForm;

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
				container, false){
			@Override
			protected void restoreSelectedWidgetsLocation() {
				//do nothing
			}
		};
		
		widgetDragController.setBehaviorDragStartSensitivity(5);
		widgetDragController.addDragHandler(dragHandler);  			
		
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
		
		aSaveForm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Form form = getForm();
			}
		});
	}
	
	/**
	 * Hello there
	 * @return Widget - Returns parent widget
	 * @author duggan
	 * 
	 */
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	Form getForm(){
		Form form = new Form();
		form.setCaption("default");
		form.setName("default");
		//form.setProperties(properties);
		
		form.setFields(getFields());
		return form;
	}

	private List<Field> getFields() {
		List<Field> fields = new ArrayList<Field>();
		
		int fieldCount = vPanel.getWidgetCount();
		for(int i=0; i<fieldCount; i++){
			Widget w = vPanel.getWidget(i);
			
			if(w instanceof FieldWidget){
				fields.add(((FieldWidget)w).getField());
			}
		}
		
		return fields;
	}
}
