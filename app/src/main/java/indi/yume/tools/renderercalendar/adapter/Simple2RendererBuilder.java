package indi.yume.tools.renderercalendar.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/23.
 */
public abstract class Simple2RendererBuilder<R, T> extends BaseDayRendererBuilder<T> {
    private Map<String, T> map = new HashMap<>();

    private ToDateConvertor<R> convertor;
    private ToTargetData<R, T> toTargetData;

    public Simple2RendererBuilder(List<R> list, ToDateConvertor<R> convertor, ToTargetData<R, T> toTargetData){
        this.convertor = convertor;
        this.toTargetData = toTargetData;
        resetContent(list);
    }

    public void resetContent(List<R> list){
        if(list == null)
            list = new ArrayList<>();
        for(R t : list)
            map.put(
                    dateKey(convertor.toDateString(t)),
                    toTargetData.convertData(t));
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

    public interface ToTargetData<R, T>{
        T convertData(R data);
    }
}
