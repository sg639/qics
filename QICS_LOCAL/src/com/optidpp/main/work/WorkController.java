package com.optidpp.main.work;

import java.io.Serializable;
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
import com.optidpp.main.erp.ErpService;
import com.optidpp.main.save.SaveService;
import com.optidpp.main.view.ViewService;
import com.optidpp.podservice.db.dto.CommonCollect;
import com.optidpp.qzfc.RFCInterFace;


/**
 *  작업내역상세검색
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Work.do")
public class WorkController {
	@Inject
	@Named("WorkService")
	private WorkService workService;

	@Inject
	@Named("SaveService")
	private SaveService saveservice;

	@Inject
	@Named("ErpService")
	private ErpService erpservice;

	@Inject
	@Named("ViewService")
	private ViewService viewService;
	/**
	 * 메인 작업추가팝업
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=workPopup")
	public String getWorkPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.getWorkPopup Start");
		Log.Debug("WorkController.getWorkPopup End");
		return "/main/popup/work";
	}
	/**
	 * 메인 작업내역상세검색
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=workList")
	public ModelAndView getWorkList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.getWorkList Start");
		String Message = "";
		//System.out.println("paramMap = "+ paramMap);
		List<?> list = workService.getWorkList(paramMap);


		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("WorkController.getWorkList End");

		return mv;
	}
	/**
	 * 메인 작업내역상세검색
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=workInfo")
	public ModelAndView getWorkInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.getWorkInfo Start");
		String Message = "";
		//System.out.println("paramMap = "+ paramMap);
		Map<?,?> map = workService.getWorkInfo(paramMap);


		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", map);
		Log.Debug("WorkController.getWorkInfo End");

		return mv;
	}
	/**
	 * 메인 작업내역상세검색
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=printList")
	public ModelAndView getPrintList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.getPrintList Start");
		String Message = "";
		//System.out.println("paramMap = "+ paramMap);
		List<?> result = workService.getPrintList(paramMap);


		Message="작업내역상세검색입니다.";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		mv.addObject("Message", Message);
		Log.Debug("WorkController.getPrintList End");

		return mv;
	}

	/**
	 * 메인 작업내역상세검색
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=erpReSend")
	public ModelAndView setErpReSend(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.setErpReSend Start");
		String Message = "";
		//System.out.println("paramMap = "+ paramMap);
		List list = new ArrayList();
		Map<String, Object> convertMap = ParamUtils.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("authIP", request.getRemoteAddr());

		List<?>  insertRows		= new ArrayList<Serializable>();
		insertRows = (List<?>) convertMap.get("insertRows");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (int i = 0; i < insertRows.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) insertRows.get(i);
			//System.out.println("hm = "+hm);
			Iterator keyIterator = hm.keySet().iterator();
			resultMap=sendErpData(hm);
			if(resultMap.isEmpty() || resultMap.size() == 0){

			}else{
				list.add(resultMap);
			}
		}

		//System.out.println("list = "+ list);


		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		mv.addObject("Message", Message);
		Log.Debug("WorkController.getPrintList End");

		return mv;
	}
	private Map<String, Object> sendErpData(HashMap paramMap) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		CommonCollect collect =null;
		HashMap hm = new HashMap();
		RFCInterFace app = new RFCInterFace();
		String SEQ_Q100 =paramMap.get("SEQ_Q100").toString();
		String SEQ_T300 =paramMap.get("SEQ_T300").toString();
		int result =-1;
		int total = -1;
		int resultCnt = -1;
		String resultMessage = "";
		String message = "";
		try {


			if("FORM_QICS_A".equals(paramMap.get("FORM_CODE")) || "FORM_QICS_B".equals(paramMap.get("FORM_CODE"))){
				resultMessage = app.MQC_COIL_INS_CHK("erp",paramMap);
			}else if("FORM_QICS_C".equals(paramMap.get("FORM_CODE"))){
				resultMessage = app.LQC_COIL_INS_CHK("erp",paramMap);
			}else if("FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
				resultMessage = app.LQC_COIL_INS_CHK("erp",paramMap);
			}else{

			}
			//System.out.println("resultMessage = "+resultMessage);
			if("".equals(resultMessage) || resultMessage == null){
				if("FORM_QICS_A".equals(paramMap.get("FORM_CODE")) || "FORM_QICS_B".equals(paramMap.get("FORM_CODE"))){
					collect =null;
					collect = app.HB_QM_UFS_IF_INSPECTION("erp", paramMap);
					hm = new HashMap();
					while (collect.next()) {
						for(int i=0;i<collect.getColumnCount();i++) {
							//System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
							hm.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
							hm.put("SEQ_T300",SEQ_T300);
							hm.put("SEQ_Q100",SEQ_Q100);
						}
					}
					if(collect != null  && collect.getColumnCount() > 0){
						resultCnt = -1;
						try{
							resultCnt =  saveservice.UP_MQC_COIL(hm);
							if(resultCnt > 0){
								message="저장 되었습니다.";
							}else{
								message="저장 할 내용이 없습니다.";
							}
						}catch(Exception e){
							resultCnt = -1; message="저장에 실패 하였습니다.";
						}
						if(resultCnt > 0){
							List<?> list1 = saveservice.SELECT_MST(paramMap);
							List<?> list2 = saveservice.SELECT_DETAIL1(paramMap);
							List<?> list3 = saveservice.SELECT_DETAIL2(paramMap);
							List<?> list4 = saveservice.SELECT_DETAIL3(paramMap);
							//System.out.println("total = "+list1.size()+" : "+list2.size()+" : "+list3.size()+" : "+list4.size());
							total =list1.size()+list2.size()+list3.size()+list4.size();
							result = app.BOL_QA_MQC_COIL(list1,list2,list3,list4);
							if(result ==  total){
								resultMap.put("ERP_UPLOAD_YN", "Y");
								paramMap.put("STATUS_CODE","C");
								paramMap.put("END_YN","Y");
								paramMap.put("ERP_UPLOAD_YN","Y");
								paramMap.put("ERP_UPLOAD_MESSAGE","");
							}else{
								resultMap.put("ERP_UPLOAD_YN", "E");
								paramMap.put("STATUS_CODE","S");
								paramMap.put("END_YN","N");
								paramMap.put("ERP_UPLOAD_YN","E");
								paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
							}
						}else{
							resultMap.put("ERP_UPLOAD_YN", "E");
							paramMap.put("STATUS_CODE","S");
							paramMap.put("END_YN","N");
							paramMap.put("ERP_UPLOAD_YN","E");
							paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
						}

					}
				}else if("FORM_QICS_C".equals(paramMap.get("FORM_CODE")) || "FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
					collect =null;
					collect = app.HB_QM_UFS_IF_INSPECTION("erp", paramMap);
					hm = new HashMap();
					while (collect.next()) {
						for(int i=0;i<collect.getColumnCount();i++) {
							//System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
							hm.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
							hm.put("SEQ_T300",SEQ_T300);
							hm.put("SEQ_Q100",SEQ_Q100);
						}
					}
					if(collect != null  && collect.getColumnCount() > 0){
						resultCnt = -1;
						try{
							resultCnt =  saveservice.UP_QA_LQC_COIL_RESULTS(hm);
							//System.out.println(resultCnt);
							if(resultCnt > 0){
								message="저장 되었습니다.";
							}else{
								message="저장 할 내용이 없습니다.";
							}
						}catch(Exception e){
							resultCnt = -1; message="저장에 실패 하였습니다.";
						}
						if(resultCnt > 0){
							List<?> list1 = saveservice.SELECT_LQC(paramMap);
							//System.out.println("total = "+list1.size());
							total =list1.size();
							result = app.BOL_QA_LQC_COIL(list1);
							if(result ==  total){
								resultMap.put("ERP_UPLOAD_YN", "C");
								paramMap.put("STATUS_CODE","C");
								paramMap.put("END_YN","Y");
								paramMap.put("ERP_UPLOAD_YN","Y");
								paramMap.put("ERP_UPLOAD_MESSAGE","");
							}else{
								resultMap.put("ERP_UPLOAD_YN", "E");
								paramMap.put("STATUS_CODE","S");
								paramMap.put("END_YN","N");
								paramMap.put("ERP_UPLOAD_YN","E");
								paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
							}
						}else{
							resultMap.put("ERP_UPLOAD_YN", "E");
							paramMap.put("STATUS_CODE","S");
							paramMap.put("END_YN","N");
							paramMap.put("ERP_UPLOAD_YN","E");
							paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
						}
					}

				}
			}else{
				resultMap.put("ERP_UPLOAD_YN", "E");
				paramMap.put("STATUS_CODE","S");
				paramMap.put("END_YN","N");
				paramMap.put("ERP_UPLOAD_YN","E");
				paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
			}

			try{
				resultCnt =  erpservice.erpPenWork(paramMap);
				if(resultCnt > 0){
					message="저장 되었습니다.";
					resultMap.put("Message", message);
				}else{
					message="저장 할 내용이 없습니다.";
					resultMap.put("Message", message);
				}
			}catch(Exception e){
				resultCnt = -1; message="저장에 실패 하였습니다.";
				resultMap.put("Message", message);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return resultMap;
	}
	/**
	 * 메인 작업추가팝업
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=workTest")
	public String getTestPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.getWorkPopup Start");
		Log.Debug("WorkController.getWorkPopup End");
		return "/main/popup/workTest";
	}
}
