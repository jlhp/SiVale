package me.jlhp.sivale.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import me.jlhp.sivale.envelope.BaseEnvelope;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class SiValeClient {
    private static final String BASE_URL = "http://148.223.134.18:8888/bancamovil/WebMethods";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(Context context, BaseEnvelope envelope, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(), soapEnvelope2StringEntity(envelope), "text/xml", responseHandler);
    }

    private static String getAbsoluteUrl() {
        return BASE_URL;
    }

    private static StringEntity soapEnvelope2StringEntity(BaseEnvelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException("'envelope' must not be null");
        }

        StringEntity entity = null;

        try {
            entity = new StringEntity(envelope.toString());
            entity.setContentType("text/xml");
            entity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return entity;
    }
}
