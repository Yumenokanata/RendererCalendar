package indi.yume.tools.renderercalendar.util;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/8.
 */
public class CalculateDayTool {
    private static final int[] mouth_calculate = {3, 6, 6, 2, 4, 7, 2, 5, 1, 3, 6, 1};
    private static final int[] mouth_sum_num = {31,28,31,30,31,30,31,31,30,31,30,31};

    // 1~7
    public static int whatDayIsIt(DayDate date){
        int week = (date.getDay() + mouth_calculate[date.getMonth()]) % 7;
        return week == 0 ? 7 : week;
    }

    public static int getMouthSumDayNum(DayDate date){
        if(date.getMonth() == 1 && ((date.getYear() % 4 == 0 && date.getYear() % 100 != 0) || date.getYear() % 400 == 0))
            return 29;
        else
            return mouth_sum_num[date.getMonth()];
    }
}
