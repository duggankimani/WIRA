package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;

public class CatalogDaoImpl extends BaseDaoImpl{

	Logger log = Logger.getLogger(CatalogDaoImpl.class);
	
	public CatalogDaoImpl(EntityManager em) {
		super(em);
	}

	public List<CatalogModel> getCatalogs() {
		return getResultList(em.createQuery("FROM CatalogModel c where c.isActive=1"));
	}

	public void generateTable(Catalog model) {
		String drop = "DROP TABLE IF EXISTS EXT_"+model.getName()+";";
		log.debug("Exec DDL: "+drop);
		
		StringBuffer create = new StringBuffer("CREATE TABLE EXT_"+model.getName()+"(");
		int i=0;
		for(CatalogColumn col: model.getColumns()){
			create.append(col.getName());
			
			if(col.isAutoIncrement()){
				create.append(col.isAutoIncrement()?" serial":"");//POSTGRES
			}else{
				create.append(" " +col.getType().name());
				create.append((col.getSize()==null|| col.getSize()==0)?"" : "("+col.getSize()+")");
			}
			
			create.append(col.isPrimaryKey()? " PRIMARY KEY":"");
			create.append(col.isNullable()? " NULL":" NOT NULL");
			
			
			if(model.getColumns().size()-1!=i){
				create.append(",");
			}
			
			++i;
		}
		create.append(")");
		log.debug("Exec DDL: "+create);
		
		//DROP
		em.createNativeQuery(drop).executeUpdate();
		//CREATE
		em.createNativeQuery(create.toString()).executeUpdate();
		
	}

	public List<Object[]> getData(String tableName,String comma_Separated_fieldNames) {
		
		return getResultList(em.createNativeQuery("select "+comma_Separated_fieldNames+" from "+tableName));
	}

	public void save(String tableName, List<CatalogColumn> columns,
			List<DocumentLine> lines) {
		String delete = "DELETE FROM "+tableName;
		log.debug("Exec DML: "+delete);
		em.createNativeQuery(delete).executeUpdate();
		
		StringBuffer buffer = new StringBuffer("INSERT INTO "+tableName+" (");
		StringBuffer values = new StringBuffer(" VALUES (");
		int i=0;
		for(CatalogColumn col: columns){
			if(col.isAutoIncrement()){
				continue;
			}
			buffer.append(col.getName());
			values.append(":"+col.getName());
			
			if(columns.size()-1!=i){
				buffer.append(",");
				values.append(",");
			}
			
			++i;
		}
		if(buffer.toString().endsWith(",")){
			buffer.setCharAt(buffer.length()-1, ' ');
			values.setCharAt(values.length()-1, ' ');
		}
		
		buffer.append(") "+values+")");
		log.debug("Exec DML: "+buffer.toString());
		
		//Document Line
		for(DocumentLine line: lines){
			Query query = em.createNativeQuery(buffer.toString());
			for(CatalogColumn col: columns){
				if(col.isAutoIncrement()){
					continue;
				}
				Value val = line.getValue(col.getName());
				query.setParameter(col.getName(), val==null? null: val.getValue());
			}
			query.executeUpdate();
		}
	
		
	}

	public int getCount(String tableName) {
		
		Number number = getSingleResultOrNull(
				em.createNativeQuery("select count(*) from "+tableName));
		return number.intValue();
	}
}
