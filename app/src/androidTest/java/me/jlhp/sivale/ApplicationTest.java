package me.jlhp.sivale;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() throws Exception {
        super(Application.class);

        setUp();

        hawkTest();
        setKeys();

        String test = getKeys();

        assertEquals("test1", test);
    }

    public void hawkTest() {
        //Hawk.init(this, getString(R.string.hawk));
        Context context = getContext();

        Hawk.init(context)
            .setEncryptionMethod(HawkBuilder.EncryptionMethod.HIGHEST)
            .setPassword(context.getString(R.string.hawk))
            .setStorage(HawkBuilder.newSharedPrefStorage(context))
            .setLogLevel(LogLevel.FULL)
            .build();
    }

    public void setKeys() {
        Hawk.put("test", "test1");
    }

    public String getKeys() {
        String test = Hawk.get("test");
        return test;
    }

}