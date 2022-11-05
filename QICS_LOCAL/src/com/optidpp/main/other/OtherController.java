package com.optidpp.main.other;

import java.net.URLEncoder;
import java.util.HashMap;
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
import com.optidpp.linkservice.view.LinkViewService;


/**
 *  타공정작업내역확인
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Other.do")
public class OtherController {
	@Inject
	@Named("OtherService")
	private OtherService otherService;

	@Inject
	@Named("LinkViewService")
	private LinkViewService linkViewService;


	/**
	 * 메인 타공정작업
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=otherPopup")
	public String getOtherPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OtherController.getOtherPopup Start");
		Log.Debug("OtherController.getOtherPopup End");
		return "/main/popup/other";
	}

	/**
	 * 메인 타공정작업내역확인
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=otherInfo")
	public ModelAndView getOtherInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OtherController.getOtherInfo Start");
		Log.Debug("paramMap " + paramMap);
		List<?> list = otherService.getOtherInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("OtherController.getOtherInfo End");
		return mv;
	}

	/**
	 * 메인 타공정작업내역 원본보기
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=otherBGInfo")
	public ModelAndView getOtherBGInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OtherController.getOtherBGInfo Start");
		Log.Debug("paramMap " + paramMap);
		//Map<?, ?> map = otherService.getOtherMap(paramMap);
		List<?> list = otherService.getOtherBGInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("OtherController.getOtherBGInfo End");

		return mv;
	}

	/**
	 * 메인 타공정작업내역 보정데이터보기
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=otherView")
	public ModelAndView getOtherView(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OtherController.getOtherView Start");

		//Map<?, ?> map = otherService.getOtherMap(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		//mv.addObject("result", map);

		mv.addObject("result", "타공정작업내역 보정데이터보기입니다.");
		Log.Debug("OtherController.getOtherView End");

		return mv;
	}
	/**
	 * 메인 타공정 리스트
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=otherList")
	public ModelAndView getOtherList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("WorkController.getOtherList Start");
		String Message = "";
		//System.out.println(paramMap);
		List<?> result = otherService.getOtherList(paramMap);


		Message="작업내역상세검색입니다.";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		mv.addObject("Message", Message);
		Log.Debug("WorkController.getOtherList End");

		return mv;
	}


	/**
	 * 메인 타공정작업내역 보정데이터보기
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=extraViewerKey")
	public ModelAndView extraViewerKey(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OtherController.extraViewerKey Start");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
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


			if(resultCnt>0) {
				resultMap.put("OTP", authUUID);

				LINE = paramMap.containsKey("EXTRAVIEW_LINE")?(String)paramMap.get("EXTRAVIEW_LINE"):"";
				POCNO = paramMap.containsKey("EXTRAVIEW_POC")?(String)paramMap.get("EXTRAVIEW_POC"):"";

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
				WD = paramMap.containsKey("EXTRAVIEW_WD")?(String)paramMap.get("EXTRAVIEW_WD"):"";
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

				String p = "LINE="+LINE+"&POCNO="+POCNO+"&WD="+WD+"&OTP="+authUUID;

				resultMap.put("P", p);
				resultMap.put("LINE", LINE);
				resultMap.put("POCNO", POCNO);
				resultMap.put("POCNO01", POCNO01);
				resultMap.put("POCNO02", POCNO02);
				resultMap.put("WD", WD);
				resultMap.put("LINKURL", URLEncoder.encode(AES256Cipher.AES_Encode(p), "UTF-8") );


			} else {
				resultMap.put("OTP", "ERROR");
				//resultMap.put("OTP_ENC", "호출방식을 확인해주세요.");
				//resultMap.put("OTP_DEC", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		//resultMap.put("Message", Message);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("RESULT", resultMap);

		Log.Debug("OtherController.extraViewerKey End");

		return mv;
	}

	/**
	 * 메인 타공정작업내역 보정데이터보기
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=extraViewer")
	public ModelAndView extraViewer(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OtherController.extraViewer Start");


		//--LINE=BA1&POCNO=C15339803-0000&WD=20160125&OTP=
		String LINE = "";
		String POCNO = "";
		String POCNO01 = "";
		String POCNO02 = "";
		String WD = "";
		String OTP = "";


		ModelAndView mv = new ModelAndView();


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
					mv.setViewName("main/popup/extraViewer");

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


		Log.Debug("OtherController.extraViewer End");

		return mv;
	}
}
