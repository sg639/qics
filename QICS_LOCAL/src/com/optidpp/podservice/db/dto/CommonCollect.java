package com.optidpp.podservice.db.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.optidpp.podservice.common.util.StringUtil;

public class CommonCollect extends ArrayList implements Serializable{
    
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
   		}
   		return StringUtil.getReplaceStr(result);
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

   	protected Iterator itor = null;
   	protected ArrayList currentRow = new ArrayList();
   	
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
