package com.optidpp.main.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.anyframe.query.QueryServiceException;
import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("SaveService")
public class SaveService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 내용 저장
	 * @param headerMap
	 *
	 * @param paramMap
	 * @param detail1
	 * @param detail2
	 * @return List
	 * @throws Exception
	 */
	public int savePenABWork(Map<String, Object> convertMap, Map<String, Object> paramMap, HashMap<String, Object> detail1, HashMap<String, Object> detail2)  throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("SaveService.savePenABWork Start");
		System.out.println("===============123");
		System.out.println(detail2.toString());
		
		int cnt=0;
		if( ((List<?>)convertMap.get("mergeRows")).size() > 0){
			cnt += dao.create("savePenWork", convertMap);
			cnt ++;
		}
			
		
		cnt += dao.create("saveChk", paramMap);
		cnt ++;
		cnt += dao.create("savePoc", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_MQC_COIL_MST", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_MQC_COIL_DETAIL1", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_MQC_COIL_DETAIL2", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_MQC_COIL_DETAIL3", paramMap);
		cnt ++;
		cnt += dao.create("BOL_QA_MQC_COIL_MST", paramMap);
		cnt ++;
		cnt += dao.create("BOL_QA_MQC_COIL_DETAIL1", detail1);
		cnt ++;
		cnt += dao.create("BOL_QA_MQC_COIL_DETAIL2", detail2);
		cnt ++;
		cnt += dao.create("BOL_QA_MQC_COIL_DETAIL3", paramMap);
		cnt ++;
		return cnt;
	}
	public Map<?, ?> HB_QM_UFS_IF_INSPECTION(Map<String, Object> paramMap) throws QueryServiceException {
		// TODO Auto-generated method stub
		Log.Debug("saveservice.HB_QM_UFS_IF_INSPECTION Start");
		return  (Map) dao.excute("HB_QM_UFS_IF_INSPECTION", paramMap);
	}
	public int UP_MQC_COIL(HashMap hm) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("UP_QA_MQC_COIL_MST", hm);
		cnt ++;
		cnt += dao.create("UP_QA_MQC_COIL_DETAIL1", hm);
		cnt ++;
		cnt += dao.create("UP_QA_MQC_COIL_DETAIL2", hm);
		cnt ++;
		cnt += dao.create("UP_QA_MQC_COIL_DETAIL3", hm);
		cnt ++;
		return cnt;
	}
	public List<?> SELECT_MST(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_MST", paramMap);
	}
	public List<?> SELECT_DETAIL1(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_DETAIL1", paramMap);
	}
	public List<?> SELECT_DETAIL2(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_DETAIL2", paramMap);
	}
	public List<?> SELECT_DETAIL3(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_DETAIL3", paramMap);
	}
	public int savePenCWork(Map<String, Object> convertMap, Map<String, Object> paramMap, HashMap<String, Object> detail1, HashMap<String, Object> detail2) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("SaveService.savePenCWork Start");
		//System.out.println(paramMap.toString());
		int cnt=0;
		if( ((List<?>)convertMap.get("mergeRows")).size() > 0){
			cnt += dao.create("savePenWork", convertMap);
			cnt ++;
		}
		cnt += dao.create("saveChk", paramMap);
		cnt ++;
		cnt += dao.create("savePoc", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_LQC_COIL_RESULTS", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_LQC_COIL_DETAIL1", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_LQC_COIL_DETAIL2", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_LQC_COIL_DETAIL3", paramMap);
		cnt ++;
		cnt += dao.create("BOL_QA_LQC_COIL_RESULTS", paramMap);
		cnt ++;
		cnt += dao.create("BOL_QA_LQC_COIL_DETAIL1", detail1);
		cnt ++;
		cnt += dao.create("BOL_QA_LQC_COIL_DETAIL2", detail2);
		cnt ++;
		cnt += dao.create("BOL_QA_LQC_COIL_DETAIL3", paramMap);
		cnt ++;
		
		return cnt;
	}
	public int savePenDWork(Map<String, Object> convertMap,
			Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("SaveService.savePenDWork Start");
		//System.out.println(paramMap.toString());
		int cnt=0;
		if( ((List<?>)convertMap.get("mergeRows")).size() > 0){
			cnt += dao.create("savePenWork", convertMap);
			cnt ++;
		}
		cnt += dao.create("saveChk", paramMap);
		cnt ++;
		cnt += dao.create("savePoc", paramMap);
		cnt ++;
		cnt += dao.create("DEL_QA_ME_COIL_RESULTS", paramMap);
		cnt ++;
		cnt += dao.create("BOL_QA_ME_COIL_RESULTS", paramMap);
		cnt ++;
		return cnt;
	}
	public List<?> SELECT_LQC(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub 
		return (List<?>)dao.getList("SELECT_LQC1", paramMap);
		
	}
	
	public List<?> SELECT_LQC_DETAIL1(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_LQC_DETAIL1", paramMap);
	}
	public List<?> SELECT_LQC_DETAIL2(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_LQC_DETAIL2", paramMap);
	}
	public List<?> SELECT_LQC_DETAIL3(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_LQC_DETAIL3", paramMap);
	}
	
	public int UP_QA_LQC_COIL_RESULTS(HashMap hm) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("UP_QA_LQC_COIL_RESULTS", hm);
		cnt ++;
		cnt += dao.create("UP_QA_LQC_COIL_DETAIL1", hm);
		cnt ++;
		cnt += dao.create("UP_QA_LQC_COIL_DETAIL2", hm);
		cnt ++;
		cnt += dao.create("UP_QA_LQC_COIL_DETAIL3", hm);
		cnt ++;
		return cnt;
	}
	public List<?> SELECT_ME(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return (List<?>)dao.getList("SELECT_ME", paramMap);
	}
	public int UP_QA_ME_COIL_RESULTS(HashMap hm) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("UP_QA_ME_COIL_RESULTS", hm);
		cnt ++;
		return cnt;
	}
	public int unFixInspect_TDPP300(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("unFixInspect_TDPP300", paramMap);
		return cnt;
	}
	public int unFixInspect_QICS100(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("unFixInspect_QICS100", paramMap);
		return cnt;
	}
	public int fixInspect_TDPP300(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("fixInspect_TDPP300", paramMap);
		return cnt;
	}
	public int fixInspect_QICS100(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("fixInspect_QICS100", paramMap);
		return cnt;
	}
	
	public int saveJudg(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("SaveService.saveJudg Start");
		//System.out.println(paramMap.toString());
		int cnt=0;
		cnt += dao.create("saveJudg", paramMap);
		
		return cnt;
	}
}
