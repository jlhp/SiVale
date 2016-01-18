package me.jlhp.sivale.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import me.jlhp.sivale.R;

/**
 * Created by JOSELUIS on 10/19/2015.
 */
public class SiValeAlarm extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = " me.jlhp.sivale.updater.SiValeAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        int syncFrequency = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(context.getString(R.string.pref_key_sync_frequency), 1);

        if(syncFrequency > 0) {
            Intent i = new Intent(context, SiValeService.class);
            context.startService(i);
        }
    }
}
