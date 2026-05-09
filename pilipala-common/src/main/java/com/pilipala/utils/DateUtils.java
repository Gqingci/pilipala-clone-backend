package com.pilipala.utils;

import com.pilipala.entity.enums.DateTimePatternEnum;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Gqingci
 */
public class DateUtils {

    private static final Object lockObj = new Object();

    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = sdfMap.get(pattern);
        if (null == threadLocal) {
            synchronized (lockObj) {
                threadLocal = sdfMap.get(pattern);
                if (null == threadLocal) {
                    threadLocal = ThreadLocal.withInitial(new Supplier<SimpleDateFormat>() {
                        @Override
                        public SimpleDateFormat get() {
                            return new SimpleDateFormat(pattern);
                        }
                    });
                    sdfMap.put(pattern, threadLocal);
                }
            }
        }
        return threadLocal.get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getBeforeDay(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        return format(calendar.getTime(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
    }

    public static List<String> getBeforeDate(Integer beforeDay) {
        LocalDate endDate = LocalDate.now();
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = beforeDay; i > 0; i--) {
            dateList.add(endDate.minusDays(i).format(formatter));
        }
        return dateList;
    }
}
