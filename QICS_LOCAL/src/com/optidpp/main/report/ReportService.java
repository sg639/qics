package com.optidpp.main.report;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("ReportService")
public class ReportService {
	@Inject
	@Named("Dao")
	private Dao dao;
	/**
	 * 품질이상보고서생성 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getReportMap(Map<String, Object> paramMap) throws Exception {
		Log.Debug("ReportService.getReportMap Start");
		return dao.getMap("getReportMap", paramMap);
	}
	/**
	 * 품질이상보고서생성 Map
	 *
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public int reportDelete(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		int cnt=dao.create("reportDelete", paramMap);
		return cnt;
	}
	public List<?> getViewResultBGReport(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ReportService.getViewResultBGReport Start");
		return (List<?>)dao.getList("getViewResultBGReport", paramMap);
	}
	public List<?> getViewBGReport(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ReportService.getViewBGReport Start");
		return (List<?>)dao.getList("getViewBGReport", paramMap);
	}
	public Map<?, ?> getReportSeq(Map<String, Object> headerMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ReportService.getReportSeq Start");
		return dao.getMap("getReportSeq", headerMap);
	}

}
