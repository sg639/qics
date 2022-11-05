package com.optidpp.main.orgin;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("OrginService")
public class OrginService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 원본보기 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getOrginMap(Map<String, Object> paramMap) throws Exception {
		Log.Debug("OrginService.getOrginMap Start");
		return dao.getMap("getOrginMap", paramMap);
	}
	public Map<?, ?> getOrginInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("OrginService.getOrginInfo Start");
		return dao.getMap("getOrginInfo", paramMap);
	}

}
