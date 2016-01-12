package indi.yume.tools.renderercalendar.listener;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/10.
 */
public interface OnDayClickListener {
    public void onDayClick(DayDate date, boolean isInThisMonth);
}
