package com.optidpp.podservice.db.dac;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleCallableStatement;

import org.apache.log4j.Logger;


public class DataAccessCmd extends AbstractDataAccessCmd implements Serializable{
    
    private static final Logger logger = Logger.getLogger(AbstractDataAccessCmd.class.getName());
    
    protected CallableStatement callStmt  		= null;
    protected OracleCallableStatement  tstmt 	= null;
    protected PreparedStatement preStmt   		= null;
    protected Statement stmt   			  		= null;
	protected String queryString          		= null;
	protected ResultSet rset              		= null;
	protected StringBuffer queryBuffer    		= new StringBuffer();

	public DataAccessCmd()throws Exception{
	    super();
	    try{
	        this.queryString = "";
	    }catch(Exception e){
	        logger.debug(e.toString());
	        throw new Exception(e.getMessage());
	    }		
	}

	public DataAccessCmd(String queryString)throws SQLException{
	    super();
	    try{
		    this.queryString = queryString;
			appendSQL(this.queryString);
		}catch(SQLException e){
		    logger.debug(e.toString());
		    throw new SQLException(e.getMessage());
		}		
	}

	public void appendSQL(String appendString)throws SQLException{
	    queryBuffer.append(appendString);
	}	

	public  ResultSet execute()throws SQLException{
		try{
			if(stmt == null){
			    stmt = getConnection().prepareStatement(queryBuffer.toString());
			}
			rset = preStmt.executeQuery();
			return rset;
		}catch(SQLException e){
			release();
			logger.debug(e.toString());
			throw new SQLException(e.getMessage());
		}
	}

	public  Statement getPreStatement() {
		try {
		    if(preStmt == null){
			    preStmt = getConnection().prepareStatement(queryBuffer.toString());
		    }
		}catch(Exception e){
		    logger.debug(e.toString());
		}
		return preStmt;
	}
	
	public  Statement getStatement() {
		try {
		    if(stmt == null){
			    stmt = getConnection().createStatement();
		    }
		}catch(Exception e){
		    logger.debug(e.toString());
		}
		return stmt;
	}

	public void setQueryString(String queryStringing)throws SQLException{
		this.queryString = queryStringing;
		queryBuffer = new StringBuffer(queryStringing);
	}
	
	public  String getQueryString(){
		queryString = queryBuffer.toString();
		return queryString;
	}

	public  ResultSet getResultSet(){
		return rset;
	}
	
	public void release(){
		if ( preStmt != null ){ 
		    try{
		        preStmt.close();
		        preStmt = null;
		    }catch(Exception e){
		        logger.debug("preStmt:"+e.toString());
		    }  
		}

		if ( stmt != null ){ 
		    try{
		        stmt.close();
		        stmt = null;
		    }catch(Exception e){
		        logger.debug("stmt:"+e.toString());
		    }  
		}
		
		if ( tstmt != null ){ 
		    try{
		    	tstmt.close();
		    	tstmt = null;
		    }catch(Exception e){
		        logger.debug("tstmt:"+e.toString());
		    }  
		}
		
		if ( callStmt != null ){ 
		    try{
		        callStmt.close();
		        callStmt = null;
		    }catch(Exception e){
		        logger.debug("callStmt:"+e.toString());
		    }  
		}

		if ( rset != null ){ 
		    try{
		        rset.close();
		        rset = null;
		    }catch(Exception e){
		        logger.debug("rset:"+e.toString());
		    }
		}

		if ( conn != null ){
		    try{
				conn.close();
				conn = null;
		    }catch(Exception e){
		        logger.debug("conn:"+e.toString());
		    }	
		}	
		
		if( resource != null ) {
		    try{
				resource.release();
				resource = null;
		    }catch(Exception e){
		        logger.debug("resource:"+e.toString());
		    }	
		}	
	}

	public void connExceptrelease(){
		if ( preStmt != null ){ 
		    try{
		        preStmt.close();
		        preStmt = null;
		    }catch(Exception e){
		        logger.debug(e.toString());
		    }  
		}

		if ( stmt != null ){ 
		    try{
		        stmt.close();
		        stmt = null;
		    }catch(Exception e){
		        logger.debug(e.toString());
		    }  
		}
		
		if ( tstmt != null ){ 
		    try{
		    	tstmt.close();
		    	tstmt = null;
		    }catch(Exception e){
		        logger.debug(e.toString());
		    }  
		}
		
		if ( callStmt != null ){ 
		    try{
		        callStmt.close();
		        callStmt = null;
		    }catch(Exception e){
		        logger.debug(e.toString());
		    }  
		}

		if ( rset != null ){ 
		    try{
		        rset.close();
		        rset = null;
		    }catch(Exception e){
		        logger.debug(e.toString());
		    }
		}		
	}
}
