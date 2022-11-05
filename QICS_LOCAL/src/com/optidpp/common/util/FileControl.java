package com.optidpp.common.util;
/*
 *  FileControl
 *  date : 2008.09.05
 *  author : ibleader
 *
 *  method list
 *  1. String saveFile(HttpServletRequest req, String fileDir,[String realPath],[boolean useTempfileName])
 *  request 에 담긴 파일들을 지정한 폴더로 저장한다.
 *  return 되는 스트링은 IBUpload 에서 사용되는 저장 결과 스트링임. 무시해도 된다.
 *  2. void saveFileToRename(HttpServletRequest req, String fileDir)
 *  request 에 담긴 파일들을 다른 파일명으로 교체하여 저장한다. 여러개의 파일이 request에 담길수 있으므로
 *  내부에 변경될 파일명은 개발자가 직접 수정하여야 한다.
 *  3. File[] getFiles(HttpServletRequest req,String fileDir)
 *  request 에 담긴 파일을 fileDir 에 지정한 경로에 저장한 후 File[]를 리턴한다.
 *  4. boolean DeleteFile(String fileDir)
 *  파일에 대한 경로를 주면 그 파일을 제거하고 결과를 Boolean형태로 리턴한다.
 *  5.  void dbUpdateFile(java.io.File[] fn)
 *  File[]을 인자로 주면 이를 오라클 디비에 blob으로 저장한다.
 *  메서드 내에 쿼리나 디비에 관한 설정은 반드시 수정하여 사용하여야 한다.
 *  6.  void dbSearchFile(String seq,String fileDir)
 *  디비에 조회조건 seq를 지정하면 디비의 blob으로 부터 파일을 받아 fileDir에 파일을 저장한다.
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jazzlib.ZipEntry;
import net.sf.jazzlib.ZipOutputStream;

import com.optidpp.common.logger.Log;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;



public class FileControl {


	public static HashMap EtcData = new HashMap();

	public static String rpath = "";  //파일 저장 위치
	private static final int BUFFER_SIZE = 1024;
	private static String saveFileResult = "";


	/*
	 * 생성자!!!
	 */
	public FileControl(){
		super();
	}
	// rpath 저장될 파일 경로
	public FileControl(String rpath){
		this.rpath = rpath;
	}


	/*
	 * 파일의 저장,삭제,다운로드 업무를 수행
	 * method
	 * fileWork( req,  res, [useTFN]  )
	 * === param list ===
	 * 1. req   HttpServletRequest 객체
	 * 2. res   HttpServletResponse 객체
	 * 3. useTFN  boolean 임의의 이름으로 파일명 변경
	 */
	public String fileWork(HttpServletRequest req, HttpServletResponse res,HttpSession session)  throws Exception{
		return fileWork(req,res,session,false);
	}

	public String fileWork(HttpServletRequest req, HttpServletResponse res,HttpSession session, boolean useTempfileName) throws Exception{
		// TODO Auto-generated method stub
		String rtnString = "";
		//넘어온 파일을 그냥 저장한다.
		String contentType = req.getContentType();
		if (contentType != null && contentType.startsWith("multipart")) {
			int maxSize=50*1024*1024;
			try{
				//이미 파일이 설정한 폴더에 저장되었다.
				MultipartRequest mreq = null;
				if( !new File(rpath).exists() || !new File(rpath).exists()  ){
					throw new Exception("지정한 폴더가 존재하지 않습니다.");
				}
				Log.Debug("Path : "+ rpath);
				Log.Debug("MaxSize : "+ maxSize);
				mreq = new MultipartRequest(req,rpath,maxSize,"utf-8",new DefaultFileRenamePolicy());
				if(!mreq.getFileNames().hasMoreElements()){return "NOFILE";}
				String _last 			= req.getParameter("last");
				String _first 			= req.getParameter("first");
				String _realFileName 	= req.getParameter("file");
				String fname 			= (String)(mreq.getFileNames().nextElement());
				String _tempFileName 	= (mreq.getFile(fname)).getName();
				//					Log.Debug("_last:"+_last);
				//					Log.Debug("_first:"+_first);
				//					Log.Debug("_realFileName:"+_realFileName);
				//					Log.Debug("fname:"+fname);
				//					Log.Debug("_tempFileName:"+_tempFileName);
				if(!("True".equals(_last))){
					if("True".equals(_first)){
						Log.Debug("TRUE TRUE");
						//대용량 파일 처음 받음 (받은파일 앞에 언더바*3 을 붙인 이름로 변경한다.
						//Log.Debug("대용량 파일 처음 받음 (받은파일 앞에 언더바*3 을 붙인 이름로 변경한다.");
						File fl = new File(rpath+"/"+_tempFileName);
						File tempfl = new File(rpath+"/___"+_tempFileName);
						fl.renameTo(tempfl);
						fl.delete();
					}else{
						Log.Debug("TRUE FALSE");
						//대용량 파일의 중간임.
						//Log.Debug("대용량 파일의 중간임");
						AppendFile(rpath,_tempFileName,_tempFileName);
					}
				}else{
					Log.Debug("FALSE");
					//대용량 마지막 이거나 작은 사이즈의 파일
					//Log.Debug("대용량 마지막 이거나 작은 사이즈의 파일");
					if(new File(rpath+"/___"+_tempFileName).exists()){
						//마지막 조각을 붙인다.
						AppendFile(rpath,_tempFileName,_tempFileName);
						//파일명을 원래 파일명으로 바꾸자
						new File(rpath+"/___"+_tempFileName).renameTo(new File(rpath+"/"+_tempFileName));
						//파일을 누적했던 임시 파일을 지운다.
						new File(rpath+"/___"+_tempFileName).delete();
					}
					rtnString = rpath+"/"+_tempFileName;
					saveFileResult = "		<SERVER-DATA SERVERFILENAME='" + convertXmlFileName(_realFileName) + "' RESULT='Finished' ERRORMSG='' DEBUGMSG='' />\r\n";
					//Log.Debug("saveFileResult : "+saveFileResult);
					//실제 저장된 파일명
					String savedFile 	= rpath+"/"+_tempFileName;
					int sessionCnt 		= 0;
					if(session.getAttribute("downloadedfilecnt")==null){
						//Log.Debug("############## Session Init ##############");
						sessionInit(session);
					}
					//저장된 파일명을 고유한 이름으로 변경 후, 세션에 넣는다.
					if(useTempfileName){
						String idFileName = rpath+"/"+getTimeStemp();
						new File(savedFile).renameTo(new File(idFileName));
						ArrayList al = (ArrayList)session.getAttribute("FileList");
						HashMap mp = new HashMap();
						String file[] = idFileName.split("/");
						mp.put("realNm",_tempFileName);
						mp.put("saveNm", file[file.length-1]);
						mp.put("sPath", idFileName);
						mp.put("size", new File(idFileName).length());
						mp.put("contentType",contentType);
						al.add(mp);
						session.setAttribute("FileList",al);
					}else{
						//저장된 이름 그대로 session에 넣는다.
						ArrayList al = (ArrayList)session.getAttribute("FileList");
						HashMap mp = new HashMap();
						mp.put("pname", savedFile);
						mp.put("lname", savedFile);
						mp.put("size",new File(savedFile).length());
						al.add(mp);
						session.setAttribute("FileList",al);
					}
					Log.Debug("3");
					//공통 세션 작업
					sessionCnt = (Integer)session.getAttribute("downloadedfilecnt");
					sessionCnt ++;
					session.setAttribute("downloadedfilecnt",sessionCnt);
					Log.Debug("########## Downcnt : "+sessionCnt);
					Log.Debug("########## FileCnt : "+Integer.parseInt(req.getParameter("FileCnt")));
					if(sessionCnt >= Integer.parseInt(req.getParameter("FileCnt"))){
						session.setAttribute("FinalFinish", "true");//마지막 파일까지 모두 받았음.
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				saveFileResult = "		<SERVER-DATA SERVERFILENAME='' RESULT='Error' ERRORMSG='" + ex.getMessage() + "' DEBUGMSG='"+ex.getMessage()+"' />\r\n";
				rtnString = "error:"+ex.getMessage();
			}finally{ }

		}else{
			Log.Debug("Control Else");
			rtnString = "__IBSHEET__";
			if("DELETE".equals(req.getParameter("STATUS"))){
				//선택한 파일을 삭제한다.
				deleteFile(req);
				rtnString = "DELETE";
			}else{

				rtnString = "DOWNLOAD";
				//선택한 파일을 다운로드 한다.
				fileDownload(req,res);

			}
		}
		return rtnString;
	}




	/*
	 * request에 pfilename,lfilename으로 들어온 파일을 압축하여, 하나의 파일을 response 쪽으로 전송한다.
	 *
	 */
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{
		//System.out.println("DOWNLOAD!!!!");

		StringBuffer sb = new StringBuffer();
		ZipOutputStream zout = null;

		InputStream in = null;
		OutputStream outt = null;
		String filenameDisplay = "";
		String browserName = getBrowser(req);

		if ("Opera".equals(browserName)){
			res.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
		} else {
			res.setHeader("Content-Type", "application/octet-stream");
		}

		try {
			String[] pname = req.getParameterValues("sFileNm");
			String[] lname = req.getParameterValues("rFileNm");
			String[] filePath = req.getParameterValues("filePath");


			if(pname.length==1){

				String fPath = URLDecoder.decode(filePath[0], "UTF-8");
				String path = fPath.substring(0, fPath.lastIndexOf("/"));
				File file = new File(path, pname[0]);
				if(!file.exists()){
					throw new Exception("<script>alert('파일이 존재하지 않습니다');</script>");
				}

				in = new BufferedInputStream(new FileInputStream(file));

				String downfilename = lname[0];//
				String sEncodingFileName = getDecodedFilename(downfilename, browserName);

				res.setHeader("Content-Disposition", "attachment;filename=\"" + sEncodingFileName + "\"");

				/*				in = new BufferedInputStream(new FileInputStream(file));
//				String downfilename = new String(lname[0].getBytes("8859_1"),"UTF-8");
//				String sEncodingFileName = getEncodedFilename(downfilename, browserName);
				res.setHeader("Content-Disposition", "attachment;filename=\"" + lname[0] + "\"");
				 */
				if(req.getParameter("PREVIEW")!=null){

				}else if(req.getParameter("DOWNLOAD")!=null){
					//					res.setHeader("Content-Disposition", "attachment;filename=\"" + sEncodingFileName + "\"");
				}

				res.setContentLength((int)file.length());
				byte b[] = new byte[1024];
				int numRead = 0;
				outt = res.getOutputStream();
				while ((numRead = in.read(b)) != -1) {
					outt.write(b, 0, numRead);
				}
				outt.flush();
				outt.close();
			}else{
				String downfilename = req.getParameter("returnFileName");
				if("".equals(downfilename)){
					java.util.Date d = new java.util.Date();
					SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
					downfilename = df.format(d)+"_DownloadedFile";
				}

				//downfilename = new String(downfilename.getBytes("8859_1"),"UTF-8");
				res.setContentType("application/zip;charset=utf-8");
				String sEncodingFileName = getDecodedFilename(downfilename, browserName);

				res.setHeader("Content-Disposition", "attachment;filename=\"" + sEncodingFileName + ".zip\";");
				outt = res.getOutputStream();
				zout = new ZipOutputStream(outt);
				for(int i=0;i<pname.length;i++){
					//파일 엔코딩 변경
					pname[i] = new String(pname[i].getBytes("8859_1"),"UTF-8");
					lname[i] = new String(lname[i].getBytes("8859_1"),"UTF-8");
					filePath[i] = new String(filePath[i].getBytes("8859_1"),"UTF-8");

					String fPath = URLDecoder.decode(filePath[0], "UTF-8");
					zipEntry(pname[i],lname[i],filePath[i],zout);
				}
				zout.flush();
				zout.close();
			}
		}finally{
			try{
				if(zout!=null){
					zout.close();
				}
				if(outt!=null){
					outt.close();
				}
			}catch(Exception ex){ex.printStackTrace();}
		}
		//System.out.println("DownloadFinish!!");
	}
	/*
	 * 여러개의 파일을 하나의 Zip 파일에 넣는다.
	 */
	private static void zipEntry(String pname,String lname,String path,ZipOutputStream zos) throws Exception{

		BufferedInputStream bis = null;
		try{
			if(!(new File(path+"/"+pname).exists())){throw new Exception(lname+" 파일이 존재하지 않습니다.");}
			bis = new BufferedInputStream(new FileInputStream(new File(path+"/"+pname)));

			ZipEntry zentry = new ZipEntry(lname);
			zos.putNextEntry(zentry);

			byte[] buffer = new byte[BUFFER_SIZE];
			int cnt = 0;
			while((cnt=bis.read(buffer,0,BUFFER_SIZE))!= -1){
				zos.write(buffer,0,cnt);
			}
			zos.closeEntry();
		}finally{
			if(bis!=null){
				bis.close();
			}
		}
	}






	/*
	 * request안에 삭제할 파일을 찾아 지운다.
	 */
	public void deleteFile(HttpServletRequest req){
		String[] lfiles = req.getParameterValues("rFileNm");
		String[] pfiles = req.getParameterValues("sFileNm");
		String[] status = req.getParameterValues("sStatus");
		if(status != null){
			String deletefilename = "";
			for(int i=0;i<status.length;i++){
				//				if("D".equals(status[i])){
				if("".equals(pfiles[i])){
					deletefilename = lfiles[i];
				}else{
					deletefilename = pfiles[i];
				}
				Log.Debug("DELETE FILE : "+rpath+"/"+deletefilename);
				new File(rpath+"/"+deletefilename).delete();
				//				}
			}


		}
	}
	/*
	 * IBSheet 로 리턴할 XML 문자열을 만든다.
	 */
	public String getSheetReturnXml(){
		String rtnXml = "";
		rtnXml += "<SHEET>";

		//EtcData 넣기
		Iterator<String> it = EtcData.keySet().iterator();
		if(it.hasNext()){
			rtnXml += "	<ETC-DATA>\r\n";
			while(it.hasNext()){
				String key = it.next();
				rtnXml += "		<ETC KEY='"+key+"'><![CDATA["+EtcData.get(key)+"]]></ETC>\r\n";
			}
			rtnXml += "	</ETC-DATA>\r\n";
		}
		rtnXml += "	<RESULT CODE='1' MESSAGE=''>";
		rtnXml += "	</RESULT>";
		rtnXml += "</SHEET>";

		Log.Debug(rtnXml);
		return rtnXml;
	}

	//	public String getReturnXml(HttpSession session){
	//		return getReturnXml();
	//	}



	/*
	 * IBUpload로 리턴할 XML문자열을 만든다.
	 */
	public String getReturnXml(){

		String rtnXml = "";

		rtnXml += "<?xml version='1.0'?>\r\n";
		rtnXml += "<RESULT>\r\n";
		rtnXml += "	<RESULT-DATA>\r\n";
		rtnXml += saveFileResult;

		//EtcData 넣기
		Iterator<String> it = EtcData.keySet().iterator();
		if(it.hasNext()){
			rtnXml += "	<ETC-DATA>\r\n";
			while(it.hasNext()){
				String key = it.next();
				rtnXml += "		<ETC KEY='"+key+"'><![CDATA["+EtcData.get(key)+"]]></ETC>\r\n";
			}
			rtnXml += "	</ETC-DATA>\r\n";
		}
		rtnXml += "	</RESULT-DATA>\r\n";
		rtnXml += "</RESULT>\r\n";
		EtcData.clear();
		return rtnXml;
	}


	/*
	 * 파일을 append한다. (내부적으로만 사용됨)
	 */
	private void AppendFile(String fileDir,String realFileName, String tempFileName) throws IOException{
		//대용량 파일의 중간임.
		InputStream inBuffer = null;
		OutputStream outBuffer = null;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try{
			fin = new FileInputStream(fileDir+"/"+tempFileName); // 임시로 저장된 대용량 조각 블럭 파일
			fout = new FileOutputStream(fileDir+"/___"+realFileName,true); // 계속 누적되는 대용량 파일

			int bytesRead;
			byte[] buffer = new byte[4096]; //버퍼를 작게 잡으면 그만큼 성능이 저하됨.
			synchronized(fin)// in 및 out 객체를 동기화시킨다.
			{
				synchronized(fout)
				{
					while((bytesRead = fin.read(buffer)) >= 0)
					{
						fout.write(buffer, 0, bytesRead);
					}
				}
			}
		}catch(IOException iex){
			iex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(fin!=null)	fin.close();
			if(fout!=null)	fout.close();
			new File(fileDir+"/"+tempFileName).delete();
		}
	}





	/*
	 * ETC 데이터에 지정한 값을 추가한다.
	 */
	public void setEtcData(String key,String value){
		EtcData.put(key, value);
	}

	/*
	 * Session에 담긴 파일 정보를 ETCDATA에 포함 시킨다.
	 */
	public void setEtcData(HttpSession session){
		String jsonStr = "{\"data\":[";

		ArrayList al = (ArrayList)session.getAttribute("FileList");
		String tempstr = "";
		String pname ="";
		String lname = "";




		for(int i=0;i<al.size();i++){
			HashMap mp = (HashMap)al.get(i);
			pname = mp.get("pname")+"";
			lname = mp.get("lname") + "";
			pname = pname.substring(pname.lastIndexOf("/") +1);
			lname = lname.substring(lname.lastIndexOf("/") +1);
			jsonStr += "{\"pfilename\":\""+pname+"\"";
			jsonStr += ",\"filepath\":\"서버\"";
			jsonStr += ",\"filesize\":"+mp.get("size");
			jsonStr += ",\"lfilename\":\""+lname+"\"},";

		}
		jsonStr = jsonStr.substring(0,jsonStr.length()-1);
		jsonStr += "]}";
		EtcData.put("IBSHEET", jsonStr);
	}




	/*
	 * 임의의 문자열을 리턴한다.
	 */
	public String getTimeStemp(){
		String rtnStr = Math.random()+ "";
		rtnStr = rtnStr.substring(2);
		return rtnStr;
	}

	/*
	 * 최초 접근시 세션에 파일정보를 담을 Attribute를 생성 한다.
	 */
	public void sessionInit(HttpSession session){
		session.setAttribute("downloadedfilecnt",0);
		session.setAttribute("FinalFinish", "false");
		session.setAttribute("FileList",new ArrayList());
	}

	/*
	 *  세션에 등록된 논리,물리 파일명 파일 개수 등을 Log.Debug()로 출력한다.
	 *  그냥 확인 용도임.
	 */
	public void sessionCheck(HttpSession ss){
		String printStr = "\n+++++++++++++++++++++\n";
		printStr += "Total File Count : "+ss.getAttribute("downloadedfilecnt") + "\n";
		Log.Debug(ss.getAttribute("FileList") .toString());
		ArrayList al = (ArrayList)ss.getAttribute("FileList");
		for(int fi=0;fi<al.size();fi++){
			HashMap hm = (HashMap)al.get(fi);
			printStr += "Server File Name : "+ hm.get("saveNm") + "\nReal File Name : "+hm.get("realNm")+"\n";
		}
		printStr += "++++++++++++++++++++++";
		Log.Debug(printStr);

	}


	/*
	 * 모든 파일이 전송되고 난 후에 호출시
	 * 세션에 이번에 전송된 파일의 개수,이름 등에 대한 정보를 지운다.
	 */
	public void sessionClear(HttpSession ss){
		Log.Debug("SESSION CLEARED!!!");
		//		if("true".equals((String)ss.getAttribute("FinalFinish"))){
		ss.setAttribute("downloadedfilecnt",null);
		ss.setAttribute("FileList",null);
		ss.setAttribute("FinalFinish", "false");

		//		}
	}


	/*
	 * 사용자 피씨의 브라우져를 확인한다.
	 */
	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		} else if (header.indexOf("Safari") > -1) {
			return "Safari";
		}
		return "Firefox";
	}
	/*
	 * 브라우저에 따라 다르게 인코딩 한다.
	 */
	private String getEncodedFilename(String filename, String browser) throws Exception {
		String encodedFilename = "";

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", " ");
			//System.out.println("==="+encodedFilename);
		} else if (browser.equals("Firefox") || browser.equals("Safari") || browser.equals("Opera")) {
			encodedFilename = new String(filename.getBytes("UTF-8"), "8859_1");
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			encodedFilename = new String(filename.getBytes("UTF-8"), "8859_1");
		}

		return encodedFilename;
	}


	/*
	 * 브라우저에 따라 다르게 디코딩 한다.
	 */
	private String getDecodedFilename(String filename, String browser) throws Exception {
		String encodedFilename = "";

		if (browser.equals("Firefox") || browser.equals("Safari") || browser.equals("Opera")) {
			encodedFilename = URLDecoder.decode(filename, "8859_1");
		} else{
			encodedFilename = filename;
		}

		return encodedFilename;
	}
	/*
	 * Oracle BLOB 컬럼에 저장하는 유형 예시
	 * 수정해서 사용할 것.
	 */
	public void dbUpdateFile(java.io.File[] fn){
		/*table 구조
		seq , filename , clob
		 */
		String sql = "insert into upload_test values ((select count(seq)+1 from upload_test), ?, ?)";
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String  url ="jdbc:oracle:thin:@192.168.0.99:1521:hubdb";
			conn = DriverManager.getConnection(url,"ibupload","ibupload");
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=0;i<fn.length;i++){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				FileInputStream fis = new FileInputStream(fn[0]);
				byte[] buffer = new byte[512];
				int icount =0;
				while((icount = fis.read(buffer))>0){
					baos.write(buffer, 0, icount);
				}
				byte[] ibytes = baos.toByteArray();
				fis.close();
				baos.close();
				ps.setString(1, fn[i].getName());
				ps.setBytes(2, ibytes);
				ps.executeUpdate();
			}
			ps.close();
			conn.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	/*
	 * Oracle BLOB에 저장된 파일을 물리적 파일로 변환하는 방법
	 * 참고만 할 것
	 */
	public void dbSearchFile(String seq,String fileDir){
		String  url ="jdbc:oracle:thin:@192.168.0.99:1521:hubdb";
		String query = "select * from upload_test where seq = ?";
		Connection conn;
		// Select for update so we can also write data.
		Log.Debug("seq = "+seq);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url,"ibupload","ibupload");
			// Prepare the statement.
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, seq);
			// Execute.
			ResultSet rs = pstmt.executeQuery();

			// Output the data.
			rs.next();

			// Get the Blob contents.
			Blob bfile = rs.getBlob(3);
			InputStream is = rs.getBinaryStream(3);
			File nfn = new File(fileDir+rs.getString(2));
			java.io.FileOutputStream fos = new java.io.FileOutputStream(nfn);
			byte[] buffer = new byte[512];
			int icount =0;
			while((icount = is.read(buffer))>0){
				fos.write(buffer, 0, icount);
			}
			fos.close();
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * XML에서 지정한 특수 문자 &,< 등을 &amp;,&lt; 형태로 변환한다.
	 */
	public String convertXmlFileName(String orgName){
		return orgName.replaceAll("&","&amp;").replaceAll("\"", "&quot;").replaceAll("'","&apos;").replaceAll("<", "&lt;").replaceAll(">","&gt;");
	}

}

