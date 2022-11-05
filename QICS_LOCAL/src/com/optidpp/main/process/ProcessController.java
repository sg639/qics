package com.optidpp.main.process;

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
 *  작업공정
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Process.do")
public class ProcessController {
	@Inject
	@Named("ProcessService")
	private ProcessService processService;

	/**
	 * 메인 작업공정 정보
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=processList")
	public ModelAndView getProcessList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request)  {
		Log.Debug("ProcessController.getProcessList Start");
		//paramMap.put("authUUID", session.getAttribute("authUUID"));
		String Message = "";
		paramMap.put("authIP", request.getRemoteAddr());

		List<?> result = null;
		try {
			result = processService.getProcessList(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message="작업공정 정보.";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		Log.Debug("ProcessController.getProcessList End");

		return mv;
	}
	/**
	 * 메인 작업공정 대강종
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=steelGroupList")
	public ModelAndView getSteelGroupList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request)  {
		Log.Debug("ProcessController.getSteelGroupList Start");
		//paramMap.put("authUUID", session.getAttribute("authUUID"));
		String Message = "";
		paramMap.put("authIP", request.getRemoteAddr());
		List<?> result = null;
		try {
			result = processService.getSteelGroupList(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message="작업공정 정보.";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		Log.Debug("ProcessController.getProcessSearchList End");

		return mv;
	}
	/**
	 * 메인 작업공정 대강종
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=qicsStampList")
	public ModelAndView getQicsStampList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request)  {
		Log.Debug("ProcessController.getQicsStampList Start");
		//paramMap.put("authUUID", session.getAttribute("authUUID"));
		String Message = "";
		paramMap.put("authIP", request.getRemoteAddr());
		List<?> result = null;
		try {
			result = processService.getQicsStampList(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message="작업공정 정보.";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", result);
		Log.Debug("ProcessController.getQicsStampList End");

		return mv;
	}
}
