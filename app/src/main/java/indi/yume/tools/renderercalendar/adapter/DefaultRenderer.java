package indi.yume.tools.renderercalendar.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Calendar;

import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/10.
 */
public class DefaultRenderer extends BaseDayRenderer<Integer> {
    private static final int[] colorList = new int[]{
            0xfff87376,
            0xff777878,
            0xff777878,
            0xff777878,
            0xff777878,
            0xff777878,
            0xff00b9d4
    };

    private Paint textPaint;
    private float textOffsetY;

    private Paint selectPaint;

    public DefaultRenderer(){
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(Color.RED);

        selectPaint = new Paint();
        selectPaint.setAntiAlias(true);
        selectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectPaint.setStrokeWidth(1);
        selectPaint.setColor(0xffee2976);
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
        if(isSelect)
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 4, selectPaint);

        if(isSelect)
            textPaint.setColor(Color.WHITE);
        else
            textPaint.setColor(colorList[date.get(Calendar.DAY_OF_WEEK) - 1]);

        canvas.drawText(date.getDay() + "",
                (getWidth() - textPaint.measureText(date.getDay() + "")) / 2,
                textOffsetY,
                textPaint);
    }
}
