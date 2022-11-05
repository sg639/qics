package com.optidpp.main.view;

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


/**
 *  Form View
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/View.do")
public class ViewController {
	@Inject
	@Named("ViewService")
	private ViewService viewService;

	/**
	 * 메인 FORM View PAD정보
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=viewPadInfo")
	public ModelAndView getViewPadInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ViewController.getViewPadInfo Start");
		String Message = "";
		List<?> list = viewService.getViewPadInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);

		Log.Debug("ViewController.getViewPadInfo End");
		return mv;
	}
	/**
	 * 메인 FORM View 사용자 작성값
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=viewValueInfo")
	public ModelAndView getViewValueInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ViewController.getViewValueInfo Start");
		String Message = "";
		List<?> list = viewService.getViewValueInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);

		Log.Debug("ViewController.getViewValueInfo End");
		return mv;
	}
	/**
	 * 메인 FORM View Image
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=viewBGInfo")
	public ModelAndView getViewBGInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ViewController.getViewBGInfo Start");
		String Message = "";
		////System.out.println(paramMap);
		List<?> list = viewService.getViewBGInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);

		Log.Debug("ViewController.getViewBGInfo End");
		return mv;
	}
	/**
	 * 메인 전자펜 작업 중간검사
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=orginViewPopup")
	public String getOrginViewPopup(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("PenWorkController.getOrginViewPopup Start");
		Log.Debug("PenWorkController.getOrginViewPopup End");
		return "/main/popup/orginView";
	}
	/**
	 * 메인 FORM View Image
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=viewUserBGInfo")
	public ModelAndView getViewUserBGInfo(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("ViewController.getViewUserBGInfo Start");
		String Message = "";
		List<?> list = viewService.getViewUserBGInfo(paramMap);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);

		Log.Debug("ViewController.getViewUserBGInfo End");
		return mv;
	}
}
