package com.optidpp.common.exception.handler;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import com.optidpp.common.exception.ServiceException;

/**
 * @author ParkMoohun
 */
public class ServiceExceptionResolver extends SimpleMappingExceptionResolver {

	Log logger = LogFactory.getLog("com.optidpp.HrExceptionResolver");

	@Inject
	private MessageSource messageSource;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver#doResolveException(
	 * 																		javax.servlet.http.HttpServletRequest, 
	 * 																		javax.servlet.http.HttpServletResponse, 
	 * 																		java.lang.Object, java.lang.Exception
	 * )
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (!(ex instanceof ServiceException)) {
//			logger.error("doResolveException getMessage:"+ex.getMessage());
//			logger.error("doResolveException handle:"+handler);
//			logger.error("doResolveException getLocalizedMessage:"+ex.getLocalizedMessage());
//			logger.error("doResolveException getCanonicalName:"+ex.getClass().getCanonicalName());
//			logger.error("doResolveException getModifiers:"+ex.getClass().getModifiers());
//			logger.error("doResolveException getName:"+ex.getClass().getName());
//			logger.error("doResolveException getSimpleName:"+ex.getClass().getSimpleName());
			ex = new ServiceException(messageSource, "com.optidpp.common.error" , new String[] {}, ex);
		}else{
			logger.debug("doResolveException  Else");
		}
		return super.doResolveException(request, response, handler, ex);
//		
//		String code = "777";
//		String message = "";
//		
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("common/error/errorMessage");
//		mv.addObject("code", code);
//		mv.addObject("message", "sss");
//		return mv;
	}
}
