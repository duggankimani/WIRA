package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.matheclipse.parser.client.eval.ComplexEvaluator;
import org.matheclipse.parser.client.eval.ComplexVariable;
import org.matheclipse.parser.client.math.Complex;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.admin.formbuilder.PalettePanel;
import com.duggan.workflow.client.ui.events.DeleteLineEvent;
import com.duggan.workflow.client.ui.events.DeleteLineEvent.DeleteLineHandler;
import com.duggan.workflow.client.ui.events.OperandChangedEvent;
import com.duggan.workflow.client.ui.events.OperandChangedEvent.OperandChangedHandler;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.duggan.workflow.client.ui.events.ResetFieldValueEvent;
import com.duggan.workflow.client.ui.events.ResetFormPositionEvent;
import com.duggan.workflow.client.ui.events.ResetFieldValueEvent.ResetFieldValueHandler;
import com.duggan.workflow.client.ui.events.ResetFormPositionEvent.ResetFormPositionHandler;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent.SavePropertiesHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.duggan.workflow.shared.requests.CreateFieldRequest;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.responses.CreateFieldResponse;
import com.duggan.workflow.shared.responses.DeleteFormModelResponse;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

//import java.util.StringTokenizer;

public abstract class FieldWidget extends AbsolutePanel implements
		HasDragHandle, PropertyChangedHandler, HasProperties,
		SavePropertiesHandler, ResetFormPositionHandler, OperandChangedHandler,
		DeleteLineHandler, ResetFieldValueHandler {

	private FocusPanel shim = new FocusPanel();
	protected long id = System.currentTimeMillis();
	protected Map<String, Property> props = new LinkedHashMap<String, Property>();

	Field field = new Field();
	// Design Mode Properties
	protected boolean designMode = true;
	protected boolean readOnly = false;
	List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	// Formula handling properties
	List<String> dependentFields = new ArrayList<String>();
	boolean isObserver = false;// depends on other fields - registered for
								// OperandChangeEvent
	boolean isObservable = false;// its value is depended upon by other fields -
									// fires an event

	public FieldWidget() {
		shim.addStyleName("demo-PaletteWidget-shim");
		defaultProperties();
		initField();
		exportSetValue();
	}

	public void defaultProperties() {
		addProperty(new Property(CAPTION, "Label Text", DataType.STRING, id));
		addProperty(new Property(NAME, "Name", DataType.STRING, id));
		addProperty(new Property(HELP, "Help", DataType.STRING, id));
	}

	private void initField() {
		field.setCaption(getPropertyValue(CAPTION));
		field.setName(getPropertyValue(NAME));
		field.setType(DataType.STRING);
		field.setValue(null);
		field.setType(getType());
	}

	protected void afterInit() {

	}

	public void addProperty(Property property) {
		assert props != null;
		assert property != null;
		assert property.getName() != null;

		if (property.getType().isDropdown()) {
			Property previous = props.get(property.getName());

			if (property.getType().isDropdown()) {
				// no need - It will overwrite server values with local

				if (property.getSelectionValues().isEmpty() && previous != null) {
					// probably frontend
					property.setSelectionValues(previous.getSelectionValues());
				}
			} else if (previous != null) {
				// need to copy lookup values
				property.setSelectionValues(previous.getSelectionValues());
			}
		}

		// overwrite previous value with current

		props.put(property.getName(), property);

		if (!property.getType().isDropdown()) {
		}
	}

	public abstract FieldWidget cloneWidget();

	public void activatePopup() {
	}

	public void activateShimHandler() {
		if (this.getParent() instanceof PalettePanel) {
			return;
		}

		shim.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int arrowPosition = shim.getAbsoluteTop() - 30;
				showProperties(arrowPosition);
			}
		});
	}

	public void showProperties(int arrowPosition) {

		/*
		 * Position of the pop-over
		 */
		int top = 7;
		int left = 75;

		if (field.getType() == DataType.JS) {
			left = 55;
		}

		// System.err.println("Field ="+field+"\n Properties = "+getProperties());

		AppManager.showPropertyPanel(field, getProperties(), top, left,
				arrowPosition);

	}

	public Field getField() {
		field.setType(getType());
		return field;
	}

	public String getPropertyValue(String key) {

		Property property = props.get(key);

		if (property == null)
			return null;

		Value value = property.getValue();
		if (value == null)
			return null;

		return value.getValue() == null ? null : value.getValue().toString();
	}

	@Override
	public Object getValue(String propertyName) {
		Property property = props.get(propertyName);

		if (property == null)
			return null;

		Value value = property.getValue();
		if (value == null)
			return null;

		return value.getValue();
	}

	/**
	 * Overriden by children to provide value
	 */
	@Override
	public Value getFieldValue() {
		return null;
	}

	@Override
	public Widget getDragHandle() {
		return shim;
	}

	/**
	 * Let shim size match our size.
	 * 
	 * @param width
	 *            the desired pixel width
	 * @param height
	 *            the desired pixel height
	 */
	@Override
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		shim.setPixelSize(width, height);
	}

	/**
	 * Let shim size match our size.
	 * 
	 * @param width
	 *            the desired CSS width
	 * @param height
	 *            the desired CSS height
	 */
	@Override
	public void setSize(String width, String height) {
		super.setSize(width, height);
		shim.setSize(width, height);
	}

	/**
	 * Adjust the shim size and attach once our widget dimensions are known.
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		initShim();
		addRegisteredHandler(PropertyChangedEvent.TYPE, this);
		addRegisteredHandler(SavePropertiesEvent.TYPE, this);

		if (isObserver) {
			// System.err.println("Registering observer.. > "+field.getName()+" : "+field.getParentId()+" : "+field.getDetailId());
			addRegisteredHandler(OperandChangedEvent.TYPE, this);
		}

		if (designMode) {
			addRegisteredHandler(ResetFormPositionEvent.TYPE, this);
			activateShimHandler();
		} else {
			// runtime
			// Check if this fields value is relied upon by other fields

			// isObservable =
			// ENV.containsObservable(field.getName(),field.getParentId());
			isObservable = ENV.containsObservable(field.getDocSpecificName(),
					field.getParentId());
			if (isObservable) {
				// System.err.println("Registering observable.. > "+field.getName()+" : "+field.getParentId()+" : "+field.getDetailId());
				registerValueChangeHandler();
				addRegisteredHandler(ResetFieldValueEvent.TYPE, this);
				addRegisteredHandler(DeleteLineEvent.TYPE, this);
			}

		}
		// register default events

	}

	protected void registerValueChangeHandler() {
	}

	/**
	 * Remove the shim to allow the widget to size itself when reattached.
	 */
	@Override
	protected void onUnload() {
		super.onUnload();
		shim.removeFromParent();
		cleanUpEvents();
	}

	/**
	 * 
	 * @param type
	 * @param handler
	 */
	public void addRegisteredHandler(Type<? extends EventHandler> type,
			EventHandler handler) {
		@SuppressWarnings("unchecked")
		HandlerRegistration hr = AppContext.getEventBus().addHandler(
				(GwtEvent.Type<EventHandler>) type, handler);
		handlers.add(hr);
	}

	/**
	 * 
	 */
	private void cleanUpEvents() {
		for (HandlerRegistration hr : handlers) {
			hr.removeHandler();
		}
		handlers.clear();
	}

	private void initShim() {
		if (!designMode) {
			return;
		}

		int offSetWidth = getOffsetWidth();
		int offSetHeight = getOffsetHeight();

		/**
		 * Non-visible or detached elements have no offsetParent<br/>
		 * AbsolutePanel.class - Line 265<br/>
		 * if (child.getElement().getOffsetParent() == null) { return; }
		 * 
		 * The code below forces the shim to be displayed, even for components
		 * that may not have been visible during initialization
		 */
		if (offSetWidth == 0) {
			offSetWidth = 433;
		}

		if (offSetHeight == 0) {
			offSetHeight = 50;
		}

		shim.setPixelSize(offSetWidth, offSetHeight);

		getElement().getStyle().setPosition(Position.RELATIVE);

		// should we do this only if this is not a property field?

		addShim(0, 0, offSetWidth, offSetHeight);

	}

	public void addShim(int left, int top, int offSetWidth, int offSetHeight) {
		add(shim, left, top);
	}

	public List<Property> getProperties() {
		List<Property> values = new ArrayList<Property>();
		for (Property prop : props.values()) {
			values.add(prop);
		}

		return values;
	}

	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if (!event.isForField()) {
			return;
		}

		assert event.getComponentId() != null;
		if (this.id != event.getComponentId()) {
			return;
		}

		String property = event.getPropertyName();
		Object value = event.getPropertyValue();

		if (property.equals(CAPTION))
			setCaption(value == null ? null : value.toString());

		if (property.equals(PLACEHOLDER))
			setPlaceHolder(value == null ? null : value.toString());

		if (property.equals(HELP))
			setHelp(value == null ? null : value.toString());

		if (property.equals(STATICCONTENT))
			setStaticContent(value == null ? null : value.toString());

		if (property.equals(SELECTIONTYPE)
				&& (this instanceof IsSelectionField)) {

			IsSelectionField fld = (IsSelectionField) this;
			fld.setSelectionValues((List<KeyValuePair>) event
					.getPropertyValue());
		}

	}

	protected void setStaticContent(String content) {

	}

	public boolean isComponentReadOnly() {
		Boolean isComponentReadOnly = getValue(READONLY) == null ? false
				: (Boolean) getValue(READONLY);
		if (isComponentReadOnly == null) {
			isComponentReadOnly = false;
		}

		return isComponentReadOnly;
	}

	public void setReadOnly(boolean readOnly) {

	}

	protected void setCaption(String caption) {
	};

	protected void setPlaceHolder(String placeHolder) {
	}

	protected void setHelp(String help) {
	}

	protected abstract DataType getType();

	public void setValue(Object value) {
		ENV.setContext(field, value);
	}

	@Override
	public void onResetFieldValue(ResetFieldValueEvent event) {
		
		if (!event.getFieldName().equals(field.getName())) {
			return;
		}

		//Window.alert("FieldName Reset = "+event.getFieldName()+" = "+event.getValue());
		setValue(parseValue(event.getValue()));

	}

	public Object parseValue(String value) {
		return value;
	}

	public static void setStringValue(String fieldName, String value) {
		AppContext.fireEvent(new ResetFieldValueEvent(fieldName, value));
	}

	public native void exportSetValue()/*-{
										
										$wnd.setFieldValue=$entry(@com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget::setStringValue(Ljava/lang/String;Ljava/lang/String;));
										
										}-*/;

	public void setFormId(Long formId) {
		field.setFormId(formId);
	}

	protected void save(Field model) {

		model.setType(getType());
		model.setProperties(getProperties());

		// System.err.println("Select values: "+field.getSelectionValues());
		// System.err.println("Save Props>>>> "+model+" :: "+model.getProperties()
		// +" \n Fields = "+model.getFields());

		model.setName(getPropertyValue(NAME));
		model.setCaption(getPropertyValue(CAPTION));

		AppContext.getDispatcher().execute(new CreateFieldRequest(model),
				new TaskServiceCallback<CreateFieldResponse>() {
					@Override
					public void processResult(CreateFieldResponse result) {
						Field savedfield = result.getField();
						savedfield.sortFields();
						setField(savedfield);
						onAfterSave();
					}
				});
	}

	public void onAfterSave() {

	}

	public void setField(Field field) {
		this.field = field;
		this.getElement().setId(field.getName() + "_Field");
		this.getInputComponent().getElement().setId(field.getName());
		if (this.getViewElement() != null) {
			this.getViewElement().setId(field.getName() + "_View");
		}

		if (field.getId() != null)
			this.id = field.getId();

		// reset all fieldids in previous properties
		for (Property prop : props.values()) {
			prop.setFieldId(id);
		}

		setProperties(field.getProperties());

		String caption = getPropertyValue(CAPTION);
		if (caption != null && !caption.isEmpty()) {
			setCaption(caption);
		} else {

			// field widgets created from FieldModels - see GridColumn
			Property prop = props.get(CAPTION);
			if (prop != null) {
				prop.setValue(new StringValue(field.getCaption()));
			}
		}

		// set place holder
		String placeHolder = getPropertyValue(PLACEHOLDER);
		if (placeHolder != null && !placeHolder.isEmpty()) {
			setPlaceHolder(placeHolder);
		}

		// set help
		String help = getPropertyValue(HELP);
		if (help != null) {
			setHelp(help);
		}

		// Set Read only
		boolean readOnly = false;
		Object val = getValue(READONLY);
		if (val != null && val instanceof Boolean) {
			readOnly = (Boolean) val;
		}
		setReadOnly(readOnly);

		String alignment = getPropertyValue(ALIGNMENT);
		if (alignment != null) {
			setAlignment(alignment);
		}

		// set dropdown choices
		if (this instanceof IsSelectionField) {
			((IsSelectionField) this).setSelectionValues(field
					.getSelectionValues());
		}

		// Set Value
		Value value = field.getValue();
		if (value != null) {
			setValue(value.getValue());
		} else {
			setValue(null);
		}

		String formula = getPropertyValue(FORMULA);
		if (formula != null) {
			setFormula(formula);
		}

		String labelPosition = getPropertyValue(LABELPOSITION);
		if (labelPosition != null) {
			setLabelPosition(labelPosition.equals("top"));
		}

	}

	protected void setLabelPosition(boolean equalsTop) {

	}

	protected void setAlignment(String alignment) {

	}

	private void setProperties(List<Property> properties) {
		if (properties != null) {
			for (Property prop : properties) {
				addProperty(prop);
			}
		}
	}

	@Override
	public void onSaveProperties(SavePropertiesEvent event) {
		FormModel model = event.getParent();

		if (model == null) {
			return;
		}

		if (model.equals(field)) {
			save((Field) model);
		}
	}

	public void save() {
		save(field);
	}

	/**
	 * This allows visual properties including Caption, Place Holder, help to be
	 * Synched with the form field, so that the changes are observed immediately
	 * 
	 * All other Properties need not be synched this way
	 * 
	 * @param property
	 * @param value
	 *            Boolean, Double
	 */
	protected void firePropertyChanged(Property property, Object value) {
		boolean isForField = property.getFieldId() != null;

		Long componentId = property.getFieldId() != null ? property
				.getFieldId() : property.getFormId();

		AppContext.getEventBus().fireEventFromSource(
				new PropertyChangedEvent(componentId, property.getName(),
						value, isForField), this);
	}

	public static FieldWidget getWidget(DataType type, Field fld,
			boolean activatePopup) {
		FieldWidget widget = null;

		switch (type) {
		case BOOLEAN:
			widget = new RadioGroup();
			break;

		case DATE:
			widget = new DateField();
			break;

		case DOUBLE:
			if (fld.getProperty(HasProperties.CURRENCY) != null) {
				widget = new CurrencyField();
			} else {
				widget = new NumberField();
			}
			break;

		case INTEGER:
			widget = new TextField();
			break;

		case STRING:
			widget = new TextField();
			break;

		case STRINGLONG:
			widget = new TextArea();
			break;

		case BUTTON:
			widget = new SingleButton();
			break;

		case CHECKBOX:
			widget = new CheckBoxField();
			break;

		case MULTIBUTTON:
			widget = new ButtonGroup();
			break;

		case SELECTBASIC:
			widget = new SelectBasic();
			break;

		case SELECTMULTIPLE:
			widget = new SelectMultiple();
			break;

		case LABEL:
			widget = new StaticText();
			break;

		case LAYOUTHR:
			widget = new HR();
			break;

		case GRID:
			widget = new GridLayout();
			/* widget = new GridField(); */
			break;
		case FILEUPLOAD:
			widget = new FileUploadField();
			break;

		case LINK:
			widget = new LinkField();
			break;
		case IFRAME:
			widget = new IFrameField();
			break;

		case JS:
			widget = new JSField();
			if (!activatePopup) {
				// hidden field
				widget.addStyleName("hide");
			}
			break;
		}

		widget.designMode = activatePopup;
		widget.setField(fld);

		if (activatePopup)
			widget.activatePopup();

		widget.afterInit();

		return widget;
	}

	public void delete() {
		if (field.getId() != null) {
			AppContext.getDispatcher().execute(
					new DeleteFormModelRequest(field),
					new TaskServiceCallback<DeleteFormModelResponse>() {
						@Override
						public void processResult(DeleteFormModelResponse result) {
						}
					});
		}
	}

	@Override
	public void onResetFormPosition(ResetFormPositionEvent event) {
		if (event.getInsertPosition() >= field.getPosition()) {
			if (getParent() != null && getParent() instanceof VerticalPanel) {
				VerticalPanel panel = (VerticalPanel) getParent();
				int idx = panel.getWidgetIndex(this);

				if (idx == -1) {
					return;
				} else {

					int previousPosition = field.getPosition();
					// System.err.println("B4 :: "+field +"[Idx="+idx+"]," +
					// " [ Previous Pos="+previousPosition+"])");
					//
					if (idx == previousPosition) {
						return;
					}

					field.setPosition(idx);
					event.addCount();
				}
			}
		}
	}

	/**
	 * Get Property Field
	 * 
	 * @param property
	 * @return
	 */
	public static FieldWidget getWidget(Property property) {

		FieldWidget widget = null;

		switch (property.getType()) {
		case BOOLEAN:
			widget = new CheckBoxField(property);
			break;

		case DATE:
			widget = new DateField(property);
			break;

		case DOUBLE:
			widget = new TextField(property);
			break;

		case INTEGER:
			widget = new TextField(property);
			break;

		case STRING:
			widget = new TextField(property);
			break;

		case STRINGLONG:
			widget = new TextArea(property);
			break;

		case CHECKBOX:
			widget = new CheckBoxField(property);
			break;

		case LABEL:
			widget = new StaticText();
			break;

		case SELECTBASIC:
			widget = new SelectBasic(property);
			break;

		}

		// System.err.println(">>>"+property.getType()+" :: "+property.getCaption());

		assert widget != null;

		if (widget != null) {
			widget.designMode = false;
		}

		return widget;
	}

	public boolean isReadOnly() {

		Object readOnly = getValue(READONLY);

		if (readOnly == null)
			return false;

		return (Boolean) readOnly;
	}

	public boolean isMandatory() {
		Object isMandatory = getValue(MANDATORY);

		if (isMandatory == null)
			return false;

		return (Boolean) isMandatory;
	}

	public FocusPanel getShim() {
		return shim;
	}

	public Widget createComponent(boolean small) {
		return null;
	}

	protected void setFormula(String formula) {

		if (formula == null || formula.isEmpty()) {
			return;
		}

		isObserver = true;
		// System.err.println(formula);
		String regex = "[(\\+)+|(\\*)+|(\\/)+|(\\-)+|(\\=)+|(\\s)+(\\[)+|(\\])+|(,)+]";
		String[] tokens = formula.split(regex);

		String digitsOnlyRegex = "[-+]?[0-9]*\\.?[0-9]+";// isNot a number
		for (String token : tokens) {
			if (token != null && !token.isEmpty()
					&& !token.matches(digitsOnlyRegex)) {
				dependentFields.add(token + field.getDocId() + "D");
			}
		}

		ENV.registerObservable(dependentFields);
	}

	/**
	 * Document qualified fieldname > concat(fieldName, docId);
	 * 
	 * @return
	 */
	public List<String> getDependentFields() {
		return dependentFields;
	}

	/**
	 * Fired whenever a formula operand(a field used in the formular) changes
	 * its value
	 */
	@Override
	public void onOperandChanged(OperandChangedEvent event) {
		String fieldName = event.getSourceField(); // Raw fieldName without
													// detailid

		if (!dependentFields.contains(fieldName)) {
			return;
		}

		/*
		 * check if the source and target are grid fields to confirm row wise
		 * updates
		 */
		if (ENV.isParent(fieldName, field.getParentId())) {
			// Same ParentId = Same Detail Grid
			Long lineRefId = event.getLineRefId();
			Long fieldDetailId = field.getLineRefId();

			if (lineRefId != null && fieldDetailId != null) {
				if (!lineRefId.equals(fieldDetailId)) {
					// two different rows
					return;
				}
			}
		}

		/*
		 * Aggregate Functions - operand Substitution
		 */
		List<String> paramFields = new ArrayList<String>();
		paramFields.addAll(dependentFields);

		String formular = parseAggregate(paramFields, getPropertyValue(FORMULA));
		// System.out.println("Formular >> "+formular);
		/*
		 * Value substitution into the evaluating engine
		 */
		ComplexEvaluator engine = new ComplexEvaluator();
		for (String fld : paramFields) {
			// fld already qualified
			Object val = ENV.getValue(fld);
			String formularFieldName = "";

			if (field.getParentId() != null && val == null) {
				// Current Field is a detail field, and no value was found for
				// dependent field

				val = ENV.getValue(formularFieldName = fld
						+ Field.getSeparator() + field.getLineRefId());
				// Added Grid Name to Qualified Name
				// val=ENV.getValue(formularFieldName=fld+Field.getSeparator()+field.getLineRefId());
			}

			// System.err.println(fld+" == "+val);
			if (val != null && (val instanceof Double)) {

				/*
				 * Dependent Fields== fieldName-DocId-D Event Source FieldName==
				 * FieldName-DocId-D
				 * 
				 * >
				 */
				formularFieldName = fld.substring(0,
						fld.indexOf(field.getDocId()));
				if (!fld.endsWith("D")) {
					formularFieldName = fld;
				}

				// System.out.println(formularFieldName+" = "+val);

				ComplexVariable dv = new ComplexVariable((Double) val);
				engine.defineVariable(formularFieldName, dv);
			}
		}

		if (formular.startsWith("=")) {
			formular = formular.substring(1, formular.length());
		}

		Double result = null;
		try {
			// System.err.println("Calculating> "+field.getName()+" = "+formular);
			Complex x = engine.evaluate(formular);
			result = x.abs();
			setValue(result);
		} catch (Exception e) {
			e.printStackTrace();
			setValue(result = new Double(0.0));
		} finally {
			boolean contained = ENV.containsObservable(field
					.getDocSpecificName());

			if (contained) {
				// potential for an endless loop
				AppContext.fireEvent(new OperandChangedEvent(field
						.getDocSpecificName(), result, field.getLineRefId()));
			}
		}
	}

	/**
	 * Replace all aggregate fields with comma separated actuals<br>
	 * i.e =Plus[total] becomes =Plus[1|total,2|total,3|total,....,RowN|Total]
	 * 
	 * @param formular
	 * @return
	 */
	private String parseAggregate(List<String> paramFields, String formular) {
		// One of the dependent is a grid detail field - A column in a grid row
		// eg formular =Plus[row_total]; where row_total is a column in invoice
		// particulars

		if (!field.isAggregate()) {
			// Target/ result field is not an aggregate/grid field

			for (String fld : dependentFields) {
				if (ENV.isAggregate(fld)) {
					// This is a grid field
					List<String> names = ENV.getQualifiedNames(fld);
					// /System.err.println(fld+" : Aggregated: "+names);

					String nameList = "";
					if (names != null) {
						for (String n : names) {
							nameList = nameList.concat(n + ",");
						}
					}

					if (!nameList.isEmpty()) {
						paramFields.remove(fld);
						paramFields.addAll(names);
						nameList = nameList.substring(0, nameList.length() - 1);

						// Formula is Document independent
						// make it dependent on doc
						String rootName = fld.substring(0,
								fld.indexOf(field.getDocId()));

						// String regex="/^"+fld+"$/";
						// System.out.println("["+rootName+"] >> ["+nameList+"]");
						formular = formular.replaceAll(rootName, nameList);
						// System.err.println("formular>> "+formular);
					}
				}
			}
		}
		return formular;
	}

	@Override
	public void onDeleteLine(DeleteLineEvent event) {
		if (isObservable && field.getLineRefId() != null) {
			Long sourceDetailId = event.getLine().getId();
			if (sourceDetailId == null) {
				sourceDetailId = event.getLine().getTempId();
			}
			if (!field.getLineRefId().equals(sourceDetailId)) {
				return;
			}
			ENV.setContext(field, new Double(0.0));
			AppContext.fireEvent(new OperandChangedEvent(field
					.getDocSpecificName(), new Double(0.0), field
					.getLineRefId()));
		}
	}

	public void manualLoad() {
		onLoad();
	}

	public void manualUnload() {
		onUnload();
	}

	public boolean isFormularField() {

		String formular = getPropertyValue(FORMULA);
		return formular != null && !formular.isEmpty();
	}

	public boolean isValid() {

		boolean isValid = true;

		Object obj = getValue(FieldWidget.MANDATORY);
		if (obj == null) {
			return true;
		}

		Boolean mandatory = (Boolean) obj;
		if (mandatory) {
			Value fieldValue = getFieldValue();
			Object val = null;

			if (fieldValue != null) {
				val = fieldValue.getValue();
			}

			if (val == null) {
				isValid = false;
			}

			if (val instanceof String) {
				if (isNullOrEmpty(val.toString())) {
					isValid = false;
				}
			}
		}

		setComponentValid(isValid);

		return isValid;
	}

	/**
	 * Override this to set style to your components
	 * 
	 * @param isValid
	 */
	private void setComponentValid(boolean isValid) {
		// this.addStyleName();
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public Value from(String key, String val) {
		return new StringValue(null, key, val);
	}

	public abstract Widget getInputComponent();

	public abstract Element getViewElement();

}
