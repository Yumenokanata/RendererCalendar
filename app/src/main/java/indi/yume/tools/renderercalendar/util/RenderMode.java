package indi.yume.tools.renderercalendar.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yume on 16-4-29.
 */
@IntDef({RenderMode.RENDER_MODE_ONLY_DAY,
        RenderMode.RENDER_MODE_ONLY_MONTH,
        RenderMode.RENDER_MODE_ALL_PAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface RenderMode {
    public static final int RENDER_MODE_ONLY_DAY = 0x11;
    public static final int RENDER_MODE_ONLY_MONTH = 0x22;
    public static final int RENDER_MODE_ALL_PAGE = 0x33;
}
