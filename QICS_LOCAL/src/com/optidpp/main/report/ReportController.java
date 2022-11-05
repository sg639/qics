package com.optidpp.main.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.optidpp.common.util.StringUtil;
import com.optidpp.main.code.CodeService;
import com.optidpp.main.penwork.PenWorkService;
import com.optidpp.main.view.ViewService;
import com.optidpp.podservice.pod.PODCreate;
import com.optidpp.zrfc.common.ConfigValue;


/**
 *  품질이상보고서생성
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Report.do")
public class ReportController {
	@Inject
	@Named("ReportService")
	private ReportService reportService;

	@Inject
	@Named("PenWorkService")
	private PenWorkService penworkService;

	@Inject
	@Named("ViewService")
	private ViewService viewService;

	@Inject
	@Named("CodeService")
	private CodeService codeService;
	/**
	 * 메인 품질이상보고서
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=reportBGPopup")
	public String getReportBGPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.getReportBGPopup Start");
		Log.Debug("ReportController.getReportBGPopup End");
		return "/main/popup/report";
	}
	/**
	 * 메인 품질이상보고서
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=reportViewBGPopup")
	public String getReportViewPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.getReportViewPopup Start");
		Log.Debug("ReportController.getReportViewPopup End");
		return "/main/popup/reportView";
	}

	/**
	 * 팝업메인 품질이상보고서
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=reportViewBGPopupExtra")
	public String getReportViewPopupExtra(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.reportViewBGPopupExtra Start");
		Log.Debug("ReportController.reportViewBGPopupExtra End");
		return "/main/popup/extraReportView";
	}

	/**
	 * 메인 품질이상보고서 결과
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */

	@RequestMapping(params = "cmd=viewResultBGReport")
	public ModelAndView getViewResultBGReport(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.getViewResultBGReport Start");
		Log.Debug("paramMap " + paramMap);
		//Map<?, ?> map = orginService.getOrginInfo(paramMap);
		List<?> list = reportService.getViewResultBGReport(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("ReportController.getViewResultBGReport End");
		return mv;
	}

	/**
	 * 메인 품질이상보고서 Info
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */

	@RequestMapping(params = "cmd=viewBGReport")
	public ModelAndView getViewBGReport(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.getViewBGReport Start");
		Log.Debug("paramMap " + paramMap);
		//Map<?, ?> map = orginService.getOrginInfo(paramMap);
		List<?> list = reportService.getViewBGReport(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("ReportController.getViewBGReport End");
		return mv;
	}

	/**
	 * 메인 품질이상보고서 인쇄요청
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */

	@RequestMapping(params = "cmd=reportPrint")
	public ModelAndView setReportPrint(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.setReportPrint Start");
		String Message = "";
		//Map<?, ?> map = orginService.getOrginInfo(paramMap);
		Message="품질이상보고서 인쇄요청입니다.";
		/*입력값을 설정한다.*/
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String authIP = request.getRemoteAddr();
		paramMap.put("authIP", authIP);
		String baseUrl = StringUtil.getBaseUrl(request);
		paramMap.put("baseUrl", baseUrl);
		List<?> list = codeService.getPodCodeList2(paramMap);
		String value ="";
		for (int i = 0; i < list.size(); i++) {
			HashMap hm = new HashMap();
			hm = (HashMap) list.get(i);
			String tempStr1 = hm.get("CODE_GUBUN").toString();
			String tempStr2 = hm.get("ATTRIBUTE2").toString();
			if("DEPT_NAME".equals(tempStr1)){
				paramMap.put("C_FRM_DPT", hm.get("CODE"));
			}else{
				if("품질이상보고서".equals(tempStr2)){
					paramMap.put("C_APP",hm.get("CODE"));
				}
			}
		}

		//System.out.println("paramMap = " +paramMap.toString());
		resultMap = createXmlData(paramMap);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		//mv.addObject("result", map);
		mv.addObject("Message", resultMap.get("Message"));
		mv.addObject("DATA", resultMap);
		Log.Debug("ReportController.setReportPrint End");
		return mv;
	}
	private Map<String, Object> createXmlData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> headerMap = new HashMap<String, Object>();
		String Message="";
		try {
			Map<?,?> tdpp201Map=penworkService.getPadMaster(paramMap);
			Map<?,?> seqMap=penworkService.getSeqT300(paramMap);
			String SEQ_T300 = seqMap.get("SEQ_T300").toString();
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
			headerMap.put("C_POC_NO",POC_NO);
			headerMap.put("authIP", paramMap.get("authIP"));
			headerMap.put("C_FRM_ID",""); //시스템 양식지명
			headerMap.put("FORM_ID",tdpp201Map.get("FORM_ID")); //검사일자
			headerMap.put("FORM_SEQ",tdpp201Map.get("FORM_SEQ"));
			headerMap.put("PRINT_PAGE",tdpp201Map.get("PAD_COUNT"));
			headerMap.put("FORM_NAME",tdpp201Map.get("FORM_NAME"));
			headerMap.put("SEQ_T300", SEQ_T300);
			headerMap.put("C_SEQ", SEQ_T300);
			headerMap.put("IN_LINE",paramMap.get("IN_LINE").toString());
			headerMap.put("FORM_CODE",paramMap.get("FORM_CODE").toString()); //검사일자
			headerMap.put("WEB_DATA_YN","N"); //검사일자
			headerMap.put("SEQ_Q100",  paramMap.get("SEQ_Q100").toString());
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			headerMap.put("C_FRM_PRN","PRINT DATE : "+sdf.format(dt).toString() +" ("+paramMap.get("IN_LINE").toString() +")");
			headerMap.put("C_MAIN",paramMap.get("IN_LINE").toString()); //공정명
			sdf = new SimpleDateFormat("yyyy/MM/dd");
			headerMap.put("C_CHK_DT",sdf.format(dt).toString());
			String cNo0 ="I";
			headerMap.put("WORK_DATE",sdf.format(dt).toString());
			String cNo1 = sdf.format(dt).toString().substring(2, 4);
			String cNo2 = sdf.format(dt).toString().substring(5, 7);
			headerMap.put("YY", cNo1);
			headerMap.put("MM", cNo2);
			headerMap.put("IN_LINE", paramMap.get("IN_LINE").toString());
			headerMap.put("FORM_TYPE",paramMap.get("FORM_TYPE")); //검사일자
			String [] capp = paramMap.get("C_APP").toString().split(",");
			for (int i = 0; i < capp.length; i++) {
				headerMap.put("C_APP0"+(i+1), capp[i]);

			}



			String cNo3="";
			if("AP2".equals(paramMap.get("IN_LINE").toString())){
				cNo3="0";
			}else if("BA1".equals(paramMap.get("IN_LINE").toString())){
				cNo3="1";
			}else if("BA2".equals(paramMap.get("IN_LINE").toString())){
				cNo3="2";
			}else if("BA3".equals(paramMap.get("IN_LINE").toString())){
				cNo3="3";
			}else{
				cNo3="Z";
			}
			int idx=0;
			Map<?, ?> seqReportMap = reportService.getReportSeq(headerMap);
			if(seqReportMap.isEmpty()){
				idx=0;
			}else{
				idx = (Integer) seqReportMap.get("REPORT_IDX");
			}
			int f=0;
			int l=0;
			String cNo4="";
			if(idx<10){
				cNo4="0"+idx;
			}else if(idx<=99){
				cNo4=idx+"";
			}else if(idx>99){
				f =(idx/10)+55;
				l = (idx%10);
				cNo4=(char) f +l +"";
			}
			headerMap.put("IDX", idx);
			headerMap.put("SUB_NO", cNo4);
			headerMap.put("FULL_NO", cNo0+"-"+cNo1+"-"+cNo2+"-"+cNo3+cNo4);
			headerMap.put("C_NO", cNo0+"-"+cNo1+"-"+cNo2+"-"+cNo3+cNo4);
			/* Calendar oCalendar = Calendar.getInstance( );
			 System.out.println("현재 년: " +  oCalendar.get(Calendar.YEAR));
			 System.out.println("현재 월: " + (oCalendar.get(Calendar.MONTH) + 1));
			 String yyyy = oCalendar.get(Calendar.YEAR)+"";
			 String mm = (oCalendar.get(Calendar.MONTH) + 1)+"";*/
			List list1 = new ArrayList();
			list1=penworkService.getPadDynamic(paramMap);
			//System.out.println("list1 = " +list1.toString());
			List list2 = new ArrayList();
			list2= viewService.getViewValueInfo(paramMap);
			HashMap dataMap = new HashMap();
			HashMap mp = new HashMap();
			String tmpPageOrder = "";
			ConfigValue cfv = new ConfigValue();
			headerMap.put("C_FRM_NO",cfv.devCfrmNo3);
			for (int i = 0; i < list2.size(); i++) {
				HashMap hm =(HashMap) list2.get(i);
				mp.put(hm.get("FIELD_NAME"),hm.get("USER_INPUT_VALUE"));
			}
			//System.out.println("dataMap = " + dataMap.toString());
			PODCreate pod = new PODCreate();
			resultMap = pod.createXmlFile2(list1,mp,headerMap);


			if("N".equals(resultMap.get("RESULT"))){
				Message="양식생성에 실패 했습니다.";
			}else{
				//4-2 TDPP203 + seqT300 + SEQ_Q100 ==> QICS203에 INSERT (페이지수가 2이상일경우 대비해서 LIST형태로
				String strUrl = paramMap.get("baseUrl")+"/FilePS.do?FORM_ID="+resultMap.get("FORM_ID")+"&PS_NAME="+resultMap.get("PS_NAME");
				headerMap.put("PS_URL",strUrl);
				resultMap.put("SEQ_T300",headerMap.get("SEQ_T300").toString());
				resultMap.put("FORM_NAME",headerMap.get("IN_LINE").toString() +" "+headerMap.get("FORM_NAME"));
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
					String pageOrder =(String) map.get("PAGE_ORDER");
					HashMap hm = new HashMap();
					if(dataMap.containsKey(pageOrder)){
						hm = (HashMap) dataMap.get(pageOrder);
					}

					if(fieldName != null && !"".equals(fieldName)){
						if(headerMap.containsKey(fieldName)){
							map.put("FIELD_POD",headerMap.get(fieldName));
							map.put("USER_INPUT_VALUE",headerMap.get(fieldName));
						}else{
							if(hm.containsKey(fieldName)){
								map.put("FIELD_POD",hm.get(fieldName));
								map.put("USER_INPUT_VALUE",hm.get(fieldName));
							}
						}
					}

					map.put("SEQ_T300", headerMap.get("SEQ_T300").toString());
					map.put("SEQ_Q100", headerMap.get("SEQ_Q100").toString());

					insertRows.add(map);
				}
				qics205Map.put( "insertRows" , insertRows );

				int resultCnt = -1;
				try{
					resultCnt =penworkService.setReportInsert( qics203Map,qics205Map,headerMap); //TEST 완료
					if(resultCnt > 0){ Message="저장 되었습니다.";
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
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return resultMap;
	}
	/**
	 * 메인 품질이상보고서 삭제
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */

	@RequestMapping(params = "cmd=reportDelete")
	public ModelAndView setReportDelete(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ReportController.setReportDelete Start");

		//Map<?, ?> map = orginService.getOrginInfo(paramMap);
		//System.out.println(paramMap);
		String Message = "";
		int resultCnt = -1;
		try{
			resultCnt =reportService.reportDelete(paramMap);
			if(resultCnt > 0){ Message="삭제 되었습니다."; } else{ Message="삭제된 내용이 없습니다."; }
		}catch(Exception e){
			resultCnt = -1; Message="삭제에 실패하 였습니다.";
		}

		//Message = "품질이상보고서 삭제입니다.";

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		//mv.addObject("result", map);

		mv.addObject("Message", Message);
		Log.Debug("ReportController.setReportDelete End");
		return mv;
	}
}
