package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.TextValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class HTMLForm extends HTMLParent {

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
					prop = new Property(HTMLCONTENT, "HTML Content",
							DataType.STRINGLONG);
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
					prop = new Property(HTMLCONTENT, "HTML Content",
							DataType.STRINGLONG);
				}
				prop.setValue(new TextValue(txtComponent.getValue()));
				props.put(HTMLCONTENT, prop);
				showView(true);
				wrapHTML();
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
		return htmlContent.getElement();
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isComponentReadOnly() || isReadOnly;
//		Window.alert("Form "+field.getName()+" Readonly = "+this.readOnly);
		for (FieldWidget w : fieldWidgets) {
			w.setReadOnly(this.readOnly);
		}
	}

	@Override
	public void setComponentValid(boolean isValid) {

	}

	boolean bindHTMLWidgets = false;

	@Override
	public void setField(Field aField) {
		super.setField(aField);
		bindHTMLWidgets = false;

		String html = getPropertyValue(HTMLCONTENT);
		bindHTML(html);

		if (isAttached()) {
			bindHTMLWidgets(htmlContent.getElement());
		} else {
			bindHTMLWidgets = true;
		}
		
	}

	// @Override
	// public void setReadOnly(boolean readOnly) {
	// this.readOnly = readOnly || isComponentReadOnly();
	// }

	private void bindHTML(String html) {
		if (html == null) {
			return;
		}
		if (designMode) {
			txtComponent.setValue(html);
		}
		htmlContent.getElement().setInnerHTML(html);
	}

	@Override
	public void onDragEnd() {
		// divActions.removeClassName("hide");
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
		// if(field.getName().equals("academicqualification")){
		// Window.alert(" >> onLoad! "+field.getName());
		// }

	}

	@Override
	protected void onAttach() {
		super.onAttach();

		if (bindHTMLWidgets) {
			bindHTMLWidgets(htmlContent.getElement());
			bindHTMLWidgets = false;
		}

		// if(field.getName().equals("academicqualification")){
		// Window.alert(" >> onAttach! "+field.getName());
		// }
	}

	/**
	 * Bind HTML Widgets.
	 * 
	 * HTML Widgets have to be bound on setField, but after onAttach has been
	 * called. The sequence of calls in different scenarios is as follows </br>
	 * Form INITIALIZATION: setField -> onLoad -> on Attach <br>
	 * HTML Form DragStart: onLoad -> onAttach <br>
	 * HTML Form DragEnd: onLoad -> onAttach -> setField <br>
	 * 
	 * As such, a flag has to be maintained to ensure this method is called in
	 * the correct instances only, otherwise, the application throws exceptions
	 * <p>
	 * 
	 * @param parent
	 */
	protected void bindHTMLWidgets(Element parent) {
		for (Field child : children.values()) {
			if (child.getName() != null) {

				Element element = getElementById(parent, child.getName());
				if (child.getName().equals(txtComponent.getElement().getId())) {
					continue;
				}

				if (element != null) {
					wrapElement(element, getElementType(child, element));
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
		String viewId = parentId.substring(0, parentId.length() - 5) + "View";
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
		// getAllInputs(parentId, elementArray);
		getAllElements(parentId, elementArray);

		List<String> inputGroups = new ArrayList<String>();

		for (int i = 0; i < elementArray.length(); i++) {
			String id = elementArray.get(i);
			if (id == null || id.isEmpty() || id.equals(parentId)
					|| id.equals(viewId)
					|| (textAreaId != null && id.equals(textAreaId))) {
				continue;
			}

			Element element = htmlContent.getElementById(id);

			if (element.hasClassName(GRID_ROW_TEMPLATE_CLASS)
					|| element.hasClassName(GRID_TEMPLATE_CLASS)) {
				// Not writable
				continue;
			}

			String type = element.getAttribute("type");
			if (type != null
					&& (type.equals("radio") || type.equals("checkbox"))) {
				String name = element.getAttribute("name");
				if (name != null) {
					if (inputGroups.contains(name)) {
						continue;
					}

					inputGroups.add(name);

					// Parent has the same id as the name of the children
					Element parentElement = htmlContent.getElementById(name);
					if (parentElement != null) {
						wrapElement(parentElement, type);
					}

					// find an element whose ID= NameOfInputs
				}
			} else {
				wrapElement(element);
			}
		}

	}

	private void wrapGrid(String parentId) {
		JsArrayString grids = (JsArrayString) JsArrayString.createArray();
		getAllGrids(parentId, grids);

		for (int i = 0; i < grids.length(); i++) {
			Element element = htmlContent.getElementById(grids.get(i));
			wrapElement(element, "grid");
		}
	}

	protected void wrapElement(Element element) {
		wrapElement(element, element.getAttribute("type"));
	}

	protected void wrapElement(Element element, String type) {
		FieldWidget widget = FieldWidget.wrap(element, type, designMode);

		if (widget != null) {
			Field child = initializeChild(widget);
			children.put(child.getName(), child);
			fieldWidgets.add(widget);
		}

		field.setFields(children.values());
	}

	// @Override
	// public void onSaveProperties(SavePropertiesEvent event) {
	// //super.onSaveProperties(event);
	// FormModel model = event.getParent();
	// if (model == null || !(model instanceof Field)) {
	// return;
	// }
	//
	// Field fieldModel = (Field) model;
	// if (!fieldModel.isHTMLWrappedField()) {
	// if (field.equals(fieldModel)) {
	// super.save(fieldModel);
	// }
	//
	// return;
	// }
	//
	// if (children.get(fieldModel.getName()) == null) {
	// // not a child of this HTMLForm - Ignore
	// return;
	// }
	//
	// children.put(fieldModel.getName(), fieldModel);
	//
	// ArrayList<Field> list = new ArrayList<Field>();
	// list.addAll(children.values());
	// field.setFields(list);
	// super.save(field);
	//
	// }

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
			target.setValue(fieldValue);

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
