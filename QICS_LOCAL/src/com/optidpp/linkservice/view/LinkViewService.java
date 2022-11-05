package com.optidpp.linkservice.view;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

/**
 * 로그인 서비스
 * 
 * @author ParkMoohun
 *
 */
@Service("LinkViewService") 
public class LinkViewService{
 
	@Inject
	@Named("Dao")
	private Dao dao;
	
	/**
	 * setAccessKey
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public int setAccessKey(Map<?, ?> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("LinkViewService.setAccessKey");
		dao.delete("LinkViewService.cleanAccessKey", paramMap);
		
		return dao.create("LinkViewService.setAccessKey", paramMap);
	}
	
	/**
	 * checkAccessKey
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public Map<?, ?> checkAccessKey(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LinkViewService.checkAccessKey");
		return dao.getMap("LinkViewService.checkAccessKey", paramMap);
	}
	
	/**
	 * 타공정현황 조회
	 * 
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public List<?> getOtherLineList(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("LinkViewService.getOtherLineList Start");
		return (List<?>)dao.getList("LinkViewService.getOtherLineList", paramMap);
	}
	
	/**
	 * 작업현황리스트(타공정) 조회
	 * 
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public List<?> getSheetListByLine(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("LinkViewService.getSheetListByLine Start");
		return (List<?>)dao.getList("LinkViewService.getSheetListByLine", paramMap);
	}
	
	
	
}