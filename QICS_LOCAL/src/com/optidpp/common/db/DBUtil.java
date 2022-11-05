package com.optidpp.common.db;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;

import com.optidpp.podservice.common.util.StringUtil;
import com.optidpp.podservice.db.ConnectionResource;
import com.optidpp.podservice.db.DBErrorMsg;
import com.optidpp.podservice.db.dac.CommonDAC;
import com.optidpp.podservice.db.dto.CommonCollect;


public class DBUtil extends CommonDAC {
	private static final Logger logger = Logger.getLogger(DBUtil.class.getName());

	public DBUtil() throws Exception {
		super();
	}
	/**
	 *   매개변수로 받은 SQL문을 실행시켜준다.
	 *
	 */
	public String execSQL(String sqlstr,String poolName) {
		String result = "NO";

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
		} catch(Exception e) {
			logger.error(e.toString());
			return DBErrorMsg.getErrMsg(e.toString());
		} finally {
			try{
				release();
			}catch(Exception e){
				logger.error(e.toString());
				return e.toString();
			}
		}
		return result;
	}

	/**
	 *   매개변수로 받은 복수개의 SQL문을 실행시켜준다.
	 *
	 */
	public void execSQL(String[] sqlstr,String poolName) {

		if (sqlstr == null) return;
		else if (sqlstr.length == 1)
		{
			execSQL(sqlstr[0],poolName);
			return;
		}

		String tmpsqlstr = "";

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);

				for (int i = 0; i < sqlstr.length; i ++)
				{
					tmpsqlstr = sqlstr[i];
					logger.debug(tmpsqlstr);
					stmt.execute(tmpsqlstr);
				}

				conn.commit();
			}
		} catch(Exception e) {
			logger.error(e.toString());
		} finally {
			release();
		}
	}

	/**
	 *   매개변수로 받은 SQL문을 실행시켜준다.
	 *
	 */
	public int execBatchSQL(String sqlstr,String poolName) {
		int result = -1;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			synchronized (conn) {
				stmt= conn.createStatement();
				logger.debug(sqlstr);
				result = stmt.executeUpdate(sqlstr);
			}
		} catch(Exception e) {
			logger.error(DBErrorMsg.getErrMsg(e.toString()));
			return -1;
		} finally {
			try{
				release();
			}catch(Exception e){
				logger.error(e.toString());
				return -1;
			}
		}
		return result;
	}

	/**
	 *   매개변수로 받은 복수개의 SQL문을 실행시켜준다.
	 *
	 */
	public int execBatchSQL(String[] sqlstr,String poolName) {
		int successCount = 0;

		if (sqlstr == null) return successCount;
		else if (sqlstr.length == 1)
		{
			return execBatchSQL(sqlstr[0],poolName);
		}

		String tmpsqlstr = "";

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);

				for (int i = 0; i < sqlstr.length; i ++)
				{
					if( sqlstr[i]!=null && !"".equals(sqlstr[i])) {
						logger.debug(i+".sql="+sqlstr[i]);
						stmt.addBatch(sqlstr[i]);
					}
				}
				int[] result = stmt.executeBatch();

				conn.commit();

				for (int rtn : result) {
					successCount += rtn;
				}

			}
		} catch(Exception e) {
			logger.error(e.toString());
			try{
				successCount = 0;
				conn.rollback();
			}catch(Exception sqlEx){
				logger.error(sqlEx.toString());
			}
		} finally {
			release();
		}

		return successCount;
	}

	/**
	 *   매개변수로 받은 복수개의 SQL문을 실행시켜준다.
	 *
	 */
	public int execBatchSQL(LinkedHashMap<Integer, String> sqlMap,String poolName) {
		int successCount = 0;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);

				for (Iterator<String> sqlit = sqlMap.values().iterator(); sqlit.hasNext();) {
					String sql = sqlit.next();
					if( sql!=null && !"".equals(sql) ) {
						logger.debug("sql="+sql);
						stmt.addBatch(sql);
					}
				}

				int[] result = stmt.executeBatch();

				conn.commit();

				for (int rtn : result) {
					successCount += rtn;
				}
			}
		} catch(Exception e) {
			logger.error(e.toString());
			try{
				successCount = 0;
				conn.rollback();
			}catch(Exception sqlEx){
				logger.error(sqlEx.toString());
			}
		} finally {
			release();
		}

		return successCount;
	}
	/**
	 *   데이터베이스 테이블로부터 첫 레코드의 첫 컬럼의 값을 되돌려준다.
	 *   비록 여러 개의 컬럼을 명시한다고 하더라도 첫번째 컬럼의 값만 되돌려준다.
	 *   여러 컬럼의 값 혹은 여러 레코드의 여러 컬럼의 값을 원한다면
	 *   getTableData()를 이용하면 된다.
	 */
	public String getTableData1x1(String sql,String poolName) {

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			synchronized (conn) {
				stmt= conn.createStatement();

				logger.debug(sql);
				rset = stmt.executeQuery(sql);

				if (!rset.next()) return null;
				else return StringUtil.getReplaceStr(rset.getString(1));
			}

		} catch(Exception e) {
			logger.error(e.toString()+"\n"+sql);
			return null;
		} finally {
			release();
		}
	}

	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 */
	public String[][] getTableData(String sql, int colLength,String poolName) {

		String sqlCnt = "";
		int recCnt = 0;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			stmt= conn.createStatement();

			// String 선언을 위한 레코드 개수 구하기
			sqlCnt = "SELECT COUNT(*) FROM (" + sql + ")";
			logger.debug(sqlCnt);
			rset = stmt.executeQuery(sqlCnt);


			int k = 0;
			while(rset.next()) {
				k ++;
				logger.debug("k ++================="+k);

				recCnt = rset.getInt(1);

			}

			if (k > 1) recCnt = k;

			logger.debug("recCnt================="+recCnt);

			if (recCnt == 0) return null;
			logger.debug(sql);
			rset = stmt.executeQuery(sql);
			String resultSet[][] = new String[recCnt][colLength];

			for(int i= 0; rset.next(); i ++) {
				for (int j = 0; j < colLength; j++ ) {
					resultSet[i][j] = StringUtil.getReplaceStr(rset.getString(j + 1));
				}
			}
			return resultSet;
		} catch(Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			release();
		}
	}

	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 *   CLOB 데이터를 가지고 올때 사용함 (clob필드는 쿼리에서 마지막에 기술)
	 */
	public String[][] getTableData_Clob(String sql, int colLength,String poolName) {

		String sqlCnt = "";
		int recCnt = 0;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			resource = new ConnectionResource(this);
			stmt= conn.createStatement();

			// String 선언을 위한 레코드 개수 구하기
			sqlCnt = "SELECT COUNT(*) FROM (" + sql + ")";
			logger.debug(sqlCnt);
			rset = stmt.executeQuery(sqlCnt);

			int k = 0;
			while(rset.next()) {
				k ++;
				recCnt = rset.getInt(1);
			}

			if (k > 1) recCnt = k;

			if (recCnt == 0) return null;
			logger.debug(sql);
			rset = stmt.executeQuery(sql);
			String resultSet[][] = new String[recCnt][colLength];

			for(int i= 0; rset.next(); i ++) {
				for (int j = 0; j < colLength; j++ ) {
					if(j+1 == colLength){ //CLOB일때..
						resultSet[i][j] = StringUtil.getReplaceStr(ClobToStr((CLOB)rset.getClob(j+1)));
					}
					else{
						resultSet[i][j] = StringUtil.getReplaceStr(rset.getString(j + 1));
					}
				}
			}
			return resultSet;
		} catch(Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			release();
		}
	}



	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 */
	public String[][] getTableData(String sql, int colLength, int rowLength,String poolName) {


		if (rowLength < 1) return null;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			synchronized (conn) {
				stmt= conn.createStatement();

				logger.debug(sql);
				rset = stmt.executeQuery(sql);
				String resultSet[][] = new String[rowLength][colLength];

				for(int i = 0; rset.next(); i ++) {
					for (int j = 0; j < colLength; j++ ) {
						if (i == rowLength) return resultSet;
						resultSet[i][j] = StringUtil.getReplaceStr(rset.getString(j + 1));
					}
				}

				return resultSet;
			}
		} catch(Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			release();
		}
	}

	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 */
	public CommonCollect getTableData(String sql,String poolName) {
		CommonCollect collect = null;
		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//			resource = new ConnectionResource(this);
			stmt= conn.createStatement();
			logger.debug(sql);
			rset = stmt.executeQuery(sql);
			//System.out.println(sql);
			if(rset != null){
				collect = new CommonCollect();
				collect.convert(rset);
				if(collect.size()< 1){
					collect = null;
				}
			}
			return collect;
		} catch(Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			release();
		}
	}
	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 * @throws SQLException
	 */

	public String ClobProc(String original_Conents,String Select_Query,String Update_Query,String poolName)
			throws SQLException {

		String returnFlag = "0" ;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			conn.setAutoCommit(false);       	 //   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");

			preStmt = conn.prepareStatement(Select_Query);
			logger.debug(Select_Query);
			logger.debug(Update_Query);
			rset = preStmt.executeQuery();

			if ( rset != null && rset.next() ) {
				CLOB clob = (CLOB)rset.getObject("CONTENTS");
				if(clob != null)          {
					StrToClob(original_Conents, clob);
					preStmt = conn.prepareStatement(Update_Query);
					preStmt.setClob(1, clob);
					preStmt.executeUpdate();
				}
			}
			returnFlag = "1";
			conn.commit();
			conn.setAutoCommit(true);
			return returnFlag;

		} catch(Exception e) {
			conn.rollback();
			logger.error(e.toString());
			return "99";
		} finally {
			conn.setAutoCommit(true);
			release();
		}
	}

	/**
	 * StrToClob
	 */
	public  void StrToClob(String str,CLOB clob )
	{
		long   getErrorCode  = 0  ;
		String getMessage    = "" ;
		Writer clobWriter    = null;

		try
		{
			clobWriter = clob.getCharacterOutputStream();
			//clobWriter = ((OracleClob)clob).getCharacterOutputStream();
			clobWriter.write(str);

		} catch(SQLException e)
		{
			getErrorCode = e.getErrorCode();
			getMessage   = "Dbw SQLException : "+e.getMessage()+"";

		} catch(IOException e)
		{
			getErrorCode = -99901;
			getMessage   = "Dbw IOException : "+e.getMessage()+"";

		}
		catch(Exception e)
		{
			getErrorCode = -99900;
			getMessage   = "Dbw Exception : "+e.getMessage()+"";
		}
		finally
		{
			try
			{
				if(clobWriter!=null)   clobWriter.flush();
				if(clobWriter!=null)   clobWriter.close();
			}
			catch(IOException e)
			{

			}
		}
	}
	/**
	 * ClobToStr
	 */
	public  String ClobToStr(CLOB inClob)
	{
		if(inClob == null) return(null);
		try
		{

			CLOB clob = inClob;
			Reader rStream = clob.getCharacterStream();

			StringBuffer sBuffer = new StringBuffer();
			int lenth = 0;
			char[] cBuffer = new char[1024];
			// 			System.out.println("start");
			while((lenth= rStream.read(cBuffer) )!= -1)
				sBuffer.append(cBuffer,0,lenth);
			rStream.close();
			// 			System.out.println("end");
			return sBuffer.toString();

		} catch(SQLException e)
		{
			e.printStackTrace();
		} catch(IOException e)
		{
			e.printStackTrace();
		}

		return(null);
	}
}
