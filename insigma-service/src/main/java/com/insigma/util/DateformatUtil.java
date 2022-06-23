package main.java.com.insigma.util;

import jdk.dynalink.beans.StaticClass;
import org.apache.kafka.common.record.TimestampType;
import sun.tools.tree.LongExpression;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @ClassName DateformatUtil
 * @Description
 * @Author carrots
 * @Date 2022/6/20 13:45
 * @Version 1.0
 */
public class DateformatUtil {
    private DateformatUtil() {

    }

    public static final String DATE_TYPE = "ESTSEDT";
    private static final ZoneId ZONE_ID = ZoneId.of("ESTSEDT");
    public static final String YYYYMMDD_LINE = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMDD";
    public static final String YYYYMMDDHHMMSS_LINE = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String formatToday() {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(YYYYMMDD_LINE);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static String formatToday(String format) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String formatyms(Date date, String format) {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date currentTime = new Date();
        try {
            currentTime = formatter.parse(dateString);
            return currentTime;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getNewDate() {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        return Calendar.getInstance(timeZone, Locale.US).getTime();
    }

    public static Long getNowTime() {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        Date date = Calendar.getInstance(timeZone, Locale.US).getTime();
        return date.getTime() / 1000;
    }

    public static Long getNowTimeMillis() {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        Date date = Calendar.getInstance(timeZone, Locale.US).getTime();
        return date.getTime();
    }

    public static String formatToddayT() {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static String formatTodayT(Date date){
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static Integer getSecondTimestamp() {
        Date date = new Date();
        String timeStamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timeStamp);
    }

    public static String timeStampToString(Long timestamp, String format) {
        TimeZone timeZone = TimeZone.getTimeZone(DATE_TYPE);
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(timestamp);
    }

    public static String plusDay(int num, String startDate, String format) {
        Date date = stringToDate(startDate, format);
        if (date == null) {
            return null;
        }
        //得到指定日期的毫秒数
        long time = date.getTime();
        //要加上的天数转换成毫秒
        Long day = num * 24 * 60 * 60 * 1000L;
        //相加得到新的毫秒数
        time += day;
        return timeStampToString(time, format);
    }


    public static Date getDateBegin(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Long parseDateInputToTimeMillis(Integer date) {
        if (date == null) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern(YYYYMMDD));
        return localDate.atStartOfDay(ZONE_ID).toInstant().toEpochMilli();
    }


}
