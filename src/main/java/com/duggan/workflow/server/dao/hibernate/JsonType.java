package com.duggan.workflow.server.dao.hibernate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.ADProcessCategory;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.IntValue;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

public abstract class JsonType implements UserType {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public int[] sqlTypes() {
		return new int[] { Types.JAVA_OBJECT };
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return this.deepCopy(cached);
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		try {
			// use serialization to create a deep copy
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(value);
			oos.flush();
			oos.close();
			bos.close();

			ByteArrayInputStream bais = new ByteArrayInputStream(
					bos.toByteArray());
			return new ObjectInputStream(bais).readObject();
		} catch (ClassNotFoundException | IOException ex) {
			throw new HibernateException(ex);
		}
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) this.deepCopy(value);
	}

	@Override
	public boolean equals(Object obj1, Object obj2) throws HibernateException {
		if (obj1 == null) {
			return obj2 == null;
		}
		return obj1.equals(obj2);
	}

	@Override
	public int hashCode(Object obj) throws HibernateException {
		return obj.hashCode();
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor arg2, Object owner) throws HibernateException,
			SQLException {
		final String cellContent = rs.getString(names[0]);
		if (cellContent == null) {
			return null;
		}
		try {
			final JSONJAXBContext mapper = getJaxbContext();
			
			return mapper.createJSONUnmarshaller().unmarshalFromJSON(new ByteArrayInputStream(cellContent.getBytes("UTF-8")), returnedClass());
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Failed to convert String \" "+cellContent+" \" to "
					+ returnedClass() +" "+ ex.getMessage(), ex);
		}
	}
	
//	Hibernate 3.6
//	@Override
//	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
//			throws HibernateException, SQLException {
//		final String cellContent = rs.getString(names[0]);
//		if (cellContent == null) {
//			return null;
//		}
//		try {
//			final JSONJAXBContext mapper = getJaxbContext();
//			
//			return mapper.createJSONUnmarshaller().unmarshalFromJSON(new ByteArrayInputStream(cellContent.getBytes("UTF-8")), returnedClass());
//		} catch (final Exception ex) {
//			ex.printStackTrace();
//			throw new RuntimeException("Failed to convert String \" "+cellContent+" \" to "
//					+ returnedClass() +" "+ ex.getMessage(), ex);
//		}
//	}


	@Override
	public void nullSafeSet(PreparedStatement ps, Object value, final int idx,
			SessionImplementor arg3) throws HibernateException, SQLException {
		if (value == null) {
			ps.setNull(idx, Types.OTHER);
			return;
		}
		try {
			final JSONJAXBContext mapper = getJaxbContext();
			final StringWriter w = new StringWriter();
			mapper.createJSONMarshaller().marshallToJSON(value,w);
			w.flush();
			ps.setObject(idx, w.toString(), Types.OTHER);
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to convert Object to String: "
					+ ex.getMessage(), ex);
		}
	}
	
//	Version 3.6
//	@Override
//	public void nullSafeSet(PreparedStatement ps, Object value, final int idx)
//			throws HibernateException, SQLException {
//		if (value == null) {
//			ps.setNull(idx, Types.OTHER);
//			return;
//		}
//		try {
//			final JSONJAXBContext mapper = getJaxbContext();
//			final StringWriter w = new StringWriter();
//			mapper.createJSONMarshaller().marshallToJSON(value,w);
//			w.flush();
//			ps.setObject(idx, w.toString(), Types.OTHER);
//		} catch (final Exception ex) {
//			throw new RuntimeException("Failed to convert Object to String: "
//					+ ex.getMessage(), ex);
//		}
//	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return this.deepCopy(original);
	}

	public static JSONJAXBContext getJaxbContext() throws JAXBException {

		JSONJAXBContext context = new JSONJAXBContext(JSONConfiguration
				.natural().build(),
				DocValues.class,
				Form.class, Field.class, Property.class, 
				StringValue.class,DoubleValue.class,IntValue.class, LongValue.class,
				BooleanValue.class,DateValue.class,KeyValue.class,
				Document.class,DocumentLine.class,Doc.class,
				ProcessDefModel.class, TaskStepModel.class,
				ADTaskStepTrigger.class, ADTrigger.class, ADDocType.class,
				ADProcessCategory.class, ADForm.class, ADOutputDoc.class,
				ADField.class, ADProperty.class, ADValue.class,
				ADKeyValuePair.class, KeyValuePair.class, CatalogModel.class,
				CatalogColumnModel.class);
		return context;
	}
}
