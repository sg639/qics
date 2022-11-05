package com.optidpp.common.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.optidpp.common.util.StringUtil;

/**
 * HandlerMapping 된 Controller로 가기전후의 추가 적인작업
 *
 * @author ParkMoohun
 */
public class Interceptor extends HandlerInterceptorAdapter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//Log.Debug("■■■■■■■■■■■■■■■■■■■■■■■ Servlet Start ■■■■■■■■■■■■■■■■■■■■■■■");
		Enumeration<?> enumsHeader = request.getHeaderNames();
		String header = "";
		while (enumsHeader.hasMoreElements()) {
			header = (String) enumsHeader.nextElement();
			//	Log.Debug("[HEADER]" + header + " : " + request.getHeader(header));
		}
		String baseUrl = StringUtil.getBaseUrl(request)+StringUtil.getRelativeUrl(request);
		//	Log.Debug("[REQUEST]baseUrl:" 		+ baseUrl);
		//	Log.Debug("[REQUEST]ContentLength:" + request.getContentLength());
		//	Log.Debug("[REQUEST]LocalName:" 	+ request.getLocalName().toString());
		//Log.Debug("[REQUEST]Method:" 		+ request.getMethod().toString());
		//Log.Debug("[REQUEST]Protocol:" 		+ request.getProtocol());
		//Log.Debug("[REQUEST]Class:" 		+ request.getClass());
		//Log.Debug("[REQUEST]Scheme:" 		+ request.getScheme());
		//Log.Debug("[REQUEST]RemoteHost:" 	+ request.getRemoteHost() + ":" + request.getRemotePort());
		//Log.Debug("[REQUEST]RequestURL:" 	+ request.getRequestURL() + "?" + request.getQueryString());
		//Log.Debug("[REQUEST]RequestURL:" 	+ request.getRequestURL() + (request.getQueryString() != null  ? "?" + request.getQueryString(): "")) ;
		//Log.Debug("[REQUEST]Session:" 		+ request.getSession());
		//Log.Debug("[REQUEST]Locales:" 		+ request.getLocale());
		//Log.Debug("[MAPPING]Controller:"	+ handler.getClass().getCanonicalName());

		/* Method 로그 안찍어!! 2014.10.30 조영대.
		Method m[] = handler.getClass().getDeclaredMethods();
		for (int i = 0; i < m.length; i++) {
			Log.Debug("[MAPPING]Method:"+m[i].toGenericString());
      	}
		 */

		String controllerName = handler.getClass().getSimpleName();
		HttpSession session	= request.getSession(false);


		//처리 시간 계산
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		return super.preHandle(request, response, handler);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		long startTime = (Long)request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		long executeTime = endTime - startTime;

		//modified the exisitng modelAndView
		//		modelAndView.addObject("executeTime",executeTime);
		//modelAndView.addObject("referer",request.getHeader("referer"));
		//log it
		//Log.Debug("[" + handler + "] executeTime : " + executeTime + "ms");
		super.postHandle(request, response, handler, modelAndView);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
	 * afterCompletion(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		//Log.Debug("■■■■■■■■■■■■■■■■■■■■■■■ Servlet End ■■■■■■■■■■■■■■■■■■■■■■■");
	}



}
