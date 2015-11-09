package me.jlhp.sivale;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
//import com.orhanobut.hawk.HawkBuilder;
import com.crashlytics.android.Crashlytics;
//import com.orhanobut.hawk.LogLevel;

import io.fabric.sdk.android.Fabric;

/**
 * Created by JOSELUIS on 3/9/2015.
 */
public class SiValeApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        try {
            Fabric.with(this, new Crashlytics());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Hawk.init(this, getString(R.string.hawk));

/*       Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.HIGHEST)
                .setPassword(getString(R.string.hawk))
                .setStorage(new TestStorage(this, "HAWK"))
                .setLogLevel(LogLevel.FULL)
                .build();*/
    }
}
