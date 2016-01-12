package indi.yume.tools.renderercalendar.model;

import java.util.Calendar;

/**
 * Created by yume on 15/10/8.
 */
public class DayDate {
    private int year = 1900;
    //0~11
    private int month = 0;
    //1~
    private int day = 1;

    private Calendar calendar;

    public DayDate(){
        calendar = Calendar.getInstance();
        setValue(calendar);
    }

    public DayDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public DayDate(Calendar day){
        setValue(day);
        calendar = Calendar.getInstance();
        calendar.setTime(day.getTime());
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        calendar.set(Calendar.YEAR, year);
    }

    public void addYear(int yearOffset){
        calendar.add(Calendar.YEAR, yearOffset);
        setValue(calendar);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        calendar.set(Calendar.MONTH, month);
    }

    public void addMonth(int monthOffset){
        calendar.add(Calendar.MONTH, monthOffset);
        setValue(calendar);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public void addDay(int dayOffset){
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);
        setValue(calendar);
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
