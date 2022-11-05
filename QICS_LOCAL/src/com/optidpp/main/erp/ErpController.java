package com.optidpp.main.erp;

import java.util.HashMap;
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
import com.optidpp.main.save.SaveService;
import com.optidpp.podservice.db.dto.CommonCollect;
import com.optidpp.qzfc.RFCInterFace;


/**
 *  내용저장
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Erp.do")
public class ErpController {
	@Inject
	@Named("ErpService")
	private ErpService erpservice;

	@Inject
	@Named("SaveService")
	private SaveService saveservice;
	/**
	 * ERP 저장
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=dataErp")
	public ModelAndView setDataErp(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ErpController.setDataErp Start");

		String SEQ_Q100 =paramMap.get("SEQ_Q100").toString();
		String SEQ_T300 =paramMap.get("SEQ_T300").toString();

		//System.out.println(paramMap);
		//1.양식지의 종류로 중간/최종/최종(ME)파악
		RFCInterFace app = new RFCInterFace();
		String resultMessage = "";
		String message = "";
		//ERP DATA 전송함
		HashMap hm = new HashMap();
		CommonCollect collect =null;
		//System.out.println(paramMap.get("ERROR_CODE"));
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(paramMap.get("ERROR_CODE").toString());
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray codeSum= new JSONArray();
		JSONArray codeType= new JSONArray();
		if("FORM_QICS_A".equals(paramMap.get("FORM_CODE")) || "FORM_QICS_B".equals(paramMap.get("FORM_CODE"))){
			resultMessage = app.MQC_COIL_INS_CHK("erp",paramMap);
		}else if("FORM_QICS_C".equals(paramMap.get("FORM_CODE"))){
			resultMessage = app.LQC_COIL_INS_CHK("erp",paramMap);
		}else if("FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
			resultMessage = app.LQC_COIL_INS_CHK("erp",paramMap);
		}else{

		}
		//	resultMessage="";
		//resultStatus R (조회 OK) S : (저장 OK) ; E: ERP 오류
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result =-1;
		int total = -1;
		int resultCnt = -1;

		if("".equals(resultMessage) || resultMessage == null){
			resultMap.put("Status","R");
			if("FORM_QICS_A".equals(paramMap.get("FORM_CODE")) || "FORM_QICS_B".equals(paramMap.get("FORM_CODE"))){

				//System.out.println("resultMessage = "+ resultMessage);

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
					//저장
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
						//전송
						//System.out.println("total = "+list1.size()+" : "+list2.size()+" : "+list3.size()+" : "+list4.size());						
						total =list1.size()+list2.size()+list3.size()+list4.size();
						result = app.BOL_QA_MQC_COIL(list1,list2,list3,list4);

						Log.Debug("paramMap="+paramMap);
						Log.Debug("SELECT_MST="+list1.size()+",SELECT_DETAIL1="+list2.size()+",SELECT_DETAIL2="+list3.size()+",SELECT_DETAIL3="+list4.size());
						Log.Debug("BOL_QA_MQC_COIL:REQUEST="+total+",BOL_QA_MQC_COIL:RESULT="+result);
						
						if(total>0 && result>0 && (result ==  total)){
							resultMap.put("Status","C");
							paramMap.put("STATUS_CODE","C");
							paramMap.put("END_YN","Y");
							paramMap.put("ERP_UPLOAD_YN","Y");
							paramMap.put("ERP_UPLOAD_MESSAGE","");
						}else{
							resultMessage = "ERP IF ERROR ";
							resultMap.put("Status","E");
							resultMap.put("errorMessage",resultMessage);
							paramMap.put("STATUS_CODE","S");
							paramMap.put("END_YN","N");
							paramMap.put("ERP_UPLOAD_YN","E");
							paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
						}
					}else{
						resultMessage = "DB 저장 실패 ";
						resultMap.put("Status","W");
						resultMap.put("errorMessage",resultMessage);
						paramMap.put("STATUS_CODE","S");
						paramMap.put("END_YN","N");
						paramMap.put("ERP_UPLOAD_YN","E");
						paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
					}

				}else{
					resultMessage = "ERP IF 조회 실패 ";
					resultMap.put("Status","W");
					resultMap.put("errorMessage",resultMessage);
					paramMap.put("STATUS_CODE","S");
					paramMap.put("END_YN","N");
					paramMap.put("ERP_UPLOAD_YN","E");
					paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
				}

			}else 	if("FORM_QICS_C".equals(paramMap.get("FORM_CODE"))){
				resultMap.put("Status","R");
				collect =null;
				collect = app.HB_QM_UFS_IF_INSPECTION("erp", paramMap);
				hm = new HashMap();
				while (collect.next()) {
					for(int i=0;i<collect.getColumnCount();i++) {
						//System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
						hm.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
						hm.put("SEQ_T300",SEQ_T300);
						hm.put("SEQ_Q100",SEQ_Q100);
						hm.put("INSPECTION_DATE", paramMap.get("WORK_DATE"));
					}
				}
				//System.out.println(hm);
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
						
						Log.Debug("paramMap="+paramMap);
						Log.Debug("SELECT_LQC="+list1.size());
						Log.Debug("BOL_QA_LQC_COIL:REQUEST="+total+",BOL_QA_LQC_COIL:RESULT="+result);
						
						if(total>0 && result>0 && (result ==  total)){
							resultMap.put("Status","C");
							paramMap.put("STATUS_CODE","C");
							paramMap.put("END_YN","Y");
							paramMap.put("ERP_UPLOAD_YN","Y");
							paramMap.put("ERP_UPLOAD_MESSAGE","");
						}else{
							resultMessage = "ERP IF ERROR ";
							resultMap.put("Status","E");
							resultMap.put("errorMessage",resultMessage);
							paramMap.put("STATUS_CODE","S");
							paramMap.put("END_YN","N");
							paramMap.put("ERP_UPLOAD_YN","E");
							paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
						}
					}else{
						resultMessage = "DB 저장 실패 ";
						resultMap.put("Status","W");
						resultMap.put("errorMessage",resultMessage);
						paramMap.put("STATUS_CODE","S");
						paramMap.put("END_YN","N");
						paramMap.put("ERP_UPLOAD_YN","E");
						paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
					}

				}else{
					resultMessage = "ERP IF 조회 실패 ";
					resultMap.put("Status","W");
					resultMap.put("errorMessage",resultMessage);
					paramMap.put("STATUS_CODE","S");
					paramMap.put("END_YN","N");
					paramMap.put("ERP_UPLOAD_YN","E");
					paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
				}
			}else if("FORM_QICS_D".equals(paramMap.get("FORM_CODE"))){
				resultMap.put("Status","R");
				collect =null;
				collect = app.HB_QM_UFS_IF_INSPECTION("erp", paramMap);
				hm = new HashMap();
				while (collect.next()) {
					for(int i=0;i<collect.getColumnCount();i++) {
						//System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
						hm.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
						hm.put("SEQ_T300",SEQ_T300);
						hm.put("SEQ_Q100",SEQ_Q100);
						hm.put("INSPECTION_DATE", paramMap.get("WORK_DATE"));
					}
				}
				if(collect != null  && collect.getColumnCount() > 0){
					resultCnt = -1;
					try{
						resultCnt =  saveservice.UP_QA_LQC_COIL_RESULTS(hm);
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

						Log.Debug("paramMap="+paramMap);
						Log.Debug("SELECT_LQC="+list1.size());
						Log.Debug("BOL_QA_LQC_COIL:REQUEST="+total+",BOL_QA_LQC_COIL:RESULT="+result);
						
						if(total>0 && result>0 && (result ==  total)){
							resultMap.put("Status","C");
							resultMap.put("errorMessage","");
							paramMap.put("STATUS_CODE","C");
							paramMap.put("END_YN","Y");
							paramMap.put("ERP_UPLOAD_YN","Y");
							paramMap.put("ERP_UPLOAD_MESSAGE","");
						}else{
							resultMessage = "ERP IF ERROR ";
							resultMap.put("Status","E");
							resultMap.put("errorMessage",resultMessage);
							paramMap.put("STATUS_CODE","S");
							paramMap.put("END_YN","N");
							paramMap.put("ERP_UPLOAD_YN","E");
							paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
						}
					}else{
						resultMessage = "DB 저장 실패 ";
						resultMap.put("Status","W");
						resultMap.put("errorMessage",resultMessage);
						paramMap.put("STATUS_CODE","S");
						paramMap.put("END_YN","N");
						paramMap.put("ERP_UPLOAD_YN","E");
						paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
					}

				}else{
					resultMessage = "ERP IF 조회 실패 ";
					resultMap.put("Status","W");
					resultMap.put("errorMessage",resultMessage);
					paramMap.put("STATUS_CODE","S");
					paramMap.put("END_YN","N");
					paramMap.put("ERP_UPLOAD_YN","E");
					paramMap.put("ERP_UPLOAD_MESSAGE",resultMessage);
				}
			}

		}else{
			resultMap.put("Status","T");
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


		resultMap.put("Code", resultCnt);
		//System.out.println("paramMap = " +paramMap );
		if("T".equals(resultMap.get("Status"))){
			resultMap.put("Message", resultMessage);
		}else if("W".equals(resultMap.get("Status"))){
			resultMap.put("Message", resultMessage);
		}else if("E".equals(resultMap.get("Status"))){
			resultMap.put("Message", resultMessage);
		}
		//System.out.println("resultMap = " +resultMap );
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", resultMap);
		Log.Debug("ErpController.setDataErp End");

		return mv;
	}
}
