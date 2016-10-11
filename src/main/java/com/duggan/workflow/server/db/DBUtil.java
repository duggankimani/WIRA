package com.duggan.workflow.server.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.IsDoc;

public class DBUtil {

	static Logger logger = Logger.getLogger(DBUtil.class);
	
	public static String getStringValue(final String sql){
		return getStringValue(sql, null);
	}
	
	/**
	 * 
	 * @param sql
	 * @param connectionName
	 * @return
	 */
	public static String getStringValue(final String sql, String connectionName){
		
		DBExecute<String> exec = new DBExecute<String>(connectionName) {
			
			@Override
			protected void setParameters() throws SQLException {
				
			}
			
			@Override
			protected String processResults(PreparedStatement pStmt, boolean hasResults)
					throws SQLException {
				
				if(hasResults){
					ResultSet rs = getResultSet();
					if(rs.next()){
						return rs.getString(1);
					}
				}
				return "";
			}
			
			@Override
			protected String getQueryString() {
				logger.info("DBUtil.getStringValue sql = "+sql);
				return sql;
			}
		};
		
		return exec.executeDbCall();
	}
	

	/**
	 * 
	 * @param sql
	 * @param connectionName
	 * @return
	 */
	public static List<Object[]> getValues(final String sql, String connectionName){
		
		DBExecute<List<Object[]>> exec = new DBExecute<List<Object[]>>(connectionName) {
			
			@Override
			protected void setParameters() throws SQLException {
				
			}
			
			@Override
			protected List<Object[]> processResults(PreparedStatement pStmt, boolean hasResults)
					throws SQLException {
				List<Object[]> rows = new ArrayList<Object[]>();

				if(hasResults){
					
					ResultSet rs = getResultSet();
					int colCount = rs.getMetaData().getColumnCount();
					while(rs.next()){
						
						//Create an Array of size colCount
						Object[] columnValues = new Object[colCount];
						for(int i=0; i<colCount; i++){
							//copy resultset column values
							columnValues[i] = rs.getObject(i+1);
						}
						rows.add(columnValues);
					}
					
				}
				return rows;
			}
			
			@Override
			protected String getQueryString() {
				logger.info("DBUtil.getStringValue sql = "+sql);
				return sql;
			}
		};
		
		return exec.executeDbCall();
	}
	
	/**
	 * 
	 * @param sql
	 * @param connectionName
	 * @param doc
	 */
	public static void mapValues(final String sql, String connectionName, Doc doc){
		mapValues(sql, connectionName, null, doc);
	}
	
	/**
	 * 
	 * @param sql
	 * @param connectionName
	 * @param gridName
	 * @param doc
	 */
	public static void mapValues(final String sql, String connectionName,String gridName, IsDoc doc){
		List<Object[]> rows = getValues(sql, connectionName);
		
		int idx = 7; //Select 
		int endIdx = sql.toLowerCase().indexOf("from");
		
		String cols = sql.substring(idx, endIdx).trim();
		String[] columns = cols.split(",");
		
		List<DocumentLine> lines = new ArrayList<DocumentLine>();
		Doc ctx = ((Doc)doc);
		Long id = (ctx instanceof Document) ? ((Document)ctx).getId() : ((HTask)ctx).getDocumentRef();
		if(gridName!=null){
			if(ctx.getDetails().containsKey(gridName)){
				lines = ctx.getDetails().get(gridName);
			}
		}
		
		for(Object[] row: rows){
			IsDoc line = gridName==null? ctx: new DocumentLine(gridName,null,id);
			
			Object value = null;
 			for(int col=0; col<row.length; col++){
				String colName = columns[col];
				colName = parse(colName);
				value = row[col];
				if(value instanceof String){
					line._s(colName, (String)value);
				}else if(value instanceof Boolean){
					line._s(colName, (Boolean)value);
				}
				else if(value instanceof Date){
					line._s(colName, (Date)value);
				}
				else if(value instanceof Number){

					if(value instanceof Double){
						line._s(colName, (Double)value);
					}
					else if(value instanceof Integer){
						line._s(colName, (Integer)value);
					}
					else if(value instanceof Long){
						line._s(colName, (Long)value);
					}
					else if(value instanceof BigInteger){
						line._s(colName, ((BigInteger)value).longValue());
					}
					else if(value instanceof BigDecimal){
						line._s(colName, ((BigDecimal)value).doubleValue());
					}
				}
				
			}
			
			if(line instanceof DocumentLine){
				//Is Line
				lines.add((DocumentLine)line);
			}
		}
		
		if(gridName!=null){
			ctx.setDetails(gridName, lines);
		}
		
	}

	private static String parse(String colName) {
		int startIdx = colName.lastIndexOf("\\s");
		if(startIdx==-1){
			return colName;
		}
		
		return colName.substring(startIdx, colName.length());
	}
}
