package me.jlhp.sivale.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.updater.SiValeAlarm;

/**
 * Created by JOSELUIS on 3/8/2015.
 */
public class Util {

    private Util() { }

    public static void setSiValeSynchronizationAlarm(Context context, int syncFrequency){
        Intent intent = new Intent(context.getApplicationContext(), SiValeAlarm.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, SiValeAlarm.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(syncFrequency == 0) {
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pIntent);
        }
        else {
            long firstMillis = System.currentTimeMillis();
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    syncFrequency*1000, pIntent);
        }
    }

    public static boolean isStringEmptyOrNull(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isStringEmptyNullOrStringNull(String s) {
        return s == null || s.trim().length() == 0 || s.trim().equals("null");
    }

    public static <T> List<T> collection2List(Collection<T> collection) {
        ArrayList<T> items = new ArrayList<>();

        if(collection == null || collection.isEmpty()) return items;

        for(T item : collection) {
            items.add(item);
        }

        return items;
    }

    public static boolean isStringInteger(String s) {
        if(isStringEmptyOrNull(s)) return false;

        for(int i = 0; i < s.length(); i++) {
            try {
                Integer.parseInt(s.charAt(i) + "");
            }
            catch (NumberFormatException ex) {
                return false;
            }
        }

        return true;
    }

    public static String getStringFromTextView(TextView t) {
        if(t == null || t.getText() == null) return "";
        return t.getText().toString();
    }

    public static void showToast(Context c, String text) {
        showToast(c, text, true);
    }

    public static void showToast(Context c, String text, boolean onCondition) {
        if(onCondition) Toast.makeText(c, text, Toast.LENGTH_LONG).show();
    }

    public static void registerEventBus(Object o) {
        EventBus.getDefault().registerSticky(o);
    }

    public static void unregisterEventBus(Object o) {
        EventBus.getDefault().unregister(o);
    }

    public static void unregisterStickyEvent(Object o) {
        EventBus.getDefault().removeStickyEvent(o);
    }

    @SafeVarargs
    public static <T> T[] addItemsToArray(T[] array, T... items) {
        if(array == null || array.length == 0 || items == null || items.length == 0) return array;

        T[] newArray = Arrays.copyOf(array, array.length + items.length);
        System.arraycopy(items, 0, newArray, array.length, items.length);

        return newArray;
    }

    @SafeVarargs
    public static <T> T[] addItemsToArray(T item, T... items) {
        if(item == null) return items;

        @SuppressWarnings("unchecked")
        T[] t = (T[]) Array.newInstance(item.getClass(), 1);
        t[0] = item;

        return addItemsToArray(t, items);
    }

    public static <T> T[] removeFirstItemFromArray(T[] array) {
        if(array == null) return array;

        array = array.length > 1 ?
                Arrays.copyOfRange(array, 1, array.length) :
                null;

        return array;
    }

    public static Date parseStringDate(String date, String... formats) {
        if(date == null) return null;

        Date d;

        for(String f : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat(f, Locale.US);

            try {
                d = sdf.parse(date.trim());
            } catch (ParseException e) {
                logInfo("Parsing error", "Date " + date + " wasn't parsed by format" + f);
                continue;
            }

            return d;
        }

        throw new IllegalStateException("Unable to parse date " + date + " using formats " + formats);
    }

    public static void logError(String tag, String error) {
        Crashlytics.log(Log.ERROR, tag, error);
    }

    public static void logInfo(String tag, String info) {
        Crashlytics.log(Log.INFO, tag, info);
    }
}
