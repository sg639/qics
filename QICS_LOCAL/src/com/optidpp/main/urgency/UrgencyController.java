package com.optidpp.main.urgency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.optidpp.common.logger.Log;
import com.optidpp.main.add.AddService;
import com.optidpp.qzfc.RFCInterFace;


/**
 *  긴급작업
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Urgency.do")
public class UrgencyController {
	@Inject
	@Named("UrgencyService")
	private UrgencyService urgencyService;

	@Inject
	@Named("AddService")
	private AddService addService;
	/**
	 * 메인 긴급작업팝업
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=urgencyPopup")
	public String getUrgencyPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("UrgencyController.getUrgencyPopup Start");
		return "/main/popup/urgency";
	}
	/**
	 * 메인 긴급작업팝업 Info
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=saveUrgency")
	public ModelAndView saveUrgency(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("UrgencyController.saveUrgency Start");
		String Message = "";
		Map<?,?> seqMap=addService.getSeqQ100(paramMap);
		String SEQ_Q100 = seqMap.get("SEQ_Q100").toString();
		paramMap.put("SEQ_Q100",SEQ_Q100);
		paramMap.put("authIP",request.getRemoteAddr());
		//System.out.println("paramMap = " +paramMap);

		List list = new ArrayList();
		list =  addService.getCheckList(paramMap);
		String existPocNo="";
		int errorCount=0;
		boolean exist =false;
		for (int j = 0; j < list.size(); j++) {
			HashMap checkMap = new HashMap();
			checkMap = (HashMap) list.get(j);
			String exInLine = checkMap.get("IN_LINE").toString();
			String exPocNo = checkMap.get("POC_NO").toString();
			String exWorkDate=checkMap.get("WORK_DATE").toString();
			String exErpUploadYn=checkMap.get("ERP_UPLOAD_YN").toString();

			String nwInLine = paramMap.get("IN_LINE").toString();
			String nwPocNo = paramMap.get("POC_NO").toString();
			String nwWorkDate = paramMap.get("WORK_DATE").toString();
			if(exInLine.equals(nwInLine) && exPocNo.equals(nwPocNo) && exWorkDate.equals(nwWorkDate)){
				if("Y".equals(exErpUploadYn)){

				}else{
					exist = true;
					existPocNo = existPocNo+nwPocNo+",";
					errorCount++;
				}
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");

		if(exist){
			existPocNo =existPocNo.substring(0, existPocNo.lastIndexOf(","));
			Message=existPocNo;
			mv.addObject("Message", Message);
		}else{
			if("M".equals(paramMap.get("FORM_TYPE"))){

			}else{
				paramMap.put("PRE_POC_NO",paramMap.get("POC_NO"));
			}
			RFCInterFace app = new RFCInterFace();
			Map<?,?> ifMap = app.commonInfo(paramMap);
			//System.out.println("ifMap = "+ifMap);
			if(ifMap.isEmpty() || ifMap == null){
				paramMap.put("MRG_WIP_ENTITY_NAME","");
				paramMap.put("MRG_R_SUPPLIER","");
				paramMap.put("MRG_R_SUPPLY_THICKNESS","");
				paramMap.put("MRG_STEEL_TYPE","");
				paramMap.put("MRG_R_WIDTH","");
			}else{
				paramMap.put("MRG_WIP_ENTITY_NAME",ifMap.get("WIP_ENTITY_NAME"));
				paramMap.put("MRG_R_SUPPLIER",ifMap.get("R_SUPPLIER"));
				paramMap.put("MRG_R_SUPPLY_THICKNESS",ifMap.get("R_SUPPLY_THICKNESS"));
				paramMap.put("MRG_STEEL_TYPE",ifMap.get("STEEL_TYPE"));
				paramMap.put("MRG_R_WIDTH",ifMap.get("LAST_WIDTH"));
			}
			int resultCnt = -1;
			try{
				resultCnt =  urgencyService.saveUrgency(paramMap);
				if(resultCnt > 0){
					Message="저장 되었습니다.";
				}else{
					Message="저장 할 내용이 없습니다.";
				}
			}catch(Exception e){
				resultCnt = -1; Message="저장에 실패 하였습니다.";
			}


			mv.addObject("Message", Message);
			if(resultCnt  > 0){
				mv.addObject("SEQ_Q100", SEQ_Q100);
			}

			Log.Debug("UrgencyController.saveUrgency End");
		}

		return mv;
	}

}
