package me.jlhp.sivale;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.HttpEntity;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class SiValeClient {
    private static final String BASE_URL = "http://148.223.134.18:8888/bancamovil/WebMethods";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(Context context, HttpEntity httpEntity, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(), httpEntity, "text/xml", responseHandler);
    }

    private static String getAbsoluteUrl() {
        return BASE_URL;
    }
}
