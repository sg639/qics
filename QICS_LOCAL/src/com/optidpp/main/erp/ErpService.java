package com.optidpp.main.erp;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("ErpService")
public class ErpService {
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
	public int erpPenWork(Map<String, Object> paramMap)  throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ErpService.erpPenWork Start");
		int cnt=0;
		cnt += dao.create("erpPenWork", paramMap);
		cnt += dao.create("erpWork", paramMap);
		if("Y".equals(paramMap.get("END_YN"))){
			cnt += dao.create("endPenWork", paramMap);
		}
		cnt ++;
		return cnt;
	}

}
