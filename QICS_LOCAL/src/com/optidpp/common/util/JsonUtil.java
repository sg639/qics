package com.optidpp.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.optidpp.common.logger.Log;
import com.optidpp.zrfc.ExportParameterDTO;
import com.optidpp.zrfc.RFCCommonCollect;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;



public class JsonUtil {
	
	public static Map<String, Object> dataToJson(ExportParameterDTO dto) throws JSONException {
		// TODO Auto-generated method stub
		RFCCommonCollect fieldCollect;
		RFCCommonCollect tableCollect = null;
		String Message = "";
		int resultCnt = -1;
		ArrayList result = new ArrayList();
		Map<String, Object> returnObj = new HashMap <String, Object> ();
		if(dto.isError()){
    		Message ="code = " +dto.getErrorCode()+ " Message = " +dto.getErrorMessage();
    	}else{
    		fieldCollect =dto.getExportMapData().get("FIELDS");
    		if (fieldCollect == null) {
    			Message ="SAP 접속에 실패 했습니다.";
    		}else if(fieldCollect.next()){
    			
			    tableCollect = dto.getExportMapData().get("O_TAB");
				if(tableCollect == null){
				 
				}else{
					result = new ArrayList();
					while(tableCollect.next()){
						
						Iterator itr = tableCollect.getMetaData().keySet().iterator();
						//Log.Debug(" key = " + tableCollect.getMetaData().keySet().toString());
						HashMap hm = new HashMap();
						while(itr.hasNext()){
							String key = (String) itr.next();
							String value = tableCollect.getString(key);
							hm.put(key, value);
						//	Log.Debug(" key = " + key+" value = " + value);
						}
						result.add(hm);
					}
				}
    			String e_return	=fieldCollect.getString("E_RETURN");
			
    			if("S".equals(e_return)){
					Message ="저장완료했습니다.";
					resultCnt = 1;
				}else{
					Message ="저장에 실패 하였습니다.";
				}
    			//Message = fieldCollect.getString("E_MSG");
    		}
    	}
		Log.Debug(" Message = " + Message);
		JSONArray jsonArray = JSONArray.fromObject(result);
		
		returnObj.put("Message", Message);
		returnObj.put("data", jsonArray);
		returnObj.put("Code", resultCnt);
		Log.Debug("jsonArray = " + jsonArray.toString());
		return returnObj;
	}

	public static Map<String, Object> dataToJson2(ExportParameterDTO dto,
			String tableNm) {
		RFCCommonCollect fieldCollect;
		RFCCommonCollect tableCollect = null;
		String Message = "";
		ArrayList result = new ArrayList();
		Map<String, Object> returnObj = new HashMap <String, Object> ();
		if(dto.isError()){
    		Message ="code = " +dto.getErrorCode()+ " Message = " +dto.getErrorMessage();
    	}else{
    		fieldCollect =dto.getExportMapData().get("FIELDS");
    		if (fieldCollect == null) {
    			Message ="SAP 접속에 실패 했습니다.";
    		}else if(fieldCollect.next()){
    			
			    tableCollect = dto.getExportMapData().get(tableNm);
				if(tableCollect == null){
				 
				}else{
					result = new ArrayList();
					while(tableCollect.next()){
						
						Iterator itr = tableCollect.getMetaData().keySet().iterator();
						HashMap hm = new HashMap();
						while(itr.hasNext()){
							String key = (String) itr.next();
							String value = tableCollect.getString(key);
							hm.put(key, value);
						}
						result.add(hm);
					}
				}
				String e_return	=fieldCollect.getString("E_RETURN");
				if("S".equals(e_return)){
					Message ="저장완료했습니다.";
				}else{
					Message ="저장에 실패 하였습니다.";
				}	

    		//	Message = fieldCollect.getString("E_MSG");
    		}
    	}
		Log.Debug(" Message = " + Message);
		JSONArray jsonArray = JSONArray.fromObject(result);
		
		returnObj.put("Message", Message);
		returnObj.put("data", jsonArray);
	
		Log.Debug("jsonArray = " + jsonArray.toString());
		return returnObj;
	}

	public static Map<String, Object> dataToMap(ExportParameterDTO dto,
			String tableNm, String I_INPUT) {
		// TODO Auto-generated method stub
		RFCCommonCollect fieldCollect;
		RFCCommonCollect tableCollect = null;
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		HashMap<String, String> map 		= null;
		
		List<Serializable>  mergeRows		= new ArrayList<Serializable>();
		List<Serializable>  insertRows		= new ArrayList<Serializable>();
		List<Serializable>  updateRows 		= new ArrayList<Serializable>();
		List<Serializable>  deleteRows 		= new ArrayList<Serializable>();
		
		if(dto.isError()){
			Log.Debug("code = " +dto.getErrorCode()+ " Message = " +dto.getErrorMessage());
    	}else{
    		fieldCollect =dto.getExportMapData().get("FIELDS");
    		if (fieldCollect == null) {
    			Log.Debug("SAP 접속에 실패 했습니다.");
    		}else if(fieldCollect.next()){
    			
			    tableCollect = dto.getExportMapData().get(tableNm);
				if(tableCollect == null){
					Log.Debug("테이블에 데이터가 없습니다.");
				}else{
					
					while(tableCollect.next()){
						map = new HashMap<String, String>();
						Iterator itr = tableCollect.getMetaData().keySet().iterator();
						//Log.Debug(" key = " + tableCollect.getMetaData().keySet().toString());
						while(itr.hasNext()){
							String key = (String) itr.next();
							String value = tableCollect.getString(key);
							map.put(key, value);
							//Log.Debug(" key = " + key+" value = " + value);
						}
						if( 	I_INPUT.equals("I") ){ insertRows.add(map); mergeRows.add(map);}
						else if(I_INPUT.equals("U") ){ updateRows.add(map); mergeRows.add(map);}
						else if(I_INPUT.equals("D") ){ deleteRows.add(map); }
					}
					
				}
    		}
    		returnMap.put( "mergeRows" , mergeRows );
			returnMap.put( "insertRows" , insertRows );
			returnMap.put( "updateRows" , updateRows );
			returnMap.put( "deleteRows" , deleteRows );
    	}
		return returnMap;
	}

	public static Map<String, Object> dataToMap2(ExportParameterDTO dto,
			RFCCommonCollect tableCollect, String I_INPUT) {
		// TODO Auto-generated method stub
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		HashMap<String, String> map 		= null;
		
		List<Serializable>  mergeRows		= new ArrayList<Serializable>();
		List<Serializable>  insertRows		= new ArrayList<Serializable>();
		List<Serializable>  updateRows 		= new ArrayList<Serializable>();
		List<Serializable>  deleteRows 		= new ArrayList<Serializable>();
		
		if(tableCollect == null){
			Log.Debug("테이블에 데이터가 없습니다.");
		}else{
			
			while(tableCollect.next()){
				map = new HashMap<String, String>();
				Iterator itr = tableCollect.getMetaData().keySet().iterator();
				/*Log.Debug(" key = " + tableCollect.getMetaData().keySet().toString());*/
				while(itr.hasNext()){
					String key = (String) itr.next();
					String value = tableCollect.getString(key);
					map.put(key, value);
					/*Log.Debug(" key = " + key+" value = " + value);*/
				}
				if( 	I_INPUT.equals("I") ){ insertRows.add(map); mergeRows.add(map);}
				else if(I_INPUT.equals("U") ){ updateRows.add(map); mergeRows.add(map);}
				else if(I_INPUT.equals("D") ){ deleteRows.add(map); }
			}
			returnMap.put( "mergeRows" , mergeRows );
			returnMap.put( "insertRows" , insertRows );
			returnMap.put( "updateRows" , updateRows );
			returnMap.put( "deleteRows" , deleteRows );
		}
		
		return returnMap;
	}

	public static Map<String, Object> dataToMapOnlyOK(ExportParameterDTO dto,
			RFCCommonCollect tableCollect, String I_INPUT) {
		// TODO Auto-generated method stub
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		HashMap<String, String> map 		= null;
		
		List<Serializable>  mergeRows		= new ArrayList<Serializable>();
		List<Serializable>  insertRows		= new ArrayList<Serializable>();
		List<Serializable>  updateRows 		= new ArrayList<Serializable>();
		List<Serializable>  deleteRows 		= new ArrayList<Serializable>();
		
		if(tableCollect == null){
			Log.Debug("테이블에 데이터가 없습니다.");
		}else{
			
			while(tableCollect.next()){
				if( "S".equals( tableCollect.getString("ZTYPE") ) ) {
					map = new HashMap<String, String>();
					Iterator itr = tableCollect.getMetaData().keySet().iterator();
					/*Log.Debug(" key = " + tableCollect.getMetaData().keySet().toString());*/
					while(itr.hasNext()){
						String key = (String) itr.next();
						String value = tableCollect.getString(key);
						map.put(key, value);
						/*Log.Debug(" key = " + key+" value = " + value);*/
					}
					if( 	I_INPUT.equals("I") ){ insertRows.add(map); mergeRows.add(map);}
					else if(I_INPUT.equals("U") ){ updateRows.add(map); mergeRows.add(map);}
					else if(I_INPUT.equals("D") ){ deleteRows.add(map); }
				}				
			}
			returnMap.put( "mergeRows" , mergeRows );
			returnMap.put( "insertRows" , insertRows );
			returnMap.put( "updateRows" , updateRows );
			returnMap.put( "deleteRows" , deleteRows );
		}
		
		return returnMap;
	}
	
	public static Map<String, Object> dataToMap3(ExportParameterDTO dto,
			RFCCommonCollect tableCollect, String I_INPUT) {
		// TODO Auto-generated method stub
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		HashMap<String, String> map 		= null;
		
		List<Serializable>  mergeRows		= new ArrayList<Serializable>();
		List<Serializable>  insertRows		= new ArrayList<Serializable>();
		List<Serializable>  updateRows 		= new ArrayList<Serializable>();
		List<Serializable>  deleteRows 		= new ArrayList<Serializable>();
		
		if(tableCollect == null){
			Log.Debug("테이블에 데이터가 없습니다.");
		}else{
			map = new HashMap<String, String>();
			while(tableCollect.next()){
				
				Iterator itr = tableCollect.getMetaData().keySet().iterator();
				Log.Debug(" key = " + tableCollect.getMetaData().keySet().toString());
				while(itr.hasNext()){
					String key = (String) itr.next();
					String value = tableCollect.getString(key);
					map.put(key, value);
					Log.Debug(" key = " + key+" value = " + value);
				}
				
			}
			if( 	I_INPUT.equals("I") ){ insertRows.add(map); mergeRows.add(map);}
			else if(I_INPUT.equals("U") ){ updateRows.add(map); mergeRows.add(map);}
			else if(I_INPUT.equals("D") ){ deleteRows.add(map); }
			returnMap.put( "mergeRows" , mergeRows );
			returnMap.put( "insertRows" , insertRows );
			returnMap.put( "updateRows" , updateRows );
			returnMap.put( "deleteRows" , deleteRows );
		}
		
		return returnMap;
	}

}
