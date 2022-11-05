package com.optidpp.linkservice.view;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.imcore.AES256Cipher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.optidpp.common.logger.Log;
import com.optidpp.common.util.StringUtil;

/**
 *  로그인
 *
 * @author Choi Sung woo
 *
 */
@Controller
public class LinkViewController {

	@Inject
	@Named("LinkViewService")
	private LinkViewService linkViewService;


	/**
	 * 외부링크 OTP 생성
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/GetAccessKey.do")
	public ModelAndView viewLogin(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("==> LinkViewController.GetAccessKey Start");
		paramMap.put("domain",	request.getServerName());
		
		String LINE = "";
		String POCNO = "";
		String POCNO01 = "";
		String POCNO02 = "";
		String WD = "";
		String OTP = "";
		
		String authUUID = UUID.randomUUID().toString();

		paramMap.put("OTP",authUUID);
		paramMap.put("REQ_IP",request.getRemoteAddr());

		
		int resultCnt = -1;
		try{
			resultCnt =linkViewService.setAccessKey(paramMap);
		}catch(Exception e){
			resultCnt = -1; 
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resultCnt>0) {
			resultMap.put("OTP", authUUID);
			
			if(paramMap!=null&&paramMap.containsKey("ISUDEV")){
				LINE = paramMap.containsKey("LINE")?(String)paramMap.get("LINE"):"BA1";
				POCNO = paramMap.containsKey("POCNO")?(String)paramMap.get("POCNO"):"C1333A285-0000";
						
				if(POCNO!=null&&POCNO.length()==9) {
								POCNO01 = POCNO;
								POCNO02 = "0000";
				} else if(POCNO!=null&&POCNO.length()>9&&POCNO.indexOf("-")>7) {
					String[] splitPOC = POCNO.split("-");
					POCNO01 = splitPOC[0];
					POCNO02 = splitPOC[1];
				} else{
					POCNO01 = POCNO;
					POCNO02 = "";
				}
				WD = paramMap.containsKey("WD")?(String)paramMap.get("WD"):"";
				if(WD!=null&&WD.indexOf("-")>-1){
					WD = WD.replaceAll("-", "/");
				}
				if(WD!=null&&WD.indexOf(".")>-1){
					WD = WD.replaceAll(".", "/");
				}
				if(WD!=null&&WD.length()==8){
					WD = WD.substring(0, 4)+"/"+WD.substring(4, 6)+"/"+WD.substring(6, 8);
				}
				
				String p = "LINE="+LINE+"&POCNO="+POCNO+"&WD="+WD+"&OTP="+authUUID;
				
				resultMap.put("P", p);
				resultMap.put("LINE", LINE);
				resultMap.put("POCNO", POCNO);
				resultMap.put("POCNO01", POCNO01);
				resultMap.put("POCNO02", POCNO02);
				resultMap.put("WD", WD);
				resultMap.put("LINKURL", URLEncoder.encode(AES256Cipher.AES_Encode(p), "UTF-8") );
			}
			
		} else {
			resultMap.put("OTP", "ERROR");
			//resultMap.put("OTP_ENC", "호출방식을 확인해주세요.");
			//resultMap.put("OTP_DEC", "");
		}
		//resultMap.put("Message", Message);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("RESULT", resultMap);
		
		Log.Debug("==> LinkViewController.GetAccessKey End");

		return mv;
	}
	
	/**
	 * 외부연결뷰
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	//@RequestMapping(params = "cmd=viewLogin")
	@RequestMapping("/QICSLinkView.do")
	public ModelAndView qicsLinkView(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("==> LinkViewController.QICSLinkView Start");
		
		//--LINE=BA1&POCNO=C15339803-0000&WD=20160125&OTP=
		String LINE = "";
		String POCNO = "";
		String POCNO01 = "";
		String POCNO02 = "";
		String WD = "";
		String OTP = "";
		String OTYPE = "";
		
		ModelAndView mv = new ModelAndView();
		
		if(paramMap!=null && paramMap.containsKey("ISUDEV")) {
			LINE = paramMap.containsKey("LINE")?(String)paramMap.get("LINE"):"BA1";
			POCNO = paramMap.containsKey("POCNO")?(String)paramMap.get("POCNO"):"C1333A285-0000";
					
			if(POCNO!=null&&POCNO.length()==9) {
							POCNO01 = POCNO;
							POCNO02 = "0000";
			} else if(POCNO!=null&&POCNO.length()>9&&POCNO.indexOf("-")>7) {
				String[] splitPOC = POCNO.split("-");
				POCNO01 = splitPOC[0];
				POCNO02 = splitPOC[1];
			} else{
				POCNO01 = POCNO;
				POCNO02 = "";
			}
			WD = paramMap.containsKey("WD")?(String)paramMap.get("WD"):"";
			if(WD!=null&&WD.indexOf("-")>-1){
				WD = WD.replaceAll("-", "/");
			}
			if(WD!=null&&WD.indexOf(".")>-1){
				WD = WD.replaceAll(".", "/");
			}
			if(WD!=null&&WD.length()==8){
				WD = WD.substring(0, 4)+"/"+WD.substring(4, 6)+"/"+WD.substring(6, 8);
			}
			if(WD==null||"".equals(WD)){
				WD="9999/12/31";
			}
			mv.addObject("LINE", LINE);
			mv.addObject("POC_NO", POCNO);
			mv.addObject("POC_NO01", POCNO01);
			mv.addObject("POC_NO02", POCNO02);
			mv.addObject("WD", WD);
			mv.addObject("OTP", OTP);
		} else {
			if(paramMap!=null && paramMap.containsKey("p")) {
				String enc_param_str = paramMap.get("p").toString();
				//enc_param_str = URLDecoder.decode(enc_param_str,"UTF-8");
				Log.Debug("enc_param_str " + enc_param_str);
				try {
					String dec_param_str = AES256Cipher.AES_Decode(enc_param_str);
					Log.Debug("dec_param_str " + dec_param_str);
					
					Map<String, String> covertMap = StringUtil.getQueryMap(dec_param_str);
					Log.Debug("getOTP " + covertMap.get("OTP"));
	
					Map<?, ?> map = linkViewService.checkAccessKey(covertMap);
					//--LINE=BA1&POCNO=C15339803-0000&WD=20160125&OTP=
					
	
					if(map!=null && map.size()>0) {
						/*******************************
						 * 임성현 수정부(2016-04-14)
						 * 수정내용 : ERP에서 QICS원문보기 조회 시 기존의 원문화면을 
						 *        출력하는 것이 아니라 보정화면을 출력하도록 분기문 추가
						 *        ERP에서 파라메터를 추가하여 QICS 호출
						 *        추가  파라메터명 : OTYPE
						 *        파라메터 값 : ERP
						 *        ※ POP은 파라메터를 추가 하지 않아도 됨 
						 *        원문 : mv.setViewName("linkservice/linkView");
						*******************************/
						OTYPE = covertMap.containsKey("OTYPE")?(String)covertMap.get("OTYPE"):"";
						Log.Debug("getOTYPE " + covertMap.get("OTYPE"));
						
						if(OTYPE.trim().equals("ERP"))
						{
							mv.setViewName("linkservice/linkViewNew"); 
						}
						else
						{
							mv.setViewName("linkservice/linkView");
						}
						//mv.setViewName("linkservice/linkView");
						
						paramMap.put("domain",	request.getServerName());
						Log.Debug("covertMap " + covertMap);
						String authUUID = UUID.randomUUID().toString();
						session.setAttribute("authUUID", authUUID);
						session.setAttribute("authIP", request.getRemoteAddr());
						
						LINE = covertMap.containsKey("LINE")?(String)covertMap.get("LINE"):"";
						POCNO = covertMap.containsKey("POCNO")?(String)covertMap.get("POCNO"):"";
						if(POCNO!=null&&POCNO.length()==9) {
							POCNO01 = POCNO;
							POCNO02 = "0000";
							
							POCNO = POCNO01 + "-" + POCNO02;
						} else if(POCNO!=null&&POCNO.length()>9&&POCNO.indexOf("-")>7) {
							String[] splitPOC = POCNO.split("-");
							POCNO01 = splitPOC[0];
							POCNO02 = splitPOC[1];
						} else{
							POCNO01 = POCNO;
							POCNO02 = "";
						}
						WD = covertMap.containsKey("WD")?(String)covertMap.get("WD"):"";
						if(WD!=null&&WD.indexOf("-")>-1){
							WD = WD.replaceAll("-", "/");
						}
						if(WD!=null&&WD.indexOf(".")>-1){
							WD = WD.replaceAll(".", "/");
						}
						if(WD!=null&&WD.length()==8){
							WD = WD.substring(0, 4)+"/"+WD.substring(4, 6)+"/"+WD.substring(6, 8);
						}
						
						if(WD==null||"".equals(WD)){
							WD="9999/12/31";
						}
						
						mv.addObject("LINE", LINE);
						mv.addObject("POC_NO", POCNO);
						mv.addObject("POC_NO01", POCNO01);
						mv.addObject("POC_NO02", POCNO02);
						mv.addObject("WD", WD);
						mv.addObject("OTP", OTP);
						mv.addObject("OTYPE", OTYPE);
					} else {
						Log.Error("비정상적 접근이 감지됨 : "+request.getRemoteAddr()+", "+paramMap);
						mv.setViewName("main/login/ban");
					}
				} catch (Exception e) {
					Log.Error("비정상적 접근이 감지됨 : "+request.getRemoteAddr()+", "+paramMap);
					Log.Error("오류내용 : "+e.toString());
					mv.setViewName("main/login/ban");
				}
			
			} else {
				Log.Error("비정상적 접근이 감지됨 : "+request.getRemoteAddr());
				mv.setViewName("main/login/ban");
			}
		}
				
		Log.Debug("==> LinkViewController.QICSLinkView End");

		return mv;
	}

	/**
	 * 타공정작업현황 정보
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/OtherLine.do")
	public ModelAndView getOtherLineList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request)  {
		Log.Debug("LinkViewController.getOtherLineList Start");
		
		//paramMap.put("authUUID", session.getAttribute("authUUID"));
		paramMap.put("authIP", request.getRemoteAddr());

		List<?> result = null;
		try {
			result = linkViewService.getOtherLineList(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		Log.Debug("LinkViewController.getOtherLineList End");

		return mv;
	}
	
	/**
	 * 타공정작업현황 정보
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/OtherLineSheet.do")
	public ModelAndView getSheetListByLine(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request)  {
		Log.Debug("LinkViewController.getSheetListByLine Start");
		
		//paramMap.put("authUUID", session.getAttribute("authUUID"));
		paramMap.put("authIP", request.getRemoteAddr());

		List<?> result = null;
		try {
			result = linkViewService.getSheetListByLine(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		Log.Debug("LinkViewController.getSheetListByLine End");

		return mv;
	}
	
	/**
	 * 메인 전자펜 작업 VIEW
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/SheetDetailView.do")
	public String sheetDetailView(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("LinkViewController.sheetDetailView Start");
		Log.Debug("LinkViewController.sheetDetailView End");
		return "linkservice/sheetDetailView";
	}
	
	/**
	 * 메인 전자펜 작업 VIEW 추가
	 * 임성현 추가부(2016-04-15)
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/SheetDetailViewNew.do")
	public String sheetDetailViewNew(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("LinkViewController.sheetDetailView Start");
		Log.Debug("LinkViewController.sheetDetailView End");
		return "linkservice/sheetDetailViewNew";
	}
}
