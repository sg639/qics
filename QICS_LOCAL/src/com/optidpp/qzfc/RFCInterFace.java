package com.optidpp.qzfc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;

import com.optidpp.common.logger.Log;
import com.optidpp.podservice.common.util.DBUtil;
import com.optidpp.podservice.common.util.StringUtil;
import com.optidpp.podservice.db.dto.CommonCollect;

public class RFCInterFace {
	private DBUtil dbu = null;
	public RFCInterFace() {
		Log.Debug("RFCInterFace init...");
		//config.wasPoolUseYn = "N";
		try {
			dbu = new DBUtil();
		} catch(Exception ex){
			Log.Error(ex.toString());
		}

	}
	public static void main(String[] args) {
		RFCInterFace app = new RFCInterFace();
		CommonCollect collect =null;

		Map<String, Object> paramMap = new HashMap<String, Object> ();
		//	paramMap.put("lineCode","AP2");
		//	paramMap.put("lineType","BGC");
		//	paramMap.put("lineGubun","BGC");
		List list = new ArrayList();
		HashMap hm = new HashMap();
		paramMap.put("IN_FACT","BGC	");
		paramMap.put("IN_ORDER","AP");
		paramMap.put("IN_LINE","AP2");
		paramMap.put("IN_TYPE","N");
		paramMap.put("POC_NO","C16431159-0000");
		hm = app.divideList2(paramMap);
		/*
		 * PASS_CNT
		 * PROD_SURFACE  ==> SURFACE
		 * CUSTOMER ==> CUSTUMER
		 * OP_USE ==> USE_NAME
		 */
		//list= app.workList(paramMap);
		//collect = app.workList(paramMap);
		/*collect =  app.getWorkList("pop",paramMap);
		while (collect.next()) {
			for(int i=0;i<collect.getColumnCount();i++) {
				if("WIP_ENTITY_NAME".equals(collect.getColumnName((i+1)))){
					paramMap.put("POC_NO",collect.getString(collect.getColumnName((i+1))));
					CommonCollect collect2 = app.getCommonInfo("pop",paramMap);
					CommonCollect collect3 = app.getDivideList("pop",paramMap);
					if(collect2.size() > 1 && collect3.size() >1 ){
						//System.out.println("########### "+paramMap.get("POC_NO"));
					}
				}
			}
	}*/
		//	  List list = app.getWorkList("pop",paramMap);
		//if(collect.size() == 0){
		//	System.out.println("정보가 없습니다.");
		//	}
		//System.out.println(list.toString());
	}
	public List workList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

		List list = new ArrayList();
		CommonCollect collect =null;
		collect = getWorkList("pop",paramMap);

		//	collect.getMetaData();
		//HashMap<String,CommonCollect> rfcOutData = collect.getMetaData();
		int idx = 0;
		while (collect.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0;i<collect.getColumnCount();i++) {

				if("MRG_WIP_ENTITY_NAME".equals(collect.getColumnName((i+1)))){
					map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
					map.put("POC_NO",collect.getString(collect.getColumnName((i+1))));
					String [] value = collect.getString(collect.getColumnName((i+1))).split("-");
					//System.out.println("value.length = "+ value.length);
					if(value.length == 1){
						map.put("POC_NO",collect.getString(collect.getColumnName((i+1)))+"-0000");
						map.put("POC_NO01",value[0]);
						map.put("POC_NO02","0000");
					}else if(value.length == 2){
						map.put("POC_NO01",value[0]);
						map.put("POC_NO02",value[1]);
					}else if(value.length == 3){
						map.put("POC_NO01",value[0]);
						map.put("POC_NO02",value[1]);
						map.put("POC_NO03",value[2]);
					}

				}else{
					map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
				}




				//	System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
			}
			map.put("IN_FACT",paramMap.get("IN_FACT"));
			map.put("IN_ORDER",paramMap.get("IN_ORDER"));
			map.put("IN_LINE",paramMap.get("IN_LINE"));
			map.put("IN_TYPE",paramMap.get("IN_TYPE"));
			/*if(map.containsKey("TARGET_THICKNESS")){
				map.put("MRG_R_SUPPLY_THICKNESS",map.get("TARGET_THICKNESS"));
			}
			if(map.containsKey("LAST_WIDTH")){
				map.put("MRG_R_WIDTH",map.get("LAST_WIDTH"));
			}*/
			List<Entry> entryList = new ArrayList<Entry>(map.entrySet());
			list.add(idx, map);
			//	list.set(idx, map);
			idx++;
			//list = entryList;
		}
		//System.out.println("list.size() = "+list.size());
		return list;
	}
	public CommonCollect getWorkList(String poolName, Map<String, Object> paramMap) {
		//		/String lineCode = paramMap.get("lineCode").toString();
		/*
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT                    ");
		sqlQuery.append("IN_FACT,                 ");
		sqlQuery.append("IN_ORDER,                ");
		sqlQuery.append("IN_LINE,                 ");
		sqlQuery.append("IN_TYPE,                 ");
		sqlQuery.append("POC_NO,                  ");
		sqlQuery.append("MRG_DISCRETE_NUMBER,     ");
		sqlQuery.append("MRG_LINE_CODE,           ");
		sqlQuery.append("MRG_STEEL_TYPE,          ");
		sqlQuery.append("MRG_R_SUPPLIER,          ");
		sqlQuery.append("MRG_WIP_ENTITY_NAME,     ");
		sqlQuery.append("MRG_R_SUPPLY_THICKNESS,  ");
		sqlQuery.append("MRG_R_WIDTH,             ");
		sqlQuery.append("PARTIAL_WEIGHT,          ");
		sqlQuery.append("TARGET_THICKNESS,        ");
		sqlQuery.append("AIM_THICKNESS,           ");
		sqlQuery.append("AIM_TOL,                 ");
		sqlQuery.append("JOB_DESCRIPTION          ");
		sqlQuery.append("FROM TEST100             ");
		sqlQuery.append("WHERE IN_LINE LIKE '%"+ paramMap.get("IN_LINE").toString()+"%'");
		System.out.println("paramMap = "+paramMap.toString());
		return dbu.getTableData(sqlQuery.toString(), poolName);*/

		try {
			String[] typeList=new String[18];
			typeList[0]="STRING";
			typeList[1]="STRING";
			typeList[2]="STRING";
			typeList[3]="STRING";
			typeList[4]="STRING";
			typeList[5]="STRING";
			typeList[6]="STRING";
			typeList[7]="STRING";
			typeList[8]="STRING";
			typeList[9]="STRING";
			typeList[10]="STRING";
			typeList[11]="STRING";
			typeList[12]="STRING";
			typeList[13]="STRING";
			typeList[14]="STRING";
			typeList[15]="STRING";
			typeList[16]="STRING";
			typeList[17]="CORSOR";
			String[] paramList=new String[18];
			Log.Debug("작업목록 paramMap  " + paramMap);
			paramList[0]=paramMap.containsKey("IN_FACT") ?  paramMap.get("IN_FACT").toString(): "";
			paramList[1]=paramMap.containsKey("IN_ORDER") ? paramMap.get("IN_ORDER").toString(): "";
			paramList[2]=paramMap.containsKey("IN_LINE") ? paramMap.get("IN_LINE").toString(): "";
			paramList[3]=paramMap.containsKey("IN_TYPE") ? paramMap.get("IN_TYPE").toString(): "";
			paramList[4]=paramMap.containsKey("IN_POCNO") ? paramMap.get("IN_POCNO").toString(): "";
			paramList[5]="ALL";
			paramList[6]="ALL";
			paramList[7]=paramMap.containsKey("IN_DISCRETE_NUMBER") ? paramMap.get("IN_DISCRETE_NUMBER").toString() : "";
			paramList[8]=paramMap.containsKey("IN_CUR_LINE") ? paramMap.get("IN_CUR_LINE").toString() : "";
			paramList[9]=paramMap.containsKey("IN_STEEL_TYPE") ? paramMap.get("IN_STEEL_TYPE").toString() : "";
			paramList[10]=paramMap.containsKey("IN_THICKNESS") ? paramMap.get("IN_THICKNESS").toString() : "";
			paramList[11]=paramMap.containsKey("IN_WIDTH") ? paramMap.get("IN_WIDTH").toString() : "";
			paramList[12]=paramMap.containsKey("IN_SURFACE") ? paramMap.get("IN_SURFACE").toString() : "";
			paramList[13]=paramMap.containsKey("IN_STEEL_GROUP") ? paramMap.get("IN_STEEL_GROUP").toString() : "";
			paramList[14]=paramMap.containsKey("IN_UT_LINE") ? paramMap.get("IN_UT_LINE").toString() : "";
			paramList[15]=paramMap.containsKey("IN_ORDER_EDL") ? paramMap.get("IN_ORDER_EDL").toString() : "";
			paramList[16]=paramMap.containsKey("IN_GUBUN") ? paramMap.get("IN_GUBUN").toString() : "";
			paramList[17]="";
			return dbu.call_QICS_Procedure("pop","DSINFO.WIP_MWIPORDDAT.MWIPORDDAT_SEL19", typeList,paramList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.Error("작업목록 Error  " + e.getMessage());
			return null;
		}

		//DSINFO.WIP_MWIPORDDAT.MWIPORDDAT_SEL18( :IN_WIP_ENTITY_NAME, :IN_ORDER_NUMBER, :IN_LINE_NUMBER, :OUT_CUR1);


	}
	public Map<String, Object> commonInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		CommonCollect collect = getCommonInfo("pop",paramMap);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(collect == null){
				Log.Debug("정보가 없습니다.");
			}else{

				while (collect.next()) {
					for(int i=0;i<collect.getColumnCount();i++) {
						String value = "";
						if("THICKNESS_MIN".equals(collect.getColumnName((i+1)))){
							value = collect.getString(collect.getColumnName((i+1)));
							if(value.indexOf(".") > -1){
								map.put("C_THMIN01_POD",value.substring(0, value.indexOf(".")));
								map.put("C_THMIN02_POD",value.substring(value.indexOf(".")+1, value.length()));
							}else{
								map.put("C_THMIN01_POD","");
								map.put("C_THMIN02_POD","");
							}
						}
						if("THICKNESS_MAX".equals(collect.getColumnName((i+1))) ){
							value = collect.getString(collect.getColumnName((i+1)));
							if(value.indexOf(".") > -1){
								map.put("C_THMAX01_POD",value.substring(0, value.indexOf(".") ));
								map.put("C_THMAX02_POD",value.substring(value.indexOf(".")+1, value.length()));
							}else{
								map.put("C_THMAX01_POD","");
								map.put("C_THMAX02_POD","");
							}
						}
						map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
						//System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.Error("작업지시 Error  " + e.getMessage());
		}
		//System.out.println("map = "+map);
		return map;
	}
	private CommonCollect getCommonInfo(String poolName,
			Map<String, Object> paramMap) {
		/*	StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT                                         \n");
		sqlQuery.append("WIP_ENTITY_NAME,                              \n");
		sqlQuery.append("R_SUPPLY_THICKNESS,                              \n");
		sqlQuery.append("AIM_THICKNESS_1,                                  \n");
		sqlQuery.append("AIM_THICKNESS,                                   \n");
		sqlQuery.append("TARGET_THICKNESS,                                   \n");
		sqlQuery.append("LAST_WIDTH,                                      \n");
		sqlQuery.append("R_SUPPLIER,                                 \n");
		sqlQuery.append("STEEL_TYPE,                                 \n");
		sqlQuery.append("HEAT_NO,                                        \n");
		sqlQuery.append("SURFACE,                                \n");
		sqlQuery.append("CUSTUMER,                                \n");
		sqlQuery.append("ORDER_NUMBER,                                 \n");
		sqlQuery.append("LINE_NUMBER,                                  \n");
		sqlQuery.append("RELEATED_SIZE,                                \n");
		sqlQuery.append("USE_NAME,                                       \n");
		sqlQuery.append("PAPER                                        \n");
		sqlQuery.append(" FROM TEST200                                 \n");
		sqlQuery.append(" WHERE WIP_ENTITY_NAME LIKE '%"+paramMap.get("POC_NO").toString()+"%'\n");
		sqlQuery.append("                                              \n");

		return dbu.getTableData(sqlQuery.toString(), poolName);*/
		Log.Debug("작업지시 paramMap  " + paramMap);
		String[] typeList=new String[5];
		typeList[0]="STRING";
		typeList[1]="STRING";
		typeList[2]="STRING";
		typeList[3]="STRING";
		typeList[4]="CORSOR";
		String[] paramList=new String[5];
		//System.out.println("공통정보 paramMap = "+paramMap);
		if("M".equals(paramMap.get("FORM_TYPE")) || "R".equals(paramMap.get("FORM_TYPE"))){
			paramList[0]=paramMap.get("POC_NO").toString();
			paramList[1]="";
			paramList[2]="";
			paramList[3]=paramMap.containsKey("IN_LINE")  ? paramMap.get("IN_LINE").toString() : "";
			paramList[4]="";
		}else{
			if(paramMap.containsKey("ORDER_NUMBER") && paramMap.containsKey("LINE_NUMBER") ){
				paramList[0]=paramMap.containsKey("PRE_POC_NO") ? paramMap.get("PRE_POC_NO").toString() : "";
				paramList[1]=paramMap.containsKey("ORDER_NUMBER") ? paramMap.get("ORDER_NUMBER").toString(): "";
				paramList[2]=paramMap.containsKey("LINE_NUMBER") ? paramMap.get("LINE_NUMBER").toString(): "";
				paramList[3]=paramMap.containsKey("IN_LINE")  ? paramMap.get("IN_LINE").toString() : "";
				paramList[4]="";
			}else{
				paramList[0]=paramMap.containsKey("PRE_POC_NO") ? paramMap.get("PRE_POC_NO").toString() : "";
				paramList[1]="";
				paramList[2]="";
				paramList[3]=paramMap.containsKey("IN_LINE")  ? paramMap.get("IN_LINE").toString() : "";
				paramList[4]="";
			}
		}
		/*
		String[] typeList=new String[4];
		typeList[0]="STRING";
		typeList[1]="STRING";
		typeList[2]="STRING";
		typeList[3]="CORSOR";
		String[] paramList=new String[4];
		System.out.println("공통정보 paramMap = "+paramMap);
		if("M".equals(paramMap.get("FORM_TYPE")) || "R".equals(paramMap.get("FORM_TYPE"))){
			paramList[0]=paramMap.get("POC_NO").toString();
			paramList[1]="";
			paramList[2]="";
			paramList[3]="";
		}else{
			if(paramMap.containsKey("ORDER_NUMBER") && paramMap.containsKey("LINE_NUMBER") ){
				paramList[0]=paramMap.containsKey("PRE_POC_NO") ? paramMap.get("PRE_POC_NO").toString() : "";
				paramList[1]=paramMap.containsKey("ORDER_NUMBER") ? paramMap.get("ORDER_NUMBER").toString(): "";
				paramList[2]=paramMap.containsKey("LINE_NUMBER") ? paramMap.get("LINE_NUMBER").toString(): "";
				paramList[3]="";
			}else{
				paramList[0]=paramMap.containsKey("PRE_POC_NO") ? paramMap.get("PRE_POC_NO").toString() : "";
				paramList[1]="";
				paramList[2]="";
				paramList[3]="";
			}
		}*/
		//DSINFO.WIP_MWIPORDDAT.MWIPORDDAT_SEL18( :IN_WIP_ENTITY_NAME, :IN_ORDER_NUMBER, :IN_LINE_NUMBER, :OUT_CUR1);

		return dbu.call_QICS_Procedure("pop","DSINFO.WIP_MWIPORDDAT.MWIPORDDAT_SEL18", typeList,paramList);
	}
	public List divideList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		CommonCollect collect =null;
		collect = getDivideList("pop",paramMap);
		//	collect.getMetaData();
		//HashMap<String,CommonCollect> rfcOutData = collect.getMetaData();
		int idx = 0;
		if(collect ==null){
		}else{
			while (collect.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i=0;i<collect.getColumnCount();i++) {
					map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
					//System.out.println(i+"."+collect.getColumnName((i+1))+":"+"'"+collect.getString(collect.getColumnName((i+1)))+"',");
				}

				List<Entry> entryList = new ArrayList<Entry>(map.entrySet());
				list.add(idx, map);
				//	list.set(idx, map);
				idx++;
				//list = entryList;
			}
		}

		//System.out.println("list.size() = "+list.size());
		return list;
	}
	public HashMap divideList2(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		HashMap hm = new HashMap();
		//CommonCollect collect =null;
		hm = getDivideList2("pop",paramMap);

		//System.out.println("list.size() = "+hm.size());
		return hm;
	}
	private HashMap getDivideList2(String string, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try {
			Log.Debug("주문지 목록 조회  " + paramMap);
			String[] typeList=new String[5];
			typeList[0]="STRING";
			typeList[1]="STRING";
			typeList[2]="STRING";
			typeList[3]="CORSOR";
			typeList[4]="CORSOR";
			String[] paramList=new String[5];
			paramList[0]="";
			paramList[1]="";
			paramList[2]=paramMap.get("WIP_ENTITY_NAME").toString();
			paramList[3]="";
			paramList[4]="";
			return dbu.call_QICS_Procedure2("pop","DSINFO.WIP_MWIPSALDAT.MWIPSALDAT_SEL04", typeList,paramList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.Error("주문지 목록 Error  " + e.getMessage());
			return null;
		}
	}
	private CommonCollect getDivideList(String poolName,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//System.out.println(paramMap.toString());
		try {
			Log.Debug("주문지 목록1 조회  " + paramMap);
			String[] typeList=new String[5];
			typeList[0]="STRING";
			typeList[1]="STRING";
			typeList[2]="STRING";
			typeList[3]="CORSOR";
			typeList[4]="CORSOR";
			String[] paramList=new String[5];
			paramList[0]="";
			paramList[1]="";
			paramList[2]=paramMap.get("POC_NO").toString();
			paramList[3]="";
			paramList[4]="";
			return dbu.call_QICS_Procedure1("pop","DSINFO.WIP_MWIPSALDAT.MWIPSALDAT_SEL04", typeList,paramList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.Error("주문지 목록1 Error  " + e.getMessage());
			return null;
		}

		/*		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT CUSTOMER,       \n");
		sqlQuery.append("       CUSTOMER_NM,   \n");
		sqlQuery.append("       ORDER_NUMBER, \n");
		sqlQuery.append("       LINE_NUMBER,   \n");
		sqlQuery.append("       ORDER_MEMO     \n");
		sqlQuery.append("  FROM TEST300        \n");

		return dbu.getTableData(sqlQuery.toString(), poolName);*/

	}
	public String MQC_COIL_INS_CHK(String poolName,	Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//System.out.println(paramMap.toString());
		try {
			Log.Debug("MQC_COIL_INS_CHK  " + paramMap);
			String[] typeList=new String[11];
			typeList[0]="STRING"; // /*ufs_page_id*/
			typeList[1]="STRING"; /*ufs_code*/
			typeList[2]="DATE";  //날짜포맷? /*검사일자*/
			typeList[3]="STRING";
			typeList[4]="STRING";
			typeList[5]="FLOAT";
			typeList[6]="FLOAT";
			typeList[7]="STRING";
			typeList[8]="INT";
			typeList[9]="INT";
			typeList[10]="STRING";

			String[] paramList=new String[11];
			paramList[0]=paramMap.get("SEQ_Q100").toString();
			paramList[1]=paramMap.get("SEQ_T300").toString();
			paramList[2]=paramMap.get("WORK_DATE").toString();
			paramList[3]=paramMap.get("POC_NO").toString();
			paramList[4]=paramMap.get("IN_LINE").toString();
			paramList[5]=paramMap.get("C_STD02").toString();
			paramList[6]=paramMap.get("C_STD01").toString();
			paramList[7]=paramMap.get("NG_CODE").toString();
			paramList[8]=paramMap.get("C_LEN").toString();
			paramList[9]=paramMap.get("C_CHK_NO").toString();
			paramList[10]="";
			return dbu.call_QICS_Procedure4("erp","HB_QM_QICS_CHK_PKG.MQC_COIL_INS_CHK", typeList,paramList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	public String LQC_COIL_INS_CHK(String poolName,	Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//System.out.println(paramMap.toString());
		try {
			Log.Debug("LQC_COIL_INS_CHK  " + paramMap);
			String[] typeList=new String[12];
			typeList[0]="STRING"; // /*ufs_page_id*/
			typeList[1]="STRING"; /*ufs_code*/
			typeList[2]="DATE";  //날짜포맷? /*검사일자*/
			typeList[3]="STRING";
			typeList[4]="STRING";
			typeList[5]="INT";
			typeList[6]="FLOAT";
			typeList[7]="STRING";
			typeList[8]="STRING";
			typeList[9]="INT";
			typeList[10]="INT";
			typeList[11]="STRING";

			String[] paramList=new String[12];
			paramList[0]=paramMap.get("SEQ_Q100").toString();
			paramList[1]=paramMap.get("SEQ_T300").toString();
			paramList[2]=paramMap.get("WORK_DATE").toString();
			paramList[3]=paramMap.get("POC_NO").toString();
			paramList[4]=paramMap.get("IN_LINE").toString();
			paramList[5]=paramMap.get("C_STD02").toString();
			paramList[6]=paramMap.get("C_STD01").toString();
			paramList[7]=paramMap.get("NG_CODE").toString();
			paramList[8]=paramMap.get("C_PP").toString();
			paramList[9]=paramMap.get("C_LEN").toString();
			paramList[10]=paramMap.get("C_CHK_NO").toString();
			paramList[11]="";
			return dbu.call_QICS_Procedure4("erp","APPS.HB_QM_QICS_CHK_PKG.LQC_COIL_INS_CHK", typeList,paramList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null; 
		}
	}
	public String ME_COIL_INS_CHK(String poolName,	Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//System.out.println(paramMap.toString());
		try {
			String[] typeList=new String[9];
			typeList[0]="STRING"; // /*ufs_page_id*/
			typeList[1]="STRING"; /*ufs_code*/
			typeList[2]="DATE";  //날짜포맷? /*검사일자*/
			typeList[3]="STRING";
			typeList[4]="STRING";
			typeList[5]="INT";
			typeList[6]="FLOAT";
			typeList[7]="STRING";
			typeList[8]="STRING";

			String[] paramList=new String[9];
			paramList[0]=paramMap.get("SEQ_Q100").toString();
			paramList[1]=paramMap.get("SEQ_T300").toString();
			paramList[2]=paramMap.get("WORK_DATE").toString();
			paramList[3]=paramMap.get("POC_NO").toString();
			paramList[4]=paramMap.get("IN_LINE").toString();
			paramList[5]=paramMap.get("C_STD02").toString();
			paramList[6]=paramMap.get("C_STD01").toString();
			paramList[7]=paramMap.get("NG_CODE").toString();
			paramList[8]="";
			return dbu.call_QICS_Procedure4("erp","APPS.HB_QM_QICS_CHK_PKG.ME_COIL_INS_CHK", typeList,paramList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.Debug("오류입니다." + e.getMessage());
			return null;
		}
	}
	public CommonCollect HB_QM_UFS_IF_INSPECTION(String  poolName ,Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//	paramMap.put("SEQ_Q300","UNIW1F6E9CA6DC9A8035F2327B7D0138");
		//	paramMap.put("SEQ_Q100","ADMI1F6EC307C4BA0017649EAE6A6525");
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("      SELECT                                                                                     \n");
		sqlQuery.append("      UFS_PAGE_ID,                                                                               \n");
		sqlQuery.append("      UFS_CODE,                                                                                  \n");
		sqlQuery.append("      INS_GUBUN,                                                                                 \n");
		sqlQuery.append("      COLLECTION_ID,                                              \n");
		sqlQuery.append("      STEEL,                                                                                     \n");
		sqlQuery.append("      OPERATION_SEQ_NUM,                                                                         \n");
		sqlQuery.append("      MFG_WEIGHT,                                                                                \n");
		sqlQuery.append("      TRANSACTION_ID,                                                                            \n");
		sqlQuery.append("      STEEL_CODE,                                                                                \n");
		sqlQuery.append("      WIDTH,                                                                                     \n");
		sqlQuery.append("      THICKNESS,                                                                                 \n");
		sqlQuery.append("      ORDER_NUMBER,                                                                              \n");
		sqlQuery.append("      LINE_NUMBER,                                                                               \n");
		sqlQuery.append("      ORDER_USAGE_CODE,                                                                          \n");
		sqlQuery.append("      ORDER_USAGE_CODE_DESC,                                                                     \n");
		sqlQuery.append("      ORDER_USAGE_END_USER_CODE,                                                                 \n");
		sqlQuery.append("      ORDER_USAGE_DESC,                                                                          \n");
		sqlQuery.append("      HEAT_POC_NUMBER,                                                                           \n");
		sqlQuery.append("      INVENTORY_ITEM_ID,                                                                         \n");
		sqlQuery.append("      TICKNESS_STANDARD,                                                                         \n");
		sqlQuery.append("      TICKNESS_SPEC_RANGE,                                                                         \n");
		sqlQuery.append("      TO_CHAR(WORK_DATE,'YYYYMMDD') AS WORK_DATE,                                                \n");
		sqlQuery.append("      WORK_TEAM,                                                                                 \n");
		sqlQuery.append("      WIP_RESOURCE_CODE,                                                                         \n");
		sqlQuery.append("      ITEM_CODE,                                                                                 \n");
		sqlQuery.append("      TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') AS OP_START_DATE,                                        \n");
		sqlQuery.append("      '' AS POP_TRANSACTION_ID,                                                                  \n");
		sqlQuery.append("      WIP_RESOURCE_CODE AS RESOURCE_CODE,                                                                       \n");
		sqlQuery.append("      CREATION_DATE,                                                                             \n");
		sqlQuery.append("      CREATED_BY,                                                                                \n");
		sqlQuery.append("      LAST_UPDATE_DATE,                                                                          \n");
		sqlQuery.append("      LAST_UPDATED_BY,                                                                           \n");
		sqlQuery.append("      LAST_UPDATE_LOGIN                                                                          \n");
		sqlQuery.append("      FROM                                                                                       \n");
		sqlQuery.append("      HB_QM_UFS_IF_INSPECTION                                                                    \n");
		//sqlQuery.append("    WHERE UFS_PAGE_ID = 'UNIW1F6E9CA6DC9A8035F2327B7D0138'                                       \n");
		//sqlQuery.append("    AND UFS_CODE = 'ADMI1F6EC307C4BA0017649EAE6A6525'                                            \n");
		sqlQuery.append(" WHERE UFS_PAGE_ID = '"+paramMap.get("SEQ_Q100")+"' \n");
		sqlQuery.append(" AND UFS_CODE = '"+paramMap.get("SEQ_T300")+"' \n");




		//		sqlQuery.append(" WHERE UFS_PAGE_ID = '"+paramMap.get("SEQ_Q100")+"' \n");
		//		sqlQuery.append(" AND UFS_CODE = '"+paramMap.get("SEQ_T300")+"' \n");

		return dbu.getTableData(sqlQuery.toString(), poolName);
	}
	public int BOL_QA_MQC_COIL(List<?> list1, List<?> list2, List<?> list3, List<?> list4) {
		// TODO Auto-generated method stub
		String str ="";
		int resultCnt =-1;
		HashMap hm = new HashMap();
		try {

			String[] batchSQL = new String[list1.size()+list2.size()+list3.size()+list4.size()];
			StringBuffer sqlQuery = new StringBuffer();
			int currentRowIndex = 0;

			for (int i = 0; i < list1.size(); i++) {

				hm = (HashMap) list1.get(i);
				sqlQuery.append("   INSERT INTO BOL_QA_MQC_COIL_MST (    \n");
				sqlQuery.append("   COLLECTION_ID,                           \n");
				sqlQuery.append("   ORGANIZATION_ID,                         \n");
				sqlQuery.append("   POC_NUMBER,                              \n");
				sqlQuery.append("   OPERATION_SEQ_NUM,                       \n");
				sqlQuery.append("   OPERATION_RESOURCE,                      \n");
				sqlQuery.append("   INSPECTION_DATE,                         \n");
				sqlQuery.append("   INSPECTOR,                               \n");
				sqlQuery.append("   STEEL_KIND,                              \n");
				sqlQuery.append("   GRADE_RESULT,                            \n");
				sqlQuery.append("   THICKNESS,                               \n");
				sqlQuery.append("   WIDTH,                                   \n");
				sqlQuery.append("   MFG_WEIGHT,                              \n");
				sqlQuery.append("   TOTAL_LENGTH,                            \n");
				sqlQuery.append("   WIDTH_RESULT,                            \n");
				sqlQuery.append("   TICKNESS_STANDARD,                       \n");
				sqlQuery.append("   TICKNESS_RESULT,                         \n");
				sqlQuery.append("   TICKNESS_SPEC_RANGE,                     \n");
				sqlQuery.append("   GRADE,                                   \n");
				sqlQuery.append("   SURFACE_Q,                               \n");
				sqlQuery.append("   GRADE_SURFACE,                           \n");
				sqlQuery.append("   ROLL_STOP,                               \n");
				sqlQuery.append("   CREATION_DATE,                           \n");
				sqlQuery.append("   CREATED_BY,                              \n");
				sqlQuery.append("   LAST_UPDATE_DATE,                        \n");
				sqlQuery.append("   LAST_UPDATED_BY,                         \n");
				sqlQuery.append("   LAST_UPDATE_LOGIN,                       \n");
				sqlQuery.append("   POP_TRANSACTION_ID,                      \n");
				sqlQuery.append("   OPERATOR,                                \n");
				sqlQuery.append("   TRANSACTION_ID,                          \n");
				sqlQuery.append("   WORK_DATE,                               \n");
				sqlQuery.append("   WORK_TEAM,                               \n");
				sqlQuery.append("   RESOURCE_CODE,                           \n");
				sqlQuery.append("   ITEM_CODE,                               \n");
				sqlQuery.append("   INVENTORY_ITEM_ID,                       \n");
				sqlQuery.append("   ORDER_NUMBER,                            \n");
				sqlQuery.append("   LINE_NUMBER,                             \n");
				sqlQuery.append("   OP_START_DATE,                           \n");
				sqlQuery.append("   ORDER_USAGE_CODE,                        \n");
				sqlQuery.append("   ORDER_USAGE_DESC,                        \n");
				sqlQuery.append("   ORDER_USAGE_END_USER_CODE,               \n");
				sqlQuery.append("   MAIN_NG_CODE,                            \n");
				sqlQuery.append("   NG_QUANTITY,                             \n");
				sqlQuery.append("   HEAT_POC_NUMBER,                         \n");
				sqlQuery.append("   STEEL_CODE,                              \n");
				sqlQuery.append("   ROLL_STOP1,                              \n");
				sqlQuery.append("   ROLL_STOP2,                               \n");
				sqlQuery.append("   ROLL_STOP3,                               \n");
				sqlQuery.append("   ROLL_STOP4,                               \n");
				sqlQuery.append("   ROLL_STOP5,                               \n");
				sqlQuery.append("   ROLL_STOP6,                               \n");
				sqlQuery.append("   ROLL_STOP7                               \n");

				sqlQuery.append("   ) VALUES (                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ORGANIZATION_ID"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("POC_NUMBER"),"string", "") +",   \n");

				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OPERATION_SEQ_NUM"),"int", "")+",                       \n");

				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("OPERATION_RESOURCE"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INSPECTION_DATE").toString().replaceAll("/", ""),"date", "YYYY-MM-DD") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INSPECTOR"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("STEEL_KIND"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("GRADE_RESULT"),"string", "") +",   \n");

				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("THICKNESS"),"int", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WIDTH"),"int", "")+",                                   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("MFG_WEIGHT"),"int", "")+",                              \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TOTAL_LENGTH"),"int", "") +",   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WIDTH_RESULT"),"int", "") +",   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TICKNESS_STANDARD"),"int", "")+",                       \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TICKNESS_RESULT"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("TICKNESS_SPEC_RANGE"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("GRADE"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("SURFACE_Q"),"string", "") +",                       \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("GRADE_SURFACE"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP"),"int", "") +",   \n");
				sqlQuery.append("   SYSDATE,                           \n");
				sqlQuery.append("   '-1',                              \n");
				sqlQuery.append("   SYSDATE,                        \n");
				sqlQuery.append("   '-1',                         \n");
				sqlQuery.append("   '-1',                       \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("POP_TRANSACTION_ID"),"string", "") +",                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("OPERATOR"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("TRANSACTION_ID"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("WORK_DATE").toString(),"date", "YYYY-MM-DD") +",   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WORK_TEAM"),"string", "") +",                               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("RESOURCE_CODE"),"string", "") +",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ITEM_CODE"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INVENTORY_ITEM_ID"),"int", "")+",                       \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_NUMBER"),"int", "")+",                            \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("LINE_NUMBER"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("OP_START_DATE").toString(),"date", "yyyy-mm-dd hh24miss") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ORDER_USAGE_CODE"),"string", "") +",                        \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ORDER_USAGE_DESC"),"string", "")  +",                        \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ORDER_USAGE_END_USER_CODE"),"string", "") +",               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("MAIN_NG_CODE"),"string", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("NG_QUANTITY"),"int", "") +",   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("HEAT_POC_NUMBER"),"string", "")+",                         \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("STEEL_CODE"),"string", "") +",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP1"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP2"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP3"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP4"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP5"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP6"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP7"),"int", "") +"   \n");

				sqlQuery.append("   )                                        \n");
				//System.out.println("BOL_QA_MQC_COIL_MST = "+sqlQuery);
				batchSQL[currentRowIndex] = new String(sqlQuery);
				currentRowIndex++;
			}


			for (int i = 0; i < list2.size(); i++) {
				hm = new HashMap();
				hm = (HashMap) list2.get(i);
				sqlQuery = new StringBuffer();
				sqlQuery.append("   INSERT INTO BOL_QA_MQC_COIL_DETAIL1 (    \n");
				sqlQuery.append("   COLLECTION_ID,                            \n");
				sqlQuery.append("   NG_CODE_ID,                               \n");
				sqlQuery.append("   SURFACE_B,                                \n");
				sqlQuery.append("   SURFACE_C,                                \n");
				sqlQuery.append("   SURFACE_D,                                \n");
				sqlQuery.append("   SURFACE_K,                                \n");
				sqlQuery.append("   REAR_B,                                   \n");
				sqlQuery.append("   REAR_C,                                   \n");
				sqlQuery.append("   REAR_D,                                   \n");
				sqlQuery.append("   REAR_K,                                   \n");
				sqlQuery.append("   CREATION_DATE,                            \n");
				sqlQuery.append("   CREATED_BY,                               \n");
				sqlQuery.append("   LAST_UPDATE_DATE,                         \n");
				sqlQuery.append("   LAST_UPDATED_BY,                          \n");
				sqlQuery.append("   LAST_UPDATE_LOGIN                         \n");

				sqlQuery.append("   ) VALUES (                                \n");

				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("NG_CODE_ID"),"string", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SURFACE_B"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SURFACE_C"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SURFACE_D"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SURFACE_K"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("REAR_B"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("REAR_C"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("REAR_D"),"int", "0")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("REAR_K"),"int", "0")+",                           \n");
				sqlQuery.append("   SYSDATE,                            \n");
				sqlQuery.append("   '-1',                               \n");
				sqlQuery.append("   SYSDATE,                         \n");
				sqlQuery.append("   '-1',                          \n");
				sqlQuery.append("   '-1'                         \n");
				sqlQuery.append("   )                                         \n");
				batchSQL[currentRowIndex] = new String(sqlQuery);
				currentRowIndex++;
				//System.out.println(sqlQuery);
			}
			for (int i = 0; i < list3.size(); i++) {
				hm = new HashMap();
				hm = (HashMap) list3.get(i);
				sqlQuery = new StringBuffer();
				sqlQuery.append("   INSERT INTO BOL_QA_MQC_COIL_DETAIL2 (    \n");
				sqlQuery.append("   COLLECTION_ID,                            \n");
				sqlQuery.append("   COLLECT_TYPE,                             \n");
				sqlQuery.append("   M_START,                                  \n");
				sqlQuery.append("   M_END,                                    \n");
				sqlQuery.append("   CREATION_DATE,                            \n");
				sqlQuery.append("   CREATED_BY,                               \n");
				sqlQuery.append("   LAST_UPDATE_DATE,                         \n");
				sqlQuery.append("   LAST_UPDATED_BY,                          \n");
				sqlQuery.append("   LAST_UPDATE_LOGIN                         \n");

				sqlQuery.append("   ) VALUES (                                \n");

				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COLLECT_TYPE"),"string", "") +",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("M_START"),"int", "") +",   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("M_END"),"int", "") +",   \n");
				sqlQuery.append("   SYSDATE,                            \n");
				sqlQuery.append("   '-1',                               \n");
				sqlQuery.append("   SYSDATE,                         \n");
				sqlQuery.append("   '-1',                          \n");
				sqlQuery.append("   '-1'                         \n");
				sqlQuery.append("   )                                         \n");
				batchSQL[currentRowIndex] = new String(sqlQuery);
				currentRowIndex++;
				//System.out.println(sqlQuery);
			}

			for (int j = 0; j < list4.size(); j++) {
				hm = new HashMap();
				hm = (HashMap) list4.get(j);
				sqlQuery = new StringBuffer();
				sqlQuery.append("   INSERT INTO BOL_QA_MQC_COIL_DETAIL3 (    \n");
				sqlQuery.append("   COLLECTION_ID,                            \n");
				sqlQuery.append("   A1,                                       \n");
				sqlQuery.append("   A2,                                       \n");
				sqlQuery.append("   A3,                                       \n");
				sqlQuery.append("   A4,                                       \n");
				sqlQuery.append("   A5,                                       \n");
				sqlQuery.append("   A6,                                       \n");
				sqlQuery.append("   A7,                                       \n");
				sqlQuery.append("   A8,                                       \n");
				sqlQuery.append("   A9,                                       \n");
				sqlQuery.append("   A10,                                      \n");
				sqlQuery.append("   A11,                                      \n");
				sqlQuery.append("   A12,                                      \n");
				sqlQuery.append("   A13,                                      \n");
				sqlQuery.append("   A14,                                      \n");
				sqlQuery.append("   A15,                                      \n");
				sqlQuery.append("   A16,                                      \n");
				sqlQuery.append("   A17,                                      \n");
				sqlQuery.append("   A18,                                      \n");
				sqlQuery.append("   B1,                                       \n");
				sqlQuery.append("   B2,                                       \n");
				sqlQuery.append("   B3,                                       \n");
				sqlQuery.append("   B4,                                       \n");
				sqlQuery.append("   B5,                                       \n");
				sqlQuery.append("   B6,                                       \n");
				sqlQuery.append("   B7,                                       \n");
				sqlQuery.append("   B8,                                       \n");
				sqlQuery.append("   B9,                                       \n");
				sqlQuery.append("   B10,                                      \n");
				sqlQuery.append("   B11,                                      \n");
				sqlQuery.append("   B12,                                      \n");
				sqlQuery.append("   B13,                                      \n");
				sqlQuery.append("   B14,                                      \n");
				sqlQuery.append("   B15,                                      \n");
				sqlQuery.append("   B16,                                      \n");
				sqlQuery.append("   B17,                                      \n");
				sqlQuery.append("   B18,                                      \n");
				sqlQuery.append("   C1,                                       \n");
				sqlQuery.append("   C2,                                       \n");
				sqlQuery.append("   C3,                                       \n");
				sqlQuery.append("   C4,                                       \n");
				sqlQuery.append("   C5,                                       \n");
				sqlQuery.append("   C6,                                       \n");
				sqlQuery.append("   C7,                                       \n");
				sqlQuery.append("   C8,                                       \n");
				sqlQuery.append("   C9,                                       \n");
				sqlQuery.append("   C10,                                      \n");
				sqlQuery.append("   C11,                                      \n");
				sqlQuery.append("   C12,                                      \n");
				sqlQuery.append("   C13,                                      \n");
				sqlQuery.append("   C14,                                      \n");
				sqlQuery.append("   C15,                                      \n");
				sqlQuery.append("   C16,                                      \n");
				sqlQuery.append("   C17,                                      \n");
				sqlQuery.append("   C18,                                      \n");
				sqlQuery.append("   D1,                                       \n");
				sqlQuery.append("   D2,                                       \n");
				sqlQuery.append("   D3,                                       \n");
				sqlQuery.append("   D4,                                       \n");
				sqlQuery.append("   D5,                                       \n");
				sqlQuery.append("   D6,                                       \n");
				sqlQuery.append("   D7,                                       \n");
				sqlQuery.append("   D8,                                       \n");
				sqlQuery.append("   D9,                                       \n");
				sqlQuery.append("   D10,                                      \n");
				sqlQuery.append("   D11,                                      \n");
				sqlQuery.append("   D12,                                      \n");
				sqlQuery.append("   D13,                                      \n");
				sqlQuery.append("   D14,                                      \n");
				sqlQuery.append("   E1,                                       \n");
				sqlQuery.append("   E2,                                       \n");
				sqlQuery.append("   E3,                                       \n");
				sqlQuery.append("   E4,                                       \n");
				sqlQuery.append("   E5,                                       \n");
				sqlQuery.append("   E6,                                       \n");
				sqlQuery.append("   E7,                                       \n");
				sqlQuery.append("   E8,                                       \n");
				sqlQuery.append("   E9,                                       \n");
				sqlQuery.append("   E10,                                      \n");
				sqlQuery.append("   E11,                                      \n");
				sqlQuery.append("   E12,                                      \n");
				sqlQuery.append("   E13,                                      \n");
				sqlQuery.append("   E14,                                      \n");
				sqlQuery.append("   F1,                                       \n");
				sqlQuery.append("   F2,                                       \n");
				sqlQuery.append("   F3,                                       \n");
				sqlQuery.append("   F4,                                       \n");
				sqlQuery.append("   F5,                                       \n");
				sqlQuery.append("   F6,                                       \n");
				sqlQuery.append("   F7,                                       \n");
				sqlQuery.append("   F8,                                       \n");
				sqlQuery.append("   F9,                                       \n");
				sqlQuery.append("   F10,                                      \n");
				sqlQuery.append("   F11,                                      \n");
				sqlQuery.append("   F12,                                      \n");
				sqlQuery.append("   F13,                                      \n");
				sqlQuery.append("   F14,                                      \n");
				sqlQuery.append("   G1,                                       \n");
				sqlQuery.append("   G2,                                       \n");
				sqlQuery.append("   G3,                                       \n");
				sqlQuery.append("   G4,                                       \n");
				sqlQuery.append("   G5,                                       \n");
				sqlQuery.append("   G6,                                       \n");
				sqlQuery.append("   G7,                                       \n");
				sqlQuery.append("   G8,                                       \n");
				sqlQuery.append("   G9,                                       \n");
				sqlQuery.append("   G10,                                      \n");
				sqlQuery.append("   G11,                                      \n");
				sqlQuery.append("   G12,                                      \n");
				sqlQuery.append("   G13,                                      \n");
				sqlQuery.append("   G14,                                      \n");
				sqlQuery.append("   H1,                                       \n");
				sqlQuery.append("   H2,                                       \n");
				sqlQuery.append("   H3,                                       \n");
				sqlQuery.append("   H4,                                       \n");
				sqlQuery.append("   H5,                                       \n");
				sqlQuery.append("   H6,                                       \n");
				sqlQuery.append("   H7,                                       \n");
				sqlQuery.append("   H8,                                       \n");
				sqlQuery.append("   H9,                                       \n");
				sqlQuery.append("   H10,                                      \n");
				sqlQuery.append("   H11,                                      \n");
				sqlQuery.append("   H12,                                      \n");
				sqlQuery.append("   H13,                                      \n");
				sqlQuery.append("   H14,                                      \n");
				sqlQuery.append("   I1,                                       \n");
				sqlQuery.append("   I2,                                       \n");
				sqlQuery.append("   I3,                                       \n");
				sqlQuery.append("   I4,                                       \n");
				sqlQuery.append("   J1,                                       \n");
				sqlQuery.append("   J2,                                       \n");
				sqlQuery.append("   J3,                                       \n");
				sqlQuery.append("   J4,                                       \n");
				sqlQuery.append("   K1,                                       \n");
				sqlQuery.append("   K2,                                       \n");
				sqlQuery.append("   K3,                                       \n");
				sqlQuery.append("   K4,                                       \n");
				sqlQuery.append("   L1,                                       \n");
				sqlQuery.append("   L2,                                       \n");
				sqlQuery.append("   L3,                                       \n");
				sqlQuery.append("   L4,                                       \n");
				sqlQuery.append("   CREATED_BY,                               \n");
				sqlQuery.append("   CREATION_DATE,                            \n");
				sqlQuery.append("   LAST_UPDATED_BY,                          \n");
				sqlQuery.append("   LAST_UPDATE_DATE,                         \n");
				sqlQuery.append("   LAST_UPDATE_LOGIN                         \n");

				sqlQuery.append("   ) VALUES (                                \n");

				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "")+",                           \n");
				for (int i = 1; i <= 18; i++) {
					String id ="A"+i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"int", "") +",   \n");
				}

				for (int i = 1; i <= 18; i++) {
					String id ="B"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"int", "") +",   \n");
				}

				for (int i = 1; i <= 18; i++) {
					String id ="C"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"int", "") +",   \n");
				}

				for (int i = 1; i <= 14; i++) {
					String id ="D"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"int", "") +",   \n");
				}

				for (int i = 1; i <= 14; i++) {
					String id ="E"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"string", "") +",   \n");
				}

				for (int i = 1; i <= 14; i++) {
					String id ="F"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"string", "") +",   \n");
				}

				for (int i = 1; i <= 14; i++) {
					String id ="G"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"string", "") +",   \n");
				}

				for (int i = 1; i <= 14; i++) {
					String id ="H"+ i;
					sqlQuery.append("   "+ StringUtil.convertAppend(hm.get(id),"string", "") +",  \n");
				}


				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("I1"),"int", "") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("I2"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("I3"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("I4"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("J1"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("J2"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("J3"),"int", "0") +",  \n");
				sqlQuery.append("   "+  StringUtil.convertAppend(hm.get("J4"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("K1"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("K2"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("K3"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("K4"),"int", "0") +",  \n");
				sqlQuery.append("   "+  StringUtil.convertAppend(hm.get("L1"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("L2"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("L3"),"int", "0") +",  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("L4"),"int", "0") +",  \n");

				sqlQuery.append("   '-1',                               \n");
				sqlQuery.append("   SYSDATE,                            \n");
				sqlQuery.append("   '-1',                          \n");
				sqlQuery.append("   SYSDATE,                         \n");
				sqlQuery.append("   '-1'                         \n");
				sqlQuery.append("   )                                         \n");
				batchSQL[currentRowIndex] = new String(sqlQuery);
				//System.out.println(sqlQuery);

			}
			resultCnt = dbu.execBatchSQL("erp",batchSQL);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultCnt;
	}

	public int BOL_QA_ME_COIL(HashMap hm, Map<String, Object> paramMap,
			JSONArray codeType, JSONArray codeSum) {
		int result =-1;
		String[] batchSQL = new String[1];
		StringBuffer sqlQuery = new StringBuffer();
		int currentRowIndex = 0;
		sqlQuery.append("   INSERT INTO BOL_QA_ME_COIL_RESULTS (    \n");
		sqlQuery.append("   POC_NUMBER,                              \n");
		sqlQuery.append("   INSPECTION_DATE,                         \n");
		sqlQuery.append("   ORGANIZATION_ID,                         \n");
		sqlQuery.append("   STEEL,                                   \n");
		sqlQuery.append("   SURFACE,                                 \n");
		sqlQuery.append("   THICKNESS,                               \n");
		sqlQuery.append("   WIDTH,                                   \n");
		sqlQuery.append("   GRADE,                                   \n");
		sqlQuery.append("   WEIGHT,                                  \n");
		sqlQuery.append("   INS_POSITION1,                           \n");
		sqlQuery.append("   INS_POSITION2,                           \n");
		sqlQuery.append("   INS_POSITION3,                           \n");
		sqlQuery.append("   INS_POSITION4,                           \n");
		sqlQuery.append("   INS_POSITION5,                           \n");
		sqlQuery.append("   INS_POSITION6,                           \n");
		sqlQuery.append("   INS_POSITION7,                           \n");
		sqlQuery.append("   INS_POSITION8,                           \n");
		sqlQuery.append("   INS_POSITION9,                           \n");
		sqlQuery.append("   INS_POSITION10,                          \n");
		sqlQuery.append("   INS_POSITION11,                          \n");
		sqlQuery.append("   INS_POSITION12,                          \n");
		sqlQuery.append("   INS_POSITION13,                          \n");
		sqlQuery.append("   INS_POSITION14,                          \n");
		sqlQuery.append("   INS_POSITION15,                          \n");
		sqlQuery.append("   INS_POSITION16,                          \n");
		sqlQuery.append("   INS_POSITION17,                          \n");
		sqlQuery.append("   INS_POSITION18,                          \n");
		sqlQuery.append("   INS_THICKNESS1,                          \n");
		sqlQuery.append("   INS_THICKNESS2,                          \n");
		sqlQuery.append("   INS_THICKNESS3,                          \n");
		sqlQuery.append("   INS_THICKNESS4,                          \n");
		sqlQuery.append("   INS_THICKNESS5,                          \n");
		sqlQuery.append("   INS_THICKNESS6,                          \n");
		sqlQuery.append("   INS_THICKNESS7,                          \n");
		sqlQuery.append("   INS_THICKNESS8,                          \n");
		sqlQuery.append("   INS_THICKNESS9,                          \n");
		sqlQuery.append("   INS_THICKNESS10,                         \n");
		sqlQuery.append("   INS_THICKNESS11,                         \n");
		sqlQuery.append("   INS_THICKNESS12,                         \n");
		sqlQuery.append("   INS_THICKNESS13,                         \n");
		sqlQuery.append("   INS_THICKNESS14,                         \n");
		sqlQuery.append("   INS_THICKNESS15,                         \n");
		sqlQuery.append("   INS_THICKNESS16,                         \n");
		sqlQuery.append("   INS_THICKNESS17,                         \n");
		sqlQuery.append("   INS_THICKNESS18,                         \n");
		sqlQuery.append("   INS_WIDTH1,                              \n");
		sqlQuery.append("   INS_WIDTH2,                              \n");
		sqlQuery.append("   INS_WIDTH3,                              \n");
		sqlQuery.append("   INS_WIDTH4,                              \n");
		sqlQuery.append("   INS_WIDTH5,                              \n");
		sqlQuery.append("   INS_WIDTH6,                              \n");
		sqlQuery.append("   INS_WIDTH7,                              \n");
		sqlQuery.append("   INS_WIDTH8,                              \n");
		sqlQuery.append("   INS_WIDTH9,                              \n");
		sqlQuery.append("   INS_WIDTH10,                             \n");
		sqlQuery.append("   INS_WIDTH11,                             \n");
		sqlQuery.append("   INS_WIDTH12,                             \n");
		sqlQuery.append("   INS_WIDTH13,                             \n");
		sqlQuery.append("   INS_WIDTH14,                             \n");
		sqlQuery.append("   INS_WIDTH15,                             \n");
		sqlQuery.append("   INS_WIDTH16,                             \n");
		sqlQuery.append("   INS_WIDTH17,                             \n");
		sqlQuery.append("   INS_WIDTH18,                             \n");
		sqlQuery.append("   COILLING_RANGE,                          \n");
		sqlQuery.append("   COIL_LENGTH,                             \n");
		sqlQuery.append("   CERTIFIED_SURFACE,                       \n");
		sqlQuery.append("   ROLL_STOP,                               \n");
		sqlQuery.append("   COILLING_INSIDE_TOP,                     \n");
		sqlQuery.append("   COILLING_INSIDE_END,                     \n");
		sqlQuery.append("   COILLING_OUTSIDE_TOP,                    \n");
		sqlQuery.append("   COILLING_OUTSIDE_END,                    \n");
		sqlQuery.append("   INVENTORY_ITEM_ID,                       \n");
		sqlQuery.append("   LAST_RESOURCE_CODE,                      \n");
		sqlQuery.append("   WRITER,                                  \n");
		sqlQuery.append("   ORDER_NUMBER,                            \n");
		sqlQuery.append("   LINE_NUMBER,                             \n");
		sqlQuery.append("   INS_POC_NUMBER,                          \n");
		sqlQuery.append("   INS_COLLECTION_ID,                       \n");
		sqlQuery.append("   HEAT_POC_NUMBER,                         \n");
		sqlQuery.append("   INSPECTOR,                               \n");
		sqlQuery.append("   COIL_CENTER,                             \n");
		sqlQuery.append("   CUSTOMER_NAME,                           \n");
		sqlQuery.append("   INSPECT_NAME,                            \n");
		sqlQuery.append("   ITEM_CODE,                               \n");
		sqlQuery.append("   COILLING_INSIDE_TOP_DESC,                \n");
		sqlQuery.append("   COILLING_INSIDE_END_DESC,                \n");
		sqlQuery.append("   COILLING_OUTSIDE_TOP_DESC,               \n");
		sqlQuery.append("   COILLING_OUTSIDE_END_DESC,               \n");
		sqlQuery.append("   PRT_CNT,                                 \n");
		sqlQuery.append("   CREATION_DATE,                           \n");
		sqlQuery.append("   CREATED_BY,                              \n");
		sqlQuery.append("   LAST_UPDATE_DATE,                        \n");
		sqlQuery.append("   LAST_UPDATED_BY,                         \n");
		sqlQuery.append("   LAST_UPDATE_LOGIN,                       \n");
		sqlQuery.append("   STEEL_CODE,                              \n");
		sqlQuery.append("   D1,                                      \n");
		sqlQuery.append("   D2,                                      \n");
		sqlQuery.append("   D3,                                      \n");
		sqlQuery.append("   D4,                                      \n");
		sqlQuery.append("   D5,                                      \n");
		sqlQuery.append("   D6,                                      \n");
		sqlQuery.append("   D7,                                      \n");
		sqlQuery.append("   D8,                                      \n");
		sqlQuery.append("   D9,                                      \n");
		sqlQuery.append("   D10,                                     \n");
		sqlQuery.append("   D11,                                     \n");
		sqlQuery.append("   D12,                                     \n");
		sqlQuery.append("   D13,                                     \n");
		sqlQuery.append("   D14,                                     \n");
		sqlQuery.append("   E1,                                      \n");
		sqlQuery.append("   E2,                                      \n");
		sqlQuery.append("   E3,                                      \n");
		sqlQuery.append("   E4,                                      \n");
		sqlQuery.append("   E5,                                      \n");
		sqlQuery.append("   E6,                                      \n");
		sqlQuery.append("   E7,                                      \n");
		sqlQuery.append("   E8,                                      \n");
		sqlQuery.append("   E9,                                      \n");
		sqlQuery.append("   E10,                                     \n");
		sqlQuery.append("   E11,                                     \n");
		sqlQuery.append("   E12,                                     \n");
		sqlQuery.append("   E13,                                     \n");
		sqlQuery.append("   E14,                                     \n");
		sqlQuery.append("   F1,                                      \n");
		sqlQuery.append("   F2,                                      \n");
		sqlQuery.append("   F3,                                      \n");
		sqlQuery.append("   F4,                                      \n");
		sqlQuery.append("   F5,                                      \n");
		sqlQuery.append("   F6,                                      \n");
		sqlQuery.append("   F7,                                      \n");
		sqlQuery.append("   F8,                                      \n");
		sqlQuery.append("   F9,                                      \n");
		sqlQuery.append("   F10,                                     \n");
		sqlQuery.append("   F11,                                     \n");
		sqlQuery.append("   F12,                                     \n");
		sqlQuery.append("   F13,                                     \n");
		sqlQuery.append("   F14,                                     \n");
		sqlQuery.append("   G1,                                      \n");
		sqlQuery.append("   G2,                                      \n");
		sqlQuery.append("   G3,                                      \n");
		sqlQuery.append("   G4,                                      \n");
		sqlQuery.append("   G5,                                      \n");
		sqlQuery.append("   G6,                                      \n");
		sqlQuery.append("   G7,                                      \n");
		sqlQuery.append("   G8,                                      \n");
		sqlQuery.append("   G9,                                      \n");
		sqlQuery.append("   G10,                                     \n");
		sqlQuery.append("   G11,                                     \n");
		sqlQuery.append("   G12,                                     \n");
		sqlQuery.append("   G13,                                     \n");
		sqlQuery.append("   G14,                                     \n");
		sqlQuery.append("   H1,                                      \n");
		sqlQuery.append("   H2,                                      \n");
		sqlQuery.append("   H3,                                      \n");
		sqlQuery.append("   H4,                                      \n");
		sqlQuery.append("   H5,                                      \n");
		sqlQuery.append("   H6,                                      \n");
		sqlQuery.append("   H7,                                      \n");
		sqlQuery.append("   H8,                                      \n");
		sqlQuery.append("   H9,                                      \n");
		sqlQuery.append("   H10,                                     \n");
		sqlQuery.append("   H11,                                     \n");
		sqlQuery.append("   H12,                                     \n");
		sqlQuery.append("   H13,                                     \n");
		sqlQuery.append("   H14,                                     \n");
		sqlQuery.append("   CUSTOMER_TAX_NO,                         \n");
		sqlQuery.append("   COIL_CENTER_TAX_NO,                      \n");
		sqlQuery.append("   TRANS_FLAG                               \n");
		sqlQuery.append("   ) VALUES (                               \n");
		sqlQuery.append("   POC_NUMBER,                              \n");
		sqlQuery.append("   INSPECTION_DATE,                         \n");
		sqlQuery.append("   ORGANIZATION_ID,                         \n");
		sqlQuery.append("   STEEL,                                   \n");
		sqlQuery.append("   SURFACE,                                 \n");
		sqlQuery.append("   THICKNESS,                               \n");
		sqlQuery.append("   WIDTH,                                   \n");
		sqlQuery.append("   GRADE,                                   \n");
		sqlQuery.append("   WEIGHT,                                  \n");
		for (int i = 1; i <= 18; i++) {
			String id ="C_MSP"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"int", "") +",   \n");
		}

		for (int i = 1; i <= 18; i++) {
			String id ="C_MST"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"int", "") +",   \n");
		}

		for (int i = 1; i <= 18; i++) {
			String id ="C_MSW"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"int", "") +",   \n");
		}
		sqlQuery.append("   COILLING_RANGE,                          \n");
		sqlQuery.append("   COIL_LENGTH,                             \n");
		sqlQuery.append("   CERTIFIED_SURFACE,                       \n");
		sqlQuery.append("   ROLL_STOP,                               \n");
		sqlQuery.append("   COILLING_INSIDE_TOP,                     \n");
		sqlQuery.append("   COILLING_INSIDE_END,                     \n");
		sqlQuery.append("   COILLING_OUTSIDE_TOP,                    \n");
		sqlQuery.append("   COILLING_OUTSIDE_END,                    \n");
		sqlQuery.append("   INVENTORY_ITEM_ID,                       \n");
		sqlQuery.append("   LAST_RESOURCE_CODE,                      \n");
		sqlQuery.append("   WRITER,                                  \n");
		sqlQuery.append("   ORDER_NUMBER,                            \n");
		sqlQuery.append("   LINE_NUMBER,                             \n");
		sqlQuery.append("   INS_POC_NUMBER,                          \n");
		sqlQuery.append("   INS_COLLECTION_ID,                       \n");
		sqlQuery.append("   HEAT_POC_NUMBER,                         \n");
		sqlQuery.append("   INSPECTOR,                               \n");
		sqlQuery.append("   COIL_CENTER,                             \n");
		sqlQuery.append("   CUSTOMER_NAME,                           \n");
		sqlQuery.append("   INSPECT_NAME,                            \n");
		sqlQuery.append("   ITEM_CODE,                               \n");
		sqlQuery.append("   COILLING_INSIDE_TOP_DESC,                \n");
		sqlQuery.append("   COILLING_INSIDE_END_DESC,                \n");
		sqlQuery.append("   COILLING_OUTSIDE_TOP_DESC,               \n");
		sqlQuery.append("   COILLING_OUTSIDE_END_DESC,               \n");
		sqlQuery.append("   PRT_CNT,                                 \n");
		sqlQuery.append("   CREATION_DATE,                           \n");
		sqlQuery.append("   CREATED_BY,                              \n");
		sqlQuery.append("   LAST_UPDATE_DATE,                        \n");
		sqlQuery.append("   LAST_UPDATED_BY,                         \n");
		sqlQuery.append("   LAST_UPDATE_LOGIN,                       \n");
		sqlQuery.append("   STEEL_CODE,                              \n");
		for (int i = 1; i <= 14; i++) {
			String id ="C_M"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"int", "") +",   \n");
		}

		for (int i = 1; i <= 14; i++) {
			String id ="C_SH"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"string", "") +",   \n");
		}

		for (int i = 1; i <= 14; i++) {
			String id ="C_ST"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"string", "") +",   \n");
		}

		for (int i = 1; i <= 14; i++) {
			String id ="C_RH"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"string", "") +",   \n");
		}

		for (int i = 1; i <= 14; i++) {
			String id ="C_RT"+(i<10 ? "0"+i : i);
			sqlQuery.append("   "+ StringUtil.convertAppend(paramMap.get(id),"string", "") +",  \n");
		}
		sqlQuery.append("   CUSTOMER_TAX_NO,                         \n");
		sqlQuery.append("   COIL_CENTER_TAX_NO,                      \n");
		sqlQuery.append("   TRANS_FLAG                               \n");
		sqlQuery.append("   )                                        \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		sqlQuery.append("                                            \n");
		batchSQL[currentRowIndex] = new String(sqlQuery);
		//System.out.println(sqlQuery);
		int resultCnt = dbu.execBatchSQL("erp",batchSQL);
		return result;
	}
	public int BOL_QA_LQC_COIL(List<?> list1) {
		// TODO Auto-generated method stub
		int resultCnt =-1;
		HashMap hm = new HashMap();
		try {
			String[] batchSQL = new String[list1.size()];

			int currentRowIndex = 0;
			for (int i = 0; i < list1.size(); i++) {
				hm = (HashMap) list1.get(i);
				StringBuffer sqlQuery = new StringBuffer();
				/***************************************
				 * 임성현 수정부(2016-05-11)
				 * 수정내용 : 1. 최종검사 실적을 템프테이블에 적재하도록 변경
				 *           BOL_QA_LQC_COIL_RESULTS > HB_QM_LQC_COIL_TRANSFERS
				 *        2. 이미 입력된 내역이 있을경우 UPDATE하고 없을 경우 INSERT 처리 하도록 MERGE구문 으로 변경
				***************************************/
				
				sqlQuery.append("   MERGE INTO HB_QM_LQC_COIL_TRANSFERS		\n");
				sqlQuery.append("         USING DUAL	\n");
				sqlQuery.append("         ON (COLLECTION_ID = "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "")+" AND POC_NUMBER = "+StringUtil.convertAppend(hm.get("POC_NUMBER"),"string", "")+" )        \n");
				sqlQuery.append("         WHEN MATCHED THEN      \n");
				sqlQuery.append("              UPDATE SET      \n");
				sqlQuery.append("                    ORGANIZATION_ID             = "+StringUtil.convertAppend(hm.get("ORGANIZATION_ID"),"int", "")+",                          \n");
				sqlQuery.append("                    OPERATION_RESOURCE          = "+StringUtil.convertAppend(hm.get("OPERATION_RESOURCE"),"string", "")+",                       \n");                       
				sqlQuery.append("                    INSPECTION_DATE             = "+StringUtil.convertAppend(hm.get("INSPECTION_DATE").toString().replaceAll("/", ""),"date", "YYYY-MM-DD") +",    \n");                    
				sqlQuery.append("                    INSPECTOR                   = "+StringUtil.convertAppend(hm.get("INSPECTOR"),"string", "")+",                                \n");                                    
				sqlQuery.append("                    WEIGHT                      = "+StringUtil.convertAppend(hm.get("WEIGHT"),"int", "")+",                                   \n");                                          
				sqlQuery.append("                    TICKNESS                    = "+StringUtil.convertAppend(hm.get("TICKNESS"),"int", "")+",                                 \n");                                        
				sqlQuery.append("                    WIDTH                       = "+StringUtil.convertAppend(hm.get("WIDTH"),"int", "")+",                                    \n");                                           
				sqlQuery.append("                    LENGTH                      = "+StringUtil.convertAppend(hm.get("LENGTH"),"int", "")+",                                   \n");                                          
				sqlQuery.append("                    SURFACE                     = "+StringUtil.convertAppend(hm.get("SURFACE"),"string", "")+",                                  \n");                                      
				sqlQuery.append("                    COATING                     = "+StringUtil.convertAppend(hm.get("COATING"),"string", "")+",                                  \n");                                      
				sqlQuery.append("                    OK_NG                       = "+StringUtil.convertAppend(hm.get("OK_NG"),"string", "")+",                                    \n");                                        
				sqlQuery.append("                    GRADE                       = "+StringUtil.convertAppend(hm.get("GRADE"),"string", "")+",                                    \n");                                        
				sqlQuery.append("                    NG_CODE                     = "+StringUtil.convertAppend(hm.get("NG_CODE"),"string", "")+",                                  \n");                                      
				sqlQuery.append("                    TICKNESS_STANDARD           = "+StringUtil.convertAppend(hm.get("TICKNESS_STANDARD"),"int", "")+",                        \n");                               
				sqlQuery.append("                    CREATION_DATE               = SYSDATE,                           \n");                                                                                            
				sqlQuery.append("                    CREATED_BY                  = '-1',                              \n");                                                                                               
				sqlQuery.append("                    LAST_UPDATE_DATE            = SYSDATE,                        \n");                                                                                            
				sqlQuery.append("                    LAST_UPDATED_BY             = '-1',                         \n");                                                                                               
				sqlQuery.append("                    LAST_UPDATE_LOGIN           = '-1',                       \n");
				sqlQuery.append("                    TRANSFER_STATUS             = null,                       \n");
				sqlQuery.append("                    OPERATOR                    = "+StringUtil.convertAppend(hm.get("OPERATOR"),"string", "")+",                                 \n");                                     
				sqlQuery.append("                    ORDER_DELIVERY              = "+StringUtil.convertAppend(hm.get("ORDER_DELIVERY"),"string", "")+",                           \n");                               
				sqlQuery.append("                    OK_SURFACE                  = "+StringUtil.convertAppend(hm.get("OK_SURFACE"),"string", "")+",                               \n");                                   
				sqlQuery.append("                    OK_GRADE                    = "+StringUtil.convertAppend(hm.get("OK_GRADE"),"string", "")+",                                 \n");                                     
				sqlQuery.append("                    DELAY_DESC                  = "+StringUtil.convertAppend(hm.get("DELAY_DESC"),"string", "")+",                               \n");                                   
				sqlQuery.append("                    PRESENCE                    = "+StringUtil.convertAppend(hm.get("PRESENCE"),"string", "")+",                                 \n");                                     
				sqlQuery.append("                    MILLEDGE_GBN                = "+StringUtil.convertAppend(hm.get("MILLEDGE_GBN"),"string", "")+",                             \n");                                 
				sqlQuery.append("                    IO_GBN                      = "+StringUtil.convertAppend(hm.get("IO_GBN"),"string", "")+",                                   \n");                                       
				sqlQuery.append("                    TEST_DESC                   = "+StringUtil.convertAppend(hm.get("TEST_DESC"),"string", "")+",                                \n");                                    
				sqlQuery.append("                    CROWN_OP                    = "+StringUtil.convertAppend(hm.get("CROWN_OP"),"int", "")+",                                 \n");                                        
				sqlQuery.append("                    CROWN_OC                    = "+StringUtil.convertAppend(hm.get("CROWN_OC"),"int", "")+",                                 \n");                                        
				sqlQuery.append("                    CROWN_CE                    = "+StringUtil.convertAppend(hm.get("CROWN_CE"),"int", "")+",                                 \n");                                        
				sqlQuery.append("                    CROWN_DC                    = "+StringUtil.convertAppend(hm.get("CROWN_DC"),"int", "")+",                                 \n");                                        
				sqlQuery.append("                    CROWN_DR                    = "+StringUtil.convertAppend(hm.get("CROWN_DR"),"int", "")+",                                 \n");                                        
				sqlQuery.append("                    SAMPLE_POSITION             = "+StringUtil.convertAppend(hm.get("SAMPLE_POSITION"),"string", "")+",                          \n");                              
				sqlQuery.append("                    SPECIAL_APPOINTMENT         = "+StringUtil.convertAppend(hm.get("SPECIAL_APPOINTMENT"),"string", "")+",                      \n");                          
				sqlQuery.append("                    USAGE_CODE                  = "+StringUtil.convertAppend(hm.get("USAGE_CODE"),"string", "")+",                               \n");                                   
				sqlQuery.append("                    USAGE_DESC                  = "+StringUtil.convertAppend(hm.get("USAGE_DESC"),"string", "")+",                               \n");                                   
				sqlQuery.append("                    USAGE_END_USER_CODE         = "+StringUtil.convertAppend(hm.get("USAGE_END_USER_CODE"),"string", "")+",                      \n");                          
				sqlQuery.append("                    ORDER_USAGE_CODE            = "+StringUtil.convertAppend(hm.get("ORDER_USAGE_CODE"),"string", "")+",                              \n");                        
				sqlQuery.append("                    ORDER_USAGE_DESC            = "+StringUtil.convertAppend(hm.get("ORDER_USAGE_DESC"),"string", "")+",                              \n");                        
				sqlQuery.append("                    ORDER_USAGE_END_USER_CODE   = "+StringUtil.convertAppend(hm.get("ORDER_USAGE_END_USER_CODE"),"string", "")+",               \n");                   
				sqlQuery.append("                    STEEL_CODE                  = "+StringUtil.convertAppend(hm.get("STEEL_CODE"),"string", "")+",                         \n");                                         
				sqlQuery.append("                    STEEL_DESC                  = "+StringUtil.convertAppend(hm.get("STEEL_DESC"),"string", "")+",                         \n");                                         
				sqlQuery.append("                    INVENTORY_ITEM_ID           = "+StringUtil.convertAppend(hm.get("INVENTORY_ITEM_ID"),"int", "")+",                \n");                                       
				sqlQuery.append("                    HEAT_POC_NUMBER             = "+StringUtil.convertAppend(hm.get("HEAT_POC_NUMBER"),"string", "")+",                               \n");                         
				sqlQuery.append("                    ITEM_THICKNESS              = "+StringUtil.convertAppend(hm.get("ITEM_THICKNESS"),"int", "")+",                               \n");                              
				sqlQuery.append("                    ITEM_WIDTH                  = "+StringUtil.convertAppend(hm.get("ITEM_WIDTH"),"int", "")+",                        \n");                                             
				sqlQuery.append("                    GOLD_DUST                   = "+StringUtil.convertAppend(hm.get("GOLD_DUST"),"string", "")+",                          \n");                                          
				//sqlQuery.append("                    NG_CODE_FIN               = "+StringUtil.convertAppend(hm.get("NG_CODE_FIN"),"string", "")+",                           \n");                    
				sqlQuery.append("                    CONFIRM_REQ_ID              = "+StringUtil.convertAppend(hm.get("CONFIRM_REQ_ID"),"int", "")+",                               \n");                              
				sqlQuery.append("                    CONFIRM_REQ_DATE            = "+StringUtil.convertAppend(hm.get("CONFIRM_REQ_DATE"),"string", "")+",                                \n");                      
				sqlQuery.append("                    CONFIRM_ID                  = "+StringUtil.convertAppend(hm.get("CONFIRM_ID"),"int", "")+",                              \n");                                       
				sqlQuery.append("                    CONFIRM_DATE                = "+StringUtil.convertAppend(hm.get("CONFIRM_DATE"),"string", "")+",                           \n");                                   
				sqlQuery.append("                    CONFIRM_FLAG                = "+StringUtil.convertAppend(hm.get("CONFIRM_FLAG"),"string", "")+",                         \n");                                     
				sqlQuery.append("                    REQ_MEMO                    = "+StringUtil.convertAppend(hm.get("REQ_MEMO"),"string", "")+",                               \n");                                       
				sqlQuery.append("                    CANCEL_DATE                 = "+StringUtil.convertAppend(hm.get("CANCEL_DATE"),"string", "")+",                             \n");                                   
				sqlQuery.append("                    CANCEL_DESC                 = "+StringUtil.convertAppend(hm.get("CANCEL_DESC"),"string", "")+",                             \n");                                   
				sqlQuery.append("                    HOOP_OK                     = "+StringUtil.convertAppend(hm.get("HOOP_OK"),"string", "")+",                                \n");
				sqlQuery.append("                    COIL_RANGE                  = "+StringUtil.convertAppend(hm.get("COIL_RANGE"),"string", "")+"                                \n");
				sqlQuery.append("      WHEN NOT MATCHED THEN                    \n");
				sqlQuery.append("           INSERT ( COLLECTION_ID,             \n");
				sqlQuery.append("                    ORGANIZATION_ID,           \n");               
				sqlQuery.append("                    POC_NUMBER,                \n");               
				sqlQuery.append("                    OPERATION_RESOURCE,        \n");               
				sqlQuery.append("                    INSPECTION_DATE,           \n");               
				sqlQuery.append("                    INSPECTOR,                 \n");               
				sqlQuery.append("                    WEIGHT,                    \n");               
				sqlQuery.append("                    TICKNESS,                  \n");               
				sqlQuery.append("                    WIDTH,                     \n");               
				sqlQuery.append("                    LENGTH,                    \n");               
				sqlQuery.append("                    SURFACE,                   \n");               
				sqlQuery.append("                    COATING,                   \n");               
				sqlQuery.append("                    OK_NG,                     \n");               
				sqlQuery.append("                    GRADE,                     \n");               
				sqlQuery.append("                    NG_CODE,                   \n");               
				sqlQuery.append("                    TICKNESS_STANDARD,         \n");               
				sqlQuery.append("                    CREATION_DATE,             \n");               
				sqlQuery.append("                    CREATED_BY,                \n");               
				sqlQuery.append("                    LAST_UPDATE_DATE,          \n");               
				sqlQuery.append("                    LAST_UPDATED_BY,           \n");               
				sqlQuery.append("                    LAST_UPDATE_LOGIN,         \n");
				sqlQuery.append("                    TRANSFER_STATUS,         \n");				
				sqlQuery.append("                    OPERATOR,                  \n");               
				sqlQuery.append("                    ORDER_DELIVERY,            \n");               
				sqlQuery.append("                    OK_SURFACE,                \n");               
				sqlQuery.append("                    OK_GRADE,                  \n");               
				sqlQuery.append("                    DELAY_DESC,                \n");               
				sqlQuery.append("                    PRESENCE,                  \n");               
				sqlQuery.append("                    MILLEDGE_GBN,              \n");               
				sqlQuery.append("                    IO_GBN,                    \n");               
				sqlQuery.append("                    TEST_DESC,                 \n");               
				sqlQuery.append("                    CROWN_OP,                  \n");               
				sqlQuery.append("                    CROWN_OC,                  \n");               
				sqlQuery.append("                    CROWN_CE,                  \n");               
				sqlQuery.append("                    CROWN_DC,                  \n");               
				sqlQuery.append("                    CROWN_DR,                  \n");               
				sqlQuery.append("                    SAMPLE_POSITION,           \n");               
				sqlQuery.append("                    SPECIAL_APPOINTMENT,       \n");               
				sqlQuery.append("                    USAGE_CODE,                \n");               
				sqlQuery.append("                    USAGE_DESC,                \n");               
				sqlQuery.append("                    USAGE_END_USER_CODE,       \n");               
				sqlQuery.append("                    ORDER_USAGE_CODE,          \n");               
				sqlQuery.append("                    ORDER_USAGE_DESC,          \n");               
				sqlQuery.append("                    ORDER_USAGE_END_USER_CODE, \n");               
				sqlQuery.append("                    STEEL_CODE,                \n");               
				sqlQuery.append("                    STEEL_DESC,                \n");               
				sqlQuery.append("                    INVENTORY_ITEM_ID,         \n");               
				sqlQuery.append("                    HEAT_POC_NUMBER,           \n");               
				sqlQuery.append("                    ITEM_THICKNESS,            \n");               
				sqlQuery.append("                    ITEM_WIDTH,                \n");               
				sqlQuery.append("                    GOLD_DUST,                 \n");               
				//sqlQuery.append("                    NG_CODE_FIN,             \n");                 
				sqlQuery.append("                    CONFIRM_REQ_ID,            \n");               
				sqlQuery.append("                    CONFIRM_REQ_DATE,          \n");               
				sqlQuery.append("                    CONFIRM_ID,                \n");               
				sqlQuery.append("                    CONFIRM_DATE,              \n");               
				sqlQuery.append("                    CONFIRM_FLAG,              \n");               
				sqlQuery.append("                    REQ_MEMO,                  \n");               
				sqlQuery.append("                    CANCEL_DATE,               \n");
				sqlQuery.append("                    CANCEL_DESC,               \n");
				sqlQuery.append("                    UFS_PAGE_ID,               \n");
				sqlQuery.append("                    UFS_CODE,               \n");
				sqlQuery.append("                    HOOP_OK,                   \n");
				sqlQuery.append("                    COIL_RANGE)                   \n");  
				sqlQuery.append("           VALUES (       \n"); 
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "")+",                            \n");                                                   
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORGANIZATION_ID"),"int", "")+",                          \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("POC_NUMBER"),"string", "")+",                               \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OPERATION_RESOURCE"),"string", "")+",                       \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INSPECTION_DATE").toString().replaceAll("/", ""),"date", "YYYY-MM-DD") +",                          \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INSPECTOR"),"string", "")+",                                \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WEIGHT"),"int", "")+",                                   \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TICKNESS"),"int", "")+",                                 \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WIDTH"),"int", "")+",                                    \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("LENGTH"),"int", "")+",                                   \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SURFACE"),"string", "")+",                                  \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COATING"),"string", "")+",                                  \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OK_NG"),"string", "")+",                                    \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("GRADE"),"string", "")+",                                    \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("NG_CODE"),"string", "")+",                                  \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TICKNESS_STANDARD"),"int", "")+",                        \n");                                           
				sqlQuery.append("   SYSDATE,                           \n");                                                                                                    
				sqlQuery.append("   '-1',                              \n");                                                                                                    
				sqlQuery.append("   SYSDATE,                        \n");                                                                                                       
				sqlQuery.append("   '-1',                         \n");                                                                                                         
				sqlQuery.append("   '-1',                       \n");
				sqlQuery.append("   null,                       \n");   
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OPERATOR"),"string", "")+",                                 \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_DELIVERY"),"string", "")+",                           \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OK_SURFACE"),"string", "")+",                               \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OK_GRADE"),"string", "")+",                                 \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("DELAY_DESC"),"string", "")+",                               \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("PRESENCE"),"string", "")+",                                 \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("MILLEDGE_GBN"),"string", "")+",                             \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("IO_GBN"),"string", "")+",                                   \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TEST_DESC"),"string", "")+",                                \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_OP"),"int", "")+",                                 \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_OC"),"int", "")+",                                 \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_CE"),"int", "")+",                                 \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_DC"),"int", "")+",                                 \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_DR"),"int", "")+",                                 \n");                                           
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SAMPLE_POSITION"),"string", "")+",                          \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SPECIAL_APPOINTMENT"),"string", "")+",                      \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("USAGE_CODE"),"string", "")+",                               \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("USAGE_DESC"),"string", "")+",                               \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("USAGE_END_USER_CODE"),"string", "")+",                      \n");                                        
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_USAGE_CODE"),"string", "")+",                              \n");                                   
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_USAGE_DESC"),"string", "")+",                              \n");                                   
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_USAGE_END_USER_CODE"),"string", "")+",                                   \n");                     
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("STEEL_CODE"),"string", "")+",                         \n");                                              
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("STEEL_DESC"),"string", "")+",                         \n");                                              
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INVENTORY_ITEM_ID"),"int", "")+",                \n");                                                   
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("HEAT_POC_NUMBER"),"string", "")+",                               \n");                                   
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ITEM_THICKNESS"),"int", "")+",                               \n");                                       
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ITEM_WIDTH"),"int", "")+",                        \n");                                                  
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("GOLD_DUST"),"string", "")+",                          \n");                                              
				//sqlQuery.append("   "+StringUtil.convertAppend(hm.get("NG_CODE_FIN"),"string", "")+",                           \n");                                         
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_REQ_ID"),"int", "")+",                               \n");                                       
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_REQ_DATE"),"string", "")+",                                \n");                                 
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_ID"),"int", "")+",                              \n");                                            
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_DATE"),"string", "")+",                           \n");                                          
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_FLAG"),"string", "")+",                         \n");                                            
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("REQ_MEMO"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CANCEL_DATE"),"string", "")+",                             \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CANCEL_DESC"),"string", "")+",                             \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SEQ_Q100"),"string", "")+",                             \n");                                         
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SEQ_T300"),"string", "")+",                             \n");                                         
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("HOOP_OK"),"string", "")+",                                \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COIL_RANGE"),"string", "")+"                                \n");
				                                                                                                                                                            
				sqlQuery.append("   )                                         \n");                                                                                             

				/*
				sqlQuery.append("   INSERT INTO HB_QM_LQC_COIL_TRANSFERS (    \n");
				//sqlQuery.append("   INSERT INTO BOL_QA_LQC_COIL_RESULTS (    \n");
				sqlQuery.append("   COLLECTION_ID,                            \n");
				sqlQuery.append("   ORGANIZATION_ID,                          \n");
				sqlQuery.append("   POC_NUMBER,                               \n");
				sqlQuery.append("   OPERATION_RESOURCE,                       \n");
				sqlQuery.append("   INSPECTION_DATE,                          \n");
				sqlQuery.append("   INSPECTOR,                                \n");
				sqlQuery.append("   WEIGHT,                                   \n");
				sqlQuery.append("   TICKNESS,                                 \n");
				sqlQuery.append("   WIDTH,                                    \n");
				sqlQuery.append("   LENGTH,                                   \n");
				sqlQuery.append("   SURFACE,                                  \n");
				sqlQuery.append("   COATING,                                  \n");
				sqlQuery.append("   OK_NG,                                    \n");
				sqlQuery.append("   GRADE,                                    \n");
				sqlQuery.append("   NG_CODE,                                  \n");
				sqlQuery.append("   TICKNESS_STANDARD,                        \n");
				sqlQuery.append("   CREATION_DATE,                            \n");
				sqlQuery.append("   CREATED_BY,                               \n");
				sqlQuery.append("   LAST_UPDATE_DATE,                         \n");
				sqlQuery.append("   LAST_UPDATED_BY,                          \n");
				sqlQuery.append("   LAST_UPDATE_LOGIN,                        \n");
				sqlQuery.append("   OPERATOR,                                 \n");
				sqlQuery.append("   ORDER_DELIVERY,                           \n");
				sqlQuery.append("   OK_SURFACE,                               \n");
				sqlQuery.append("   OK_GRADE,                                 \n");
				sqlQuery.append("   DELAY_DESC,                               \n");
				sqlQuery.append("   PRESENCE,                                 \n");
				sqlQuery.append("   MILLEDGE_GBN,                             \n");
				sqlQuery.append("   IO_GBN,                                   \n");
				sqlQuery.append("   TEST_DESC,                                \n");
				sqlQuery.append("   CROWN_OP,                                 \n");
				sqlQuery.append("   CROWN_OC,                                 \n");
				sqlQuery.append("   CROWN_CE,                                 \n");
				sqlQuery.append("   CROWN_DC,                                 \n");
				sqlQuery.append("   CROWN_DR,                                 \n");
				sqlQuery.append("   SAMPLE_POSITION,                          \n");
				sqlQuery.append("   SPECIAL_APPOINTMENT,                      \n");
				sqlQuery.append("   USAGE_CODE,                               \n");
				sqlQuery.append("   USAGE_DESC,                               \n");
				sqlQuery.append("   USAGE_END_USER_CODE,                      \n");
				sqlQuery.append("   ORDER_USAGE_CODE,                         \n");
				sqlQuery.append("   ORDER_USAGE_DESC,                         \n");
				sqlQuery.append("   ORDER_USAGE_END_USER_CODE,                \n");
				sqlQuery.append("   STEEL_CODE,                               \n");
				sqlQuery.append("   STEEL_DESC,                               \n");
				sqlQuery.append("   INVENTORY_ITEM_ID,                        \n");
				sqlQuery.append("   HEAT_POC_NUMBER,                          \n");
				sqlQuery.append("   ITEM_THICKNESS,                           \n");
				sqlQuery.append("   ITEM_WIDTH,                               \n");
				sqlQuery.append("   GOLD_DUST,                                \n");
				//sqlQuery.append("   NG_CODE_FIN,                              \n");
				sqlQuery.append("   CONFIRM_REQ_ID,                           \n");
				sqlQuery.append("   CONFIRM_REQ_DATE,                         \n");
				sqlQuery.append("   CONFIRM_ID,                               \n");
				sqlQuery.append("   CONFIRM_DATE,                             \n");
				sqlQuery.append("   CONFIRM_FLAG,                             \n");
				sqlQuery.append("   REQ_MEMO,                                 \n");
				sqlQuery.append("   CANCEL_DATE,                              \n");
				sqlQuery.append("   CANCEL_DESC,                              \n");
				sqlQuery.append("   HOOP_OK                                   \n");
				sqlQuery.append("   ) VALUES (                                \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COLLECTION_ID"),"int", "")+",                            \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORGANIZATION_ID"),"int", "")+",                          \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("POC_NUMBER"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OPERATION_RESOURCE"),"string", "")+",                       \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INSPECTION_DATE").toString().replaceAll("/", ""),"date", "YYYY-MM-DD") +",                          \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INSPECTOR"),"string", "")+",                                \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WEIGHT"),"int", "")+",                                   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TICKNESS"),"int", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("WIDTH"),"int", "")+",                                    \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("LENGTH"),"int", "")+",                                   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SURFACE"),"string", "")+",                                  \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("COATING"),"string", "")+",                                  \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OK_NG"),"string", "")+",                                    \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("GRADE"),"string", "")+",                                    \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("NG_CODE"),"string", "")+",                                  \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TICKNESS_STANDARD"),"int", "")+",                        \n");
				sqlQuery.append("   SYSDATE,                           \n");
				sqlQuery.append("   '-1',                              \n");
				sqlQuery.append("   SYSDATE,                        \n");
				sqlQuery.append("   '-1',                         \n");
				sqlQuery.append("   '-1',                       \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OPERATOR"),"string", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_DELIVERY"),"string", "")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OK_SURFACE"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("OK_GRADE"),"string", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("DELAY_DESC"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("PRESENCE"),"string", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("MILLEDGE_GBN"),"string", "")+",                             \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("IO_GBN"),"string", "")+",                                   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("TEST_DESC"),"string", "")+",                                \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_OP"),"int", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_OC"),"int", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_CE"),"int", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_DC"),"int", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CROWN_DR"),"int", "")+",                                 \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SAMPLE_POSITION"),"string", "")+",                          \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("SPECIAL_APPOINTMENT"),"string", "")+",                      \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("USAGE_CODE"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("USAGE_DESC"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("USAGE_END_USER_CODE"),"string", "")+",                      \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_USAGE_CODE"),"string", "")+",                              \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_USAGE_DESC"),"string", "")+",                              \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ORDER_USAGE_END_USER_CODE"),"string", "")+",                                   \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("STEEL_CODE"),"string", "")+",                         \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("STEEL_DESC"),"string", "")+",                         \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("INVENTORY_ITEM_ID"),"int", "")+",                \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("HEAT_POC_NUMBER"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ITEM_THICKNESS"),"int", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("ITEM_WIDTH"),"int", "")+",                        \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("GOLD_DUST"),"string", "")+",                          \n");
				//sqlQuery.append("   "+StringUtil.convertAppend(hm.get("NG_CODE_FIN"),"string", "")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_REQ_ID"),"int", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_REQ_DATE"),"string", "")+",                                \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_ID"),"int", "")+",                              \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_DATE"),"string", "")+",                           \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CONFIRM_FLAG"),"string", "")+",                         \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("REQ_MEMO"),"string", "")+",                               \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CANCEL_DATE"),"string", "")+",                             \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("CANCEL_DESC"),"string", "")+",                             \n");
				sqlQuery.append("   "+StringUtil.convertAppend(hm.get("HOOP_OK"),"string", "")+"                                \n");

				sqlQuery.append("   )                                         \n");
				*/
				//System.out.println("BOL_QA_LQC_COIL_RESULTS = "+sqlQuery);
				batchSQL[currentRowIndex] = new String(sqlQuery);
				
				currentRowIndex++;

			}
			//System.out.println(batchSQL.length);
			resultCnt = dbu.execBatchSQL("erp",batchSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCnt;
	}
	public int BOL_QA_ME_COIL(List<?> list1) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int resultCnt =-1;
		HashMap hm = new HashMap();
		try {
			String[] batchSQL = new String[list1.size()];

			int currentRowIndex = 0;
			for (int i = 0; i < list1.size(); i++) {
				hm = (HashMap) list1.get(i);
				StringBuffer sqlQuery = new StringBuffer();
				sqlQuery.append("   INSERT INTO BOL_QA_ME_COIL_RESULTS (    \n");
				sqlQuery.append("   POC_NUMBER,                              \n");
				sqlQuery.append("   INSPECTION_DATE,                         \n");
				sqlQuery.append("   ORGANIZATION_ID,                         \n");
				sqlQuery.append("   STEEL,                                   \n");
				sqlQuery.append("   SURFACE,                                 \n");
				sqlQuery.append("   THICKNESS,                               \n");
				sqlQuery.append("   WIDTH,                                   \n");
				sqlQuery.append("   GRADE,                                   \n");
				sqlQuery.append("   WEIGHT,                                  \n");
				sqlQuery.append("   INS_POSITION1,                           \n");
				sqlQuery.append("   INS_POSITION2,                           \n");
				sqlQuery.append("   INS_POSITION3,                           \n");
				sqlQuery.append("   INS_POSITION4,                           \n");
				sqlQuery.append("   INS_POSITION5,                           \n");
				sqlQuery.append("   INS_POSITION6,                           \n");
				sqlQuery.append("   INS_POSITION7,                           \n");
				sqlQuery.append("   INS_POSITION8,                           \n");
				sqlQuery.append("   INS_POSITION9,                           \n");
				sqlQuery.append("   INS_POSITION10,                          \n");
				sqlQuery.append("   INS_POSITION11,                          \n");
				sqlQuery.append("   INS_POSITION12,                          \n");
				sqlQuery.append("   INS_POSITION13,                          \n");
				sqlQuery.append("   INS_POSITION14,                          \n");
				sqlQuery.append("   INS_POSITION15,                          \n");
				sqlQuery.append("   INS_POSITION16,                          \n");
				sqlQuery.append("   INS_POSITION17,                          \n");
				sqlQuery.append("   INS_POSITION18,                          \n");
				sqlQuery.append("   INS_THICKNESS1,                          \n");
				sqlQuery.append("   INS_THICKNESS2,                          \n");
				sqlQuery.append("   INS_THICKNESS3,                          \n");
				sqlQuery.append("   INS_THICKNESS4,                          \n");
				sqlQuery.append("   INS_THICKNESS5,                          \n");
				sqlQuery.append("   INS_THICKNESS6,                          \n");
				sqlQuery.append("   INS_THICKNESS7,                          \n");
				sqlQuery.append("   INS_THICKNESS8,                          \n");
				sqlQuery.append("   INS_THICKNESS9,                          \n");
				sqlQuery.append("   INS_THICKNESS10,                         \n");
				sqlQuery.append("   INS_THICKNESS11,                         \n");
				sqlQuery.append("   INS_THICKNESS12,                         \n");
				sqlQuery.append("   INS_THICKNESS13,                         \n");
				sqlQuery.append("   INS_THICKNESS14,                         \n");
				sqlQuery.append("   INS_THICKNESS15,                         \n");
				sqlQuery.append("   INS_THICKNESS16,                         \n");
				sqlQuery.append("   INS_THICKNESS17,                         \n");
				sqlQuery.append("   INS_THICKNESS18,                         \n");
				sqlQuery.append("   INS_WIDTH1,                              \n");
				sqlQuery.append("   INS_WIDTH2,                              \n");
				sqlQuery.append("   INS_WIDTH3,                              \n");
				sqlQuery.append("   INS_WIDTH4,                              \n");
				sqlQuery.append("   INS_WIDTH5,                              \n");
				sqlQuery.append("   INS_WIDTH6,                              \n");
				sqlQuery.append("   INS_WIDTH7,                              \n");
				sqlQuery.append("   INS_WIDTH8,                              \n");
				sqlQuery.append("   INS_WIDTH9,                              \n");
				sqlQuery.append("   INS_WIDTH10,                             \n");
				sqlQuery.append("   INS_WIDTH11,                             \n");
				sqlQuery.append("   INS_WIDTH12,                             \n");
				sqlQuery.append("   INS_WIDTH13,                             \n");
				sqlQuery.append("   INS_WIDTH14,                             \n");
				sqlQuery.append("   INS_WIDTH15,                             \n");
				sqlQuery.append("   INS_WIDTH16,                             \n");
				sqlQuery.append("   INS_WIDTH17,                             \n");
				sqlQuery.append("   INS_WIDTH18,                             \n");
				sqlQuery.append("   D1,                                      \n");
				sqlQuery.append("   D2,                                      \n");
				sqlQuery.append("   D3,                                      \n");
				sqlQuery.append("   D4,                                      \n");
				sqlQuery.append("   D5,                                      \n");
				sqlQuery.append("   D6,                                      \n");
				sqlQuery.append("   D7,                                      \n");
				sqlQuery.append("   D8,                                      \n");
				sqlQuery.append("   D9,                                      \n");
				sqlQuery.append("   D10,                                     \n");
				sqlQuery.append("   D11,                                     \n");
				sqlQuery.append("   D12,                                     \n");
				sqlQuery.append("   D13,                                     \n");
				sqlQuery.append("   D14,                                     \n");
				/*				sqlQuery.append("   D15,                                     \n");
				sqlQuery.append("   D16,                                     \n");
				sqlQuery.append("   D17,                                     \n");
				sqlQuery.append("   D18,                                     \n");*/
				sqlQuery.append("   E1,                                      \n");
				sqlQuery.append("   E2,                                      \n");
				sqlQuery.append("   E3,                                      \n");
				sqlQuery.append("   E4,                                      \n");
				sqlQuery.append("   E5,                                      \n");
				sqlQuery.append("   E6,                                      \n");
				sqlQuery.append("   E7,                                      \n");
				sqlQuery.append("   E8,                                      \n");
				sqlQuery.append("   E9,                                      \n");
				sqlQuery.append("   E10,                                     \n");
				sqlQuery.append("   E11,                                     \n");
				sqlQuery.append("   E12,                                     \n");
				sqlQuery.append("   E13,                                     \n");
				sqlQuery.append("   E14,                                     \n");
				/*				sqlQuery.append("   E15,                                     \n");
				sqlQuery.append("   E16,                                     \n");
				sqlQuery.append("   E17,                                     \n");
				sqlQuery.append("   E18,                                     \n");*/
				sqlQuery.append("   F1,                                      \n");
				sqlQuery.append("   F2,                                      \n");
				sqlQuery.append("   F3,                                      \n");
				sqlQuery.append("   F4,                                      \n");
				sqlQuery.append("   F5,                                      \n");
				sqlQuery.append("   F6,                                      \n");
				sqlQuery.append("   F7,                                      \n");
				sqlQuery.append("   F8,                                      \n");
				sqlQuery.append("   F9,                                      \n");
				sqlQuery.append("   F10,                                     \n");
				sqlQuery.append("   F11,                                     \n");
				sqlQuery.append("   F12,                                     \n");
				sqlQuery.append("   F13,                                     \n");
				sqlQuery.append("   F14,                                     \n");
				/*				sqlQuery.append("   F15,                                     \n");
				sqlQuery.append("   F16,                                     \n");
				sqlQuery.append("   F17,                                     \n");
				sqlQuery.append("   F18,                                     \n");*/
				sqlQuery.append("   G1,                                      \n");
				sqlQuery.append("   G2,                                      \n");
				sqlQuery.append("   G3,                                      \n");
				sqlQuery.append("   G4,                                      \n");
				sqlQuery.append("   G5,                                      \n");
				sqlQuery.append("   G6,                                      \n");
				sqlQuery.append("   G7,                                      \n");
				sqlQuery.append("   G8,                                      \n");
				sqlQuery.append("   G9,                                      \n");
				sqlQuery.append("   G10,                                     \n");
				sqlQuery.append("   G11,                                     \n");
				sqlQuery.append("   G12,                                     \n");
				sqlQuery.append("   G13,                                     \n");
				sqlQuery.append("   G14,                                     \n");
				/*				sqlQuery.append("   G15,                                     \n");
				sqlQuery.append("   G16,                                     \n");
				sqlQuery.append("   G17,                                     \n");
				sqlQuery.append("   G18,                                     \n");*/
				sqlQuery.append("   H1,                                      \n");
				sqlQuery.append("   H2,                                      \n");
				sqlQuery.append("   H3,                                      \n");
				sqlQuery.append("   H4,                                      \n");
				sqlQuery.append("   H5,                                      \n");
				sqlQuery.append("   H6,                                      \n");
				sqlQuery.append("   H7,                                      \n");
				sqlQuery.append("   H8,                                      \n");
				sqlQuery.append("   H9,                                      \n");
				sqlQuery.append("   H10,                                     \n");
				sqlQuery.append("   H11,                                     \n");
				sqlQuery.append("   H12,                                     \n");
				sqlQuery.append("   H13,                                     \n");
				sqlQuery.append("   H14,                                     \n");
				/*				sqlQuery.append("   H15,                                     \n");
				sqlQuery.append("   H16,                                     \n");
				sqlQuery.append("   H17,                                     \n");
				sqlQuery.append("   H18,                                     \n");*/
				sqlQuery.append("   COILLING_RANGE,                          \n");
				sqlQuery.append("   COIL_LENGTH,                             \n");
				sqlQuery.append("   CERTIFIED_SURFACE,                       \n");
				sqlQuery.append("   ROLL_STOP,                               \n");
				//				sqlQuery.append("   ROLL_STOP1,                              \n");
				//				sqlQuery.append("   ROLL_STOP2,                              \n");
				//				sqlQuery.append("   ROLL_STOP3,                              \n");
				//				sqlQuery.append("   ROLL_STOP4,                              \n");
				//				sqlQuery.append("   ROLL_STOP5,                              \n");
				//				sqlQuery.append("   ROLL_STOP6,                              \n");
				//				sqlQuery.append("   ROLL_STOP7,                              \n");
				sqlQuery.append("   COILLING_INSIDE_TOP,                     \n");
				sqlQuery.append("   COILLING_INSIDE_END,                     \n");
				sqlQuery.append("   COILLING_OUTSIDE_TOP,                    \n");
				sqlQuery.append("   COILLING_OUTSIDE_END,                    \n");
				sqlQuery.append("   INVENTORY_ITEM_ID,                       \n");
				sqlQuery.append("   LAST_RESOURCE_CODE,                      \n");
				sqlQuery.append("   WRITER,                                  \n");
				sqlQuery.append("   ORDER_NUMBER,                            \n");
				sqlQuery.append("   LINE_NUMBER,                             \n");
				sqlQuery.append("   INS_POC_NUMBER,                          \n");
				sqlQuery.append("   INS_COLLECTION_ID,                       \n");
				sqlQuery.append("   HEAT_POC_NUMBER,                         \n");
				sqlQuery.append("   INSPECTOR,                               \n");
				sqlQuery.append("   COIL_CENTER,                             \n");
				sqlQuery.append("   CUSTOMER_NAME,                           \n");
				sqlQuery.append("   INSPECT_NAME,                            \n");
				sqlQuery.append("   ITEM_CODE,                               \n");
				sqlQuery.append("   COILLING_INSIDE_TOP_DESC,                \n");
				sqlQuery.append("   COILLING_INSIDE_END_DESC,                \n");
				sqlQuery.append("   COILLING_OUTSIDE_TOP_DESC,               \n");
				sqlQuery.append("   COILLING_OUTSIDE_END_DESC,               \n");
				sqlQuery.append("   PRT_CNT,                                 \n");
				sqlQuery.append("   CREATION_DATE,                           \n");
				sqlQuery.append("   CREATED_BY,                              \n");
				sqlQuery.append("   LAST_UPDATE_DATE,                        \n");
				sqlQuery.append("   LAST_UPDATED_BY,                         \n");
				sqlQuery.append("   LAST_UPDATE_LOGIN,                       \n");
				sqlQuery.append("   STEEL_CODE,                              \n");
				sqlQuery.append("   CUSTOMER_TAX_NO,                         \n");
				sqlQuery.append("   COIL_CENTER_TAX_NO,                      \n");
				sqlQuery.append("   TRANS_FLAG                               \n");
				sqlQuery.append("   ) VALUES (                               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("POC_NUMBER"),"stirng", "")+",                              \n");
				sqlQuery.append("   "+  StringUtil.convertAppend(hm.get("INSPECTION_DATE").toString().replaceAll("/", ""),"date", "YYYY-MM-DD") +",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ORGANIZATION_ID"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("STEEL"),"string", "")+",                                   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("SURFACE"),"string", "")+",                                 \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("THICKNESS"),"int", "")+",                               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("WIDTH"),"int", "")+",                                   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("GRADE"),"string", "")+",                                   \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("WEIGHT"),"int", "")+",                                  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION1"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION2"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION3"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION4"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION5"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION6"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION7"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION8"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION9"),"int", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION10"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION11"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION12"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION13"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION14"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION15"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION16"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION17"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POSITION18"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS1"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS2"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS3"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS4"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS5"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS6"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS7"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS8"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS9"),"int", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS10"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS11"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS12"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS13"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS14"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS15"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS16"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS17"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_THICKNESS18"),"int", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH1"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH2"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH3"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH4"),"int", "")+",                              \n");
				sqlQuery.append("  "+ StringUtil.convertAppend(hm.get("INS_WIDTH5"),"int", "")+",                              \n");
				sqlQuery.append("  "+ StringUtil.convertAppend(hm.get("INS_WIDTH6"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH7"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH8"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH9"),"int", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH10"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH11"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH12"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH13"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH14"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH15"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH16"),"int", "")+",                             \n");
				sqlQuery.append("  "+ StringUtil.convertAppend(hm.get(" INS_WIDTH17"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_WIDTH18"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D1"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D2"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D3"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D4"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D5"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D6"),"string", "")+",                                      \n");
				sqlQuery.append("  "+ StringUtil.convertAppend(hm.get(" D7"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D8"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D9"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D10"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D11"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D12"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D13"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D14"),"string", "")+",                                     \n");
				/*				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D15"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D16"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D17"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("D18"),"string", "")+",                                     \n");*/
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E1"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E2"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E3"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E4"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E5"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E6"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E7"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E8"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E9"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E10"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E11"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E12"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E13"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E14"),"string", "")+",                                     \n");
				/*				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E15"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E16"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E17"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("E18"),"string", "")+",                                     \n");*/
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F1"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F2"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F3"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F4"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F5"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F6"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F7"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F8"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F9"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F10"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F11"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F12"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F13"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F14"),"string", "")+",                                     \n");
				/*				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F15"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F16"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F17"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("F18"),"string", "")+",                                     \n");*/
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G1"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G2"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G3"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G4"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G5"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G6"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G7"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G8"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G9"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G10"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G11"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G12"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G13"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G14"),"string", "")+",                                     \n");
				/*				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G15"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G16"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G17"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("G18"),"string", "")+",                                     \n");*/
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H1"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H2"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H3"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H4"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H5"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H6"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H7"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H8"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H9"),"string", "")+",                                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H10"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H11"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H12"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H13"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H14"),"string", "")+",                                     \n");
				/*				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H15"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H16"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H17"),"string", "")+",                                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("H18"),"string", "")+",                                     \n");*/
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_RANGE"),"string", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COIL_LENGTH"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("CERTIFIED_SURFACE"),"string", "")+",                       \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP"),"string", "")+",                               \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP1"),"string", "")+",                              \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP2"),"string", "")+",                              \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP3"),"string", "")+",                              \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP4"),"string", "")+",                              \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP5"),"string", "")+",                              \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP6"),"string", "")+",                              \n");
				//				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ROLL_STOP7"),"string", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_INSIDE_TOP"),"string", "")+",                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_INSIDE_END"),"string", "")+",                     \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_OUTSIDE_TOP"),"string", "")+",                    \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_OUTSIDE_END"),"string", "")+",                    \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INVENTORY_ITEM_ID"),"int", "")+",                       \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("LAST_RESOURCE_CODE"),"string", "")+",                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("WRITER"),"string", "")+",                                  \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ORDER_NUMBER"),"int", "")+",                            \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("LINE_NUMBER"),"int", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_POC_NUMBER"),"string", "")+",                          \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INS_COLLECTION_ID"),"int", "")+",                       \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("HEAT_POC_NUMBER"),"string", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INSPECTOR"),"string", "")+",                               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COIL_CENTER"),"string", "")+",                             \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("CUSTOMER_NAME"),"string", "")+",                           \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("INSPECT_NAME"),"string", "")+",                            \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("ITEM_CODE"),"string", "")+",                               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_INSIDE_TOP_DESC"),"string", "")+",                \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_INSIDE_END_DESC"),"string", "")+",                \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_OUTSIDE_TOP_DESC"),"string", "")+",               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COILLING_OUTSIDE_END_DESC"),"string", "")+",               \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("PRT_CNT"),"int", "")+",                                 \n");
				sqlQuery.append("   SYSDATE,                           \n");
				sqlQuery.append("   '-1',                              \n");
				sqlQuery.append("   SYSDATE,                        \n");
				sqlQuery.append("   '-1',                         \n");
				sqlQuery.append("   '-1',                       \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("STEEL_CODE"),"string", "")+",                              \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("CUSTOMER_TAX_NO"),"string", "")+",                         \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("COIL_CENTER_TAX_NO"),"string", "")+",                      \n");
				sqlQuery.append("   "+ StringUtil.convertAppend(hm.get("TRANS_FLAG"),"string", "")+"                              \n");
				sqlQuery.append("   )                                        \n");
				//System.out.println("BOL_QA_ME_COIL_RESULTS = "+sqlQuery);
				batchSQL[currentRowIndex] = new String(sqlQuery);
				currentRowIndex++;

			}
			resultCnt = dbu.execBatchSQL("erp",batchSQL);
		} catch (Exception e) {
			e.printStackTrace();
			resultCnt =-1;
		}
		return resultCnt;
	}

}
