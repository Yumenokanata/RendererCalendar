package indi.yume.tools.renderercalendar.interpolator;

/**
 * Created by yume on 15/10/10.
 */
public class InertiaScrollInterpolator extends Interpolator {
    private static final float BASE_INERTIA_MAX_VELOCITY = 1200;

    private float mInertiaDeceleration;
    private float mInertiaMaxVelocity = BASE_INERTIA_MAX_VELOCITY;
    private float inertiaMaxVeRate = 1;

    private int direct;
    private float startScrollDistance;
    private float endScrollDistance;
    private float cellTime;
    private float sumTime;
    private float currentTime;
    private boolean isScrollOver = true;

    private float backScroll = 0;

    private float mWidth;
    private long mPeriod;

    public InertiaScrollInterpolator(){}

    public InertiaScrollInterpolator(float width, long period){
        mWidth = width;
        mPeriod = period;

        resetValue();
    }

    public void setInitValue(float width, long period){
        mWidth = width;
        mPeriod = period;

        resetValue();
    }

    public void setInertiaMaxVeRate(float inertiaMaxVeRate) {
        this.inertiaMaxVeRate = inertiaMaxVeRate;
    }

    public void resetValue(){
        mInertiaMaxVelocity = mWidth / 1.4f * inertiaMaxVeRate;
    }

    public void startScroll(float startScrollDistance, float endScrollDistance){
        if(Math.abs(startScrollDistance - endScrollDistance) < 1){
            this.startScrollDistance = startScrollDistance;
            this.endScrollDistance = endScrollDistance;
            this.backScroll = startScrollDistance;
            calculateZeroDistance((float) Math.floor(endScrollDistance));
            return;
        }
        this.startScrollDistance = startScrollDistance;
        this.endScrollDistance = endScrollDistance;
        this.backScroll = startScrollDistance;

        direct = (int) Math.signum(endScrollDistance - startScrollDistance);
        mInertiaDeceleration = (2 * mInertiaMaxVelocity * mInertiaMaxVelocity) / Math.abs(endScrollDistance - startScrollDistance);
        cellTime = mInertiaMaxVelocity / mInertiaDeceleration;
        sumTime = 3 * cellTime;
        currentTime = 0;
        isScrollOver = false;
    }

    private void calculateZeroDistance(float distance){
        direct = 1;
        mInertiaDeceleration = 0;
        cellTime = mPeriod / 1000f / 3;
        sumTime = cellTime;
        currentTime = 2 * sumTime;
        isScrollOver = false;
    }

    public boolean isOver(){
        return isScrollOver;
    }

    public void stopScroll(){
        isScrollOver = true;
        resetValue();
    }

    public float scroll(){
        if(currentTime > sumTime) {
            isScrollOver = true;
            return endScrollDistance - backScroll;
        }
        currentTime += (mPeriod / 1000f);
//            Log.d(TAG, "switch " + Math.round(currentTime / cellTime - 0.5f));
//            Log.d(TAG, "currentTime= " + currentTime);
        float offset;
        switch (Math.round(currentTime / cellTime - 0.5f)){
            case 0:
                offset = 1f / 2 * mInertiaDeceleration * currentTime * currentTime;
                break;
            case 1:
                offset = 1f / 2 * mInertiaDeceleration * cellTime * cellTime + (currentTime - cellTime) * mInertiaMaxVelocity;
                break;
            default:
                float offsetTime = currentTime - cellTime * 2;
                offset = 1f / 2 * mInertiaDeceleration * cellTime * cellTime + cellTime * mInertiaMaxVelocity +
                        offsetTime * mInertiaMaxVelocity - 1f / 2 * mInertiaDeceleration * offsetTime * offsetTime;
                break;
        }
//            offsetX = startScrollX + direct * offsetX;
//            Log.d(TAG, "offsetX= " + offsetX);
        float offsetD = startScrollDistance + direct * offset - backScroll;
        backScroll = startScrollDistance + direct * offset;
        return offsetD;
    }
}
