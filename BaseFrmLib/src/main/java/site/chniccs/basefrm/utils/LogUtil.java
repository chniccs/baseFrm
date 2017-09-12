package site.chniccs.basefrm.utils;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * Created by chniccs on 2017/9/2 14:27.
 */

public class LogUtil {
    private static String TAG = "chniccs";
    private static boolean DEBUG = true;

    public static void init(boolean debug) {
        DEBUG = debug;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return DEBUG;
            }
        });
    }


    public static void d(Object o) {
        Logger.d(o);
    }

    public static void w(String o) {
        Logger.w(o);
    }

    public static void i(String o) {
        Logger.i(o);
    }

    public static void e(String o) {
        Logger.e(o);
    }
}
