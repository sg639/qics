package com.optidpp.main.other;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("OtherService")
public class OtherService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 타공정작업내역확인 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getOtherInfo(Map<String, Object> paramMap) throws Exception {
		Log.Debug("OtherService.getOtherInfo Start");
		return (List<?>)dao.getList("getOtherInfo", paramMap);
	}
	public List<?> getOtherBGInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("OtherService.getOtherBGInfo Start");
		return (List<?>)dao.getList("getOtherBGInfo", paramMap);
	}
	public List<?> getOtherList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("OtherService.getOtherList Start");
		return (List<?>)dao.getList("getOtherList", paramMap);
	}

}
