package indi.yume.tools.renderercalendar.adapter;

import java.util.ArrayList;
import java.util.List;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/10.
 */
public class DefaultRendererBuilder extends BaseDayRendererBuilder<Integer> {
    private List<Class<? extends BaseDayRenderer<Integer>>> renderList = new ArrayList<>();

    public DefaultRendererBuilder(){
        renderList.add(DefaultRenderer.class);
        renderList.add(DefaultNotInThisMonthRenderer.class);
    }

    @Override
    protected int getTypeClassIndex(Integer content, boolean isInThisMonth) {
        return isInThisMonth ? 0 : 1;
    }

    @Override
    protected List<Class<? extends BaseDayRenderer<Integer>>> getAllRendererClass() {
        return renderList;
    }

    @Override
    protected Integer getContent(DayDate date, boolean isInThisMonth) {
        return date.getDay();
    }
}
