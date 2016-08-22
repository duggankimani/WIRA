package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.TextValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class HTMLForm extends FieldWidget {

	private static HTMLFormUiBinder uiBinder = GWT
			.create(HTMLFormUiBinder.class);

	interface HTMLFormUiBinder extends UiBinder<Widget, HTMLForm> {
	}

	private final Widget widget;

	@UiField
	HTMLPanel htmlContent;
	@UiField
	com.duggan.workflow.client.ui.component.TextArea txtComponent;
	@UiField
	Anchor aView;
	@UiField
	Anchor aEdit;
	@UiField
	Anchor aSave;
	@UiField
	Element divActions;
	@UiField
	Element designHeader;

	HashMap<String, Field> children = new HashMap<String, Field>();
	List<FieldWidget> fieldWidgets = new ArrayList<FieldWidget>();

	public HTMLForm() {
		super();
		widget = uiBinder.createAndBindUi(this);
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));

		Property htmlContentValue = new Property(HTMLCONTENT, "HTML Content",
				DataType.STRINGLONG);
		htmlContentValue.setShowInPropertyPanel(false);
		addProperty(htmlContentValue);
		add(widget);

		aView.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				htmlContent.getElement().setInnerHTML(txtComponent.getValue());
				Property prop = props.get(HTMLCONTENT);
				if (prop == null) {
					prop  = new Property(HTMLCONTENT, "HTML Content", DataType.STRINGLONG);
				}
				prop.setValue(new TextValue(txtComponent.getValue()));
				props.put(HTMLCONTENT, prop);
				showView(true);
			}
		});

		aEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showView(false);
			}
		});

		aSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				htmlContent.getElement().setInnerHTML(txtComponent.getValue());
				Property prop = props.get(HTMLCONTENT);
				if (prop == null) {
					prop  = new Property(HTMLCONTENT, "HTML Content", DataType.STRINGLONG);
				}
				prop.setValue(new TextValue(txtComponent.getValue()));
				props.put(HTMLCONTENT, prop);
				showView(true);
				save();
			}
		});
	}

	protected void showView(boolean show) {
		if (show) {
			aView.addStyleName("hide");
			txtComponent.addStyleName("hide");

			aEdit.removeStyleName("hide");
			htmlContent.removeStyleName("hide");
		} else {
			aEdit.addStyleName("hide");
			htmlContent.addStyleName("hide");

			txtComponent.removeStyleName("hide");
			aView.removeStyleName("hide");
		}
	}

	@Override
	public void save() {
		wrapHTML();
		super.save();
	}

	@Override
	public FieldWidget cloneWidget() {
		return new HTMLForm();
	}

	@Override
	protected DataType getType() {
		return DataType.FORM;
	}

	@Override
	public Widget getInputComponent() {

		return txtComponent;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	@Override
	public void setComponentValid(boolean isValid) {

	}

	@Override
	public void setField(Field aField) {
		super.setField(aField);

		if (aField.getFields() != null) {
			for (Field child : aField.getFields()) {
				children.put(child.getName(), child);
			}
		}

		String html = getPropertyValue(HTMLCONTENT);
		bindHTML(html);

		if (isAttached()) {
			initHTMLWidgets();
		}
	}

	private void bindHTML(String html) {
		if (html == null) {
			return;
		}
		txtComponent.setValue(html);
		//SafeHtml safeHTML = SafeHtmlUtils.fromSafeConstant(html);
		//htmlContent.clear();
		htmlContent.getElement().setInnerHTML(html);
	}

	@Override
	public void onDragEnd() {
		divActions.removeClassName("hide");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		htmlContent.removeStyleName("hide");
		if (designMode) {
			shim.setWidth("80%");
			shim.setHeight("30px");
			divActions.removeClassName("hide");
			txtComponent.addStyleName("hide");
		} else {
			designHeader.addClassName("hide");
			htmlContent.getElement().getStyle().setMarginBottom(12, Unit.PX); // margin-bottom:
																				// 15px;
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		initHTMLWidgets();
	}

	private void initHTMLWidgets() {
		for (Field field : children.values()) {
			if (field.getName() != null) {
				Element element = htmlContent.getElementById(field.getName());
				if(field.getName().equals(txtComponent.getElement().getId())){
					continue;
				}
				if (element != null){
					if(field.getType()==DataType.CHECKBOXGROUP){
						wrapElement(element,"checkbox");
					}else if(field.getType()==DataType.BOOLEAN){
						wrapElement(element,"radio");
					}else if(field.getType()==DataType.GRID){
						wrapElement(element,"grid");
					}
					else{
						wrapElement(element);
					}
					
				}
			}
		}
	}

	/**
	 * Scan HTML & Generate FieldWidgets
	 * 
	 * Note: HTMLForm must be attached before this method can be called
	 * 
	 */
	private void wrapHTML() {
		fieldWidgets.clear();
		if (!isAttached()) {
			GWT.log("HTMLForm not attached, ignoring FieldWidget attachment");
			assert isAttached();// Let it fail here
		}

		String parentId = getElement().getId();
		String textAreaId = getPropertyValue(NAME);
		
		/**
		 * Grids
		 */
		wrapGrid(parentId);

		/**
		 * Inputs
		 */
		JsArrayString elementArray = (JsArrayString) JsArrayString
				.createArray();
		getAllInputs(parentId, elementArray);
		
		List<String> inputGroups = new ArrayList<String>();
		
		for (int i = 0; i < elementArray.length(); i++) {
			String id = elementArray.get(i);
			if (id == null || id.isEmpty() || id.equals(parentId)
					|| (textAreaId != null && id.equals(textAreaId))) {
				continue;
			}

			Element element = htmlContent.getElementById(id);
			
			String type = element.getAttribute("type");
			if(type!=null && (type.equals("radio") || type.equals("checkbox"))){
				String name = element.getAttribute("name");
				if(name!=null){
					if(inputGroups.contains(name)){
						continue;
					}
					
					inputGroups.add(name);
					
					//Parent has the same id as the name of the children
					Element parentElement = htmlContent.getElementById(name);
					if(parentElement!=null){
						wrapElement(parentElement,type);
					}
					
					//find an element whose ID= NameOfInputs
				}
			}else{
				wrapElement(element);
			}
		}
		
	}

	private void wrapGrid(String parentId) {
		JsArrayString grids = (JsArrayString) JsArrayString
				.createArray();
		getAllGrids(parentId, grids);
		
		for (int i = 0; i < grids.length(); i++) {
			Element element = htmlContent.getElementById(grids.get(i));
			wrapElement(element, "grid");
		}
	}

	private HashMap<String, ArrayList<Element>> wrapRadios(
			ArrayList<Element> checkboxes) {
		HashMap<String, ArrayList<Element>> elements = new HashMap<String, ArrayList<Element>>();

		for (Element checkbox : checkboxes) {
			String name = checkbox.getAttribute("name");
			if (name == null) {
				continue;
			}

			ArrayList<Element> groupedElements = new ArrayList<Element>();	
			if (elements.get(name) != null) {
				groupedElements = elements.get(name);
			}

			groupedElements.add(checkbox);
			elements.put(name, groupedElements);
		}

		return elements;
	}

	private void wrapElements(NodeList<Element> nodes) {
		wrapElements(nodes, null);
	}

	private void wrapElements(NodeList<Element> nodes, ArrayList<Element> radios) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = nodes.getItem(i);
			if (radios != null) {
				if (element.getAttribute("type") != null
						&& "radio".equals(element.getAttribute("type"))) {
					radios.add(element);
					continue;
				}
			}
			wrapElement(element);
		}
	}

	private void wrapElement(Element element) {
		wrapElement(element,element.getAttribute("type"));
	}
	
	private void wrapElement(Element element, String type) {
		FieldWidget widget = FieldWidget.wrap(element,type, designMode);
		if (widget != null) {
			Field child = widget.getField();
			// Copy General Metadata Fields from the Parent
			child.setForm(field.getFormId(),field.getRefId());
			child.setParent(null,field.getRefId());
			assert field.getRefId() != null;

			child.setDocId(field.getDocId());
			child.setDocRefId(field.getDocRefId());
			child.setHTMLWrappedField(true);

			if (children.containsKey(element.getId())) {
				Field dbEl = children.get(element.getId());
				copy(child, dbEl);
				widget.setField(child);
			}

			field.addField(child);
			fieldWidgets.add(widget);
		}
	}

	/**
	 * Copy DB Field values into Front End Field
	 * 
	 * @param feField
	 * @param dbField
	 */
	private void copy(Field feField, Field dbField) {
		if (dbField == null) {
			return;
		}

//		feField.setId(dbField.getId());
		feField.setRefId(dbField.getRefId());
		// Name, Caption, my be ignored - Use Front end values
		feField.setDependentFields(dbField.getDependentFields());
		feField.setDocRefId(dbField.getDocRefId());
		feField.setDynamicParent(dbField.isDynamicParent());
		feField.setFields(dbField.getFields());
		feField.setForm(dbField.getFormId(),dbField.getRefId());
		feField.setGridName(dbField.getGridName());
		feField.setLineRefId(dbField.getLineRefId());

		feField.setProps(dbField.getProps());

		feField.setSelectionValues(dbField.getSelectionValues());
		feField.setValue(dbField.getValue());
	}

	@Override
	public void onSaveProperties(SavePropertiesEvent event) {
		//super.onSaveProperties(event);
		FormModel model = event.getParent();
		if (model == null || !(model instanceof Field)) {
			return;
		}

		Field fieldModel = (Field) model;
		if (!fieldModel.isHTMLWrappedField()) {
			if (field.equals(fieldModel)) {
				super.save(fieldModel);
			}

			return;
		}

		if (children.get(fieldModel.getName()) == null) {
			// not a child of this HTMLForm - Ignore
			return;
		}

		children.put(fieldModel.getName(), fieldModel);

		ArrayList<Field> list = new ArrayList<Field>();
		list.addAll(children.values());
		field.setFields(list);
		super.save(field);

	}

	/**
	 * Get All Field Values
	 * 
	 * @return
	 */
	public ArrayList<Value> getFieldValues() {
		ArrayList<Value> values = new ArrayList<Value>();
		for (FieldWidget w : fieldWidgets) {
			Field target = w.getField();
			Value fieldValue = w.getFieldValue();
			if (fieldValue != null) {
				assert target.getName() != null;
				assert !target.getName().isEmpty();
				fieldValue.setKey(target.getName());
				values.add(fieldValue);
			}
		}
		return values;
	}

}
