package com.duggan.workflow.server.dao.helper;

import static com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.SELECTIONTYPE;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADFieldJson;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADFormJson;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.HasProperties;
import com.duggan.workflow.server.dao.model.IDUtils;
import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.LookupLoader;
import com.duggan.workflow.server.db.LookupLoaderImpl;
import com.duggan.workflow.server.export.AnnotationParserImpl;
import com.duggan.workflow.server.helper.dao.JaxbFormExportProviderImpl;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.TextValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;

public class FormDaoHelper {

	static final Logger logger = Logger.getLogger(FormDaoHelper.class);

	/**
	 * 
	 * @return Forms in the detabase (Only general form details are included -
	 *         no fields & Form properties are included)
	 */
	public static List<Form> getForms(Long processDefId) {
		return getForms(processDefId, false);
	}

	public static List<Form> getForms(Long processDefId, boolean loadFields) {

		FormDaoImpl dao = DB.getFormDao();
		List<ADForm> adforms = dao.getAllForms(processDefId);

		List<Form> forms = new ArrayList<>();
		for (ADForm adform : adforms) {
			forms.add(getForm(adform, loadFields));
		}

		return forms;
	}

	public static List<Form> getForms(String processRefId, boolean loadFields) {

		FormDaoImpl dao = DB.getFormDao();
		List<ADForm> adforms = dao.getAllForms(processRefId);

		List<Form> forms = new ArrayList<>();
		for (ADForm adform : adforms) {
			forms.add(getForm(adform, loadFields));
		}

		return forms;
	}

	/**
	 * Returns a Form based on ID - with all form details included
	 * 
	 * @param id
	 * @return
	 */
	public static Form getForm(Long id, boolean loadDetails) {
		FormDaoImpl dao = DB.getFormDao();

		ADForm adform = dao.getForm(id);
		Form form = getForm(adform, loadDetails);

		return form;
	}

	public static Form getForm(ADForm adform, boolean loadFields) {
		if (adform == null) {
			return null;
		}

		FormDaoImpl dao = DB.getFormDao();

		Form form = new Form();
		form.setCaption(adform.getCaption());

		if (loadFields) {
			Collection<ADField> fields = dao.getFields(adform);
			for (ADField fld : fields) {
				Field field = getField(fld);

				if (!field.getDependentFields().isEmpty()) {
					form.addFieldDependency(field.getDependentFields(),
							field.getName());
				}

				form.addField(field);
			}
			// form.setFields(getFields());
		}

		form.setRefId(adform.getRefId());
		form.setRefId(adform.getRefId());
		form.setName(adform.getName());
		form.setProcessDefId(adform.getProcessDefId());
		form.setProcessRefId(adform.getProcessRefId());
		form.setProperties(getProperties(dao.getProperties(adform)));

		return form;
	}

	/**
	 * Retrieves a field and field value
	 * 
	 * @param id
	 * @return
	 */
	public static Field getField(Long id) {
		FormDaoImpl dao = DB.getFormDao();
		ADField adfield = dao.getField(id);
		Field field = getField(adfield);

		return field;
	}

	private static Field getField(ADField adfield) {
		Field field = new Field();
		field.setCaption(adfield.getCaption());
		if (adfield.getForm() != null) {
			field.setForm(adfield.getForm().getId(), adfield.getForm()
					.getRefId());
		}

		field.setRefId(adfield.getRefId());
		field.setName(adfield.getName());
		field.setProperties(getProperties(DB.getFormDao()
				.getProperties(adfield)));
		field.setType(adfield.getType());
		field.setValue(getValue(adfield.getValue(), adfield.getType()));
		field.setPosition(adfield.getPosition() == null ? 0 : adfield
				.getPosition().intValue());

		if (field.getType().isLookup()) {
			loadLookup(null, field);
		}

		if (field.getType().hasChildren()) {

			Collection<ADField> flds = DB.getFormDao().getFields(adfield);

			for (ADField fld : flds) {
				Field retrieved = getField(fld);
				if (!field.contains(retrieved)) {
					field.addField(retrieved);
				}

			}
		}

		return field;
	}

	private static void loadLookup(Doc context, Field field) {
		String type = null;
		String sqlDS = null;
		String sqlSelect = null;

		for (KeyValuePair prop : field.getProps()) {
			if (prop.getName().equals("SELECTIONTYPE")) {
				type = prop.getValue();
			}

			if (prop.getName().equals("SQLSELECT")) {
				sqlSelect = prop.getValue();
			}

			if (prop.getName().equals("SQLDS")) {
				sqlDS = prop.getValue();
			}

		}

		if (sqlDS != null && !sqlDS.isEmpty()) {
			if (sqlSelect != null && !sqlSelect.isEmpty()) {
				// Takes Precedence
				try {
					LookupLoader loader = new LookupLoaderImpl();
					field.setDependentFields(AnnotationParserImpl
							.extractAnnotations(sqlSelect));

					if (context != null) {
						sqlSelect = AnnotationParserImpl.parseForSQL(context,
								sqlSelect);
						logger.debug("SQL Parse: " + sqlSelect);
					}

					field.setSelectionValues(loader.getValuesByDataSourceName(
							sqlDS, sqlSelect));
				} catch (Exception e) {
					logger.warn("#Dropdown Query Failed -  " + e.getMessage());
				}
			}

		} else if (type != null) {

			field.setSelectionValues(getDropdownValues(type));
		}

	}

	public static Collection<? extends FormModel> getFields(Long parentId,
			boolean loadDetailsToo) {
		FormDaoImpl dao = DB.getFormDao();
		List<ADField> adfields = dao.getFields(parentId);

		return getFields(adfields);
	}

	private static List<Field> getFields(Collection<ADField> adfields) {
		List<Field> fields = new ArrayList<>();

		if (adfields != null) {
			for (ADField adfield : adfields) {
				fields.add(getField(adfield));
			}
		}

		return fields;
	}

	/**
	 * Retrieves a Property and its value
	 * 
	 * @param id
	 * @return
	 */
	public static Property getProperty(Long id) {
		FormDaoImpl dao = DB.getFormDao();
		ADProperty adproperty = dao.getProperty(id);
		Property property = getProperty(adproperty);

		return property;
	}

	private static Property getProperty(ADProperty adproperty) {

		if (adproperty == null)
			return null;

		Property property = new Property();
		property.setCaption(adproperty.getCaption());

		ADField field = adproperty.getField();
		property.setFieldRefId(field == null ? null : field.getRefId());

		ADForm form = adproperty.getForm();
		property.setFormRefId(form == null ? null : form.getRefId());

		assert form == null ^ field == null; // XOR

		property.setRefId(adproperty.getRefId());
		property.setName(adproperty.getName());
		property.setType(adproperty.getType());

		property.setValue(getValue(adproperty.getValue(), adproperty.getType()));

		if (property.getType().isDropdown()) {
			if (property.getName() != null
					&& property.getName().equals("SQLDS")) {
				property.setSelectionValues(DSConfigHelper.getKeyValuePairs());
			}
		}

		return property;
	}

	private static ArrayList<Property> getProperties(
			Collection<ADProperty> properties) {
		ArrayList<Property> props = new ArrayList<>();
		if (properties == null)
			return props;

		for (ADProperty property : properties) {
			props.add(getProperty(property));
		}

		return props;
	}

	public static Value getValue(ADValue advalue, DataType type) {
		if (advalue == null)
			return null;

		if (type == null) {
			logger.warn("FormDaoHelper.GetValue: [Field="
					+ advalue.getFieldName() + "] Datatype must be provided");
			return null;
		}

		Value value = null;

		Long id = advalue.getId();
		String key = advalue.getFieldName();

		switch (type) {
		case BOOLEAN:
			value = new BooleanValue(id, key, advalue.getBooleanValue());
			break;

		case DATE:
			value = new DateValue(id, key, advalue.getDateValue());
			break;

		case DOUBLE:
			value = new DoubleValue(id, key, advalue.getDoubleValue());
			break;

		case INTEGER:
			value = new LongValue(id, key, advalue.getLongValue());
			break;

		case STRING:
		case CHECKBOXGROUP:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

		case CHECKBOX:
			value = new BooleanValue(id, key, advalue.getBooleanValue());
			break;

		case MULTIBUTTON:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

		case SELECTBASIC:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

		case SELECTMULTIPLE:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

		case STRINGLONG:
			if (advalue.getTextValue() != null) {
				value = new TextValue(id, key, advalue.getTextValue());
			} else {
				// For backward compatibility
				value = new TextValue(id, key, advalue.getStringValue());
			}

			break;
		}

		return value;
	}

	public static Value getValue(Long id, String key, Object obj, DataType type) {
		if (obj == null)
			return null;

		if (type == null) {
			logger.warn("FormDaoHelper.GetValue: [Field=" + key
					+ "] Datatype must be provided");
			return null;
		}

		Value value = null;

		switch (type) {
		case BOOLEAN:
			value = new BooleanValue(id, key, (Boolean) obj);
			break;

		case DATE:
			value = new DateValue(id, key, (Date) obj);
			break;

		case DOUBLE:
			value = new DoubleValue(id, key, ((Number) obj).doubleValue());
			break;

		case INTEGER:
			value = new LongValue(id, key, ((Number) obj).longValue());
			break;

		case STRING:
		case CHECKBOXGROUP:
			value = new StringValue(id, key, obj.toString());
			break;

		case CHECKBOX:
			value = new BooleanValue(id, key, (Boolean) obj);
			break;

		case MULTIBUTTON:
			value = new StringValue(id, key, obj.toString());
			break;

		case SELECTBASIC:
			value = new StringValue(id, key, obj.toString());
			break;

		case SELECTMULTIPLE:
			value = new StringValue(id, key, obj.toString());
			break;

		case STRINGLONG:
			value = new TextValue(id, key, obj.toString());
			break;
		}

		return value;
	}

	public static void deleteValue(Long valueId) {
		DB.getFormDao().deleteValue(valueId);
	}

	public static Form createForm(Form form, Boolean reloadAll) {

		FormDaoImpl dao = DB.getFormDao();

		ADForm adform = new ADForm();
		if (form.getRefId() != null) {
			adform = dao.findByRefId(form.getRefId(), ADForm.class);
		}
		adform.setCaption(form.getCaption());
		adform.setName(form.getName());
		adform.setProcessDefId(form.getProcessDefId());
		adform.setProcessRefId(form.getProcessRefId());

		if (form.getProcessDefId() == null) {
			if (form.getProcessRefId() != null) {
				adform.setProcessDefId(DB.getProcessDao().getProcessDefId(
						form.getProcessRefId()));
			}
		}

		getADFields(form.getFields(), adform);
		// getADProperties(form.getProperties(), adform); --Method replaced with
		// getXXXJson
		// adform.setProperties(properties);
		dao.save(adform);

		assert adform.getId() != null;

		return getForm(adform, reloadAll);
	}

	private static void getADFields(List<Field> fields, ADForm adform) {

		if (fields == null) {
			return;
		}

		for (Field field : fields) {
			adform.addField(getField(field));
		}
	}

	private static ADField getField(Field field) {

		FormDaoImpl dao = DB.getFormDao();

		ADField adfield = new ADField();
		int previous = -1;

		if (field.getRefId() != null) {
			adfield = dao.findByRefId(field.getRefId(), ADField.class);
			previous = adfield.getPosition() == null ? -1 : adfield
					.getPosition();
		}

		adfield.setCaption(field.getCaption());
		adfield.setName(field.getName());

		if (field.getFormId() != null)
			adfield.setForm(dao.getForm(field.getFormId()));

		// for a grid - Grid Columns
		// adfield.getFields().clear();
		adfield.setFields(new HashSet<ADField>());
		if (field.getFields() != null) {
			List<Field> children = field.getFields();

			for (Field childField : children) {
				ADField child = getField(childField);
				adfield.addField(child);
			}
		}

		// copy
		// adfield.getProperties().size();// force load?
		// getADProperties(field.getProperties(), adfield); -- Replaced with
		// json method

		adfield.setType(field.getType());
		adfield.setValue(getValue(adfield.getValue(), field.getValue()));

		// call this only if field is a direct child of form
		// Field may be a child of a grid - which resets positions automatically
		// from the front end
		if (adfield.getForm() != null) {
			dao.setPosition(adfield, previous, field.getPosition());
		} else {
			adfield.setPosition(field.getPosition());
		}

		return adfield;
	}

	public static Field createField(Field field) {
		// List<KeyValuePair> pairs = field.getSelectionValues();
		String selectionKey = null;

		if (field.getType().equals(DataType.GRID)) {
			if (field.getName() == null || field.getName().isEmpty()) {
				field.setName(UUID.randomUUID().toString());
			}
		}
		FormDaoImpl dao = DB.getFormDao();

		ADField adfield = getField(field);

		dao.save(adfield);

		if (selectionKey != null && !selectionKey.trim().isEmpty()) {
			save(selectionKey, field.getSelectionValues());
		}

		return getField(adfield);
	}

	private static void getADProperties(List<Property> propertiesFrom,
			HasProperties formComponent) {

		if (propertiesFrom != null) {
			for (Property prop : propertiesFrom) {

				ADProperty property = get(prop);
				formComponent.addProperty(property);
			}
		}
	}

	public static Property createProperty(Property property) {
		FormDaoImpl dao = DB.getFormDao();

		ADProperty adprop = get(property);

		dao.save(adprop);

		return getProperty(adprop);
	}

	private static ADProperty get(Property property) {
		FormDaoImpl dao = DB.getFormDao();

		ADProperty adprop = new ADProperty();

		if (property.getRefId() != null) {
			adprop = dao.findByRefId(property.getRefId(), ADProperty.class);
		}

		if (property.getFormRefId() != null) {
			ADForm form = dao
					.findByRefId(property.getFormRefId(), ADForm.class);
			adprop.setForm(form);
		}

		if (property.getFieldRefId() != null) {
			ADField field = dao.findByRefId(property.getFieldRefId(),
					ADField.class);
			adprop.setField(field);
		}

		adprop.setCaption(property.getCaption());
		adprop.setRefId(property.getRefId());
		adprop.setName(property.getName());
		adprop.setType(property.getType());

		adprop.setValue(getValue(adprop.getValue(), property.getValue()));

		return adprop;
	}

	public static Value createValueForField(Long fieldId, Value value) {
		FormDaoImpl dao = DB.getFormDao();
		ADField field = dao.getField(fieldId);

		ADValue advalue = getValue(field.getValue(), value);
		field.setValue(advalue);

		dao.save(advalue);
		return getValue(dao.getValue(advalue.getId()), value.getDataType());
	}

	public static Value createValueForProperty(Long propertyId, Value value) {
		FormDaoImpl dao = DB.getFormDao();
		ADProperty prop = dao.getProperty(propertyId);

		ADValue advalue = getValue(prop.getValue(), value);
		advalue.setProperty(prop);

		dao.save(advalue);
		return getValue(dao.getValue(advalue.getId()), value.getDataType());
	}

	public static ADValue getValue(ADValue advalue, Value value) {
		if (value == null)
			return null;

		if (advalue == null) {
			advalue = new ADValue();
		} else if (advalue.getId() == null && value.getId() != null) {
			advalue.setId(value.getId());
		}

		advalue.setFieldName(value.getKey());
		if (value.getValue() == null)
			return advalue;

		switch (value.getDataType()) {
		case BOOLEAN:
			advalue.setBooleanValue((Boolean) value.getValue());
			break;

		case DATE:
			advalue.setDateValue((Date) value.getValue());
			break;

		case DOUBLE:
			advalue.setDoubleValue((Double) value.getValue());
			break;

		case INTEGER:
			if (value.getValue() instanceof Integer) {
				advalue.setLongValue(new Long((Integer) value.getValue()));
			} else {
				advalue.setLongValue((Long) value.getValue());
			}
			break;

		case CHECKBOX:
			advalue.setBooleanValue((Boolean) value.getValue());
			break;

		case STRING:
		case CHECKBOXGROUP:
		case MULTIBUTTON:
		case SELECTBASIC:
		case SELECTMULTIPLE:
			// System.err.println("Save Value >> "+value.getValue());
			advalue.setStringValue((String) value.getValue());
			break;

		case STRINGLONG:
			advalue.setTextValue((String) value.getValue());
			break;

		case FILEUPLOAD:
			advalue.setLongValue((Long) value.getValue()); // Id of attachment
			break;
		}

		return advalue;
	}

	public static void delete(FormModel model) {

		FormDaoImpl dao = DB.getFormDao();
		PO po = null;
		if (model instanceof Form) {
			deleteJsonForm(model.getRefId());
			// po = dao.findByRefId(model.getRefId(), ADForm.class);
		}

		if (model instanceof Field) {
			deleteJsonField(model.getRefId());
			// po = dao.findByRefId(model.getRefId(), ADField.class);

		}

		if (po != null) {
			dao.delete(po);
		}
	}

	public static ArrayList<KeyValuePair> getDropdownValues(String type) {
		FormDaoImpl dao = DB.getFormDao();
		List<ADKeyValuePair> adkeyvaluepairs = dao.getKeyValuePairs(type);

		ArrayList<KeyValuePair> keyvaluepairs = new ArrayList<>();
		for (ADKeyValuePair adpair : adkeyvaluepairs) {
			KeyValuePair kvp = new KeyValuePair();
			kvp.setKey(adpair.getName());
			kvp.setValue(adpair.getDisplayValue());

			keyvaluepairs.add(kvp);
		}

		return keyvaluepairs;
	}

	public static FormModel getFormByName(String formName) {
		if (formName == null) {
			return null;
		}
		Long formId = DB.getFormDao().getFormByName(formName);

		if (formId == null) {
			return null;
		}
		return getForm(formId, true);
	}

	public static void save(String selectionKey,
			Collection<KeyValuePair> keyValuePairs) {
		FormDaoImpl dao = DB.getFormDao();
		List<ADKeyValuePair> adkeyvaluepairs = dao
				.getKeyValuePairs(selectionKey);// previous set

		if (adkeyvaluepairs != null)
			for (ADKeyValuePair kvp : adkeyvaluepairs) {
				dao.delete(kvp);
			}

		if (keyValuePairs != null)
			for (KeyValuePair keyValuePair : keyValuePairs) {
				ADKeyValuePair pair = new ADKeyValuePair();
				pair.setReferenceType(selectionKey);
				pair.setDisplayValue(keyValuePair.getDisplayName());

				if (keyValuePair.getName() == null
						|| keyValuePair.getName().trim().isEmpty()) {
					pair.setName(keyValuePair.getValue());
				} else {
					pair.setName(keyValuePair.getName());
				}

				dao.save(pair);
			}
	}

	public static Long importForm(String xml) {
		FormDaoImpl dao = DB.getFormDao();
		ADForm form = transform(xml);

		assert form != null;

		String name = form.getName();
		String caption = form.getCaption();
		boolean exists = false;
		if (dao.exists(form.getName())) {
			exists = true;
			form.setName(name + "00000");
			form.setCaption(caption + "0000");
		}

		Collection<ADProperty> properties = dao.getProperties(form);
		for (ADProperty prop : properties) {
			prop.setForm(form);

			if (prop.getValue() != null) {
				prop.getValue().setProperty(prop);
			}
		}

		Collection<ADField> formFields = dao.getFields(form);
		for (ADField field : formFields) {
			field.setForm(form);
			Collection<ADProperty> fieldProps = dao.getProperties(field);
			if (fieldProps != null)
				for (ADProperty prop : fieldProps) {
					prop.setField(field);
					if (prop.getValue() != null) {
						prop.getValue().setProperty(prop);
					}

					if (field.getKeyValuePairs() != null
							&& prop.getName() != null
							&& prop.getName().equals(SELECTIONTYPE)
							&& prop.getValue() != null) {
						save(prop.getValue().getStringValue(),
								field.getKeyValuePairs());
					}
				}

			Collection<ADField> children = dao.getFields(field);
			if (children != null)
				for (ADField child : children) {
					child.setParentField(field);
				}

			if (field.getValue() != null) {
				field.getValue().setField(field);
			}
		}

		dao.save(form);

		if (exists) {
			// update
			form.setName(name + "-" + form.getId());
			form.setCaption(caption + "-" + form.getId());
			dao.save(form);
		}

		return form.getId();
	}

	public static ADForm transform(String xml) {

		JAXBContext context = new JaxbFormExportProviderImpl()
				.getContext(ADForm.class);

		ADForm form = null;

		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			Object obj = unmarshaller.unmarshal(new StringReader(xml));

			form = (ADForm) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return form;
	}

	public static List<Field> loadFieldValues(Doc doc, List<Field> fields) {
		List<Field> reloaded = new ArrayList<Field>();

		for (Field field : fields) {
			loadLookup(doc, field);
			reloaded.add(field);
		}

		return reloaded;
	}

	public static Form createJson(Form form) {
		ADFormJson jsonForm = new ADFormJson(form);
		if (form.getRefId() != null) {
			jsonForm = DB.getFormDao().findByRefId(form.getRefId(),
					ADFormJson.class);
			if (jsonForm == null) {
				jsonForm = new ADFormJson(form);
			} else {
				jsonForm.setForm(form);
			}
		} else {
			// Generate Ref
			form.setRefId(IDUtils.generateId());
			jsonForm.setRefId(form.getRefId());
		}

		boolean isNew = false;
		if (jsonForm.getId() == null) {
			isNew = true;
		}

		// Save
		DB.getFormDao().save(jsonForm);
		if (isNew) {
			// Set Default values
			setDefaultValues(jsonForm);
		}

		if (form.getFields() != null) {
			for (Field field : form.getFields()) {
				field.setForm(jsonForm.getId(), jsonForm.getRefId());
				createJson(field);
			}
		}

		return jsonForm.getForm();
	}

	private static void setDefaultValues(ADFormJson jsonForm) {
		Form form = jsonForm.getForm();
		if (jsonForm.getName() == null || jsonForm.getName().isEmpty()
				|| jsonForm.getName().equals("Untitled")) {
			jsonForm.setName("Untitled" + jsonForm.getId());
			form.setName(jsonForm.getName());
		}

		if (jsonForm.getCaption() == null
				|| jsonForm.getCaption().equals("Untitled")) {
			jsonForm.setCaption("Untitled" + jsonForm.getId());
			form.setCaption(jsonForm.getCaption());
		}

		ArrayList<KeyValuePair> props = new ArrayList<KeyValuePair>();
		props.add(new KeyValuePair("NAME", ""));
		props.add(new KeyValuePair("CAPTION", ""));
		// props.add(new KeyValuePair("DESCRIPTION", ""));

		for (KeyValuePair prop : props) {
			if (prop.getName().equals("NAME")) {

				if (prop.getValue() == null
						|| (prop.getValue() == null || !prop.getValue().equals(
								jsonForm.getName()))) {
					prop.setValue(jsonForm.getName());
				}
			}

			if (prop.getName().equals("CAPTION")) {

				if (prop.getValue() == null
						|| (prop.getValue() == null || !prop.getValue().equals(
								jsonForm.getCaption()))) {
					prop.setValue(jsonForm.getCaption());
				}
			}

			if (prop.getName().equals("DESCRIPTION")) {
				if (prop.getValue() == null) {
					prop.setValue(jsonForm.getName());
				}
			}
		}

		form.setProps(props);
		jsonForm.setForm(form);

		DB.getFormDao().save(jsonForm);

	}

	public static Field createJson(Field field) {
		// Save Parent
		ADFieldJson jsonField = new ADFieldJson(field);
		int previousIdx = -1;
		if (field.getRefId() != null) {
			jsonField = DB.getFormDao().findByRefId(field.getRefId(),
					ADFieldJson.class);
			if (jsonField == null) {
				jsonField = new ADFieldJson(field);
			} else {
				previousIdx = jsonField.getField().getPosition();
				jsonField.setField(field);
			}
		} else {
			// Generate Ref
			field.setRefId(IDUtils.generateId());
			jsonField.setRefId(field.getRefId());
		}
		
		DB.getFormDao().save(jsonField);
		field.setId(jsonField.getId());

		if(field.getParentRef()==null){
			//shift form fields incase there was a change
			shiftPositions(field,previousIdx, field.getPosition());
		}

		for (Field child : field.getFields()) {
			child.setParent(jsonField.getId(), jsonField.getRefId());
			child.setForm(field.getFormId(), field.getFormRef());
			createJson(child);
		}

		return field;
	}

	private static void shiftPositions(Field updatedField, int previousPos,
			int newPosition) {
		if(previousPos==-1 || previousPos==newPosition){
			return;
		}
		boolean up = previousPos>newPosition;
		
		logger.debug("Shifting field "+updatedField.getName()+" position from index "+previousPos+" to index "+newPosition);
		ArrayList<Field> affectedFields = DB.getFormDao().getFormFieldsByPosition(updatedField.getFormRef(), previousPos, newPosition);
		Collections.sort(affectedFields, new Comparator<FormModel>() {
			public int compare(FormModel o1, FormModel o2) {
				Field field1 = (Field) o1;
				Field field2 = (Field) o2;

				Integer pos1 = field1.getPosition();
				Integer pos2 = field2.getPosition();

				return pos1.compareTo(pos2);
			};

		});
		
		
		for(Field shift: affectedFields){
			if(up){
				//Shift every field one step down from this field's new  position
				shift.setPosition(++newPosition);
			}else{
				/*
				 * Shift every field one step up from the field's previous position with the first field in the list 
				 * taking the field's previous position
				 */
				shift.setPosition(previousPos++);
			}
			
			//Save field
			ADFieldJson jsonField = DB.getFormDao().findByRefId(shift.getRefId(),
					ADFieldJson.class);
			jsonField.setField(shift);
			DB.getFormDao().save(jsonField);
		}
	}

	public static Field getFieldJson(String refId) {
		return getFieldJson(refId, true);
	}

	public static Field getFieldJson(String refId, boolean loadChildren) {

		Field field = DB.getFormDao().findJsonField(refId);

		if (field.getType().hasChildren() && loadChildren) {
			loadFieldsForParent(field);
		}

		return field;
	}

	public static Form getFormJson(String refId, boolean loadFields) {
		ADFormJson jsonForm = DB.getFormDao().findByRefId(refId,
				ADFormJson.class);
		if(jsonForm==null){
			return null;
		}

		Form form = jsonForm.getForm();
		form.setRefId(refId);

		if (loadFields) {
			form.setFormulae(DB.getFormDao().getFormulae(refId));
			// Retrieve fields
			loadFormFields(form);
			
		}
		return form;
	}

	private static void loadFormFields(Form form) {
		ArrayList<Field> fields = DB.getFormDao()
				.findJsonFieldsForForm(form.getRefId());

		for (Field field : fields) {
			//Load selection values
			if(field.getType().isLookup()){
				loadLookupJson(null, field); //Field Dependencies set here
			}
			
			form.addFieldDependency(field.getDependentFields(),
					field.getName());//Add Field Dependencies to the form
			
			if (field.getType().hasChildren()) {
				loadFieldsForParent(field);
			}
		}
		form.setFields(fields);
	}

	public static void loadFieldsForParent(Field parentField) {
		ArrayList<Field> children = DB.getFormDao().findJsonFieldsForField(
				parentField.getRefId());
		for(Field child : children){
			//Load selection values
			if(child.getType().isLookup()){
				loadLookupJson(null, child);
			}
			
			if(child.getType().hasChildren()){
				loadFieldsForParent(child);
			}
		}
		parentField.setFields(children);
	}

	public static List<Form> getFormsJson(String processRefId,
			boolean loadChildren) {

		List<Form> forms = DB.getFormDao()
				.findJsonFormsForProcess(processRefId);
		if (loadChildren) {
			for (Form form : forms) {
				loadFormFields(form);
			}
		}
		return forms;
	}
	
	private static void loadLookupJson(Doc context, Field field) {
		String type = null;
		String sqlDS = null;
		String sqlSelect = null;

		for (KeyValuePair prop : field.getProps()) {
			if (prop.getName().equals("SELECTIONTYPE")) {
				type = prop.getValue();
			}

			if (prop.getName().equals("SQLSELECT")) {
				sqlSelect = prop.getValue();
			}

			if (prop.getName().equals("SQLDS")) {
				sqlDS = prop.getValue();
			}

		}

		if (sqlDS != null && !sqlDS.isEmpty()) {
			if (sqlSelect != null && !sqlSelect.isEmpty()) {
				// Takes Precedence
				try {
					LookupLoader loader = new LookupLoaderImpl();
					field.setDependentFields(AnnotationParserImpl
							.extractAnnotations(sqlSelect));

					if (context != null) {
						sqlSelect = AnnotationParserImpl.parseForSQL(context,
								sqlSelect);
						logger.debug("SQL Parse: " + sqlSelect);
					}

					field.setSelectionValues(loader.getValuesByDataSourceName(
							sqlDS, sqlSelect));
				} catch (Exception e) {
					logger.warn("#Dropdown Query Failed -  " + e.getMessage());
				}
			}

		} else if (type != null) {
			//No longer required in json, since selection values are stored as a json array in the field
			//field.setSelectionValues(getDropdownValues(type));
		}

	}

	private static void deleteJsonField(String fieldRefId) {
		FormDaoImpl dao = DB.getFormDao();
		dao.deleteJsonField(fieldRefId);
	}

	private static void deleteJsonForm(String fieldRefId) {
		FormDaoImpl dao = DB.getFormDao();
		dao.deleteJsonForm(fieldRefId);
	}

	public static String exportForm(Form form) {
		return ProcessDaoHelper.exportFormJson(form);
	}
	
	public static String exportForm(String formRefId) {
		Form form = getFormJson(formRefId, true);
		return ProcessDaoHelper.exportFormJson(form);
	}
}
