package com.optidpp.main.penwork;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.optidpp.common.util.StringUtil;
import com.optidpp.main.code.CodeService;
import com.optidpp.podservice.db.dto.CommonCollect;
import com.optidpp.podservice.pod.PODCreate;
import com.optidpp.qzfc.RFCInterFace;
import com.optidpp.zrfc.common.ConfigValue;


/**
 *  전자펜작업목록
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/PenWork.do")
public class PenWorkController {
	@Inject
	@Named("PenWorkService")
	private PenWorkService penworkService;

	@Inject
	@Named("CodeService")
	private CodeService codeService;
	/**
	 * 메인 전자펜 작업 중간검사
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkM")
	public String getPenWorkM(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getPenWorkM Start");
		Log.Debug("PenWorkController.getPenWorkM End");
		return "/main/penwork/penWorkM";
	}
	/**
	 * 메인 전자펜 작업 최종검사
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkF")
	public String getPenWorkF(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getPenWorkM Start");
		Log.Debug("PenWorkController.getPenWorkM End");
		return "/main/penwork/penWorkF";
	}
	/**
	 * 메인 전자펜 작업 VIEW
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkV")
	public String getPenWorkV(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getPenWorkV Start");
		Log.Debug("PenWorkController.getPenWorkV End");
		return "/main/penwork/penWorkV";
	}

	/**
	 * 메인 전자펜 작업 VIEW
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=ufsWorkV")
	public String getUfsWorkV(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getUfsWorkV Start");
		Log.Debug("PenWorkController.getUfsWorkV End");
		return "/main/penwork/ufsWorkV";
	}
	/**
	 * 메인 전자펜 작업 목록
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkList")
	public ModelAndView getPenWorkList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getPenWorkList Start");

		String Message = "";
		//System.out.println("paramMap = "+ paramMap);
		List<?> list = penworkService.getPenWorkList(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("PenWorkController.getPenWorkList End");

		return mv;
	}
	/**
	 * 메인 전자펜 작업 Info
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkInfo")
	public ModelAndView getPenWorkInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getPenWorkInfo Start");

		String Message = "전자펜 작업 Info입니다.";

		Map<?,?> map = penworkService.getPenWorkInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", map);
		Log.Debug("PenWorkController.getPenWorkInfo End");

		return mv;
	}
	/**
	 * 메인 전자펜 작업 중간검사 직접입력
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkMiddleDirect")
	public ModelAndView setPenWorkMiddleDirect(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkMiddleDirect Start");



		/*입력값을 설정한다.*/
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String authIP = request.getRemoteAddr();
		paramMap.put("authIP", authIP);
		List<?> list = codeService.getPodCodeList(paramMap);
		String value ="";
		for (int i = 0; i < list.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) list.get(i);
			String tempStr1 = hm.get("CODE_GUBUN").toString();
			String tempStr2 = hm.get("ATTRIBUTE2").toString();
			if("DEPT_NAME".equals(tempStr1)){
				paramMap.put("C_FRM_DPT", hm.get("CODE"));
			}else{
				if("품질검사".equals(tempStr2)){
					paramMap.put("C_APP",hm.get("CODE"));
				}
			}
		}
		resultMap = createWebData(paramMap);


		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", resultMap.get("Message"));
		mv.addObject("RESULT", resultMap.get("RESULT"));
		Log.Debug("PenWorkController.setPenWorkMiddleDirect End");

		return mv;
	}
	private Map<String, Object> createWebData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		try {


			String Message = "";
			String RESULT = "";
			String POC_NO = "";
			String POC_NO01 = paramMap.get("POC_NO01").toString();
			String POC_NO02 = paramMap.get("POC_NO02").toString();
			String POC_NO03 = paramMap.get("POC_NO03").toString();

			if(POC_NO03 !=null && !"".equals(POC_NO03)){
				POC_NO = POC_NO01+"-"+POC_NO02+"-"+POC_NO03;

			}else{
				POC_NO = POC_NO01+"-"+POC_NO02;
			}
			headerMap.put("POC_NO",POC_NO);
			headerMap.put("FORM_TYPE",paramMap.get("FORM_TYPE")); //검사일자
			headerMap.put("IN_LINE",paramMap.get("IN_LINE")); //검사일자
			if(paramMap.containsKey("PRE_SEQ_Q100")){
				//String PRE_POC_NO = paramMap.get("POC_NO").toString();
				String PRE_POC_NO = paramMap.get("MRG_WIP_ENTITY_NAME").toString();
				//System.out.println("PRE_POC_NO = "+ PRE_POC_NO);
				headerMap.put("IN_FACT",paramMap.get("IN_FACT").toString());
				headerMap.put("IN_ORDER",paramMap.get("IN_ORDER").toString());
				headerMap.put("IN_LINE",paramMap.get("IN_LINE").toString());
				headerMap.put("NEW_FINAL",paramMap.get("NEW_FINAL").toString());
				headerMap.put("PRE_SEQ_Q100",paramMap.get("PRE_SEQ_Q100").toString());
				headerMap.put("MRG_SEQ",paramMap.get("MRG_SEQ").toString());
				headerMap.put("MRG_DISCRETE_NUMBER",paramMap.get("MRG_DISCRETE_NUMBER").toString());
				headerMap.put("MRG_LINE_CODE;",paramMap.get("MRG_LINE_CODE").toString());
				headerMap.put("MRG_STEEL_TYPE",paramMap.get("MRG_STEEL_TYPE").toString());
				headerMap.put("MRG_R_SUPPLIER",paramMap.get("MRG_R_SUPPLIER").toString());
				headerMap.put("MRG_WIP_ENTITY_NAME",PRE_POC_NO);
				//headerMap.put("MRG_WIP_ENTITY_NAME",POC_NO);
				headerMap.put("MRG_R_SUPPLY_THICKNESS",paramMap.get("MRG_R_SUPPLY_THICKNESS").toString());
				headerMap.put("MRG_R_WIDTH",paramMap.get("MRG_R_WIDTH").toString());
				headerMap.put("PARTIAL_WEIGHT",paramMap.get("PARTIAL_WEIGHT").toString());
				headerMap.put("TARGET_THICKNESS",paramMap.get("TARGET_THICKNESS").toString());
				headerMap.put("AIM_THICKNESS",paramMap.get("AIM_THICKNESS").toString());
				headerMap.put("AIM_TOL",paramMap.get("AIM_TOL").toString());
				headerMap.put("JOB_DESCRIPTION",paramMap.get("JOB_DESCRIPTION").toString());
				if("Y".equals(paramMap.get("NEW_FINAL").toString())){
					if(  ( paramMap.get("ORDER_NUMBER") != null && !"".equals(paramMap.get("ORDER_NUMBER"))   )
							&& (paramMap.get("LINE_NUMBER") != null && !"".equals(paramMap.get("LINE_NUMBER"))     )

							){
						headerMap.put("ORDER_NUMBER",paramMap.get("ORDER_NUMBER"));
						headerMap.put("LINE_NUMBER",paramMap.get("LINE_NUMBER"));
						headerMap.put("PRE_POC_NO",PRE_POC_NO);

					}else{

						headerMap.put("PRE_POC_NO",PRE_POC_NO);
					}
				}else{
					headerMap.put("PRE_POC_NO",PRE_POC_NO);
				}
				//System.out.println("headerMap11 = "+headerMap);
			}

			String CHECK_DATA  = paramMap.get("CHECK_DATA").toString();

			if("".equals(CHECK_DATA) || CHECK_DATA == null){

			}else{
				String [] DATA = CHECK_DATA.split(",");

				for (int i = 0; i < DATA.length; i++) {

					String stampValue = "";
					String stampName="C_STAMP0"+(i+1);
					stampValue =DATA[i];
					headerMap.put(stampName+"_POD",stampValue);
					headerMap.put(stampName,"QICS_STAMP_BASE.png");
				}
			}



			headerMap.put("authIP",paramMap.get("authIP").toString());

			//System.out.println("headerMap = "+headerMap);
			//검사양식공통정보(IF) 를 가져온다
			RFCInterFace app = new RFCInterFace();
			Map<String, Object> ifMap = app.commonInfo(headerMap);
			Log.Debug("작업지시 I/F = "+ifMap);

			if(ifMap.isEmpty() || ifMap == null){
				if(paramMap.containsKey("PRE_SEQ_Q100")){
					ifMap = defaultFinalInfo(paramMap);
				}else{
					ifMap = defaultInfo(paramMap);
				}

			}else{
				if(ifMap.containsKey("C_THMAX01_POD")){
					headerMap.put("C_THMAX01_POD",ifMap.get("C_THMAX01_POD")); //POC No.
				}
				if(ifMap.containsKey("C_THMAX02_POD")){
					headerMap.put("C_THMAX02_POD",ifMap.get("C_THMAX02_POD")); //POC No.
				}
				if(ifMap.containsKey("C_THMIN01_POD")){
					headerMap.put("C_THMIN01_POD",ifMap.get("C_THMIN01_POD")); //POC No.
				}
				if(ifMap.containsKey("C_THMIN02_POD")){
					headerMap.put("C_THMIN02_POD",ifMap.get("C_THMIN02_POD")); //POC No.
				}

			}
			//SD9/DZ9/EZ9/HOOP
			//System.out.println("1111 ifMap = "+ ifMap);
			if(paramMap.containsKey("ORDER_NUMBER")
					&& ( "SD9".equals(paramMap.get("ORDER_NUMBER"))
							|| "DZ9".equals(paramMap.get("ORDER_NUMBER"))
							|| "EZ9".equals(paramMap.get("ORDER_NUMBER"))
							|| "HOOP".equals(paramMap.get("ORDER_NUMBER"))
							)
					) {

				//System.out.println("2222 ifMap = "+ paramMap.get("ORDER_NUMBER"));
				ifMap.put("CUSTUMER" ,"");
				ifMap.put("ORDER_NUMBER" ,paramMap.get("ORDER_NUMBER"));
				ifMap.put("LINE_NUMBER" ,"");
				ifMap.put("RELEATED_SIZE" ,"");
				ifMap.put("USE_NAME" ,"");

				headerMap.put("CUSTUMER" ,"");
				headerMap.put("ORDER_NUMBER" ,paramMap.get("ORDER_NUMBER"));
				headerMap.put("LINE_NUMBER" ,"");
				headerMap.put("RELEATED_SIZE" ,"");
				headerMap.put("USE_NAME" ,"");

			} else {

			}
			//System.out.println("3333 ifMap = "+ ifMap);
			if("HOOP".equals(paramMap.get("ORDER_NUMBER"))){
				ifMap.put("LAST_WIDTH" ,"");
			}
			try {
				Map<?,?> tdpp201Map=penworkService.getPadMaster(paramMap);
				//System.out.println("tdpp201Map = "+tdpp201Map);
				//TDPP300 시퀀스를 만든다.
				Map<?,?> seqMap=penworkService.getSeqT300(paramMap);
				String SEQ_T300 = seqMap.get("SEQ_T300").toString();
				//System.out.println("SEQ_T300 = "+SEQ_T300);
				if("FORM_QICS_D".equals(paramMap.get("FORM_CODE").toString())){
					headerMap.put("C_FRM_ID","※ 본 M/E Coil 검사결과표는 내경(안쪽)부터 검사한 결과 입니다."); //시스템 양식지명
				}else{
					headerMap.put("C_FRM_ID",""); //시스템 양식지명
				}
				//headerMap.put("C_FRM_ID",paramMap.get("IN_LINE").toString() +" "+tdpp201Map.get("FORM_NAME")); //시스템 양식지명
				headerMap.put("C_SEQ",SEQ_T300); //시스템 양식지명
				headerMap.put("C_MAIN_PRC",paramMap.get("IN_LINE").toString()); //공정명
				headerMap.put("C_POC_NO01_POD",POC_NO01); //POC No.
				headerMap.put("C_POC_NO02_POD",POC_NO02); //POC No.
				headerMap.put("C_POC_NO03_POD",POC_NO03); //POC No.
				headerMap.put("POC_NO01",POC_NO01); //POC No.
				headerMap.put("POC_NO02",POC_NO02); //POC No.
				headerMap.put("POC_NO03",POC_NO03); //POC No.
				headerMap.put("C_CHK_DT",paramMap.get("WORK_DATE").toString()); //검사일자
				headerMap.put("WORK_DATE",paramMap.get("WORK_DATE").toString()); //검사일자
				headerMap.put("FORM_CODE",paramMap.get("FORM_CODE").toString()); //검사일자
				headerMap.put("FORM_ID",tdpp201Map.get("FORM_ID")); //검사일자
				headerMap.put("STATUS_CODE","S"); //검사일자
				headerMap.put("WEB_DATA_YN","Y"); //검사일자
				headerMap.put("DATA_YN","Y"); //검사일자
				ConfigValue cfv = new ConfigValue();
				if(paramMap.containsKey("FORM_CODE")){
					if("FORM_QICS_D".equals(headerMap.get("FORM_CODE"))){
						headerMap.put("C_FRM_NO",cfv.devCfrmNo2);
					}else if("FORM_QICS_A".equals(headerMap.get("FORM_CODE")) || "FORM_QICS_B".equals(headerMap.get("FORM_CODE")) || "FORM_QICS_C".equals(headerMap.get("FORM_CODE"))){
						headerMap.put("C_FRM_NO",cfv.devCfrmNo1);
					}
				}
				headerMap.put("C_FRM_DPT",paramMap.get("C_FRM_DPT"));

				String [] capp = paramMap.get("C_APP").toString().split(",");
				for (int i = 0; i < capp.length; i++) {
					headerMap.put("C_APP0"+(i+1), capp[i]);

				}

				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


				headerMap.put("C_FRM_PRN","PRINT DATE : "+sdf.format(dt).toString() +" ("+paramMap.get("IN_LINE").toString() +")");

				headerMap.put("FORM_SEQ",tdpp201Map.get("FORM_SEQ"));
				headerMap.put("PRINT_PAGE",tdpp201Map.get("PAD_COUNT"));
				headerMap.put("FORM_NAME",tdpp201Map.get("FORM_NAME"));
				headerMap.put("SEQ_T300", SEQ_T300);
				headerMap.put("C_SEQ", SEQ_T300);
				headerMap.put("SEQ_Q100",  paramMap.get("SEQ_Q100").toString());
				resultMap.put("SEQ_T300",headerMap.get("SEQ_T300").toString());
				resultMap.put("FORM_NAME",paramMap.get("IN_LINE").toString() +" "+tdpp201Map.get("FORM_NAME"));
				//STEMP 정보를 가져온다.
				//headerMap.put("C_STAMP1","QICS_STAMP_BASE.png"); //형상약도 IMAGE
				//headerMap.put("C_STAMP1_POD","현대자동차"); //형상약도 TEXT


				//4. QICS200 Table(검사공통정보 POD-IF) DATA입력


				Map<String, Object> qics200Map = ifMap;
				qics200Map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString() );
				qics200Map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
				qics200Map.put("authIP", headerMap.get("authIP").toString());


				// 5-1 TDPP203의 정보를 가져온다.


				//4-2 TDPP203 + seqT300 + SEQ_Q100 ==> QICS203에 INSERT (페이지수가 2이상일경우 대비해서 LIST형태로
				List bglist = new ArrayList();
				Map qics203Map=new HashMap();
				HashMap  map 		= null;
				List  insertRows		= new ArrayList ();
				bglist=penworkService.getBgImage(tdpp201Map);

				for (int i = 0; i < bglist.size(); i++) {

					map =   (HashMap) bglist.get(i);

					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());

					insertRows.add(map);
				}
				qics203Map.put( "insertRows" , insertRows );

				//6-1

				List fieldlist = new ArrayList();
				Map qics205Map=new HashMap();
				map 		= null;
				insertRows		= new ArrayList ();
				fieldlist=penworkService.getFieldInfo(tdpp201Map);

				for (int i = 0; i < fieldlist.size(); i++) {
					map = new HashMap<String, String>();
					map =   (HashMap) fieldlist.get(i);
					String erpCname = (String) map.get("ERP_CNAME");
					String fieldName = (String) map.get("FIELD_NAME");
					if(erpCname !=null && !"".equals(erpCname)){

						if(ifMap.get(erpCname) != null && !"".equals(ifMap.get(erpCname))){
							//System.out.println("ifMap = "+ ifMap);
							if("ORDER_NUMBER".equals(erpCname) &&ifMap.containsKey("ORDER_NUMBER") && ifMap.containsKey("LINE_NUMBER")){
								String orderNumber= ifMap.get("ORDER_NUMBER").toString();
								String lineNumber =  ifMap.get("LINE_NUMBER").toString();
								if("SD9".equals(orderNumber) || "DZ9".equals(orderNumber) || "EZ9".equals(orderNumber) || "HOOP".equals(orderNumber)){
									map.put("FIELD_POD",orderNumber);
								}else{
									map.put("FIELD_POD",orderNumber+"-"+lineNumber);
								}

								map.put("USER_INPUT_VALUE",ifMap.get("ORDER_NUMBER")+"-"+ifMap.get("LINE_NUMBER"));
							}else{
								map.put("FIELD_POD",ifMap.get(erpCname));
								map.put("USER_INPUT_VALUE",ifMap.get(erpCname));
							}


						} else {
							if(headerMap.containsKey(fieldName)){
								map.put("FIELD_POD",headerMap.get(fieldName));
								map.put("USER_INPUT_VALUE",headerMap.get(fieldName));
							} else {
								map.put("FIELD_POD","");
								map.put("USER_INPUT_VALUE","");
							}
						}
					}else{
						if(fieldName != null && !"".equals(fieldName)){
							if(headerMap.containsKey(fieldName)){
								map.put("FIELD_POD",headerMap.get(fieldName));
								map.put("USER_INPUT_VALUE",headerMap.get(fieldName));
							}
						} else {
							map.put("FIELD_POD","");
							map.put("USER_INPUT_VALUE","");
						}

					}


					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());
					//System.out.println("1111111111 map =  "+map);
					insertRows.add(map);
				}
				qics205Map.put( "insertRows" , insertRows );

				//TDPP207
				String xmlFile = headerMap.get("FORM_ID").toString()+"_"+ headerMap.get("SEQ_T300").toString(); //
				headerMap.put("XML_FILE",xmlFile);
				headerMap.put("PRINT_CODE",headerMap.get("SEQ_T300").toString());
				////System.out.println(qics205Map);
				int resultCnt = -1;
				try{
					if("M".equals(paramMap.get("FORM_TYPE"))){
						resultCnt =penworkService.setTotalWebInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
					}else{
						resultCnt =penworkService.setTotalFinalWebInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
					}

					if(resultCnt > 0){ Message="저장 되었습니다."; RESULT="Y";  } else{ Message="저장된 내용이 없습니다.";RESULT=""; }
				}catch(Exception e){
					resultCnt = -1; Message="저장에 실패하였습니다.";
					RESULT="N";
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Message="저장에 실패하였습니다.";
				RESULT="N";
			}


			resultMap.put("Message",Message);
			resultMap.put("RESULT",RESULT);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	private Map<String, Object> defaultFinalInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//System.out.println("defaultInfo = "+paramMap);
			//Map<?,?> dataMap=penworkService.getPenWorkFinalInfo(paramMap);
			//System.out.println("dataMap = "+dataMap);
			resultMap.put("IN_FACT",paramMap.get("IN_FACT"));
			resultMap.put("IN_ORDER",paramMap.get("IN_ORDER"));
			resultMap.put("IN_LINE",paramMap.get("IN_LINE"));
			resultMap.put("IN_TYPE",paramMap.get("IN_TYPE"));
			//resultMap.put("R_SUPPLY_THICKNESS",headerMap.get("MRG_R_SUPPLY_THICKNESS"));
			resultMap.put("STEEL_TYPE",paramMap.get("MRG_STEEL_TYPE"));
			resultMap.put("R_SUPPLIER",paramMap.get("MRG_R_SUPPLIER"));
			resultMap.put("TARGET_THICKNESS",paramMap.get("MRG_R_SUPPLY_THICKNESS"));
			resultMap.put("LAST_WIDTH",paramMap.get("MRG_R_WIDTH"));
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return resultMap;
	}
	/**
	 * 메인 전자펜 작업 중간검사 인쇄요청
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkMiddlePrint")
	public ModelAndView setPenWorkMiddlePrint(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkMiddlePrint Start");


		String Message = "";
		/*입력값을 설정한다.*/
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String authIP = request.getRemoteAddr();
		paramMap.put("authIP", authIP);
		String baseUrl = StringUtil.getBaseUrl(request);
		paramMap.put("baseUrl", baseUrl);
		List<?> list = codeService.getPodCodeList(paramMap);
		String value ="";
		for (int i = 0; i < list.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) list.get(i);
			String tempStr1 = hm.get("CODE_GUBUN").toString();
			String tempStr2 = hm.get("ATTRIBUTE2").toString();
			if("DEPT_NAME".equals(tempStr1)){
				paramMap.put("C_FRM_DPT", hm.get("CODE"));
			}else{
				if("품질검사".equals(tempStr2)){
					paramMap.put("C_APP",hm.get("CODE"));
				}
			}
		}

		//System.out.println("C_FRM_DPT " + paramMap.get("C_FRM_DPT"));
		//System.out.println("C_APP " + paramMap.get("C_APP"));

		resultMap = createXmlData(paramMap);
	
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!");	
System.out.println(resultMap);				
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!");	
		
		resultMap.put("RESULT", "Y");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", Message);
		mv.addObject("DATA", resultMap);
		mv.addObject("INDATA", paramMap);
		Log.Debug("PenWorkController.setPenWorkMiddlePrint End");

		return mv;
	}
	
	@RequestMapping(params = "cmd=penWorkMiddlePrint2")
	public ModelAndView setPenWorkMiddlePrint2(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkMiddlePrint2 Start");


		String Message = "";
		/*입력값을 설정한다.*/
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String authIP = request.getRemoteAddr();
		paramMap.put("authIP", authIP);
		String baseUrl = StringUtil.getBaseUrl(request);
		paramMap.put("baseUrl", baseUrl);
		List<?> list = codeService.getPodCodeList(paramMap);
		String value ="";
		for (int i = 0; i < list.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) list.get(i);
			String tempStr1 = hm.get("CODE_GUBUN").toString();
			String tempStr2 = hm.get("ATTRIBUTE2").toString();
			if("DEPT_NAME".equals(tempStr1)){
				paramMap.put("C_FRM_DPT", hm.get("CODE"));
			}else{
				if("품질검사".equals(tempStr2)){
					paramMap.put("C_APP",hm.get("CODE"));
				}
			}
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!");	
		System.out.println(paramMap);				
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!");	

		int resultCnt = -1;
		resultCnt = penworkService.setPrintM(paramMap);
	
		if(resultCnt > 0) {			
			resultMap.put("RESULT", "Y");
		}else {			
			resultMap.put("RESULT", "N");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", Message);
		Log.Debug("PenWorkController.setPenWorkMiddlePrint2 End");

		return mv;
	}
	
	private Map<String, Object> createXmlData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();

		String Message="";
		String POC_NO = "";
		String POC_NO01 = paramMap.get("POC_NO01").toString();
		String POC_NO02 = paramMap.get("POC_NO02").toString();
		String POC_NO03 = paramMap.get("POC_NO03").toString();


		if(POC_NO03 !=null && !"".equals(POC_NO03)){
			POC_NO = POC_NO01+"-"+POC_NO02+"-"+POC_NO03;

		}else{
			POC_NO = POC_NO01+"-"+POC_NO02;
		}
		headerMap.put("POC_NO",POC_NO);
		if(paramMap.containsKey("PRE_SEQ_Q100")){
			//String PRE_POC_NO = paramMap.get("POC_NO").toString();
			String PRE_POC_NO = paramMap.get("MRG_WIP_ENTITY_NAME").toString();
			//System.out.println("PRE_POC_NO = "+ PRE_POC_NO);
			headerMap.put("IN_FACT",paramMap.get("IN_FACT").toString());
			headerMap.put("IN_ORDER",paramMap.get("IN_ORDER").toString());
			headerMap.put("PRE_SEQ_Q100",paramMap.get("PRE_SEQ_Q100").toString());
			headerMap.put("NEW_FINAL",paramMap.get("NEW_FINAL").toString());
			headerMap.put("MRG_SEQ",paramMap.get("MRG_SEQ").toString());
			headerMap.put("MRG_DISCRETE_NUMBER",paramMap.get("MRG_DISCRETE_NUMBER").toString());
			headerMap.put("MRG_LINE_CODE;",paramMap.get("MRG_LINE_CODE").toString());
			headerMap.put("MRG_STEEL_TYPE",paramMap.get("MRG_STEEL_TYPE").toString());
			headerMap.put("MRG_R_SUPPLIER",paramMap.get("MRG_R_SUPPLIER").toString());
			//headerMap.put("MRG_WIP_ENTITY_NAME",POC_NO);
			headerMap.put("MRG_WIP_ENTITY_NAME",PRE_POC_NO);
			headerMap.put("MRG_R_SUPPLY_THICKNESS",paramMap.get("MRG_R_SUPPLY_THICKNESS").toString());
			headerMap.put("MRG_R_WIDTH",paramMap.get("MRG_R_WIDTH").toString());
			headerMap.put("PARTIAL_WEIGHT",paramMap.get("PARTIAL_WEIGHT").toString());
			headerMap.put("TARGET_THICKNESS",paramMap.get("TARGET_THICKNESS").toString());
			headerMap.put("AIM_THICKNESS",paramMap.get("AIM_THICKNESS").toString());
			headerMap.put("AIM_TOL",paramMap.get("AIM_TOL").toString());
			headerMap.put("JOB_DESCRIPTION",paramMap.get("JOB_DESCRIPTION").toString());
			if("Y".equals(paramMap.get("NEW_FINAL").toString())){
				if(  ( paramMap.get("ORDER_NUMBER") != null && !"".equals(paramMap.get("ORDER_NUMBER"))   )
						&& (paramMap.get("LINE_NUMBER") != null && !"".equals(paramMap.get("LINE_NUMBER"))     )

						){
					headerMap.put("ORDER_NUMBER",paramMap.get("ORDER_NUMBER"));
					headerMap.put("LINE_NUMBER",paramMap.get("LINE_NUMBER"));
					headerMap.put("PRE_POC_NO",PRE_POC_NO);

				}else{

					headerMap.put("PRE_POC_NO",PRE_POC_NO);
				}
			}else{
				headerMap.put("PRE_POC_NO",PRE_POC_NO);
			}
		}


		String CHECK_DATA  = paramMap.get("CHECK_DATA").toString();

		if("".equals(CHECK_DATA) || CHECK_DATA == null){

		}else{
			String [] DATA = CHECK_DATA.split(",");

			for (int i = 0; i < DATA.length; i++) {

				String stampValue = "";
				String stampName="C_STAMP0"+(i+1);
				stampValue =DATA[i];
				headerMap.put(stampName+"_POD",stampValue);
				headerMap.put(stampName,"QICS_STAMP_BASE.png");
			}
		}
		headerMap.put("authIP", paramMap.get("authIP"));
		headerMap.put("POC_NO01", POC_NO01);
		headerMap.put("POC_NO02", POC_NO02);
		headerMap.put("POC_NO03", POC_NO03);
		headerMap.put("C_POC_NO01_POD",POC_NO01); //POC No.
		headerMap.put("C_POC_NO02_POD",POC_NO02); //POC No.
		headerMap.put("C_POC_NO03_POD",POC_NO03); //POC No.
		headerMap.put("C_CHK_DT",paramMap.get("WORK_DATE").toString()); //검사일자
		headerMap.put("WORK_DATE",paramMap.get("WORK_DATE").toString()); //검사일자
		headerMap.put("IN_LINE",paramMap.get("IN_LINE").toString());
		headerMap.put("FORM_CODE",paramMap.get("FORM_CODE").toString()); //검사일자
		headerMap.put("STATUS_CODE","P"); //검사일자
		headerMap.put("WEB_DATA_YN","N"); //검사일자
		headerMap.put("C_MAIN_PRC",paramMap.get("IN_LINE").toString());
		headerMap.put("FORM_TYPE",paramMap.get("FORM_TYPE")); //검사일자
		ConfigValue cfv = new ConfigValue();
		if(headerMap.containsKey("FORM_CODE")){
			if("FORM_QICS_D".equals(headerMap.get("FORM_CODE"))){
				headerMap.put("C_FRM_NO",cfv.devCfrmNo2);
			}else if("FORM_QICS_A".equals(headerMap.get("FORM_CODE")) || "FORM_QICS_B".equals(headerMap.get("FORM_CODE")) || "FORM_QICS_C".equals(headerMap.get("FORM_CODE"))){
				headerMap.put("C_FRM_NO",cfv.devCfrmNo1);
			}
		}



		headerMap.put("C_FRM_DPT",paramMap.get("C_FRM_DPT"));




		String [] capp = paramMap.get("C_APP").toString().split(",");
		for (int i = 0; i < capp.length; i++) {
			headerMap.put("C_APP0"+(i+1), capp[i]);

		}
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


		headerMap.put("C_FRM_PRN","PRINT DATE : "+sdf.format(dt).toString() +" ("+paramMap.get("IN_LINE").toString() +")");

		RFCInterFace app = new RFCInterFace();
		Map<String, Object> ifMap = app.commonInfo(headerMap);
		Log.Debug("작업지시 I/F = "+ifMap);
		if(ifMap.isEmpty() || ifMap == null){
			if(paramMap.containsKey("PRE_SEQ_Q100")){
				ifMap = defaultFinalInfo(paramMap);
			}else{
				ifMap = defaultInfo(paramMap);
			}
		}else{

			String tempValue="";
			//Map<String, Object> map = new HashMap<String, Object>();

			if(ifMap.containsKey("R_SUPPLIER")){
				tempValue =ifMap.get("R_SUPPLIER").toString();
				if("".equals(tempValue) || tempValue == null){
					ifMap.put("R_SUPPLIER",headerMap.get("MRG_R_SUPPLIER"));
					//ifMap=map;
				}
			}
			if(ifMap.containsKey("STEEL_TYPE")){
				tempValue =ifMap.get("STEEL_TYPE").toString();
				if("".equals(tempValue) || tempValue == null){
					//map = new HashMap<String, Object>();
					ifMap.put("STEEL_TYPE",headerMap.get("MRG_STEEL_TYPE"));
					//ifMap=map;
				}
			}

			if(ifMap.containsKey("C_THMAX01_POD")){
				headerMap.put("C_THMAX01_POD",ifMap.get("C_THMAX01_POD")); //POC No.
			}
			if(ifMap.containsKey("C_THMAX02_POD")){
				headerMap.put("C_THMAX02_POD",ifMap.get("C_THMAX02_POD")); //POC No.
			}
			if(ifMap.containsKey("C_THMIN01_POD")){
				headerMap.put("C_THMIN01_POD",ifMap.get("C_THMIN01_POD")); //POC No.
			}
			if(ifMap.containsKey("C_THMIN02_POD")){
				headerMap.put("C_THMIN02_POD",ifMap.get("C_THMIN02_POD")); //POC No.
			}




		}


		//SD9/DZ9/EZ9/HOOP
		if(paramMap.containsKey("ORDER_NUMBER")
				&& ( "SD9".equals(paramMap.get("ORDER_NUMBER"))
						|| "DZ9".equals(paramMap.get("ORDER_NUMBER"))
						|| "EZ9".equals(paramMap.get("ORDER_NUMBER"))
						|| "HOOP".equals(paramMap.get("ORDER_NUMBER"))
						)
				) {

			//System.out.println("2222 ifMap = "+ paramMap.get("ORDER_NUMBER"));
			ifMap.put("CUSTUMER" ,"");
			ifMap.put("ORDER_NUMBER" ,paramMap.get("ORDER_NUMBER"));
			ifMap.put("LINE_NUMBER" ,"");
			ifMap.put("RELEATED_SIZE" ,"");
			ifMap.put("USE_NAME" ,"");

			headerMap.put("CUSTUMER" ,"");
			headerMap.put("ORDER_NUMBER" ,paramMap.get("ORDER_NUMBER"));
			headerMap.put("LINE_NUMBER" ,"");
			headerMap.put("RELEATED_SIZE" ,"");
			headerMap.put("USE_NAME" ,"");

		} else {

		}
		if("HOOP".equals(paramMap.get("ORDER_NUMBER"))){
			ifMap.put("LAST_WIDTH" ,"");
		}
		try {
			Map<?,?> tdpp201Map=penworkService.getPadMaster(paramMap);
			Map<?,?> seqMap=penworkService.getSeqT300(paramMap);
			String SEQ_T300 = seqMap.get("SEQ_T300").toString();
			if("FORM_QICS_D".equals(paramMap.get("FORM_CODE").toString())){
				headerMap.put("C_FRM_ID","※ 본 M/E Coil 검사결과표는 내경(안쪽)부터 검사한 결과 입니다."); //시스템 양식지명
			}else{
				headerMap.put("C_FRM_ID",""); //시스템 양식지명
			}
			//	headerMap.put("C_FRM_ID",paramMap.get("IN_LINE").toString() +" "+tdpp201Map.get("FORM_NAME")); //시스템 양식지명
			headerMap.put("FORM_ID",tdpp201Map.get("FORM_ID")); //검사일자
			headerMap.put("FORM_SEQ",tdpp201Map.get("FORM_SEQ"));
			headerMap.put("PRINT_PAGE",tdpp201Map.get("PAD_COUNT"));
			headerMap.put("FORM_NAME",tdpp201Map.get("FORM_NAME"));
			headerMap.put("SEQ_T300", SEQ_T300);
			headerMap.put("C_SEQ", SEQ_T300);
			headerMap.put("SEQ_Q100",  paramMap.get("SEQ_Q100").toString());


			List list = new ArrayList();
			list=penworkService.getPadDynamic(headerMap);
			
			int resultCnt = -1;
			Map<String, Object> qics200Map = ifMap;
			qics200Map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString() );
			Map qics203Map=new HashMap();
			Map qics205Map=new HashMap();
			
			try{
				if("M".equals(paramMap.get("FORM_TYPE"))){
					resultCnt =penworkService.setTotalInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
				}else{
					resultCnt =penworkService.setTotalFinalInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
				}
				resultMap.put("RESULT","Y" );
				if(resultCnt > 0){
					Message="저장 되었습니다.";
					String xmlTempFile = resultMap.get("xmlTempFile").toString();
					String xmlOrginFile= resultMap.get("xmlOrginFile").toString();
					File file = new File(xmlTempFile);
					File fileToMove = new File(xmlOrginFile);
					boolean isMoved = file .renameTo(fileToMove);
				} else{ Message="저장된 내용이 없습니다."; }
			}catch(Exception e){
				resultCnt = -1; Message="저장에 실패하 였습니다.";
				resultMap.put("RESULT","N" );
				resultMap.put("PS_NAME","" );
				resultMap.put("FORM_ID","" );
			}
			
			/*
			PODCreate pod = new PODCreate();

			resultMap = pod.createXmlFile(list,ifMap,headerMap);

			if("N".equals(resultMap.get("RESULT"))){
				Message="양식생성에 실패 했습니다.";
			}else{
				String strUrl = paramMap.get("baseUrl")+"/FilePS.do?FORM_ID="+resultMap.get("FORM_ID")+"&PS_NAME="+resultMap.get("PS_NAME");
				headerMap.put("PS_URL",strUrl);
				resultMap.put("SEQ_T300",headerMap.get("SEQ_T300").toString());
				resultMap.put("FORM_NAME",headerMap.get("IN_LINE").toString() +" "+tdpp201Map.get("FORM_NAME"));

				Map<String, Object> qics200Map = ifMap;
				qics200Map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString() );
				qics200Map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
				qics200Map.put("authIP", headerMap.get("authIP").toString());


				// 5-1 TDPP203의 정보를 가져온다.


				//4-2 TDPP203 + seqT300 + SEQ_Q100 ==> QICS203에 INSERT (페이지수가 2이상일경우 대비해서 LIST형태로
				List bglist = new ArrayList();
				Map qics203Map=new HashMap();
				HashMap  map 		= null;
				List  insertRows		= new ArrayList ();
				bglist=penworkService.getBgImage(tdpp201Map);

				for (int i = 0; i < bglist.size(); i++) {

					map =   (HashMap) bglist.get(i);

					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());

					insertRows.add(map);
				}
				qics203Map.put( "insertRows" , insertRows );

				//6-1

				List fieldlist = new ArrayList();
				Map qics205Map=new HashMap();
				map 		= null;
				insertRows		= new ArrayList ();
				fieldlist=penworkService.getFieldInfo(tdpp201Map);

				for (int i = 0; i < fieldlist.size(); i++) {
					map = new HashMap<String, String>();
					map =   (HashMap) fieldlist.get(i);
					String erpCname = (String) map.get("ERP_CNAME");
					String fieldName = (String) map.get("FIELD_NAME");
					if(erpCname !=null && !"".equals(erpCname)){

						if(ifMap.get(erpCname) != null && !"".equals(ifMap.get(erpCname))){
							//System.out.println("ifMap = "+ ifMap);
							if("ORDER_NUMBER".equals(erpCname) &&ifMap.containsKey("ORDER_NUMBER") && ifMap.containsKey("LINE_NUMBER")){
								String orderNumber= ifMap.get("ORDER_NUMBER").toString();
								String lineNumber =  ifMap.get("LINE_NUMBER").toString();
								if("SD9".equals(orderNumber) || "DZ9".equals(orderNumber) || "EZ9".equals(orderNumber) || "HOOP".equals(orderNumber)){
									map.put("FIELD_POD",orderNumber);
								}else{
									map.put("FIELD_POD",orderNumber+"-"+lineNumber);
								}

								map.put("USER_INPUT_VALUE",ifMap.get("ORDER_NUMBER")+"-"+ifMap.get("LINE_NUMBER"));
							}else{
								map.put("FIELD_POD",ifMap.get(erpCname));
								map.put("USER_INPUT_VALUE",ifMap.get(erpCname));
							}


						} else {
							if(headerMap.containsKey(fieldName)){
								map.put("FIELD_POD",headerMap.get(fieldName));
								map.put("USER_INPUT_VALUE",headerMap.get(fieldName));
							} else {
								map.put("FIELD_POD","");
								map.put("USER_INPUT_VALUE","");
							}
						}
					}else{
						if(fieldName != null && !"".equals(fieldName)){
							if(headerMap.containsKey(fieldName)){
								map.put("FIELD_POD",headerMap.get(fieldName));
								map.put("USER_INPUT_VALUE",headerMap.get(fieldName));
							}
						} else {
							map.put("FIELD_POD","");
							map.put("USER_INPUT_VALUE","");
						}

					}


					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());
					//System.out.println("1111111111 map =  "+map);
					insertRows.add(map);
				}
				qics205Map.put( "insertRows" , insertRows );

				int resultCnt = -1;
				try{
					if("M".equals(paramMap.get("FORM_TYPE"))){
						resultCnt =penworkService.setTotalInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
					}else{
						resultCnt =penworkService.setTotalFinalInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
					}
					if(resultCnt > 0){
						Message="저장 되었습니다.";
						String xmlTempFile = resultMap.get("xmlTempFile").toString();
						String xmlOrginFile= resultMap.get("xmlOrginFile").toString();
						File file = new File(xmlTempFile);
						File fileToMove = new File(xmlOrginFile);
						boolean isMoved = file .renameTo(fileToMove);
					} else{ Message="저장된 내용이 없습니다."; }
				}catch(Exception e){
					resultCnt = -1; Message="저장에 실패하 였습니다.";
					resultMap.put("RESULT","N" );
					resultMap.put("PS_NAME","" );
					resultMap.put("FORM_ID","" );
				}
			}*/
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return resultMap;
	}
	private Map<String, Object> defaultInfo(Map<String, Object> headerMap)  {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//System.out.println("defaultInfo = "+headerMap);
			Map<?,?> dataMap=penworkService.getPenWorkInfo(headerMap);
			//System.out.println("dataMap = "+dataMap);
			resultMap.put("IN_FACT",dataMap.get("IN_FACT"));
			resultMap.put("IN_ORDER",dataMap.get("IN_ORDER"));
			resultMap.put("IN_LINE",dataMap.get("IN_LINE"));
			resultMap.put("IN_TYPE",dataMap.get("IN_TYPE"));
			//resultMap.put("R_SUPPLY_THICKNESS",headerMap.get("MRG_R_SUPPLY_THICKNESS"));
			resultMap.put("STEEL_TYPE",dataMap.get("MRG_STEEL_TYPE"));
			resultMap.put("R_SUPPLIER",dataMap.get("MRG_R_SUPPLIER"));
			resultMap.put("TARGET_THICKNESS",dataMap.get("MRG_R_SUPPLY_THICKNESS"));
			resultMap.put("LAST_WIDTH",dataMap.get("MRG_R_WIDTH"));
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return resultMap;
	}
	/**
	 * 메인 전자펜 작업 최종검사 분할정보
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkFinalDivideInfo")
	public ModelAndView setPenWorkFinalDivideInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkFinalDivideInfo Start");

		String Message = "";

		//List list = new ArrayList();
		RFCInterFace app = new RFCInterFace();
		HashMap hm = new HashMap();

		/*String POC_NO = "";
		String POC_NO01 = paramMap.get("POC_NO01").toString();
		String POC_NO02 = paramMap.get("POC_NO02").toString();
		String POC_NO03 = paramMap.get("POC_NO03").toString();
		if(POC_NO03 !=null && !"".equals(POC_NO03)){
			POC_NO = POC_NO01+"-"+POC_NO02+"-"+POC_NO03;
		}else{
			POC_NO = POC_NO01+"-"+POC_NO02;
		}*/
		//paramMap.put("POC_NO",paramMap.get(""));
		//System.out.println("paramMap = "+paramMap);
		hm = app.divideList2(paramMap);
		
		List<?> list = penworkService.getSalesEndUser(paramMap);
/*
		Iterator keyIterator = hm.keySet().iterator();
		while( keyIterator.hasNext() ){
			String name = (String)keyIterator.next();
			//System.out.println("name= "+name);
			CommonCollect collect= (CommonCollect) hm.get(name);
			int idx = 0;
			if(collect ==null){
			}else{
				while (collect.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for(int i=0;i<collect.getColumnCount();i++) {
						map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
						//Log.Debug(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
					}
					if("cusor1".equals(name)){
						List<Entry> entryList = new ArrayList<Entry>(map.entrySet());
						list.add(idx, map);
						Log.Debug("주문정보 상세 = "+idx+" : "+map);
						idx++;
					}

					//	list.set(idx, map);
					//list = entryList;
				}
			}

		}
*/
		//System.out.println(" list.size() = "+ list.size());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		mv.addObject("Message", Message);
		Log.Debug("PenWorkController.setPenWorkFinalDivideInfo End");

		return mv;
	}
	/**
	 * 메인 전자펜 작업 최종검사 직접입력
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkFinalDirect")
	public ModelAndView setPenWorkFinalDirect(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkFinalDirect Start");

		String Message = "";
		Map<String, Object> convertMap = ParamUtils.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("authIP", request.getRemoteAddr());
		//신규 QICS 값을 가져온다.
		List list = new ArrayList();
		String authIP = request.getRemoteAddr();
		paramMap.put("authIP", authIP);
		paramMap.put("PRE_SEQ_Q100", paramMap.get("SEQ_Q100"));
		List<?> list1 = codeService.getPodCodeList(paramMap);
		//System.out.println("list1 = "+ list1.size());
		String strValue ="";
		for (int i = 0; i < list1.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) list1.get(i);
			String tempStr1 = hm.get("CODE_GUBUN").toString();
			String tempStr2 = hm.get("ATTRIBUTE2").toString();
			if("DEPT_NAME".equals(tempStr1)){
				paramMap.put("C_FRM_DPT", hm.get("CODE"));
			}else{
				if("품질검사".equals(tempStr2)){
					paramMap.put("C_APP",hm.get("CODE"));
				}
			}
		}
		//System.out.println("paramMap = "+ paramMap);
		Map<String, Object> resultMap =new HashMap<String, Object>();

		List<?>  insertRows		= new ArrayList<Serializable>();
		insertRows = (List<?>) convertMap.get("insertRows");
		//System.out.println("insertRows = "+ insertRows);
		for (int i = 0; i < insertRows.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) insertRows.get(i);
			Map<?,?> seqMap=penworkService.getSeqQ100(hm);
			String SEQ_Q100 = seqMap.get("SEQ_Q100").toString();
			hm.put("SEQ_Q100",SEQ_Q100);

			String formCode ="";
			String pocNo2 ="";
			String checkData="";

			Iterator keyIterator = paramMap.keySet().iterator();
			while( keyIterator.hasNext() ){
				String name = (String)keyIterator.next();
				String value = paramMap.get(name).toString();

				if(!hm.containsKey(name)){
					hm.put(name,value);
				}
			}



			resultMap= createWebData(hm);


			if(resultMap.isEmpty() || resultMap.size() == 0){

			}else{
				list.add(resultMap);
			}

		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		mv.addObject("Message", Message);
		Log.Debug("PenWorkController.setPenWorkFinalDirect End");

		return mv;
	}

	/**
	 * 메인 전자펜 작업 최종검사 인쇄요청
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkFinalPrint")
	public ModelAndView setPenWorkFinalPrint(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkFinalPrint Start");

		String Message = "";
		List list = new ArrayList();
		try {
			Map<String, Object> convertMap = ParamUtils.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
			convertMap.put("authIP", request.getRemoteAddr());
			//신규 QICS 값을 가져온다.

			String authIP = request.getRemoteAddr();
			paramMap.put("authIP", authIP);
			String baseUrl = StringUtil.getBaseUrl(request);
			paramMap.put("baseUrl", baseUrl);
			paramMap.put("PRE_SEQ_Q100", paramMap.get("SEQ_Q100"));
			List<?> list1 = codeService.getPodCodeList(paramMap);
			String strValue ="";
			for (int i = 0; i < list1.size(); i++) {
				HashMap hm = new HashMap();
				hm = (HashMap) list1.get(i);
				String tempStr1 = hm.get("CODE_GUBUN").toString();
				String tempStr2 = hm.get("ATTRIBUTE2").toString();
				if("DEPT_NAME".equals(tempStr1)){
					paramMap.put("C_FRM_DPT", hm.get("CODE"));
				}else{
					if("품질검사".equals(tempStr2)){
						paramMap.put("C_APP",hm.get("CODE"));
					}
				}
			}


			Map<String, Object> resultMap =new HashMap<String, Object>();

			List<?>  insertRows		= new ArrayList<Serializable>();
			insertRows = (List<?>) convertMap.get("insertRows");
			Map  ifMap =new HashMap();
			for (int i = 0; i < insertRows.size(); i++) {
				HashMap hm = new HashMap();
				hm = (HashMap) insertRows.get(i);
				Map<?,?> seqMap=penworkService.getSeqQ100(hm);
				String SEQ_Q100 = seqMap.get("SEQ_Q100").toString();
				hm.put("SEQ_Q100",SEQ_Q100);

				String formCode ="";
				String pocNo2 ="";
				String checkData="";

				Iterator keyIterator = paramMap.keySet().iterator();
				while( keyIterator.hasNext() ){
					String name = (String)keyIterator.next();
					String value = paramMap.get(name).toString();

					if(!hm.containsKey(name)){
						hm.put(name,value);
					}
				}
				resultMap = createXmlData(hm);

				if(resultMap.isEmpty() || resultMap.size() == 0){

				}else{
					list.add(resultMap);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


		//	Map<?,?> map = penworkService.getPenWorkInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		mv.addObject("Message", Message);
		Log.Debug("PenWorkController.setPenWorkFinalPrint End");

		return mv;
	}
	@RequestMapping(params = "cmd=penWorkFinalPrint2")
	public ModelAndView setPenWorkFinalPrint2(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.setPenWorkFinalPrint2 Start");
		
		String Message = "";
		String seqQics100 = "";
		String seqTemp = "";
		List list = new ArrayList();
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(paramMap);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		try {
			Map<String, Object> convertMap = ParamUtils.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
			convertMap.put("authIP", request.getRemoteAddr());
			//신규 QICS 값을 가져온다.
			
			String authIP = request.getRemoteAddr();
			paramMap.put("authIP", authIP);
			String baseUrl = StringUtil.getBaseUrl(request);
			paramMap.put("baseUrl", baseUrl);
			paramMap.put("PRE_SEQ_Q100", paramMap.get("SEQ_Q100"));
			List<?>  insertRows		= new ArrayList<Serializable>();
			insertRows = (List<?>) convertMap.get("insertRows");
			Map  ifMap =new HashMap();
			List<?> list1 = codeService.getPodCodeList(paramMap);
			
			//분할일경우
			if(!"0000".equals(paramMap.get("POC_NUMBER"))){
				for (int i = 0; i < insertRows.size(); i++) {
					
					Map<String, Object> resultMap =new HashMap<String, Object>();
					String strValue ="";
					for (int j = 0; j < list1.size(); j++) {
						HashMap hm = new HashMap();
						hm = (HashMap) list1.get(j);
						String tempStr1 = hm.get("CODE_GUBUN").toString();
						String tempStr2 = hm.get("ATTRIBUTE2").toString();
						if("DEPT_NAME".equals(tempStr1)){
							paramMap.put("C_FRM_DPT", hm.get("CODE"));
						}else{
							if("품질검사".equals(tempStr2)){
								paramMap.put("C_APP",hm.get("CODE"));
							}
						}
					}
					
					HashMap hm = new HashMap();
					hm = (HashMap) insertRows.get(i);
					Map<?,?> seqMap=penworkService.getSeqQ100(hm);
					String SEQ_Q100 = seqMap.get("SEQ_Q100").toString();
					if("".equals(seqQics100)) {
						seqQics100 = SEQ_Q100;
					}else {
						seqQics100 = seqQics100+","+SEQ_Q100;
					}
					hm.put("SEQ_Q100",SEQ_Q100);
					
					String formCode ="";
					String pocNo2 ="";
					String checkData="";
					
					Iterator keyIterator = paramMap.keySet().iterator();
					while( keyIterator.hasNext() ){
						String name = (String)keyIterator.next();
						String value = paramMap.get(name).toString();
						
						if(!hm.containsKey(name)){
							hm.put(name,value);
						}
					}
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println(hm);
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					
					int resultCnt = -1;
					Map<?,?> seqMap2=penworkService.getSeqTemp(hm);
					String SEQ_TEMP = seqMap2.get("SEQ_TEMP").toString();
					if("".equals(seqTemp)) {
						seqTemp = SEQ_TEMP;
					}else {
						seqTemp = seqTemp+","+SEQ_TEMP;
					}
					hm.put("SEQ_TEMP", SEQ_TEMP);
					
					resultCnt = penworkService.setPrintF(hm);
					
					if(resultCnt > 0) {			
						resultMap.put("RESULT", "Y");
						System.out.println("!!!!!!!!!!!!!!!!!!hm!!!!!!!!12!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println(hm);
						System.out.println("!!!!!!!!!!!!!!!!!!hm!!!!!!132!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						penworkService.setPrintF2(hm);
					}else {			
						resultMap.put("RESULT", "N");
					}
					list.add(resultMap);
					penworkService.setPrintM(hm);		
					
				}
			}else {
				Map<String, Object> resultMap =new HashMap<String, Object>();
				String strValue ="";
				for (int i = 0; i < list1.size(); i++) {
					HashMap hm = new HashMap();
					hm = (HashMap) list1.get(i);
					String tempStr1 = hm.get("CODE_GUBUN").toString();
					String tempStr2 = hm.get("ATTRIBUTE2").toString();
					if("DEPT_NAME".equals(tempStr1)){
						paramMap.put("C_FRM_DPT", hm.get("CODE"));
					}else{
						if("품질검사".equals(tempStr2)){
							paramMap.put("C_APP",hm.get("CODE"));
						}
					}
				}
				
				penworkService.setPrintM(paramMap);		
				seqQics100 = paramMap.get("SEQ_Q100").toString();
				seqTemp = paramMap.get("SEQ_TEMP").toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//	Map<?,?> map = penworkService.getPenWorkInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		mv.addObject("Message", Message);
		mv.addObject("seqQics100", seqQics100);
		mv.addObject("seqTemp", seqTemp);
		Log.Debug("PenWorkController.setPenWorkFinalPrint2 End");
		
		return mv;
	}
	private Map createInterFaceMap(Map ifMap, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		ifMap.put("IN_FACT",paramMap.get("IN_FACT"));
		ifMap.put("IN_ORDER",paramMap.get("IN_ORDER"));
		ifMap.put("IN_LINE",paramMap.get("IN_LINE"));
		ifMap.put("IN_TYPE",paramMap.get("IN_TYPE"));
		/*		ifMap.put("MRG_R_SUPPLY_THICKNESS",paramMap.get("MRG_R_SUPPLY_THICKNESS"));
		ifMap.put("MRG_R_WIDTH",paramMap.get("MRG_R_WIDTH"));
		ifMap.put("PARTIAL_WEIGHT",paramMap.get("PARTIAL_WEIGHT"));*/
		return ifMap;
	}
	/*
	private Map<String, Object> createImportData(Map<String, Object> paramMap, Map ifMap) {
		// TODO Auto-generated method stub

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		String Message = "";
		String POC_NO = "";
		String POC_NO01 = paramMap.get("POC_NO01").toString();
		String POC_NO02 = paramMap.get("POC_NO02").toString();
		String POC_NO03 = paramMap.get("POC_NO03").toString();
		if(POC_NO03 !=null && !"".equals(POC_NO03)){
			POC_NO = POC_NO01+"-"+POC_NO02+"-"+POC_NO03;
		}else{
			POC_NO = POC_NO01+"-"+POC_NO02;
		}
		headerMap.put("POC_NO",POC_NO);
		headerMap.put("POC_NO01",POC_NO01);
		headerMap.put("POC_NO02",POC_NO02);
		headerMap.put("POC_NO03",POC_NO03);
		headerMap.put("PRE_SEQ_Q100", paramMap.get("PRE_SEQ_Q100"));
		String CHECK_DATA  = ifMap.get("CHECK_DATA").toString();
		if(ifMap.containsKey("MRG_R_SUPPLY_THICKNESS")){
			headerMap.put("C_STD01",ifMap.get("MRG_R_SUPPLY_THICKNESS")); //POC No.

		}
		if(ifMap.containsKey("MRG_R_WIDTH")){
			headerMap.put("C_STD02",ifMap.get("MRG_R_WIDTH")); //POC No.
		}

		if("".equals(CHECK_DATA) || CHECK_DATA == null){

		}else{
			String [] DATA = CHECK_DATA.split(",");

			for (int i = 0; i < DATA.length; i++) {
				String stampValue = "";
				String stampName="C_STAMP0"+(i+1);
				stampValue =DATA[i];

				if("".equals(stampValue) || stampValue==null){

				}else{
					headerMap.put(stampName+"_POD",stampValue);
					headerMap.put(stampName,"QICS_STAMP_BASE.png");
				}

			}
		}

		try {
			Map<?,?> tdpp201Map=penworkService.getPadMaster(paramMap);
			System.out.println("tdpp201Map = "+tdpp201Map);
			//TDPP300 시퀀스를 만든다.
			Map<?,?> seqMap=penworkService.getSeqT300(paramMap);
			String SEQ_T300 = seqMap.get("SEQ_T300").toString();

			headerMap.put("C_FRM_ID",paramMap.get("LINE_CODE").toString() +" "+tdpp201Map.get("FORM_NAME")); //시스템 양식지명
			headerMap.put("C_SEQ",SEQ_T300); //시스템 양식지명
			headerMap.put("C_MAIN_PRC",paramMap.get("LINE_CODE").toString()); //공정명
			headerMap.put("C_POC_NO01_POD",POC_NO01); //POC No.
			headerMap.put("C_POC_NO02_POD",POC_NO02); //POC No.
			headerMap.put("C_POC_NO03_POD",POC_NO03); //POC No.
			headerMap.put("C_CHK_DT",paramMap.get("WORK_DATE").toString()); //검사일자
			headerMap.put("FORM_CODE",paramMap.get("FORM_CODE").toString()); //검사일자
			headerMap.put("FORM_ID",tdpp201Map.get("FORM_ID")); //검사일자
			headerMap.put("STATUS_CODE","P"); //검사일자
			headerMap.put("WEB_DATA_YN","N"); //검사일자
			ConfigValue cfv = new ConfigValue();
			headerMap.put("C_FRM_NO",cfv.devCfrmNo);
			headerMap.put("C_FRM_DPT",cfv.devCfrmDpt);
			headerMap.put("SEQ_Q100",ifMap.get("SEQ_Q100"));
			headerMap.put("IN_FACT",ifMap.get("IN_FACT"));
			headerMap.put("IN_ORDER",ifMap.get("IN_ORDER"));
			headerMap.put("IN_LINE",ifMap.get("IN_LINE"));
			headerMap.put("IN_TYPE",ifMap.get("IN_TYPE"));
			headerMap.put("MRG_R_SUPPLY_THICKNESS",ifMap.get("MRG_R_SUPPLY_THICKNESS"));
			headerMap.put("MRG_R_WIDTH",ifMap.get("MRG_R_WIDTH"));
			headerMap.put("PARTIAL_WEIGHT",ifMap.get("PARTIAL_WEIGHT"));
			String [] capp = cfv.devCapp.split("_");
			for (int i = 0; i < capp.length; i++) {
				headerMap.put("C_APP0"+(i+1), capp[i]);

			}

			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


			headerMap.put("C_FRM_PRN","PRINT DATE : "+sdf.format(dt).toString() +" ("+paramMap.get("LINE_CODE").toString() +")");

			headerMap.put("FORM_SEQ",tdpp201Map.get("FORM_SEQ"));
			headerMap.put("PAD_COUNT",tdpp201Map.get("PAD_COUNT"));
			headerMap.put("FORM_NAME",tdpp201Map.get("FORM_NAME"));
			headerMap.put("SEQ_T300", SEQ_T300);
			//STEMP 정보를 가져온다.
			//headerMap.put("C_STAMP1","QICS_STAMP_BASE.png"); //형상약도 IMAGE
			//headerMap.put("C_STAMP1_POD","현대자동차"); //형상약도 TEXT


			//전자펜 양식지 필드(TDPP203)를 가져온다. (dynamic)
			List list = new ArrayList();
			list=penworkService.getPadDynamic(headerMap);
			System.out.println("dataMap = "+ headerMap);
			PODCreate pod = new PODCreate();

			resultMap = pod.createXmlFile(list,ifMap,headerMap);
			System.out.println("headerMap = "+ headerMap);
			if("N".equals(resultMap.get("RESULT"))){
				Message="양식생성에 실패 했습니다.";

			}else{

				//4. QICS200 Table(검사공통정보 POD-IF) DATA입력
				Map<String, Object> qics200Map = ifMap;
				qics200Map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString() );
				qics200Map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
				qics200Map.put("authIP", paramMap.get("authIP").toString());


				// 5-1 TDPP203의 정보를 가져온다.

				//4-2 TDPP203 + seqT300 + SEQ_Q100 ==> QICS203에 INSERT (페이지수가 2이상일경우 대비해서 LIST형태로
				List bglist = new ArrayList();
				Map qics203Map=new HashMap();
				HashMap  map 		= null;
				List  insertRows		= new ArrayList ();
				bglist=penworkService.getBgImage(tdpp201Map);

				for (int i = 0; i < bglist.size(); i++) {

					map =   (HashMap) bglist.get(i);

					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());

					insertRows.add(map);
				}
				qics203Map.put( "insertRows" , insertRows );

				//6-1

				List fieldlist = new ArrayList();
				Map qics205Map=new HashMap();
				map 		= null;
				insertRows		= new ArrayList ();
				fieldlist=penworkService.getFieldInfo(tdpp201Map);

				for (int i = 0; i < fieldlist.size(); i++) {
					map = new HashMap<String, String>();
					map =   (HashMap) fieldlist.get(i);
					String erpCname = (String) map.get("ERP_CNAME");
					String fieldName = (String) map.get("FIELD_NAME");
					if(erpCname !=null && !"".equals(erpCname)){
						map.put("FIELD_POD",ifMap.get(erpCname));
					}else{
						map.put("FIELD_POD","");
					}
					if(fieldName != null && !"".equals(fieldName)){
						if(paramMap.containsKey(fieldName)){
							map.put("FIELD_POD",paramMap.get(fieldName));
						}
					}

					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());

					insertRows.add(map);
				}
				qics205Map.put( "insertRows" , insertRows );

				int resultCnt = -1;
				try{
					resultCnt =penworkService.setTotalFinalInsert(qics200Map,qics203Map,qics205Map,headerMap); //TEST 완료
					if(resultCnt > 0){ Message="저장 되었습니다."; } else{ Message="저장된 내용이 없습니다."; }
				}catch(Exception e){
					resultCnt = -1; Message="저장에 실패하 였습니다.";
					resultMap.put("RESULT","N" );
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("RESULT","N" );
		}

		return resultMap;
	}*/
	/**
	 * 메인 전자펜 작업 보정화면
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=penWorkView")
	public ModelAndView getPenWorkView(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getPenWorkView Start");

		String Message = "";

		List<?> result = penworkService.getPenWorkView(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("result", result);
		Log.Debug("PenWorkController.getPenWorkView End");

		return mv;
	}
}
