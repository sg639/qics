package com.optidpp.main.add;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.optidpp.common.util.ParamUtils;
import com.optidpp.qzfc.RFCInterFace;


/**
 *  작업추가
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Add.do")
public class AddController {
	@Inject
	@Named("AddService")
	private AddService addService;

	/**
	 * 메인 작업추가팝업
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=addPopup")
	public String getAddPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("AddController.getAddPopup Start");
		Log.Debug("AddController.getAddPopup End");
		return "/main/popup/add";
	}
	/**
	 * 메인 작업추가
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=addList")
	public ModelAndView getAddList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("AddController.getAddList Start");

		//paramMap.put("ZJISA", session.getAttribute("ZJISA"));
		List list = new ArrayList();
		String Message = "";
		/*try {
			list =  addService.getAddList(paramMap);
		} catch (Exception e) {
			Message="작업추가목록을 불러 오지 못했습니다.";
		}*/
		//	System.out.println("paramMap = "+paramMap.toString());
//		RFCInterFace app = new RFCInterFace();
//		list = app.workList(paramMap);
		Log.Debug("작업 목록 리시트 = " +list);
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(paramMap);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		addService.saveTempViewToQics100(paramMap);
		
		list = addService.workList(paramMap);
		if(list.isEmpty()){
			Message="조회된 값이 없습니다.";
		}else{
			Message="OK";
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		mv.addObject("Message", Message);
		Log.Debug("AddController.getAddList End");

		return mv;
	}
	/**
	 * 메인 작업추가 저장
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=addInsert")
	public ModelAndView setAddInsert(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("AddController.setAddInsert Start");
		Map<String, Object> convertMap = ParamUtils.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("authIP", request.getRemoteAddr());
		//session.setAttribute("authIP", request.getRemoteAddr());
		String Message = "";
		//System.out.println("paramMap = "+paramMap);
		//SEQ100 시퀀스 가져오기
		List list = new ArrayList();

		List   subTempList		= new ArrayList();
		List   insertRows		= new ArrayList();
		subTempList = (List<?>) convertMap.get("insertRows");
		HashMap<String, Object> returnMap 	= new HashMap<String, Object>();
		list =  addService.getCheckList(paramMap);
		String existPocNo="";
		int totalCount = subTempList.size();
		int errorCount=0;
		for (int i = 0; i < subTempList.size(); i++) {
			HashMap hm = new HashMap();
			HashMap dataMap = new HashMap();
			hm = (HashMap) subTempList.get(i);
			Iterator keyIterator = hm.keySet().iterator();
			while( keyIterator.hasNext() ){
				String name = (String)keyIterator.next();
				String value = hm.get(name).toString();
				dataMap.put(name,value);
			}
			dataMap.put("WORK_DATE",paramMap.get("WORK_DATE"));
			//System.out.println("dataMap = "+dataMap);
			boolean exist =false;
			for (int j = 0; j < list.size(); j++) {
				HashMap checkMap = new HashMap();
				checkMap = (HashMap) list.get(j);
				//System.out.println("checkMap = "+checkMap);
				String exInLine = checkMap.get("IN_LINE").toString();
				String exPocNo = checkMap.get("POC_NO").toString();
				String exWorkDate=checkMap.get("WORK_DATE").toString();
				String exErpUploadYn=checkMap.get("ERP_UPLOAD_YN").toString();

				String nwInLine = dataMap.get("IN_LINE").toString();
				String nwPocNo = dataMap.get("POC_NO").toString();
				String nwWorkDate = dataMap.get("WORK_DATE").toString();
				if(exInLine.equals(nwInLine) && exPocNo.equals(nwPocNo) && exWorkDate.equals(nwWorkDate)){
					if("Y".equals(exErpUploadYn)){

					}else{
						exist = true;
						existPocNo = existPocNo+nwPocNo+",";
						errorCount++;
					}
				}

			}
			if(exist){

			}else{
				Map<?,?> seqMap=addService.getSeqQ100(paramMap);
				String SEQ_Q100 = seqMap.get("SEQ_Q100").toString();
				dataMap.put("SEQ_Q100",SEQ_Q100);
				insertRows.add(dataMap);
			}

		}
		returnMap.put( "insertRows" , insertRows );

		int resultCnt = -1;
		try{
			resultCnt =addService.addInsert(returnMap);
			if(resultCnt > 0){ Message="저장 되었습니다."; } else{ Message="저장된 내용이 없습니다."; }
		}catch(Exception e){
			resultCnt = -1; Message="저장에 실패하 였습니다.";
		}
		if("".equals(existPocNo)){

		}else{
			existPocNo =existPocNo.substring(0, existPocNo.lastIndexOf(","));
		}
		//Message="작업추가 저장입니다.";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("Code", resultCnt);
		resultMap.put("Message", Message);
		resultMap.put("existPocNo", existPocNo);
		resultMap.put("totalCount", totalCount);
		resultMap.put("errorCount", errorCount);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Result", resultMap);
		Log.Debug("AddController.setAddInsert End");

		return mv;
	}
	
	/**
	 * 메인 작업추가 저장
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=addQcMapping")
	public ModelAndView addQcMapping(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("AddController.addQcMapping Start");
		String Message = "";
		int resultCnt = -1;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			resultCnt =addService.addQcMapping(paramMap);
			if(resultCnt > 0){ Message="저장 되었습니다."; } else{ Message="저장된 내용이 없습니다."; }
		}catch(Exception e){
			resultCnt = -1;
		}
		
		resultMap.put("Message", Message);
		resultMap.put("resultCnt", resultCnt);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Result", resultMap);
		Log.Debug("AddController.addQcMapping End");

		return mv;
	}
	@RequestMapping(params = "cmd=addQcMapping2")
	public ModelAndView addQcMapping2(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("AddController.addQcMapping Start");
		String Message = "";
		int resultCnt = -1;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			resultCnt =addService.addQcMapping2(paramMap);
			if(resultCnt > 0){ Message="저장 되었습니다."; } else{ Message="저장된 내용이 없습니다."; }
		}catch(Exception e){
			resultCnt = -1;
		}
		
		resultMap.put("Message", Message);
		resultMap.put("resultCnt", resultCnt);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Result", resultMap);
		Log.Debug("AddController.addQcMapping2 End");
		
		return mv;
	}
}
