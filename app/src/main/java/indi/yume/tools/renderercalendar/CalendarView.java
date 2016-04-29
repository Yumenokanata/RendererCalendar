package indi.yume.tools.renderercalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import indi.yume.tools.renderercalendar.adapter.BaseDayRendererBuilder;
import indi.yume.tools.renderercalendar.adapter.DefaultRendererBuilder;
import indi.yume.tools.renderercalendar.interpolator.InertiaScrollInterpolator;
import indi.yume.tools.renderercalendar.interpolator.TargetFlingInterpolator;
import indi.yume.tools.renderercalendar.listener.GestureListener;
import indi.yume.tools.renderercalendar.listener.OnDayClickListener;
import indi.yume.tools.renderercalendar.listener.OnMonthChangedListener;
import indi.yume.tools.renderercalendar.model.DayDate;
import indi.yume.tools.renderercalendar.util.LogUtil;
import indi.yume.tools.renderercalendar.util.RenderMode;

/**
 * Created by yume on 15/9/29.
 */
public class CalendarView extends View {
    private static final String[] DEFAULT_ENG_TITLE = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String[] DEFAULT_JAP_TITLE = {"日", "月", "火", "水", "木", "金", "土"};

    private static final int MAX_CACHE_PAGE_COUNT = 3;
    private static final long mPeriod = (long) (1000 / 65f);

    private static final int BOLD_MASK = 0x1;
    private static final int ITALIC_MASK = 0x2;

    private BaseDayRendererBuilder rendererBuilder = new DefaultRendererBuilder();

    List<PageData> pageList = new ArrayList<>();

    private int width = -1;
    private int height = -1;

    private float offsetX = 0;

    private float titlePadding = -1;
    private float pageLRPadding = -1;
    private RectF padding = new RectF(5, 5, 5, 5);

    private RectF pageRect = new RectF();
    private RectF titleRect = new RectF();

    private DayDate toMonth;
    private DayDate selectDay;
    private boolean isSelected;

    private DayDate onTouchMonth = new DayDate();
    private DayDate minDay;

    private boolean showBorder;
    private int borderWidth;
    private int borderColor;

    private boolean showTitle;
    private float titleTextSize;
    private float titleHeight;
    //    private int titleTextColor;
    private String[] titles;

    private float changePageVeRate = 4f;
    private float backToOriOffsetVeRate = 1;

    private RectF titleCellRect = new RectF();
    private float titleTextHeight;
    private float titleTextOffsetY;

    private int[] titleColorList = new int[]{0xfff97c7f,
            0xff979797,
            0xff979797,
            0xff979797,
            0xff979797,
            0xff979797,
            0xff01bcd5};

    private Paint titlePaint;
    private int textStyle;

    private GestureDetector mGestureDetector;

    private TargetFlingInterpolator mFlingScroll = new TargetFlingInterpolator();
    private InertiaScrollInterpolator mInertiaScroll = new InertiaScrollInterpolator();
    private boolean isTouching = false;

    private FrameTimerTask mTimer;
    private Timer scrollDelayTimer;

    private int renderMode = RenderMode.RENDER_MODE_ONLY_DAY;

    private GestureListener gestureListener;

    private OnMonthChangedListener mOnMonthChangedListener;

    private boolean monthChanged = false;

    private BaseDayRendererBuilder.DataChangedListener mDataChangedListener = new BaseDayRendererBuilder.DataChangedListener() {
        @Override
        public void refresh() {
            dataChangedRefresh = true;
        }
    };
    private boolean dataChangedRefresh = false;

    private boolean scrollable = true;

    private DayDate willSelectDay = null;

    private boolean refreshAllPage = false;

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttr(context, attrs);
        init(context);
    }

    private void initAttr(Context context, AttributeSet attrs){
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);

        int paddingValue = tArray.getDimensionPixelSize(R.styleable.CalendarView_android_padding, 0);
        int paddingLeft = tArray.getDimensionPixelSize(R.styleable.CalendarView_android_paddingLeft, -1);
        int paddingRight = tArray.getDimensionPixelSize(R.styleable.CalendarView_android_paddingRight, -1);
        int paddingTop = tArray.getDimensionPixelSize(R.styleable.CalendarView_android_paddingTop, -1);
        int paddingBottom = tArray.getDimensionPixelSize(R.styleable.CalendarView_android_paddingBottom, -1);
        padding.set(paddingLeft != -1 ? paddingLeft : paddingValue,
                paddingTop != -1 ? paddingTop : paddingValue,
                paddingRight != -1 ? paddingRight : paddingValue,
                paddingBottom != -1 ? paddingBottom : paddingValue);

        showBorder = tArray.getBoolean(R.styleable.CalendarView_showBorderLine, true);
        borderWidth = tArray.getDimensionPixelSize(R.styleable.CalendarView_borderLineWidth, 2);
        borderColor = tArray.getColor(R.styleable.CalendarView_borderLineColor, 0xffe6e6e6);

        CharSequence[] title_temp;
        title_temp = tArray.getTextArray(R.styleable.CalendarView_titleList);
        if(title_temp == null) {
//            title_temp = context.getResources().getStringArray(R.array.week_title);
            if(tArray.getInt(R.styleable.CalendarView_titleStyle, 1) == 1)
                titles = DEFAULT_JAP_TITLE;
            else
                titles = DEFAULT_ENG_TITLE;
        } else {
            List<String> list = new ArrayList<>();
            for (CharSequence cs : title_temp)
                list.add(String.valueOf(cs));
            titles = list.toArray(new String[list.size()]);
        }

        showTitle = tArray.getBoolean(R.styleable.CalendarView_showTitle, true);
        titleTextSize = tArray.getDimensionPixelSize(R.styleable.CalendarView_titleTextSize, -1);
//        titleTextColor = tArray.getColor(R.styleable.CalendarView_titleTextColor, Color.BLACK);
        textStyle = tArray.getInt(R.styleable.CalendarView_titleTextShowStyle, 0);
        titlePadding = tArray.getDimensionPixelSize(R.styleable.CalendarView_titlePadding, -1);
        pageLRPadding = tArray.getDimensionPixelSize(R.styleable.CalendarView_pageLRPadding, -1);

        titleHeight = tArray.getDimensionPixelSize(R.styleable.CalendarView_titleHeight, -1);

        mFlingScroll.setMinVeRate(tArray.getFloat(R.styleable.CalendarView_minFlingVelocityRate, 1));
        mFlingScroll.setMaxVeRate(tArray.getFloat(R.styleable.CalendarView_maxFlingVelocityRate, 1));
        mFlingScroll.setFlingDeRate(tArray.getFloat(R.styleable.CalendarView_flingDecelerationRate, 1));
        mFlingScroll.resetValue();

        scrollable = tArray.getBoolean(R.styleable.CalendarView_scrollable, true);

        changePageVeRate = tArray.getFloat(R.styleable.CalendarView_changePageVeRate, changePageVeRate);
        backToOriOffsetVeRate = tArray.getFloat(R.styleable.CalendarView_backToOriOffsetVeRate, backToOriOffsetVeRate);
        mInertiaScroll.setInertiaMaxVeRate(backToOriOffsetVeRate);
        mInertiaScroll.resetValue();

        tArray.recycle();
    }

    private void init(Context context){
        toMonth = new DayDate();
        selectDay = new DayDate();

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
//        titlePaint.setColor(titleTextColor);
        titlePaint.setFakeBoldText((textStyle & BOLD_MASK) != 0);
        if((textStyle & ITALIC_MASK) != 0)
            titlePaint.setTextSkewX(-0.5f);

        mGestureDetector = new GestureDetector(context, new GestureDetectorListener());

        rendererBuilder.setDataChangedListener(mDataChangedListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mTimer == null) {
            mTimer = new FrameTimerTask();
            new Thread(mTimer).start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTimer.stop();
        mTimer = null;
    }

    class FrameTimerTask implements Runnable {
        private boolean isRun = true;

        public void stop(){
            isRun = false;
        }

        @Override
        public void run() {
            long periodTime = System.currentTimeMillis();

            while (isRun) {
                updateStatus();
                if (isDrawing()) {
                    postInvalidate();
                } else if (dataChangedRefresh) {
                    redrawCurrentPage();
                    postInvalidate();
                    dataChangedRefresh = false;
//                    System.out.println("Draw over; offsetX= " + offsetX);
                } else if (monthChanged && offsetX == 0
                        && (onTouchMonth.getYear() != toMonth.getYear() || onTouchMonth.getMonth() != toMonth.getMonth())) {
                    LogUtil.m("onMonthChangedOver");
                    if(mOnMonthChangedListener != null)
                        post(new Runnable() {
                            @Override
                            public void run() {
                                mOnMonthChangedListener.onMonthChangedOver(toMonth);
                            }
                        });

                    monthChanged = false;
                    postInvalidate();
//                    System.out.println("Draw over; offsetX= " + offsetX);
                } else if(willSelectDay != null) {
                    selectDay.setValue(willSelectDay);
                    isSelected = true;
                    willSelectDay = null;
                    for (PageData pageData : pageList)
                        if (pageData.getPage().getSelectDay() != null) {
                            pageData.getPage().setSelectDay(selectDay);
                            pageData.getPage().renderAllDays(pageData.getCanvas());
                        } else {
                            pageData.getPage().setSelectDay(selectDay);
                            if(pageData.getPage().getSelectDay() != null)
                                pageData.getPage().renderAllDays(pageData.getCanvas());
                        }
                    postInvalidate();
//                    System.out.println("Draw over; offsetX= " + offsetX);
                } else if(refreshAllPage) {
                    refreshAllPage = false;
                    for (PageData pageData : pageList)
                        pageData.getPage().renderAllDays(pageData.getCanvas());
                    postInvalidate();
                }

//            if(mFlingScroll.isOver() && mInertiaScroll.isOver()) {
//                mTimer.cancel();
//                mTimer = null;
//            }

                if (System.currentTimeMillis() - periodTime < mPeriod)
                    try {
                        Thread.sleep(
                                Math.max(0,
                                        Math.min(mPeriod,
                                                mPeriod - (System.currentTimeMillis() - periodTime))));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                periodTime = System.currentTimeMillis();
            }
        }
    }

    private boolean isDrawing(){
        return isTouching || !mFlingScroll.isOver() || !mInertiaScroll.isOver();
    }

    private void updateStatus(){
        if(!mFlingScroll.isOver()){
            offsetX += mFlingScroll.scroll();
            calculatePage();
            if(mFlingScroll.isOver())
                backOriginOffset();
        }

        if (!mInertiaScroll.isOver()) {
            offsetX += mInertiaScroll.scroll();

            calculatePage();
        }
    }

    private void backOriginOffset(){
        if(offsetX != 0){
            int pageWidth = (int) (pageLRPadding + pageRect.width());
            mInertiaScroll.resetValue();
            if(Math.abs(offsetX) > pageWidth / 2){
                mInertiaScroll.startScroll(offsetX, Math.signum(offsetX) * pageWidth);
            }else{
                mInertiaScroll.startScroll(offsetX, 0);
            }
        }
    }

    public void setMinDay(DayDate minDay) {
        this.minDay = minDay;
    }

    public void setOnDayClickListener(GestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    public void setOnMonthChangedListener(OnMonthChangedListener mOnMonthChangedListener) {
        this.mOnMonthChangedListener = mOnMonthChangedListener;
    }

    public void setRendererBuilder(BaseDayRendererBuilder rendererBuilder) {
        this.rendererBuilder = rendererBuilder;
        rendererBuilder.setDataChangedListener(mDataChangedListener);

        if(width != -1 && height != -1 && pageList.size() == 0)
            initPageList();
    }

    public void setTitleColor(int[] colorList){
        titleColorList = colorList;
        if(colorList.length != titles.length)
            throw new Error("CalendarView.setTitleColor(int[] colorList): colorList.length is wrong.");
    }

    public DayDate getCurrentMonth(){
        return toMonth;
    }

    public DayDate getSelectDay(){
        if(isSelected)
            return selectDay;
        return null;
    }

    public void moveToNextMonth(){
        if(offsetX != 0)
            return;

        int pageWidth = (int) (pageLRPadding + pageRect.width());
        mInertiaScroll.setInertiaMaxVeRate(changePageVeRate);
        mInertiaScroll.resetValue();
        mInertiaScroll.startScroll(offsetX, -pageWidth);
        mInertiaScroll.setInertiaMaxVeRate(backToOriOffsetVeRate);

        monthChanged = true;
    }

    public void moveToBackMonth(){
        if(offsetX != 0)
            return;

        int pageWidth = (int) (pageLRPadding + pageRect.width());
        mInertiaScroll.setInertiaMaxVeRate(changePageVeRate);
        mInertiaScroll.resetValue();
        mInertiaScroll.startScroll(offsetX, pageWidth);
        mInertiaScroll.setInertiaMaxVeRate(backToOriOffsetVeRate);

        monthChanged = true;
    }

    public void jumpToDay(DayDate targetDay, boolean select){
        if(offsetX != 0)
            return;
        monthChanged = true;

        toMonth.setValue(targetDay);
        for(int i = 0; i < pageList.size(); i++)
            resetPage(i, pageList.get(i));

        if(select) {
            this.selectDay.setValue(targetDay);
            isSelected = true;
        }

        if(select || isSelected){
            if(pageList.size() >= MAX_CACHE_PAGE_COUNT) {
                int index = MAX_CACHE_PAGE_COUNT / 2;
                PageData pd = pageList.get(index);

                pd.getPage().setSelectDay(selectDay);
                pd.getPage().renderAllDays(pd.getCanvas());
                postInvalidate();

                DayDate currentDate = pd.getPage().getMonth();
                int year = currentDate.getYear();
                int month = currentDate.getMonth();

                if(gestureListener != null)
                    gestureListener.onSelect(year, month, new DayDate(selectDay), pd.getPage().inSameMonth(selectDay));
            }
//            } else{
//                if(mOnDayClickListener != null)
//                    post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mOnDayClickListener.onDayClick(selectDay, true);
//                        }
//                    });
//            }
        }
        postOnMonthChangedListener();
    }

    public void setSelectDay(DayDate selectDay) {
        setSelectDay(selectDay, false);
    }

    public void setSelectDay(DayDate selectDay, boolean renderAllPage) {
        isSelected = true;

        if(renderAllPage) {
            for (PageData pageData : pageList)
                if (pageData.getPage().getSelectDay() != null) {
                    pageData.getPage().setSelectDay(selectDay);
                    pageData.getPage().renderAllDays(pageData.getCanvas());
                } else {
                    pageData.getPage().setSelectDay(selectDay);
                }
        } else {

        }

        postInvalidate();
    }

//    private void postOnDayClickListener(final PageData pd){
//        if(mOnDayClickListener != null) {
//            final DayDate selectDayTemp = new DayDate(selectDay);
//            final boolean isInThisMonth = pd.getPage().inSameMonth(selectDayTemp);
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    mOnDayClickListener.onDayClick(selectDayTemp, isInThisMonth);
//                }
//            });
//        }
//    }
//
//    private void postOnDayDoubleClickListener(final PageData pd){
//        if(mOnDayDoubleClickListener != null) {
//            final DayDate selectDayTemp = new DayDate(selectDay);
//            final boolean isInThisMonth = pd.getPage().inSameMonth(selectDayTemp);
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    mOnDayDoubleClickListener.onDayClick(selectDayTemp, isInThisMonth);
//                }
//            });
//        }
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(rendererBuilder != null && pageList.size() == 0)
            initPageList();
        else
            for(int i = 0; i < pageList.size(); i++) {
                PageData pd = pageList.get(i);
                changePageSize(pageRect, pd);
                resetPage(i, pd);
            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long time = System.currentTimeMillis();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = widthSize;
        height = heightSize;

        mFlingScroll.setInitValue(width, mPeriod);
        mInertiaScroll.setInitValue(width, mPeriod);

//        if(padding.left == 0 && padding.right == 0 && padding.top == 0 && padding.bottom == 0)
//            padding.set(0, width / 30, 0, width / 30);

        float pageWidth = width - padding.left - padding.right;
        float pageHeight = CalendarPage.onMeasureWidthWidth(pageWidth);

        if(showTitle) {
            if(titleHeight == -1 && titleTextSize == -1) {
                titleHeight = CalendarPage.measureTitleHeight(pageHeight);
                titleTextSize = titleHeight / 1.3f;
            } else if(titleHeight == -1){
                titleHeight = CalendarPage.measureTitleHeight(pageHeight);
            } else if(titleTextSize == -1){
                titleTextSize = titleHeight / 1.3f;
            }

            titlePaint.setTextSize(titleTextSize);

            Paint.FontMetrics fm = titlePaint.getFontMetrics();
            titleTextOffsetY = -fm.ascent;
            titleTextHeight = fm.bottom - fm.ascent;

            if(titlePadding == -1)
//                titlePadding = titleHeight / 5;
                titlePadding = 0;
        } else {
            //默认情况下titlePadding为-1,而在不显示Title的时候需要对其进行处理

//                titlePadding = titleHeight / 5;
            titlePadding = 0;
            titleTextHeight = 0;
        }

        if(pageLRPadding == -1)
            pageLRPadding = width / 30;
        float calendarHeight = titlePadding * 2 + titleHeight + pageHeight + padding.top + padding.bottom;

//        if(calendarHeight > height){
//            titleHeight = CalendarPage.measureTitleHeight(height);
//            pageHeight = height - titleHeight - titlePadding * 2 - padding.top - padding.bottom;
//
//            pageWidth = CalendarPage.onMeasureWidthHeight(pageHeight);
//            titleHeight = CalendarPage.measureTitleHeight(pageHeight);
//            pageHeight = height - titleHeight - titlePadding * 2 - padding.top - padding.bottom;
//        }

        titleRect.set((width - pageWidth) / 2,
                padding.top + titlePadding,
                (width - pageWidth) / 2 + pageWidth,
                padding.top + titlePadding + titleHeight);
        titleCellRect.set(titleRect.left,
                titleRect.top,
                titleRect.left + titleRect.width() / 7,
                titleRect.bottom);

        pageRect.set(titleRect.left,
                titleRect.bottom + titlePadding,
                titleRect.right,
                titleRect.bottom + titlePadding + pageHeight);

        height = (int) calendarHeight;

        setMeasuredDimension(width, height);

        LogUtil.m("onMeasure: " + (System.currentTimeMillis() - time));
    }

    private void initPageList(){
        for(int i = 0; i < MAX_CACHE_PAGE_COUNT; i++) {
            RectF rect = new RectF(pageRect);
            PageData pd = createPageData(rect);
            resetPage(i, pd);
            pageList.add(pd);
        }
    }

    private PageData createPageData(RectF rect){
        //4.4及以下 在边缘绘制会被掩盖，所以扩大一个像素
        Bitmap bitmap = Bitmap.createBitmap((int)rect.width() + 1, (int)rect.height() + 1, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);

        CalendarPage page = new CalendarPage(rendererBuilder, getContext());
        page.setSize((int) rect.width(), (int) rect.height());
        page.setBorderColor(borderColor);
        page.setBorderWidth(borderWidth);
        page.setShowBorder(showBorder);

        return new PageData(page, canvas, bitmap, rect);
    }

    private PageData changePageSize(RectF rect, PageData pd){
        //4.4及以下 在边缘绘制会被掩盖，所以扩大一个像素
        Bitmap bitmap = Bitmap.createBitmap((int)rect.width() + 1, (int)rect.height() + 1, Bitmap.Config.ARGB_4444);

        pd.getCanvas().setBitmap(bitmap);
        pd.getBitmap().recycle();
        pd.setBitmap(bitmap);

        pd.getPageRect().set(rect);

        pd.getPage().setSize((int) rect.width(), (int) rect.height());
        pd.getPage().setBorderWidth(borderWidth);

        return pd;
    }

    long onDrawStartTime = System.currentTimeMillis();
    @Override
    protected void onDraw(Canvas canvas) {
//        System.out.println("offsetX= " + offsetX);
        for(int i = 0; i < pageList.size(); i++) {
            PageData pd = pageList.get(i);
            canvas.drawBitmap(pd.getBitmap(), null, pd.getPageRect(), null);
        }

        if(showTitle){
            for(int i = 0; i < titles.length; i++) {
                titlePaint.setColor(titleColorList[i]);
                canvas.drawText(titles[i],
                        titleCellRect.left + titleCellRect.width() * i + (titleCellRect.width() - titlePaint.measureText(titles[i])) / 2,
                        titleCellRect.top + (titleCellRect.height() - titleTextHeight) / 2 + titleTextOffsetY,
                        titlePaint);
            }
        }

        LogUtil.m("onDraw spend time: " + (System.currentTimeMillis() - onDrawStartTime));
        onDrawStartTime = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                onTouchMonth.setValue(toMonth);

                if(scrollDelayTimer != null)
                    scrollDelayTimer.cancel();

                mInertiaScroll.stopScroll();
                mFlingScroll.stopScroll();
                LogUtil.m("Down");
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;

                scrollDelayTimer = new Timer();
                scrollDelayTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        backOriginOffset();
                    }
                }, 200);

                LogUtil.m("Up");
                break;
        }

        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class GestureDetectorListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(!scrollable)
                return false;

            monthChanged = true;

            offsetX -= distanceX;
            calculatePage();
            postInvalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(!scrollable)
                return false;

            monthChanged = true;

            int pageWidth = (int) (pageLRPadding + pageRect.width());
            float defaultOffset = mFlingScroll.calculateDefaultTargetOffset(velocityX);
            float offset = (Math.round((defaultOffset + offsetX) / pageWidth)) * pageWidth - offsetX;

            mFlingScroll.startScroll(velocityX, offset);
            if(scrollDelayTimer != null)
                scrollDelayTimer.cancel();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(offsetX != 0)
                return true;

            int index = MAX_CACHE_PAGE_COUNT / 2;
            PageData pd = pageList.get(index);
            float x = e.getX() - pageRect.left;
            float y = e.getY() - pageRect.top;
            DayDate currentDate = pd.getPage().getMonth();
            int year = currentDate.getYear();
            int month = currentDate.getMonth();

            DayDate touchDay = pd.getPage().getTouchDay(x, y);
            if(gestureListener == null || gestureListener.onClick(year, month, touchDay, pd.getPage().inSameMonth(touchDay))) {
                renderOnSelectDayChanged(touchDay);

                if(gestureListener != null)
                    gestureListener.onSelect(year, month, touchDay, pd.getPage().inSameMonth(touchDay));
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(offsetX != 0)
                return true;

            int index = MAX_CACHE_PAGE_COUNT / 2;
            PageData pd = pageList.get(index);
            float x = e.getX() - pageRect.left;
            float y = e.getY() - pageRect.top;
            DayDate currentDate = pd.getPage().getMonth();
            int year = currentDate.getYear();
            int month = currentDate.getMonth();

            DayDate touchDay = pd.getPage().getTouchDay(x, y);
            if(gestureListener == null || gestureListener.onDoubleClick(year, month, touchDay, pd.getPage().inSameMonth(touchDay))) {
                renderOnSelectDayChanged(touchDay);

                if(gestureListener != null)
                    gestureListener.onSelect(year, month, touchDay, pd.getPage().inSameMonth(touchDay));
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }
    }

    public void setRenderMode(@RenderMode int renderMode) {
        this.renderMode = renderMode;
    }

    void renderOnSelectDayChanged(DayDate willSelectDay) {
        switch (renderMode) {
            case RenderMode.RENDER_MODE_ONLY_DAY:
                renderOnlySelectDay(willSelectDay);
                break;
            case RenderMode.RENDER_MODE_ONLY_MONTH:
                renderOnlySelectPage(willSelectDay);
                break;
            case RenderMode.RENDER_MODE_ALL_PAGE:
                renderAllPage(willSelectDay);
                break;
        }
    }

    void renderAllPage(DayDate willSelectDay) {
        selectDay.setValue(willSelectDay);
        isSelected = true;

        for (PageData pageData : pageList)
            pageData.getPage().setSelectDay(selectDay);

        refreshAllPage = true;
    }

    void renderOnlySelectPage(DayDate willSelectDay) {
        this.willSelectDay = willSelectDay;
    }

    void renderOnlySelectDay(DayDate willSelectDay) {
        DayDate oldSelectDay = new DayDate(selectDay);
        selectDay.setValue(willSelectDay);
        isSelected = true;

        for(PageData pageData : pageList) {
            CalendarPage calendarPage = pageData.getPage();
            if(calendarPage.getSelectDay() != null) {
                calendarPage.setSelectDay(willSelectDay);
                if(calendarPage.inSameMonth(oldSelectDay))
                    calendarPage.renderDay(pageData.getCanvas(), oldSelectDay);
            } else {
                calendarPage.setSelectDay(willSelectDay);
            }

            if(calendarPage.inSameMonth(willSelectDay))
                calendarPage.renderDay(pageData.getCanvas(), willSelectDay);
        }

        postInvalidate();
    }

    private void calculatePage(){
        if(minDay != null && minDay.getYear() == toMonth.getYear() && minDay.getMonth() == toMonth.getMonth() && offsetX > 0)
            offsetX = 0;

        int pageWidth = (int) (pageLRPadding + pageRect.width());
        for(int i = 0; i < pageList.size(); i++){
            PageData pd = pageList.get(i);
            pd.getPageRect().offsetTo(
                    pageRect.left + offsetX + pageWidth * (i - MAX_CACHE_PAGE_COUNT / 2),
                    pageRect.top);
        }
        while(offsetX <= -pageWidth || offsetX >= pageWidth){
            if(offsetX <= -pageWidth){
                offsetX += pageWidth;
                if(Math.abs(offsetX - 0) < 1)
                    offsetX = 0;
                toMonth.addMonth(1);

                PageData pd = pageList.remove(0);
                pageList.add(pd);
                resetPage(pageList.size() - 1, pd);

                postOnMonthChangedListener();
                continue;
            }

            if(offsetX >= pageWidth){
                offsetX -= pageWidth;
                if(Math.abs(offsetX - 0) < 1)
                    offsetX = 0;
                toMonth.addMonth(-1);

                PageData pd = pageList.remove(pageList.size() - 1);
                pageList.add(0, pd);
                resetPage(0, pd);

                postOnMonthChangedListener();
                continue;
            }
        }
    }

    private void postOnMonthChangedListener(){
        if(mOnMonthChangedListener != null)
            post(new Runnable() {
                @Override
                public void run() {
                    mOnMonthChangedListener.onMonthChanged(toMonth);
                }
            });
    }

    private void resetPage(int index, PageData page) {
        page.getPage().setDate(toMonth, index - MAX_CACHE_PAGE_COUNT / 2);
        page.getCanvas().drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        page.getPageRect().offsetTo(
                pageRect.left + offsetX + (pageLRPadding + pageRect.width()) * (index - MAX_CACHE_PAGE_COUNT / 2),
                pageRect.top);

        if(isSelected)
            page.getPage().setSelectDay(selectDay);

        page.getPage().renderAllDays(page.getCanvas());
    }

    private void redrawCurrentPage(){
        redrawPage(pageList.get(MAX_CACHE_PAGE_COUNT / 2));
    }

    private void redrawAllPage(){
        for(PageData pd : pageList)
            redrawPage(pd);
    }

    private void redrawPage(PageData page){
        page.getPage().renderAllDays(page.getCanvas());
    }

    private static class PageData{
        private CalendarPage page;
        private Canvas canvas;
        private Bitmap bitmap;
        private RectF pageRect;

        public PageData(CalendarPage page, Canvas canvas, Bitmap bitmap, RectF pageRect) {
            this.page = page;
            this.canvas = canvas;
            this.bitmap = bitmap;
            this.pageRect = pageRect;
        }

        public CalendarPage getPage() {
            return page;
        }

        public void setPage(CalendarPage page) {
            this.page = page;
        }

        public Canvas getCanvas() {
            return canvas;
        }

        public void setCanvas(Canvas canvas) {
            this.canvas = canvas;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public RectF getPageRect() {
            return pageRect;
        }

        public void setPageRect(RectF pageRect) {
            this.pageRect = pageRect;
        }
    }
}
