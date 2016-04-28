package indi.yume.tools.renderercalendar.model;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yume on 15/10/8.
 */
public class DayDate {
    private static final int[] mouth_calculate = {2, 5, 5, 1, 3, 6, 1, 4, 0, 2, 5, 0};
    private static final int[] mouth_sum_num = {31,28,31,30,31,30,31,31,30,31,30,31};

    private int year = 1900;
    //0~11
    @IntRange(from = 0, to = 11)
    private int month = 0;
    //1~
    private int day = 1;

    private Calendar calendar;

    public static DayDate preDayDate(@Nullable DayDate targetDate) {
        if (targetDate == null) {
            targetDate  = new DayDate();
        }
        int currentYear = targetDate.getYear();
        int currentMoth = targetDate.getMonth();
        if (currentMoth == 0) {
            --currentYear;
            currentMoth = 11;
        } else {
            --currentMoth;
        }
        return new DayDate(currentYear, currentMoth, 1);
    }

    public DayDate(){
        calendar = Calendar.getInstance();
        setValue(calendar);
    }

    public DayDate(int year,
                   @IntRange(from = 0, to = 11) int month,
                   int day) {
        calendar = Calendar.getInstance();
        setValue(year, month, day);
    }

    public DayDate(DayDate day){
        this(day.calendar);
    }

    public DayDate(Calendar day){
        setValue(day);
        calendar = Calendar.getInstance();
        calendar.setTime(day.getTime());
    }

    public void setValue(int year,
                         @IntRange(from = 0, to = 11) int month,
                         int day) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        setValue(calendar);

        calendar.set(Calendar.DAY_OF_MONTH,
                Math.min(day, getMaxDay()));

        setValue(calendar);
    }

    public void setValue(DayDate date){
        setYear(date.getYear());
        setMonth(date.getMonth());
        setDay(date.getDay());
    }

    private void setValue(Calendar calendar) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public long getTime() {
        return calendar.getTimeInMillis();
    }

    public void setTime(long timeInMillis) {
        calendar.setTimeInMillis(timeInMillis);
        setValue(calendar);
    }

    public int getYear() {
        return year;
    }

    public void setYear(@Nullable Integer year) {
        if (year == null) return;
        setValue(year, this.month, this.day);
    }

    public void addYear(int yearOffset){
        calendar.add(Calendar.YEAR, yearOffset);
        setValue(calendar);
    }

    @IntRange(from = 0, to = 11)
    public int getMonth() {
        return month;
    }

    public void setMonth(@Nullable @IntRange(from = 0, to = 11) Integer month) {
        if (month == null) return;
        setValue(this.year, month, this.day);
    }

    public void addMonth(int monthOffset){
        calendar.add(Calendar.MONTH, monthOffset);
        setValue(calendar);
    }

    public int getDay() {
        return day;
    }

    public void setDay(@Nullable Integer day) {
        if (day == null) return;
        setValue(this.year, this.month, day);
    }

    public void addDay(int dayOffset){
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);
        setValue(calendar);
    }

    public int getMaxDay() {
        return getMouthMaxDay(year, month + 1);
    }

    // 0~6
    public static int whatDayIsIt(int year, int mouth, int day){
        return (day + mouth_calculate[mouth - 1]) % 7;
    }

    public static int getMouthMaxDay(int year,
                                     @IntRange(from = 1, to = 12) int mouth){
        if(mouth == 2 && ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0))
            return 29;
        else
            return mouth_sum_num[mouth - 1];
    }

    public void getDate(Calendar date) {
        date.setTime(new Date(calendar.getTimeInMillis()));
    }

    /*
     * returns the value of the given field after computing the field values by
     * calling {@code complete()} first.
     *
     * eg: get(Calendar.DAY_OF_MONTH)
     */
    public int get(int field){
        return calendar.get(field);
    }

    @Override
    public String toString() {
        return String.format("%04d/%02d/%02d", year, month + 1, day);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DayDate))
            return false;
        DayDate d = (DayDate) o;

        return d.getYear() == year
                && d.getMonth() == month
                && d.getDay() == day;
    }
}
