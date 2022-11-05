package com.optidpp.common.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Common Session Manager
 * 
 * @author ParkMoohun
 */
public class SessionUtil {
	
	/**
	 * 세션 값 획득
	 * 
	 * @param attrNm
	 */
	public static Object getRequestAttribute(String attrNm){
		return RequestContextHolder.getRequestAttributes().getAttribute(attrNm, RequestAttributes.SCOPE_SESSION);
	}
	
	/**
	 * 세션 값 생성
	 * 
	 * @param attrNm
	 * @param attrObj
	 */
	public static void setRequestAttribute(String attrNm, Object attrObj){
		RequestContextHolder.getRequestAttributes().setAttribute(attrNm, attrObj, RequestAttributes.SCOPE_SESSION);
	}
}
