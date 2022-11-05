package com.optidpp.podservice.db;

import java.sql.*;
import javax.naming.*;
import javax.sql.*;

import org.apache.log4j.Logger;

import com.optidpp.zrfc.common.ConfigValue;
public class DBHandler
{
	private static final Logger logger = Logger.getLogger(DBHandler.class.getName());
	
	protected DBConnectionManager connMgr;
	protected Connection conn;  
	public  String dbName = "";
	 //"jdbc/ds"; 
	/** 
	 * DB Pool로부터 접속을 하나 얻어주는 메쏘드.
	 * @param dbName Database Name
	 */
	public Connection getConnection(String dbName) {
		
			
		DataSource pool = null;
		Context env = null;
		synchronized ( DBHandler.class ){ 
			if(pool == null){
		  		try {
		  			//if(ConfigValue.wasPoolUseYn.equals("Y")){
		  			//	env = (Context) new InitialContext();
		  			//}else{
		  				env = (Context) new InitialContext().lookup("java:comp/env");
		  			//}
				    pool = (DataSource) env.lookup(dbName);    
						    
				} catch (Exception e) {
					logger.error("DBHandler=>NamingException="+e );
				} finally{  
					if ( env != null ) {
						try{
							env.close();
						}catch(Exception e){
							logger.error("DBHandler=>NamingException="+e );
						}
					}
				}
			}
 
		}
		try {
			conn = pool.getConnection();
		} catch(Exception e){
			logger.error("DBHandler=>getConnection Error="+e );
		} 		
		return conn;
	}
	
	public Connection getConnection(){
		this.dbName = ConfigValue.wsJndiName;
		return this.getConnection(dbName);
	}

	
	public synchronized void release() throws Exception {
		if (conn == null)
			return;
		// The following is needed for some DB connection pool.
		boolean mode = true;
		try {
			mode = conn.getAutoCommit();
		} catch (Exception e) {
		}
		if (mode == false) {
			try {
				conn.rollback();
			} catch (Exception e) {
				logger.error("DBHandler=>conn.rollback Error="+e );
			}
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
				logger.error("DBHandler=>conn.setAutoCommit(true) Error="+e );
			}
		}
		try{
			freeDbConnection(dbName);
			conn = null;
	    }catch(Exception e){
	    	logger.error("DBHandler=>freeDbConnection Error="+e );
	    }
	
	}
	
	
	/**
	 * DB Pool에 접속을 돌려주는 메쏘드.
	 * @param dbName Database Name
	 */
	public void freeDbConnection(String dbName) {
		try{
			if(conn!=null && !conn.isClosed()) conn.close(); 
		}catch(Exception e){}
		
	
	}
	public void freeDbConnection() {
		try{
			if(conn!=null && !conn.isClosed()) conn.close(); 			
		}catch(Exception e){}
		
	} 

	

	

}