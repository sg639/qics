package com.optidpp.main.work;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("WorkService")
public class WorkService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 작업내역상세검색
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getWorkList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("WorkService.getPenWorkList Start");
		return (List<?>)dao.getList("getWorkList", paramMap);
	}
	public List<?> getPrintList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("WorkService.getPrintList Start");
		return (List<?>)dao.getList("getPrintList", paramMap);
	}
	public Map<?, ?> getWorkInfo(Map<String, Object> paramMap) throws Exception {
		Log.Debug("WorkService.getWorkInfo Start");
		return dao.getMap("getWorkInfo", paramMap);
	}

}
