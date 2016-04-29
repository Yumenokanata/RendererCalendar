package indi.yume.tools.renderercalendar;

import indi.yume.tools.renderercalendar.CalendarView;
import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 16-4-29.
 */
public class OnClickListener {
    private CalendarView mCalendarView;

    public boolean onClick(DayDate date, boolean isInThisMonth) {
        return true;
    }

    public boolean onDoubleClick(DayDate date, boolean isInThisMonth) {
        return true;
    }

    protected void renderAllDays() {

    }

    void bindCalendarView(CalendarView calendarView) {
        this.mCalendarView = calendarView;
    }
}
