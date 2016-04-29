package indi.yume.tools.renderercalendar.listener;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import indi.yume.tools.renderercalendar.CalendarView;
import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 16-4-29.
 */
public class GestureListener {
    public void onSelect(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
    }

    public boolean onClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
        return isInThisMonth;
    }

    public boolean onDoubleClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
        return isInThisMonth;
    }
}
