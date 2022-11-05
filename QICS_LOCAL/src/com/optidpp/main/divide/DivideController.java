package com.optidpp.main.divide;

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


/**
 *  분할양식추가
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Divide.do")
public class DivideController {
	@Inject
	@Named("DivideService")
	private DivideService divideService;

	/**
	 * 메인 원본보기
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=dividePopup")
	public String getDividePopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("DivideController.getDividePopup Start");
		Log.Debug("DivideController.getDividePopup End");
		return "/main/popup/divide";
	}


	/**
	 * 메인 분할양식추가 info
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=divideInfo")
	public ModelAndView getDivideInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("DivideController.getDivideInfo Start");


		Map<?, ?> map = divideService.getDivideInfo(paramMap);
		Log.Debug("getDivideInfo " + map);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", map);
		//mv.addObject("result", "분할양식추가 Info입니다.");
		Log.Debug("DivideController.getDivideInfo End");

		return mv;
	}
	/**
	 * 메인 분할양식추가 직접입력
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=divideDirect")
	public ModelAndView setDivideDirect(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("DivideController.setDivideDirect Start");

		Log.Debug("paramMap " + paramMap);

		//Map<?, ?> map = divideService.getDivideMap(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		//mv.addObject("result", map);

		mv.addObject("result", "분할양식추가 Info입니다.");
		Log.Debug("DivideController.setDivideDirect End");

		return mv;
	}
	
}
