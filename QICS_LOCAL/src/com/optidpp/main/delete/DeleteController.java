package com.optidpp.main.delete;

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
 *  삭제
 *
 * @author Choi Sung Woo
 *
 */

@Controller
@RequestMapping("/Delete.do")
public class DeleteController {
	@Inject
	@Named("DeleteService")
	private DeleteService deleteservice;

	/**
	 * 메인 삭제
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=dataDelete")
	public ModelAndView setDataDelete(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("DeleteController.setDataDelete Start");
		//System.out.println(paramMap);
		String message = "";
		int resultCnt = -1;
		//System.out.println(paramMap);
		try{
			resultCnt = deleteservice.deletePenWork(paramMap);
			if(resultCnt > 0){
				message="삭제 되었습니다.";
			}else{
				message="삭제 할 내용이 없습니다.";
			}
		}catch(Exception e){
			resultCnt = -1; message="삭제에 실패 하였습니다.";
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", message);
		Log.Debug("DeleteController.setDataDelete End");

		return mv;
	}
	/**
	 * 메인 초기화
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=clearPenWork")
	public ModelAndView setClearPenWork(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("DeleteController.setDataDelete Start");
		//System.out.println(paramMap);
		String message = "";
		int resultCnt = -1;
		try{
			resultCnt = deleteservice.clearPenWork(paramMap);
			if(resultCnt > 0){
				message="삭제 되었습니다.";
			}else{
				message="삭제 할 내용이 없습니다.";
			}
		}catch(Exception e){
			resultCnt = -1; message="삭제에 실패 하였습니다.";
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", message);
		Log.Debug("DeleteController.setDataDelete End");

		return mv;
	}
}
