package indi.yume.tools.renderercalendar.interpolator;

/**
 * Created by yume on 15/10/10.
 */
@Deprecated
public class FlingScrollInterpolator extends Interpolator {
    private static final float BASE_MIN_FLING_VELOCITY = 900;
    private static final float BASE_MAX_FLING_VELOCITY = 6000;
    private static final float BASE_FLING_DECELERATION = 1200;

    private float minFlingVelocity = BASE_MIN_FLING_VELOCITY;
    private float maxFlingVelocity = BASE_MAX_FLING_VELOCITY;
    private float flingDeceleration = BASE_FLING_DECELERATION;

    private float minVeRate = 1;
    private float maxVeRate = 1;
    private float flingDeRate = 1;

    private int direct;
    private float currentVelocityX;
    private float startVelocityX;
    private boolean isOver = true;

    private float mWidth;
    private long mPeriod;

    public FlingScrollInterpolator(){}

    public FlingScrollInterpolator(float width, long period){
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
        flingDeceleration = mWidth / 5 * flingDeRate;
    }

    public void startScroll(float startVelocityX){
        direct = (int) Math.signum(startVelocityX);
        startVelocityX = Math.abs(startVelocityX);
        startVelocityX = Math.min(startVelocityX, maxFlingVelocity);
        this.startVelocityX = Math.abs(startVelocityX);
        this.currentVelocityX = startVelocityX;
        isOver = false;
    }

    public boolean isOver(){
        return isOver;
    }

    public void stopScroll(){
        isOver = true;
        resetValue();
    }

    public float scroll(){
        currentVelocityX -= flingDeceleration;
//            Log.d(TAG, "currentVelocityX= " + currentVelocityX);
        if(currentVelocityX < minFlingVelocity){
            isOver = true;
            return direct * minFlingVelocity * (mPeriod / 1000f);
        }
//            Log.d(TAG, "direct * currentVelocityX * (mPeriod / 1000)= " + (direct * currentVelocityX * (mPeriod / 1000f)));

        return direct * currentVelocityX * (mPeriod / 1000f);
    }
}
