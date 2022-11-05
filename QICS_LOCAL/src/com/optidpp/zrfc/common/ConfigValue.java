package com.optidpp.zrfc.common;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigValue {
	private static final Logger logger 			= null;

	private static InputStream is = null;

	public static String defaultHost 	  		= null;	//DOMAIN

	public static String useSAP		= "Y";
	public static String sapClientNum = ""; //sapClientNum
	public static String sapUsername = ""; //sapUsername
	public static String sapPassword = ""; //sapPassword
	public static String sapLang = ""; //sapLang
	public static String sapAshost = ""; //sapAshost
	public static String sapSysnr = ""; //sapSysnr
	public static String sapSid = ""; //sapSid
	public static String sapGroup = ""; //sapGroup
	public static String sapMaxConn = ""; //sapMaxConn
	public static String sapMshost="";//sapMshost
	public static String sapR3name="";//sapR3name

	public static String wasPoolUseYn			= "N"; //Was Pool Use Y/N ( Use:Y, UnUse(JDBC Pool Use):N ).
	public static String dbConnectionPool		= null; //DB Connection Pool name.
	public static String dbJdbcConnectionPool	= null; //DB JDBC Connection Pool name.
	public static String wsJndiName				= "jdbc/oracle"; //JNDI

	//DPP 정보
	public static String dppBackupRootPath = ""; //dppBackupRootPath
	public static String importXMLPath = ""; //dppBackupRootPath
	public static String psRootPath = ""; //dppBackupRootPath
	public static String importTempPath = ""; //dppBackupRootPath
	public static String psBgRootPath = ""; //dppBackupRootPath
	public static String bgRootPath = ""; //dppBackupRootPath
	public static String padRootPath = ""; //dppBackupRootPath

	public static String imageMagicKPath = ""; //imageMagicKPath
	public static String postfix_thumbnail_name = ""; //postfix_thumbnail_name
	public static int thumbnail_width = 700; //thumbnail_width
	public static int thumbnail_height = 900; //thumbnail_height
	public static int thumbnail_quality = 100; //thumbnail_quality

	public static String filePadPath		= "";
	public static String filePsPath		= "";
	public static String fileExportPath		= "";
	public static String fileFilesPath		= "";

	public static String messengerURL		= "";
	public static String isDev = "";
	public static String devMessSabun = "";

	public static String devCfrmNo1 = "";
	public static String devCfrmNo2 = "";
	public static String devCfrmNo3 = "";
	public static String devCfrmDpt = "";
	public static String devCapp = "";


	public ConfigValue() {
		if(is == null){
			is = getClass().getResourceAsStream("/system.properties");
		}
		init(this);
	}

	public ConfigValue(Object caller){
		this();
		//추�?구현
	}

	public final static synchronized void init(Object caller){
		Logger logger = null;
		if ( caller != null ){
			logger = Logger.getLogger(caller.getClass().getName());
		}else{
			logger = Logger.getLogger(ConfigValue.class);
		}
		if(defaultHost == null){
			Properties props = new Properties();
			try {
				if(is != null){
					props.load(is);
					defaultHost 	    	= props.getProperty("defaultHost","localhost");			//DOMAIN

					useSAP = props.getProperty("useSAP", "Y");

					sapClientNum = props.getProperty("sapClientNum", "");
					sapUsername = props.getProperty("sapUsername", "");
					sapPassword = props.getProperty("sapPassword", "");
					sapLang = props.getProperty("sapLang", "");
					sapAshost = props.getProperty("sapAshost", "");
					sapSysnr = props.getProperty("sapSysnr", "");
					sapSid = props.getProperty("sapSid", "");
					sapGroup = props.getProperty("sapGroup", "");
					sapMaxConn = props.getProperty("sapMaxConn", "");
					sapMshost = props.getProperty("sapMshost", "");
					sapR3name = props.getProperty("sapR3name", "");

					wasPoolUseYn		         = props.getProperty("wasPoolUseYn","N");
					wsJndiName			= props.getProperty("wsJndiName","jdbc/mssql");
					dbConnectionPool	     = props.getProperty("dbConnectionPool","pool1");
					dbJdbcConnectionPool = props.getProperty("dbJdbcConnectionPool","pool1");

					importXMLPath = props.getProperty("importXMLPath", "");
					bgRootPath = props.getProperty("bgRootPath", "");
					psBgRootPath = props.getProperty("psBgRootPath", "");
					psRootPath = props.getProperty("psRootPath", "");
					importTempPath = props.getProperty("importTempPath", "");
					padRootPath = props.getProperty("padRootPath", "");

					imageMagicKPath = props.getProperty("imageMagicKPath", "");

					fileFilesPath = props.getProperty("fileFilesPath", "");

					postfix_thumbnail_name = props.getProperty("POSTFIX_THUMBNAIL_NAME", "");
					thumbnail_width = Integer.parseInt(props.getProperty("THUMBNAIL_WIDTH", "700"));
					thumbnail_height = Integer.parseInt(props.getProperty("THUMBNAIL_HEIGHT", "900"));
					thumbnail_quality = Integer.parseInt(props.getProperty("THUMBNAIL_QUALITY", "100"));

					messengerURL = props.getProperty("messengerURL", "");
					isDev = props.getProperty("isDev", "Y");
					devMessSabun = props.getProperty("devMessSabun", "e307224");

					devCfrmNo1 =props.getProperty("C_FRM_NO1", "");
					devCfrmNo2 =props.getProperty("C_FRM_NO2", "");
					devCfrmNo3=props.getProperty("C_FRM_NO3", "");
					devCfrmDpt =props.getProperty("C_FRM_DPT", "");
					devCapp =props.getProperty("C_APP", "");

				}
			}catch (Exception e) {
				logger.error("Can't read the properties file. ");
				return;
			}
		}
	}
}