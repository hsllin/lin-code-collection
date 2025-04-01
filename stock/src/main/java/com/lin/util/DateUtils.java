package com.lin.util;

import com.lin.bean.DateFormatEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 */
public class DateUtils {
    //默认时间格式
    public static String defaultFormat = "yyyy-MM-dd HH:mm:ss";
    /**
     * 当天
     */
    public final static Integer TODAY = 0;

    /**
     * 次日
     */
    public final static Integer TOMORROW = 1;

    /**
     * 获取当前日期，格式为2021-10-10
     *
     * @return
     */
    public static String getNowDate() {
        String format = "yyyy-MM-dd";
        LocalDateTime date = LocalDateTime.now();
        //创建日期时间对象格式化器，日期格式类似： 2020-02-23 22:18:38
        DateTimeFormatter formatter = null;
        if (StringUtils.isBlank(format)) {
            formatter = DateTimeFormatter.ofPattern(defaultFormat);
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        //将时间转化为对应格式的字符串
        String fomateDate = date.format(formatter).toString();
        return fomateDate;
    }

    public static String getDate(DateFormatEnum dateFormatEnum) {
        String format = dateFormatEnum.getValue();
        LocalDateTime date = LocalDateTime.now();
        //创建日期时间对象格式化器，日期格式类似： 2020-02-23 22:18:38
        DateTimeFormatter formatter = null;
        if (StringUtils.isBlank(format)) {
            formatter = DateTimeFormatter.ofPattern(defaultFormat);
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        //将时间转化为对应格式的字符串
        String fomateDate = date.format(formatter).toString();
        return fomateDate;
    }

    /**
     * 获取昨天日期，格式为2021-10-10
     *
     * @return
     */
    public static String getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 获取昨天日期，格式为2021-10-10
     *
     * @return
     */
    public static String getYesterday(DateFormatEnum dateFormatEnum) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //得到前一天
        Date date = calendar.getTime();
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取昨天日期，格式为2021-10-10
     *
     * @return
     */
    public static String getBeforeDay(DateFormatEnum dateFormatEnum) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2); //得到前一天
        Date date = calendar.getTime();
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取指定日期的前一天
     *
     * @param queryDate
     * @return
     */
    public static String getBeforeDay(String queryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(queryDate);
        } catch (Exception e) {

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取昨日日期，格式为2021-10-10 06:00
     *
     * @return
     */
    public static String getYesterdayTime(String Time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(Time);
        } catch (Exception e) {

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取明天日日期，格式为2021-10-10 06:00
     *
     * @return
     */
    public static String getTomorrowTime(String Time, DateFormatEnum dateFormatEnum) {

        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        Date date = new Date();
        try {
            date = sdf.parse(Time);
        } catch (Exception e) {

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取当前时间，获取当前时间,格式为小时+分钟+秒；15：00：00
     *
     * @return
     */
    public static String getNowTime() {
        String format = "HH:mm:ss";
        LocalDateTime date = LocalDateTime.now();
        //创建日期时间对象格式化器，日期格式类似： 2020-02-23 22:18:38
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern(format);
        //将时间转化为对应格式的字符串
        String fomateDate = date.format(formatter).toString();
        return fomateDate;
    }

    /**
     * 获取当前时间,格式为小时+分钟；15：00
     *
     * @return
     */
    public static String getNowDateTimeWithMin() {
        String format = "yyyy-MM-dd HH:mm";
        LocalDateTime date = LocalDateTime.now();
        //创建日期时间对象格式化器，日期格式类似： 2020-02-23 22:18:38
        DateTimeFormatter formatter = null;
        if (StringUtils.isBlank(format)) {
            formatter = DateTimeFormatter.ofPattern(defaultFormat);
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        //将时间转化为对应格式的字符串
        String fomateDate = date.format(formatter).toString();
        return fomateDate;
    }

    /**
     * 获取当前时间,格式为小时+分钟；15：00
     *
     * @return
     */
    public static String getNowTimeWithMin() {
        String format = "HH:mm";
        LocalDateTime date = LocalDateTime.now();
        //创建日期时间对象格式化器，日期格式类似： 2020-02-23 22:18:38
        DateTimeFormatter formatter = null;
        if (StringUtils.isBlank(format)) {
            formatter = DateTimeFormatter.ofPattern(defaultFormat);
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        //将时间转化为对应格式的字符串
        String fomateDate = date.format(formatter).toString();
        return fomateDate;
    }

    /**
     * 设置一个很远的时间点，用于拉链表区分当前版本和历史版本
     *
     * @return
     */
    public static String getMaxTime() {
        //拉链表设置未来很遥远的时间用这个
        return "3000-12-31";
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 计算起始时间的时长,单位为分钟
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int countTimeForMinute(String startTime, String endTime, DateFormatEnum dateFormatEnum) {
        SimpleDateFormat simpleFormat = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        int minutes = 0;
        try {
            long from = simpleFormat.parse(startTime).getTime();
            long to = simpleFormat.parse(endTime).getTime();
            minutes = (int) ((to - from) / (1000 * 60));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Math.abs(minutes);
    }

    /**
     * 计算总时长，返回小时+分钟
     *
     * @param totalMinute
     * @return
     */
    public static String countTotalTime(long totalMinute) {
        int floor = (int) Math.floor(totalMinute / 60);//分钟计算小时
        int fen = (int) totalMinute % 60;//分钟计算
        return floor + "小时" + fen + "分钟";
    }

    /**
     * 计算总时长，返回小时+分钟Map
     *
     * @param totalMinute
     * @return
     */
    public static Map countTotalTimeForMap(long totalMinute) {
        int floor = (int) Math.floor(totalMinute / 60);//分钟计算小时
        int fen = (int) totalMinute % 60;//分钟计算
        Map<String, Integer> map = new HashMap<>();
        map.put("hour", floor);
        map.put("min", fen);
        return map;
    }

    /**
     * 获取查询时间属于星期几,返回数字的星期几
     *
     * @return
     */
    public static String getWhichDay(String queryDate) {
        String[][] strArray = {
                {"MONDAY", "1"}, {"TUESDAY", "2"}, {"WEDNESDAY", "3"}, {"THURSDAY", "4"}, {"FRIDAY", "5"}, {"SATURDAY", "6"}, {"SUNDAY", "7"}};
        LocalDate currentDate = LocalDate.parse(queryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String k = String.valueOf(currentDate.getDayOfWeek());
        //获取行数
        for (int i = 0; i < strArray.length; i++) {
            if (k.equals(strArray[i][0])) {
                k = strArray[i][1];
                break;
            }
        }
        return k;
    }


    /**
     * 根据日期查询星期几
     *
     * @param queryDate
     * @return
     */
    public static String getWeekDay(String queryDate) {
        Map<Integer, String> weekMap = new HashMap<>(7);
        weekMap.put(1, "星期日");
        weekMap.put(2, "星期一");
        weekMap.put(3, "星期二");
        weekMap.put(4, "星期三");
        weekMap.put(5, "星期四");
        weekMap.put(6, "星期五");
        weekMap.put(7, "星期六");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar calendar = Calendar.getInstance();
        Date time = new Date();
        try {
            time = sdf.parse(queryDate);
        } catch (Exception e) {

        }
        calendar.setTime(time);
        return weekMap.get(calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * 获取日期-时间，分为当天和次日
     *
     * @param time
     * @param todayOrTomorrow
     * @return
     */
    public static String getTimeWithDay(String time, Integer todayOrTomorrow) {
        if (StringUtils.isBlank(time)) {
            return "";
        }
        if (TODAY == todayOrTomorrow) {
            return getNowDate() + " " + time;
        } else {
            Date today = getNow();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int day = calendar.get(Calendar.DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //                      此处修改为+1则是获取后一天
            calendar.set(Calendar.DATE, day + 1);

            String tomorrow = sdf.format(calendar.getTime());
            return tomorrow + " " + time;
        }

    }

    /**
     * 获取日期-时间，分为当天和次日
     *
     * @param time
     * @param todayOrTomorrow
     * @return
     */
    public static String getDateTimeWithDay(String date, String time, Integer todayOrTomorrow) {
        if (StringUtils.isBlank(time) || StringUtils.isBlank(date)) {
            return "";
        }
        if (TODAY == todayOrTomorrow) {
            return date + " " + time;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date targetDate = new Date();
            try {
                targetDate = sdf.parse(date);
            } catch (Exception e) {

            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(targetDate);
            int day = calendar.get(Calendar.DATE);

            //                      此处修改为+1则是获取后一天
            calendar.set(Calendar.DATE, day + 1);

            String tomorrow = sdf.format(calendar.getTime());
            return tomorrow + " " + time;
        }

    }

    /**
     * 获取两个时间的中间时间，返回2021-10-01 10:10这种格式
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getMiddleDateTime(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime)) {
            startTime = getNowDate() + " 00:00";
        }
        if (StringUtils.isBlank(endTime)) {
            endTime = getNowDate() + " 23:59";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long startDate = 0;
        long endDate = 0;
        try {
            //从对象中拿到时间
            startDate = df.parse(startTime).getTime();
            endDate = df.parse(endTime).getTime();
            //相差的一般时间
            long diff = (endDate - startDate) / 2;
            startDate += diff;
        } catch (Exception e) {

        }

        Date date = new Date(startDate);
        return df.format(date);
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getMiddleTime(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime)) {
            startTime = "00:00";
        }
        if (StringUtils.isBlank(endTime)) {
            endTime = "23:59";
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        long startDate = 0;
        long endDate = 0;
        try {
            //从对象中拿到时间
            startDate = df.parse(startTime).getTime();
            endDate = df.parse(endTime).getTime();
            //相差的一般时间
            long diff = (endDate - startDate) / 2;
            startDate += diff;
        } catch (Exception e) {

        }

        Date date = new Date(startDate);
        return df.format(date);
    }

    /**
     * 比较两个时间的大小，包含相等的情况
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareTimeWithEquals(String time1, String time2, DateFormatEnum dateFormatEnum) {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        //将字符串形式的时间转化为Date类型的时间
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (Exception e) {

        }
        return date1.before(date2) || date1.equals(date2);
    }

    /**
     * 比较两个时间的大小,不包含相等的情况
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareTimeWithNoEquals(String time1, String time2, DateFormatEnum dateFormatEnum) {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        //将字符串形式的时间转化为Date类型的时间
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (Exception e) {

        }
        return date1.before(date2);
    }

    /**
     * 比较两个日期的大小，只有日期
     *
     * @param date1
     * @param date2
     * @param includeToday 是否包含当天
     * @return
     */
    public static boolean compareDate(String date1, String date2, boolean includeToday) {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //将字符串形式的时间转化为Date类型的时间
        Date dateOne = new Date();
        Date dateTwo = new Date();
        try {
            dateOne = sdf.parse(date1);
            dateTwo = sdf.parse(date2);
        } catch (Exception e) {

        }
        if (includeToday) {
            return dateOne.before(dateTwo) || dateOne.equals(dateTwo);
        }
        return dateOne.before(dateTwo);
    }

    /**
     * 比较两个日期的大小,包括日期和时间，需要考虑时间相等的情况
     *
     * @param date1
     * @param date2
     * @return
     */
//    public static boolean compareDateTimeWithEquals(String date1, String date2) {
//        //如果想比较日期则写成"yyyy-MM-dd"就可以了
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //将字符串形式的时间转化为Date类型的时间
//        Date dateOne = new Date();
//        Date dateTwo = new Date();
//        try {
//            dateOne = sdf.parse(date1);
//            dateTwo = sdf.parse(date2);
//        } catch (Exception e) {
//
//        }
//        return dateOne.before(dateTwo) || dateOne.equals(dateTwo);
//    }


//    /**
//     * 比较两个日期的大小,包括日期和时间，不需要考虑时间相等的情况
//     *
//     * @param date1
//     * @param date2
//     * @return
//     */
//    public static boolean compareDateTimeWithNoEquals(String date1, String date2) {
//        //如果想比较日期则写成"yyyy-MM-dd"就可以了
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //将字符串形式的时间转化为Date类型的时间
//        Date dateOne = new Date();
//        Date dateTwo = new Date();
//        try {
//            dateOne = sdf.parse(date1);
//            dateTwo = sdf.parse(date2);
//        } catch (Exception e) {
//
//        }
//        return dateOne.before(dateTwo);
//    }

    /**
     * 计算相差小时数，保留小数点一位
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static double caculateTotalTimeForHour(String startTime, String endTime, DateFormatEnum dateFormatEnum) {
        SimpleDateFormat formatter = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = formatter.parse(startTime);
            date2 = formatter.parse(endTime);
        } catch (Exception e) {

        }
        long ts1 = date1.getTime();     //getTime()返回共 多少毫 秒。x/1000=?秒； 继续 x/60=?分； 继续 x/60=?小时； 继续 x/24=?天； 继续 x/360=?年 ！！！
        long ts2 = date2.getTime();
        double ts3 = ts2 - ts1;

        double totalTime = 0;
        totalTime = (ts3 / (3600 * 1000));


        String st = null;    //String 基本类型，被final定义，所以必须初始化
        DecimalFormat df = new DecimalFormat("#.#");    //保留1位小数
        st = df.format(totalTime);

        return Math.abs(Double.valueOf(st));
    }

    /**
     * 计算相差分钟数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Integer caculateTotalTimeForMin(String startTime, String endTime, DateFormatEnum dateFormatEnum) {
        SimpleDateFormat formatter = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = formatter.parse(startTime.toString());
            date2 = (Date) formatter.parseObject(endTime);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long ts1 = date1.getTime();     //getTime()返回共 多少毫 秒。x/1000=?秒； 继续 x/60=?分； 继续 x/60=?小时； 继续 x/24=?天； 继续 x/360=?年 ！！！
        long ts2 = date2.getTime();
        double ts3 = ts2 - ts1;

        double totalTime = 0;
        totalTime = (ts3 / (60 * 1000));


        String st = null;    //String 基本类型，被final定义，所以必须初始化
        DecimalFormat df = new DecimalFormat("#");    //不保留小数
        st = df.format(totalTime);

        return Math.abs(Integer.valueOf(st));
    }

    /**
     * 将分钟转成小时
     *
     * @param min
     * @return
     */
    public static Double transferMinToHour(Integer min) {
        double totalTime = 0.0;
        if (min < 0) {
            min = 0;
        }
        totalTime = (double) min / 60;
        String st = null;    //String 基本类型，被final定义，所以必须初始化
        DecimalFormat df = new DecimalFormat("#.#");
        st = df.format(totalTime);
        return Double.parseDouble(st);
    }

    /**
     * 获取当前日期一周内的所有日期
     *
     * @param n         -1代表上一周 +1代表下一周
     * @param queryDate
     * @return
     */
    public static List getWeek(int n, String queryDate) {

        String[] date = new String[7];
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            Date time = sdf.parse(queryDate);
            cal.setTime(time);

            //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            //获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, (cal.getFirstDayOfWeek() - day + 7 * n));
            date[0] = sdf.format(cal.getTime());
            for (int i = 1; i < 7; i++) {
                cal.add(Calendar.DATE, 1);
                date[i] = sdf.format(cal.getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        return Arrays.asList(date);
    }

    /**
     * 获取给定日期一个月的范围
     *
     * @param queryDate
     * @return
     */
    public static List<String> getMonthList(String queryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        Date time = new Date();
        try {
            time = sdf.parse(queryDate);
        } catch (Exception e) {

        }
        cal.setTime(time);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
            dateList.add(df);

        }
        return dateList;
    }

    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static String getMonthFistDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String monthfirst = sdf.format(c.getTime());
        return monthfirst;
    }

    /**
     * 获取给定日期1号至截至日期的时间范围,返回格式 2021-10-24
     *
     * @param queryDate
     * @return
     */
    public static List<String> getMonthListWithEnd(String queryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        Date time = new Date();
        try {
            time = sdf.parse(queryDate);
        } catch (Exception e) {

        }
        cal.setTime(time);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int dayNumOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
//            String dateWithWeek = df + " (" + getWeekDay(df) + ")";
            dateList.add(df);

        }
        return dateList;
    }

    /**
     * 获取给定日期1号至截至日期的时间范围,返回格式 2021-10-24
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getMonthListScope(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static boolean isSameDay(String time1, String time2) {
        if (StringUtils.isBlank(time1) || StringUtils.isBlank(time2)) {
            return false;
        }
        String s1 = time1.substring(0, 10);
        return time1.substring(0, 10).equalsIgnoreCase(time2.substring(0, 10));

    }

    /**
     * 获取一个月中的最大天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 当天的开始时间，不带日期
     *
     * @return
     */
    public static String getStartTime() {
        return "00:00";
    }

    /**
     * 当天的结束时间，不带日期
     *
     * @return
     */
    public static String getEndTime() {
        return "23:59";
    }

    /**
     * 当天的开始时间
     *
     * @return
     */
    public static String getDayStartTime() {
        return getNowDate() + " 00:00";
    }

    /**
     * 当天的结束时间
     *
     * @return
     */
    public static String getDayEndTime() {
        return getNowDate() + " 23:59";
    }

//    public static String addOneMinute(String dateTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        Date date = new Date();
//        try {
//            date = sdf.parse(dateTime);
//        } catch (Exception e) {
//
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.MINUTE, 1);
//        date = calendar.getTime();
//        return sdf.format(date);
//    }

    /**
     * 当前时间加减
     *
     * @param min
     * @param isAdd
     * @return
     */
    public static String addTime(String startTime, Integer min, boolean isAdd, DateFormatEnum dateFormatEnum) {
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        Date date = new Date();
        try {
            date = sdf.parse(startTime);
        } catch (Exception e) {

        }

        long time = min * 60 * 1000;//多少分钟
        Date resultTime = null;
        if (isAdd) {
            resultTime = new Date(date.getTime() + time);

        } else {
            resultTime = new Date(date.getTime() - time);
        }
        System.out.println(sdf.format(resultTime));
        return sdf.format(resultTime);
    }

    /**
     * 当前时间加减
     *
     * @param min
     * @param isAdd
     * @return
     */
    public static String addDateTime(String startTime, Integer min, boolean isAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(startTime);
        } catch (Exception e) {

        }

        long time = min * 60 * 1000;//多少分钟
        Date resultTime = null;
        if (isAdd) {
            resultTime = new Date(date.getTime() + time);

        } else {
            resultTime = new Date(date.getTime() - time);
        }
        System.out.println(sdf.format(resultTime));
        return sdf.format(resultTime);
    }

    public static String transferFormatTime(String time, DateFormatEnum sourceFormat, DateFormatEnum targetFormat) {
        SimpleDateFormat sourceSdf = DateFormatEnum.getSimpleDateFormat(sourceFormat);
        SimpleDateFormat targetSdf = DateFormatEnum.getSimpleDateFormat(targetFormat);
        Date date = new Date();
        try {
            date = sourceSdf.parse(time);
        } catch (Exception e) {

        }
        return targetSdf.format(date);
    }

    public static String transferTimeFormat(String time, DateFormatEnum dateFormatEnum) {
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (Exception e) {

        }
        return sdf.format(date);
    }

    public static void main(String[] args) {
//        System.out.println(getNow(""));
//        String star = "09:18";
//        String den = "23:17";
//        long re = countTimeForMinute(star, den);
//        System.out.println(re);
//        System.out.println(getWhichDay());
//        getMiddleTime("", "09:00");
//        System.out.println(transferMinToHour(80));
//        System.out.println(getWeek(0, "2021-10-02").toString());
//        System.out.println(getMonthList("2020-11-24").toString());

//        System.out.println(getMiddleDateTime("2020-11-24 18:00", "2020-11-25 09:00"));
//        System.out.println(isSameDay("2021-12-22 10:30", "2021-12-22 10:30"));
//        System.out.println(getBeforeDay("2021-12-27"));

//        System.out.println(getWeekDay("2022-01-09"));
//        System.out.println(getMonthList("2020-10-10"));
//        System.out.println(addOneMinute("23:59"));
//        System.out.println(getMonthList("2022-01"));
        System.out.println(caculateTotalTimeForMin("2022-02-26 10:24", "2022-02-26 10:00", DateFormatEnum.DATE_HOUR_MIN));
        System.out.println(DateUtils.compareTimeWithEquals("2022-02-26 10:24", "2022-02-26 10:00", DateFormatEnum.DATE_HOUR_MIN));
//        System.out.println(getNowTime());
//        System.out.println(getNowTimeWithMin());
//        System.out.println(getMiddleTime("23:00:00", "27:00:00"));
//        System.out.println(countTimeForMinute("16:18:00", "15:13:00"));
//        System.out.println(getNowTime());
//
//        System.out.println(compareTime("11:40", "12:00"));
//        System.out.println(countTimeForMinute("15:14","16:18"));
//        System.out.println(getWeekDay("2021-11-29"));

        System.out.println(transferFormatTime("2022-02-26 10:24", DateFormatEnum.DATE_HOUR_MIN, DateFormatEnum.HOUR_MIN));
        System.out.println(addTime("9:00", 20, true, DateFormatEnum.HOUR_MIN));

        System.out.println(getDate(DateFormatEnum.DATE_WITH_OUT_LINE));
        System.out.println(rightTime("2018-10-09 18:50:09"));
        System.out.println(rightTime("2022-04-28 09:40:00"));
        int x = 380;
        int y = 3800;
        double d1 = x * 1.0;
        double d2 = y * 1.0;
        // 设置保留几位小数， “.”后面几个零就保留几位小数，这里设置保留四位小数
        DecimalFormat decimalFormat = new DecimalFormat("##");
        ;
        System.out.println(Integer.valueOf(decimalFormat.format((d1 / d2) * 100)));

        List<String> list = new ArrayList<>(16);
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        List result = getPageLimit(list, 0, 2);
        System.out.println(Arrays.toString(new List[]{result}));

        System.out.println(transferTimeFormat("2023-07-01 16:09:10", DateFormatEnum.DATE));
        System.out.println(transferTimeFormat("2023-07-01", DateFormatEnum.DATE));
    }


    /**
     * 获取分页数据
     *
     * @param dataList 进行分页的数据集合
     * @param pageNum  第几页
     * @param pageSize 每页显示多少条
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List getPageLimit(List dataList, int pageNum, int pageSize) {
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        List resultList = new ArrayList();
        // 所有dataList数据中的第几条
        int currIdx = pageNum > 1 ? (pageNum - 1) * pageSize : 0;
        for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
            resultList.add(dataList.get(currIdx + i));
        }
        return resultList;
    }

    private static boolean rightTime(String time) {
        String rule = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(time);
        return matcher.find();
    }


    /**
     * 计算相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long dateDifferenceDayCalculator(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);

            long differenceInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
            long differenceInDays = differenceInMilliseconds / (24 * 60 * 60 * 1000);

            return differenceInDays;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String changeTime(long time,DateFormatEnum dateFormatEnum){
        SimpleDateFormat sdf=new SimpleDateFormat(dateFormatEnum.getValue());
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(time))* 1000L));
        return sd;
    }

    /**
     * 获取昨天日期，格式为2021-10-10
     *
     * @return
     */
    public static long getDateTime(String dateTime,DateFormatEnum dateFormatEnum) {
        SimpleDateFormat sdf = DateFormatEnum.getSimpleDateFormat(dateFormatEnum);
        Date date = new Date();
        try {
            date = sdf.parse(dateTime);
        } catch (Exception e) {

        }
        return date.getTime();
    }

}
