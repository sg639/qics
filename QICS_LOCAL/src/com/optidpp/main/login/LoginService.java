package com.optidpp.main.login;

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
@Service("LoginService") 
public class LoginService{
 
	@Inject
	@Named("Dao")
	private Dao dao;
	
	/**
	 * 회사 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<?> getLoginEnterList(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.getLoginEnterList Start");
		return (List<?>)dao.getList("getLoginEnterList", paramMap);
	}

	/**
	 *  회사 조회 Service 
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getLoginEnterMap(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.getLoginEnterMap");
		return dao.getMap("getLoginEnterMap", paramMap);
	}
	
	
	/**
	 * 본인확인  Service
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public Map<?, ?> getLoginPwdFind(Map<String, Object> paramMap) throws Exception {
		Log.Debug("LoginService.getLoginPwdFind");
		return dao.getMap("getLoginPwdFind", paramMap);
	}
	
	
	
	/**
	 * 사용자 조회
	 * 
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public Map<?, ?> loginUser(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.loginUser");
		return (Map<?, ?>) dao.getMap("loginUser", paramMap);
	}
	
	public Map<?, ?> loginSabun(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.loginSabun");
		return (Map<?, ?>) dao.getMap("loginSabun", paramMap);
	}	
	/**
	 * 권한 조회
	 * 
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public  List getAuthGrpList(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.getAuthGrpList Start");
		return (List) dao.getList("getAuthGrpList", paramMap);
	}
	
	/**
	 * 로그인 로그 (성공)
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public void loginUserLog(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.loginUser");
		dao.insertLog("success", "로그인", paramMap);
	}
	
	/**
	 * 사용자 조회
	 * 
	 * @param paramMap
	 * @return Map
	 * @throws Exception
	 */
	public Map<?, ?> chgUser(Map<?, ?> paramMap) throws Exception {
		Log.Debug("LoginService.chgUser");
		return (Map<?, ?>) dao.getMap("chgUser", paramMap);
	}
	
}