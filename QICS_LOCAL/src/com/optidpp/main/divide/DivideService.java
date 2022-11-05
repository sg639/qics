package com.optidpp.main.divide;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("DivideService")
public class DivideService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 분할양식추가 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getDivideInfo(Map<String, Object> paramMap) throws Exception {
		Log.Debug("DivideService.getDivideInfo Start");
		return dao.getMap("getDivideInfo", paramMap);
	}

}
