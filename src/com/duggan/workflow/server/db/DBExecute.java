package com.duggan.workflow.server.db;

import static java.lang.Thread.currentThread;
import static java.sql.Types.DATE;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DBExecute<T> {

	private static int cmdCount = 0;
	private static Logger log = LoggerFactory.getLogger( DB.class );
	PreparedStatement preparedStatement = null;
	ResultSet lastResultSet = null;
	int genVal = 0;

	String connectionName;
	boolean isJNDI=false;
	public DBExecute(String connectionName){
		this.connectionName = connectionName;
	}
	
	public T executeDbCall() {
		int currentCommandCount = cmdCount++;

		try {

			String queryString = getQueryString();
			log.debug( "DBCall [" + currentThread().getName()
					+ " " + currentCommandCount + "] Start: " + queryString);
			
			
			try {	
				
//				preparedStatement = DB.getConnection(connectionName).prepareStatement(
//						queryString, Statement.RETURN_GENERATED_KEYS);

				preparedStatement = DB.getConnection(connectionName).prepareStatement(
						queryString);
				
			} catch (Exception ex) {
				log.debug("Error connecting to database - ["
						+ currentCommandCount + "] " + queryString);
				
				throw new RuntimeException(ex);
				
			}

			try {
				setParameters();
			} catch (Exception ex) {
				log.debug("Error setting parameters - ["
						+ currentCommandCount + "] " + queryString);
				throw new RuntimeException(ex);
			}

			Date dtStart = new Date();
			Date dtEndExecute = new Date();

			boolean hasResults = false;
			try {
				String cmdTimoutStr = "5000";
				
				if (cmdTimoutStr != null && !cmdTimoutStr.isEmpty()) {
					preparedStatement.setQueryTimeout(Integer
							.parseInt(cmdTimoutStr));

				}

				hasResults = preparedStatement.execute();
				dtEndExecute = new Date();
				long elapsedMs = dtEndExecute.getTime() - dtStart.getTime();
				log.debug( "DBCall ["
						+ currentCommandCount + "]  DBExecution :  "
						+ queryString + " :  Took " + elapsedMs + " ms");

			} catch (Exception ex) {
				log.debug("Error executing command - ["
						+ currentCommandCount + "] " + queryString);
				throw new RuntimeException(ex);
			}

			T retVal = null;
			try {
				retVal = processResults(preparedStatement, hasResults);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.debug("Error processing  command results - ["
								+ currentCommandCount + "] " + queryString);
				throw new RuntimeException(
						"Unrecoverable server error occurred. See server log for details");
			}

			Date dtEnd = new Date();
			long elapsedMs = dtEnd.getTime() - dtEndExecute.getTime();

			// log.debug("DBCall Processing ["
			// +
			// currentCommandCount + "]  Processing Results :  " + queryString +
			// " :  Took " + elapsedMs + " ms");

			long totalElapsedTime = dtEnd.getTime() - dtStart.getTime();

			if (elapsedMs > 1000) {
				log.debug(
						"DBCall Execute and Processing [" + currentCommandCount
								+ "] Finish :  " + queryString + " :  Took "
								+ totalElapsedTime + " ms");
			}

			return retVal;
		} finally {
			try {
				preparedStatement.close();
			} catch (Exception ignored) {
			}
		}

	}

	protected abstract String getQueryString();

	protected abstract void setParameters() throws SQLException;

	protected abstract T processResults(PreparedStatement pStmt,
			boolean hasResults) throws SQLException;
	
	 // Resultset WRappers
	  protected void setBoolean(int paramValue, Boolean val) throws SQLException {
	    log.debug("DBCall [PARAM]" + paramValue + "=" + (val == null ? "<null>" : val));
	    if (val == null) {
	      preparedStatement.setNull(paramValue, Types.BOOLEAN);
	    }
	    else {
	      preparedStatement.setBoolean(paramValue, val);
	    }
	  }

	  protected void setInt(int paramValue, Integer val) throws SQLException {
	    if (val == null) {
	      preparedStatement.setNull(paramValue, Types.INTEGER);
	    }
	    else {
	      preparedStatement.setInt(paramValue, val);
	    }
	  }

	  protected void setDateTime(int paramValue, Date val) throws SQLException {
	    log.debug("DBCall [PARAM]" + paramValue + "="
	        + (val == null ? "<null>" : val.toString()));
	    if (val == null) {
	      preparedStatement.setNull(paramValue, Types.TIMESTAMP);
	    }
	    else {
	      preparedStatement.setTimestamp(paramValue, new Timestamp(val.getTime()));
	    }
	  }

	  /**
	   * Sets a parameter with a Date.
	   * 
	   * @param aParameterNumber the parameter to set (1 is the first index)
	   * @param aValue a value (can be null)
	   * @throws SQLException if there was a sql exception
	   */
	  protected void setDate(int aParameterNumber, Date aValue) throws SQLException {
	    if (aValue == null) {
	      log.debug("DBCall [PARAM]" + aParameterNumber + "=<null>");
	      preparedStatement.setNull(aParameterNumber, DATE);
	      return;
	    }
	    log.debug("DBCall [PARAM]" + aParameterNumber + "=" + aValue);
	    preparedStatement.setDate(aParameterNumber, new java.sql.Date(aValue.getTime()));
	  }
	  
	  protected void setBlob(int paramValue, InputStream content, long contentLength) throws SQLException {
	    log.debug("DBCall [PARAM]" + paramValue + "=blob(" + contentLength + ")");
	    if (content != null) {
	    	preparedStatement.setBinaryStream(paramValue, content, contentLength);
	    } else {
	    	preparedStatement.setNull(paramValue, Types.BLOB);
	    }
	  }

	  protected void setString(int paramValue, String val) throws SQLException {
	    log.debug("DBCall [PARAM]" + paramValue + "="
	        + (val == null ? "<null>" : val.toString()));
	    preparedStatement.setString(paramValue, val);

	  }
	  
	  protected void setLong(int paramValue, Long val) throws SQLException {
	    log.debug("DBCall [PARAM]" + paramValue + "=" + (val == null ? "<null>" : val));
	    if (val == null) {
	      preparedStatement.setNull(paramValue, Types.INTEGER);
	    }
	    else {
	      preparedStatement.setLong(paramValue, val);
	    }
	  }

	  protected void setDouble(int paramValue, Double val) throws SQLException {
	    log.debug("DBCall [PARAM]" + paramValue + "=" + (val == null ? "<null>" : val));
	    if (val == null) {
	      preparedStatement.setNull(paramValue, Types.DOUBLE);
	    }
	    else {
	      preparedStatement.setDouble(paramValue, val);
	    }
	  }

	  protected ResultSet getResultSet() throws SQLException {
		    if (lastResultSet != null) {
		      if (!lastResultSet.isClosed()) {
		        lastResultSet.close();
		      }

		      lastResultSet = null;
		    }

		    lastResultSet = preparedStatement.getResultSet();
		    return lastResultSet;
		  }

		  protected boolean getMoreResults() throws SQLException {
		    return preparedStatement.getMoreResults();
		  }


}
