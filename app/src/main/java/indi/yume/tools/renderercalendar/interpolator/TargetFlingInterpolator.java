package indi.yume.tools.renderercalendar.interpolator;

import indi.yume.tools.renderercalendar.util.LogUtil;

/**
 * Created by yume on 15/11/10.
 */
public class TargetFlingInterpolator extends Interpolator {
    private static final float BASE_MIN_FLING_VELOCITY = 900;
    private static final float BASE_MAX_FLING_VELOCITY = 6000;
    private static final float BASE_FLING_DECELERATION = 1200;

    private float minFlingVelocity = BASE_MIN_FLING_VELOCITY;
    private float maxFlingVelocity = BASE_MAX_FLING_VELOCITY;
    private float defaultFlingDeceleration = BASE_FLING_DECELERATION;

    private float flingDeceleration = BASE_FLING_DECELERATION;

    private float minVeRate = 1;
    private float maxVeRate = 1;
    private float flingDeRate = 1;

    private int direct;
    private float currentVelocityX;
    private float startVelocityX;
    private boolean isOver = true;

    private float offsetSum = 0;

    private float mWidth;
    private long mPeriod;

    public TargetFlingInterpolator(){}

    public TargetFlingInterpolator(float width, long period){
        mWidth = width;
        mPeriod = period;

        resetValue();
    }

    public void setMinVeRate(float minVeRate) {
        this.minVeRate = minVeRate;
    }

    public void setMaxVeRate(float maxVeRate) {
        this.maxVeRate = maxVeRate;
    }

    public void setFlingDeRate(float flingDeRate) {
        this.flingDeRate = flingDeRate;
    }

    public void setInitValue(float width, long period){
        mWidth = width;
        mPeriod = period;

        resetValue();
    }

    public void resetValue(){
        minFlingVelocity = mWidth / 2 * minVeRate;
        maxFlingVelocity = mWidth * 3 * maxVeRate;
        defaultFlingDeceleration = mWidth * 3.5f * flingDeRate;
    }

    public float calculateDefaultTargetOffset(float startVelocityX){
        float velocityX = Math.abs(startVelocityX);
        velocityX = Math.min(velocityX, maxFlingVelocity);
        return Math.signum(startVelocityX) * velocityX * velocityX / 2 / defaultFlingDeceleration;
    }

    public void startScroll(float startVelocityX, float targetOffset){
        assert Math.signum(startVelocityX) == Math.signum(targetOffset);
        offsetSum = 0;
        direct = (int) Math.signum(startVelocityX);
        startVelocityX = Math.abs(startVelocityX);
        startVelocityX = Math.min(startVelocityX, maxFlingVelocity);
        this.startVelocityX = Math.abs(startVelocityX);
        this.currentVelocityX = startVelocityX;

        flingDeceleration = (startVelocityX * startVelocityX) / 2 / Math.abs(targetOffset);

        isOver = targetOffset == 0;
    }

    public boolean isOver(){
        return isOver;
    }

    public void stopScroll(){
        isOver = true;
        resetValue();
    }

    public float scroll(){
        float offset = 0;
//            Log.d(TAG, "currentVelocityX= " + currentVelocityX);
        if(currentVelocityX - flingDeceleration * (mPeriod / 1000f) < 0){
            isOver = true;
            offset = direct * calculateS(-flingDeceleration, currentVelocityX, currentVelocityX / flingDeceleration);
            LogUtil.m("offsetSum= " + offsetSum);
        } else {
            offset = direct * calculateS(-flingDeceleration, currentVelocityX, mPeriod / 1000f);
        }

        currentVelocityX -= flingDeceleration * (mPeriod / 1000f);
        offsetSum += offset;
        return offset;
    }

    private float calculateS(float A, float V0, float T){
        return V0 * T + A * T * T / 2;
    }
}
