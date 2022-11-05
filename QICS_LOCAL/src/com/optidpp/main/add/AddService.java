package com.optidpp.main.add;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.anyframe.query.QueryServiceException;
import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;
import com.optidpp.podservice.common.util.DBUtil;
import com.optidpp.podservice.db.dto.CommonCollect;

@Service("AddService")
public class AddService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 작업추가 List
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getAddList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.getAddList Start");
		return (List<?>)dao.getList("getAddList", paramMap);
	}
	public int addInsert(Map<String, Object> convertMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.addInsert Start");
		//System.out.println(convertMap.toString());
		int cnt=0;
		if( ((List<?>)convertMap.get("insertRows")).size() > 0){
			cnt += dao.create("addInsert", convertMap);
			cnt ++;
		}
		return cnt;
	}
	public Map<?, ?> getSeqQ100(Map<String, Object> paramMap) throws QueryServiceException {
		// TODO Auto-generated method stub
		Log.Debug("AddService.getSeqQ100 Start1");
		paramMap.put("inVal","2");
		return (Map) dao.excute("getSeqQ100", paramMap);
	}
	public Map<?, ?> getSeqTemp(Map<String, Object> paramMap) throws QueryServiceException {
		// TODO Auto-generated method stub
		Log.Debug("AddService.getSeqTemp Start2");
		paramMap.put("inVal","2");
		return (Map) dao.excute("getSeqTemp", paramMap);
	}
	public List getCheckList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.getCheckList Start");
		return (List<?>)dao.getList("getCheckList", paramMap);
	}
	
	public int saveTempViewToQics100(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.saveTempViewToQics100 Start");
		DBUtil dbu = null;
		try {
			dbu = new DBUtil();
		} catch(Exception ex){
			Log.Error(ex.toString());
		}
		String sql	=	" SELECT *  \r\n" + 
							" FROM VIEW_QCM_WIP_LIST \r\n" + 
							" WHERE OPER_CODE='" + paramMap.get("IN_LINE") + "'";
		
		CommonCollect cc = dbu.getTableData(sql, "view");
		
		//dao.delete("deleteTempQics", paramMap);
		
		List list = new ArrayList();
		int idx = 0;
		
		if(cc != null) {
			while (cc.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i=0;i<cc.getColumnCount();i++) {
					map.put(cc.getColumnName((i+1)), cc.getString(cc.getColumnName((i+1))));
					System.out.println(cc.getColumnName((i+1)) + " : " +  cc.getString(cc.getColumnName((i+1))));
				}
				Map<?,?> seqMap= getSeqTemp(paramMap);
				String SEQ_Temp = seqMap.get("SEQ_TEMP").toString();
				map.put("SEQ_TEMP",SEQ_Temp);
				list.add(idx, map);
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println(list);
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				idx++;
			}
			
			int cnt=0;
			
			if(list.size() > 0) {
				for(int i=0; i<list.size(); i++) {
					cnt += dao.create("saveTempViewToQics100", (HashMap<String, String>)list.get(i));
				}
			}
		}
		
		/*
		List<HashMap<String, String>> tempQics100List = (List<HashMap<String, String>>)dao.getList("getTempQics100List", paramMap);

		for(HashMap<String, String> map : tempQics100List){
			Map<String, Object> pMap	= new HashMap<String, Object>();
			
			paramMap.put("inVal","2");
			Map<?,?> seqMap = (Map) dao.excute("getSeqQ100", paramMap);
			String seq	= seqMap.get("SEQ_Q100").toString();
			
			pMap.put("SEQ_Q100", seq);
			pMap.put("IN_FACT", map.get("FACTORY_CODE"));
			pMap.put("IN_LINE", map.get("OPER_CODE"));
			pMap.put("POC_NO", map.get("POC_NO"));
			pMap.put("MRG_DISCRETE_NUMBER", map.get("ORDER_NO"));
			pMap.put("MRG_LINE_CODE", map.get("OPER_CODE"));
			pMap.put("MRG_STEEL_TYPE", map.get("STEEL_TYPE"));
			pMap.put("MRG_R_SUPPLIER", map.get("RAW_VENDOR_DESC"));
			pMap.put("MRG_R_SUPPLY_THICKNESS", map.get("DEMAND_THICK"));
			pMap.put("MRG_R_WIDTH", map.get("DEMAND_WIDTH"));
			pMap.put("PARTIAL_WEIGHT", map.get("ORD_QTY"));
			pMap.put("TARGET_THICKNESS", map.get("GOCHING_G"));
			pMap.put("AIM_THICKNESS", map.get("FINAL_AIM"));
			pMap.put("AIM_TOL", map.get("AIM_G"));
			pMap.put("JOB_DESCRIPTION", map.get("ORD_COMMENT"));
			pMap.put("WORK_DATE", paramMap.get("WORK_DATE"));
			
			dao.create("insertQics100", pMap);
		}
		*/
//		dao.delete("deleteTempQics", paramMap);
		
		return 1;
	}
	
	public List workList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.workList Start");
		return (List<?>)dao.getList("workList", paramMap);
	}
	
	public int addQcMapping(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.addQcMapping Start");
		int cnt=dao.create("addQcMapping", paramMap);
		Log.Debug("AddService.addQcMapping End");
		return cnt;
	}
	public int addQcMapping2(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("AddService.addQcMapping2 Start");
		int cnt=dao.create("addQcMapping2", paramMap);
		Log.Debug("AddService.addQcMapping2 End");
		return cnt;
	}
}
