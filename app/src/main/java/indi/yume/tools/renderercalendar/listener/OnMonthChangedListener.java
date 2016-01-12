package indi.yume.tools.renderercalendar.listener;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/12.
 */
public interface OnMonthChangedListener {
    void onMonthChanged(DayDate date);
    void onMonthChangedOver(DayDate date);
}
