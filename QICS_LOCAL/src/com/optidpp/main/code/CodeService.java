package com.optidpp.main.code;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("CodeService")
public class CodeService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 내용 저장
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getCodeList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("CodeService.getCodeList Start");
		return(List<?>)dao.getListNoLogging("getCodeList", paramMap);
	}
	public List<?> getPodCodeList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("CodeService.getPodCodeList Start");
		return (List<?>)dao.getListNoLogging("getPodCodeList", paramMap);
	}
	public List<?> getPodCodeList2(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("CodeService.getPodCodeList2 Start");
		return (List<?>)dao.getListNoLogging("getPodCodeList2", paramMap);
	}
	public Map<?, ?> getSteelDescMap(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("CodeService.getSteelDescMap Start");
		return dao.getMap("getSteelDescMap", paramMap);
	}

}
