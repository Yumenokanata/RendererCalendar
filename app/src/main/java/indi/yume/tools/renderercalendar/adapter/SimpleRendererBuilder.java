package indi.yume.tools.renderercalendar.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/23.
 */
public abstract class SimpleRendererBuilder<T> extends BaseDayRendererBuilder<T> {
    private Map<String, T> map = new HashMap<>();

    public SimpleRendererBuilder(List<T> list, ToDateConvertor<T> convertor){
        resetContent(list, convertor);
    }

    public void resetContent(List<T> list, ToDateConvertor<T> convertor){
        for(T t : list)
            map.put(
                    dateKey(convertor.toDateString(t)),
                    t);
    }

    @Override
    protected T getContent(DayDate date, boolean isInThisMonth) {
        return map.get(dateKey(date.getYear(), date.getMonth() + 1, date.getDay()));
    }

    private String dateKey(int[] date){
        return String.format("%04d%02d%02d", date[0], date[1], date[2]);
    }

    private String dateKey(int year, int month, int day){
        return String.format("%04d%02d%02d", year, month, day);
    }

    public interface ToDateConvertor<T>{
        //return int[3]{Year, Month, Day}
        int[] toDateString(T data);
    }
}
