package com.optidpp.main.penwork;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("PenWorkService")
public class PenWorkService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 전자펜작업목록 조회
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getPenWorkList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPenWorkList Start");
		return (List<?>)dao.getList("getPenWorkList", paramMap);
	}
	/**
	 * 전자펜작업 Info
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getPenWorkInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPenWorkInfo Start");
		return dao.getMap("getPenWorkInfo", paramMap);
	}

	/**
	 * 전자펜작업 View
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getPenWorkView(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPenWorkView Start");
		return (List<?>)dao.getList("getPenWorkView", paramMap);
	}
	/**
	 * Form Seq 가져오기
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getFormSeq(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getFormSeq Start");
		return dao.getMap("getFormSeq", paramMap);
	}
	public List getPadInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPadInfo Start");
		return (List<?>)dao.getList("getPadInfo", paramMap);
	}
	public List getPadDynamic(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPadDynamic Start");
		//System.out.println("paramMap.get('FORM_CODE')"+paramMap.get("FORM_CODE"));
		return (List<?>)dao.getList("getPadDynamic", paramMap);
	}
	public Map<?, ?> getPadMaster(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPadMaster Start");
		return dao.getMap("getPadMaster", paramMap);
	}
	public Map<?, ?> getSeqT300(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getSeqT300 Start");
		//int cnt = dao.update("getSeq", paramMap);
		paramMap.put("inVal","2");
		//paramMap= (Map) dao.excute("getSeq", paramMap);
		//System.out.println("temp = " + paramMap.toString());
		//return paramMap;
		return (Map) dao.excute("getSeqT300", paramMap);
	}
	public Map<?, ?> getSeqQ100(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getSeqQ100 Start");
		paramMap.put("inVal","2");
		return (Map) dao.excute("getSeqQ100", paramMap);
	}
	public Map<?, ?> getSeqTemp(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getSeqTemp Start");
		paramMap.put("inVal","2");
		return (Map) dao.excute("getSeqTemp", paramMap);
	}
	public List getBgImage(Map<?, ?> tdpp200Map) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getBgImage Start");
		return (List<?>)dao.getList("getBgImage", tdpp200Map);
	}
	public List getSalesEndUser(Map<?, ?> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getSalesEndUser Start");
		return (List<?>)dao.getList("getSalesEndUser", paramMap);
	}
	public int setQICS203(Map<?, ?> convertMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setQICS203 Start");
		//System.out.println(convertMap.toString());
		int cnt=0;
		if( ((List<?>)convertMap.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS203", convertMap);
			cnt ++;
		}
		return cnt;
	}
	public int setQICS200(Map<String, Object> qics200Map) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setQICS200 Start");
		int cnt=0;
		cnt += dao.create("setQICS200", qics200Map);
		cnt ++;
		return cnt;
	}
	public int setQICS205(Map convertMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setQICS205 Start");
		//System.out.println(convertMap.toString());
		int cnt=0;
		if( ((List<?>)convertMap.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS205", convertMap);
			cnt ++;
		}
		return cnt;
	}
	public List getFieldInfo(Map<?, ?> tdpp200Map) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getFieldInfo Start");
		return (List<?>)dao.getList("getFieldInfo", tdpp200Map);
	}
	public int setTDPP300(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setTDPP300 Start");
		int cnt=0;
		cnt += dao.create("setTDPP300", paramMap);
		cnt ++;
		return cnt;
	}
	public int setQICS213(Map convertMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setQICS213 Start");
		//System.out.println(convertMap.toString());
		int cnt=0;
		if( ((List<?>)convertMap.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS213", convertMap);
			cnt ++;
		}
		return cnt;
	}
	public int setQICS000(Map<String, Object> qics000Map) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setQICS000 Start");
		int cnt=0;
		cnt += dao.create("setQICS000", qics000Map);
		cnt ++;
		return cnt;
	}
	public int setFinalQICS100(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setFinalQICS100 Start");
		int cnt=0;
		cnt += dao.create("setFinalQICS100", paramMap);
		cnt ++;
		return cnt;
	}
	public int setTotalInsert(Map<String, Object> qics200Map, Map qics203Map,
			Map qics205Map, Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setTotalInsert Start");
		int cnt=0;
//		cnt += dao.create("setQICS200", qics200Map);
//		cnt ++;
//		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
//			cnt += dao.create("setQICS203", qics203Map);
//			cnt ++;
//		}
//		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
//			cnt += dao.create("setQICS205", qics205Map);
//			cnt ++;
//		}
//		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
//			cnt += dao.create("setQICSPod213", qics205Map);
//			cnt ++;
//		}
//		cnt += dao.create("setQICS000", paramMap);
//		cnt ++;
//		cnt += dao.create("setTDPP300", paramMap);
//		cnt ++;
		cnt += dao.create("setFinalQICS100", paramMap);
		cnt ++;
		return cnt;
	}
	public int setPrintM(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setPrintM Start");
		int cnt=0;
		cnt += dao.create("setPrintM", paramMap);
		cnt ++;
		cnt += dao.create("addQcMapping2", paramMap);
		cnt ++;
		return cnt;
	}
	public int setPrintF(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setPrintF Start");
		int cnt=0;
		cnt += dao.create("setPrintF", paramMap);
		cnt ++;
		return cnt;
	}
	public int setPrintF2(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setPrintF2 Start");
		int cnt=0;
		cnt += dao.create("setPrintF2", paramMap);
		cnt ++;
		return cnt;
	}
	public int setTotalWebInsert(Map<String, Object> qics200Map,
			Map qics203Map, Map qics205Map, Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setTotalWebInsert Start");
		int cnt=0;
		cnt += dao.create("setQICS200", qics200Map);
		cnt ++;
		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS203", qics203Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS205", qics205Map);
			cnt ++;
		}
		cnt += dao.create("setTDPP207", paramMap);
		cnt ++;
		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
			cnt += dao.create("setTDPP211", qics203Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setTDPP213", qics205Map);
			cnt ++;
		}

		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICSWeb213", qics205Map);
			cnt ++;
		}
		cnt += dao.create("setTDPP300", paramMap);
		cnt ++;
		cnt += dao.create("setFinalQICS100", paramMap);
		cnt ++;
		return cnt;
	}
	public int setTotalFinalInsert(Map<String, Object> qics200Map,
			Map qics203Map, Map qics205Map, Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setTotalFinalInsert Start");
		int cnt=0;
		cnt += dao.create("setQICS100", paramMap);
		cnt ++;
		cnt += dao.create("setQICS200", qics200Map);
		cnt ++;
		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS203", qics203Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS205", qics205Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICSPod213", qics205Map);
			cnt ++;
		}
		cnt += dao.create("setQICS000", paramMap);
		cnt ++;
		cnt += dao.create("setTDPP300", paramMap);
		cnt ++;
		cnt += dao.create("setFinalQICS100", paramMap);
		cnt ++;
		if("Y".equals(paramMap.get("NEW_FINAL"))) {
			cnt += dao.create("delFinalQICS100", paramMap);
			cnt ++;
		}
		return cnt;
	}
	public int setTotalFinalWebInsert(Map<String, Object> qics200Map,
			Map qics203Map, Map qics205Map, Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.setTotalWebInsert Start");
		int cnt=0;
		cnt += dao.create("setQICS100", paramMap);
		cnt ++;
		cnt += dao.create("setQICS200", qics200Map);
		cnt ++;
		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS203", qics203Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS205", qics205Map);
			cnt ++;
		}
		cnt += dao.create("setTDPP207", paramMap);
		cnt ++;
		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
			cnt += dao.create("setTDPP211", qics203Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setTDPP213", qics205Map);
			cnt ++;
		}

		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICSWeb213", qics205Map);
			cnt ++;
		}
		cnt += dao.create("setTDPP300", paramMap);
		cnt ++;
		cnt += dao.create("setFinalQICS100", paramMap);
		cnt ++;
		if("Y".equals(paramMap.get("NEW_FINAL"))) {
			cnt += dao.create("delFinalQICS100", paramMap);
			cnt ++;
		}

		return cnt;
	}
	public int setReportInsert(Map qics203Map, Map qics205Map,
			Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		if( ((List<?>)qics203Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS203", qics203Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICS205", qics205Map);
			cnt ++;
		}
		if( ((List<?>)qics205Map.get("insertRows")).size() > 0){
			cnt += dao.create("setQICSPod213", qics205Map);
			cnt ++;
		}
		cnt += dao.create("setQICS000", paramMap);
		cnt ++;
		cnt += dao.create("setTDPP300", paramMap);
		cnt ++;
		cnt += dao.create("setTDPP400", paramMap);
		cnt ++;
		return cnt;
	}
	public Map<?, ?> getPenWorkFinalInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("PenWorkService.getPenWorkInfo Start");
		return dao.getMap("getPenWorkFinalInfo", paramMap);
	}
}
