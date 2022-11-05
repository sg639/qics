package com.optidpp.main.login;

import java.util.Map;
import java.util.UUID;

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
 *  로그인
 *
 * @author Choi Sung woo
 *
 */
@Controller
public class LoginController {

	@Inject
	@Named("LoginService")
	private LoginService loginService;


	/**
	 * 메인 로그인 화면
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	//@RequestMapping(params = "cmd=viewLogin")
	@RequestMapping("/Login.do")
	public ModelAndView viewLogin(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("==> LoginController.Login Start");
		paramMap.put("domain",	request.getServerName());
		Log.Debug("paramMap " + paramMap);
		String authUUID = UUID.randomUUID().toString();
		ModelAndView mv = new ModelAndView();
		session.setAttribute("authUUID", authUUID);
		session.setAttribute("authIP", request.getRemoteAddr());
		/*
		Log.Debug("Session Chk Start ");
		String chkSabun =(String)session.getAttribute("ssnSabun");
		Log.Debug("Session Chk end "+ chkSabun);

		if (null != chkSabun ) {
			return new ModelAndView( "redirect:/Main.do");
		}
		 */
		//Log.Debug("LoginController.viewLogin End");
		//return new ModelAndView("main/login/login");
		mv.setViewName("main/login/login");
		if(paramMap.containsKey("SEQ_T300")){
			mv.addObject("SEQ_T300", paramMap.get("SEQ_T300"));
		}else{
			mv.addObject("SEQ_T300", "");
		}

		Log.Debug("==> LoginController.Login End");

		return mv;
	}
	@RequestMapping("/Guide.do")
	public ModelAndView viewTest(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("==> LoginController.Guide Start");
		paramMap.put("domain",	request.getServerName());
		Log.Debug("paramMap " + paramMap);
		String authUUID = UUID.randomUUID().toString();
		ModelAndView mv = new ModelAndView();
		session.setAttribute("authUUID", authUUID);
		session.setAttribute("authIP", request.getRemoteAddr());
		/*
		Log.Debug("Session Chk Start ");
		String chkSabun =(String)session.getAttribute("ssnSabun");
		Log.Debug("Session Chk end "+ chkSabun);

		if (null != chkSabun ) {
			return new ModelAndView( "redirect:/Main.do");
		}
		 */
		//Log.Debug("LoginController.viewLogin End");
		//return new ModelAndView("main/login/login");

		mv.setViewName("main/login/index");

		Log.Debug("==> LoginController.Guide End");

		return mv;
	}
}
