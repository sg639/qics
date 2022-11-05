package com.optidpp.podservice.db;

import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ConnectionResource{

	private static final Logger logger = Logger.getLogger("dbconn");

	private boolean DEBUG_MODE = false;
	private String DEFAULT_POOLNAME = "pool1";    // default db name
	private long GET_CONNECTION_TIMEOUT = 2000;    // wait only 2 seconds
	private long infoNING_MAX_ELAPSED_TIME = 3000; // avaiable connection in the pool

	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd/HHmmss");
	private DBConnectionManager manager = null;
	private Connection conn = null;
	private String poolName = DEFAULT_POOLNAME;
	private String caller = "unknown";
	private long starttime = 0;

	private static int used_conn_count = 0;
	private static Object lock = new Object();

	// detault constructor
	public ConnectionResource(){
		//init();
		manager = DBConnectionManager.getInstance();
	}

	// For debugging, get the caller object reference
	public ConnectionResource(Object caller_obj){
		this();
		if ( caller_obj != null )
			caller = caller_obj.getClass().getName();
	}

	public Connection getConnection()  throws Exception{
		return getConnection(poolName);
	}

	public Connection getConnection(String poolName)  throws Exception{

		if ( conn != null ) throw new Exception ("You must release the connection first to get connection again !!");
		this.poolName = poolName;
		// CONNECTION_TIMEOUT is very important factor for performance tuning
		conn = manager.getConnection(poolName, GET_CONNECTION_TIMEOUT);
		synchronized( lock ) { ++used_conn_count; }
		starttime = System.currentTimeMillis();
		return conn;
	}

	// you don't have to get "connection reference" as parameter,
	// because we already have the referece as the privae member variable.
	public synchronized void release() throws Exception {
		if ( conn == null ) return;
		// The following is needed for some DB connection pool.
		boolean mode = true;
		try{
			mode = conn.getAutoCommit();
		}catch(Exception e){}
		if ( mode == false ) {
			try{conn.rollback();}catch(Exception e){}
			try{conn.setAutoCommit(true);}catch(Exception e){}
		}
		manager.freeConnection(poolName, conn);
		conn = null;
		int count = 0;
		synchronized( lock ) { count = --used_conn_count; }

		long endtime = System.currentTimeMillis();
		if ( (endtime-starttime) > infoNING_MAX_ELAPSED_TIME ) {
			logger.info("POOL:infoNING:" + count +":(" + (endtime-starttime) + "):" +"\t");
		}
	}
	// finalize() method will be called when JVM's GC time.
	public void finalize(){
		// if "conn" is not null, this means developer did not release the "conn".
		if ( conn != null ) {
			logger.info("POOL:ERROR connection was not released:" +	used_conn_count + ":\t");
			try{
				release();
			}catch(Exception e){
				logger.info("POOL:infoNING:" + "Exception in finalize");
			}
		}
	}

	private void init() {
		InputStream is = getClass().getResourceAsStream("/otherDBContext.properties");  //$$
		Properties props = new Properties();
		try {
			props.load(is);
		}
		catch (Exception e) {
			logger.error("Can't read the properties file. " +
					"Make sure db.properties is in the CLASSPATH");
			return;
		}

		Enumeration propNames = props.propertyNames();
		poolName = props.getProperty("poolName");
	}
}