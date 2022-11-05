/*
 * 화    일    명  	: CommonCollect.java
 * 
 * 화  일  설  명	: 
 *
 * 저 작 권 공 지	: 이 소프트웨어는 신동아화재해상보험주식회사 소유입니다.
 *            	    신동아화재해상보험주식회사의 허가없는 배포 및 수정할 수 없습니다.
 * 				  Copyright 2006 by Shindongah Fire & Marine insurance Co., Ltd. AllRights Reserved. 
 * 
 * 패키지 버젼 	: 1.0
 * 
 * 작   성    자  	: 이한일
 * 
 * 작  성  일 자 	: 2004. 10. 5.오전 10:50:4
 * 
 * @version		: $Revision: 1.1.1.1 $
 * 
 */

package com.optidpp.zrfc;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.optidpp.zrfc.common.StringUtil;

public class RFCCommonCollect extends ArrayList implements Serializable{
	private static final Logger logger = Logger.getLogger(RFCCommonCollect.class.getName());
	
   	protected HashMap MapFieldName =  new HashMap();
   	protected HashMap MapFieldIndex =  new HashMap();

   	public HashMap getMetaData(){
   		return this.MapFieldName;
   	}

   	public int getColumnCount(){
   		return this.MapFieldName.size();
   	}
   	
   	public String getColumnName(int idx){
   		return (String)MapFieldIndex.get(new Integer(idx));
   	}
   	
   	/**
   	 * 2005.12.02 김성환 수정
   	 * 국세청 신고파일 등 공백을 TRIM하지 않고 그냥 
	 * 넘겨주어야 할 경우가 있어 flag를 사용하여 처리토록
	 *
   	 */
   	public String getString(int idx){
   	    
   		return getString(idx, true);

   	}
   	
   	public String getString(int idx, boolean needTrim){
   	    
   		String result = "";

   		try{
   		    if(currentRow.get(idx-1) == null){
   		        result = "";
   		    }if(currentRow.get(idx-1) instanceof String){
   		        result =  (String)currentRow.get(idx-1);
   		    }else if(currentRow.get(idx-1) instanceof Integer){
   		        result = currentRow.get(idx-1).toString();
   		    }else if(currentRow.get(idx-1) instanceof Timestamp){
   		        result = ((java.sql.Timestamp)currentRow.get(idx-1)).toString();
   		    }else if(currentRow.get(idx-1) instanceof BigDecimal){
	   		    Object obj = currentRow.get(idx-1);
	   	   		String decimalString =null;
	   	   		if((obj == null)||(obj.toString().equals("null"))){
	   	   		    result = decimalString = "0";
	   	   		}else{
	   	   		    result = decimalString = (obj).toString() ;
	   		    }
   			}
   		}catch(Exception e){
   		}
   		
   		if( needTrim )
   			return StringUtil.getReplaceStr(result);
   		else
   			return StringUtil.getReplaceStr(result, false);

   	}
   	
   	public String getString(String columnName){
   		String result = "";
   		
   		if(MapFieldName.get(columnName.toUpperCase())==null) {   			
   			logger.debug("["+columnName+"](을)를 찾을수 없습니다. 컬럼명을 확인해주시기 바랍니다");
   			result = "";
   		} else {
   			
   	   		int idx = new Integer(MapFieldName.get(columnName.toUpperCase()).toString()).intValue();
   	   		try{
   	   		    if(currentRow.get(idx-1) instanceof String){
   	   		        result =  (String)currentRow.get(idx-1);
   	   		    }else if(currentRow.get(idx-1) instanceof Integer){
   	   		        result = currentRow.get(idx-1).toString();
   	   		    }else if(currentRow.get(idx-1) instanceof Timestamp){
   	   		        result = ((java.sql.Timestamp)currentRow.get(idx-1)).toString();
   	   		    }else if(currentRow.get(idx-1) instanceof BigDecimal){
   		   		    Object obj = currentRow.get(idx-1);
   		   	   		String decimalString =null;
   		   	   		if((obj == null)||(obj.toString().equals("null"))){
   		   	   		    result = decimalString = "0";
   		   	   		}else{
   		   	   		    result = decimalString = (obj).toString() ;
   		   		    }
   	   			}
   	   		}catch(Exception e){
   	   			result = "["+columnName+"](을)를 확인하는 중 오류가 발생";
   	   		}   			
   		}

   		return StringUtil.getReplaceSpcStr(result);
   	}
	public byte[] getByteArray(String columnName) {
		// TODO Auto-generated method stub
		byte[] result = null;
		int idx = new Integer(MapFieldName.get(columnName.toUpperCase()).toString()).intValue();
		
		result =  (byte[])currentRow.get(idx-1);
		return result;
	}
   	public int getInt(int idx){
   		return (new Integer(currentRow.get(idx-1).toString())).intValue();
   	}
   	
   	public int getInt(String columnName){
   		int idx = new Integer(MapFieldName.get(columnName.toUpperCase()).toString()).intValue();
   		return (new Integer(currentRow.get(idx-1).toString())).intValue();
   	}

   	public java.sql.Timestamp getDate(int idx){
   		return (java.sql.Timestamp)currentRow.get(idx-1);
   	}
   	
   	public java.sql.Timestamp getDate(String columnName){
   		int idx = new Integer(MapFieldName.get(columnName.toUpperCase()).toString()).intValue();
   		return (java.sql.Timestamp)currentRow.get(idx-1);
   	}

   	public BigDecimal getBigDecimal(int idx){
   		Object obj = currentRow.get(idx-1);
   		String decimalString =null;
   		if((obj == null)||(obj.toString().equals("null")))
   			decimalString = "0";
   		else
   			decimalString = (obj).toString() ;
   		return new BigDecimal(decimalString);
   	}
   	
   	public BigDecimal getBigDecimal(String columnName){
   		int idx = new Integer(MapFieldName.get(columnName.toUpperCase()).toString()).intValue();
   		return getBigDecimal(idx);
   	}

   	public void convert(ResultSet rset)throws SQLException{

   		ResultSetMetaData rsmd = rset.getMetaData();

   		int columnCount = rsmd.getColumnCount();
   		
   		for(int i=0; i < columnCount ; i++){
   			try{
   				String cName = rsmd.getColumnLabel(i+1);
   				MapFieldName.put(cName.toUpperCase(), new Integer(i+1));
   				MapFieldIndex.put(new Integer(i+1),cName.toUpperCase());
   			}catch(NullPointerException ne){
   				MapFieldName.put(new Integer(i+1), new Integer(i+1));
   			}
   		}
   		
   		try{
   			if(rset!=null){
   				while(rset.next()){
   				 ArrayList row = new ArrayList();
   					for(int i=0; i<columnCount ; i++ ){
   						row.add(rset.getObject(i+1));
   					}
   					add(row);
   				}
   			}
   		}catch(Exception e){

   		}

   	}

   	public void convert(ArrayList list) {

   		ListIterator iterator = list.listIterator();
   		boolean setMapFieldNameFlag = false;

   		try{
   			
			while(iterator.hasNext()){
				
				HashMap map = (HashMap)iterator.next();

				//필드명과 필드인덱스 생성
	   			if(!setMapFieldNameFlag){   				
	   				Iterator keyIterator = map.keySet().iterator();
	   				int i=0;
	   				while( keyIterator.hasNext() ){
	   		   			try{
	   		   				String cName = (String)keyIterator.next();
	   		   				MapFieldName.put(cName.toUpperCase(), new Integer(i+1));
	   		   				MapFieldIndex.put(new Integer(i+1),cName.toUpperCase());
	   		   			}catch(NullPointerException ne){
	   		   				MapFieldName.put(new Integer(i+1), new Integer(i+1));
	   		   			}
	   					i++;
	   				}	
	   				setMapFieldNameFlag = true;
	   			}	
	   			
	   			//데이터를 row단위로 분리하여 저장
	   			ArrayList row = new ArrayList();
				
				for(int i=0;i<map.size();i++) {
					row.add(map.get(MapFieldIndex.get(new Integer(i+1))));
				}
				add(row);
			}
   		}catch(Exception e){

   		}

   	}   	
   	protected Iterator itor = null;
   	protected ArrayList currentRow = new ArrayList();

	public String E_RETCODE = "4";
   	
   	public String getE_RETCODE() {
		return E_RETCODE;
	}

	public void setE_RETCODE(String eRETCODE) {
		E_RETCODE = eRETCODE;
	}

	public void refresh(){
   		try{
   			itor = iterator();
   		}catch(Exception e){
   		}
   	}
   	
   	public boolean next(){
   		if(itor == null){
   			itor = iterator();
   		}
   		if(itor.hasNext()){
   			currentRow = (ArrayList)itor.next();
   			return true;
   			
   		}
   		return false;
   	}



}
