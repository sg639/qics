package com.optidpp.main.orgin;

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
 *  원본보기
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Orgin.do")
public class OrginController {
	@Inject
	@Named("OrginService")
	private OrginService orginService;

	/**
	 * 메인 원본보기
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=orginPopup")
	public String getOrginPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OrginController.getOrginPopup Start");
		Log.Debug("OrginController.getOrginPopup End");
		return "/main/popup/orgin";
	}

	/**
	 * 메인 원본보기 Info
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=orginInfo")
	public ModelAndView getOrginInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("OrginController.getOrginInfo Start");
		Log.Debug("paramMap " + paramMap);
		//Map<?, ?> map = orginService.getOrginInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		//mv.addObject("result", map);

		mv.addObject("result", "원본보기 Info입니다.");
		Log.Debug("AddController.getOrginInfo End");
		return mv;
	}
	
}
