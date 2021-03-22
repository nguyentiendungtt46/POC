/**
 * 
 */
package vn.com.cmc.utils;

/**
 * @author nvtiep
 *
 * created: Dec 25, 2019 5:35:43 PM
 */

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author Admin
 * @version 1.0
 */
public final class DateUtils {
	public static final String PATTERN_DDMMYYYY = "ddMMyyyy";
	public static final String PATTERN_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
	public static final String PATTERN_HHMM = "hh:mm";

	public static boolean compare(Date date1, Date date2, String format) throws Exception {
		if (date1 == null || date2 == null) {
			return false;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		if (dateFormat.format(date1).equals(dateFormat.format(date2))) {
			return true;
		} else {
			return false;
		}
	}

	public static String dateTime2Stringddmmyyyy(Date value) {
		if (value != null) {
			SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return dateTime.format(value);
		}
		return "";
	}

	public static boolean equalOnlyField(Date date1, Date date2, int type) {
		if (date1 == null || date2 == null) {
			return false;
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		return c1.get(type) == c2.get(type);

	}

	/**
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return long
	 */
	public static long minusDate(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2MMyyString(Date value) {
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat("MM/yyyy");
			return date.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2ddMMyyNoSlashString(Date value) {
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
			return date.format(value);
		}
		return "";
	}

	public static String date2yyyyMMddStringWithSlash(Date value) {// convert Date to String
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
			return date.format(value);
		}
		return "";
	}
	
	public static String date2String(Date value, String fomrat) {
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat(fomrat);
			return date.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2yyyyMMddHHString(Date value) {
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHH");
			return date.format(value);
		}
		return "";
	}

	public static String date2yyyyMMddHHmmssString(Date value) {
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
			return date.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            String
	 * @return Date
	 */
	public static Date string2Date(String value, String format) {
		try {
			SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat(format);
			return dbUpdateDateTime.parse(value);
		} catch (ParseException ex) {
		}

		return new Date();
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2String(Date value) {
		if (value != null) {
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
			return date.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2StringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat dateNoSlash = new SimpleDateFormat("yyyyMMdd");
			return dateNoSlash.format(value);
		}
		return "";
	}
	
	public static Date addMin(Date date, int numMin) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, numMin);
			date = cal.getTime();
		}
		return date;
	}

	public static Date addDate(Date date, int numDate) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, numDate);
			date = cal.getTime();
		}
		return date;
	}
	
	public static Date addHour(Date date, int numHour) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR, numHour);
			date = cal.getTime();
		}
		return date;
	}
	
	public static Date addSecond(Date date, int numSecond) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.SECOND, numSecond);
			date = cal.getTime();
		}
		return date;
	}
	
	public static Date addMonth(Date date, int numMonth) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, numMonth);
			date = cal.getTime();
		}
		return date;
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String dateH2StringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat dateNoSlash = new SimpleDateFormat("yyyyMMddHH");
			return dateNoSlash.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String dateTime2StringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("yyyyMMddHHmmss");
			return dateTimeNoSlash.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String dateTime2String(Date value) {
		if (value != null) {
			SimpleDateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return dateTime.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String dbUpdateDateTime2String(Date value) {
		if (value != null) {
			SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dbUpdateDateTime.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return Timestamp
	 */
	public static Timestamp date2Timestamp(Date value) {
		if (value != null) {
			return new Timestamp(value.getTime());
		}
		return null;
	}

	/**
	 * @return Date
	 */
	public static Date sysDate() {
		return new Date();
	}

	/**
	 * return now if input date is null, otherwise return date
	 *
	 * @param date
	 * @return
	 */
	public static Date dateToNow(Date date) {
		return date == null ? new Date() : date;
	}

	/**
	 * @return Date
	 */
	public static Date sysdateYmd() {
		return nextdate(0);
	}

	/**
	 * @param day
	 *            integer
	 * @return Date
	 */
	public static Date nextdate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + day, 0, // hour
				0, // min
				0); // sec
		/**
		 * clear millisecond field
		 */
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	}

	/**
	 * @param date
	 *            Date
	 * @param day
	 *            integer
	 * @return Date
	 */
	public static Date nextdate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + day, 0, // hour
				0, // min
				0); // sec
		/**
		 * clear millisecond field
		 */
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	}

	/**
	 * get the next n month.
	 *
	 * @param date
	 *            Date
	 * @param month
	 *            number of next month
	 * @return Date
	 */
	public static Date nextMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + month, calendar.get(Calendar.DATE), 0, // hour
				0, // min
				0); // sec
		/**
		 * clear millisecond field
		 */
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	}

	public static Date nextMonthDateTime(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * get the previos n month
	 *
	 * @param date
	 *            Date
	 * @param month
	 *            integer
	 * @return Date
	 */
	public static Date getPreMonthDate(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - month, calendar.get(Calendar.DATE), 0, // hour
				0, // min
				0); // sec
		/**
		 * clear millisecond field
		 */
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();

	}

	/**
	 * @return String
	 */
	public static String sysdateString() {
		SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dbUpdateDateTime.format(new Date());
	}

	/**
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getDate() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		return date;
	}

	/**
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getDateTime() {
		SimpleDateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateTime;
	}

	/**
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getDateTimeMinute() {
		SimpleDateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return dateTime;
	}

	/**
	 * [timestampToStringFF function.].<br>
	 * [Detail description of method.]
	 *
	 * @param date
	 *            Timestamp
	 * @return String
	 */
	public static String timestampToStringFF(Timestamp date) {
		if (date != null) {
			SimpleDateFormat dbDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return dbDateTimeString.format(date);
		}
		return "";
	}

	/**
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getDbUpdateDateTime() {
		SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dbUpdateDateTime;
	}

	/**
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getYYYYMM() {
		SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
		return yyyymm;
	}

	/**
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getMMdd() {
		SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd");
		return mmdd;
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2ddMMyyyyString(Date value) {
		if (value != null) {
			SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
			return ddMMyyyy.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String getFirstDateOfMonth(Date value) {
		if (value != null) {
			SimpleDateFormat MMyyyy = new SimpleDateFormat("MM/yyyy");
			return "01/" + MMyyyy.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2MMyyyyString(Date value) {
		if (value != null) {
			SimpleDateFormat yyyyMM = new SimpleDateFormat("MM/yyyy");
			return yyyyMM.format(value);
		}
		return "";
	}

	/**
	 * date to yyMMddString
	 *
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2yyMMddString(Date value) {
		if (value != null) {
			SimpleDateFormat yyMMdd = new SimpleDateFormat("yy/MM/dd");
			return yyMMdd.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2yyMMddStringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");
			return yyMMdd.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2yyyyMMStringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			return yyyymm.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2yyyyMMddStringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMMdd");
			return yyyymm.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2yyMMStringNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat yymm = new SimpleDateFormat("yyMM");
			return yymm.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2MMddString(Date value) {
		if (value != null) {
			SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd");
			return mmdd.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String second2String(Date value) {
		if (value != null) {
			return SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM).format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return []String
	 */
	public static String[] getSplitDate(Date value) {
		if (value != null) {
			DecimalFormat df = new DecimalFormat("00");
			String[] dateTime = dateTime2String(value).split(" ");
			String[] date = new String[6];
			String[] tmpDate = dateTime[0].split("/");
			date[0] = df.format(Integer.parseInt(tmpDate[0]));
			date[1] = df.format(Integer.parseInt(tmpDate[1]));
			date[2] = df.format(Integer.parseInt(tmpDate[2]));
			tmpDate = dateTime[1].split(":");
			date[3] = df.format(Integer.parseInt(tmpDate[0]));
			date[4] = df.format(Integer.parseInt(tmpDate[1]));
			date[5] = df.format(Integer.parseInt(tmpDate[2]));
			return date;
		}
		return new String[6];
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2MMddTime(Date value) {
		if (value != null) {
			SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd HH:mm:ss");
			return mmdd.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2YYYYMMddTime(Date value) {
		if (value != null) {
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return yyyyMMdd.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2HHMMssNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("HHmmss");
			return dateTimeNoSlash.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String date2ddMMyyyyHHMMssNoSlash(Date value) {
		if (value != null) {
			SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("ddMMyyyyHHmmss");
			return dateTimeNoSlash.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String dateyyyyMMddHHmmSS(Date value) {
		if (value != null) {
			SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateTimeNoSlash.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 *            Date
	 * @return String
	 */
	public static String dateyyyyMMdd(Date value) {
		if (value != null) {
			SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("yyyy-MM-dd");
			return dateTimeNoSlash.format(value);
		}
		return "";
	}

	/**
	 * @return
	 */
	public static Timestamp nowDateMilli() {
		return new Timestamp(sysDate().getTime());
	}

	/**
	 * @param date
	 *            Date
	 * @return integer
	 */
	public static int getYY(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (calendar.get(Calendar.YEAR)) % 100;
	}

	/**
	 * @param nowDate
	 *            Date
	 * @return integer
	 */
	public static int getMonth(Date nowDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * @param nowDate
	 *            Date
	 * @return integer
	 */
	public static int getDay(Date nowDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	// ==========================================================================

	/**
	 * addMilli.<br>
	 *
	 * @param nowDate
	 *            Date
	 * @param period
	 *            integer
	 * @return Timestamp
	 */
	// ==========================================================================
	public static Timestamp addMilli(Timestamp nowDate, int period) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.MILLISECOND, period);

		Timestamp stopTerm = date2Timestamp(calendar.getTime());

		return stopTerm;
	}

	/**
	 * add minute
	 *
	 * @param nowDate
	 *            Date
	 * @param period
	 *            integer
	 * @return Date
	 */
	public static Date addMinute(Date nowDate, int period) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.MINUTE, period);

		return calendar.getTime();
	}
	
	public static boolean isValidDateFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (Exception ex) {
            return false;
        }
        return date != null;
	}

}
