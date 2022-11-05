/*===================================================================================
 * System             : ��ƿ��Ƽ �ý���
 * Program Id         :
 * Program Name       :
 * Source File Name   :
 * Description        : ����, �ð��� ��õ� �ټ��� ����� �����ϴ� Ŭ����.
 * Author             : �����
 * Version            : 2.1.0
 * File name related  :
 * Class Name related :
 * Created Date       : 2004-02
 * Updated Date       : 2005-02
 * Last modifier      : �����
 * Updated content    :
 * License            :
 *==================================================================================*/
package com.optidpp.zrfc.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
// import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * ����, �ð��� ��õ� �ټ��� ����� �����ϴ� Ŭ����
 * <p>
 * �������� ������ ����
 * <pre>
 *	 Symbol   Meaning                 Presentation        Example
 *	 ------   -------                 ------------        -------
 *	 G        era designator          (Text)              AD
 *	 y        year                    (Number)            1996
 *	 M        month in year           (Text & Number)     July & 07
 *	 d        day in month            (Number)            10
 *	 h        hour in am/pm (1~12)    (Number)            12
 *	 H        hour in day (0~23)      (Number)            0
 *	 m        minute in hour          (Number)            30
 *	 s        second in minute        (Number)            55
 *	 S        millisecond             (Number)            978
 *	 E        day in week             (Text)              Tuesday
 *	 D        day in year             (Number)            189
 *	 F        day of week in month    (Number)            2 (2nd Wed in July)
 *	 w        week in year            (Number)            27
 *	 W        week in month           (Number)            2
 *	 a        am/pm marker            (Text)              PM
 *	 k        hour in day (1~24)      (Number)            24
 *	 K        hour in am/pm (0~11)    (Number)            0
 *	 z        time zone               (Text)              Pacific Standard Time
 *	 '        escape for text         (Delimiter)
 *	 ''       single quote            (Literal)           '
 *
 *  [����]
 *	 Format Pattern                         Result
 *	 --------------                         -------
 *	 "yyyyMMdd"                        ->>  19960710
 *	 "yyyy-MM-dd"                      ->>  1996-07-10
 *	 "HHmmss"                          ->>  210856
 *	 "HH:mm:ss"                        ->>  21:08:56
 *	 "hh:mm:ss"                        ->>  09:08:56
 *	 "yyyy.MM.dd hh:mm:ss"             ->>  1996.07.10 15:08:56
 *	 "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
 *	 "h:mm a"                          ->>  12:08 PM
 *	 "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
 *	 "K:mm a, z"                       ->>  0:00 PM, PST
 *	 "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM
 *
 * </pre>
 * ��Ÿ �ڼ��� ���� <a href="http://java.sun.com/j2se/1.3/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
 * Class API Document �� �����Ұ�
 *
 * @author YSS
 * @version 2.1.0
 */
public class DateUtil {

	/**
	 * <p>��¥ ǥ���� ���ڿ��� ����� ���� ��¥ ǥ�������� �ٲ��ִ� �޼ҵ��̴�. StringŸ���� ��¥ 8�ڸ���
	 * �Է¹޾Ƽ� yyyy-MM-dd �Ǵ� yyyy/MM/dd�� �ٲ��ش�.</p>
	 * @param date ��¥�� ǥ���� ���ڿ�(20011223�� ǥ���Ǿ�� �Ѵ�)
	 * @param
	 *		format ��¥�� �������<br>
	 *		����������δ� ������ ���� ���� ���ڷ� �־�� �Ѵ�(��ҹ��� �������� �ʴ´�).
	 *		<UL>
	 *		<li>"yyyy-MM-dd" : 2001-12-23�� ǥ��
	 *		<li>"yyyy/MM/dd" : 2001/12/23�� ǥ��
	 *		<li>"yyyy�� MM�� dd��" : 2001�� 12�� 23���� ǥ��
	 *		<li>"yyyy:MM:dd" : 2001:12:23�� ǥ��
	 *		</UL>
	 * @return
	 *  String ��ȯ�� ���ڿ�<br>
	 *  ���ڿ� date�� null�� ���<br>
	 * @throws IllegalDateLengthException ��¥ǥ�� ���ڿ��� 8�ڸ��� �ƴ� ���
	 * @throws IllegalFormatterException �������� �ʴ� ������ ����� ���
	 * @throws java.lang.NumberFormatException ��¥ǥ�� ���ڿ��� ���ڰ� �ƴ� ���� �� �ִ� ���
	 * </p>
	 */
	private static final Logger logger =     Logger.getLogger(DateUtil.class.getName());

	public static String dateFormat(String date, String format) throws NumberFormatException {
		if (date == null) return "";
		Long.parseLong(date);
		if (date.length() != 8) throw new NumberFormatException("��¥�� ǥ���� ���ڿ��� �ݵ�� 8�ڸ��̾�� �մϴ�.");

		int delimeter = 0;
		if (format.equalsIgnoreCase("yyyy-MM-dd"))
			delimeter = 0;
		else if (format.equalsIgnoreCase("yyyy/MM/dd"))
			delimeter = 1;
		else if (format.equalsIgnoreCase("yyyy�� MM�� dd��"))
			delimeter = 2;
		else if (format.equalsIgnoreCase("yyyy:MM:dd"))
			delimeter = 3;
		else if (format.equalsIgnoreCase("yyyy.MM.dd"))
			delimeter = 4;
		else
			throw new NumberFormatException("�������� �ʴ� ���������Դϴ�.");

		StringBuffer sb = new StringBuffer();

		switch(delimeter) {
		case 0 :
			sb.append(date.substring(0,4)).append("-");
			sb.append(date.substring(4,6)).append("-");
			sb.append(date.substring(6,8));
			break;
		case 1 :
			sb.append(date.substring(0,4)).append("/");
			sb.append(date.substring(4,6)).append("/");
			sb.append(date.substring(6,8));
			break;
		case 2 :
			sb.append(date.substring(0,4)).append("�� ");
			sb.append(date.substring(4,6)).append("�� ");
			sb.append(date.substring(6,8)).append("�� ");
			break;
		case 3 :
			sb.append(date.substring(0,4)).append(":");
			sb.append(date.substring(4,6)).append(":");
			sb.append(date.substring(6,8));
			break;
		case 4 :
			sb.append(date.substring(0,4)).append(".");
			sb.append(date.substring(4,6)).append(".");
			sb.append(date.substring(6,8));
			break;
		}
		return sb.toString();
	}

	/**
	 * <p>�ð� ǥ���� ���ڿ��� ����� ���� �ð� ǥ�������� �ٲ��ִ� �޼ҵ��̴�. StringŸ���� �ð� 6�ڸ���
	 * �Է¹޾Ƽ� hh:MM:ss �Ǵ� hh-MM-ss�� �ٲ��ش�.</p>
	 * @param time �ð��� ǥ���� ���ڿ�(153312�� ǥ���Ǿ�� �Ѵ�)
	 * @param
	 *		format �ð��� �������<br>
	 *		����������δ� ������ ���� ���� ���ڷ� �־�� �Ѵ�(��ҹ��� �������� �ʴ´�).
	 *		<UL>
	 *		<li>"hh:MM:ss" : 15:33:23�� ǥ��
	 *		<li>"hh/MM/ss" : 15/33/23�� ǥ��
	 *		<li>"hh�� MM�� ss��" : 15�� 33�� 23�ʸ� ǥ��
	 *		<li>"hh-MM-ss" : 15-33-23�� ǥ��
	 *		</UL>
	 * @return
	 *  String ��ȯ�� ���ڿ�<br>
	 *  ���ڿ� time�� null�� ���<br>
	 * @throws IllegalTimeLengthException ��¥ǥ�� ���ڿ��� 6�ڸ��� �ƴ� ���
	 * @throws IllegalFormatterException �������� �ʴ� ��������� ����� ���
	 * @throws java.lang.NumberFormatException ��¥ǥ�� ���ڿ��� ���ڰ� �ƴ� ���� �� �ִ� ���
	 * </p>
	 */
	public static String timeFormat(String time, String format) throws NumberFormatException {
		if (time == null) return "";
		Long.parseLong(time);
		if (time.length() != 6) throw new NumberFormatException("�ð��� ǥ���� ���ڿ��� �ݵ�� 6�ڸ��̾�� �մϴ�.");

		int delimeter = 0;
		if (format.equalsIgnoreCase("hh:MM:ss"))
			delimeter = 0;
		else if (format.equalsIgnoreCase("hh/MM/ss"))
			delimeter = 1;
		else if (format.equalsIgnoreCase("hh�� MM�� ss��"))
			delimeter = 2;
		else if (format.equalsIgnoreCase("hh-MM-ss"))
			delimeter = 3;
		else
			throw new NumberFormatException("�������� �ʴ� ���������Դϴ�.");

		StringBuffer sb = new StringBuffer();

		switch(delimeter) {
		case 0 :
			sb.append(time.substring(0,2)).append(":");
			sb.append(time.substring(2,4)).append(":");
			sb.append(time.substring(4,6));
			break;
		case 1 :
			sb.append(time.substring(0,2)).append("/");
			sb.append(time.substring(2,4)).append("/");
			sb.append(time.substring(4,6));
			break;
		case 2 :
			sb.append(time.substring(0,2)).append("�� ");
			sb.append(time.substring(2,4)).append("�� ");
			sb.append(time.substring(4,6)).append("�� ");
			break;
		case 3 :
			sb.append(time.substring(0,2)).append("-");
			sb.append(time.substring(2,4)).append("-");
			sb.append(time.substring(4,6));
			break;
		}
		return sb.toString();
	}

	/**
	 * <p>��¥�� �ð� ǥ���� ���ڿ��� ����� ���� ��¥�ð� ǥ�������� �ٲ��ִ� �޼ҵ��̴�. StringŸ���� ��¥�ð� 14�ڸ�(��¥ 8�ڸ��� �ð� 6�ڸ�)��
	 * �Է¹޾Ƽ� yyyy-MM-dd hh:MM:ss �Ǵ� yyyy:MM:dd hh:MM:ss�� �ٲ��ش�.</p>
	 * @param dateTime ��¥�� �ð��� ǥ���� ���ڿ�(20011223081235�� ǥ���Ǿ�� �Ѵ�)
	 * @param
	 *		dateFormatter ��¥�� �������<br>
	 *		����������δ� ������ ���� ���� ���ڷ� �־�� �Ѵ�(��ҹ��� �������� �ʴ´�).
	 *		<UL>
	 *		<li>"yyyy-MM-dd" : 2001-12-23�� ǥ��
	 *		<li>"yyyy/MM/dd" : 2001/12/23�� ǥ��
	 *		<li>"yyyy�� MM�� dd��" : 2001�� 12�� 23���� ǥ��
	 *		<li>"yyyy:MM:dd" : 2001:12:23�� ǥ��
	 *		</UL>
	 * @param
	 *		timeFormatter �ð��� �������<br>
	 *		����������δ� ������ ���� ���� ���ڷ� �־�� �Ѵ�(��ҹ��� �������� �ʴ´�).
	 *		<UL>
	 *		<li>"hh:MM:ss" : 15:33:23�� ǥ��
	 *		<li>"hh/MM/ss" : 15/33/23�� ǥ��
	 *		<li>"hh�� MM�� ss��" : 15�� 33�� 23�ʸ� ǥ��
	 *		<li>"hh-MM-ss" : 15-33-23�� ǥ��
	 *		</UL>
	 * @return
	 *  String ��ȯ�� ���ڿ�<br>
	 *  ���ڿ� dateTime�� null�� ���<br>
	 * @throws java.lang.NumberFormatException ��¥ �ð�ǥ�� ���ڿ��� ���ڰ� �ƴ� ���� �� �ִ� ���
	 * @throws IllegalLengthException ��¥�ð� ǥ�� ���ڿ��� 14�ڸ��� �ƴ� ���
	 * @throws IllegalFormatterException ��¥ �����̳� �ð� ������ �������� �ʴ� ���
	 * </p>
	 */
	public static String dateFormat(String dateTime, String dateFormatter, String timeFormatter) throws NumberFormatException {
		if (dateTime == null) return "";
		Long.parseLong(dateTime);
		if (dateTime.length() != 14) throw new NumberFormatException("��¥�� 8�ڸ�, �ð��� 6�ڸ��̾�� �մϴ�.");

		StringBuffer sb = new StringBuffer();
		sb.append(dateFormat(dateTime.substring(0,8),dateFormatter));
		sb.append(" ");
		sb.append(timeFormat(dateTime.substring(8,14),timeFormatter));

		return sb.toString();
	}

	/**
	 * <p>���� ��¥�� �ð��� �־��� Pattern�� ���缭 ��ȯ�Ѵ�.
	 * <BR>java.util.Date ��ü�� "2001/11/24, 01/11/17"�� ���� ��Ŀ� ���� ��ȯ�ÿ� ����Ѵ�.<br>
	 * ��ȯ�� �� �ִ� ����� �پ��� ���¸� ����� �� �ִ�.<br>
	 * <b>��, ������ ����� ��ҹ��ڸ� �����Ѵ�.</B> '��(M)'�� �ݵ�� �빮�ڷ� ǥ���Ѵ�.</p>
	 *	<UL>
	 *	<li>yy : �⵵. �ݵ�� �ҹ��ڷ� ǥ��
	 * 	<li>MM : ��. �ݵ�� �빮�ڷ� ǥ��
	 * 	<li>dd : ��. �ݵ�� �ҹ��ڷ� ǥ��
	 * 	<li>hh : �ð�. �ҹ��ڸ� 0-12��, �빮�ڸ� 0-23��
	 *  <li>kk : �ð�. 1-24��
	 * 	<li>mm : ��. �ݵ�� �ҹ��ڷ� ǥ��
	 * 	<li>ss : ��. �ݵ�� �ҹ��ڷ� ǥ��
	 *	</UL>
	 * ����� �� �ִ� Pattern�� ���ؼ��� Java API�� java.text.SimpleDateFormatŬ������ ����Ѵ�.<br>
	 * ������ ��� ���̴�.<br>
	 *	<UL>
	 * 	<li>yyyy-MM-dd, hh:mm:ss &gt;&gt; 2001-12-24, 01:15:23���� ǥ��
	 *	<li>yyyy-MM-dd, HH:mm:ss &gt;&gt; 2001-12-24, 13:15:23���� ǥ��
	 * 	<li>yyyy/MM/dd hh/mm/ss &gt;&gt; 2001/12/24 01/15/23���� ǥ��
	 * 	<li>yyyy-MM-dd &gt;&gt; 2001-12-24�� ǥ��
	 * 	<li>hh�� mm�� ss�� &gt;&gt; 01�� 15�� 23�ʷ� ǥ��
	 * 	<li>�ȳ��ϼ���. ������ yyyy�� MM�� dd�Դϴ�. ���� �ð��� hh�� mm�� ss���Դϴ�. &gt;&gt; �ȳ��ϼ���. ������ 2001�� 12�� 24���Դϴ�. ���� �ð��� 01�� 15�� 23���Դϴ�.
	 *	</UL>
	 * @param
	 *		pattern ��¥�� �ð��� ��ȯ�� ���<br>
	 *		pattern�� null�̰ų� ���ڿ��� ��쿡�� Default������ yyyyMMddHHmmss�� ����.
	 * @return
	 *  String ��ȯ�� ���ڿ�<br>
	 * @throws java.lang.IllegalArgumentException �������� ���ϴ� Pattern�� ����� ��� �߻��Ѵ�.
	 * </p>
	 */
	public static String getCurrentDateTime(String pattern) throws java.lang.IllegalArgumentException {
		if ((pattern == null) || (pattern.equals(""))) pattern = "yyyyMMddHHmmss";
		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(pattern);
		return formater.format(date);
	}

	/**
	 * Ư�� ��¥�� Ư�� ��Ŀ� ���缭 ��Ʈ������ �����Ѵ�. ����� <code>getCurrentDateTime(String)��
	 * Pattern�� ����Ѵ�.
	 *
	 * @param date Ư�� ��¥
	 * @param pattern ��¥�� �ð��� ��ȯ�� ���<br>
	 *				pattern�� null�̰ų� ���ڿ��� ��쿡�� Default������ yyyy-MM-dd�� ����.
	 * @return ��ȯ�� ���ڿ�
	 * @throws IllegalArgumentException �������� ���ϴ� Pattern�� ����� ��� �߻��Ѵ�.
	 */
	public static String getFomattedDateTime(java.util.Date date, String pattern) throws java.lang.IllegalArgumentException {
		if ((pattern == null) || (pattern.equals(""))) pattern = "yyyy-MM-dd";
		if (date == null) return null;
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(pattern);
		return formater.format(date);
	}

	/**
	 * <p>��¥ ����� �ٲ� �ִ� �Լ�</p>
	 * �Է� ������ �ִ� ��¥ ���� : yyyymmdd, yyyymm
	 *                               8        6
	 * @param dateValue String
	 * @param format String
	 * @return String ���˿� �°� ��ȯ�� ��¥ ��Ʈ��.
	 *                ��¥ ��� üũ�� ���� ���� ����. �������� ��¥�� ���´ٰ� �����ϰ� ��ȯ��
	 */
	public static String formatDate(String dateValue, String format) {
		try {
			if ( dateValue==null || dateValue.equals("") || format.equals("") ) {
				return "";
			}

			int len = dateValue.length();

			// ù��° �ɷ���
			if ( len!=8 && len!=6 ) {
				logger.debug(
						"���� �ȸ��� - �Է¹��� �� �ִ� �⺻����� �ƴ�. : " +
								dateValue);
				return dateValue;
			}

			char[] argArray  = findFormatInformation(format);
			char arrange   = argArray[0];
			char yearLen   = argArray[1];
			char monthLen  = argArray[2];
			char dayLen    = argArray[3];
			char delimiter = argArray[4];

			// �ι�° �ɷ���
			if ( len==6 && dayLen=='2' ) {
				logger.debug("'" + dateValue +
						"' ��¥�� ��� ����� ��¥�� �ִ� " + format +
						" ������� ��ȯ �� �� ����ϴ�.");
				return dateValue;
			} else if ( len==8 && dayLen=='0' ) {
				logger.debug("'" + dateValue +
						"' ��¥�� �ִ� ����� ��¥�� ��� " + format +
						" ������� ��ȯ �� �� ����ϴ�.");
				return dateValue;
			}

			String inYear  = null;
			String inMonth = null;
			String inDay   = null;

			switch (len) {
			case 8 :
				inYear  = dateValue.substring(0,4);
				inMonth = dateValue.substring(4,6);
				inDay   = dateValue.substring(6,8);
				break;
			case 6 :
				inYear  = dateValue.substring(0,4);
				inMonth = dateValue.substring(4,6);
				inDay   = "";
				break;
			default :
				inYear  = "";
				inMonth = "";
				inDay   = "";
			}

			if ( yearLen=='2' ) {
				inYear = inYear.substring(2, 4);
			}

			if ( monthLen=='3' ) {
				switch (Integer.parseInt(inMonth)) {
				case  1 :    inMonth = "JAN";    break;
				case  2 :    inMonth = "FEB";    break;
				case  3 :    inMonth = "MAR";    break;
				case  4 :    inMonth = "APR";    break;
				case  5 :    inMonth = "MAY";    break;
				case  6 :    inMonth = "JUN";    break;
				case  7 :    inMonth = "JUL";    break;
				case  8 :    inMonth = "AUG";    break;
				case  9 :    inMonth = "SEP";    break;
				case 10 :    inMonth = "OCT";    break;
				case 11 :    inMonth = "NOV";    break;
				case 12 :    inMonth = "DEC";    break;
				default :    inMonth = "mmm";
				}
				if ( inMonth.equals("mmm") ) {
					logger.debug("�� �߸�� ���� �������. : " + dateValue);
					return dateValue;
				}
			}

			String newDate = null;
			String delimiterStr = (new Character(delimiter)).toString().trim();

			switch (arrange) {
			case 'y' :
				if ( dayLen=='0' ) {
					newDate = inYear  + delimiterStr + inMonth;
				} else {
					newDate = inYear  + delimiterStr + inMonth + delimiterStr + inDay ;
				}
				break;
			case 'm' :
				if ( dayLen=='0' ) {
					newDate = inMonth + delimiterStr + inYear;
				} else {
					newDate = inMonth + delimiterStr + inDay   + delimiterStr + inYear;
				}
				break;
			case 'd' :
				newDate = inDay   + delimiterStr + inMonth + delimiterStr + inYear;
				break;
			}

			return newDate;

		} catch (Exception e) {
			//System.out.println("[CMUtilDate.scmFormatDate()] �Ƹ��� ��¥ ����� ���� �ʴ� ���� ���... ������ �׷��� �̻� ������ ���� �ٶ�... ");
			//System.out.println("[CMUtilDate.scmFormatDate()] Exception : " + e.getMessage());
			return dateValue;
		}
	}

	/**
	 * ��¥ ����� �⺻ ������� �ٲ� �ִ� �Լ� ("-", ".", "/"�� ����)
	 *
	 * @param dateValue String
	 * @param format String
	 * @return String yyyymmdd, yyyymm����� ��ȯ�� ��¥ ��Ʈ��. ��¥ ��� üũ�� ���� ����.
	 *                �������� ��¥�� ���´ٰ� �����ϰ� ��ȯ��
	 */
	public static String formatDateToDefault(String dateValue, String format){
		try {
			if ( dateValue==null || dateValue.equals("") || format.equals("") ) {
				return "";
			}

			if ( dateValue.length()<4 || 11<dateValue.length() ) {
				logger.debug("���� �ȸ��� - �Է¹��� �� �ִ� ����� ���̰� �ƴ�");
				return dateValue;
			}

			char[] argArray = findFormatInformation(format);
			char arrange   = argArray[0];
			char yearLen   = argArray[1];
			char monthLen  = argArray[2];
			char dayLen    = argArray[3];
			char delimiter = argArray[4];

			dateValue = delDelimiter(dateValue, delimiter);

			int yearStart  = 0;
			int monthStart = 0;
			int dayStart   = 0;

			switch (arrange) {
			case 'y':
				yearStart = 0;
				monthStart = Character.getNumericValue(yearLen);
				dayStart = Character.getNumericValue(yearLen) + Character.getNumericValue(monthLen);
				break;
			case 'm':
				monthStart = 0;
				dayStart = Character.getNumericValue(monthLen);
				yearStart = Character.getNumericValue(monthLen) + Character.getNumericValue(dayLen);
				break;
			case 'd':
				dayStart = 0;
				monthStart = Character.getNumericValue(dayLen);
				yearStart = Character.getNumericValue(dayLen) + Character.getNumericValue(monthLen);
				break;
			}

			String newYear  = dateValue.substring(yearStart , yearStart  + Character.getNumericValue(yearLen)  );
			String newMonth = dateValue.substring(monthStart, monthStart + Character.getNumericValue(monthLen) );
			String newDay   = dateValue.substring(dayStart  , dayStart   + Character.getNumericValue(dayLen)   );


			if ( yearLen=='2' ) {
				int newYearInt = Integer.parseInt(newYear);
				if ( 0<=newYearInt && newYearInt<50 ) {     // 00 ~ 49
					newYearInt += 2000;
				} else if ( 50<=newYearInt && newYearInt <100 ) {     // 50 ~ 99
					newYearInt += 1900;
				}
				newYear = newYearInt+"";
			}

			if ( monthLen=='3' ) {
				if ( newMonth.equals("JAN") ) {    newMonth = "01";
				} else if ( newMonth.equals("FEB") ) {    newMonth = "02";
				} else if ( newMonth.equals("MAR") ) {    newMonth = "03";
				} else if ( newMonth.equals("APR") ) {    newMonth = "04";
				} else if ( newMonth.equals("MAY") ) {    newMonth = "05";
				} else if ( newMonth.equals("JUN") ) {    newMonth = "06";
				} else if ( newMonth.equals("JUL") ) {    newMonth = "07";
				} else if ( newMonth.equals("AUG") ) {    newMonth = "08";
				} else if ( newMonth.equals("SEP") ) {    newMonth = "09";
				} else if ( newMonth.equals("OCT") ) {    newMonth = "10";
				} else if ( newMonth.equals("NOV") ) {    newMonth = "11";
				} else if ( newMonth.equals("DEC") ) {    newMonth = "12";
				} else                               {    newMonth = "99";
				}
			}

			return newYear + newMonth + newDay;

		} catch (Exception e) {
			logger.debug("[CMUtilDate.scmFormatDateToDefault()] �Ƹ��� ��¥ ����� ���� �ʴ� ���� ���... ������ �׷��� �̻� ������ ���� �ٶ�... ");
			logger.debug("[CMUtilDate.scmFormatDateToDefault()] Exception : " + e.getMessage());
			return dateValue;
		}
	}

	/**
	 * ��¥ ����� minor code�� ������ �� ��Ŀ� ���� ������ �˷���
	 *
	 * @param format minor code
	 * @return char[] (arrange, yearLen, monthLen, dayLen, delimiter )
	 */
	public static char[] findFormatInformation(String format) {
		char minor1 = format.charAt(0);
		char minor2 = format.charAt(1);

		char arrange;
		char yearLen;
		char monthLen;
		char dayLen;
		char delimiter;

		char[] argArray = new char[5];

		switch (minor1) {
		case 'A':    arrange  = 'y';    yearLen  = '4';    monthLen = '2';    dayLen  = '2';    break;
		case 'B':    arrange  = 'y';    yearLen  = '4';    monthLen = '3';    dayLen  = '2';    break;
		case 'C':    arrange  = 'y';    yearLen  = '2';    monthLen = '2';    dayLen  = '2';    break;
		case 'D':    arrange  = 'y';    yearLen  = '2';    monthLen = '3';    dayLen  = '2';    break;

		case 'E':    arrange  = 'y';    yearLen  = '4';    monthLen = '2';    dayLen  = '0';    break;
		case 'F':    arrange  = 'y';    yearLen  = '4';    monthLen = '3';    dayLen  = '0';    break;
		case 'G':    arrange  = 'y';    yearLen  = '2';    monthLen = '2';    dayLen  = '0';    break;
		case 'H':    arrange  = 'y';    yearLen  = '2';    monthLen = '3';    dayLen  = '0';    break;

		case 'I':    arrange  = 'm';    yearLen  = '4';    monthLen = '2';    dayLen  = '2';    break;
		case 'J':    arrange  = 'm';    yearLen  = '4';    monthLen = '3';    dayLen  = '2';    break;
		case 'K':    arrange  = 'm';    yearLen  = '2';    monthLen = '2';    dayLen  = '2';    break;
		case 'L':    arrange  = 'm';    yearLen  = '2';    monthLen = '3';    dayLen  = '2';    break;

		case 'M':    arrange  = 'm';    yearLen  = '4';    monthLen = '2';    dayLen  = '0';    break;
		case 'N':    arrange  = 'm';    yearLen  = '4';    monthLen = '3';    dayLen  = '0';    break;
		case 'O':    arrange  = 'm';    yearLen  = '2';    monthLen = '2';    dayLen  = '0';    break;
		case 'P':    arrange  = 'm';    yearLen  = '2';    monthLen = '3';    dayLen  = '0';    break;

		case 'Q':    arrange  = 'd';    yearLen  = '4';    monthLen = '2';    dayLen  = '2';    break;
		case 'R':    arrange  = 'd';    yearLen  = '4';    monthLen = '3';    dayLen  = '2';    break;
		case 'S':    arrange  = 'd';    yearLen  = '2';    monthLen = '2';    dayLen  = '2';    break;
		case 'T':    arrange  = 'd';    yearLen  = '2';    monthLen = '3';    dayLen  = '2';    break;
		default :    arrange  = 'y';    yearLen  = '4';    monthLen = '2';    dayLen  = '2';    break;
		}

		switch (minor2) {
		case '1':    delimiter = ' ';     break;
		case '2':    delimiter = '-';    break;
		case '3':    delimiter = '/';    break;
		case '4':    delimiter = '.';    break;
		default :    delimiter = ' ';
		}

		argArray[0] = arrange;
		argArray[1] = yearLen;
		argArray[2] = monthLen;
		argArray[3] = dayLen;
		argArray[4] = delimiter;

		return argArray;
	}


	/*
	 * �Է¹��� ��¥�� delimiter�� �����Ѵ�.
	 *
	 * @param date ��¥��
	 * @param delimiter char �𸮹���
	 * @return String �𸮹��Ͱ� ���ŵ� ��¥ ��
	 */
	private static String delDelimiter(String date, char delimiter) {
		String arg = date.trim();
		int len = arg.length();
		char[] tempChar = new char[len];
		int j=0;

		for ( int i=0 ; i<len ; i++ ) {
			if ( arg.charAt(i) == delimiter || arg.charAt(i) == ' ' ){
				continue;
			} else {
				tempChar[j] = arg.charAt(i);
				j++;
			}
		}
		return new String(tempChar).trim();
	}

	/*
	 * �Է¹��� ��¥�� ��Ʈ�� ������ Hyphen(-, ., /)�� �����Ѵ�.
	 *
	 * @param date ��¥��
	 * @return String Hyphen�� ���ŵ� ��¥ ��
	 */
	private static String delHyphen(String arg) {
		arg = arg.trim();
		int len = arg.length();
		char[] tempChar = new char[len];
		int j=0;

		for (int i=0 ; i<len ; i++){
			if (arg.charAt(i) == '-' || arg.charAt(i) == '.'
					|| arg.charAt(i) == '/' || arg.charAt(i) == ' ' ){
				continue;
			}else{
				tempChar[j] = arg.charAt(i);
				j++;
			}
		}
		return new String(tempChar).trim();
	}

	/**
	 * �Է¹��� ������ �ִ� ���̸� ��ȯ�Ѵ�.
	 *
	 * @param format ����
	 * @return int �ִ����
	 */
	private static int getMaxLength(String format) {
		if(format==null || format.equals("")) {
			return 11;
		}

		char[] argArray = findFormatInformation(format);
		char arrange   = argArray[0];
		char yearLen   = argArray[1];
		char monthLen  = argArray[2];
		char dayLen    = argArray[3];
		char delimiter = argArray[4];

		int delimiterLength = delimiter==' ' ? 0 : 1;

		int maxLength =   Character.getNumericValue(yearLen) + Character.getNumericValue(monthLen)
				+ Character.getNumericValue(dayLen)  + delimiterLength;

		return maxLength;
	}

	/**
	 * �� �ڸ����� ���ڸ� �� �ڸ� String���� ��ȯ
	 *
	 * @param arg int
	 * @return   String date
	 */
	public static String addZero(int arg) {
		String date = arg + "";
		if ( date.length()==1 ) {
			return "0"+date;
		} else {
			return date;
		}
	}

	/**
	 * �� �ڸ� String�� �� �ڸ����� ���ڷ� ��ȯ
	 *
	 * @param arg String
	 * @return  int date
	 */
	public static int delZero(String arg) {
		if ( arg==null ) {
			return 0;
		} else {
			return Integer.parseInt(arg);
		}
	}

	/**
	 * ���� ��¥�� return ����
	 *
	 * @return String ���糯¥ "20000910"
	 */
	public static String getToDay() {
		Calendar cal = Calendar.getInstance();
		int year, month, day;
		String today;

		year = cal.get(Calendar.YEAR);
		month= cal.get(Calendar.MONTH)+1; // calendar class�� MONTH�� 0�� 1���̹Ƿ� +1
		day  = cal.get(Calendar.DATE);

		if (month < 10) {
			today = Integer.toString( year )  + "0" + month;
		} else {
			today = Integer.toString( year )  + month;
		}

		if (day < 10) {
			today = today + "0" + day;
		} else {
			today = today + day;
		}

		return today;
	}

	/**
	 *���� ��¥�� return ����
	 *
	 * @return String ����� 01 String "20000901"
	 */
	public static String getFirstDay() {
		Calendar cal = Calendar.getInstance();
		int year, month, day;
		String today;

		year = cal.get(Calendar.YEAR);
		month= cal.get(Calendar.MONTH)+1; // calendar class�� MONTH�� 0�� 1���̹Ƿ� +1
		day  = cal.get(Calendar.DATE);

		if (month < 10) {
			today = Integer.toString( year )  + "0" + month;
		} else {
			today = Integer.toString( year )  + month;
		}

		today = today + "01";

		return today;
	}

	/**
	 * ���� �ð��� return ����
	 *
	 * @return String : ���� �ð� "HHMMSS"
	 */
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		String currentTime = null;
		String hour, min, sec ;

		int h = cal.get(Calendar.HOUR_OF_DAY) ;
		int m = cal.get(Calendar.MINUTE) ;
		int s = cal.get(Calendar.SECOND);

		if ( h <10) {
			hour = "0" + Integer.toString(h) ;
		} else {
			hour = Integer.toString(h) ;
		}

		if (m <10) {
			min = "0" + Integer.toString(m) ;
		} else {
			min = Integer.toString(m) ;
		}

		if (s <10) {
			sec = "0" + Integer.toString(s) ;
		} else {
			sec = Integer.toString(s) ;
		}


		currentTime = ""+hour + min + sec;

		return currentTime;     // HHMMSS
	}

	/**
	 * ���� �ð��� Millisecond return ����
	 *
	 * @return String : ���� �ð�
	 */
	public static String getDateTimeInMillis() {
		java.util.Date curDate = new java.util.Date();
		long currentMillis = curDate.getTime();


		return Long.toString(currentMillis);
	}

	/**
	 * ���� �ð��� return ����
	 *
	 * @return String ���� �ð� "HHMM"
	 */
	public static String getCurrentTimeS() {
		Calendar cal = Calendar.getInstance();
		String currentTime = null;
		String hour, min;

		int h = cal.get(Calendar.HOUR_OF_DAY) ;
		int m = cal.get(Calendar.MINUTE) ;

		if ( h <10) {
			hour = "0" + Integer.toString(h) ;
		} else {
			hour = Integer.toString(h) ;
		}

		if (m <10) {
			min = "0" + Integer.toString(m) ;
		} else {
			min = Integer.toString(m) ;
		}

		currentTime = ""+hour + min ;

		return currentTime;     // HHMM
	}

	/**
	 * �Է��� ���� �ϼ��� ��ȯ
	 *
	 * @param aMonth "200009"
	 * @return int	�Է´��� �ϼ�
	 */
	public static int getLastDayOfMonth ( String aMonth ) {

		int year, month;

		year = Integer.parseInt(aMonth.substring(0, 4));
		month= Integer.parseInt(aMonth.substring(4, 6));

		if(month < 1 || month > 12) {
			return 0;
		}

		if(month == 2) {
			if ( ((year%4 == 0) && (year%100 != 0)) || (year%400 == 0) ) {
				return 29;
			}
			else {
				return 28;
			}
		}
		else if(month==4||month==6||month==9||month==11) {
			return 30;
		}
		else {
			return 31;
		}
	}

	/**
	 * �Է¹��� �Ϸκ��� x���ĸ� ����Ͽ� return
	 *
	 * @param date "20000908"
	 * @param interval ���ϰ��� �ϴ� ����
	 * @return String - date �Ϸκ��� interval ���� ��¥
	 */
	public static String getRelativeDate(String date, int interval) {
		String relativeDate;
		int year, month, day;

		year = Integer.parseInt(date.substring(0, 4));
		month= Integer.parseInt(date.substring(4, 6));
		day  = Integer.parseInt(date.substring(6, 8));

		Calendar cal = Calendar.getInstance();
		cal.set( year, month-1, day );
		cal.add( Calendar.DATE, interval );

		year = cal.get( Calendar.YEAR );
		month= cal.get( Calendar.MONTH ) + 1;
		day  = cal.get( Calendar.DATE );

		if( month < 10 ) {
			relativeDate = Integer.toString(year) + "0" + month;
		} else {
			relativeDate = Integer.toString(year) + month;
		}

		if( day < 10 ) {
			relativeDate = relativeDate + "0" + day;
		} else {
			relativeDate = relativeDate + day;
		}

		return relativeDate;
	}

	/**
	 *���� �ð����κ��� x�ð��ĸ� ����Ͽ� return
    <br>
	 *@param interval ���ϰ��� �ϴ� �ð�
	 *@return String - ���ð����κ��� interval �� �ð�
	 */
	public static String getRelativeTime(int interval) {
		String relativeTime;
		int hh = 0;
		String mm;

		hh = Integer.parseInt(getCurrentTime().substring(0, 2));
		mm= getCurrentTime().substring(2, 4);

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR_OF_DAY, hh);
		cal.add( Calendar.HOUR_OF_DAY, interval );

		hh = cal.get( Calendar.HOUR_OF_DAY );

		if( hh < 10 ) {
			relativeTime =  "0" + Integer.toString(hh)+mm;
		} else {
			relativeTime = Integer.toString(hh)+mm ;
		}

		return relativeTime;
	}

	/**
	 * �־��� ��¥ ���̿� ��⵵ ����� ����ִ����� �迭�� ����
	 *
	 * @param fromDate "20000710"
	 * @param endDate	"20001111"
	 * @return String[] - fromDate, endDate ���̿� ����ִ� �⵵, ��
	 *         ex) "200008","200009","200010"
	 */
	public static String[] getMonthsDuringPeriod(String fromDate, String endDate) {
		// String('yyyymm') Vector
		Vector v = new Vector();
		String[] months = null;

		int fromYYYY, fromMM, endYYYY, endMM, temp;
		String month, from;

		fromYYYY= Integer.parseInt(fromDate.substring(0,4));
		fromMM  = Integer.parseInt(fromDate.substring(4,6));
		endYYYY = Integer.parseInt(endDate.substring(0,4));
		endMM   = Integer.parseInt(endDate.substring(4,6));

		if ((fromMM>12)||(endMM>12)||(fromMM<1)||(endMM<1)) {
			return null;
		}

		/* fromDate�� endDate���� Ŭ ��� swap���ִ� ��ƾ */
		if (fromDate.compareTo(endDate) > 0) {
			temp = fromYYYY;
			fromYYYY = endYYYY;
			endYYYY = temp;
			temp = fromMM;
			fromMM = endMM;
			endMM = temp;
		}

		v.addElement( fromDate.substring(0,6) );

		while ((fromYYYY < endYYYY) || (fromMM < endMM)) {
			if (fromMM == 12) {
				fromYYYY++;
				fromMM = 1;
			} else {
				fromMM++;
			}

			if (fromMM < 10) {
				month = Integer.toString(fromYYYY) + "0" + fromMM;
			} else {
				month = Integer.toString(fromYYYY) + fromMM;
			}

			v.addElement( month );
		}

		months = new String[v.size()];
		v.copyInto(months);

		return months;
	}

	/**
	 * �־��� ��¥ ���̿� ��⵵ ��� ���ְ� ����ִ����� ���ͷ� ����
	 *
	 * @param fromDate "20000710"
	 * @param endDate	"20001111"
	 * @return String[] fromDate, endDate ���̿� ����ִ� �⵵, ��, ��
	 */
	public static String[] getWeeksDuringPeriod(String fromDate, String endDate) {
		// 'yyyymmw' Vector
		Vector v = new Vector();
		String[] weeks = null;

		String[] monthsDuringPeriod = null;

		int year, month, day;
		int lastDayOfMonth, lastWeekOfMonth;

		Calendar cal = Calendar.getInstance();
		// �� �޿� �� �ְ� �ִ����� ã�� ��ƾ

		year = Integer.parseInt(fromDate.substring(0, 4));
		month= Integer.parseInt(fromDate.substring(4, 6));
		day  = Integer.parseInt(fromDate.substring(6, 8));

		lastDayOfMonth = getLastDayOfMonth(fromDate.substring(0, 6));
		cal.set(year, month-1, lastDayOfMonth);
		lastWeekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);

		cal.set(year, month-1, day);

		for( int i=cal.get(Calendar.WEEK_OF_MONTH); i <= lastWeekOfMonth; i++ ) {
			v.addElement( fromDate.substring(0, 6) + i );
		}

		monthsDuringPeriod = getMonthsDuringPeriod( fromDate, endDate );

		for( int i=0 ; i < monthsDuringPeriod.length ; i++ ) {
			year = Integer.parseInt((monthsDuringPeriod[i]).substring(0, 4));
			month= Integer.parseInt((monthsDuringPeriod[i]).substring(4, 6));

			lastDayOfMonth = getLastDayOfMonth((monthsDuringPeriod[i]).substring(0, 6));
			cal.set(year, month-1, lastDayOfMonth);
			lastWeekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);

			for( int j=1; j <= lastWeekOfMonth; j++ ) {
				v.addElement((monthsDuringPeriod[i]).substring(0, 6) + i );
			}
		}

		year = Integer.parseInt(endDate.substring(0, 4));
		month= Integer.parseInt(endDate.substring(4, 6));
		day  = Integer.parseInt(fromDate.substring(6, 8));

		cal.set(year, month-1, day);
		lastWeekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);

		for( int i=1 ; i <= lastWeekOfMonth; i++ ) {
			v.addElement( endDate.substring(0, 6) + i );
		}

		weeks = new String[v.size()];
		v.copyInto(weeks);

		return weeks;
	}

	/**
	 * ����Ŭ�� months_between�� ���� ����
	 *
	 * @param fromDate "20000710"
	 * @param endDate	"20001111"
	 * @return float - Month Between
	 */
	public static float getMonthsBetweenPeriod(String fromDate, String endDate) {
		Calendar cal = Calendar.getInstance();
		int fromYear, fromMonth, fromDay, toYear, toMonth, toDay, lastDate;
		float monthsBetweenPeriod;

		fromYear = Integer.parseInt(fromDate.substring(0, 4));
		fromMonth= Integer.parseInt(fromDate.substring(4, 6));
		fromDay  = Integer.parseInt(fromDate.substring(6, 8));

		toYear = Integer.parseInt(endDate.substring(0, 4));
		toMonth= Integer.parseInt(endDate.substring(4, 6));
		toDay  = Integer.parseInt(endDate.substring(6, 8));

		lastDate = getLastDayOfMonth( fromDate );
		monthsBetweenPeriod = (lastDate - fromDay - 1) / lastDate;

		monthsBetweenPeriod += 12-fromMonth + (toYear-fromYear-1)*12 + toMonth-1;

		lastDate = getLastDayOfMonth( endDate );
		monthsBetweenPeriod += toDay / lastDate;

		return monthsBetweenPeriod;
	}

	/**
	 * �� �Ⱓ������ ����
	 *
	 * @param fromDate "20000710"
	 * @param toDate	"20001111"
	 * @return float Month Between days
	 */
	public static int getDaysBetweenPeriod(String fromDate, String toDate) {
		Calendar cal = Calendar.getInstance();
		int fromYear, fromMonth, fromDay, toYear, toMonth, toDay, curDay, year;
		int daysBetweenPeriod;
		int temp;

		fromYear = Integer.parseInt(fromDate.substring(0, 4));
		fromMonth= Integer.parseInt(fromDate.substring(4, 6));
		fromDay  = Integer.parseInt(fromDate.substring(6, 8));

		toYear = Integer.parseInt(toDate.substring(0, 4));
		toMonth= Integer.parseInt(toDate.substring(4, 6));
		toDay  = Integer.parseInt(toDate.substring(6, 8));

		/* fromDate�� toDate���� Ŭ ��� swap���ִ� ��ƾ */
		if (fromDate.compareTo(toDate) > 0) {
			temp = fromYear;
			fromYear = toYear;
			toYear = temp;
			temp = fromMonth;
			fromMonth = toMonth;
			toMonth = temp;
			temp = fromDay;
			fromDay = toDay;
			toDay = temp;
		}

		cal.set( toYear, toMonth-1, toDay );
		daysBetweenPeriod = cal.get( Calendar.DAY_OF_YEAR );

		cal.set( fromYear, fromMonth-1, fromDay );
		curDay = cal.get( Calendar.DAY_OF_YEAR );

		if (toYear==fromYear) {
			daysBetweenPeriod -= curDay;
		} else {
			if ( ((fromYear%4 == 0) && (fromYear%100 != 0)) || (fromYear%400 == 0) ) {
				daysBetweenPeriod += 365 - curDay;
			} else {
				daysBetweenPeriod += 364 - curDay;
			}

			for( year=fromYear+1; year < toYear; year++ ) {
				if ( ((year%4 == 0) && (year%100 != 0)) || (year%400 == 0) ) {
					daysBetweenPeriod += 366;
				} else {
					daysBetweenPeriod += 365;
				}
			}
		}

		return daysBetweenPeriod;
	}

	/**
	 * ���ڿ��� ���� ���ڰ����� ����
	 *
	 * @param textDate ���ڰ��� ���� 8�ڸ� ���ڿ� ��) '20010806'
	 * @return ���ڰ��̸� true, �ƴϸ� false
	 */
	public static boolean isDate( String textDate ){
		try {
			dateCheck(textDate);
		} catch ( Exception e ) {
			return false;
		}
		return true;
	}

	// �������� Date Value Check�� ��
	private static void dateCheck( String textDate ) throws Exception{
		if ( textDate.length() != 8 )
			throw new Exception("[" + textDate + "] is not date value");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			sdf.setLenient( false );
			java.util.Date dt = sdf.parse(textDate);
		}catch(Exception e) {
			throw new Exception("[" + textDate + "] is not date value");
		}
		return;
	}

	/**
	 * ���ڰ��� ���� 8�ڸ� ���ڿ��� Calendar ��ü�� ��
	 *
	 * @param textDate ���ڰ��� ���� 8�ڸ� ���ڿ� ��) '20010806'
	 * @return Calendar ��ü
	 */
	public static Calendar getCalendar(String textDate) throws Exception{
		dateCheck(textDate);
		int year = Integer.parseInt(textDate.substring(0,4));
		int month = Integer.parseInt(textDate.substring(4,6));
		int date = Integer.parseInt(textDate.substring(6));
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		cal.set(year,month-1,date);
		return cal;
	}

	/**
	 * ���ڰ��� ���� 8�ڸ� ���ڿ��� Date ��ü�� ��
	 * @param textDate ���ڰ��� ���� 8�ڸ� ���ڿ� ��) '20010806'
	 * @return Date ��ü
	 */
	public static java.util.Date getDate(String textDate) throws Exception{
		return getCalendar(textDate).getTime();
	}

	/**
	 * �־��� Date ��ü�� �̿��Ͽ� �־��� ���� ��¥���� ���ڿ��� ����.
	 *
	 * @param date ���ϴ� ������ Date ��ü
	 * @param pattern ���ϴ� ���� ����
	 * @return �־��� ������ ����
	 */
	public static String getDateString(java.util.Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * �־��� Date ��ü�� �̿��Ͽ� �⺻��¥��('yyyyMMdd')�� ���ڿ��� ����.
	 *
	 * @param date ���ϴ� ������ Date ��ü
	 * @return �־��� ������ ����
	 */
	public static String getDateString(java.util.Date date) throws Exception{
		return getDateString(date, "yyyyMMdd");
	}

	/**
	 * �־��� ���ڸ� �̿��Ͽ� �־��� ���� ��¥���� ���ڿ��� ����.
	 *
	 * @param textDate ���ڰ��� ���� 8�ڸ� ���ڿ� ��) '20010806'
	 * @param pattern ���ϴ� ���� ����
	 * @return �־��� ������ ����
	 */
	public static String getDateString(String textDate, String pattern)  throws Exception{
		return getDateString(getDate(textDate), pattern);
	}

	/**
	 * �־��� ���� ��¥�� �ý������ڸ� ����
	 *
	 * @param pattern ���ϴ� ���� ����
	 * @return �ý��� ����
	 */
	public static String getToday(String pattern){
		return getDateString(new java.util.Date(), pattern);
	}

	/**
	 * �⺻����('yyyyMMdd') ��¥�� �ý������ڸ� ����
	 *
	 * @return �⺻��('yyyyMMdd')�� �ý��� ����
	 */
	public static String getToday(){
		return getToday("yyyyMMdd");
	}

	/**
	 * �⺻����('HHmmss') ��¥�� �ý��۽ð��� ����
	 *
	 * @return �⺻��('HHmmss')�� �ý��� �ð�
	 */
	public static String getTime(){
		return getToday("HHmmss");
	}

	/**
	 * ������ �и��ڸ� �̿��� �ý������ڸ� ����
	 *
	 * @param delmt ���ϴ� �и��� ���� ��) ':', '/' ...
	 * @return �и��ڰ� ���Ե� �ý��� �ð�
	 */
	public static String getTime(char delmt){
		return getToday("HH" + delmt + "mm" + delmt + "ss");
	}

	/**
	 * ������ ���ڷ� ���� �����Ⱓ ������ ���ڸ� ����
	 *
	 * @param fromDate ��������
	 * @param termDays ���ϴ� �Ⱓ
	 * @param both ����ֱ� ����
	 * @return �����Ⱓ ������ ���� ('yyyyMMdd')
	 */
	public static String getToDate(String fromDate, int termDays, boolean both) throws Exception{
		if (both) termDays = termDays - 1;
		Calendar cal = getCalendar(fromDate);
		cal.add(Calendar.DATE, termDays);
		return getDateString(cal.getTime(),"yyyyMMdd");
	}

	/**
	 * ������ ���ڷ� ���� �����Ⱓ ������ ���ڸ� ����ֱ������� ����.
	 *
	 * @param fromDate ��������
	 * @param termDays ���ϴ� �Ⱓ
	 * @return �����Ⱓ ������ ���� ('yyyyMMdd')
	 */
	public static String getToDate(String fromDate, int termDays) throws Exception{
		return getToDate(fromDate, termDays, false);
	}

	/**
	 * �����Ϸκ��� �����ϱ����� �ϼ��� ����
	 *
	 * @param fromDate ��������
	 * @param toDate ��������
	 * @param both ����ֱ� ����
	 * @return �������ڷ� ���� �����ϱ����� �ϼ�
	 */
	public static int getDiffDays(java.util.Date fromDate, java.util.Date toDate, boolean both){
		long diffDays = toDate.getTime() - fromDate.getTime();
		long days = diffDays / (24 * 60 * 60 * 1000);
		if (both){
			if (days >= 0) days += 1; else days -= 1;
		}
		return new Long(days).intValue();
	}

	/**
	 * �����Ϸκ��� �����ϱ����� �ϼ��� ����ֱ�� �����.
	 *
	 * @param fromDate ��������
	 * @param toDate ��������
	 * @return �������ڷ� ���� �����ϱ����� �ϼ�
	 */
	public static int getDiffDays(java.util.Date fromDate, java.util.Date toDate){
		return getDiffDays(fromDate, toDate, false);
	}

	/**
	 * �����Ϸκ��� �����ϱ����� �ϼ��� ����
	 *
	 * @param fromDate ��������
	 * @param toDate ��������
	 * @param both ����ֱ� ����
	 * @return �������ڷ� ���� �����ϱ����� �ϼ�
	 */
	public static int getDiffDays(String fromDate, String toDate, boolean both) throws Exception{
		return getDiffDays(getDate(fromDate),getDate(toDate), both);
	}

	/**
	 * �����Ϸκ��� �����ϱ����� �ϼ��� ����ֱ�� �����.
	 *
	 * @param fromDate ��������
	 * @param toDate ��������
	 * @return �������ڷ� ���� �����ϱ����� �ϼ�
	 */
	public static int getDiffDays(String fromDate, String toDate) throws Exception{
		return getDiffDays(getDate(fromDate),getDate(toDate), false);
	}

	/**
	 * �����Ϸκ��� ��¥ �����Լ�
	 *
	 * @param datestr ��������(yyyyMMdd)
	 * @param flag (+,-)
	 * @param num ������,������
	 * @return �������ڷ� ���� �����ϱ����� �ϼ�
	 */

	public static String getadddate(String datestr,String flag, int num){

		int iyyyy = Integer.parseInt(datestr.substring(0,4));
		int imm	  = Integer.parseInt(datestr.substring(4,6));
		int idd	  = Integer.parseInt(datestr.substring(6,8));

		Calendar cal = Calendar.getInstance();
		if(flag.equals("+")){
			cal.set(iyyyy,imm-1,idd+num);
		}else if(flag.equals("-")){
			cal.set(iyyyy,imm-1,idd-num);
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		return sdf.format(cal.getTime());
	}

	public static String getaddyear(String datestr,String flag, int num){

		int iyyyy = Integer.parseInt(datestr.substring(0,4));
		int imm	  = Integer.parseInt(datestr.substring(4,6));
		int idd	  = Integer.parseInt(datestr.substring(6,8));

		Calendar cal = Calendar.getInstance();
		if(flag.equals("+")){
			cal.set(iyyyy+num,imm-1,idd+num);
		}else if(flag.equals("-")){
			cal.set(iyyyy-num,imm-1,idd-num);
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		return sdf.format(cal.getTime());
	}

	public static String getaddmonth(String datestr,String flag, int num){

		int iyyyy = Integer.parseInt(datestr.substring(0,4));
		int imm	  = Integer.parseInt(datestr.substring(4,6));
		int idd	  = Integer.parseInt(datestr.substring(6,8));

		Calendar cal = Calendar.getInstance();
		if(flag.equals("+")){
			cal.set(iyyyy,imm-1+num,idd);
		}else if(flag.equals("-")){
			cal.set(iyyyy,imm-1-num,idd);
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		return sdf.format(cal.getTime());
	}

	/**
	 * <p>��¥ ����� �ٲ� �ִ� �Լ�</p>
	 * �Է� ������ �ִ� ��¥ ���� : yyyy/mm/dd, yyyy.mm.dd �ּ�
	 *
	 * @param date String
	 * @param delimiter ���ڱ����� ��) ':', '/' ...    *
	 * @param outPattern ��� ������� ���� �⺻�� : yyyy-MM-dd
	 * @return String ���˿� �°� ��ȯ�� ��¥ ��Ʈ��.
	 *                ���������� üũ��
	 */
	public static String getRemakeDateStr(String date, String delimiter, String outPattern) {
		if(date==null || date.length()<8 || date.length()>10) {
			return "";
		}

		if ((outPattern == null) || (outPattern.equals(""))) outPattern = "yyyy-MM-dd";

		String [] splitDate = date.split(delimiter);

		try {
			if(splitDate.length==3) {
				String year = splitDate[0];
				String month = addZero(Integer.parseInt(splitDate[1],10));
				String day = addZero(Integer.parseInt(splitDate[2],10));

				String fullDateStr = year + month + day;

				if(fullDateStr.length()==8 && isDate(fullDateStr)) {
					java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(outPattern);
					return formater.format(getDate(fullDateStr));
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

}