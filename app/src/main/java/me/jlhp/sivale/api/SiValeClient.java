package me.jlhp.sivale.api;

import android.content.Context;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import cz.msebera.android.httpclient.entity.StringEntity;
import me.jlhp.sivale.envelope.BaseEnv;
import me.jlhp.sivale.envelope.BaseEnvelope;
import me.jlhp.sivale.envelope.BaseEnvelopeV2;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
class SiValeClient {
    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    private static final int MAX_RETRIES = 1;
    private static final String BASE_URL = "http://148.223.134.18:8888/bancamovil/WebMethods";

    private static AsyncHttpClient mAsyncClient = new AsyncHttpClient(true, 80, 443);
    private static SyncHttpClient mSyncClient = new SyncHttpClient(true, 80, 443);

    static {
        //mAsyncClient.setProxy("192.168.15.15", 8888);
        //mSyncClient.setProxy("192.168.15.15", 8888);
        mAsyncClient.setMaxRetriesAndTimeout(MAX_RETRIES, CONNECTION_TIMEOUT);
        mSyncClient.setMaxRetriesAndTimeout(MAX_RETRIES, CONNECTION_TIMEOUT);
    }

    static void post(Context context, BaseEnv envelope, ResponseHandlerInterface responseHandler, boolean mSyncMode) {
        if(envelope instanceof BaseEnvelopeV2) {
            mAsyncClient.post(context, "https://wls-wsprod.sivale.mx/sivale/Integracion/Servicios/AppSiVale", soapEnvelope2StringEntity(envelope), "text/xml", responseHandler);
        }
        else {
            if (mSyncMode) {
                responseHandler.setUseSynchronousMode(true);
                mSyncClient.post(context, getAbsoluteUrl(), soapEnvelope2StringEntity(envelope), "text/xml", responseHandler);
            } else {
                mAsyncClient.post(context, getAbsoluteUrl(), soapEnvelope2StringEntity(envelope), "text/xml", responseHandler);
            }
        }
    }

    private static String getAbsoluteUrl() {
        return BASE_URL;
    }

    private static StringEntity soapEnvelope2StringEntity(BaseEnv envelope) {
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
