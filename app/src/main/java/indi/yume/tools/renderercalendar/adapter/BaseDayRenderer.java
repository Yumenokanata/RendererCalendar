package indi.yume.tools.renderercalendar.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.TypedValue;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/8.
 */
public abstract class BaseDayRenderer<T> {

    private int width = 0;
    private int height = 0;

    public BaseDayRenderer(){}

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
        onSizeChanged(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected int sizeConvert(float size, Context context, int unit){
        return (int) TypedValue.applyDimension(unit,
                size,
                context.getResources().getDisplayMetrics());
    }

    public abstract void onSizeChanged(int width, int height);
    public abstract void render(Canvas canvas, DayDate date, T content, boolean isInThisMonth, boolean isSelect);
}
