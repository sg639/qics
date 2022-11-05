package com.optidpp.common.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.optidpp.common.logger.Log;
/**
 * Common Parameter Manager
 *
 * @author ParkMoohun
 */
public class ParamUtils {

	/**
	 * 열단위로 들어오는 인자값을 행단위 데이터셋으로 변환
	 * @param request
	 * @param paramNames
	 * @param paramValues
	 * @return HashMap<String, Object>
	 */
	public static synchronized  HashMap<String, Object> requestInParamsMultiDML( HttpServletRequest request, String paramNames, String paramValues ) {
		String[] exCols 					= null;
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		HashMap<String, String> map 		= null;
		// COLS LIST
		List<Serializable>  mergeRows		= new ArrayList<Serializable>();
		List<Serializable>  insertRows		= new ArrayList<Serializable>();
		List<Serializable>  updateRows 		= new ArrayList<Serializable>();
		List<Serializable>  deleteRows 		= new ArrayList<Serializable>();
		List<Serializable>  subTempList		= new ArrayList<Serializable>();
		int rowSize 				= 0;
		int colSize 				= 0;
		String paramName 			= "";

		if(paramNames != null){
			String[] colNames  = paramNames.split(",");
			String[] cols      = null;
			if(paramValues != null && !paramValues.equals("")){
				cols = paramValues.split(",");
			}else{
				cols = paramNames.split(",");
			}
			Map<?, ?> paramMap 		= request.getParameterMap();
			if(paramMap.get(cols[0]) != null){
				rowSize = ((String[]) paramMap.get( cols[0]) ).length;
				colSize = cols.length;
				// COLS 인것
				if ( null != cols || cols.length > 0 )  {
					// ROW 형식
					for ( int i = 0; i < rowSize; i++ )  {
						map = new HashMap<String, String>();
						for ( int j = 0; j < colSize; j++ )  {
							if(paramMap.get(cols[j]) == null) {
								Log.Debug(cols[j]+" ================ null value");
							}
							map.put( colNames[j] , StringUtil.replaceSingleQuot( ((String[]) paramMap.get(cols[j]) )[i] ) );
						}
						if( 	map.get("sStatus").equals("I") ){ insertRows.add(map); mergeRows.add(map);}
						else if(map.get("sStatus").equals("U") ){ updateRows.add(map); mergeRows.add(map);}
						else if(map.get("sStatus").equals("D") ){ deleteRows.add(map); }
					}
					returnMap.put( "mergeRows" , mergeRows );
					returnMap.put( "insertRows" , insertRows );
					returnMap.put( "updateRows" , updateRows );
					returnMap.put( "deleteRows" , deleteRows );
				}
				Enumeration<?> enumeration = request.getParameterNames();

				// COLS 이외의 것들
				for ( int i = 0; i < paramMap.size(); i++ )
				{
					paramName = ( String ) enumeration.nextElement();
					if ( existCols( cols, paramName ) ) { continue; }
					exCols = (String[]) paramMap.get( paramName );
					if ( exCols.length > 1)
					{
						for ( int j = 0; j < exCols.length; j++ )
						{
							subTempList.add( exCols[j] );
						}
						returnMap.put( paramName, subTempList );
					} else {
						returnMap.put( paramName, exCols[0] );
					}
				}
			}
		}
		return returnMap;
	}


	/**
	 * 열단위로 들어오는 인자값을 행단위 데이터셋으로 변환
	 * @param request
	 * @param paramNames
	 * @param paramValues
	 * @return HashMap<String, Object>
	 */
	public static synchronized  HashMap<String, Object> requestInParamsMultiDML2( HttpServletRequest request, String paramNames, Map reqMap ) {
		String[] exCols 					= null;
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		HashMap<String, String> map 		= null;
		// COLS LIST
		List<Serializable>  mergeRows		= new ArrayList<Serializable>();
		List<Serializable>  insertRows		= new ArrayList<Serializable>();
		List<Serializable>  updateRows 		= new ArrayList<Serializable>();
		List<Serializable>  deleteRows 		= new ArrayList<Serializable>();
		List<Serializable>  subTempList		= new ArrayList<Serializable>();
		int rowSize 				= 0;
		int colSize 				= 0;
		String paramName 			= "";

		if(paramNames != null){
			String[] colNames  = paramNames.split(",");
			String[] cols      = null;
			cols = paramNames.split(",");

			Map<?, ?> paramMap 		= reqMap;
			if(paramMap.get(cols[0]) != null){
				rowSize = ((String[]) paramMap.get( cols[0]) ).length;
				colSize = cols.length;
				// COLS 인것
				if ( null != cols || cols.length > 0 )  {
					// ROW 형식
					for ( int i = 0; i < rowSize; i++ )  {
						map = new HashMap<String, String>();
						for ( int j = 0; j < colSize; j++ )  {
							//map.put( colNames[j] , StringUtil.replaceSingleQuot( ((String[]) paramMap.get(cols[j]) )[i] ) );
							String paramValue = "";
							if(paramMap.get(cols[j]) == null || ((String[]) paramMap.get(cols[j]) )[i] == null) {
								paramValue = "";
							} else {
								paramValue = ((String[]) paramMap.get(cols[j]) )[i];
							}
							map.put( colNames[j] , StringUtil.replaceSingleQuot(paramValue));
						}
						if( 	map.get("sStatus").equals("I") ){ insertRows.add(map); mergeRows.add(map);}
						else if(map.get("sStatus").equals("U") ){ updateRows.add(map); mergeRows.add(map);}
						else if(map.get("sStatus").equals("D") ){ deleteRows.add(map); }
					}
					returnMap.put( "mergeRows" , mergeRows );
					returnMap.put( "insertRows" , insertRows );
					returnMap.put( "updateRows" , updateRows );
					returnMap.put( "deleteRows" , deleteRows );
				}

				/*
				Enumeration<?> enumeration = request.getParameterNames();

				// COLS 이외의 것들
				for ( int i = 0; i < paramMap.size(); i++ )
				{
					paramName = ( String ) enumeration.nextElement();
					if ( existCols( cols, paramName ) ) { continue; }
					exCols = (String[]) paramMap.get( paramName );
					if ( exCols != null && exCols.length > 1)
					{
						for ( int j = 0; j < exCols.length; j++ )
						{
							subTempList.add( exCols[j] );
						}
						returnMap.put( paramName, subTempList );
					} else {
						returnMap.put( paramName, exCols[0] );
					}
				}
				 */
			}
		}
		return returnMap;
	}
	/**
	 * request를 넘겨 받아 안에든 모든 값을 보여줌
	 * @param request
	 */
	public static synchronized void viewParam(HttpServletRequest request){
		Log.Debug("┌────────────────── Request Parameter View Start ────────────────────────");
		String paramName 			= "";
		String[] exCols 			= null;
		Map<?, ?> paramMap 			= request.getParameterMap();
		Enumeration<?> enumeration 	= request.getParameterNames();

		for ( int i = 0; i < paramMap.size(); i++ )  {
			paramName = ( String ) enumeration.nextElement();
			exCols = (String[]) paramMap.get( paramName );
			if ( exCols.length > 1)  {
				for ( int j = 0; j < exCols.length; j++ )  {
					Log.Debug("│  {"+paramName+ " = "+ exCols[j]+"}");
				}
			} else {
				Log.Debug("│  {"+paramName+ " = "+ exCols[0]+"}");
			}
		}
		Log.Debug("└────────────────── Request Parameter View End ──────────────────────────");
	}

	public static synchronized Map<String, Object> converterParams(HttpServletRequest request) throws UnsupportedEncodingException {
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		String[] exCols 					= null;
		List<String> subTempList 			= new ArrayList<String>();
		String paramName 					= "";
		Map<?, ?> paramMap 					= request.getParameterMap();
		Enumeration<?> enumeration 			= request.getParameterNames();

		for ( int i = 0; i < paramMap.size(); i++ )  {
			paramName = ( String ) enumeration.nextElement();
			exCols = (String[]) paramMap.get( paramName );

			if ( exCols.length > 1) {
				for ( int j = 0; j < exCols.length; j++ ) {
					subTempList.add( URLDecoder.decode( exCols[j] , "UTF-8" ) );
				}
				returnMap.put( paramName, subTempList );
			} else {
				returnMap.put( paramName, URLDecoder.decode( exCols[0] , "UTF-8" ));
			}
		}
		return returnMap;
	}

	/**
	 * 고정된 컬럼이 있는지 검색
	 * @param cols
	 * @param paramName
	 * @return boolean
	 */
	public static synchronized boolean existCols(String[] cols, String paramName) {
		for (int i = 0; i < cols.length; i++) {
			if (paramName.equals(cols[i])) { return true; }
		}
		return false;
	}
}
