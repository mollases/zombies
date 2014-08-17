package com.mollases.zombies.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mollases on 5/11/14.
 */
public class ZTime {

    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int HOUR = 0;
    private static final int MINUTE = 1;


    private final GregorianCalendar calendar;

    public ZTime() {
        calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.MONTH, calendar.get(GregorianCalendar.MONTH) + 1);
    }


    public ZTime(String time) {
        int year, month, day, hour, minute;

        String[] fields = time.split(" ");
        String[] ymd = fields[0].split("-");
        String[] hms = fields[1].split(":");

        year = Integer.valueOf(ymd[YEAR]);
        month = Integer.valueOf(ymd[MONTH]);
        day = Integer.valueOf(ymd[DAY]);

        hour = Integer.valueOf(hms[HOUR]);
        minute = Integer.valueOf(hms[MINUTE]);

        calendar = new GregorianCalendar(year, month, day, hour, minute, 0);
    }

    public ZTime(int year, int month, int day, int hour, int minute) {
        calendar = new GregorianCalendar(year, month, day, hour, minute, 0);
    }


    public String toString() {
        return getDate() + " " + getTime();
    }


    String getDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + month + "-" + day;
    }


    public String getDisplayDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return Month.values()[month - 1] + " " + day + ", " + year;
    }


    public String getDisplayTime() {
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        String sMinute = digitize(minute);

        String amOrPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";

        return hour + ":" + sMinute + " " + amOrPm;
    }


    String getTime() {
        int hour = calendar.get(Calendar.HOUR);
        hour += calendar.get(Calendar.AM_PM) == Calendar.PM ? 12 : 0;
        int minute = calendar.get(Calendar.MINUTE);

        String sHour = digitize(hour);
        String sMinute = digitize(minute);

        return sHour + ":" + sMinute + ":00";
    }

    private String digitize(int digit) {
        String dig = String.valueOf(digit);

        dig = dig.length() == 1 ? "0" + dig : dig;
        return dig;
    }

    public int compare(ZTime zTime) {
        return calendar.compareTo(zTime.calendar);
    }

    public long timeUntil(String time) {
        ZTime until = new ZTime(time);
        long millis = until.calendar.getTimeInMillis() - calendar.getTimeInMillis();
        return millis;
    }

    private enum Month {
        January,
        Febuary,
        March,
        April,
        May,
        June,
        July,
        August,
        September,
        October,
        November,
        December
    }
}
