package indi.yume.tools.renderercalendar.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yume on 16-4-29.
 */
@IntDef({ScrollMode.MULTIPLE_PAGE,
        ScrollMode.SINGLE_PAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface ScrollMode {
    public static final int MULTIPLE_PAGE = 0x11;
    public static final int SINGLE_PAGE = 0x22;
}
