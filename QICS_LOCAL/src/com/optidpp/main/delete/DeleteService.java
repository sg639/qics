package com.optidpp.main.delete;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("DeleteService")
public class DeleteService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 삭제 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public int deletePenWork(Map<String, Object> paramMap)  throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("DeleteService.deletePenWork Start");
		int cnt=0;
		cnt += dao.create("deletePrintWork", paramMap);
		cnt ++;
		cnt += dao.create("deletePenWork", paramMap);
		cnt ++;

		return cnt;
	}
	public int clearPenWork(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=0;
		cnt += dao.create("clearTempQics", paramMap);
		cnt ++;
		cnt += dao.create("clearPenWork", paramMap);
		cnt ++;
		return cnt;
	}

}
