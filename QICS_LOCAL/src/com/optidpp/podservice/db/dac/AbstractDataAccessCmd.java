package com.optidpp.podservice.db.dac;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.optidpp.podservice.db.ConnectionResource;
import com.optidpp.podservice.db.DBHandler;
import com.optidpp.zrfc.common.ConfigValue;

public abstract class AbstractDataAccessCmd implements Serializable {

	private static final Logger logger = Logger.getLogger(AbstractDataAccessCmd.class.getName());

	protected DBHandler resource1;
	protected ConnectionResource resource;
	protected Connection conn;

	protected InitialContext ctx = null ;
	protected DataSource ds =null;


	public Connection getConnection (){

		try{
			if( ConfigValue.wasPoolUseYn != null && ConfigValue.wasPoolUseYn.equals("Y") ) {
				resource1 = new DBHandler();
				conn = resource1.getConnection();
			} else { // JDBC Pool
				resource = new ConnectionResource(this);
				conn = resource.getConnection(ConfigValue.dbConnectionPool);
			}

			return conn;
		}catch(Exception e){
			logger.debug(e.toString());
		}
		return null;
	}

	public Connection getConnection (String dbConnectionPool){

		try{
			// JDBC Pool ���
			resource = new ConnectionResource(this);
			conn = resource.getConnection(dbConnectionPool);

			return conn;
		}catch(Exception e){
			logger.debug(e.toString());
		}
		return null;
	}

	public abstract void appendSQL(String appendSQL)throws SQLException;

	public abstract void setQueryString(String sqlString)throws SQLException;

	public abstract ResultSet execute() throws SQLException;

	public abstract Statement getStatement();

	public abstract String getQueryString();

	public abstract ResultSet getResultSet();

	public abstract void release();
}
