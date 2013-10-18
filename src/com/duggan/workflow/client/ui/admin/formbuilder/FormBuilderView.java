package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.formbuilder.component.DragHandlerImpl;

import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;

import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
		FormBuilderPresenter.IFormBuilderView, HasProperties {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FormBuilderView> {
	}
	
	@UiField AbsolutePanel container;
	@UiField VerticalPanel vPanel;
	@UiField DropDownList<Form> frmDropdown;
	
	@UiField Anchor aNewForm;
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

	PickupDragController widgetDragController;
	boolean IsMinimized;
	
	//Form properties
	protected Map<String, Property> props = new LinkedHashMap<String, Property>();
	
	Form form = new Form();
	
	@Inject
	public FormBuilderView(final Binder binder) {
		/**
		 * Switching between the tabs	
		 */
		widget = binder.createAndBindUi(this);
		addProperty(new Property(NAME, "Form ID", DataType.STRING));
		addProperty(new Property(CAPTION, "Caption", DataType.STRING));
		addProperty(new Property(HELP, "Help", DataType.STRINGLONG));
			
		DragHandlerImpl dragHandler = new DragHandlerImpl(this.asWidget()){
			@Override
			public void onDragEnd(DragEndEvent event) {
				super.onDragEnd(event);
				FieldWidget draggable = (FieldWidget)event.getContext().draggable;
				
				
				int idx = vPanel.getWidgetIndex(draggable);
				if(idx==-1){
					draggable.delete();
				}else{
					//event.getContext().
					draggable.setFormId(form.getId());
					draggable.save();			
				}
			}
		};
		
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
		
		DeactivatePalete();
		
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
		
			
		formLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event){
				/*set the position of the pop-up to be displayed in % */
				int top=7;
				int left=60;
				int arrowposition =formLabel.getAbsoluteTop()-30;
				AppManager.showPropertyPanel(form,getProperties(), top, left, arrowposition);
			}
		});	
		
		frmDropdown.addValueChangeHandler(new ValueChangeHandler<Form>() {		
			@Override
			public void onValueChange(ValueChangeEvent<Form> event) {
				Long previousId = form.getId();
				Long id = event.getValue().getId();
				
				if(previousId!=null && previousId.equals(id)){
					return;
				}
				
				if(id!=null && (id.equals(previousId))){
					return;
				}
				
				clear();
				//
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
	
	public Form getForm(){
		
		String name = getValue(NAME);
		if(name==null){
			name = "Untitled";
		}
		String caption = getValue(CAPTION);
		if(caption==null){
			caption="Untitled";
		}
		
		form.setName(name);
		form.setCaption(caption);
		form.setProperties(getProperties());
		form.setFields(getFields());
		form.setId(form.getId());
		return form;
	}
	
	/**
	 * Registers Default Items in the palette panel;
	 */
	public void registerInputDrag(){	
		vTextInputPanel.registerDragController(widgetDragController);
		vDatePanel.registerDragController(widgetDragController);
		vTextAreaPanel.registerDragController(widgetDragController);
		vInlineRadioPanel.registerDragController(widgetDragController);
		vInlineCheckBoxPanel.registerDragController(widgetDragController);
		vSelectBasicPanel.registerDragController(widgetDragController);
	}
	
	/**
	 * Activates the palette to be usable. Removes the grey background and activates the drag & drop mechanism
	 */
	public void activatePalette(){
		divPaletteBody.removeClassName("working-request");
		
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
	
	private void DeactivatePalete() {
		divPaletteBody.addClassName("working-request");
		
		//De-register the Drag-Controllers
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
	
	public Anchor getNewButton(){
		return aNewForm;
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		
		for(Property prop: form.getProperties()){
			addProperty(prop);
		}

		String caption = getValue(CAPTION);
		
		Property captionProperty = props.get(CAPTION);
		if(caption==null && captionProperty!=null){
			Value val = captionProperty.getValue();
			if(val==null){
				captionProperty.setValue(new StringValue(form.getCaption()));
			}else{
				captionProperty.getValue().setValue(form.getCaption());
			}
		}
		
		String name = getValue(NAME);
		Property nameProperty = props.get(NAME);
		if(name==null && nameProperty!=null){
			Value val = nameProperty.getValue();
			if(val==null){
				nameProperty.setValue(new StringValue(form.getName()));
			}else{
				nameProperty.getValue().setValue(form.getName());
			}
		}
		
		formLabel.setText(caption);
		setFields(form.getFields());
		//frmDropdown.setItems(form.getProperties());
	}
	
	private void setFields(List<Field> fields) {
		if(vPanel.getWidgetCount()==0){
			for(Field field: fields){
				FieldWidget widget = FieldWidget.getWidget(field.getType(),field,true);
				widgetDragController.makeDraggable(widget);
				vPanel.add(widget);
			}
		}
	}

	public void addProperty(Property property) {
		assert props !=null;
		assert property!=null;
		assert property.getName()!=null;
		
		props.put(property.getName(), property);
	}

	public List<Property> getProperties(){
		List<Property> values = new ArrayList<Property>();
		values.addAll(props.values());
		return values;
	}
	
	public String getValue(String key) {
		
		Property property = props.get(key);
		
		if(property==null)
			return null;
		
		Value value = property.getValue();
		if(value==null)
			return null;
		
		return value.getValue()==null? null : value.getValue().toString();
	}
	
	@Override
	public InlineLabel getFormLabel() {

		return formLabel;
	}

	@Override
	public void setProperty(String property, String value) {

		if(property.equals(CAPTION)){
			//setCaption(value);
			formLabel.setText(value);
			form.setCaption(value);
		}

		if(property.equals(HELP)){
			//setHelp(value);
			formLabel.setText(value);
		}
	}
	
	public HasValueChangeHandlers<Form> getFormDropDown(){
		return frmDropdown;
	}

	@Override
	public void setForms(List<Form> forms) {
		frmDropdown.setItems(forms);
	}

	@Override
	public void clear() {
		formLabel.setText("");
		List<Property> properties = getProperties();
		for(Property prop: properties){
			prop.setValue(null);
			prop.setId(null);
		}
		
		vPanel.clear();
	}
}
