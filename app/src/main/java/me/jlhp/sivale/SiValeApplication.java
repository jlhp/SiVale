package me.jlhp.sivale;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

/**
 * Created by JOSELUIS on 3/9/2015.
 */
public class SiValeApplication extends Application {

    @Override
    public void onCreate(){
        Hawk.init(this, getString(R.string.hawk));
    }
}
