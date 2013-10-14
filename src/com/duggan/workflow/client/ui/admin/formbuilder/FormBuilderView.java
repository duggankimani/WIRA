package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.formbuilder.component.DragHandlerImpl;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
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
	@UiField AbsolutePanel container;
	@UiField VerticalPanel vPanel;
	@UiField DropDownList<Form> frmDropdown;
	
	@UiField Anchor aInputtab;
	@UiField Anchor aSelecttab;
	@UiField Anchor aButtontab;
	@UiField Anchor aMinimize;
	@UiField LIElement liSelect;
	@UiField LIElement liInput;
	@UiField LIElement liButton;
	@UiField Element hPaletetitle;
	@UiField InlineLabel formLabel;
	
	@UiField HTMLPanel divButtons;
	@UiField HTMLPanel divSelect;
	@UiField HTMLPanel divInput;
	@UiField HTMLPanel divPalettePanel;
	@UiField HTMLPanel divFormContent;
	@UiField DivElement divPaletteBody;
	
	@UiField PalettePanel vTextInputPanel;
	@UiField PalettePanel vDatePanel;
	@UiField PalettePanel vTextAreaPanel;
	@UiField PalettePanel vInlineRadioPanel;
	@UiField PalettePanel vInlineCheckBoxPanel;
	@UiField PalettePanel vSelectBasicPanel;
	@UiField PalettePanel vSelectMultiplePanel;
	@UiField PalettePanel vSingleButtonPanel;
	@UiField PalettePanel vMultipleButtonPanel;
	
	public interface Binder extends UiBinder<Widget, FormBuilderView> {
	}

	PickupDragController widgetDragController;
	boolean IsMinimized;
	protected List<Property> properties = new ArrayList<Property>();
	protected List<Form> dropDownItems = new ArrayList<Form>();
	
	@Inject
	public FormBuilderView(final Binder binder) {
		/**
		 * Switching between the tabs	
		 */
	widget = binder.createAndBindUi(this);
	
	dropDownItems.add(new Form(0,"leave-process","Leave Application"));
	dropDownItems.add(new Form(1,"leave-process","Invoice"));
	dropDownItems.add(new Form(2,"leave-process","LPO Approval"));
	
	frmDropdown.setItems(dropDownItems);
	
	setFormProperties();
	formLabel.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event){
			
			/*set the position of the pop-up to be displayed in % */
			int top=7;
			int left=60;
			int arrowposition =formLabel.getAbsoluteTop()-30;
			AppManager.showPropertyPanel(properties, top, left, arrowposition);
		}
	});	
		
	DragHandlerImpl dragHandler = new DragHandlerImpl(this.asWidget());
		
		/*set up pick-up and move
		 * parameters: absolutePanel, boolean(whether items can be placed to any location)
		 *  */
		widgetDragController = new PickupDragController(
				container, false){
			@Override
			protected void restoreSelectedWidgetsLocation() {
				//do nothing -- Dont drop the widget back to the palette menu if target was missed;
			}
		};
		
		//Drag Controller
		widgetDragController.setBehaviorDragStartSensitivity(5);
		widgetDragController.addDragHandler(dragHandler);  			
		
	
		//Drop Controller 
		VerticalPanelDropController widgetDropController = new VerticalPanelDropController(vPanel);
		widgetDragController.registerDropController(widgetDropController);
		
		registerInputDrag();//Register drag controllers for the 1st Tab
		
		aMinimize.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(IsMinimized){
				divPalettePanel.getElement().getStyle().setWidth(40.0, Unit.PCT);
				divFormContent.getElement().getStyle().setWidth(59.0, Unit.PCT);
				divPaletteBody.removeClassName("hidden");
				aMinimize.setStyleName("minimize minimize-left");
				hPaletetitle.removeClassName("hidden");
				IsMinimized=false;
				}else{
				divPalettePanel.getElement().getStyle().setWidth(10.0, Unit.PCT);
				divFormContent.getElement().getStyle().setWidth(89.0, Unit.PCT);
				divPaletteBody.addClassName("hidden");
				aMinimize.setStyleName("minimize minimize-right");
				hPaletetitle.addClassName("hidden");
				IsMinimized=true;
			}
			}
		});
		
		aInputtab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divInput.addStyleName("active");
				divSelect.removeStyleName("active");
				divButtons.removeStyleName("active");
				liInput.addClassName("active");
				liSelect.removeClassName("active");
				liButton.removeClassName("active");
				
				registerInputDrag();
			}
		});
		
		aSelecttab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divSelect.addStyleName("active");
				divInput.removeStyleName("active");
				divButtons.removeStyleName("active");
				
				liInput.removeClassName("active");
				liSelect.addClassName("active");
				liButton.removeClassName("active");
				
				vSelectBasicPanel.registerDragController(widgetDragController);
				vSelectMultiplePanel.registerDragController(widgetDragController);
			}
		});
		
		
		aButtontab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divButtons.addStyleName("active");
				divSelect.removeStyleName("active");
				divInput.removeStyleName("active");
				
				liInput.removeClassName("active");
				liSelect.removeClassName("active");
				liButton.addClassName("active");
				
				vSingleButtonPanel.registerDragController(widgetDragController);
				vMultipleButtonPanel.registerDragController(widgetDragController);
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
	
	private void registerInputDrag(){	
		vTextInputPanel.registerDragController(widgetDragController);
		vDatePanel.registerDragController(widgetDragController);
		vTextAreaPanel.registerDragController(widgetDragController);
		vInlineRadioPanel.registerDragController(widgetDragController);
		vInlineCheckBoxPanel.registerDragController(widgetDragController);
		vSelectBasicPanel.registerDragController(widgetDragController);
	}
	
	private void setFormProperties(){
		properties.add(new Property("ID", "form Id:", DataType.STRING));
		properties.add(new Property("CAPTION", "Form Name:", DataType.STRING));
		properties.add(new Property("DOCTYPE", "Document Type:", DataType.STRING));
	}

}
