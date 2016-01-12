package indi.yume.tools.renderercalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import indi.yume.tools.renderercalendar.adapter.BaseDayRendererBuilder;
import indi.yume.tools.renderercalendar.model.DayDate;

/**
 * Created by yume on 15/10/8.
 */
public class CalendarPage {
    private BaseDayRendererBuilder rendererBuilder;
    private List<CanvasData> dayCanvasList = new ArrayList<>();
    private Context context;

    private float mWidth;
    private float mHeight;

    private static int rowNum = 6;
    private static int columnNum = 7;

    private DayDate startDate = new DayDate();
    private DayDate tempDate = new DayDate();

    private DayDate mDate = new DayDate();
    private DayDate selectDay = new DayDate();
    private boolean isSelect = false;

    private Paint borderPaint;
    private boolean showBorder = true;
    private int borderWidth;
    private int borderColor;

    public CalendarPage(BaseDayRendererBuilder builder, Context context){
        this(builder, context, true);
    }

    public CalendarPage(BaseDayRendererBuilder builder, Context context, boolean showBorder){
        rendererBuilder = builder;
        this.showBorder = showBorder;
        this.context = context;

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
    }

    public boolean inSameMonth(DayDate day){
        return day.getYear() == mDate.getYear() && day.getMonth() == mDate.getMonth();
    }

    public DayDate getMonth(){
        return mDate;
    }

    public DayDate getSelectDay(){
        if(isSelect)
            return selectDay;
        return null;
    }

    public void setSelectDay(DayDate day){
        if(inSameMonth(day)) {
            isSelect = true;
            selectDay.setValue(day);
//        selectDay.setDay(day.getDay());
        } else {
            isSelect = false;
            selectDay.setValue(day);
        }
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        borderPaint.setStrokeWidth(borderWidth);
        this.borderWidth = borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        borderPaint.setColor(borderColor);
        this.borderColor = borderColor;
    }

    public void setDate(DayDate date){
        this.setDate(date, 0);
    }

    public void setDate(DayDate date, int offsetMonth){
        mDate.setValue(date);
        mDate.setDay(1);
        mDate.addMonth(offsetMonth);

        selectDay.setValue(mDate);

        isSelect = false;

        startDate.setValue(mDate);

        int dayOfWeek = mDate.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek != Calendar.SUNDAY)
            startDate.addDay(-(dayOfWeek - 1));
    }

    public static float onMeasureWidthWidth(float width){
        return width / columnNum * rowNum;
    }

    public static float onMeasureWidthHeight(float height){
        return height / rowNum * columnNum;
    }

    public static float measureTitleHeight(float height){
        return height / rowNum / 3;
    }

    public void setSize(float width, float height){
        mWidth = width;
        mHeight = height;

        dayCanvasList.clear();
        float cellWidth = width / columnNum;
        float cellHeight = height / rowNum;
        for(int i = 0; i < rowNum; i++)
            for(int j = 0; j < columnNum; j++){
                RectF rect = new RectF(cellWidth * j,
                        cellHeight * i,
                        cellWidth * (j + 1),
                        cellHeight * (i + 1));
                CanvasData canvasData = new CanvasData(rect);
                dayCanvasList.add(canvasData);
            }
        rendererBuilder.onSizeChanged((int)cellWidth, (int)cellHeight, context);
    }

    public void renderAllDays(Canvas canvas){
        tempDate.setValue(startDate);
        for(int i = 0; i < rowNum; i++)
            for(int j = 0; j < columnNum; j++){
                CanvasData canvasData = dayCanvasList.get(i * columnNum + j);
                canvasData.setDate(tempDate);
//                canvasData.getCanvas().drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                canvas.save();
                RectF rect = canvasData.getRect();
                canvas.clipRect(rect);
                canvas.translate(rect.left, rect.top);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                rendererBuilder.render(tempDate,
                        tempDate.getMonth() == mDate.getMonth(),
                        isSelect && tempDate.equals(selectDay),
                        canvas);
                canvas.restore();

                tempDate.addDay(1);
            }

        drawBorder(canvas);
    }

    public void renderSelectDay(Canvas canvas){
        if(isSelect)
            this.renderDay(canvas, selectDay);
    }

    public void renderDay(Canvas canvas, DayDate date){
        for(CanvasData cd : dayCanvasList)
            if(date.equals(cd.getDate())){
                canvas.save();
                RectF rect = cd.getRect();
                canvas.clipRect(rect);
                canvas.translate(rect.left, rect.top);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                rendererBuilder.render(date,
                        date.getMonth() == mDate.getMonth(),
                        isSelect && date.equals(selectDay),
                        canvas);
                canvas.restore();
            }

        drawBorder(canvas);
    }

    private void drawBorder(Canvas canvas){
        if(showBorder) {
            canvas.drawLine(mWidth - borderWidth / 2, 0, mWidth - borderWidth / 2, mHeight, borderPaint); //right
            canvas.drawLine(0, mHeight - borderWidth / 2, mWidth, mHeight - borderWidth / 2, borderPaint); //bottom
            canvas.drawLine(borderWidth / 2, 0, borderWidth / 2, mHeight, borderPaint); //left
            canvas.drawLine(0, borderWidth / 2, mWidth, borderWidth / 2, borderPaint); //top
            for (int i = 1; i < rowNum; i++)
                canvas.drawLine(0, mHeight / rowNum * i, mWidth, mHeight / rowNum * i, borderPaint);
            for (int j = 1; j < columnNum; j++)
                canvas.drawLine(mWidth / columnNum * j, 0, mWidth / columnNum * j, mHeight, borderPaint);
        }
    }

    public boolean onSingleTapUp(float x, float y){
        for(CanvasData cd : dayCanvasList)
            if(cd.getRect().contains(x, y)) {
                selectDay.setValue(cd.getDate());
                isSelect = true;
                return true;
            }
        return false;
    }

    private static class CanvasData{
        private RectF rect;
        private DayDate date = new DayDate();

        public CanvasData(RectF rect) {
            this.rect = rect;
        }

        public RectF getRect() {
            return rect;
        }

        public void setRect(RectF rect) {
            this.rect = rect;
        }

        public DayDate getDate() {
            return date;
        }

        public void setDate(DayDate date) {
            this.date.setValue(date);
        }
    }
}
