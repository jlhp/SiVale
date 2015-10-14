package me.jlhp.sivale;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.crashlytics.android.Crashlytics;
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
    }
}
