package com.optidpp.podservice.common.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;

import com.optidpp.common.logger.Log;
import com.optidpp.podservice.db.ConnectionResource;
import com.optidpp.podservice.db.DBErrorMsg;
import com.optidpp.podservice.db.dac.CommonDAC;
import com.optidpp.podservice.db.dto.CommonCollect;

public class DBUtil extends CommonDAC{

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
			//conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				Log.Debug(sqlstr);
				boolean tempResult = stmt.execute(sqlstr);
				if(!tempResult){
					result = "OK";
				}
			}
		} catch(Exception e) {
			Log.Error(e.toString());
			return DBErrorMsg.getErrMsg(e.toString());
		} finally {
			try{
				release();
			}catch(Exception e){
				Log.Error(e.toString());
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
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);

				for (int i = 0; i < sqlstr.length; i ++)
				{
					tmpsqlstr = sqlstr[i];
					Log.Debug(tmpsqlstr);
					stmt.execute(tmpsqlstr);
				}

				conn.commit();
			}
		} catch(Exception e) {
			Log.Error(e.toString());
		} finally {
			release();
		}
	}

	/**
	 *   매개변수로 받은 SQL문을 실행시켜준다.
	 *
	 */
	public int execBatchSQL2(String poolName,String sqlstr) {
		int result = -1;

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				Log.Debug(sqlstr);
				result = stmt.executeUpdate(sqlstr);
			}
		} catch(Exception e) {
			Log.Error(DBErrorMsg.getErrMsg(e.toString()));
			return -1;
		} finally {
			try{
				release();
			}catch(Exception e){
				Log.Error(e.toString());
				return -1;
			}
		}
		return result;
	}
	public int execBatchSQL3(String sqlstr) {
		int result = -1;

		try {
			//resource = new ConnectionResource(this);
			//conn = resource.getConnection(poolName);
			conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				Log.Debug(sqlstr);
				result = stmt.executeUpdate(sqlstr);
			}
		} catch(Exception e) {
			Log.Error(DBErrorMsg.getErrMsg(e.toString()));
			return -1;
		} finally {
			try{
				release();
			}catch(Exception e){
				Log.Error(e.toString());
				return -1;
			}
		}
		return result;
	}
	/**
	 *   매개변수로 받은 복수개의 SQL문을 실행시켜준다.
	 *
	 */
	public int execBatchSQL(String poolName,String[] sqlstr) {
		int successCount = 0;

		if (sqlstr == null) return successCount;
		else if (sqlstr.length == 1)
		{
			return execBatchSQL2(poolName,sqlstr[0]);
		}

		String tmpsqlstr = "";

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);
				//	System.out.println("sqlstr.length = "+sqlstr.length);
				for (int i = 0; i < sqlstr.length; i ++)
				{
					if( sqlstr[i]!=null && !"".equals(sqlstr[i])) {
						Log.Debug(i+".sql="+sqlstr[i]);
						tmpsqlstr = sqlstr[i];
						//		System.out.println(i+".sql="+sqlstr[i]);
						stmt.addBatch(tmpsqlstr);
					}
				}
				int[] result = stmt.executeBatch();

				conn.commit();

				for (int rtn : result) {
					successCount += rtn;
				}

			}
		} catch(Exception e) {
			Log.Error(e.toString());
			e.printStackTrace();
			try{
				successCount = 0;
				conn.rollback();
			}catch(Exception sqlEx){
				Log.Error(sqlEx.toString());
			}
		} finally {
			release();
		}

		return successCount;
	}

	public int execBatchSQL(String[] sqlstr) {
		int successCount = 0;

		if (sqlstr == null) return successCount;
		else if (sqlstr.length == 1)
		{
			return execBatchSQL3(sqlstr[0]);
		}

		String tmpsqlstr = "";

		try {
			//resource = new ConnectionResource(this);
			//	conn = resource.getConnection(poolName);
			conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);

				for (int i = 0; i < sqlstr.length; i ++)
				{
					if( sqlstr[i]!=null && !"".equals(sqlstr[i])) {
						Log.Debug(i+".sql="+sqlstr[i]);
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
			Log.Error(e.toString());
			try{
				successCount = 0;
				conn.rollback();
			}catch(Exception sqlEx){
				Log.Error(sqlEx.toString());
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
	public int execBatchSQL(LinkedHashMap<Integer, String> sqlMap) {
		int successCount = 0;

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();
				conn.setAutoCommit(false);

				for (Iterator<String> sqlit = sqlMap.values().iterator(); sqlit.hasNext();) {
					String sql = sqlit.next();
					if( sql!=null && !"".equals(sql) ) {
						Log.Debug("sql="+sql);
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
			Log.Error(e.toString());
			try{
				successCount = 0;
				conn.rollback();
			}catch(Exception sqlEx){
				Log.Error(sqlEx.toString());
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
	public String getTableData1x1(String sql) {

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();

				Log.Debug(sql);
				rset = stmt.executeQuery(sql);

				if (!rset.next()) return null;
				else return StringUtil.getReplaceStr(rset.getString(1));
			}

		} catch(Exception e) {
			Log.Error(e.toString()+"\n"+sql);
			return null;
		} finally {
			release();
		}
	}

	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 */
	public String[][] getTableData(String sql, int colLength) {

		String sqlCnt = "";
		int recCnt = 0;

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			stmt= conn.createStatement();

			// String 선언을 위한 레코드 개수 구하기
			sqlCnt = "SELECT COUNT(*) FROM (" + sql + ")";
			Log.Debug(sqlCnt);
			rset = stmt.executeQuery(sqlCnt);


			int k = 0;
			while(rset.next()) {
				k ++;
				Log.Debug("k ++================="+k);

				recCnt = rset.getInt(1);

			}

			if (k > 1) recCnt = k;

			Log.Debug("recCnt================="+recCnt);

			if (recCnt == 0) return null;
			Log.Debug(sql);
			rset = stmt.executeQuery(sql);
			String resultSet[][] = new String[recCnt][colLength];

			for(int i= 0; rset.next(); i ++) {
				for (int j = 0; j < colLength; j++ ) {
					resultSet[i][j] = StringUtil.getReplaceStr(rset.getString(j + 1));
				}
			}
			return resultSet;
		} catch(Exception e) {
			Log.Error(e.toString());
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
	public String[][] getTableData_Clob(String sql, int colLength) {

		String sqlCnt = "";
		int recCnt = 0;

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			stmt= conn.createStatement();

			// String 선언을 위한 레코드 개수 구하기
			sqlCnt = "SELECT COUNT(*) FROM (" + sql + ")";
			Log.Debug(sqlCnt);
			rset = stmt.executeQuery(sqlCnt);

			int k = 0;
			while(rset.next()) {
				k ++;
				recCnt = rset.getInt(1);
			}

			if (k > 1) recCnt = k;

			if (recCnt == 0) return null;
			Log.Debug(sql);
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
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}
	}



	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 */
	public String[][] getTableData(String sql, int colLength, int rowLength) {


		if (rowLength < 1) return null;

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			synchronized (conn) {
				stmt= conn.createStatement();

				Log.Debug(sql);
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
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}
	}

	/**
	 * 데이타베이스에 프로시져를 호출하고 인자값을 넘겨준 후 String 타입의 결과값을 받는다.
	 * @param procudureName 프로시져이름
	 * @param typeList  파라메트 타입정의
	 * @param paramList  파라메트 값
	 * @return result_message 프로시져 호출 후 결과메시지
	 */
	public String call_IUD_Procedure(String procudureName, String[] typeList, String[] paramList){
		CallableStatement callstmt = null;
		String result_message = "Exception";
		Vector outVec = new Vector();

		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return "등록에 실패했습니다.";
		}

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i < typeList.length ; i++){
					paramString = paramString + "?,";
				}
				paramString = paramString.substring(0, paramString.length()-1);
				prepareCallString = "{call "+procudureName+"(?,"+paramString+")}";

				callstmt = conn.prepareCall(prepareCallString);

				callstmt.registerOutParameter(1, OracleTypes.VARCHAR);
				for(int i = 0 ; i < typeList.length ; i++){
					if(typeList[i].toUpperCase().equals("INT")){
						callstmt.setInt(i+2, Integer.parseInt(paramList[i]));
					}else if(typeList[i].toUpperCase().equals("STRING")){
						callstmt.setString(i+2, paramList[i]);
					}else{
						Log.Error("type error");
						return "프로시져 변수 타입이 다릅니다.";
					}
				}

				boolean successFlag = callstmt.execute();

				outVec.addElement(callstmt.getString(1));
				result_message = outVec.elementAt(0).toString();
			}
		} catch(Exception e) {
			Log.Error(e.toString());
			return e.toString()+"<br>예외가 발생했습니다..";
		} finally {
			release();
		}
		return result_message;
	}


	public String call_common_Procedure(String procudureName, String[] typeList, String[] paramList){

		CallableStatement callstmt = null;
		String result_message = "Exception";
		Vector outVec = new Vector();

		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return "등록에 실패했습니다.";
		}

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i <= typeList.length ; i++){
					paramString = paramString + "?,";
				}
				paramString = paramString.substring(0, paramString.length()-1);
				prepareCallString = "{call "+procudureName+"("+paramString+")}";

				callstmt = conn.prepareCall(prepareCallString);

				callstmt.registerOutParameter(1, OracleTypes.VARCHAR);
				for(int i = 0 ; i < typeList.length ; i++){
					if(typeList[i].toUpperCase().equals("INT")){
						callstmt.setInt(i+2, Integer.parseInt(paramList[i]));
					}else if(typeList[i].toUpperCase().equals("STRING")){
						callstmt.setString(i+2, paramList[i]);
					}else{
						Log.Error("type error");
						return "프로시져 변수 타입이 다릅니다.";
					}
				}

				boolean successFlag = callstmt.execute();
				if(successFlag) result_message = "OK";
			}
		} catch(Exception e) {
			Log.Error(e.toString());
			return e.toString()+"<br>예외가 발생했습니다..";
		} finally {
			release();
		}
		return result_message;
	}

	/**
	 * 데이타베이스에 프로시져를 호출하고 인자값을 넘겨준 후 조건의 타입의 결과값을 받는다.
	 * @param procudureName 프로시져이름
	 * @param fieldCnt 셀렉트하는 필드 개수
	 * @return resultData 프로시져 호출 후 결과값
	 */
	public String[][] call_S_Procedure(String poolName,String procudureName, String[] typeList, String[] paramList, int fieldCnt){
		CallableStatement callstmt = null;
		String resultData[][] = null;
		Vector outVec = new Vector();
		ResultSet ServerRSet = null;
		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return null;
		}

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i < typeList.length ; i++){
					paramString = paramString + "?,";
				}

				paramString = paramString.substring(0, paramString.length()-1);

				//prepareCallString = "{call "+procudureName+"("+paramString+")}";
				prepareCallString = "{? = call  "+procudureName+"("+paramString+") }";
				//System.out.println("prepareCallString = "+prepareCallString);

				callstmt = conn.prepareCall(prepareCallString);

				//callstmt.registerOutParameter(1, OracleTypes.CURSOR);
				for(int i = 0 ; i < typeList.length ; i++){
					if(typeList[i].toUpperCase().equals("INT")){
						callstmt.setInt(i+2, Integer.parseInt(paramList[i]));
					}else if(typeList[i].toUpperCase().equals("STRING")){

						if(paramList[i] == null || "".equals(paramList[i])){
							//System.out.println("i = "+(i+2)+" NULL");
							callstmt.setString(i+2,"");
						}else{
							//System.out.println(" i = "+(i+2)+" "+paramList[i]);
							callstmt.setString(i+2, paramList[i]);
						}

					}else if(typeList[i].toUpperCase().equals("CORSOR")){
						////System.out.println("i = "+(i+1) +" CURSOR");
						callstmt.registerOutParameter(i+1, OracleTypes.CURSOR);
					}else{
						Log.Error("type error");
						return null;
					}

				}

				boolean successFlag = callstmt.execute();
				//System.out.println("successFlag = "+successFlag);
				OracleCallableStatement  tstmt = null;
				tstmt = (OracleCallableStatement)callstmt;
				ServerRSet =  tstmt.getCursor (4);
				//System.out.println("ServerRSet.getRow() = "+ServerRSet.getRow());
				if(ServerRSet != null){
					resultData = new String[ServerRSet.getRow()][fieldCnt];

					try {
						for(int i = 0 ; i < ServerRSet.getRow() ; i++){

							if(!ServerRSet.getObject(1).equals("NULL") && !ServerRSet.getObject(1).equals("null")){
								String dataRow = "";
								for(int j = 1 ; j <= fieldCnt ; j++){
									resultData[i][j] = StringUtil.nvl(ServerRSet.getObject(j),"");
									//System.out.println("resultData[i][j] = "+resultData[i][j]);
								}
							}
						}
					}catch (Exception e){
						Log.Error(e.toString());
					}
				}
			}
		} catch(Exception e) {
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}
		return resultData;
	}

	/**
	 * 데이타베이스에 프로시져를 호출하고 인자값을 넘겨준 후 조건의 타입의 결과값을 받는다.
	 * @param procudureName 프로시져이름
	 * @param fieldCnt 셀렉트하는 필드 개수
	 * @return resultData 프로시져 호출 후 결과값
	 */
	public String[][] call_S_Procedure(String procudureName, int fieldCnt){
		CallableStatement callstmt = null;
		String resultData[][] = null;
		Vector outVec = new Vector();
		ResultSet ServerRSet = null;

		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				paramString = paramString.substring(0, paramString.length()-1);
				prepareCallString = "{call "+procudureName+"(?,"+paramString+")}";

				callstmt = conn.prepareCall(prepareCallString);

				callstmt.registerOutParameter(1, OracleTypes.CURSOR);

				boolean successFlag = callstmt.execute();

				OracleCallableStatement  tstmt = null;
				tstmt = (OracleCallableStatement)callstmt;
				ServerRSet =  tstmt.getCursor (1);

				if(ServerRSet != null){
					resultData = new String[ServerRSet.getRow()][fieldCnt];
					try {
						for(int i = 0 ; i < ServerRSet.getRow() ; i++){
							if(!ServerRSet.getObject(1).equals("NULL") && !ServerRSet.getObject(1).equals("null")){
								String dataRow = "";
								for(int j = 1 ; j <= fieldCnt ; j++){
									resultData[i][j] = StringUtil.nvl(ServerRSet.getObject(j),"");
								}
							}
						}
					}catch (Exception e){
						Log.Error(e.toString());
					}
				}
			}
		} catch(Exception e) {
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}
		return resultData;
	}

	/**
	 *   데이터베이스 테이블로부터 자료를 가져올 때, 가져올 컬럼의 갯수와
	 *   SQL문을 받아 실행 후 그 결과를 되돌려준다.
	 */
	public CommonCollect getTableData(String sql) {
		CommonCollect collect = null;
		try {
			//   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");
			conn = getConnection();
			stmt= conn.createStatement();
			Log.Debug(sql);
			rset = stmt.executeQuery(sql);
			if(rset != null){
				collect = new CommonCollect();
				collect.convert(rset);
				if(collect.size()< 1){
					collect = null;
				}
			}
			return collect;
		} catch(Exception e) {
			Log.Error(e.toString());
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

	public String ClobProc(String original_Conents,String Select_Query,String Update_Query)
			throws SQLException {

		String returnFlag = "0" ;

		try {
			conn = getConnection();
			conn.setAutoCommit(false);       	 //   resource = new ConnectionResource(this);
			//   connn = resource.getConnection("pool1");

			preStmt = conn.prepareStatement(Select_Query);
			Log.Debug(Select_Query);
			Log.Debug(Update_Query);
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
			Log.Error(e.toString());
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
	public CommonCollect getTableData(String sql,String poolName) {
		CommonCollect collect = null;
		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//			resource = new ConnectionResource(this);
			stmt= conn.createStatement();
			Log.Debug(sql);
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
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}
	}

	public CommonCollect call_QICS_Procedure1(String poolName, String procudureName,
			String[] typeList, String[] paramList) {
		CallableStatement callstmt = null;
		ResultSet ServerRSet = null;
		CommonCollect collect = null;
		int idx=0;
		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return null;
		}

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i < typeList.length ; i++){
					paramString = paramString + "?,";
				}

				paramString = paramString.substring(0, paramString.length()-1);

				//prepareCallString = "{call "+procudureName+"("+paramString+")}";
				prepareCallString = "{ call  "+procudureName+"("+paramString+") }";
				//System.out.println("prepareCallString = "+prepareCallString);

				callstmt = conn.prepareCall(prepareCallString);

				//	callstmt.registerOutParameter(1, OracleTypes.CURSOR);
				for(int i = 0 ; i < typeList.length ; i++){
					if(typeList[i].toUpperCase().equals("INT")){
						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setInt(i+1, Integer.parseInt(paramList[i]));
						}
					}else if(typeList[i].toUpperCase().equals("STRING")){

						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setString(i+1, paramList[i]);
						}

					}else if(typeList[i].toUpperCase().equals("DATE")){
						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setDate(i+1,null);
						}else{
							DateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
							Date dt = sdFormat.parse(paramList[i]);
							java.sql.Date dateNow = new java.sql.Date(dt.getTime());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							callstmt.setDate(i+1,dateNow);//(i+1, sdf.format(paramList[i]).toString());
						}
						//callstmt.setString(i+1,"");
					}else if(typeList[i].toUpperCase().equals("CORSOR")){
						idx = i+1;
						callstmt.registerOutParameter(i+1, OracleTypes.CURSOR);
						//callstmt.setString(i+1,"");
					}else{
						Log.Error("type error");
						return null;
					}

				}
				callstmt.execute();
				//System.out.println("idx = "+idx);
				rset =  (ResultSet) callstmt.getObject (4);

				if(rset != null){
					collect = new CommonCollect();
					collect.convert(rset);
					//System.out.println("collect.size() "+collect.size());
					if(collect.size()< 1){
						collect = null;
					}
				}
				/*while(ServerRSet.next()){
					System.out.println("WIP_ENTITY_NAME ="+ServerRSet.getString("WIP_ENTITY_NAME"));
					System.out.println("R_SUPPLY_THICKNESS ="+ServerRSet.getString("R_SUPPLY_THICKNESS"));
					System.out.println("AIM_THICKNESS_1 ="+ServerRSet.getString("AIM_THICKNESS_1"));
					System.out.println("AIM_THICKNESS ="+ServerRSet.getString("AIM_THICKNESS"));
					System.out.println("TARGET_THICKNESS ="+ServerRSet.getString("TARGET_THICKNESS"));
					System.out.println("LAST_WIDTH ="+ServerRSet.getString("LAST_WIDTH"));
					System.out.println("R_SUPPLIER ="+ServerRSet.getString("R_SUPPLIER"));
					System.out.println("STEEL_TYPE ="+ServerRSet.getString("STEEL_TYPE"));
					System.out.println("HEAT_NO ="+ServerRSet.getString("HEAT_NO"));
					//		System.out.println("PASS_CNT ="+ServerRSet.getString("PASS_CNT"));
					System.out.println("PROD_SURFACE ="+ServerRSet.getString("PROD_SURFACE"));
					System.out.println("CUSTUMER ="+ServerRSet.getString("CUSTUMER"));
					System.out.println("ORDER_NUMBER ="+ServerRSet.getString("ORDER_NUMBER"));
					System.out.println("LINE_NUMBER ="+ServerRSet.getString("LINE_NUMBER"));
					System.out.println("RELEATED_SIZE ="+ServerRSet.getString("RELEATED_SIZE"));
					System.out.println("OP_USE ="+ServerRSet.getString("OP_USE"));
					System.out.println("PAPER ="+ServerRSet.getString("PAPER"));
				}*/


			}
			return collect;
		} catch(Exception e) {
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}

	}
	public HashMap call_QICS_Procedure2(String poolName, String procudureName,
			String[] typeList, String[] paramList) {
		CallableStatement callstmt = null;
		ResultSet ServerRSet1 = null;
		ResultSet ServerRSet2 = null;
		CommonCollect collect = null;
		HashMap hm=new HashMap();
		int idx=0;
		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return null;
		}

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i < typeList.length ; i++){
					paramString = paramString + "?,";
				}

				paramString = paramString.substring(0, paramString.length()-1);

				//prepareCallString = "{call "+procudureName+"("+paramString+")}";
				prepareCallString = "{ call  "+procudureName+"("+paramString+") }";
				//System.out.println("prepareCallString = "+prepareCallString);

				callstmt = conn.prepareCall(prepareCallString);

				//	callstmt.registerOutParameter(1, OracleTypes.CURSOR);
				for(int i = 0 ; i < typeList.length ; i++){
					if(typeList[i].toUpperCase().equals("INT")){
						callstmt.setInt(i+1, Integer.parseInt(paramList[i]));
					}else if(typeList[i].toUpperCase().equals("STRING")){

						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setString(i+1, paramList[i]);
						}

					}else if(typeList[i].toUpperCase().equals("CORSOR")){
						idx = i+1;
						callstmt.registerOutParameter(idx, OracleTypes.CURSOR);
						//callstmt.setString(i+1,"");
					}else{
						Log.Error("type error");
						return null;
					}

				}
				callstmt.execute();
				//System.out.println("idx = "+idx);
				ServerRSet1 =  (ResultSet) callstmt.getObject (4);
				//System.out.println(ServerRSet1.getRow());
				if(ServerRSet1 != null){
					collect = new CommonCollect();
					collect.convert(ServerRSet1);

					//System.out.println("collect.size() "+collect.size());
					if(collect.size()< 1){
						collect = null;
					}
					//System.out.println(collect);
					hm.put("cusor1",collect);
				}
				ServerRSet2=  (ResultSet) callstmt.getObject (5);
				//System.out.println(ServerRSet2.getRow());
				if(ServerRSet2 != null){
					collect = new CommonCollect();
					collect.convert(ServerRSet2);

					//System.out.println("collect.size() "+collect.size());
					if(collect.size()< 1){
						collect = null;
					}
					hm.put("cusor2",collect);
				}
				/*while(ServerRSet.next()){
					System.out.println("WIP_ENTITY_NAME ="+ServerRSet.getString("WIP_ENTITY_NAME"));
					System.out.println("R_SUPPLY_THICKNESS ="+ServerRSet.getString("R_SUPPLY_THICKNESS"));
					System.out.println("AIM_THICKNESS_1 ="+ServerRSet.getString("AIM_THICKNESS_1"));
					System.out.println("AIM_THICKNESS ="+ServerRSet.getString("AIM_THICKNESS"));
					System.out.println("TARGET_THICKNESS ="+ServerRSet.getString("TARGET_THICKNESS"));
					System.out.println("LAST_WIDTH ="+ServerRSet.getString("LAST_WIDTH"));
					System.out.println("R_SUPPLIER ="+ServerRSet.getString("R_SUPPLIER"));
					System.out.println("STEEL_TYPE ="+ServerRSet.getString("STEEL_TYPE"));
					System.out.println("HEAT_NO ="+ServerRSet.getString("HEAT_NO"));
					//		System.out.println("PASS_CNT ="+ServerRSet.getString("PASS_CNT"));
					System.out.println("PROD_SURFACE ="+ServerRSet.getString("PROD_SURFACE"));
					System.out.println("CUSTUMER ="+ServerRSet.getString("CUSTUMER"));
					System.out.println("ORDER_NUMBER ="+ServerRSet.getString("ORDER_NUMBER"));
					System.out.println("LINE_NUMBER ="+ServerRSet.getString("LINE_NUMBER"));
					System.out.println("RELEATED_SIZE ="+ServerRSet.getString("RELEATED_SIZE"));
					System.out.println("OP_USE ="+ServerRSet.getString("OP_USE"));
					System.out.println("PAPER ="+ServerRSet.getString("PAPER"));
				}*/


			}
			return hm;
		} catch(Exception e) {
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}

	}
	public CommonCollect call_QICS_Procedure(String poolName, String procudureName,
			String[] typeList, String[] paramList) {
		CallableStatement callstmt = null;
		ResultSet ServerRSet = null;
		CommonCollect collect = null;
		int idx=0;
		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return null;
		}

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i < typeList.length ; i++){
					paramString = paramString + "?,";
				}

				paramString = paramString.substring(0, paramString.length()-1);

				//prepareCallString = "{call "+procudureName+"("+paramString+")}";
				prepareCallString = "{ call  "+procudureName+"("+paramString+") }";
				//System.out.println("prepareCallString = "+prepareCallString);

				callstmt = conn.prepareCall(prepareCallString);

				//	callstmt.registerOutParameter(1, OracleTypes.CURSOR);
				for(int i = 0 ; i < typeList.length ; i++){
					if(typeList[i].toUpperCase().equals("INT")){
						callstmt.setInt(i+1, Integer.parseInt(paramList[i]));
					}else if(typeList[i].toUpperCase().equals("STRING")){
						//System.out.println(i+" : " +paramList[i]);
						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setString(i+1, paramList[i]);
						}

					}else if(typeList[i].toUpperCase().equals("CORSOR")){
						idx = i+1;
						callstmt.registerOutParameter(idx, OracleTypes.CURSOR);
						//callstmt.setString(i+1,"");
					}else{
						Log.Error("type error");
						return null;
					}

				}
				callstmt.execute();
				//System.out.println("idx = "+idx);
				rset =  (ResultSet) callstmt.getObject (idx);

				if(rset != null){
					collect = new CommonCollect();
					collect.convert(rset);
					//System.out.println("collect.size() "+collect.size());
					if(collect.size()< 1){
						collect = null;
					}
				}
				/*while(ServerRSet.next()){
					System.out.println("WIP_ENTITY_NAME ="+ServerRSet.getString("WIP_ENTITY_NAME"));
					System.out.println("R_SUPPLY_THICKNESS ="+ServerRSet.getString("R_SUPPLY_THICKNESS"));
					System.out.println("AIM_THICKNESS_1 ="+ServerRSet.getString("AIM_THICKNESS_1"));
					System.out.println("AIM_THICKNESS ="+ServerRSet.getString("AIM_THICKNESS"));
					System.out.println("TARGET_THICKNESS ="+ServerRSet.getString("TARGET_THICKNESS"));
					System.out.println("LAST_WIDTH ="+ServerRSet.getString("LAST_WIDTH"));
					System.out.println("R_SUPPLIER ="+ServerRSet.getString("R_SUPPLIER"));
					System.out.println("STEEL_TYPE ="+ServerRSet.getString("STEEL_TYPE"));
					System.out.println("HEAT_NO ="+ServerRSet.getString("HEAT_NO"));
					//		System.out.println("PASS_CNT ="+ServerRSet.getString("PASS_CNT"));
					System.out.println("PROD_SURFACE ="+ServerRSet.getString("PROD_SURFACE"));
					System.out.println("CUSTUMER ="+ServerRSet.getString("CUSTUMER"));
					System.out.println("ORDER_NUMBER ="+ServerRSet.getString("ORDER_NUMBER"));
					System.out.println("LINE_NUMBER ="+ServerRSet.getString("LINE_NUMBER"));
					System.out.println("RELEATED_SIZE ="+ServerRSet.getString("RELEATED_SIZE"));
					System.out.println("OP_USE ="+ServerRSet.getString("OP_USE"));
					System.out.println("PAPER ="+ServerRSet.getString("PAPER"));
				}*/


			}
			return collect;
		} catch(Exception e) {
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}

	}
	public String call_QICS_Procedure4(String poolName, String procudureName,
			String[] typeList, String[] paramList) {
		CallableStatement callstmt = null;
		Vector outVec = new Vector();
		String result_message = "";
		int idx=0;
		if(typeList.length != paramList.length ){
			Log.Error("parameterList count is no equal typeList count");
			return null;
		}

		try {
			resource = new ConnectionResource(this);
			conn = resource.getConnection(poolName);
			//conn = getConnection();

			synchronized (conn) {

				String paramString = "";
				String prepareCallString = "";

				for(int i = 0 ; i < typeList.length ; i++){
					paramString = paramString + "?,";
				}

				paramString = paramString.substring(0, paramString.length()-1);

				//prepareCallString = "{call "+procudureName+"("+paramString+")}";
				prepareCallString = "{ call  "+procudureName+"("+paramString+") }";
				//System.out.println("prepareCallString = "+prepareCallString);

				callstmt = conn.prepareCall(prepareCallString);

				//	callstmt.registerOutParameter(1, OracleTypes.CURSOR);
				for(int i = 0 ; i < typeList.length ; i++){
					//System.out.println(typeList[i]+" paramList["+i+"] = "+ paramList[i]);
					if(typeList[i].toUpperCase().equals("INT")){

						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setInt(i+1, Integer.parseInt(paramList[i]));
						}
					}else if(typeList[i].toUpperCase().equals("FLOAT")){

						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setFloat(i+1, Float.parseFloat(paramList[i]));
						}

					}else if(typeList[i].toUpperCase().equals("STRING")){

						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setString(i+1,null);
						}else{
							callstmt.setString(i+1, paramList[i]);
						}

					}else if(typeList[i].toUpperCase().equals("DATE")){
						if(paramList[i] == null || "".equals(paramList[i])){
							callstmt.setDate(i+1,null);
						}else{
							DateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
							Date dt = sdFormat.parse(paramList[i]);
							java.sql.Date dateNow = new java.sql.Date(dt.getTime());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							//System.out.println("dateNow = "+dateNow);
							callstmt.setDate(i+1,dateNow);//(i+1, sdf.format(paramList[i]).toString());
						}
						//callstmt.setString(i+1,"");
					}else{
						Log.Error("type error");
						return null;
					}
					idx++;
				}
				//System.out.println("idx = "+idx);

				callstmt.registerOutParameter(idx, OracleTypes.VARCHAR);
				//	callstmt.execute();

				boolean successFlag = callstmt.execute();
				//System.out.println("successFlag = "+ successFlag);
				result_message = callstmt.getString(idx);

				/*while(ServerRSet.next()){
					System.out.println("WIP_ENTITY_NAME ="+ServerRSet.getString("WIP_ENTITY_NAME"));
					System.out.println("R_SUPPLY_THICKNESS ="+ServerRSet.getString("R_SUPPLY_THICKNESS"));
					System.out.println("AIM_THICKNESS_1 ="+ServerRSet.getString("AIM_THICKNESS_1"));
					System.out.println("AIM_THICKNESS ="+ServerRSet.getString("AIM_THICKNESS"));
					System.out.println("TARGET_THICKNESS ="+ServerRSet.getString("TARGET_THICKNESS"));
					System.out.println("LAST_WIDTH ="+ServerRSet.getString("LAST_WIDTH"));
					System.out.println("R_SUPPLIER ="+ServerRSet.getString("R_SUPPLIER"));
					System.out.println("STEEL_TYPE ="+ServerRSet.getString("STEEL_TYPE"));
					System.out.println("HEAT_NO ="+ServerRSet.getString("HEAT_NO"));
					//		System.out.println("PASS_CNT ="+ServerRSet.getString("PASS_CNT"));
					System.out.println("PROD_SURFACE ="+ServerRSet.getString("PROD_SURFACE"));
					System.out.println("CUSTUMER ="+ServerRSet.getString("CUSTUMER"));
					System.out.println("ORDER_NUMBER ="+ServerRSet.getString("ORDER_NUMBER"));
					System.out.println("LINE_NUMBER ="+ServerRSet.getString("LINE_NUMBER"));
					System.out.println("RELEATED_SIZE ="+ServerRSet.getString("RELEATED_SIZE"));
					System.out.println("OP_USE ="+ServerRSet.getString("OP_USE"));
					System.out.println("PAPER ="+ServerRSet.getString("PAPER"));
				}*/


			}
			return result_message;
		} catch(Exception e) {
			e.printStackTrace();
			Log.Error(e.toString());
			return null;
		} finally {
			release();
		}

	}
}