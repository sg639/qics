package com.optidpp.main.code;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.optidpp.podservice.db.dto.CommonCollect;
import com.optidpp.qzfc.RFCInterFaceCode;


/**
 *  내용저장
 *
 * @author Choi Sung Woo
 *
 */
@Controller
@RequestMapping("/Code.do")
public class CodeController {
	@Inject
	@Named("CodeService")
	private CodeService codeService;

	/**
	 * 코드 조회
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=codeList")
	public ModelAndView getCodeList(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("CodeController.getCodeList Start");

		List<?> list = codeService.getCodeList(paramMap);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("DATA", list);
		Log.Debug("CodeController.getCodeList End");

		return mv;
	}
	/**
	 * 코드 Sync
	 *
	 * @param paramMap
	 * @param request
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=codeSync")
	public ModelAndView getCodeSync(HttpSession session,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		Log.Debug("CodeController.getCodeSync Start");

		//List<?> list = codeService.getCodeList(paramMap);
		String Message = "";
		RFCInterFaceCode app = new RFCInterFaceCode();
		CommonCollect collect =null;
		ArrayList list = new ArrayList();
		collect= app.getCodeList("erp");
		while (collect.next()) {
			HashMap hm = new HashMap();
			for(int i=0;i<collect.getColumnCount();i++) {
				//	System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
				hm.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
			}
			list.add(hm);
		}

		int result1 = app.ifCodeT(list);
		//System.out.println("result1 = "+result1);
		int result2 = app.sysCode(list);

		//System.out.println("result 2= "+result2);
		String RESULT ="";
		if(result1 > 0 && result2 > 0){
			Message ="동기화 성공했습니다.";
			RESULT="Y";
		}else{
			Message ="동기화 실패했습니다.";
			RESULT="N";
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		mv.addObject("Message", Message);
		mv.addObject("RESULT", RESULT);
		Log.Debug("CodeController.getCodeSync End");

		return mv;
	}
}
