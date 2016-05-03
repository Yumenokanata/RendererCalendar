package indi.yume.tools.renderercalendar.adapter;

import android.content.Context;
import android.graphics.Canvas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/8.
 */
public abstract class BaseDayRendererBuilder<T> {
    private Map<Class<? extends BaseDayRenderer>, BaseDayRenderer<T>> renderMap = new HashMap<>();

    private int width = 0;
    private int height = 0;

    private DataChangedListener mDataChangedListener;

    public void setDataChangedListener(DataChangedListener dataChangedListener) {
        this.mDataChangedListener = dataChangedListener;
    }

    private void initRendererMap(Context context){
        for(Class<? extends BaseDayRenderer<T>> clazz : getAllRendererClass()){
            try {
                BaseDayRenderer<T> render = clazz.newInstance();
                if(render instanceof ContextAware)
                    ((ContextAware)render).setContext(context);

                renderMap.put(clazz, render);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSizeChanged(int width, int height, Context context){
        this.width = width;
        this.height = height;

        initRendererMap(context);
        for(Class<? extends BaseDayRenderer> clazz : renderMap.keySet())
            renderMap.get(clazz).setSize(width, height);
    }

    public void render(DayDate date, boolean isInThisMonth, boolean isSelect, Canvas canvas){
        BaseDayRenderer<T> render = getRender(date, isInThisMonth);
        if(render != null)
            render.render(canvas, date, getContent(date, isInThisMonth), isInThisMonth, isSelect);
    }

    private BaseDayRenderer<T> getRender(DayDate date, boolean isInThisMonth){
        Class<? extends BaseDayRenderer<T>> clazz = getAllRendererClass()
                .get(getTypeClassIndex(
                        getContent(date, isInThisMonth),
                        isInThisMonth));
        if(renderMap.containsKey(clazz))
            return renderMap.get(clazz);

        try {
            BaseDayRenderer<T> render = clazz.newInstance();
            render.setSize(width, height);
            renderMap.put(clazz, render);
            return render;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void notifyThisPageDataHasChanged(){
        if(mDataChangedListener != null)
            mDataChangedListener.refreshThisPage();
    }

    public void notifyAllPageDataHasChanged(){
        if(mDataChangedListener != null)
            mDataChangedListener.refreshAllPage();
    }

    public void notifyDataHasChanged(DayDate dayDate){
        if(mDataChangedListener != null)
            mDataChangedListener.refresh(dayDate);
    }

    protected abstract int getTypeClassIndex(T content, boolean isInThisMonth);
    protected abstract List<Class<? extends BaseDayRenderer<T>>> getAllRendererClass();

    protected abstract T getContent(DayDate date, boolean isInThisMonth);

    public static interface DataChangedListener{
        void refreshThisPage();
        void refreshAllPage();
        void refresh(DayDate dayDate);
    }
}
