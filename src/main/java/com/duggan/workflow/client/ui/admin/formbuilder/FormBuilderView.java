package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.formbuilder.component.DragHandlerImpl;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
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
	@UiField DropDownList<ProcessDef> processDropdown;
	
	@UiField Anchor aNewForm;
	@UiField Anchor aCloneForm;
	@UiField Anchor aDeleteForm;
	@UiField Anchor aInputtab;
	@UiField Anchor aSelecttab;
	@UiField Anchor aButtontab;
	@UiField Anchor aLayouttab;
	@UiField Anchor aMinimize;
	@UiField Anchor aExportForm;
	@UiField Anchor aImportForm;
	@UiField LIElement liSelect;
	@UiField LIElement liInput;
	@UiField LIElement liButton;
	@UiField LIElement liLayout;
	@UiField Element hPaletetitle;
	@UiField InlineLabel formLabel;
	
	@UiField HTMLPanel divButtons;
	@UiField HTMLPanel divSelect;
	@UiField HTMLPanel divInput;
	@UiField HTMLPanel divPalettePanel;
	@UiField HTMLPanel divFormContent;
	@UiField HTMLPanel divLayoutComponents;
	@UiField DivElement divPaletteBody;
	
	@UiField PalettePanel vTextInputPanel;
	@UiField PalettePanel vNumberInputPanel;
	@UiField PalettePanel vCurrencyInputPanel;
	@UiField PalettePanel vDatePanel;
	@UiField PalettePanel vTextAreaPanel;
	@UiField PalettePanel vInlineRadioPanel;
	@UiField PalettePanel vInlineCheckBoxPanel;
	@UiField PalettePanel vSelectBasicPanel;
	@UiField PalettePanel vSelectMultiplePanel;
	@UiField PalettePanel vSingleButtonPanel;
	@UiField PalettePanel vCheckBoxGroup;
	@UiField PalettePanel vMultipleButtonPanel;
	@UiField PalettePanel vLabelPanel;
	@UiField PalettePanel vGridPanel;
	@UiField PalettePanel vFileUploadPanel;
	@UiField PalettePanel vPanelIFrame;
	@UiField PalettePanel jsPanel;
	@UiField PalettePanel vHTMLFormPanel;
	
	@UiField PalettePanel vHRPanel;
//	@UiField PalettePanel vGridPanel;

	@UiField InlineLabel fldHelp;
		
	PickupDragController widgetDragController;
	boolean IsMinimized;
	
	//Form properties
	protected HashMap<String, Property> props = new LinkedHashMap<String, Property>();
	
	Form form = new Form();
	
	@Inject
	public FormBuilderView(final Binder binder) {
		/**
		 * Switching between the tabs	
		 */
		widget = binder.createAndBindUi(this);
		addProperty(new Property(NAME, "Form ID", DataType.STRING));
		addProperty(new Property(CAPTION, "Caption", DataType.STRING));
		addProperty(new Property(HELP, "Help", DataType.STRING));
		
		
		//Vertical Panel Display
		vPanel.getElement().getStyle().setWidth(100, Unit.PCT);
		
			
		DragHandlerImpl dragHandler = new DragHandlerImpl(this.asWidget()){
			@Override
			public void onDragEnd(DragEndEvent event) {
				super.onDragEnd(event);
				FieldWidget draggable = (FieldWidget)event.getContext().draggable;
				
				int idx = vPanel.getWidgetIndex(draggable);
				if(idx==-1){
					draggable.delete();
				}else{
					draggable.setFormId(form.getId());		
					draggable.getField().setPosition(idx);
					draggable.save();			
					draggable.onDragEnd();
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
					divPalettePanel.getElement().getStyle().setWidth(5, Unit.PCT);
					divFormContent.getElement().getStyle().setWidth(94.0, Unit.PCT);
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
				
				if(form.getId()!=null){
					AppManager.showPropertyPanel(form,getProperties());
				}
			}
		});	
		
		frmDropdown.addValueChangeHandler(new ValueChangeHandler<Form>() {		
			@Override
			public void onValueChange(ValueChangeEvent<Form> event) {
				
				Long previousId = null;
				if(form!=null){
					previousId = form.getId();
				}
				
				Long id = event.getValue()==null? null: event.getValue().getId();
				
				if(previousId==null || id==null){
					clear();
				}else if(previousId!=null && previousId.equals(id)){
					return;
				}
					
				clear();
				
				//
			}
		});
		
		aDeleteForm.setVisible(false);
		aCloneForm.setVisible(false);		
		aExportForm.setVisible(false);
	
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
		if(form==null){
			//hit new
			form = new Form();
		}
		
		String name = getPropertyValue(NAME);
		if(name==null){
			name = "Untitled";
		}
		String caption = getPropertyValue(CAPTION);
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
	 * Registers Default Items in the palette panel for dragging;
	 */
	public void registerInputDrag(){	
		vTextInputPanel.registerDragController(widgetDragController);
		vNumberInputPanel.registerDragController(widgetDragController);
		vCurrencyInputPanel.registerDragController(widgetDragController);
		vDatePanel.registerDragController(widgetDragController);
		vTextAreaPanel.registerDragController(widgetDragController);
		vInlineCheckBoxPanel.registerDragController(widgetDragController);
		vSelectBasicPanel.registerDragController(widgetDragController);
		vLabelPanel.registerDragController(widgetDragController);
		
		//select
		vInlineRadioPanel.registerDragController(widgetDragController);
		vInlineRadioPanel.getWidget(0).addStyleName(DragClientBundle.INSTANCE.css().draggable());
		vSelectMultiplePanel.registerDragController(widgetDragController);
		
		//Buttons
		vSingleButtonPanel.registerDragController(widgetDragController);
		vCheckBoxGroup.registerDragController(widgetDragController);
		vMultipleButtonPanel.registerDragController(widgetDragController);
		
		//layout
		vHRPanel.registerDragController(widgetDragController);
		vGridPanel.registerDragController(widgetDragController);
		vFileUploadPanel.registerDragController(widgetDragController);
		vHTMLFormPanel.registerDragController(widgetDragController);
		vPanelIFrame.registerDragController(widgetDragController);
		jsPanel.registerDragController(widgetDragController);
		
	}
	
	/**
	 * Activates the palette to be usable. Removes the grey background and activates the drag & drop mechanism
	 */
	public void activatePalette(){
		divPaletteBody.removeClassName("working-request");
		
		aInputtab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				disableTabs();
				divInput.addStyleName("active");
				liInput.addClassName("active");			
			}
		});
		
		aSelecttab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				disableTabs();
				divSelect.addStyleName("active");
				liSelect.addClassName("active");
			}
		});
		
		
		aButtontab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				disableTabs();
				divButtons.addStyleName("active");
				liButton.addClassName("active");
			}
		});
		
		aLayouttab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				disableTabs();
				divLayoutComponents.addStyleName("active");
				liLayout.addClassName("active");
			}
		});
	}
	
	protected void disableTabs() {
		divSelect.removeStyleName("active");
		divButtons.removeStyleName("active");
		divInput.removeStyleName("active");
		divLayoutComponents.removeStyleName("active");
		
		liInput.removeClassName("active");
		liSelect.removeClassName("active");
		liButton.removeClassName("active");
		liLayout.removeClassName("active");
	}

	private void DeactivatePalete() {
		divPaletteBody.addClassName("working-request");		
	}

	private ArrayList<Field> getFields() {
		ArrayList<Field> fields = new ArrayList<Field>();
		
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
	
	public Anchor getCloneButton(){
		return aCloneForm;
	}
	
	public Anchor getDeleteButton(){
		return aDeleteForm;
	}

	public Anchor getExportButton(){
		return aExportForm;
	}
	
	public Anchor getImportButton(){
		return aImportForm;
	}
	
	@Override
	public void setForm(Form form) {
		this.form = form;
		
		frmDropdown.setValue(form);
		registerInputDrag();
		activatePalette();
		
		if(form==null || form.getId()==null){
			aDeleteForm.setVisible(false);
			aCloneForm.setVisible(false);
			aExportForm.setVisible(false);
		}else{
			aDeleteForm.setVisible(true);
			aCloneForm.setVisible(true);
			aExportForm.setVisible(true);
		}
		
		if(form==null){
			formLabel.setText("");
			return;
		}
		
		if(form.getProperties()!=null)
		for(Property prop: form.getProperties()){
			addProperty(prop);
			
			Value val = prop.getValue();
			if(val!=null){
				setProperty(prop.getName(), ((StringValue)val).getValue());
			}else{
				setProperty(prop.getName(), null);
			}
			
		}

		String caption = getPropertyValue(CAPTION);
		
		Property captionProperty = props.get(CAPTION);
		if(caption==null && captionProperty!=null){
			Value val = captionProperty.getValue();
			if(val==null){
				captionProperty.setValue(new StringValue(form.getCaption()));
			}else{
				captionProperty.getValue().setValue(form.getCaption());
			}
		}
		
		String name = getPropertyValue(NAME);
		Property nameProperty = props.get(NAME);
		if(name==null && nameProperty!=null){
			Value val = nameProperty.getValue();
			if(val==null){
				nameProperty.setValue(new StringValue(form.getName()));
			}else{
				nameProperty.getValue().setValue(form.getName());
			}
		}
		
		formLabel.getElement().setInnerHTML(caption);
		
		setFields(form.getFields());
		//frmDropdown.setItems(form.getProperties());
	}
	
	private void setFields(ArrayList<Field> fields) {
		if(fields==null || fields.size()==0)
			return;
		
		vPanel.clear();
		
		Collections.sort(fields, new Comparator<FormModel>() {
			public int compare(FormModel o1, FormModel o2) {
				Field field1 = (Field)o1;
				Field field2 = (Field)o2;
				
				Integer pos1 = field1.getPosition();
				Integer pos2 = field2.getPosition();
				
				return pos1.compareTo(pos2);
			};
			
		});
		
		
		for(Field field: fields){
			FieldWidget widget = FieldWidget.getWidget(field.getType(),field,true);
			widgetDragController.makeDraggable(widget);
			vPanel.add(widget);
		}
	
	}

	public void addProperty(Property property) {
		assert props !=null;
		assert property!=null;
		assert property.getName()!=null;
		
		props.put(property.getName(), property);
	}

	public ArrayList<Property> getProperties(){
		ArrayList<Property> values = new ArrayList<Property>();
		values.addAll(props.values());
		return values;
	}
	
	public String getPropertyValue(String key) {
		
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
			formLabel.getElement().setInnerHTML(value);
			form.setCaption(value);
		}

		if(property.equals(HELP)){
			//setHelp(value);
			fldHelp.getElement().setInnerHTML(value);
		}
	}
	
	public HasValueChangeHandlers<Form> getFormDropDown(){
		return frmDropdown;
	}

	@Override
	public void setForms(ArrayList<Form> forms) {
		frmDropdown.setItems(forms);
		if(form!=null)
		if(form.getId()!=null){
			frmDropdown.setValue(form);
		}
	}

	@Override
	public void clear() {
		this.form=null;
		formLabel.setText("");
		ArrayList<Property> properties = getProperties();
		for(Property prop: properties){
			prop.setValue(null);
			prop.setId(null);
		}
		
		vPanel.clear();
	}

	@Override
	public Object getValue(String propertyName) {
	
		Property property = props.get(propertyName);
		
		if(property==null)
			return null;
		
		Value value = property.getValue();
		if(value==null)
			return null;
		
		return value.getValue();
	}

	@Override
	public Value getFieldValue() {
		return null;
	}

	@Override
	public String getFormName() {
		
		if(form!=null)
			return form.getCaption();
		
		return "Untitled";
	}

	@Override
	public void setProcesses(ArrayList<ProcessDef> processes) {
		processDropdown.setNullText("--Process--");
		processDropdown.setItems(processes);
	}
	
	@Override
	public DropDownList<ProcessDef> getProcessDropDown() {
		return processDropdown;
	}

	@Override
	public void enableCreateForm(boolean isProcessSelected) {
		aNewForm.setVisible(isProcessSelected);
		aImportForm.setVisible(isProcessSelected);
		
		if(!isProcessSelected){
			setForm(null);
		}
	}
}
