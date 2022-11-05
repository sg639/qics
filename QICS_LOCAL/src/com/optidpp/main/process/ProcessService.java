package com.optidpp.main.process;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("ProcessService")
public class ProcessService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 작업공정 조회
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getProcessList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ProcessService.getProcessList Start");
		return (List<?>)dao.getList("getProcessList", paramMap);
	}
	public List<?> getSteelGroupList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ProcessService.getSteelGroupList Start");
		return (List<?>)dao.getList("getSteelGroupList", paramMap);
	}
	public List<?> getQicsStampList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ProcessService.getQicsStampList Start");
		return (List<?>)dao.getList("getQicsStampList", paramMap);
	}

}
