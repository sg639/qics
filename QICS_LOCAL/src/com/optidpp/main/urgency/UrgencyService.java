package com.optidpp.main.urgency;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("UrgencyService")
public class UrgencyService {
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
	public Map<?, ?> getUrgencyInfo(Map<String, Object> paramMap) throws Exception  {
		// TODO Auto-generated method stub
		Log.Debug("UrgencyService.getUrgencyInfo Start");
		return dao.getMap("getUrgencyInfo", paramMap);
	}
	public Map<?, ?> getUrgencyMiddle(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("UrgencyService.getUrgencyMiddle Start");
		return dao.getMap("getUrgencyMiddle", paramMap);
	}
	public int saveUrgency(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("UrgencyService.saveUrgency Start");
		int cnt=0;
		cnt += dao.create("saveUrgency", paramMap);
		cnt ++;
		return cnt;
	}


}
