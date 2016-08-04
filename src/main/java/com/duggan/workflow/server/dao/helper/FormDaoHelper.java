package com.duggan.workflow.server.dao.helper;

import static com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.NAME;
import static com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.SELECTIONTYPE;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
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
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.HasProperties;
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

		form.setId(adform.getId());
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
		field.setFormId(adfield.getForm() == null ? null : adfield.getForm()
				.getId());
		field.setId(adfield.getId());
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

		for (Property prop : field.getProperties()) {
			if (prop.getName().equals("SELECTIONTYPE")) {
				Object value = prop.getValue() == null ? null : prop.getValue()
						.getValue();
				type = value == null ? null : value.toString();
			}

			if (prop.getName().equals("SQLSELECT")) {
				Object value = prop.getValue() == null ? null : prop.getValue()
						.getValue();
				sqlSelect = value == null ? null : value.toString();
			}

			if (prop.getName().equals("SQLDS")) {
				Object value = prop.getValue() == null ? null : prop.getValue()
						.getValue();
				sqlDS = value == null ? null : value.toString();
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
		property.setFieldId(field == null ? null : field.getId());

		ADForm form = adproperty.getForm();
		property.setFormId(form == null ? null : form.getId());

		assert form == null ^ field == null; // XOR

		property.setId(adproperty.getId());
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
		if (form.getId() != null) {
			adform = dao.getForm(form.getId());
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
		getADProperties(form.getProperties(), adform);
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

		if (field.getId() != null) {
			adfield = dao.getField(field.getId());
			previous = adfield.getPosition() == null ? -1 : adfield
					.getPosition();
		}

		adfield.setCaption(field.getCaption());
		adfield.setName(field.getName());

		if (field.getFormId() != null)
			adfield.setForm(dao.getForm(field.getFormId()));

		adfield.setId(field.getId());

		// for a grid - Grid Columns
		//adfield.getFields().clear();
		adfield.setFields(new HashSet<ADField>());
		if (field.getFields() != null) {
			List<Field> children = field.getFields();

			for (Field childField : children) {
				ADField child = getField(childField);
				adfield.addField(child);
			}
		}

		// copy
//		adfield.getProperties().size();// force load?
		getADProperties(field.getProperties(), adfield);

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

		for (Property prop : field.getProperties()) {
			if (field.getType() == DataType.GRID && prop.getName().equals(NAME)) {
				// mostly for grids
				Value value = prop.getValue();
				String name = null;
				if (value != null) {
					name = value.getValue() == null ? null : value.getValue()
							.toString();
				}

				if (name == null || name.isEmpty()) {
					prop.setValue(new StringValue(null, NAME, field.getName()));
				}
			}

			if (prop.getName().equals(SELECTIONTYPE)) {
				Value value = prop.getValue();
				if (value != null) {
					selectionKey = value.getValue() == null ? null : value
							.getValue().toString();
				}

				if (selectionKey == null || selectionKey.trim().isEmpty()) {
					selectionKey = UUID.randomUUID().toString();
					prop.setValue(new StringValue(
							null,
							com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.SELECTIONTYPE,
							selectionKey));
				}
				break;

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
				// System.err.println("Save: "+property);

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

		if (property.getId() != null) {
			adprop = dao.getProperty(property.getId());
		}

		if (property.getFormId() != null) {
			adprop.setForm(dao.getForm(property.getFormId()));
		}

		if (property.getFieldId() != null) {
			adprop.setField(dao.getField(property.getFieldId()));
		}

		adprop.setCaption(property.getCaption());
		adprop.setId(property.getId());
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
			po = dao.getForm(model.getId());
		}

		if (model instanceof Field) {
			po = dao.getField(model.getId());

		}

		if (model instanceof Property) {
			po = dao.getProperty(model.getId());
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

	/**
	 * Converts Form into an XML representation
	 * 
	 * @param formId
	 * @return
	 */
	public static String exportForm(Long formId) {
		FormDaoImpl dao = DB.getFormDao();
		ADForm form = dao.getForm(formId);
		Collection<ADField> fields = dao.getFields(form);

		if (fields != null)
			for (ADField field : fields) {
				if (field.getType() == DataType.SELECTBASIC) {
					ADProperty prop = field.getProperty(SELECTIONTYPE);
					ADValue value = prop.getValue();
					String referenceName = value.getStringValue();
					field.setKeyValuePairs(getDropdownValues(referenceName));
				}
			}

		return exportForm(form);
	}

	public static String exportForm(ADForm form) {
		JAXBContext context = new JaxbFormExportProviderImpl()
				.getContext(ADForm.class);
		String out = null;
		try {
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter writer = new StringWriter();
			marshaller.marshal(form, writer);

			out = writer.toString();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
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
}
