package com.optidpp.main.view;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("ViewService")
public class ViewService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 긴급작업 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */

	public List<?> getViewPadInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ViewService.getViewPadInfo Start");
		return (List<?>)dao.getListNoLogging("getViewPadInfo", paramMap);
	}

	public List<?> getViewBGInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ViewService.getViewBGInfo Start");
		return (List<?>)dao.getList("getViewBGInfo", paramMap);
	}

	public List<?> getViewValueInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ViewService.getViewValueInfo Start");
		return (List<?>)dao.getList("getViewValueInfo", paramMap);
	}

	public List<?> getViewUserBGInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ViewService.getViewUserBGInfo Start");
		return (List<?>)dao.getList("getViewUserBGInfo", paramMap);
	}
}
