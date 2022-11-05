package com.optidpp.podservice.pod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.optidpp.common.db.DBUtil;
import com.optidpp.podservice.db.dto.CommonCollect;
import com.optidpp.qzfc.RFCInterFace;
import com.optidpp.zrfc.common.ConfigValue;

public class PODCreate {
	private static final Logger logger = Logger.getLogger(PODCreate.class.getName());
	private final int HANGUL_UNICODE_START = 0xAC00;
	private final int HANGUL_UNICODE_END = 0xD7AF;
	private DBUtil dbu = null;

	public void copyFile(String source, String target) throws IOException {
		FileChannel inChannel = new FileInputStream(source).getChannel();
		FileChannel outChannel = new FileOutputStream(target).getChannel();
		try {
			// magic number for Windows, 64Mb - 32Kb
			int maxCount = (64 * 1024 * 1024) - (32 * 1024);
			long size = inChannel.size();
			long position = 0;
			while (position < size) {
				position += inChannel.transferTo(position, maxCount, outChannel);
			}
			logger.debug("파일복사 성공["+source+"]->["+target+"]");
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}

	}

	public void moveFile(String source, String target) throws IOException {
		FileChannel inChannel = new FileInputStream(source).getChannel();
		FileChannel outChannel = new FileOutputStream(target).getChannel();
		try {
			// magic number for Windows, 64Mb - 32Kb
			int maxCount = (64 * 1024 * 1024) - (32 * 1024);
			long size = inChannel.size();
			long position = 0;
			while (position < size) {
				position += inChannel.transferTo(position, maxCount, outChannel);
			}

			File f = new File(source);
			if(f.isFile()) f.deleteOnExit();

			logger.debug("파일이동 성공["+source+"]->["+target+"]");
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}

	}
	/**
	 * @param in      - 원본 파일명
	 * @param out     - 썸네일 파일명
	 * @param width   - 썸네일 파일 가로 크기
	 * @param height  - 썸네일 파일 세로 크기
	 * @param quality - 썸네일 품질 (1 ~ 100)
	 */
	public boolean createThumbnail(String in, String out, int width, int height, int quality){
		if (quality < 0 || quality > 100) {
			quality = 100;
		}

		File loadFile = new File(in);
		File saveFile = new File(out);

		ArrayList command = new ArrayList(10);

		// ImageMagick 이 설치된 폴더 설정
		command.add(ConfigValue.imageMagicKPath);
		// 기본 설정
		command.add("-depth");
		command.add(""+"4");
		command.add("-geometry");
		command.add(width + "x" + height);
		command.add("-quality");
		command.add("" + quality);

		command.add(loadFile.getAbsolutePath());
		command.add(saveFile.getAbsolutePath());

		logger.debug(command);

		return exec((String[])command.toArray(new String[1]));
	}
	private boolean exec(String[] command) {
		Process proc;

		try {
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			logger.error("IOException while trying to execute " + command);
			return false;
		}

		int exitStatus;

		while (true) {
			try {
				exitStatus = proc.waitFor();
				break;
			} catch (java.lang.InterruptedException e) {
				logger.error("Interrupted: Ignoring and waiting");
			}
		}

		if (exitStatus != 0) {
			logger.error("Error executing command: " + exitStatus);
		}

		return (exitStatus == 0);
	}

	//Initialize global variables
	public PODCreate() {
		logger.debug("PODCreate init...");
		ConfigValue config = new ConfigValue(PODCreate.class.getName());
		//config.wasPoolUseYn = "N";
		try {
			dbu = new DBUtil();
		} catch(Exception ex){
			logger.error(ex.toString());
		}

	}

	public HashMap createXmlFile(List list, Map<?, ?> datamap, Map<String, Object> headerMap) {
		// TODO Auto-generated method stub
		String xmlName="";
		String psName="";
		String importXmlFilePath = "";
		HashMap resultMap = new HashMap();
		try {

			Element FormData = new Element("FormData");
			FormData.setAttribute("id", headerMap.get("FORM_ID").toString());
			FormData.setAttribute("systemid", "QICS");
			FormData.setAttribute("printer", "PS File (Laser)");
			int pageIndex = 0;
			String tmpPageOrder = "";
			HashMap<String, String> map 		= null;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				map = (HashMap<String, String>) list.get(i);
				if(!tmpPageOrder.equals(map.get("PAGE_ORDER"))) {
					tmpPageOrder = map.get("PAGE_ORDER");
					pageIndex++;

					Element newPage = new Element("Page");
					newPage.setAttribute("order", ""+map.get("PAGE_ORDER"));
					FormData.addContent(newPage);

				}

				Element Page = (Element)FormData.getChildren("Page").get((pageIndex-1));
				Element Field = new Element("Field");
				Field.setAttribute("name", map.get("FIELD_NAME"));

				String erpCname = map.get("ERP_CNAME");
				if(erpCname !=null && !"".equals(erpCname)){
					String fieldName =  map.get("FIELD_NAME");
					//	System.out.println("FIELD_NAME = "+ fieldName);
					if(datamap.get(erpCname)!=null && !"".equals(datamap.get(erpCname))){
						if("ORDER_NUMBER".equals(erpCname) &&datamap.containsKey("ORDER_NUMBER") && datamap.containsKey("LINE_NUMBER")){
							Field.setAttribute("datatype", "text");
							if("SD9".equals(datamap.get("ORDER_NUMBER")) || "DZ9".equals(datamap.get("ORDER_NUMBER"))  || "EZ9".equals(datamap.get("ORDER_NUMBER"))  || "HOOP".equals(datamap.get("ORDER_NUMBER"))  ){
								Field.setContent(new CDATA(datamap.get("ORDER_NUMBER").toString()));
							}else{
								Field.setContent(new CDATA(datamap.get("ORDER_NUMBER")+"-"+datamap.get("LINE_NUMBER")));
							}

							Page.addContent(Field);
						}else{
							Field.setAttribute("datatype", "text");
							Field.setContent(new CDATA((String) datamap.get(erpCname)));
							Page.addContent(Field);
						}

					} else {
						if(headerMap.containsKey(fieldName)){
							Field.setAttribute("datatype", "text");
							Field.setContent(new CDATA((String) headerMap.get(fieldName)));
							Page.addContent(Field);
						}else {

						}
					}
				} else {
					String fieldName =  map.get("FIELD_NAME");
					//	System.out.println("FIELD_NAME = "+ fieldName);
					if(headerMap.containsKey(fieldName)){
						//System.out.println("HEADER_NAME = "+ fieldName);
						if("C_SEQ".equals(fieldName)) {
							Field.setAttribute("datatype", "text");
							Field.setAttribute("db", "Y");
						}
						if(fieldName.indexOf("C_STAMP") >-1){
							if(fieldName.indexOf("POD") > -1){
								Field.setAttribute("datatype", "text");
							}else{
								Field.setAttribute("datatype", "image");
							}
						}else{
							Field.setAttribute("datatype", "text");
						}
						Field.setContent(new CDATA((String) headerMap.get(fieldName)));
						Page.addContent(Field);
					}
				}




			}
			Document doc = new Document(FormData);
			XMLOutputter xmlOut = new XMLOutputter(); // 출력객체
			Format format = Format.getPrettyFormat(); // XML포맷
			format.setEncoding("UTF-8");

			xmlName = headerMap.get("FORM_ID").toString() + "_" +headerMap.get("C_SEQ").toString()  +".xml";
			psName = headerMap.get("FORM_ID").toString() + "_" +headerMap.get("C_SEQ").toString() +".ps";
			String xmlTempFile = ConfigValue.importTempPath + File.separatorChar + xmlName;
			String xmlOrginFile = ConfigValue.importXMLPath + File.separatorChar + xmlName;
			//System.out.println("xmlFileName = " + xmlTempFile);
			xmlOut.setFormat(format);

			FileOutputStream	f = new FileOutputStream(xmlTempFile);
			OutputStreamWriter writer = new OutputStreamWriter(f, "utf-8");
			xmlOut.output(doc, writer);
			writer.close();
			//System.out.println("파일이동 "+isMoved+" ["+xmlTempFile+"]->["+xmlOrginFile+"]");
			//moveFile(xmlTempFile,xmlOrginFile);
			resultMap.put("xmlTempFile",xmlTempFile);
			resultMap.put("xmlOrginFile",xmlOrginFile );
			resultMap.put("RESULT","Y" );
			resultMap.put("PS_NAME",psName );
			resultMap.put("FORM_ID",headerMap.get("FORM_ID").toString()  );
		} catch (Exception e) {
			resultMap.put("xmlTempFile","");
			resultMap.put("xmlOrginFile","" );
			resultMap.put("RESULT","N" );
			resultMap.put("PS_NAME","");
			resultMap.put("FORM_ID","" );
			e.printStackTrace();
		}
		return resultMap;
	}
	public static void main(String[] args) {
		ConfigValue config = new ConfigValue(PODCreate.class.getName());
		Map<String, Object> paramMap=new HashMap<String, Object>();
		PODCreate pod= new PODCreate();
		RFCInterFace app = new RFCInterFace();
		HashMap resultMap = new HashMap();
		Map<?,?> ifMap = app.commonInfo(paramMap);
		Map<?,?> masterMap=pod.getPadMaster(paramMap);
		List list = new ArrayList();
		list=pod.getPadDynamic(paramMap);




		Map<String, Object> headerMap =(Map<String, Object>) masterMap;
		headerMap.put("FORM_ID","FORM_QICS_A_R01");
		headerMap.put("FORM_CODE","FORM_QICS_A");
		headerMap.put("C_FRM_ID","COIL 검사결과표"); //시스템 양식지명
		headerMap.put("C_SEQ","1"); //시스템 양식지명
		headerMap.put("C_MAIN_PRC","BA1"); //공정명
		headerMap.put("C_POC_NO01_POD","C12ZT013"); //POC No.
		headerMap.put("C_POC_NO02_POD","0000"); //POC No.
		headerMap.put("C_POC_NO03_POD",""); //POC No.
		headerMap.put("C_CHK_DT","20150122"); //검사일자
		headerMap.put("C_STAMP01","QICS_STAMP_BASE.png"); //형상약도 IMAGE
		headerMap.put("C_STAMP01_POD","현대자동차"); //형상약도 TEXT

		resultMap= pod.createXmlFile(list,ifMap,headerMap);
	}

	private List getPadDynamic(Map<String, Object> paramMap) {
		List list = new ArrayList();
		// TODO Auto-generated method stub
		CommonCollect collect = getPadDynamic("pool1",paramMap);
		int idx = 0;
		while (collect.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0;i<collect.getColumnCount();i++) {
				map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
			}
			list.add(idx, map);
		}
		return list;
	}

	private CommonCollect getPadDynamic(String poolName,
			Map<String, Object> paramMap) {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT * FROM TDPP205                                                           \n");
		sqlQuery.append("WHERE FORM_SEQ = (SELECT MAX(FORM_SEQ) FROM TDPP201 WHERE FORM_CODE='FORM_QICS_A')\n");
		sqlQuery.append("    AND FIELD_DYNAMIC='true'                                                   \n");


		return dbu.getTableData(sqlQuery.toString(), poolName);
	}

	public Map<?, ?> getPadMaster(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		CommonCollect collect = getMasterInfo("pool1",paramMap);
		Map<String, Object> map = new HashMap<String, Object>();
		while (collect.next()) {
			for(int i=0;i<collect.getColumnCount();i++) {
				map.put(collect.getColumnName((i+1)),collect.getString(collect.getColumnName((i+1))));
			}
		}
		return map;
	}

	private CommonCollect getMasterInfo(String poolName,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT * FROM TDPP201                                         \n");
		sqlQuery.append(" WHERE FORM_SEQ = (SELECT MAX(FORM_SEQ) FROM TDPP201 WHERE FORM_CODE='FORM_QICS_A') \n");
		return dbu.getTableData(sqlQuery.toString(), poolName);
	}

	public Map<String, Object> createXmlFile2(List list1, HashMap dataMap,
			Map<String, Object> headerMap) {
		// TODO Auto-generated method stub
		String xmlName="";
		String psName="";
		String importXmlFilePath = "";
		HashMap resultMap = new HashMap();
		try {

			Element FormData = new Element("FormData");
			FormData.setAttribute("id", headerMap.get("FORM_ID").toString());
			FormData.setAttribute("systemid", "QICS");
			FormData.setAttribute("printer", "PS File (Laser)");
			int pageIndex = 0;
			String tmpPageOrder = "";
			HashMap<String, String> map 		= null;
			for (int i = 0; i < list1.size(); i++) {
				map = new HashMap<String, String>();
				map = (HashMap<String, String>) list1.get(i);
				if(!tmpPageOrder.equals(map.get("PAGE_ORDER"))) {
					tmpPageOrder = map.get("PAGE_ORDER");
					pageIndex++;

					Element newPage = new Element("Page");
					newPage.setAttribute("order", ""+map.get("PAGE_ORDER"));
					FormData.addContent(newPage);

				}

				Element Page = (Element)FormData.getChildren("Page").get((pageIndex-1));
				Element Field = new Element("Field");
				Field.setAttribute("name", map.get("FIELD_NAME"));
				//Field.setAttribute("datatype", "text");

				String fieldName =  map.get("FIELD_NAME");
				//	System.out.println("FIELD_NAME = "+ fieldName);
				if(headerMap.containsKey(fieldName)){
					if("C_SEQ".equals(fieldName)) {
						Field.setAttribute("datatype", "text");
						Field.setAttribute("db", "Y");
					}
					if(fieldName.indexOf("C_STAMP") >-1){
						if(fieldName.indexOf("POD") > -1){
							Field.setAttribute("datatype", "image");
						}else{
							Field.setAttribute("datatype", "text");
						}
					}
					//System.out.println("HEADER_NAME = "+ fieldName);
					Field.setContent(new CDATA((String) headerMap.get(fieldName)));
					Page.addContent(Field);
				}else{
					if(dataMap.containsKey(fieldName)){
						//System.out.println("WRITE_NAME = "+ fieldName);
						Field.setAttribute("datatype", "text");
						Field.setContent(new CDATA((String) dataMap.get(fieldName)));
						Page.addContent(Field);
					}


				}

			}
			Document doc = new Document(FormData);
			XMLOutputter xmlOut = new XMLOutputter(); // 출력객체
			Format format = Format.getPrettyFormat(); // XML포맷
			format.setEncoding("UTF-8");

			xmlName = headerMap.get("FORM_ID").toString() + "_" +headerMap.get("C_SEQ").toString()  +".xml";
			psName = headerMap.get("FORM_ID").toString() + "_" +headerMap.get("C_SEQ").toString() +".ps";
			String xmlTempFile = ConfigValue.importTempPath + File.separatorChar + xmlName;
			String xmlOrginFile = ConfigValue.importXMLPath + File.separatorChar + xmlName;
			//System.out.println("xmlFileName = " + xmlTempFile);
			xmlOut.setFormat(format);

			FileOutputStream	f = new FileOutputStream(xmlTempFile);
			OutputStreamWriter writer = new OutputStreamWriter(f, "utf-8");
			xmlOut.output(doc, writer);
			writer.close();
			//moveFile(xmlTempFile,xmlOrginFile);
			resultMap.put("xmlTempFile",xmlTempFile );
			resultMap.put("xmlOrginFile",xmlOrginFile );
			resultMap.put("RESULT","Y" );
			resultMap.put("PS_NAME",psName );
			resultMap.put("FORM_ID",headerMap.get("FORM_ID").toString()  );
		} catch (Exception e) {
			resultMap.put("xmlTempFile","" );
			resultMap.put("xmlOrginFile","" );
			resultMap.put("RESULT","N" );
			resultMap.put("PS_NAME","");
			resultMap.put("FORM_ID","" );
			e.printStackTrace();
		}
		return resultMap;
	}


}
