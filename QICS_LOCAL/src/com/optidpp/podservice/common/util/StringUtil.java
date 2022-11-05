package com.optidpp.podservice.common.util;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class StringUtil {

	private static final Logger logger = Logger.getLogger(StringUtil.class.getName());
	private static final String ILLEGAL_EXP = "[:\\\\/%*?:|\"<>]";
	/**
	 * 데이타 트랜젝션 결과값 문자 만들기.
	 * @param result 수행결과 문자
	 * @param maxCnt 리턴값
	 * @return
	 */
	public static String returnValue(String result, String maxCnt){
		if(result.equals("OK")){
			return result+"_"+maxCnt;
		}else{
			return result;
		}
	}

	/**
	 * 데이타 트랜젝션 결과값 문자 만들기.
	 * @param result 수행결과 문자
	 * @param maxCnt 리턴값
	 * @return
	 */
	public static String returnValue(String result, String[] maxCnt){
		if(result.equals("OK")){
			for(int i = 0 ; i < maxCnt.length ; i++){
				result = result+"_"+maxCnt[i];
			}
			return result;
		}else{
			return result;
		}
	}

	/**
	 * 해당화일의 모든 내용을 string으로 읽어드린다.
	 *@param fname 파일명
	 *@return String
	 */
	public static String getFileToString(String fname)  throws Exception{
		try {
			RandomAccessFile fp = new RandomAccessFile(fname , "r");
			long size = fp.length();
			byte b[] = new byte[(int)size];
			fp.read(b);
			String str = new String(b, "KSC5601");
			fp.close();

			if(str.trim().equals("")){
				logger.warn("쿼리문이 없습니다.");
			}
			return str;
		} catch (Exception e) {
			logger.warn(e.toString());
			throw new Exception(e.toString());
		}
	}

	/**
	 * 문자열을 치환
	 * @param replaceContent 대상 문자열
	 * @param searchStr 바꿀 문자열
	 * @param replaceStr 바뀔문자열
	 * @return replaceContent 치환된 문자열
	 */
	public static String stringReplace(String replaceContent, String searchStr, String replaceStr) {
		String result = "";
		int i;

		do{
			i = replaceContent.indexOf(searchStr);

			if(i != -1){
				result = replaceContent.substring(0, i);
				result = result + replaceStr;
				if (replaceContent.length() > i + searchStr.length()) result = result + replaceContent.substring(i + searchStr.length());
				replaceContent = result;
			}
		} while(i != -1);

		return replaceContent ;
	}

	/**
	 * 데이타베이스에 저장하기전 문자열 치환
	 * @param srcStr 데이타베이스에 저장하기전 변경할 문자열
	 * @return 치환된 문자열
	 */
	public static String putReplaceStr(String srcStr){
		if(srcStr != null){
			srcStr = srcStr.replace('\'','＇');
			//srcStr = srcStr.replace(',','，');  //금액부분  콤마때문에 임시로 막음
		}
		return srcStr;
	}

	/**
	 * 데이타베이스에 변경된 문자열 치환하여 가져오기
	 * @param targStr 데이타베이스에서 가져온 문자열
	 * @return 치환된 문자열
	 */
	public static String getReplaceStr(String targStr){
		return getReplaceStr(targStr, true);
	}

	/**
	 * 2005.12.02 김성환 수정
	 * 국세청 신고파일 등 공백을 TRIM하지 않고 그냥
	 * 넘겨주어야 할 경우가 있어 flag를 사용하여 처리토록
	 *
	 * 데이타베이스에 변경된 문자열 치환하여 가져오기
	 * @param targStr 데이타베이스에서 가져온 문자열
	 * @return 치환된 문자열
	 */
	public static String getReplaceStr(String targStr, boolean needTrim){
		if(targStr != null){
			targStr = targStr.replace('','\'');
			targStr = targStr.replace('',',');
			targStr = targStr.replace('＇','\'');
			targStr = targStr.replace('，',',');
		}else{
			targStr = "";
		}
		if( needTrim ) {
			return targStr.trim();
		} else {
			return targStr;
		}
	}

	public static String getReplaceSpcStr(String targStr){
		return getReplaceSpcStr(targStr, true);
	}

	public static String getReplaceSpcStr(String targStr, boolean needTrim){
		if(targStr != null){
			targStr = targStr.replace("\'","＇");
			//targStr = targStr.replace("&","&amp;");
			//targStr = targStr.replace("<","&lt;");
			//targStr = targStr.replace(">'","&qt;");
		}else{
			targStr = "";
		}
		if( needTrim ) {
			return targStr.trim();
		} else {
			return targStr;
		}
	}

	/**
	 *
	 * @param original : 실제로 반환되어야할 데이터.
	 * @param replacement : 반환되어야할 데이터가 null인경우, replacement를 반환한다.
	 * @return String : 반환되는 데이터.
	 */
	public static String nvl(String original, String replacement) {
		if(original==null)
			return replacement;
		else
			return original;
	}

	/**
	 *
	 * @param original : 실제로 반환되어야할 데이터.
	 * @param replacement : 반환되어야할 데이터가 null인경우, replacement를 반환한다.
	 * @return String : 반환되는 데이터.
	 */
	public static String nvl(Object original, String replacement) {
		if(original==null)
			return replacement;
		else
			return original.toString();
	}

	/**
	 *
	 * @param obj : 임의의 문자열.
	 * @return boolean : 문자열이 널이거나 공백이면 true, 그렇지않으면 false
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) return true;
		else if (obj instanceof String) {
			String objstr = (String)obj;
			if (objstr.equals("") || objstr.toLowerCase().equals("null"))
				return true;
			else return false;
		} else return true;
	}

	/**
	 *
	 * @param arrStr : 하나의 스트링으로 변환될 배열요소..
	 * @param delimeter : 변환된 스트링의 원래 배열요소를 구분하기위한 요소.
	 * @return String : 변환된 스트링.
	 *
	 */
	public static String arrayToString(String arrStr[][], String delimeter_m, String delimeter_s) {
		StringBuffer str = new StringBuffer();

		for(int i=0; i < arrStr.length; i++) {
			str.append(arrayToString(arrStr[i], delimeter_s));
			str.append(delimeter_m);
		}	// for(int i=0; i < arrStr.length; i++) {

		return str.toString();
	}


	/**
	 *
	 * @param arrStr : 하나의 스트링으로 변환될 배열요소..
	 * @param delimeter : 변환된 스트링의 원래 배열요소를 구분하기위한 요소.
	 * @return String : 변환된 스트링.
	 *
	 */
	public static String arrayToString(String arrStr[], String delimeter) {
		StringBuffer str = new StringBuffer();

		for(int i=0; i < arrStr.length; i++) {
			str.append(arrStr[i]);
			str.append(delimeter);
		}	// for(int j=0; j < arrStr[i].length; j++) {

		return str.toString();
	}


	/**
	 *
	 * @param arrStr : 하나의 스트링으로 변환될 배열요소..
	 * @param delimeter : 변환된 스트링의 원래 배열요소를 구분하기위한 요소.
	 * @return String : 변환된 스트링.
	 *
	 */
	public static String list(String arrStr[]) {
		return arrayToString(arrStr, ",");
	}  // public static final String list(String arrStr[]) {


	/**
	 *
	 * @param arrStr : 하나의 스트링으로 변환될 배열요소..
	 * @param delimeter : 변환된 스트링의 원래 배열요소를 구분하기위한 요소.
	 * @return String : 변환된 스트링.
	 *
	 */
	public static String list(String arrStr[][]) {
		return arrayToString(arrStr, ";", ",");
	}	// public static final String list(String arrStr[][]) {

	/**
	 *
	 * @param str : delimeter에 의해서 요소들이 구분되는 문자열
	 * @param delimeter : 주로 delimeter_m은 '\n'으로 사용하고,
	 * 											delimeter_s는	 '\t'로 사용한다.
	 * @return String[][] : delimeter에
	 *//*
        public static String[][] stringToArray(String str, String delimeter_m, String delimeter_s) {
                String tmpArray[] = null;
                String array[][] = null;


     //메인 스트링 토크나이저.
                tmpArray = stringToArray(str,delimeter_m);

                for(int i=0; i<tmpArray.length; i++)	{
                //서브 스트링 토크나이저.
                        array[i] = stringToArray(tmpArray[i],delimeter_s);
                }

                return array;
        }*/


	/**
	 *
	 * @param str : delimeter에 의해서 요소들이 구분되는
	 * @param delimeter
	 * @return String[][]
	 */
	public static String[] stringToArray(String str, String delimeter) {
		String array[] = null;

		//스트링 토크나이저.
		StringTokenizer st = new StringTokenizer(str,delimeter);
		array = new String[st.countTokens()];

		for(int i=0; st.hasMoreTokens(); i++)	{
			array[i] = st.nextToken();
		}

		return array;
	}

	/**
	 *
	 * @param str : 2차원배열형태의 스트링
	 * @param colNo : 뽑아내려는 컬럼번호
	 * @return String[] : 뽑아낸 컬럼의 1차원배열
	 */

	public static String[] columnToRow(String[][] str, int colNo) {
		String[] tmp = new String[str.length];

		for(int i=0; i < str.length; i++)	tmp[i] = str[i][colNo];

		return tmp;
	}

	/**
	 *
	 * @param str : sql문에서 따옴표를 붙여야하는 문자형 데이터
	 * @return String : 따옴표가 양쪽으로 붙여진 스트링
	 */
	public static String makeSql(String str) {
		if(str==null) return "''";
		return "'" + str + "'";
	}

	/**
	 *
	 * @author Administrator
	 */
	public static String toCurrency(String strNo) {
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();

		long no = Long.parseLong(strNo);

		return  nf.format(no);
	}

	public static String toCurrency(long no) {
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();

		return  nf.format(no);
	}


	/** YYYYMMDD를 YYYY-MM-DD로 돌려준다. */
	public static String formatYmd(String str, boolean bEdit)
	{
		if (str == null)
		{
			if (bEdit) return "";
			else return "&nbsp;";
		}
		if (str.length() != 8) return str;
		return str.substring(0,4) + "-" + str.substring(4,6) + "-" + str.substring(6,8);
	}

	/** YYYYMM을 YYYY-MM으로 돌려준다. */
	public static String formatYmm(String str, boolean bEdit)
	{
		if (str == null)
		{
			if (bEdit) return "";
			else return "&nbsp;";
		}
		if (str.length() != 6) return str;
		return str.substring(0,4) + "-" + str.substring(4,6);
	}

	/** 주민번호을 XXXXXX-XXXXXXX으로 돌려준다. */
	public static String formatResno(String str, boolean bEdit)
	{
		if (str == null)
		{
			if (bEdit) return "";
			else return "&nbsp;";
		}
		if (str.length() != 13) return str;
		return str.substring(0,6) + "-" + str.substring(6,13);
	}

	/** 주민번호을 XXXXXX-XXXXXXX으로 돌려준다. */
	public static String unFormatYmd(String str, boolean bEdit)
	{
		if (str == null)
		{
			if (bEdit) return "";
			else return "&nbsp;";
		}
		if (str.length() != 10) return str;
		String yyyy = str.substring(0, 4);
		String mm = str.substring(5, 7);
		String dd = str.substring(8, 10);
		return yyyy+mm+dd;
	}

	/** 문자열에서 특정 문자제거 */
	public static String remove(String str, char tok)
	{
		String sResult="";
		if (str == null) return sResult;
		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) != tok) sResult = sResult + str.charAt(i);
		}
		return sResult;
	}

	/**
	 * 첫번째 문자를 찾아서 문자열을 대치
	 * @param sourceStr 대상 문자열
	 * @param findStr 찾을 문자열
	 * @param replaceStr 대치될 문자열
	 * @return sourceStr
	 */
	public static String replaceStr(String sourceStr, String findStr, String replaceStr) {


		if (sourceStr.indexOf(findStr) < 0 || findStr == null || "" == findStr.trim()){
			return sourceStr;
		}

		String retutnStr = "";
		retutnStr = sourceStr.substring(0,sourceStr.indexOf(findStr)) + replaceStr +
				sourceStr.substring(sourceStr.indexOf(findStr)+findStr.length(), sourceStr.length());

		return retutnStr;
	}

	/**
	 * 문자뒤에 입력받은  문자를 추가한다.
	 * @param src 대상문자
	 * @param addStr 추가할 문자
	 * @param count 추가할 개수
	 * @return
	 */
	public static String addStrRear(String src, String addStr, int count){

		int loop   = count - src.length();
		for(int i = 0 ; i < loop ; i++){
			src = src + addStr;
		}
		return src;
	}

	/**
	 * 문자앞에 입력받은  문자를 추가한다.
	 * @param src 대상문자
	 * @param addStr 추가할 문자
	 * @param count 추가할 개수
	 * @return
	 */
	public static String addStrFront(String src, String addStr, int count){

		int loop   = count - src.length();
		for(int i = 0 ; i < loop ; i++){
			src = addStr + src;
		}
		return src;
	}

	/**
	 * 문자열에서 특정 문자에 개수를 찾는다.
	 * @param src 대상문자열
	 * @param findChar 찾을 문자
	 * @return 개수
	 */
	public static int findCharCnt(String src, char findChar){
		int cnt = 0;
		String descStr = src;
		if (descStr.trim().length() < 1){
			return cnt;
		}
		for(;descStr.indexOf(findChar)>-1;){
			cnt++;
			descStr = descStr.substring(descStr.indexOf(findChar)+1,descStr.length());
		}
		return cnt;
	}

	/**
	 * 2005.12.02 김성환 추가
	 * 문자열 replace (문자열 전체)
	 *
	 */
	public static String replace(String str, String target, String replace)
	{
		if (str == null) return "";

		int targetLen = target.length();

		StringBuffer value = new StringBuffer();
		int i = 0;
		int j = 0;

		while (j > -1)
		{
			j = str.indexOf(target, i);

			if (j > -1)
			{
				value.append(str.substring(i, j)).append(replace);
				i = j + targetLen;
			}
		}

		value.append(str.substring(i, str.length()));

		return value.toString();
	}//:

	/**
	 * 2006.01.02 추가 - 김성환
	 * 한글 문자열을 Byte 단위로 substring
	 * - 한글깨짐을 방지하기 위한 메소드이므로 반환되는 문자열의 실제 길이는
	 *   pos 와 다를 수 있음
	 *
	 */
	public static String cutByteString(String str, int cutlen) {

		if( str == null )
			return "";

		byte[] byteString = str.getBytes();
		int i, len, usedBytes;
		len = byteString.length;

		if( len <= cutlen ) return str;

		usedBytes = 0;
		for( i=0; i < cutlen; i++ ) {
			if( (byteString[i] & 0xff) > 0x80 ) {
				usedBytes++;
			}
		}

		// if( usedBytes % 2 == 1 )
		if( (usedBytes - ((usedBytes >> 1)*2)) == 1 )
			i--;


		return new String( byteString, 0, i);

	}//:

	public static String NulltoString(String sourceStr,String changeStr){
		if( sourceStr == null || sourceStr.equals("")){
			return changeStr;
		}else{
			return sourceStr;
		}
	}

	public static String nl2br(String sourceStr){
		String str = sourceStr;

		str = str.replaceAll("\r\n", "<br>");
		str = str.replaceAll("\r", "<br>");
		str = str.replaceAll("\n", "<br>");

		return str;
	}

	public static String yn2TrueFalse(String sourceStr){
		if( sourceStr == null || sourceStr.equals("")){
			return sourceStr;
		}else{
			if(sourceStr.toUpperCase().equals("Y")){
				return "true";
			}else{
				return "false";
			}
		}
	}

	public static String trueFalse2yn(String sourceStr){
		if( sourceStr == null || sourceStr.equals("")){
			return sourceStr;
		}else{
			if(sourceStr.toUpperCase().equals("TRUE")){
				return "Y";
			}else{
				return "N";
			}
		}
	}

	public static String trueFalse2yn(boolean sourceStr){
		if(sourceStr){
			return "Y";
		}else{
			return "N";
		}
	}

	/**
	 * 2008.10.06 추가 - inchuli
	 * 특수문자 Replace 반환한다.
	 *
	 */
	public static String replaceString( String str )
	{
		byte[] b = str.getBytes() ;
		StringBuffer ret = new StringBuffer() ;

		for(int i = 0 ; i < b.length; i++)
		{
			if(b[i] == 0x08)        { ret.append('\\') ; ret.append('b') ;    }    // \b     Backspace
			else if(b[i] == 0x09)    { ret.append('\\') ; ret.append('t') ;    }    // \t    Tab
			else if(b[i] == 0x0A)    { ret.append('\\') ; ret.append('n') ;    }    // \n    New line
			else if(b[i] == 0x0D)    { ret.append('\\') ; ret.append('r') ;    }    // \r    carriage Return
			else if(b[i] == 0x22)    { ret.append('\\') ; ret.append('\"') ; }    // \"
			else if(b[i] == 0x27)    { ret.append('\\') ; ret.append('\'') ; }    // \'
			else if(b[i] == 0x5C)    { ret.append('\\') ; ret.append('\\') ; }    // \\   Back slash
			else
			{
				if((b[i] & 0x80) == 0x80)    // 한글
				{
					ret.append(new String(b, i, 2)) ;
					i++ ;
				}
				else                        // 영숫자
				{
					ret.append((char)b[i]) ;
				}
			}
		}

		return ret.toString() ;
	}

	//-->> 47. 3자리마다 콤마찍어서 봔환 ( String )
	/**
	 * 2010.01.05 추가 - inchuli
	 * 3자리마다 콤마찍어서 봔환 ( String )
	 *
	 */
	public static String setComma(String number) {
		String result = "";
		String tail = "";
		boolean flag = false;

		if( number.equals("") ) return result;

		int pts = number.indexOf(".");

		if(pts > 0){
			tail = number.substring(pts);
			number = number.substring(0,pts);
		}
		if( Long.parseLong(number) < 0 ){
			number = String.valueOf(Long.parseLong(number) * (-1));
			flag = true;
		}
		int len = number.length();

		if(!number.equals("")) {
			StringBuffer buffer = new StringBuffer(number);

			for(int i = len - 3; i > 0; i -= 3)
				buffer.insert(i, ',');

			if( pts > 0 )
				result = buffer.toString()+tail;
			else
				result = buffer.toString();

			if( flag ) result = "-" + result;
		}
		logger.debug(result);
		return result;
	}
	//-->> 암호화( String )
	/**
	 * 2010.10.26 추가 -
	 * 암호화 ( String )
	 *
	 */
	public static String getIncoding(String in){
		String key="(R2F6Y[#A@8?7H!)H-Q7";
		int tmp=0;
		int tmpCheck = 0;
		String result = "";

		for (int i=0 ;i < in.length(); i++) {
			tmp = in.charAt(i) + key.charAt(i%20)%key.length() +25288/key.charAt(i%20);

			if((tmp/10000) > 0) tmpCheck = (key.charAt(i%20)/10 + 2)%10;
			else if((tmp/100) > 0) tmpCheck = (key.charAt(i%20)/10 + 1)%10;
			result = result+tmpCheck+String.valueOf(tmp);
		}

		return result;
	}
	//-->> 복호화( String )
	/**
	 * 2010.10.26 추가 -
	 * 복호화 ( String )
	 *
	 */
	public static String getDecoding(String in){
		String key="(R2F6Y[#A@8?7H!)H-Q7";
		int tmp=0;
		int tmpCheck = 0;
		String result = "";
		int j=0;
		int k=0;

		do {
			tmpCheck = Integer.parseInt(in.substring(j,j+1));
			if(tmpCheck == 1) tmpCheck = 2;
			else if(tmpCheck < 2) tmpCheck = (tmpCheck+10) - key.charAt(k%20)/10;
			else tmpCheck = tmpCheck - key.charAt(k%20)/10;

			if(tmpCheck == 1) {
				tmp = Integer.parseInt(in.substring(j+1,j+4));
				j = j+ 4;
			} else if(tmpCheck == 2) {
				tmp = Integer.parseInt(in.substring(j+1,j+6));
				j = j+ 6;
			}

			result = result + (char)(tmp-key.charAt(k%20)%key.length()-25288/key.charAt(k%20));
			k++;
		} while (in.length()>j);

		return result;
	}

	/*
	 * 숫자형을 자릿수를 맞추고 싶을때 사용한다. .lpad(23, 9) => 000000023
	 */
	public static String lpadForInt(int num, int size) {
		String f = "%0"+size+"d";
		return String.format(f, num);
	}

	/**
	 * lpad 함수
	 *
	 * @param str   대상문자열, len 길이, addStr 대체문자
	 * @return      문자열
	 */

	public static String lpad(String str, int len, String addStr) {
		String result = str;
		int templen   = len - result.length();

		for (int i = 0; i < templen; i++){
			result = addStr + result;
		}

		return result;
	}


	/**
    	         * 파일 이름이 유효한지 확인한다.
    	         *
    	         * @author mcsong@gmail.com
    	         * @param fileName 파일의 이름, Path를 제외한 순수한 파일의 이름..
    	         * @return
    	         */
	public static boolean isValidFileName(String fileName) {
		if(fileName == null || fileName.trim().length() == 0)	return false;
		return !Pattern.compile(ILLEGAL_EXP).matcher(fileName).find();
	}

	/**
	 * 파일 이름에 사용할 수 없는 캐릭터를 바꿔서 유효한 파일로 만든다.
	 *
	 * @author mcsong@gmail.com
	 * @param fileName 파일 이름, Path를 제외한 순수한 파일의 이름..
	 * @param replaceStr 파일 이름에 사용할 수 없는 캐릭터의 교체 문자
	 * @return
	 */
	public static String makeValidFileName(String fileName, String replaceStr){
		if(fileName == null || fileName.trim().length() == 0 || replaceStr == null) return String.valueOf(System.currentTimeMillis());

		return fileName.replaceAll(ILLEGAL_EXP, replaceStr);
	}

	public static String convertAppend(Object obj, String objType, String datepattern) {
		String rtn = "";

		if(obj != null && !"".equals(obj)){
			if("string".equals(objType)) {
				rtn = "'" + obj + "'";
			} else if("int".equals(objType)) {
				if(datepattern=="0"){
					if(Float.parseFloat(obj.toString()) > 0){
						rtn = "" + obj + "";
					}else{
						rtn = "null";
					}
				}else{
					rtn = "" + obj + "";
				}

			} else if("date".equals(objType)) {
				rtn = "TO_DATE('" + obj + "','"+datepattern+"') ";
			} else if("float".equals(objType)) {
				//rtn = "'" + obj + "'";
			} else {
				rtn = "'" + obj + "'";
			}
		}else{
			rtn = "null";
		}
		return rtn;
	}

}