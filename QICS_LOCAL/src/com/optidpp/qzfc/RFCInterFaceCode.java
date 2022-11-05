package com.optidpp.qzfc;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.optidpp.podservice.common.util.DBUtil;
import com.optidpp.podservice.db.dto.CommonCollect;

public class RFCInterFaceCode {
	private static final Logger logger = Logger.getLogger(RFCInterFaceCode.class.getName());
	private DBUtil dbu = null;
	private String FIXED_DATE = "";
	public RFCInterFaceCode() {
		logger.debug("RFCInterFaceCode init...");
		//config.wasPoolUseYn = "N";
		try {
			dbu = new DBUtil();
		} catch(Exception ex){
			logger.error(ex.toString());
		}

	}
	private String timerBuffer; // 04:11:15 등의 경과 시간 문자열이 저장될 버퍼 정의
	private int oldTime; // 타이머가 ON 되었을 때의 시각을 기억하고 있는 변수

	public void stopwatch(int onOff) {
		if (onOff == 1) // 타이머 켜기
			oldTime = (int) System.currentTimeMillis() / 1000;

		if (onOff == 0) // 타이머 끄고, 시분초를 timerBuffer 에 저장
			secToHHMMSS(((int) System.currentTimeMillis() / 1000) - oldTime);

	}
	public void secToHHMMSS(int secs) {
		int hour, min, sec;

		sec = secs % 60;
		min = secs / 60 % 60;
		hour = secs / 3600;

		timerBuffer = hour + "시간 " + min + "분 " + sec + "초";
	}
	public void run(String yyyy,String mm,String dd) {

		FIXED_DATE = yyyy+mm+dd;
		//FIXED_DATE = DateUtil.getToday("yyyyMMdd");

		logger.debug("getIsoCdList 시작!! 기준일["+FIXED_DATE+"]");

		CommonCollect collect = getCodeList("erp");
		if(collect==null) {
			logger.debug("getWerksList is EMPTY!!!");
		} else {
			while(collect.next()) {
				HashMap hm = new HashMap();
				hm.put("GUBUN",collect.getString("GUBUN"));
				hm.put("CODE1",collect.getString("CODE1"));
				hm.put("CODE2",collect.getString("CODE2"));
				hm.put("CODE3",collect.getString("CODE3"));
				hm.put("GUBUN",collect.getString("CODE"));
				logger.debug("["+collect.getString("GUBUN")+"]"+" 시작..");
				//boolean result = setIsoCdList(hm);
				logger.debug("["+collect.getString("GUBUN")+"]"+" 종료..");
			}
		}

		logger.debug("getIsoCdList 종료!! 기준일["+FIXED_DATE+"]");

		logger.debug("---------------------------------------------------------------");

	}
	public CommonCollect getCodeList(String poolName) {
		// TODO Auto-generated method stub
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT                    ");
		sqlQuery.append("*                ");
		sqlQuery.append("FROM                ");
		sqlQuery.append("HB_QM_QICS_CODE_V                 ");

		return dbu.getTableData(sqlQuery.toString(), poolName);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RFCInterFaceCode app = new RFCInterFaceCode();
		CommonCollect collect =null;
		ArrayList list = new ArrayList();
		collect= app.getCodeList("erp");
		while (collect.next()) {
			HashMap hm = new HashMap();
			for(int i=0;i<collect.getColumnCount();i++) {
				//	System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
				hm.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
			}
			list.add(hm);
		}
		//System.out.println(list.size());
		int result = app.ifCodeT(list);

		//System.out.println("result = "+result);
		result = app.sysCode(list);
		//System.out.println("result = "+result);
	}
	public int sysCode(ArrayList list) {
		String[] dbStr=new String[list.size()+1];
		StringBuffer sqlQuery = new StringBuffer();
		int idx=0;
		sqlQuery.append("UPDATE TSYS100 SET USED_YN='N'        \n");
		// TODO Auto-generated method stub
		dbStr[idx]=sqlQuery.toString();
		idx++;
		for(int i =0; i < list.size(); i++){
			HashMap hm = new HashMap();
			hm = (HashMap) list.get(i);
			sqlQuery = new StringBuffer();
			sqlQuery.append("MERGE INTO TSYS100 A                        \n");

			sqlQuery.append("USING (select 'X' as DUAL) AS B  ON (A.CODE_GUBUN='"+hm.get("GUBUN")+"' AND A.CODE='"+hm.get("CODE1")+"' AND ISNULL(A.ATTRIBUTE1,'')='"+hm.get("CODE2")+"' AND ISNULL(A.ATTRIBUTE2,'')='"+hm.get("CODE3")+"')        \n");

			sqlQuery.append("WHEN MATCHED THEN                   \n");
			sqlQuery.append("    UPDATE SET                         \n");
			sqlQuery.append("           USED_YN='Y'  ,         \n");
			sqlQuery.append("           IF_DATE=getdate()                                                     \n");
			sqlQuery.append("WHEN NOT MATCHED THEN               \n");

			sqlQuery.append("INSERT (                        \n");
			sqlQuery.append("CODE,                                                    \n");
			sqlQuery.append("CODE_GUBUN,                                    \n");
			if("".equals(hm.get("CODE2"))){

			}else{
				sqlQuery.append("ATTRIBUTE1,                                         \n");
			}
			if("".equals(hm.get("CODE3"))){

			}else{
				sqlQuery.append("ATTRIBUTE2,                                         \n");
			}
			sqlQuery.append("USED_YN,                                                 \n");
			sqlQuery.append("IF_DATE                                                 \n");
			sqlQuery.append(") VALUES (                                             \n");
			sqlQuery.append("'"+hm.get("CODE1")+"',                        \n");
			sqlQuery.append("'"+hm.get("GUBUN")+"',                       \n");
			if("".equals(hm.get("CODE2"))){

			}else{
				sqlQuery.append("'"+hm.get("CODE2")+"',                        \n");
			}
			if("".equals(hm.get("CODE3"))){

			}else{
				sqlQuery.append("'"+hm.get("CODE3")+"',                        \n");
			}
			sqlQuery.append("'Y',                            						\n");
			sqlQuery.append("getdate()                            						\n");
			sqlQuery.append(") ;                                								\n");
			if(idx == 1){
				//System.out.println(sqlQuery.toString());
			}
			dbStr[idx]=sqlQuery.toString();
			idx++;
		}
		return dbu.execBatchSQL("pool1",dbStr);
	}
	public int ifCodeT(ArrayList list) {
		// TODO Auto-generated method stub
		String[] dbStr=new String[list.size()+1];
		StringBuffer sqlQuery = new StringBuffer();
		int idx=0;
		sqlQuery.append("DELETE  FROM IF_CODE        \n");
		dbStr[idx]=sqlQuery.toString();
		idx++;
		for(int i =0; i < list.size(); i++){
			HashMap hm = new HashMap();
			hm = (HashMap) list.get(i);
			sqlQuery = new StringBuffer();
			sqlQuery.append("INSERT INTO IF_CODE(                        \n");
			sqlQuery.append("CODE,                                                    \n");
			sqlQuery.append("CODE_GUBUN,                                    \n");
			if("".equals(hm.get("CODE2"))){

			}else{
				sqlQuery.append("ATTRIBUTE1,                                         \n");
			}
			if("".equals(hm.get("CODE3"))){

			}else{
				sqlQuery.append("ATTRIBUTE2,                                         \n");
			}

			sqlQuery.append("IF_DATE                                                 \n");
			sqlQuery.append(") VALUES (                                             \n");
			sqlQuery.append("'"+hm.get("CODE1")+"',                        \n");
			sqlQuery.append("'"+hm.get("GUBUN")+"',                       \n");
			if("".equals(hm.get("CODE2"))){

			}else{
				sqlQuery.append("'"+hm.get("CODE2")+"',                        \n");
			}
			if("".equals(hm.get("CODE3"))){

			}else{
				sqlQuery.append("'"+hm.get("CODE3")+"',                        \n");
			}
			sqlQuery.append("getdate()                            						\n");
			sqlQuery.append(")                                 								\n");
			if(idx == 1){
				//System.out.println(sqlQuery.toString());
			}
			dbStr[idx]=sqlQuery.toString();
			idx++;
		}
		return dbu.execBatchSQL("pool1",dbStr);
	}
}
