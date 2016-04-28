package indi.yume.tools.renderercalendar.util;

import indi.yume.tools.renderercalendar.BuildConfig;

/**
 * Created by yume on 16/3/18.
 */
public class LogUtil {
    private static boolean debug = BuildConfig.DEBUG;

    private LogUtil(){}

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    public static void m(String tag, String msg) {
        if(debug)
            System.out.println(tag + "| " + msg);
    }

    public static void m(Class clazz, String msg) {
        m(clazz.getSimpleName(), msg);
    }

    public static void m(String msg) {
        if(debug)
            System.out.println(msg);
    }

    public static void e(Throwable t) {
        if(debug)
            t.printStackTrace();
    }
}
