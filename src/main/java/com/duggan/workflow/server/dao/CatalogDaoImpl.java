package com.duggan.workflow.server.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.impl.SessionImpl;

import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.shared.model.DBType;
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
	
	@Override
	public void delete(PO po) {
		if(po instanceof CatalogModel){
			String name = ((CatalogModel)po).getName();
			if(!name.toLowerCase().startsWith("ext_")){
				name = name.toLowerCase().startsWith("ext_")? name.toLowerCase(): "ext_"+name.toLowerCase();
			}
			String query = "DROP TABLE "+name;
			em.createNativeQuery(query).executeUpdate();
		}
		super.delete(po);
	}

	public List<CatalogModel> getCatalogs(String searchTerm) {
		StringBuffer jpql = new StringBuffer(
				"FROM CatalogModel c where c.isActive=1");

		Map<String, Object> params = new HashMap<String, Object>();
		if (searchTerm != null) {
			jpql.append(" and (lower(c.name) like :searchTerm or lower(c.description) like :searchTerm) and c.isActive=1 ");
			params.put("searchTerm", "%" + searchTerm.toLowerCase() + "%");
		}
		jpql.append(" order by c.description");

		Query query = em.createQuery(jpql.toString());

		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query);

	}

	public void generateTable(Catalog model) {
		if (model.getType() == CatalogType.REPORTVIEW) {
			return;
		}

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
	

	public void updateTable(Catalog catalog) {
		CatalogModel model = getById(CatalogModel.class, catalog.getId());
		if(!catalog.getName().toLowerCase().equals(model.getName().toLowerCase())){
			String changeName = "ALTER TABLE ext_"+model.getName()+" RENAME TO ext_"+catalog.getName();
			logger.debug("RENAME TABLE - "+changeName);
			em.createNativeQuery(changeName).executeUpdate();
		}
				
		List<CatalogColumn> columns = catalog.getColumns();
		for(CatalogColumn col: columns){
			if(col.getId()==null){
				//create column
				addColumn(catalog, col);
			}else{
				//alter column
				alterColumn(catalog, col);
			}
		}
	}

	private void alterColumn(Catalog catalog, CatalogColumn col) {
		CatalogColumnModel model = getById(CatalogColumnModel.class, col.getId());
		if(!isChanged(col, model)){
			return;
		}
		
		String catalogName = catalog.getName().toLowerCase();
		if(!catalogName.startsWith("ext_")){
			catalogName = "ext_"+catalogName;
		}
		
		String ALTER_TABLE = "ALTER TABLE "+catalogName;
		String columnName="\""+col.getName()+"\"";
		if(!model.getName().equals(col.getName())){
			renameColumn(catalogName, model.getName(), col.getName());
		}
		
		//ALTER Column TYPE
		StringBuffer alter = new StringBuffer(ALTER_TABLE+" ALTER COLUMN "+columnName+" TYPE "+col.getType().name());
		if(col.getSize()!=null && col.getSize()!=0){
			alter.append("("+col.getSize()+")");
		}
		logger.debug("ALTER COLUMN: "+alter.toString());
		em.createNativeQuery(alter.toString()).executeUpdate();
		
		
		
		updateNullability(catalogName, columnName,col.isNullable(), col.isNullable()!=model.isNullable());
		updatePrimaryKey(catalogName, columnName,col.isPrimaryKey(), col.isPrimaryKey()!=model.isPrimaryKey());
	}

	private void addColumn(Catalog catalog, CatalogColumn col) {
		String catalogName = catalog.getName().toLowerCase();
		if(!catalogName.startsWith("ext_")){
			catalogName = "ext_"+catalogName;
		}
		
		String ALTER_TABLE = "ALTER TABLE "+catalogName;
		
		String columnName="\""+col.getName()+"\"";
		
		//ADD Column
		StringBuffer alter = new StringBuffer(ALTER_TABLE+" ADD COLUMN "+columnName);
		if (col.isAutoIncrement()) {
			alter.append(col.isAutoIncrement() ? " serial" : "");// POSTGRES
		} else {
			alter.append(" " + col.getType().name());
			alter.append((col.getSize() == null || col.getSize() == 0) ? ""
					: "(" + col.getSize() + ")");
		}

		alter.append(col.isPrimaryKey() ? " PRIMARY KEY" : "");
		alter.append(col.isNullable() ? " NULL" : " NOT NULL");
		
		logger.debug("ALTER COLUMN: "+alter.toString());
		em.createNativeQuery(alter.toString()).executeUpdate();
	}
	
	/**
	 * 
	 * @param catalogName
	 * @param previousName
	 * @param newName
	 */
	private void renameColumn(String catalogName, String previousName, String newName) {
		previousName = "\""+previousName+"\"";
		newName = "\""+newName+"\"";
		String rename = "ALTER TABLE "+catalogName+" RENAME "+previousName+" TO "+newName;
		em.createNativeQuery(rename).executeUpdate();
	}

	private void updatePrimaryKey(String catalogName, String columnName,
			boolean isPrimaryKey, boolean isChanged) {
		//Primary Key
		if(isChanged){
			String primaryKey = null;
			String dropKey = "ALTER TABLE "+catalogName+" DROP CONSTRAINT IF EXISTS "+catalogName+"_pkey";
			log.warn("DROP_KEY: "+dropKey);
			if(isPrimaryKey){
				em.createNativeQuery(dropKey).executeUpdate();
				primaryKey = "ALTER TABLE "+catalogName+" ADD PRIMARY KEY("+columnName+")";
			}else{
				primaryKey = "ALTER TABLE "+catalogName+" DROP CONSTRAINT IF EXISTS "+catalogName+"_pkey";
				em.createNativeQuery(primaryKey).executeUpdate();
			}
			logger.debug("PRIMARY KEY QUERY - "+primaryKey);
		}
	}

	private void updateNullability(String catalogName, String columnName,boolean nullable, boolean isChanged) {
		String ALTER_TABLE = "ALTER TABLE "+catalogName;
		//Nullability
		if(isChanged){
			String nullableQuery = null;
			if(nullable){
				nullableQuery =  ALTER_TABLE+" ALTER COLUMN "+columnName+" SET NOT NULL";
			}else{
				nullableQuery= ALTER_TABLE+" ALTER COLUMN "+columnName+" SET NULL";
			}
			logger.debug("NULLABLE QUERY = "+nullableQuery);
			em.createNativeQuery(nullableQuery).executeUpdate();
		}
	}


	private boolean isChanged(CatalogColumn col, CatalogColumnModel model) {
		boolean isChanged = false;
		if(!col.getName().equals(model.getName())){
			log.debug("["+col.getName()+"] Column Name changed from '"+model.getName()+"' -to- "+col.getName());
			isChanged = true;
		}
		
		if(col.getType()!= model.getType()){
			log.debug("["+col.getName()+"] Column Type changed from '"+model.getType()+"' -to- "+col.getType());
			isChanged = true;
		}
		
		if(col.isNullable()!=model.isNullable()){
			log.debug("["+col.getName()+"] Column Nullability changed from '"+model.isNullable()+"' -to- "+col.isNullable());
			isChanged = true;
		}
		
		if(col.isPrimaryKey()!= model.isPrimaryKey()){
			log.debug("["+col.getName()+"] Primary Key Field from "+model.isPrimaryKey()+" -to "+col.isPrimaryKey());
			isChanged = true;
		}
		
		if(col.isAutoIncrement()!=model.isAutoIncrement()){
			log.debug("["+col.getName()+"] Autoincrement changed from '"+model.isAutoIncrement()+"' -to- "+col.isAutoIncrement());
			isChanged = true;
		}
		
		if(coalesce(col.getSize(),0)!=coalesce(model.getSize(),0)){
			log.debug("["+col.getName()+"] Column Size changed from "+model.getSize()+" -to- "+col.getSize());
			isChanged = true;
		}
		
		return isChanged;
	}

	private int coalesce(Integer val, int valueIfNull) {
		return val==null? valueIfNull: val.intValue();
	}

	public List<Object[]> getData(String tableName,
			String comma_Separated_fieldNames, String searchTerm,
			List<String> searchCols) {

		String[] quotedFieldNames = comma_Separated_fieldNames.split(",");
		comma_Separated_fieldNames="";
		int len = quotedFieldNames.length;
		for(int i=0; i<len; i++){
			comma_Separated_fieldNames =   comma_Separated_fieldNames+"\""+quotedFieldNames[i]+"\"";
			if(i+1!=len){
				comma_Separated_fieldNames = comma_Separated_fieldNames+",";
			}
		}
		
		StringBuffer jpql = new StringBuffer("select "
				+ comma_Separated_fieldNames + " from " + tableName);

		Map<String, Object> params = new HashMap<String, Object>();
		if (searchTerm != null && !searchCols.isEmpty()) {
			jpql.append(" where (");

			for (int i=0; i<searchCols.size(); i++) {
				if(i!=0){
					jpql.append("or ");
				}
				String col = searchCols.get(i);
				jpql.append("lower("+col+") like :searchTerm  ");
			}
			
			jpql.append(" )");

			params.put("searchTerm", "%" + searchTerm.toLowerCase() + "%");
		}
		
		log.debug("Catalog ReportData Query: "+jpql);

		Query query = em.createNativeQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return getResultList(query);
	}

	/**
	 * This method saves data into the given table.
	 * 
	 * This does should not be called with views!
	 * 
	 * @param tableName
	 * @param columns
	 * @param lines
	 * @param isClearExisting
	 */
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
			String columnName = col.getName(); 
			if (col.isPrimaryKey()) {
				primaryKey = columnName;
				whereBuffer.append("\"" + col.getName() + "\"=:" + col.getName());
			}

			if (col.isAutoIncrement()) {
				continue;
			}

			// Insert Statement
			insertBuffer.append("\""+col.getName()+"\"");
			values.append(":" + col.getName());

			// Update Statement
			if (!col.isPrimaryKey()) {
				updateBuffer.append(col.getName() + "=:" + col.getName());
			}

			if (columns.size() - 1 != i) {
				insertBuffer.append(",");
				values.append(",");

				if (!col.isPrimaryKey())
					updateBuffer.append(",");
			}

			++i;
		}

		if (insertBuffer.toString().endsWith(",")) {
			insertBuffer.setCharAt(insertBuffer.length() - 1, ' ');
			values.setCharAt(values.length() - 1, ' ');
		}

		if (updateBuffer.toString().endsWith(",")) {
			updateBuffer.setCharAt(updateBuffer.length() - 1, ' ');
		}

		insertBuffer.append(") " + values + ")");

		if (primaryKey != null) {
			updateBuffer.append(" " + whereBuffer.toString());
		}

		// Document Line
		for (DocumentLine line : lines) {

			boolean isUpdate = !isClearExisting && primaryKey != null;

			if (isUpdate) {
				Value val = line.getValue(primaryKey);
				if (val != null && val.getValue() != null) {
					String existsQuery = "SELECT exists(select * from "
							+ tableName + " " + whereBuffer.toString() + ")";
					log.debug("#CheckExistsQuery: " + existsQuery);
					isUpdate = (Boolean) em.createNativeQuery(existsQuery)
							.setParameter(primaryKey, val.getValue())
							.getSingleResult();
				} else {
					logger.warn("#Report Table update row cannot be executed. "
							+ "Primary Key Field  '" + primaryKey
							+ "' is Null. Record: " + line);
					isUpdate = false;
				}
			}

			Query query = null;

			if (isUpdate) {
				log.debug("Exec DML: " + updateBuffer.toString());
				query = em.createNativeQuery(updateBuffer.toString());
			} else {
				log.debug("Exec DML: " + insertBuffer.toString());
				query = em.createNativeQuery(insertBuffer.toString());
			}

			// Set Values
			for (CatalogColumn col : columns) {
				if (col.isAutoIncrement()) {
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

	public List<Catalog> getViews() {
		List<Catalog> catalogs = new ArrayList<Catalog>();

		try {
			Connection conn = ((SessionImpl) (em.getDelegate()))
					.getJDBCContext().connection();
			ResultSet rs = null;
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, null, new String[] { "VIEW" });

			while (rs.next()) {
				Catalog catalog = new Catalog();
				String tableName = rs.getString("TABLE_NAME");
				catalog.setName(tableName);
				catalog.setDescription(tableName);
				catalog.setType(CatalogType.REPORTVIEW);
				catalog.setRecordCount(getCount(tableName));

				ResultSet colsRs = meta.getColumns(null, null, tableName, null);

				List<CatalogColumn> columns = new ArrayList<CatalogColumn>();
				while (colsRs.next()) {
					CatalogColumn column = new CatalogColumn();
					String name = colsRs.getString("COLUMN_NAME");
					column.setName(name);
					column.setLabel(name);

					String type = colsRs.getString("TYPE_NAME");
					column.setType(DBType.valueOf(type.toUpperCase()));

					int size = colsRs.getInt("COLUMN_SIZE");
					column.setSize(size);
					columns.add(column);
				}
				colsRs.close();

				catalog.getColumns().addAll(columns);
				catalogs.add(catalog);
			}
			rs.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return catalogs;
	}

	public boolean checkExists(String catalogName) {
		SessionImpl sessionImp = (SessionImpl) em.getDelegate();
		boolean exists = false;
		
		if(!catalogName.toLowerCase().startsWith("ext_")){
			catalogName = "EXT_"+catalogName;
		}
		
		try{
			DatabaseMetaData metadata = sessionImp.connection().getMetaData();
			ResultSet rs = metadata.getTables(null, null, catalogName.toLowerCase(), new String[]{"TABLE"});
			exists = rs.next();
			rs.close();
		}catch(Exception e){
			log.warn("Catalog '"+catalogName+"' does not exist: ["+e.getMessage()+"].");
			//Does not exist
		}
		return exists;
	}

}
