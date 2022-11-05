package com.optidpp.main.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.optidpp.common.logger.Log;
import com.optidpp.main.code.CodeService;


/**
 *  내용저장
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Save.do")
public class SaveController {
	@Inject
	@Named("SaveService")
	private SaveService saveservice;

	@Inject
	@Named("CodeService")
	private CodeService codeService;
	/**
	 * 내용 저장
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=dataSave")
	public ModelAndView setDataSave(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("SaveController.setDataSave Start");

		//System.out.println("paramMap = "+paramMap.toString());
		String message = "";
		Map<String, Object> convertMap = new HashMap<String, Object> ();
		Map<String, Object> headerMap = new HashMap<String, Object> ();
		List  mergeRows		= new ArrayList ();
		Iterator keyIterator = paramMap.keySet().iterator();
		HashMap mp = new HashMap();
		HashMap<String, Object> detail1 	= new HashMap<String, Object>();
		HashMap<String, Object> detail2 	= new HashMap<String, Object>();
		String SEQ_Q100 =paramMap.get("SEQ_Q100").toString();
		String SEQ_T300 =paramMap.get("SEQ_T300").toString();
		//System.out.println(SEQ_Q100 + " : " +SEQ_T300);
		try {


			while( keyIterator.hasNext() ){
				String name = (String)keyIterator.next();
				String value = paramMap.get(name).toString();
				if(name.indexOf("C_") > -1){
					mp = new HashMap();
					mp.put("SEQ_T300",SEQ_T300);
					mp.put("SEQ_Q100",SEQ_Q100);
					mp.put("FORM_SEQ",paramMap.get("FORM_SEQ"));
					mp.put("PAGE_ORDER",paramMap.get("PAGE_ORDER"));
					mp.put("FIELD_NAME",name);
					if("C_CHK_NO".equals(name)){
						paramMap.put(name,value);
					}
					if("C_CHKER".equals(name)){
						paramMap.put(name,value);
					}
					//System.out.println(" name  = "+name +" value = " + value);
					mp.put("USER_LAST_VALUE",value);
					mergeRows.add(mp);
				}

			}
			convertMap.put( "mergeRows" , mergeRows );
			Map<?,?> HB_QM_UFS_IF_INSPECTIONMap=saveservice.HB_QM_UFS_IF_INSPECTION(paramMap);
			Iterator keyIter = HB_QM_UFS_IF_INSPECTIONMap.keySet().iterator();
			while( keyIter.hasNext() ){
				String name = (String)keyIter.next();
				String value = HB_QM_UFS_IF_INSPECTIONMap.get(name).toString();
				paramMap.put(name,value);
			}
			Map <?,?> steelMap = codeService.getSteelDescMap(paramMap);
			String steelDesc ="";
			if(steelMap == null){

			}else{
				if(steelMap.containsKey("ATTRIBUTE2")){
					steelDesc = steelMap.get("ATTRIBUTE2").toString();

					//System.out.println(" steelDesc = "+ steelDesc);
				}
			}
			paramMap.put("C_STP_NM",steelDesc);
			//System.out.println(paramMap.get("ERROR_CODE"));
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(paramMap.get("ERROR_CODE").toString());
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray codeSum= new JSONArray();
			JSONArray codeType= new JSONArray();
			JSONArray collingSE= new JSONArray();

			for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				if("codeType".equals(key)){
					codeType = (JSONArray) jsonObject.get(key);
				}else if("codeSum".equals(key)){
					codeSum =  (JSONArray) jsonObject.get(key);
				}else if("collingSE".equals(key)){
					collingSE =  (JSONArray) jsonObject.get(key);
				}
			}
			List   insertRows		= new ArrayList();
			HashMap dataMap = new HashMap();

			for (int i = 0; i < codeSum.size(); i++) {
				JSONObject cS = (JSONObject) codeSum.get(i);
				paramMap.put("SC" ,cS.get("SC"));
				paramMap.put("SD" ,cS.get("SD"));
				paramMap.put("SK" ,cS.get("SK"));
				paramMap.put("ST" ,cS.get("ST"));
				paramMap.put("SCP" ,cS.get("SCP"));
				paramMap.put("SDP" ,cS.get("SDP"));
				paramMap.put("SKP" ,cS.get("SKP"));
				paramMap.put("STP" ,cS.get("STP"));
				paramMap.put("RC" ,cS.get("RC"));
				paramMap.put("RD" ,cS.get("RD"));
				paramMap.put("RK" ,cS.get("RK"));
				paramMap.put("RT" ,cS.get("RT"));
				paramMap.put("RCP" ,cS.get("RCP"));
				paramMap.put("RDP" ,cS.get("RDP"));
				paramMap.put("RKP" ,cS.get("RKP"));
				paramMap.put("RTP" ,cS.get("RTP"));
				//System.out.println("codeSum = "+paramMap);
			}
			//paramMap = (Map<String, Object>) HB_QM_UFS_IF_INSPECTIONMap;

			for (int i = 0; i < codeType.size(); i++) {
				JSONObject cT = (JSONObject) codeType.get(i);
				dataMap = new HashMap();
				dataMap.put("COLLECTION_ID","");
				dataMap.put("SEQ_Q100",SEQ_Q100);
				dataMap.put("SEQ_T300",SEQ_T300);
				dataMap.put("code" ,cT.get("code"));
				dataMap.put("SB" ,cT.get("SB"));
				dataMap.put("SC" ,cT.get("SC"));
				dataMap.put("SD" ,cT.get("SD"));
				dataMap.put("SK" ,cT.get("SK"));
				dataMap.put("RB" ,cT.get("RB"));
				dataMap.put("RC" ,cT.get("RC"));
				dataMap.put("RD" ,cT.get("RD"));
				dataMap.put("RK" ,cT.get("RK"));
				//System.out.println("codeType = "+dataMap);
				insertRows.add(dataMap);
			}
			detail1.put( "insertRows" , insertRows );
			insertRows		= new ArrayList();
			dataMap = new HashMap();
			for (int i = 0; i < collingSE.size(); i++) {
				dataMap = new HashMap();
				JSONObject cT = (JSONObject) collingSE.get(i);
				dataMap.put("COLLECTION_ID","");
				dataMap.put("SEQ_Q100",SEQ_Q100);
				dataMap.put("SEQ_T300",SEQ_T300);
				dataMap.put("M_START" ,cT.get("M_START"));
				dataMap.put("M_END" ,cT.get("M_END"));
				dataMap.put("COLLECT_TYPE" ,cT.get("COLLECT_TYPE"));
				//System.out.println("collingSE = "+dataMap);
				insertRows.add(dataMap);
			}
			detail2.put( "insertRows" , insertRows );
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int resultCnt = -1;
		try{
			if("FORM_QICS_A".equals(paramMap.get("FORM_CODE")) || "FORM_QICS_B".equals(paramMap.get("FORM_CODE"))){
				resultCnt =  saveservice.savePenABWork(convertMap,paramMap,detail1,detail2);
			}else if("FORM_QICS_C".equals(paramMap.get("FORM_CODE"))  ||  "FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
				if("FORM_QICS_C".equals(paramMap.get("FORM_CODE"))){
					paramMap.put("MILLEDGE_GBN","N");
					paramMap.put("PRESENCE","N");
					paramMap.put("SPECIAL_APPOINTMENT","N");
					paramMap.put("GOLD_DUST","N");
				}else if ("FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
					paramMap.put("MILLEDGE_GBN","Y");
					paramMap.put("PRESENCE","N");
					paramMap.put("SPECIAL_APPOINTMENT","N");
					paramMap.put("GOLD_DUST","N");
				}
				resultCnt =  saveservice.savePenCWork(convertMap,paramMap,detail1,detail2);
				//	}else if("FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
				//		resultCnt =  saveservice.savePenDWork(convertMap,paramMap);
			}else{
			}

			if(resultCnt > 0){
				message="저장 되었습니다.";
			}else{
				message="저장 할 내용이 없습니다.";
			}
		}catch(Exception e){
			resultCnt = -1; message="저장에 실패 하였습니다.";
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", message);
		Log.Debug("SaveController.setDataSave End");

		return mv;
	}
	
	/**
	 * 최종검사 상태 변경(erp 전송완료 > 검사중, 검사대기 상태로 변경)
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=unFixInspect")
	public ModelAndView setUnFixInspect(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("SaveController.setUnFixInspect Start");
		//System.out.println(paramMap);
		String message = "";
		int resultCnt = -1;
		try{
			resultCnt = saveservice.unFixInspect_TDPP300(paramMap);
			if(resultCnt > 0){
				message="TDPP300의 상태 변경이 성공 하였습니다.(전송완료 > 검사중)";
			}else{
				message="TDPP300의 상태 변경이 실패 하였습니다.";
			}
			resultCnt = saveservice.unFixInspect_QICS100(paramMap);
			if(resultCnt > 0){
				message="QICS100의 상태 변경이 성공 하였습니다.(전송완료 > 검사중)";
			}else{
				message="QICS100의 상태 변경이 실패 하였습니다.";
			}
		}catch(Exception e){
			resultCnt = -1; message="상태변경을 실패 하였습니다.";
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", message);
		Log.Debug("SaveController.setUnFixInspect End");

		return mv;
	}
	
	/**
	 * 최종검사 상태 변경(erp 전송완료 > 검사중, 검사대기 상태로 변경)
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=fixInspect")
	public ModelAndView setFixInspect(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("SaveController.setFixInspect Start");
		//System.out.println(paramMap);
		String message = "";
		int resultCnt = -1;
		try{
			resultCnt = saveservice.fixInspect_TDPP300(paramMap);
			if(resultCnt > 0){
				message="TDPP300의 상태 변경이 성공 하였습니다.(검사중 > 전송완료)";
			}else{
				message="TDPP300의 상태 변경이 실패 하였습니다.";
			}
			resultCnt = saveservice.fixInspect_QICS100(paramMap);
			if(resultCnt > 0){
				message="QICS100의 상태 변경이 성공 하였습니다.(검사중 > 전송완료)";
			}else{
				message="QICS100의 상태 변경이 실패 하였습니다.";
			}
		}catch(Exception e){
			resultCnt = -1; message="상태변경을 실패 하였습니다.";
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", message);
		Log.Debug("SaveController.setFixInspect End");

		return mv;
	}
	
	@RequestMapping(params = "cmd=saveJudg")
	public ModelAndView setSaveJudg(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("SaveController.setSaveJudg Start");
		String message = "";
		
		int resultCnt =  saveservice.saveJudg(paramMap);
		
		if(resultCnt > 0){
			message="저장 되었습니다.";
		}else{
			message="저장 할 내용이 없습니다.";
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", message);
		Log.Debug("SaveController.setSaveJudg End");

		return mv;
	}
	
}
