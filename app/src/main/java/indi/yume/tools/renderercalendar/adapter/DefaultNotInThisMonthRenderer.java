package indi.yume.tools.renderercalendar.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Calendar;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/12.
 */
public class DefaultNotInThisMonthRenderer extends BaseDayRenderer<Integer> {
    private Paint textPaint;
    private float textOffsetY;
    private static final int[] colorList = new int[]{
            0xfffde4e4,
            0xffe5e5e5,
            0xffe5e5e5,
            0xffe5e5e5,
            0xffe5e5e5,
            0xffe5e5e5,
            0xffd2f2f7
    };

    public DefaultNotInThisMonthRenderer(){
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(Color.RED);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        textPaint.setTextSize(width / 3.2f);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.ascent;
        textOffsetY = (height - textHeight) / 2 - fm.ascent;
    }

    @Override
    public void render(Canvas canvas, DayDate date, Integer content, boolean isInThisMonth, boolean isSelect) {
        textPaint.setColor(colorList[date.get(Calendar.DAY_OF_WEEK) - 1]);
        canvas.drawText(date.getDay() + "",
                (getWidth() - textPaint.measureText(date.getDay() + "")) / 2,
                textOffsetY,
                textPaint);
    }
}
