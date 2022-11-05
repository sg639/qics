package com.optidpp.common.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.optidpp.common.logger.Log;
import com.optidpp.main.view.ViewService;
import com.optidpp.zrfc.common.ConfigValue;


/**
 * 공통모음
 *
 */
@Controller
public class ImageController {
	@Inject
	@Named("ImageService")
	private ImageService imageService;

	@Inject
	@Named("ViewService")
	private ViewService viewService;
	/**
	 * Image 출력 test
	 *
	 * @param session
	 * @param request
	 * @param paramMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/ImageBG.do")
	public void getImageBG(HttpSession session,HttpServletRequest request,
			HttpServletResponse response,@RequestParam("FORM_ID") String FORM_ID,@RequestParam("BG_NAME")String BG_NAME) throws Exception {

		Log.DebugStart();
		///common/images/common/btn_skin.gif


		ConfigValue cfv = new ConfigValue();
		String BG_PATH = cfv.padRootPath;
		String fullPath = BG_PATH+File.separator+FORM_ID+File.separator+BG_NAME.replace(".png", "_Resize.png");
		File imgFile = new  File(fullPath)      ;

		// 파일 미 존재시
		if (!imgFile.isFile()){
			imgFile =  new  File("/common/images/common/blank.gif");
		}

		FileInputStream ifo = new FileInputStream(imgFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int readlength = 0;
		while( (readlength =ifo.read(buf)) != -1 )
		{
			baos.write(buf,0,readlength);
		}
		byte[] imgbuf = null;
		imgbuf = baos.toByteArray();
		baos.close();
		ifo.close();

		int length = imgbuf.length;
		OutputStream out = response.getOutputStream();
		out.write(imgbuf , 0, length);
		out.close();
	}

	/**
	 * Image 출력 test
	 *
	 * @param session
	 * @param request
	 * @param paramMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/ImagePS.do")
	public void getImagePrint(HttpSession session,HttpServletRequest request,
			HttpServletResponse response,@RequestParam("BG_PATH") String BG_PATH,@RequestParam("P_BG")String P_BG) throws Exception {

		Log.DebugStart();
		///common/images/common/btn_skin.gif

		//Log.Debug("path========================>"+ paramMap.get("path").toString());

		//ConfigValue cfv = new ConfigValue();
		String fullPath ="";
		//String BG_PATH = cfv.psBgRootPath;
		if(BG_PATH.indexOf("UFS_IMG") > -1){
			fullPath = BG_PATH+File.separator+P_BG;
		}else{
			fullPath = BG_PATH+File.separator+P_BG.replaceAll(".png", "_THUMBNAIL.png");
		}

		//Log.Debug("path========================>"+ fullPath);

		File imgFile = new  File(fullPath)      ;

		// 파일 미 존재시
		if (!imgFile.isFile()){
			imgFile =  new  File("/common/images/common/blank.gif");
		}

		FileInputStream ifo = new FileInputStream(imgFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int readlength = 0;
		while( (readlength =ifo.read(buf)) != -1 )
		{
			baos.write(buf,0,readlength);
		}
		byte[] imgbuf = null;
		imgbuf = baos.toByteArray();
		baos.close();
		ifo.close();

		int length = imgbuf.length;
		OutputStream out = response.getOutputStream();
		out.write(imgbuf , 0, length);
		out.close();
	}
	/**
	 * Image 출력 test
	 *
	 * @param session
	 * @param request
	 * @param paramMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/FilePS.do")
	public void getFilePrint(HttpSession session,HttpServletRequest request,
			HttpServletResponse response,@RequestParam("FORM_ID") String FORM_ID,@RequestParam("PS_NAME")String PS_NAME) throws Exception {

		Log.DebugStart();
		///common/images/common/btn_skin.gif

		//Log.Debug("path========================>"+ paramMap.get("path").toString());

		ConfigValue cfv = new ConfigValue();
		String PS_PATH = cfv.psRootPath;
		InputStream in = null;
		String fullPath = PS_PATH+File.separator+FORM_ID+File.separator+PS_NAME;
		//String fullPath = PS_PATH+File.separator+PS_NAME;
		//Log.Debug("path========================>"+ fullPath);

		File psFile = new  File(fullPath)      ;

		// 파일 미 존재시
		if (!psFile.isFile()){
			throw new Exception("<script>alert('파일이 존재하지 않습니다');</script>");
		}else{
			PS_NAME = new String(PS_NAME.getBytes("EUC-KR"), "8859_1");

			// internet explorer 버전 체크
			if( request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1 ) {
				//5.5 버전일때(다운로드 안됨)
				response.setHeader("Content-Type", "doesn/matter;");
				response.setHeader("Content-Disposition", "filename=" + PS_NAME + ";");
			} else {
				response.setHeader("Content-Type", "application/octet-stream; charset=EUC-KR");
				response.setHeader("Content-Disposition", "attachment; filename=" + PS_NAME + ";");
			}

			response.setHeader("Content-Length", psFile.length() + "");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try {
				bis = new BufferedInputStream(new FileInputStream(psFile));
				/*
				//jsp가 java로 변환되는 과정에서 outputstream을 이용하므로 jsp페이지에서 outputstream을 사용하면 오류가 발생한다.
				//java.lang.IllegalStateException: getOutputStream() has already been called for this response 오류 방지
				out.clear();
				out.flush();
				 */
				bos = new BufferedOutputStream(response.getOutputStream());

				int readBytes = 0;
				byte b[] = new byte[(int) psFile.length()];

				// 파일을 쓴다.
				while ((readBytes = bis.read(b)) != -1) {
					bos.write(b, 0, readBytes);
				}
			} catch (Exception e) {
				//System.out.println(e);
			} finally {
				response.flushBuffer();  // response 에 적용
				if (bos != null){
					try {
						bos.close();
					} catch (Exception e) {
						//System.out.println(e);
					}
				}
				if (bis != null){
					try {
						bis.close();
					} catch (Exception e) {
						//System.out.println(e);
					}
				}
			}
		}

	}

	/**
	 * Image 출력 test
	 *
	 * @param session
	 * @param request
	 * @param paramMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/QICSImageDownload.do")
	public void getImageDownload(HttpSession session,HttpServletRequest request,
			HttpServletResponse response,@RequestParam("SEQ_T300") String SEQ_T300) throws Exception {

		Log.DebugStart();
		///common/images/common/btn_skin.gif
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("SEQ_T300",SEQ_T300 );
		//Log.Debug("path========================>"+ paramMap.get("path").toString());
		List<?> list = imageService.getDownloadImageInfo(paramMap);
		String PS_PATH ="";
		String PS_NAME ="";
		String IMAGE_NAME="";
		String fullPath = "";
		if(list.size() > 0 ){
			HashMap hm = (HashMap) list.get(0);
			PS_PATH =hm.get("P_BG_PATH").toString();
			PS_NAME = hm.get("P_BG").toString();
			fullPath = PS_PATH+File.separator+PS_NAME;
			IMAGE_NAME = hm.get("IN_LINE").toString()+"_"+hm.get("WORK_DATE").toString().replaceAll("/", "")+"_"+hm.get("POC_NO").toString()+".png";
		}



		InputStream in = null;

		//String fullPath = PS_PATH+File.separator+PS_NAME;
		//Log.Debug("path========================>"+ fullPath);

		File psFile = new  File(fullPath)      ;

		// 파일 미 존재시
		if (!psFile.isFile()){
			throw new Exception("<script>alert('파일이 존재하지 않습니다');</script>");
		}else{
			IMAGE_NAME = new String(IMAGE_NAME.getBytes("EUC-KR"), "8859_1");

			// internet explorer 버전 체크
			if( request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1 ) {
				//5.5 버전일때(다운로드 안됨)
				response.setHeader("Content-Type", "doesn/matter;");
				response.setHeader("Content-Disposition", "filename=" + IMAGE_NAME + ";");
			} else {
				response.setHeader("Content-Type", "application/octet-stream; charset=EUC-KR");
				response.setHeader("Content-Disposition", "attachment; filename=" + IMAGE_NAME + ";");
			}

			response.setHeader("Content-Length", psFile.length() + "");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try {
				bis = new BufferedInputStream(new FileInputStream(psFile));
				/*
				//jsp가 java로 변환되는 과정에서 outputstream을 이용하므로 jsp페이지에서 outputstream을 사용하면 오류가 발생한다.
				//java.lang.IllegalStateException: getOutputStream() has already been called for this response 오류 방지
				out.clear();
				out.flush();
				 */
				bos = new BufferedOutputStream(response.getOutputStream());

				int readBytes = 0;
				byte b[] = new byte[(int) psFile.length()];

				// 파일을 쓴다.
				while ((readBytes = bis.read(b)) != -1) {
					bos.write(b, 0, readBytes);
				}
			} catch (Exception e) {
				//System.out.println(e);
			} finally {
				response.flushBuffer();  // response 에 적용
				if (bos != null){
					try {
						bos.close();
					} catch (Exception e) {
						//System.out.println(e);
					}
				}
				if (bis != null){
					try {
						bis.close();
					} catch (Exception e) {
						//System.out.println(e);
					}
				}
			}
		}

	}



}
