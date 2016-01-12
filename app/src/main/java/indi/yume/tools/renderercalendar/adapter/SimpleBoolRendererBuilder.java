package indi.yume.tools.renderercalendar.adapter;

import java.util.List;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/23.
 */
public abstract class SimpleBoolRendererBuilder<T> extends Simple2RendererBuilder<T, Boolean> {
    public SimpleBoolRendererBuilder(List<T> list, ToDateConvertor<T> convertor) {
        super(list, convertor, new ToTargetData<T, Boolean>() {
            @Override
            public Boolean convertData(T data) {
                return true;
            }
        });
    }

    @Override
    public Boolean getContent(DayDate date, boolean isInThisMonth) {
        return isInThisMonth && super.getContent(date, isInThisMonth) != null;
    }
}
