package com.optidpp.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 *
 *
 * apache의 StringUtils에 없는것을 정의한다.
 * 만들어놓고 한번도 안봄.. 나중에 정리하기.
 */

/**
 * 01 StringUtil =>  (String str)
 * 02 replaceSingleQuot => (String str)
 * 03 nl2br = (String sourceStr)
 * 04 stringReplace = (String replaceContent, String searchStr, String replaceStr)
 * 05 replaceInParam = (String str)
 * 06 join=  배열을 받아 연결될 문자열로 연결한다. 이때 각 엘레멘트 사이에 구분문자열을 추가한다.
 * 07 split = 문자열을 지정된 Token Seperator로 Tokenize한다.(문자열 배열을 리턴)
 * sortStringArray
 *
 * 07 linkedText ==> 자동링크 작업중[]
 * 08 nvl => strTarget이 null 이거나 화이트스페이스 일 경우 strDest을 반환한다
 * 09 isNull => 대상문자열이 null 인지 여부 확인하기
 * 10 cutText => 대상 문자열이 지정한 길이보다 길 경우 지정한 길이만큼 잘라낸 문자열 반환하기
 * 11 indexOfIgnore => 대상문자열에 지정한 문자가 위치한 위치 값을 반환하기(대소문자 무시)
 * 12 replace => 대상 문자열 치환하기
 * 13 replaceAll => 대상 문자열 치환하기
 * 14 removeFormat => 각종 구분자 제거하기
 * 15 removeComma => 콤마 제거하기
 * 16 removeHTML => HTML 태그 제거하기
 * 17 padValue => 값 채우기
 * 18 padLeft => 좌측으로 값 채우기
 * 19 padRight  => 우측으로 값 채우기
 * 20 encodingHTML => HTML을 캐리지 리턴 값으로 변환하기
 * 21 decodingHTML => 캐리지리턴값을 HTML 태그로 변환하기
 * 22 formatMoney => 대상 문자열을 금액형 문자열로 변환하기
 * 23 round => 대상문자열의 소숫점 설정하기
 * 24 formatDate => 대상 문자열을 날짜 포멧형 문자열로 변환하기
 * 25 formatSSN => 대상 문자열을 주민등록번호 형식의 문자열로 변환하기
 * 26 formatPhone  =>대상 문자열을 전화번호 포멧 문자열로 변환하기
 * 27 formatZIP =>대상문자열을 우편번호 형식으로 변환하기
 * 28 transDecToBinary => RGB 10진수 값을 2진수 값으로 리턴
 * 29 transDecToOctal  => RGB 10진수 값을 8진수 값으로 리턴
 * 30 transDecToHex => RGB 10진수 값을 16진수 값으로 리턴
 * 31 parseInt => 안전하게 String을 int로 변환
 * 32 parseLong => 안전하게 String을 long으로 변환
 * 33 parseFloat => 안전하게 String을 float으로 변환
 * 34 parseDouble =>안전하게 String을 double로 변환
 * 35 escape  => String Escape 처리
 * 36 unescape => String UnEscape 처리
 */

/**
 * Common String Util
 *
 * @author ParkMoohun
 */

public class StringUtil extends StringUtils {


	/**
	 * 공백문자
	 */
	public final static char WHITE_SPACE = ' ';

	static String m_whiteSpace = " \t\n\r";

	static char m_citChar = '"';

	/**
	 * XSS 공격 치환
	 * @param str
	 * @return String
	 */
	public synchronized static String removeXSS(String str) {
		if(str.length() < 1 ) return str;
		str = str	.replaceAll("&", "&amp;")
				.replaceAll("\"","&quot;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\'","&#39;");
		return str;
	}

	/**
	 * SingleQuot 변환
	 * @param str
	 * @return String
	 */
	public synchronized static String replaceSingleQuot(String str) {
		if(null == str) return "";
		if(str.length() < 1 ) return str;
		str = str.replaceAll("\'", "''");
		return str;
	}


	public synchronized static String nl2br(String sourceStr){
		String str = sourceStr;

		str = str.replaceAll("\r\n", "<br>");
		str = str.replaceAll("\r", "<br>");
		str = str.replaceAll("\n", "<br>");

		return str;
	}


	/**
	 * 문자열을 치환
	 * @param replaceContent 대상 문자열
	 * @param searchStr 바꿀 문자열
	 * @param replaceStr 바뀔문자열
	 * @return replaceContent 치환된 문자열
	 * import org.anyframe.util.StringUtil; 하면 content 값이 없어짐 ==> 테스트 해봐야함
	 * content = StringUtil.replace(content, key, (String) this.MAIL_MAPPING_MAP.get(key));
	 *
	 */
	public synchronized static String stringReplace(String replaceContent, String searchStr, String replaceStr) {
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
	 * 배열을 받아 연결될 문자열로 연결한다. 이때 각 엘레멘트 사이에 구분문자열을 추가한다.<br>
	 *
	 * @param aobj
	 * 문자열로 만들 배열
	 * @param s
	 * 각 엘레멘트의 구분 문자열
	 * @return 연결된 문자열
	 * String[] test = {"aaa", "bbb"}; StringUtil.collectionToCommaDelimitedString(test,",")="aaa,bbb" 참조
	 * <code>
	 * String[] source = new String[] {"AAA","BBB","CCC"};<br>
	 * String result = TextUtil.join(source,"+");<br>
	 * </code> <code>result</code>는 <code>"AAABBBCCC"</code>를 가지게 된다.
	 */
	public static String join(Object aobj[], String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = aobj.length;
		if (i > 0) {
			stringbuffer.append(aobj[0].toString());
		}

		for (int j = 1; j < i; j++) {
			stringbuffer.append(s);
			stringbuffer.append(aobj[j].toString());
		}

		return stringbuffer.toString();
	}



	/**
	 * 문자열을 지정된 Token Seperator로 Tokenize한다.(문자열 배열을 리턴)<br>
	 * @param s  원본 문지열
	 * @param s1 Token Seperators
	 * @return 토큰들의 배열
	 *
	 * <code>
	 * String source = "Text token\tis A Good\nAnd bad.";<br>
	 * String[] result = TextUtil.split(source, " \t\n");<br>
	 * </code> <code>result</code>는
	 * <code>"Text","token","is","A","Good","And","bad."</code> 를 가지게 된다.
	 */
	public static String[] split(String s, String s1) {
		StringTokenizer stringtokenizer = new StringTokenizer(s, s1);
		int i = stringtokenizer.countTokens();
		String as[] = new String[i];
		for (int j = 0; j < i; j++) {
			as[j] = stringtokenizer.nextToken();
		}
		return as;
	}




	/**
	 * 원본 문자열을 일반적인 공백문자(' ','\n','\t','\r')로 토큰화 한다.
	 *
	 * @param s  원본문자열
	 * @return 토큰의 배열
	 *
	 * <code>
	 * String source = "Text token\tis A Good\nAnd\rbad.";<br>
	 * String[] result = TextUtil.splitwords(source);<br>
	 * </code> <code>result</code>는
	 * <code>"Text","token","is","A","Good","And","bad."</code> 를 가지게 된다.
	 */
	public static String[] splitwords(String s) {
		return splitwords(s, m_whiteSpace);
	}

	/**
	 * 원본 문자열을 일반적인 공백문자(' ','\n','\t','\r')로 토큰화 한다.<br> 겹따옴표('"') 안의 내용은 하나의 토큰으로 판단하여 문자열을 토큰화 한다.
	 *
	 * @param s   원본 문자열
	 * @param s1  Token Seperators
	 * @return Description of the Returned Value
	 *
	 * <code>
	 * String source = "Text token\tis A \"Good day\"\nAnd\r\"bad day.\"";<br>
	 * String[] result = TextUtil.splitwords(source);<br>
	 * </code> <code>result</code>는
	 * <code>"Text","token","is","A","Good day","And","bad day."</code> 를 가지게된다.
	 */
	public static String[] splitwords(String s, String s1) {
		boolean flag = false;
		StringBuffer stringbuffer = null;
		Vector<StringBuffer> vector = new Vector<StringBuffer>();
		for (int i = 0; i < s.length();) {
			char c = s.charAt(i);
			if (!flag && s1.indexOf(c) != -1) {
				if (stringbuffer != null) {
					vector.addElement(stringbuffer);
					stringbuffer = null;
				}
				for (; i < s.length() && s1.indexOf(s.charAt(i)) != -1; i++) {
					;
				}
			} else {
				if (c == m_citChar) {
					if (flag) {
						flag = false;
					} else {
						flag = true;
					}
				} else {
					if (stringbuffer == null) {
						stringbuffer = new StringBuffer();
					}
					stringbuffer.append(c);
				}
				i++;
			}
		}

		if (stringbuffer != null) {
			vector.addElement(stringbuffer);
		}
		String as[] = new String[vector.size()];
		for (int j = 0; j < vector.size(); j++) {
			as[j] = new String(vector.elementAt(j));
		}

		return as;
	}

	/**
	 * 배열을 Vector로 만든다.<br>
	 *
	 * @param array   원본 배열
	 * @return 배열과 같은 내용을 가지는 Vector
	 *
	 */
	public static Vector<Object> toVector(Object[] array) {
		if (array == null) {
			return null;
		}
		Vector<Object> vec = new Vector<Object>(array.length);

		for (int i = 0; i < array.length; i++) {
			vec.add(i, array[i]);
		}
		return vec;
	}

	/**
	 * 문자열의 배열을 소팅한다.
	 *
	 * @param array  원본 배열
	 * @return 배열과 같은 내용을 가지는 Vector
	 */
	public static String[] sortStringArray(String[] source) {

		java.util.Arrays.sort(source);

		return source;
	}

	/**
	 * 문자열의 Enemration을 소팅된 배열로 반환한다.
	 *
	 * @param source   원본 열거형
	 * @return 열거형의 값을 가진 배열
	 */
	public static String[] sortStringArray(Enumeration<?> source) {
		Vector buf = new Vector();
		while (source.hasMoreElements()) {
			buf.add(source.nextElement());
		}
		String[] buf2 = new String[buf.size()];

		for (int i = 0; i < buf.size(); i++) {

			Object obj = buf.get(i);
			if (obj instanceof String) {
				buf2[i] = (String) obj;
			} else {
				throw new IllegalArgumentException("Not String Array");
			}
		}
		java.util.Arrays.sort(buf2);
		return buf2;
	}


	/**
	 * 필요한가? .. 고민중..
	 * inParam으로  변환
	 * @param str
	 * @return String
	 */
	public synchronized static String replaceInParam(String str) {
		if(null == str) return "";
		if(str.length() < 1 ) return str;
		str = "'"+str.replaceAll(",", "','")+"'";
		return str;
	}



	/**
	 * 문자열을 치환하여 HTML링크를 만들어 준다. 게시판 댓글 등에 사용 https?
	 * 공백 문자는 자동으로 링크가 안되니 주의! => 작업중[게시판 자동링크 작업중]
	 */
	/*
    public static String linkedText(CharSequence sText) {
        Pattern p = Pattern.compile("(http|https|ftp)://[^\\s^\\.]+(\\.[^\\s^\\.]+)*");
        Matcher m = p.matcher(sText);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "<a href='" + m.group() + "'>" + m.group() + "</a>");
        }
        m.appendTail(sb);

        return sb.toString();

    }

    String[] test = {"aaa", "bbb"}; StringUtil.collectionToCommaDelimitedString(Arrays.asList(test),",")="aaa,bbb"


	 */



	/**
	 * strTarget이 null 이거나 화이트스페이스 일 경우 strDest을 반환한다.
	 *
	 * @param strTarget 대상 문자열
	 * @param strDest 대체 문자열
	 * @return strTarget이 null 이거나 화이트스페이스 일 경우 strDest 문자열로 반환
	 */
	public static String nvl(final String strTarget, final String strDest) {
		if (strTarget == null || "".equals(strTarget)) {
			return strDest;
		}
		return strTarget;
	}

	/**
	 * strTarget이 null 이거나 화이트스페이스 일 경우 화이트스페이스로 반환한다.
	 *
	 * @param strTarget 대상문자열
	 * @return strTarget이 null 이거나 화이트스페이스 일 경우 화이트스페이스로 반환
	 */
	public static String nvl(final String strTarget) {
		return nvl(strTarget, "");
	}

	/**
	 * 대상문자열이 null 인지 여부 확인하기
	 *
	 * @paramstrTarget 대상 문자열
	 * @return null 여부
	 */
	public static boolean isNull(final String strTarget) {
		if (strTarget == null) {
			return true;
		}
		return false;
	}

	/**
	 * 대상 문자열이 지정한 길이보다 길 경우 지정한 길이만큼 잘라낸 문자열 반환하기
	 *
	 * @param strTarget 대상 문자열
	 * @param nLimit 길이
	 * @param bDot 잘린 문자열이 존재할 경우 ... 표시 여부
	 * @return 대상 문자열이 지정한 길이보다 길 경우 지정한 길이만큼 잘라낸 문자열
	 */
	public static String cutText(final String strTarget, final int nLimit, final boolean bDot) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}

		String retValue = null;
		int nLen = strTarget.length();
		int nTotal = 0;
		int nHex = 0;
		String strDot = "";

		if (bDot) {
			strDot = "...";
		}

		for (int i = 0; i < nLen; i++) {
			nHex = strTarget.charAt(i);
			nTotal += Integer.toHexString(nHex).length() / 2;

			if (nTotal > nLimit) {
				retValue = strTarget.substring(0, i) + strDot;
				break;
			}
			else if (nTotal == nLimit) {
				if (i == (nLen - 1)) {
					retValue = strTarget.substring(0, i - 1) + strDot;
					break;
				}

				retValue = strTarget.substring(0, i + 1) + strDot;
				break;
			}
			else {
				retValue = strTarget;
			}
		}

		return retValue;
	}

	/**
	 * 대상문자열에 지정한 문자가 위치한 위치 값을 반환하기(대소문자 무시)
	 *
	 * @param strTarget 대상 문자열
	 * @param strDest 찾고자 하는 문자열
	 * @param nPos 시작 위치
	 * @return 대상문자열에 지정한 문자가 위치한 위치 값을 반환
	 */
	public static int indexOfIgnore(final String strTarget, final String strDest, final int nPos) {
		if (strTarget == null || "".equals(strTarget)) {
			return -1;
		}
		return strTarget.toLowerCase().indexOf(strDest.toLowerCase(), nPos);
	}

	/**
	 * 대상문자열에 지정한 문자가 위치한 위치 값을 반환하기(대소문자 무시)
	 *
	 * @param strTarget 대상 문자열
	 * @param strDest 찾고자 하는 문자열
	 * @return 대상문자열에 지정한 문자가 위치한 위치 값을 반환
	 */
	public static int indexOfIgnore(final String strTarget, final String strDest) {
		return indexOfIgnore(strTarget, strDest, 0);
	}

	/**
	 * 대상 문자열 치환하기
	 *
	 * @param strTarget 대상문자열
	 * @param strOld 찾고자 하는 문자열
	 * @param strNew 치환할 문자열
	 * @param bIgnoreCase 대소문자 구분 여부
	 * @param bOnlyFirst 한 번만 치환할지 여부
	 * @return 치환한 문자열
	 */
	public static String replace(final String strTarget, final String strOld, final String strNew, final boolean bIgnoreCase, final boolean bOnlyFirst) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}

		StringBuffer objDest = new StringBuffer("");
		int nLen = strOld.length();
		int strTargetLen = strTarget.length();
		int nPos = 0;
		int nPosOld = 0;

		if (bIgnoreCase) { // 대소문자 구분하지 않을 경우
			while ((nPos = indexOfIgnore(strTarget, strOld, nPosOld)) >= 0) {
				objDest.append(strTarget.substring(nPosOld, nPos));
				objDest.append(strNew);
				nPosOld = nPos + nLen;

				if (bOnlyFirst) { // 한번만 치환할시
					break;
				}
			}
		}
		else { // 대소문자 구분하는 경우
			while ((nPos = strTarget.indexOf(strOld, nPosOld)) >= 0) {
				objDest.append(strTarget.substring(nPosOld, nPos));
				objDest.append(strNew);
				nPosOld = nPos + nLen;

				if (bOnlyFirst) {
					break;
				}
			}
		}

		if (nPosOld < strTargetLen) {
			objDest.append(strTarget.substring(nPosOld, strTargetLen));
		}

		return objDest.toString();
	}
	/**
	 * 대상 문자열 치환하기
	 *
	 * @param strTarget 대상문자열
	 * @param strOld 찾고자 하는 문자열
	 * @param strNew 치환할 문자열
	 * @return 치환된 문자열
	 */
	public static String replaceAll(final String strTarget, final String strOld, final String strNew) {
		return replace(strTarget, strOld, strNew, false, false);
	}

	/**
	 * 각종 구분자 제거하기
	 *
	 * @param String strTarget 대상문자열
	 * @return String 구분자가 제거된 문자열
	 */
	public static String removeFormat(final String strTarget) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}
		return strTarget.replaceAll("[$|^|*|+|?|/|:|\\-|,|.|\\s]", "");
	}

	/**
	 * 콤마 제거하기
	 *
	 * @param String
	 *            strTarget 대상 문자열
	 * @return String 콤마가 제거된 문자열
	 */
	public static String removeComma(final String strTarget) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}
		return strTarget.replaceAll("[,|\\s]", "");
	}

	/**
	 * HTML 태그 제거하기
	 *
	 * @param strTarget 대상문자열
	 * @return 태그가 제거된 문자열
	 */
	public static String removeHTML(final String strTarget) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}
		return strTarget.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}

	/**
	 * 값 채우기
	 *
	 * @param strTarget 대상 문자열
	 * @param strDest 채워질 문자열
	 * @param nSize 총 문자열 길이
	 * @param bLeft 채워질 문자의 방향이 좌측인지 여부
	 * @return 지정한 길이만큼 채워진 문자열
	 */
	public static String padValue(final String strTarget, final String strDest, final int nSize, final boolean bLeft) {
		if (strTarget == null) {
			return strTarget;
		}

		String retValue = null;
		StringBuffer objSB = new StringBuffer();
		int nLen = strTarget.length();
		int nDiffLen = nSize - nLen;

		for (int i = 0; i < nDiffLen; i++) {
			objSB.append(strDest);
		}

		if (bLeft) { // 채워질 문자열의 방향이 좌측일 경우
			retValue = objSB.toString() + strTarget;
		}
		else { // 채워질 문자열의 방향이 우측일 경우
			retValue = strTarget + objSB.toString();
		}

		return retValue;
	}

	/**
	 * 좌측으로 값 채우기
	 *
	 * @param strTarget 대상 문자열
	 * @param strDest 채워질 문자열
	 * @param nSize 총 문자열 길이
	 * @return 채워진 문자열
	 */
	public static String padLeft(final String strTarget, final String strDest, final int nSize) {
		return padValue(strTarget, strDest, nSize, true);
	}

	/**
	 * 좌측에 공백 채우기
	 *
	 * @param strTarget 대상 문자열
	 * @param nSize 총 문자열 길이
	 * @return 채워진 문자열 길이
	 */
	public static String padLeft(final String strTarget, final int nSize) {
		return padValue(strTarget, " ", nSize, true);
	}

	/**
	 * 우측으로 값 채우기
	 *
	 * @param strTarget 대상문자열
	 * @param strDest 채워질 문자열
	 * @param nSize 총 문자열 길이
	 * @return 채워진 문자열 길이
	 */
	public static String padRight(final String strTarget, final String strDest, final int nSize) {
		return padValue(strTarget, strDest, nSize, false);
	}

	/**
	 * 우측으로 공백 채우기
	 *
	 * @param strTarget 대상문자열
	 * @param nSize 총 문자열 길이
	 * @return 채워진 문자열
	 */
	public static String padRight(final String strTarget, final int nSize) {
		return padValue(strTarget, " ", nSize, false);
	}

	/**
	 * HTML을 캐리지 리턴 값으로 변환하기
	 *
	 * @param strTarget 대상 문자열
	 * @return HTML을 캐리지 리턴값으로 반환한 문자열
	 */
	public static String encodingHTML(final String strTarget) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult)) {
			return strResult;
		}

		strResult = strResult.replaceAll("<br>", "\r\n");
		strResult = strResult.replaceAll("<q>", "'");
		strResult = strResult.replaceAll("&quot;", "\"");
		strResult = strResult.replaceAll("<BR>", "\r\n");
		strResult = strResult.replaceAll("<Q>", "'");
		strResult = strResult.replaceAll("&QUOT;", "\"");

		return strResult;
	}

	/**
	 * 캐리지리턴값을 HTML 태그로 변환하기
	 *
	 * @param strTarget 대상 문자열
	 * @return 캐리지 리턴값을 HTML 태그로 변환한 문자열
	 */
	public static String decodingHTML(final String strTarget) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult)) {
			return strResult;
		}

		strResult = strResult.replaceAll("\r\n", "<br>");
		strResult = strResult.replaceAll("'", "<q>");
		strResult = strResult.replaceAll("\"", "&quot;");
		strResult = strResult.replaceAll("\r\n", "<BR>");
		strResult = strResult.replaceAll("'", "<Q>");
		strResult = strResult.replaceAll("\"", "&QUOT;");

		return strResult;
	}

	/**
	 * 대상 문자열을 금액형 문자열로 변환하기
	 *
	 * @param strTarget 대상 문자열
	 * @return 금액형 문자열
	 */
	public static String formatMoney(final String strTarget) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult.trim())) {
			return "0";
		}

		strResult = removeComma(strResult);
		String strSign = strResult.substring(0, 1);

		if ("+".equals(strSign) || "-".equals(strSign)) { // 부호가 존재할 경우
			strSign = strResult.substring(0, 1);
			strResult = strResult.substring(1);
		}
		else {
			strSign = "";
		}

		String strDot = "";

		if (strResult.indexOf(".") != -1) { // 소숫점이 존재할 경우
			int nPosDot = strResult.indexOf(".");
			strDot = strResult.substring(nPosDot, strResult.length());
			strResult = strResult.substring(0, nPosDot);
		}

		StringBuffer objSB = new StringBuffer(strResult);
		int nLen = strResult.length();

		for (int i = nLen; 0 < i; i -= 3) // Comma 단위
		{
			objSB.insert(i, ",");
		}

		return strSign + objSB.substring(0, objSB.length() - 1) + strDot;
	}

	/**
	 * 대상문자열의 소숫점 설정하기
	 *
	 * @param strTarget  대상문자열
	 * @param nDotSize 소숫점 길이
	 * @return
	 */
	public static String round(final String strTarget, final int nDotSize) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult.trim())) {
			return strResult;
		}

		String strDot = null;
		int nPosDot = strResult.indexOf(".");

		if (nPosDot == -1) { // 소숫점이 존재하지 않을 경우
			strDot = (nDotSize == 0) ? padValue("", "0", nDotSize, false) : "." + padValue("", "0", nDotSize, false);
		}
		else { // 소숫점이 존재할 경우
			String strDotValue = strResult.substring(nPosDot + 1); // 소숫점 이하 값
			strResult = strResult.substring(0, nPosDot); // 정수 값

			if (strDotValue.length() >= nDotSize) { // 실제 소숫점 길이가 지정한 길이보다 크다면
				// 지정한 소숫점 길이 만큼 잘라내기
				strDot = "." + strDotValue.substring(0, nDotSize);
			}
			else { // 실제 소숫점길이가 지정한 길이보다 작다면 지정한 길이만큼 채우기
				strDot = "." + padValue(strDotValue, "0", nDotSize, false);
			}
		}

		return strResult + strDot;
	}

	/**
	 * 대상 문자열을 날짜 포멧형 문자열로 변환하기
	 *
	 * @param strTarget 대상 문자열
	 * @return 날짜 포멧 문자열로 변환하기
	 */
	public static String formatDate(String strTarget) {
		String strValue = removeFormat(strTarget);

		if (strValue.length() != 8) {
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);
		objSB.insert(4, "-");
		objSB.insert(7, "-");

		return objSB.toString();
	}

	/**
	 * 대상 문자열을 주민등록번호 형식의 문자열로 변환하기
	 *
	 * @param strTarget 대상 문자열
	 * @return 주민등록번호 형식 문자열
	 */
	public static String formatSSN(String strTarget) {
		String strValue = removeFormat(strTarget);

		if (strValue.length() != 8) {
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);
		objSB.insert(4, "-");
		objSB.insert(7, "-");

		return objSB.toString();
	}

	/**
	 * 대상 문자열을 전화번호 포멧 문자열로 변환하기
	 *
	 * @param strTarget 대상문자열
	 * @return 전화번호 포멧 문자열
	 */
	public static String formatPhone(String strTarget) {
		String strValue = removeFormat(strTarget);
		int nLength = strValue.length();

		if (nLength < 9 || nLength > 12) { // 9 ~ 12 占쏙옙' 占쏙옙占쏙옙 占쏙옙占�
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);

		if (strValue.startsWith("02")) { // 서울지역일 경우
			if (nLength == 9) {
				objSB.insert(2, "-");
				objSB.insert(6, "-");
			}
			else {
				objSB.insert(2, "-");
				objSB.insert(7, "-");
			}
		}
		else { // 서울외 지역 또는 휴대폰 일 경우
			if (nLength == 10) {
				objSB.insert(3, "-");
				objSB.insert(7, "-");
			}
			else { // 내선번호등과 같은 특수 번호일 경우
				objSB.insert(3, "-");
				objSB.insert(8, "-");
			}
		}

		return objSB.toString();
	}

	/**
	 * 대상문자열을 우편번호 형식으로 변환하기
	 *
	 * @param strTarget 대상문자열
	 * @return 우편번호형 문자열
	 */
	public static String formatZIP(String strTarget) {
		String strValue = removeFormat(strTarget);

		if (strValue.length() != 6) {
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);
		objSB.insert(3, "-");

		return objSB.toString();
	}

	/**
	 * RGB 10진수 값을 2진수 값으로 리턴
	 *
	 * @param rgb
	 * @return rgb (ex: FFFFFF)
	 */
	public static String transDecToBinary(int rgb) {
		return Integer.toBinaryString(rgb).toUpperCase();
	}

	/**
	 * RGB 10진수 값을 8진수 값으로 리턴
	 *
	 * @param rgb
	 * @return rgb (ex: FFFFFF)
	 */
	public static String transDecToOctal(int rgb) {
		return Integer.toOctalString(rgb).toUpperCase();
	}

	/**
	 * RGB 10진수 값을 16진수 값으로 리턴
	 *
	 * @param rgb
	 * @return rgb (ex: FFFFFF)
	 */
	public static String transDecToHex(int rgb) {
		return Integer.toHexString(rgb).toUpperCase();
	}

	/**
	 * 안전하게 String을 int로 변환
	 *
	 * @param str
	 * @return
	 */
	public static int parseInt(String str) {
		int num;
		try {
			num = Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 안전하게 String을 long으로 변환
	 *
	 * @param str
	 * @return
	 */
	public static long parseLong(String str) {
		long num;
		try {
			num = Long.parseLong(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 안전하게 String을 float으로 변환
	 *
	 * @param str
	 * @return
	 */
	public static float parseFloat(String str) {
		float num;
		try {
			num = Float.parseFloat(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 안전하게 String을 double로 변환
	 *
	 * @param str
	 * @return
	 */
	public static double parseDouble(String str) {
		double num;
		try {
			num = Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * String Escape 처리
	 *
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
				tmp.append(j);
			}
			else if (j < 256) {
				tmp.append("%");
				if (j < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(j, 16));
			}
			else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * String UnEscape 처리
	 *
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				}
				else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			}
			else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				}
				else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}



	public static String getRelativeUrl(HttpServletRequest request) {
		String baseUrl = null;

		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
			baseUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		else
			baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();

		String url = request.getRequestURI();
		String urlpath = request.getContextPath();

		baseUrl = url.substring(urlpath.length()) + (request.getQueryString() != null  ? "?" + request.getQueryString(): "");
		return baseUrl;
	}

	public static String getBaseUrl(HttpServletRequest request) {
		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
			return request.getScheme() + "://" +  request.getServerName() + request.getContextPath();
		else
			return request.getScheme() + "://" +  request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
	}


	/**
	 * 문자열을 특정 문자의 개수를 구한다.
	 */
	public static int getCharCount(String str, char ch) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch) {
				count++;
			}
		}
		return count;
	}


	/**
	 * strs들중 일부라도 매치가 되면 true를 리턴한다.
	 */
	public static boolean isMatchIgnoreCase(String body, String... strs) {
		body = body.toUpperCase();
		for (String str : strs)
			if (StringUtil.contains(body, str.toUpperCase())) return true;
		return false;
	}

	/**
	 * 카멜 케이스를 "_" 형태로 연결한다. prototype의 underscore와는 달리 대문자 이다. ex) userName =>
	 * USER_NAME
	 */
	public static String getUnderscore(String str) {

		char[] chars = str.toCharArray();
		StringBuffer stringBuffer = new StringBuffer();
		for (char cha : chars) {
			if (cha >= 'A' && cha <= 'Z') stringBuffer.append('_');
			stringBuffer.append(cha);
		}
		return stringBuffer.toString().toUpperCase();
	}

	/** '_' or '-' 형태의 연결을 카멜 케이스로 변환한다. ex) USER_NAME => userName */
	public static String getCamelize(String str) {
		char[] chars = str.toCharArray();
		boolean nextCharIsUpper = false;
		StringBuffer stringBuffer = new StringBuffer();
		for (char cha : chars) {
			if (cha == '_' || cha == '-') {
				nextCharIsUpper = true;
				continue;
			}
			if (nextCharIsUpper) {
				stringBuffer.append(Character.toUpperCase(cha));
				nextCharIsUpper = false;
			} else stringBuffer.append(Character.toLowerCase(cha));
		}
		return stringBuffer.toString();
	}

	static final String ILLEGAL_EXP = "[:\\\\/%*?:|\"<>]";

	/**
	 * 파일 이름이 유효한지 확인한다.
	 *
	 * @author mcsong@gmail.com
	 * @param fileName 파일의 이름, Path를 제외한 순수한 파일의 이름..
	 * @return
	 */
	public static boolean isValidFileName(String fileName) {
		if(fileName == null || fileName.trim().length() == 0)
			return false;

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
	public static String makeValidFileName(String fileName, String replaceStr) {
		if(fileName == null || fileName.trim().length() == 0 || replaceStr == null)
			return String.valueOf(System.currentTimeMillis());

		return fileName.replaceAll(ILLEGAL_EXP, replaceStr);
	}

	
	 public static Map<String, String> getQueryMap(String query)  
	 {  
		 Map<String, String> map = new HashMap<String, String>();  
		 try {
			 String[] params = query.split("&");  
		    
		     for (String param : params)  
		     {  
		    	 String [] splitParam = param.split("=");
		    	 String name = "";  
		         String value = "";
		    	 if(splitParam!=null&&splitParam.length==2) {
	 		         name = splitParam[0];  
	 		         value = splitParam[1];  
		    	 } else if(splitParam!=null&&splitParam.length==1) {
	 		         name = splitParam[0];  
	 		         value = "";  
		    	 } else {
		    		 name = param;  
	 		         value = ""; 
		    	 }

		         map.put(name, value);  
		     }  
		} catch (Exception e) {
		}
	    return map;  
	 }  
}
