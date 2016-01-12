package indi.yume.tools.renderercalendar.interpolator;

/**
 * Created by yume on 15/10/10.
 */
public abstract class Interpolator {
    boolean isOver = true;

    public abstract void resetValue();

    public abstract float scroll();

    public boolean isOver(){
        return isOver;
    }

    public void stopScroll(){
        isOver = true;
    }
}
