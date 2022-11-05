package com.optidpp.podservice.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

public class DBConnectionManager {

	static private DBConnectionManager instance;       // The single instance
	static private int clients;
	private static final Logger logger = Logger.getLogger("dbconn");

	private Vector drivers = new Vector();
	private Hashtable pools = new Hashtable();

	/**
	 * This inner class represents a connection pool. It creates new
	 * connections on demand, up to a max number if specified.
	 * It also makes sure a connection is still open before it is
	 * returned to a client.
	 */
	class DBConnectionPool {
		private int checkedOut;
		private Vector freeConnections = new Vector();
		private int maxConn;
		private String DBName;
		private String poolName;
		private String password;
		private String URL;
		private String user;

		private Vector lastAccessed = new Vector();
		private long 	 timeOutInterval;

		/**
		 * Creates new connection pool.
		 *
		 * @param name The pool name
		 * @param URL The JDBC URL for the database
		 * @param user The database user, or null
		 * @param password The database user password, or null
		 * @param maxConn The maximal number of connections, or 0
		 *   for no limit
		 */
		public DBConnectionPool(String DBName, String poolName, String URL, String user, String password,
				int maxConn) {
			this.DBName = DBName;
			this.poolName = poolName;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;

			this.timeOutInterval = 1000 * 60 *  30;   // default timeout 30 min

		}

		/**
		 * Creates new connection pool.
		 *
		 * @param name The pool name
		 * @param URL The JDBC URL for the database
		 * @param user The database user, or null
		 * @param password The database user password, or null
		 * @param maxConn The maximal number of connections, or 0
		 *   for no limit
		 */
		public DBConnectionPool(String DBName, String poolName, String URL, String user, String password,
				int maxConn, int initConn) {
			this.DBName = DBName;
			this.poolName = poolName;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;

			this.timeOutInterval = 1000 * 60 *  30;   // default timeout 30 min

			for (int i = 0; i < initConn; i++) {
				freeConnections.addElement(newConnection());
				lastAccessed.addElement(new Long(System.currentTimeMillis()));
			}
		}


		public DBConnectionPool(String DBName, String poolName, String URL, String user, String password,
				int maxConn, int initConn, long timeOut) {
			this(DBName, poolName, URL, user, password, maxConn, initConn);
			this.timeOutInterval = timeOut;
		}


		/**
		 * Checks in a connection to the pool. Notify other Threads that
		 * may be waiting for a connection.
		 *
		 * @param con The connection to check in
		 */
		public synchronized void freeConnection(Connection con) {
			// Put the connection at the end of the Vector
			freeConnections.addElement(con);
			lastAccessed.addElement(new Long(System.currentTimeMillis()));
			checkedOut--;
			notifyAll();
			//logger.info(" freeConnection: Connection Count[" + checkedOut + "] Clients["+ clients + "] maxConn[" + maxConn + "] pool name[" + poolName + "] freeConnections.size[" + freeConnections.size() + "] con[" + con + "] pool name[" + poolName + "]");
		}

		/**
		 * Checks out a connection from the pool. If no free connection
		 * is available, a new connection is created unless the max
		 * number of connections has been reached. If a free connection
		 * has been closed by the database, it's removed from the pool
		 * and this method is called again recursively.
		 */
		public synchronized Connection getConnection() {
			Connection con = null;
			boolean validConn = true;// checkedOut 값을 한번만 증가시키기 위한 변수.
			Long accessTime;
			if (freeConnections.size() > 0) {
				// Pick the first Connection in the Vector
				// to get round-robin usage
				con = (Connection) freeConnections.firstElement();
				accessTime = (Long)lastAccessed.firstElement();

				freeConnections.removeElementAt(0);
				lastAccessed.removeElementAt(0);
				try {
					if (con.isClosed()) {
						//logger.info("Removed bad connection from " + DBName);
						validConn = false;
						// Try again recursively
						con = getConnection();
					} else if(isTimeOuted(accessTime)) {
						//logger.info("Removed TimeOuted connection from " + DBName);
						con.close();
						validConn = false;
						con = getConnection();
					}
				}catch (SQLException e) {
					logger.error("Removed bad connection from " + DBName);
					// Try again recursively
					validConn = false;
					con = getConnection();
				}
			}
			else if (maxConn == 0 || checkedOut < maxConn) {
				con = newConnection();
			}
			if ((con != null) && (validConn == true)) {
				checkedOut++;
			}
			//logger.info(" getConnection: Connection Count[" + checkedOut + "] Clients["+ clients + "] maxConn[" + maxConn + "] pool name[" + poolName + "] freeConnections.size[" + freeConnections.size() + "] con[" + con + "]");
			return con;
		}

		/**
		 * Checks out a connection from the pool. If no free connection
		 * is available, a new connection is created unless the max
		 * number of connections has been reached. If a free connection
		 * has been closed by the database, it's removed from the pool
		 * and this method is called again recursively.
		 * <P>
		 * If no connection is available and the max number has been
		 * reached, this method waits the specified time for one to be
		 * checked in.
		 *
		 * @param timeout The timeout value in milliseconds
		 */
		public synchronized Connection getConnection(long timeout) {
			long startTime = new Date().getTime();
			Connection con;
			while ((con = getConnection()) == null) {
				try {
					wait(timeout);
				}catch (InterruptedException e) {}
				if ((new Date().getTime() - startTime) >= timeout) {
					// Timeout has expired
					return null;
				}
			}
			return con;
		}

		/**
		 * Closes all available connections.	-- DBConnectionPool
		 */

		public synchronized void release() {
			Enumeration allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements()) {
				Connection con = (Connection) allConnections.nextElement();
				try {
					con.close();
					//logger.info("Closed connection for pool " + poolName);
				}catch (SQLException e) {
					logger.error("Can't close connection for pool " + poolName + " : " + e.toString());
				}
			}
			freeConnections.removeAllElements();
			lastAccessed.removeAllElements();
			//logger.info(" 모든 connection 제거 : Connection Count[" + checkedOut + "] Clients["+ clients + "] maxConn[" + maxConn + "] pool name[" + poolName + "] freeConnections.size[" + freeConnections.size() + "]");

		}

		/**
		 * Creates a new connection, using a userid and password
		 * if specified.
		 */
		private Connection newConnection() {
			Connection con = null;
			try {

				if (user == null) {
					con = DriverManager.getConnection(URL);
				}
				else {
					con = DriverManager.getConnection(URL, user, password);
				}
				//logger.info("Created a new connection in pool " + poolName);
			}catch (SQLException e) {
				logger.error("Connection 생성실패 --- Connection Count[" + checkedOut + "] --- maxConn[" + maxConn + "] --- pool name[" + poolName + "] freeConnections.size[" + freeConnections.size() + "] ---  con[" + con + "]  --- pool name[" + poolName + "] --- 에러메세지[" + e.getMessage() + "]");
				logger.error("Can't create a new connection for " + URL + " : " + e.toString());
				return null;
			}
			//logger.info("Connection생성 --- Connection Count[" + checkedOut + "] --- maxConn[" + maxConn + "] --- pool name[" + poolName + "] freeConnections.size[" + freeConnections.size() + "] ---  con[" + con + "]  --- pool name[" + poolName + "]");
			return con;
		}

		private boolean isTimeOuted(Long accessTime) {
			long interval = System.currentTimeMillis() - accessTime.longValue();
			if(interval > timeOutInterval)
			{
				//logger.info("timeoutInterval[" + timeOutInterval + "] interval[" + interval + "]");
			}

			return interval > timeOutInterval;

			//return ((System.currentTimeMillis() - accessTime.longValue()) > timeOutInterval);
		}
	}

	/**
	 * A private constructor since this is a Singleton
	 */
	private DBConnectionManager() {
		init();
	}
	/**
	 * Closes all open connections and deregisters all drivers.
	 */
	public synchronized void release() {
		// Wait until called by the last client
		//logger.info(" Mgr.release(): Clients["+ clients + "] ");
		if (--clients != 0) {
			return;
		}

		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				//logger.info("Deregistered JDBC driver " + driver.getClass().getName());
			}catch (SQLException e) {
				logger.error("Can't deregister JDBC driver: " + driver.getClass().getName() + " : " + e.toString());
			}
		}
	}


	/*
  private synchronized void all_release() {
    // Wait until called by the last client
	  if (--clients != 0) {
	  	return;
	  }

	   Enumeration allPools = pools.elements();
	   while (allPools.hasMoreElements()) {
	       DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
	       pool.release();
	   }
	   Enumeration allDrivers = drivers.elements();
	   while (allDrivers.hasMoreElements()) {
	       Driver driver = (Driver) allDrivers.nextElement();
		   try {
		       DriverManager.deregisterDriver(driver);
		       logger.info("Deregistered JDBC driver " + driver.getClass().getName());
		       this.getClass() + " getConnection() in "+poolName);
	       }catch (SQLException e) {
	           logger.error("Can't deregister JDBC driver: " + driver.getClass().getName()+" : "+e.toString());
	       }
       }
  }
	 */
	/**
	 * Creates instances of DBConnectionPool based on the properties.
	 * A DBConnectionPool can be defined with the following properties:
	 * <PRE>
	 * &lt;poolname&gt;.url         The JDBC URL for the database
	 * &lt;poolname&gt;.user        A database user (optional)
	 * &lt;poolname&gt;.password    A database user password (if user specified)
	 * &lt;poolname&gt;.maxconn     The maximal number of connections (optional)
	 * </PRE>
	 *
	 * @param props The connection pool properties
	 */
	private void createPools(Properties props) {

		Enumeration propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			name = name.trim();

			String poolName =name.substring(0,name.indexOf("."));

			//	String poolName = props.getProperty(poolName + ".name").trim();
			if (name.endsWith(".url")) {
				String url = props.getProperty(poolName + ".url");
				String DBName = "";

				if (url == null) {
					//logger.info("No URL specified for " + poolName);
					continue;
				}else{
					String driver = props.getProperty(poolName + ".driver");
					driver=driver.substring(driver.lastIndexOf(".")+1);
					//System.out.println("driver = " + driver);
					if("SQLServerDriver".equals(driver)){
						DBName = url.substring(url.lastIndexOf("=")+1).replaceAll(";", "");

					}else if("OracleDriver".equals(driver)){
						DBName = url.substring(url.lastIndexOf(":")+1);
					}else{

					}
					//System.out.println("DBName = " + DBName);
				}

				String user = props.getProperty(poolName + ".user");
				String password = props.getProperty(poolName + ".password");
				String maxconn = props.getProperty(poolName + ".maxconn");
				//System.out.println("maxconn = " + maxconn);
				int max;
				try {
					max = Integer.valueOf(maxconn).intValue();
				}catch (NumberFormatException e) {
					logger.error("Invalid maxconn value " + maxconn + " for " + poolName);
					max = 0;
				}

				String initconn = props.getProperty(poolName + ".initconn");
				int init;
				try {
					init = Integer.valueOf(initconn).intValue();
				}catch (NumberFormatException e) {
					logger.error("Invalid initconn value " + initconn + " for " + poolName);
					init = 0;
				}

				String timeout = props.getProperty(poolName + ".timeout", "1800000");  // default 30 min
				long tout;
				try {
					tout = Long.valueOf(timeout).longValue();
				}catch (NumberFormatException e) {
					logger.error("Invalid timeout value " + timeout + " for " + poolName);
					tout = 1800000;
				}

				DBConnectionPool pool =
						new DBConnectionPool(DBName, poolName, url, user, password, max, init, tout);

				pools.put(poolName, pool);
				//logger.info("Initialized pool " + poolName);
			}
		}
	}

	/**
	 * Returns a connection to the named pool.
	 *
	 * @param name The pool name as defined in the properties file
	 * @param con The Connection
	 */
	public void freeConnection(String poolName, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(poolName);
		if (pool != null) {
			//logger.info(this.getClass() + " freeConnection() in "+poolName);
			pool.freeConnection(con);
		}
	}
	/**
	 * Returns an open connection. If no one is available, and the max
	 * number of connections has not been reached, a new connection is
	 * created.
	 *
	 * @param name The pool name as defined in the properties file
	 * @return Connection The connection or null
	 */
	public Connection getConnection(String poolName) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(poolName);
		if (pool != null) {
			//logger.info(this.getClass() + " getConnection() in "+poolName);
			return pool.getConnection();
		}
		return null;
	}
	/**
	 * Returns an open connection. If no one is available, and the max
	 * number of connections has not been reached, a new connection is
	 * created. If the max number has been reached, waits until one
	 * is available or the specified time has elapsed.
	 *
	 * @param name The pool name as defined in the properties file
	 * @param time The number of milliseconds to wait
	 * @return Connection The connection or null
	 */
	public Connection getConnection(String poolName, long time) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(poolName);
		if (pool != null) {

			return pool.getConnection(time);
		}
		return null;
	}
	/**
	 * Returns the single instance, creating one if it's the
	 * first time this method is called.
	 *
	 * @return DBConnectionManager The single instance.
	 */
	static synchronized public DBConnectionManager getInstance() {
		if (instance == null) {
			//common.Logger.info.println(this.getClass() + " getInstance() ");
			instance = new DBConnectionManager();
		}
		clients++;
		return instance;
	}
	/**
	 * Loads properties and initializes the instance with its values.
	 */
	private void init() {
		InputStream is = getClass().getResourceAsStream("/otherDBContext.properties");  //$$
		Properties dbProps = new Properties();
		try {
			dbProps.load(is);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Can't read the properties file. " +
					"Make sure db.properties is in the CLASSPATH");
			return;
		}

		loadDrivers(dbProps);
		createPools(dbProps);
	}
	/**
	 * Loads and registers all JDBC drivers. This is done by the
	 * DBConnectionManager, as opposed to the DBConnectionPool,
	 * since many pools may share the same driver.
	 *
	 * @param props The connection pool properties
	 */
	private void loadDrivers(Properties props) {
		Enumeration propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".name")) {
				String poolName =name.substring(0,name.indexOf("."));
				String driverClasses = props.getProperty(poolName + ".driver");
				//String driverClasses = props.getProperty("drivers");
				StringTokenizer st = new StringTokenizer(driverClasses);
				while (st.hasMoreElements()) {
					String driverClassName = st.nextToken().trim();
					try {
						Driver driver = (Driver)
								Class.forName(driverClassName).newInstance();
						DriverManager.registerDriver(driver);
						drivers.addElement(driver);
						//logger.info("Registered JDBC driver " + driverClassName);
					}catch (Exception e) {
						//logger.info("Can't register JDBC driver: " +
						//		driverClassName + ", Exception: " + e);
					}
				}
			}
		}

	}
}