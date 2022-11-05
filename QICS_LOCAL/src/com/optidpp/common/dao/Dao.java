package com.optidpp.common.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.anyframe.query.QueryService;
import org.anyframe.query.QueryServiceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.optidpp.common.logger.Log;
import com.optidpp.common.util.SessionUtil;

/**
 * 공통 DAO 
 * @author ParkMoohun
 */
@Repository("Dao")
public class Dao{
	
	@Inject
	@Named("queryService")
	private QueryService queryService;
	private String variableName = "param";

	/**
	 * List Type 다행 조회
	 * 
	 * @param queryId
	 * @param paramMap
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<?> getList(String queryId, Map<?, ?> paramMap) throws Exception {
		Object[] param = convertParams(paramMap);
		Collection<?> collection = queryService.find(queryId, param );
		Object[] rtnMap = collection.toArray();
		
		Log.Debug("┌────────────────── "+queryId+" Result List Start────────────────────────");
		for(Object m:rtnMap){
			Log.Debug("│  "+m.toString());
		}
		Log.Debug("└────────────────── "+queryId+" Result List End──────────────────────────");
		insertLog(queryId, "조회", paramMap);
		return collection;
	}
	
	/**
	 * List Type 다행 조회
	 * 
	 * @param queryId
	 * @param paramMap
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<?> getListNoLogging(String queryId, Map<?, ?> paramMap) throws Exception {
		Object[] param = convertParams(paramMap);
		Collection<?> collection = queryService.find(queryId, param );
		Object[] rtnMap = collection.toArray();
		
		Log.Debug("┌────────────────── "+queryId+" Result List Start────────────────────────");
		for(Object m:rtnMap){
			Log.Debug("│  "+m.toString());
			Log.Debug("│  ...생략");
			break;
		}
		Log.Debug("└────────────────── "+queryId+" Result List End──────────────────────────");
		insertLog(queryId, "조회", paramMap);
		return collection;
	}
	
	/**
	 * Map Type 단행 조회
	 * 
	 * @param queryId
	 * @param paramMap
	 * @return Collection
	 * @throws Exception
	 */
	public Map<?, ?> getMap(String queryId, Map<?, ?> paramMap) throws Exception {
		Object[] param = convertParams(paramMap);
		Map<?,?> map = null;
		List<?> list = (List<?>) queryService.find(queryId, param );
		if(!list.isEmpty()) {
			Log.Debug("┌────────────────── "+queryId+" Result Map Start────────────────────────");
			map = (Map<?, ?>) list.get(0);
			Log.Debug("│  "+map.toString());
			Log.Debug("└────────────────── "+queryId+" Result Map End──────────────────────────");
		}
		insertLog(queryId, "조회", paramMap);
		return map;
	}
	
	/**
	 * 데이터 생성
	 * 
	 * @param queryId
	 * @param insertMap
	 * @return int
	 * @throws Exception
	 */
	public int create(String queryId, Map<?, ?> insertMap) throws Exception {
		Object[] param = convertParams(insertMap);
		int cnt = queryService.create(queryId, param );
		insertLog(queryId, "생성", insertMap);
		return cnt;
	}
	
	/**
	 * 데이터 갱신
	 * 
	 * @param queryId
	 * @param updateMap
	 * @return int
	 * @throws Exception
	 */
	public int update(String queryId, Map<?, ?> updateMap) throws Exception {
		Object[] param = convertParams(updateMap);
		int cnt = queryService.update(queryId, param );
		insertLog(queryId, "저장", updateMap);
		return cnt;
	}

	/**
	 * @param queryId
	 * @param deleteMap
	 * @return int
	 * @throws Exception
	 */
	public int delete(String queryId, Map<?, ?> deleteMap) throws Exception {
		Object[] param = convertParams(deleteMap);
		int cnt = queryService.remove(queryId, param );
		insertLog(queryId, "삭제", deleteMap);
		return cnt;
	}
	
	/**
	 * param 값 변환
	 * 
	 * @param targetMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Object[] convertParams(Map<?, ?> targetMap) {
		Object[] params = new Object[targetMap.size()];
		Iterator<?> targetMapIterator = targetMap.entrySet().iterator();
		int i = 0;
		while (targetMapIterator.hasNext()) {
			Map.Entry entry = (Map.Entry) targetMapIterator.next();
			params[i] = new Object[] { entry.getKey(), entry.getValue() };
			i++;
		}
		
		return params;
	}
	
	/**
	 * @param targetObj
	 * @return
	 */
	private Object[] convertParams(Object targetObj) {
		return new Object[] { new Object[] { variableName, targetObj } };
	}
	
	/**
	 * 로그 생성
	 * 
	 * @param queryId
	 * @param job
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	public int insertLog(String queryId, String job, Map<?,?> paramMap) throws Exception {
		String enterCd = (String) SessionUtil.getRequestAttribute("ssnEnterCd");
		String controllerName = (String)  SessionUtil.getRequestAttribute("logController");
		//String controllerName = handler.getClass().getSimpleName();
		
		if(enterCd == null || enterCd == "") return -1;
		
		Map<String, Object> logParam = new HashMap<String, Object>();
		logParam.put("logEnterCd",		SessionUtil.getRequestAttribute("ssnEnterCd") );
		logParam.put("logJob", 			job );
		logParam.put("logIp", 			SessionUtil.getRequestAttribute("logIp") );
		logParam.put("logRequestUrl", 	SessionUtil.getRequestAttribute("logRequestUrl") );
		logParam.put("logRequestBaseUrl", 	SessionUtil.getRequestAttribute("logRequestBaseUrl") );
		//logParam.put("logController", 	SessionUtil.getRequestAttribute("logController") );
		logParam.put("logController", 	controllerName );
		logParam.put("logParameter", 	paramMap.toString() );
		logParam.put("logQueryId", 		queryId );
		logParam.put("logSabun", 		SessionUtil.getRequestAttribute("ssnSabun") );
		
		if(!controllerName.equals( "CommonCodeController")
		&& !controllerName.equals( "MainController") // main
		&& !controllerName.equals( "SampleController") // Sample
		&& !controllerName.equals( "ImageUploadController") // iamge
		&& !controllerName.equals( "ImageUploadTorg903Controller") // iamge
		){
			
			Log.Debug("controllerName────────────────── log────────────────────────"+ controllerName);
			
			//Temp ToDo 
			List<?> ynlist = (List<?>) queryService.find("getLogMgrY", convertParams(logParam) );
			Map<?, ?> ynmap = new HashMap<Object, Object>();
			if(!ynlist.isEmpty()) {
				ynmap = (Map) ynlist.get(0);
			}
			String logYn = ynmap.get("cnt").toString();
			
			//Temp ToDo 
			if(SessionUtil.getRequestAttribute("logController").equals( "LoginController"))
				logYn = "1";

			//Log YN Check
			if(!logYn.equals("0")){
			
				List list = (List) queryService.find("getLogMgrSeqMap", convertParams(logParam) );
				Map map = new HashMap();
				if(!list.isEmpty()) {
					map = (Map) list.get(0);
				}
				String logSeq = map.get("seq").toString();
				logParam.put("logSeq", 			logSeq );
				queryService.execute("insertLogMgr", logParam);
				queryService.execute("updateLogMgr", logParam);
			}
		}
		return 1;
	}
	
	
	/**
	 * Sign 로그 생성
	 * 
	 * @param queryId
	 * @param job
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	public int insertSignLog(String queryId, String job, Map<?,?> paramMap) throws Exception {
		String enterCd = (String) SessionUtil.getRequestAttribute("ssnEnterCd");
		
		if(enterCd == null || enterCd == "") return -1;
		
		Map<String, Object> logParam = new HashMap<String, Object>();
		logParam.put("logEnterCd",		SessionUtil.getRequestAttribute("ssnEnterCd") );
		logParam.put("logJob", 			job );
		logParam.put("logIp", 			SessionUtil.getRequestAttribute("logIp") );
		logParam.put("logRequestUrl", 	SessionUtil.getRequestAttribute("logRequestUrl") );
		logParam.put("logRequestBaseUrl", 	SessionUtil.getRequestAttribute("logRequestBaseUrl") );
		logParam.put("logController", 	SessionUtil.getRequestAttribute("logController") );
		logParam.put("logParameter", 	paramMap.toString() );
		logParam.put("logQueryId", 		queryId );
		logParam.put("logSabun", 		SessionUtil.getRequestAttribute("ssnSabun") );
		
		List list = (List) queryService.find("getLogMgrSeqMap", convertParams(logParam) );
		Map map = new HashMap();
		if(!list.isEmpty()) {
			map = (Map) list.get(0);
		}
		String logSeq = map.get("seq").toString();
		logParam.put("logSeq", 			logSeq );
		queryService.execute("insertLogMgr", logParam);
		queryService.execute("updateLogMgr", logParam);

		
		return 1;
	}	
	
	
	/**
	 * Procedure 
	 * 
	 * @param queryId
	 * @param paramMap
	 * @return Object
	 * @throws QueryServiceException
	 */
	public Object excute(String queryId, Map<?, ?> paramMap) throws QueryServiceException {
		return queryService.execute(queryId, paramMap);
	}
	
	/**
	 * Query Info 
	 * 
	 * @param queryId
	 * @throws QueryServiceException
	 */
	public String getStatement(String queryId) throws QueryServiceException {
	    	return queryService.getStatement(queryId);
	}
	
	
	/**
	 * 데이터 갱신
	 * 
	 * @param queryId
	 * @param updateMap
	 * @return int
	 * @throws Exception
	 */
	public int updateClob(String queryId, Map<?, ?> updateMap) throws Exception {
		Map map = queryService.execute( queryId, updateMap );
		insertLog(queryId, "updateMap저장", updateMap);
		
		int cnt = Integer.parseInt( map.get("#update-count-1").toString() );
		return cnt;
	}

	/**
	 * Sign 데이터 갱신
	 * 
	 * @param queryId
	 * @param updateMap
	 * @return int
	 * @throws Exception
	 */
	public int updateSignClob(String queryId, Map<?, ?> updateMap) throws Exception {
		Map map = queryService.execute( queryId, updateMap );
		insertSignLog(queryId, "updateMap저장", updateMap);
		
		int cnt = Integer.parseInt( map.get("#update-count-1").toString() );
		return cnt;
	}
	
	/**
	 * Batch를 실행
	 * 
	 * @param queryId
	 * @param params
	 */
	public int[] batchUpdate(String queryId, List params) throws Exception {
		return queryService.batchUpdate(queryId, params);
	}
	
//	@SuppressWarnings("rawtypes")
//	private Object[] convertParams(Map<?, ?> targetMap1, Map<?, ?> targetMap2) {
//		Object[] params = new Object[targetMap1.size() + targetMap2.size()];
//		Iterator<?> targetMapIterator1 = targetMap1.entrySet().iterator();
//		Iterator<?> targetMapIterator2 = targetMap2.entrySet().iterator();
//		int i = 0;
//		while (targetMapIterator1.hasNext()) {
//			Map.Entry entry = (Map.Entry) targetMapIterator1.next();
//			params[i] = new Object[] { entry.getKey(), entry.getValue() };
//			i++;
//		}
//		
//		while (targetMapIterator2.hasNext()) {
//			Map.Entry entry = (Map.Entry) targetMapIterator2.next();
//			params[i] = new Object[] { entry.getKey(), entry.getValue() };
//			i++;
//		}
//		return params;
//	}
//
//	public Object findByPk(String queryId, Map<?, ?> targetMap) throws QueryServiceException {
//		Object[] params = convertParams(targetMap);
//		return findByPk(queryId, params);
//	}
//	
//	public Object findByPk(String queryId, List<?> targetList) throws QueryServiceException {
//		Object[] params = convertParams(targetList);
//		return findByPk(queryId, params);
//	}
//	
//	public Object findByPk(String queryId, Object[] targetObjs) throws QueryServiceException {
//		Collection<?> collection = queryService.find(queryId, targetObjs, 0, 0);
//		if (collection == null || collection.size() == 0)
//			return null;
//		return collection.iterator().next();
//	}	
//	
//	public Collection<?> getList(String queryId, Map<?, ?> searchMap, Map<?, ?> paramMap) throws Exception {
//		Object[] param = convertParams(searchMap, paramMap);
//		return queryService.find(queryId, param );
//	}
//	public int create(String queryId, Map<?, ?> insertMap, Map<?, ?> paramMap) throws Exception {
//		Object[] param = convertParams(insertMap, paramMap);
//		return queryService.create(queryId, param );
//	}
//	
//	public int create(String queryId, Object tagetObj) throws Exception {
//		Object[] param = convertParams(tagetObj);
//		return queryService.create(queryId, param );
//	}
//	public int update(String queryId, Map<?, ?> updateMap, Map<?, ?> paramMap) throws Exception {
//		Object[] param = convertParams(updateMap, paramMap);
//		return queryService.update(queryId, param );
//	}
//	
//	public int update(String queryId, Object tagetObj) throws Exception {
//		Object[] param = convertParams(tagetObj);
//		return queryService.update(queryId, param );
//	}
//	
//	public int update(String queryId, Object[] targetObj) throws Exception {
//		return queryService.update(queryId, new Object[]{targetObj});
//	}
//	
//	
//	public int delete(String queryId, Map<?, ?> deleteMap, Map<?, ?> paramMap) throws Exception {
//		Object[] param = convertParams(deleteMap, paramMap);
//		return queryService.remove(queryId, param );
//	}
//	
//	public int delete(String queryId, Object targetObj) throws QueryServiceException {
//		Object[] params = convertParams(targetObj);
//		return queryService.remove(queryId, params);
//	}
//	
//	public Page findListWithPaging(String queryId, Map<?, ?> searchMap, int pageIndex, int pageSize, int pageUnit) throws QueryServiceException {
//		Object[] params = convertParams(searchMap);
//		return findListWithPaging(queryId, params, pageIndex, pageSize, pageUnit);
//	}
//
//	public Page findListWithPaging(String queryId, Object[] targetObjs, int pageIndex, int pageSize, int pageUnit)
//			throws QueryServiceException {
//		Map<?, ?> queryMap = queryService.findWithRowCount(queryId, targetObjs, pageIndex, pageSize);
//		List<?> resultList = (List<?>) queryMap.get(QueryService.LIST);
//		int totalSize = ((Long) queryMap.get(QueryService.COUNT)).intValue();
//		return new Page(resultList, (new Integer(pageIndex)).intValue(), totalSize, pageUnit, pageSize);
//	}
//	public Collection<?> getList(String queryId, Object[] targetObj) throws Exception {
//		return queryService.find(queryId, convertParams(targetObj));
//	}
//	
//	public int multiUpdate(String queryId, Map<?, ?> targetObj) throws Exception {
//		return queryService.update(queryId, new Object[]{new Object[] {"rm",targetObj}});
//		//return queryService.update(queryId, new Object[]{new Object[] {"rm",targetObj}, criteria});
//		//return queryService.update(queryId, new Object[]{new Object[] {"navi",saveData}, new Object[] {"rm",targetObj},"GBM="+gbm,"USER="+user}); 
//	}
	
	/**
	 * Commit
	 */
	public void commit() throws Exception {
		JdbcTemplate jdbcTemplate = queryService.getQueryServiceJdbcTemplate();
		DataSource dataSource = null;
		Connection con = null;
		if(jdbcTemplate != null) {
			dataSource = jdbcTemplate.getDataSource();
			if(dataSource != null) {
				con = dataSource.getConnection();
				if(con != null) {
					con.commit();
				}
			}
		}
	}
	
	/**
	 * Rollback
	 */
	public void rollback() throws Exception {
		JdbcTemplate jdbcTemplate = queryService.getQueryServiceJdbcTemplate();
		DataSource dataSource = null;
		Connection con = null;
		if(jdbcTemplate != null) {
			dataSource = jdbcTemplate.getDataSource();
			if(dataSource != null) {
				con = dataSource.getConnection();
				if(con != null) {
					con.rollback();
				}
			}
		}
	}
}