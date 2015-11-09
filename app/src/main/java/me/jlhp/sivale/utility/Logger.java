package me.jlhp.sivale.utility;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

/**
 * Created by JOSELUIS on 11/8/2015.
 */
public class Logger {
    private static Logger mLogger;

    private Logger() {}

    public static Logger getInstance() {
        if(mLogger == null) {
            mLogger = new Logger();
        }

        return mLogger;
    }

    public void logError(String error) {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String tag = className + ":" + methodName;

        Crashlytics.log(Log.ERROR, tag, error);
    }

    public void logInfo(String info) {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String tag = className + ":" + methodName;

        Crashlytics.log(Log.INFO, tag, info);
    }
}
