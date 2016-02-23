package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.duggan.workflow.shared.model.catalog.CatalogType;

public class CatalogDaoImpl extends BaseDaoImpl {

	Logger log = Logger.getLogger(CatalogDaoImpl.class);

	public CatalogDaoImpl(EntityManager em) {
		super(em);
	}

	public List<CatalogModel> getCatalogs() {
		return getResultList(em
				.createQuery("FROM CatalogModel c where c.isActive=1"));
	}

	public void generateTable(Catalog model) {
		String drop = "DROP TABLE IF EXISTS EXT_" + model.getName() + ";";
		log.debug("Exec DDL: " + drop);

		StringBuffer create = new StringBuffer("CREATE TABLE EXT_"
				+ model.getName() + "(");
		int i = 0;
		for (CatalogColumn col : model.getColumns()) {
			create.append("\"" + col.getName() + "\"");

			if (col.isAutoIncrement()) {
				create.append(col.isAutoIncrement() ? " serial" : "");// POSTGRES
			} else {
				create.append(" " + col.getType().name());
				create.append((col.getSize() == null || col.getSize() == 0) ? ""
						: "(" + col.getSize() + ")");
			}

			create.append(col.isPrimaryKey() ? " PRIMARY KEY" : "");
			create.append(col.isNullable() ? " NULL" : " NOT NULL");

			if (model.getColumns().size() - 1 != i) {
				create.append(",");
			}

			++i;
		}
		create.append(")");
		log.debug("Exec DDL: " + create);

		// DROP
		em.createNativeQuery(drop).executeUpdate();
		// CREATE
		em.createNativeQuery(create.toString()).executeUpdate();

	}

	public List<Object[]> getData(String tableName,
			String comma_Separated_fieldNames) {

		return getResultList(em.createNativeQuery("select "
				+ comma_Separated_fieldNames + " from " + tableName));
	}

	public void save(String tableName, List<CatalogColumn> columns,
			List<DocumentLine> lines, boolean isClearExisting) {

		if (isClearExisting) {
			String delete = "DELETE FROM " + tableName;
			log.debug("Exec DML: " + delete);
			em.createNativeQuery(delete).executeUpdate();
		}

		StringBuffer insertBuffer = new StringBuffer("INSERT INTO " + tableName
				+ " (");

		StringBuffer updateBuffer = new StringBuffer("UPDATE " + tableName
				+ " SET ");

		StringBuffer whereBuffer = new StringBuffer("WHERE ");

		String primaryKey = null;

		StringBuffer values = new StringBuffer(" VALUES (");
		int i = 0;
		for (CatalogColumn col : columns) {
			if (col.isPrimaryKey()) {
				primaryKey = col.getName();
				whereBuffer.append("" + col.getName() + "=:" + col.getName());
			}

			if (col.isAutoIncrement()) {
				continue;
			}

			// Insert Statement
			insertBuffer.append(col.getName());
			values.append(":" + col.getName());

			// Update Statement
			if (!col.isPrimaryKey()) {
				updateBuffer.append(col.getName() + "=:" + col.getName());
			}

			if (columns.size() - 1 != i) {
				insertBuffer.append(",");
				values.append(",");

				updateBuffer.append(",");
			}

			++i;
		}

		if (insertBuffer.toString().endsWith(",")) {
			insertBuffer.setCharAt(insertBuffer.length() - 1, ' ');
			updateBuffer.setCharAt(updateBuffer.length() - 1, ' ');
			values.setCharAt(values.length() - 1, ' ');
		}

		insertBuffer.append(") " + values + ")");

		if (primaryKey != null) {
			updateBuffer.append(whereBuffer);
		}

		if(isClearExisting){
			log.debug("Exec DML: " + insertBuffer.toString());
		}else{
			log.debug("Exec DML: " + updateBuffer.toString());
		}
		

		// Document Line
		for (DocumentLine line : lines) {
			
			boolean isUpdate = !isClearExisting && primaryKey != null;
			
			if (isUpdate) {
				Value val = line.getValue(primaryKey);
				if (val != null && val.getValue() != null) {
					Boolean isExists = (Boolean) em
							.createNamedQuery(
									"SELECT exists(select * from " + tableName
											+ " " + whereBuffer.toString()
											+ ")")
							.setParameter(primaryKey, val).getSingleResult();
					isUpdate = isExists;
				} else {
					logger.warn("#Report Table update row cannot be executed. "
							+ "Primary Key Field  '" + primaryKey
							+ "' is Null. Record: " + line);
					isUpdate = false;
				}
			}

			Query query = null;

			if (isUpdate) {
				query = em.createNativeQuery(updateBuffer.toString());
			} else {
				query = em.createNativeQuery(insertBuffer.toString());
			}

			//Set Values
			for (CatalogColumn col : columns) {
				if (col.isAutoIncrement()) {
					continue;
				}
				if(isUpdate && col.isPrimaryKey()){
					continue;
				}
				
				Value val = line.getValue(col.getName());
				Object value = val == null ? null : val.getValue();

				log.debug("CatalogDaoImpl.save(" + tableName
						+ ") sql Parameter " + col.getName() + "::"
						+ col.getType() + " = " + value);

				// Hardcoded to handle hibernate null
				if (value == null
						&& col.getType().getFieldType().equals(DataType.DOUBLE)) {
					query.setParameter(col.getName(), 0.0);
				} else if (val != null && val.getDataType().isDate()) {
					query.setParameter(col.getName(), value);
				} else {
					query.setParameter(col.getName(), value);
				}

			}
			query.executeUpdate();

		}

	}

	public int getCount(String tableName) {

		Number number = getSingleResultOrNull(em
				.createNativeQuery("select count(*) from " + tableName));
		return number.intValue();
	}

	public List<CatalogModel> getReportTablesByProcessId(String processId) {

		return getResultList(em
				.createQuery(
						"FROM CatalogModel c where c.isActive=1 and c.type=:type "
								+ " and exists "
								+ "(select id from ProcessDefModel p"
								+ " where p.id=c.processDefId and p.processId=:processId and p.isActive=1)")
				.setParameter("type", CatalogType.REPORTTABLE)
				.setParameter("processId", processId));

	}
}
