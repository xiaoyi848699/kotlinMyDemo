package demo.xy.com.mylibrary;

import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by xy on 2018/12/6.
 */
public class DateUtils {
    /**
     * 获取当前时间
     *
     * @param mDateFormat
     *            日期格式化字符串(eg:yyyy-MM-dd hh:mm:ss，12小时制为hh，24小时制为HH)
     * @return
     */
    public static String getCurDateTime(SimpleDateFormat mDateFormat) {
        String date = mDateFormat.format(getCurDate());
        return date;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getCurDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     *
     * 获取当前年份
     *
     * @return
     */
    public static int getCurYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     *
     * 获取当前月份
     *
     * @return
     */
    public static int getCurMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     *
     * 获取当前是几号
     *
     * @return
     */
    public static int getCurDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将字符串格式转化成毫秒
     *
     * @param time 字符串格式的时间 2016-10-23 10:00:00
     * @param dateForma 传入解析时间Strng格式  yyyy-MM-dd HH:mm:ss
     * @return time long
     * **/
    public static long formatDataToLong(String time,String dateForma)
    {
        SimpleDateFormat sf = new SimpleDateFormat(dateForma,Locale.getDefault());
        Date date = null;
        try
        {
            date = sf.parse(time);
        }
        catch (ParseException e)
        {
            Log.d("dateUtils", "ParseException:"+e.getMessage());
        }
        long mlong = 0;
        if (date != null)
        {
            mlong = date.getTime();
        }
        return mlong;

    }

    public static final String FORMAT_Y_M_D_H_M = "yy/MM/dd HH:mm";
    public static final String FORMAT_H_M = "HH:mm";
    public static final String FORMAT_Y_M_D = "yy/MM/dd";
    public static final String PIC_FORMAT = "yyyyMMddHHmmss";//上传图片到服务器
    /**
     * 时间戳转换成本地时间
     *
     * @param time 时间戳
     * @param dateFormat 返回格式  yyyy-MM-dd HH:mm:ss
     * @param timeZone GMT-0
     * @return 返回需要的格式
     */
    public static String formatData(long time,String dateFormat,String timeZone)
    {
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        TimeZone t = TimeZone.getTimeZone(timeZone);
        sf.setTimeZone(t);
        return sf.format(date);
    }
    public static String formatData(long time,String dateFormat)
    {
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sf.format(date);
    }

}
