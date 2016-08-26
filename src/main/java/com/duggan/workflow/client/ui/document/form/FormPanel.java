package com.duggan.workflow.client.ui.document.form;

import static com.duggan.workflow.client.ui.util.DateUtils.DATEFORMAT;
import static com.duggan.workflow.client.ui.util.DateUtils.MONTHDAYFORMAT;
import static com.duggan.workflow.client.ui.util.DateUtils.MONTHTIME;
import static com.duggan.workflow.client.ui.util.DateUtils.TIMEFORMAT12HR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.admin.formbuilder.component.SingleButton;
import com.duggan.workflow.client.ui.admin.formbuilder.component.TextArea;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.delegate.FormDelegate;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.LegendElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AttachDetachException;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * Runtime form
 * 
 * @author duggan
 *
 */
public class FormPanel extends Composite {

	private static FormPanelUiBinder uiBinder = GWT
			.create(FormPanelUiBinder.class);

	interface FormPanelUiBinder extends UiBinder<Widget, FormPanel> {
	}

	@UiField
	HTMLPanel panelFields;
	@UiField
	LegendElement divFormCaption;
	@UiField
	SpanElement divFormHelp;
	@UiField
	IssuesPanel issues;
	boolean isReadOnly = true;

	@UiField
	SpanElement spnCreated;
	@UiField
	SpanElement spnDeadline;

	FormDelegate formDelegate = new FormDelegate();
	MODE mode = MODE.VIEW;
	ArrayList<String> jsScripts = new ArrayList<String>();

	public FormPanel(Form form, Doc doc) {
		this(form, doc, MODE.VIEW);
	}

	public FormPanel(Form form, Doc doc, MODE mode) {
		initWidget(uiBinder.createAndBindUi(this));
		this.mode = mode;

		form.getCaption();
		divFormHelp.setInnerText("");
		if (form.getProps() != null)
			for (KeyValuePair prop : form.getProps()) {
				if (prop.getName() != null) {
					String val = prop.getValue();
					if (prop.getName().equals(HasProperties.CAPTION)) {
						
						if (val != null) {
							divFormCaption.setInnerHTML(val);
						}

					}
					if (prop.getName().equals(HasProperties.HELP)) {
						if (val != null) {
							divFormHelp.setInnerHTML(val);
						}
					}
				}
			}

		ArrayList<Field> fields = form.getFields();
		Collections.sort(fields, new Comparator<FormModel>() {
			public int compare(FormModel o1, FormModel o2) {
				Field field1 = (Field) o1;
				Field field2 = (Field) o2;

				Integer pos1 = field1.getPosition();
				Integer pos2 = field2.getPosition();

				return pos1.compareTo(pos2);
			};

		});

		//Bind formulae
		bindFormula(form.getFormulae());
		//Bind Fields
		bind(fields, doc, form.getDependencies().keySet());

	}
	
	private void bindFormula(ArrayList<String> formulae) {
		ArrayList<String> dependentFields = new ArrayList<String>();
		for(String formula: formulae){
			setFormula(formula, dependentFields);
		}
		ENV.registerObservable(dependentFields);
	}
	
	protected void setFormula(String formula,ArrayList<String> dependentFields) {

		if (formula == null || formula.isEmpty()) {
			return;
		}

		// System.err.println(formula);
		String regex = "[(\\+)+|(\\*)+|(\\/)+|(\\-)+|(\\=)+|(\\s)+(\\[)+|(\\])+|(,)+]";
		String[] tokens = formula.split(regex);

		String digitsOnlyRegex = "[-+]?[0-9]*\\.?[0-9]+";// isNot a number
		for (String token : tokens) {
			if (token != null && !token.isEmpty()
					&& !token.matches(digitsOnlyRegex)) {
				//dependentFields.add(token + field.getDocId() + "D");
				dependentFields.add(token);
			}
		}
		
	}

	private void execJs() {
		for (String js : jsScripts) {
			if (js != null && !js.isEmpty()) {
				ScriptInjector.fromString(js).inject();
			}
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		execJs();
	}
	
	@Override
	protected void onAttach() {
		try{
			super.onAttach();
		}catch(AttachDetachException e){
			StringBuffer buffer = new StringBuffer();
			getTrace(e,buffer);
			Window.alert(e.getMessage()+"<p>"+buffer.toString());
		}
	}
	
	private void getTrace(Throwable e, StringBuffer buffer) {
		if(e==null){
			return;
		}
		
		for(StackTraceElement elem: e.getStackTrace()){
			buffer.append(elem.toString()+"\r\n");
		}
		buffer.append("--------------------------------------------------------- \r\n");
		getTrace(e.getCause(), buffer);
		
	}

	void bind(ArrayList<Field> fields, Doc doc, Collection<String> parentFields) {
		bind(fields, doc, parentFields,false);
	}
	
	void bind(ArrayList<Field> fields, Doc doc, Collection<String> parentFields, boolean isRawHTMLFormFields) {
		
		HashMap<String, Value> values = doc.getValues();
		Long documentId = null;
		Long taskId = null;

		if (doc instanceof Document) {
			documentId = (Long) doc.getId();
			taskId = null;
		} else {
			documentId = ((HTSummary) doc).getDocumentRef();
			taskId = ((HTSummary) doc).getId();
		}
		for (Field field : fields) {
			if(field==null){
				continue;
			}
			field.setHTMLWrappedField(isRawHTMLFormFields);
			
			if(field.getType()==DataType.JS){
				//Load Scripts
				jsScripts.add(field.getPropertyValue(HasProperties.JS));
				continue;
			}
			
			String name = field.getName();
			field.setDocId(documentId + ""); // Add DocId to all field
			field.setDocRefId(doc.getRefId());
			if (name == null || name.isEmpty()) {
				continue;
			}

			if (field.getType() == DataType.GRID) {
				ArrayList<DocumentLine> lines = doc.getDetails()
						.get(field.getName());
				if (lines != null) {
					GridValue value = new GridValue();
					value.setKey(field.getName());
					value.setCollectionValue(lines);
					// System.err.println(">>"+lines.size());
					field.setValue(value);
				}

				bind(field);
				continue;

			}else if(field.getType()==DataType.FORM){ 
				
				bind(field.getFields(), doc,parentFields, true);
				
			}else if (field.getType() == DataType.BUTTON) {
				String submitType = field
						.getPropertyValue(SingleButton.SUBMITTYPE);
				if (submitType != null) {
					if (submitType.equals("CompleteProcess")) {
						// Override default complete
						// getView().overrideDefaultCompleteProcess();
					} else if (submitType.equals("StartProcess")) {
						// Override default start
						// getView().overrideDefaultStartProcess();
					}

				}

				if (doc instanceof Document) {
					DocStatus status = ((Document) doc).getStatus();
					if (status == DocStatus.DRAFTED) {
						Property prop = new Property(HasProperties.READONLY,
								"Read only", DataType.BOOLEAN);
						prop.setValue(new BooleanValue(null,
								HasProperties.READONLY, false));
						field.addProperty(prop);
					}
				}
			}

			Value value = values.get(name);
			field.setValue(value);

			if (value == null) {
				if (name.equals("subject")) {
					value = new StringValue(doc.getCaseNo());
				}

				if (name.equals("description")) {
					value = new StringValue(doc.getDescription());
				}

				if (name.equals("docDate")) {
					value = new DateValue(doc.getCreated());
				}
				field.setValue(value);
			}
			
			if(parentFields.contains(field.getName())){
				field.setDynamicParent(true);
			}

			// Bind this field to the form
			bind(field);
		}
	}

	public void bind(Field field) {
		if(field==null){
			return;
		}
		
		if(field.isHTMLWrappedField()){
			//HTML Field - Do not instanciate field widget
			if (mode == MODE.VIEW) {
				//SET READONLY
			}
			return;
		}
		
		
		FieldWidget fieldWidget = FieldWidget.getWidget(field.getType(), field,
				false);
		if (mode == MODE.VIEW) {
			// set read only
			fieldWidget.setReadOnly(true);
		}

		if (fieldWidget instanceof TextArea) {
			((TextArea) fieldWidget).getContainer().removeStyleName("hidden");
		}

		panelFields.add(fieldWidget);
	}

	public void setCreated(Date created) {

		if (created != null) {
			// DateUtils.getTimeDifferenceAsString(created);
			String timeDiff = getTime(created);
			spnCreated.setInnerText(timeDiff);
			// TIMEFORMAT12HR.format(created)+" ("+timeDiff+" )");
		}

	}

	private String getTime(Date date) {
		String timeDiff = "";
		int days = CalendarUtil.getDaysBetween(date, new Date());
		if (days >= 1 && days < 8) {
			// Jan 18 (2 days ago)
			timeDiff = MONTHDAYFORMAT.format(date) + " ("
					+ DateUtils.getTimeDifferenceAsString(date) + ")";
		} else if (days < 0) {
			// future
			timeDiff = MONTHTIME.format(date);
		} else if (days < 1) {
			// Several hr ago
			timeDiff = TIMEFORMAT12HR.format(date);
		} else {
			// More than 8 days
			timeDiff = DATEFORMAT.format(date);
		}
		return timeDiff;
	}

	public void setCompletedOn(Date completedOn) {
		if (completedOn != null) {
			String timeDiff = getTime(completedOn);// MONTHDAYFORMAT.format(completedOn);//DateUtils.getTimeDifferenceAsString(created);
			spnDeadline.setInnerText("Done " + timeDiff);
			// TIMEFORMAT12HR.format(created)+" ("+timeDiff+" )");
		}
	}

	public void setDeadline(Date endDateDue) {
		if (endDateDue == null) {
			return;
		}

		String deadline = getTime(endDateDue);
		// String timeDiff = DateUtils.getTimeDifferenceAsString(endDateDue);

		// if(timeDiff != null){
		// deadline = MONTHDAYFORMAT.format(endDateDue);
		// //TIMEFORMAT12HR.format(endDateDue)+" ("+timeDiff+" )";
		// }

		if (DateUtils.isOverdue(endDateDue)) {
			spnDeadline.removeClassName("hidden");
			spnDeadline.getStyle().setColor("#DD4B39");
		} else if (DateUtils.isDueInMins(30, endDateDue)) {
			spnDeadline.removeClassName("hidden");
			spnDeadline.getStyle().setColor("#F89406");
		}

		spnDeadline.setInnerText("Due " + deadline);
	}

	public boolean isValid() {
		boolean isValid = formDelegate.isValid(issues, panelFields);
		;

		if (!isValid) {
			issues.getElement().scrollIntoView();
		}
		return isValid;
	}

	public HashMap<String, Value> getValues() {
		return formDelegate.getValues(panelFields);
	}

	public void setReadOnly(boolean readOnly) {
		this.isReadOnly = readOnly;
		formDelegate.setReadOnly(readOnly, (ComplexPanel) panelFields);
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

}
