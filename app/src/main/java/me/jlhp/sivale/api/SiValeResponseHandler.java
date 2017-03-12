package me.jlhp.sivale.api;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import me.jlhp.sivale.model.server.FaultData;
import me.jlhp.sivale.model.server.SiValeData;
import me.jlhp.sivale.utility.Logger;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 11/7/2015.
 */
public
    abstract class SiValeResponseHandler
        <ResponseType extends SiValeData>
    extends
        AsyncHttpResponseHandler {

    private final SiValeParser mSoapParser = new SiValeParser();
    private Class<ResponseType> mResponseClass;

    public SiValeResponseHandler(Class<ResponseType> responseClass) {
        this.mResponseClass = responseClass;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        ResponseType response = getSoapResponse(mResponseClass, responseBody);

        if(response == null) {
            Logger.getInstance().logError("null_response{" +
                    "statusCode='" + statusCode + '\'' +
                    ", headers=" + Arrays.deepToString(headers) +
                    ", responseBody=" + Arrays.toString(responseBody) +
                    '}');
            return;
        }

        Logger.getInstance().logInfo(response.toString());

        if (isInvalidLogin(response)) {
            onInvalidLogin();
        } else if (isSessionExpired(response)) {
            onSessionExpired();
        } else if (isError(response)) {
            onError(response.getError());
        } else {
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          byte[] responseBody, Throwable error) {
        String e = getError(responseBody, error);

        if(e == null) return;

        onError(e);
    }

    public abstract void onSuccess(ResponseType response);

    public void onSessionExpired() {
        Logger.getInstance().logInfo("Session is expired");
    }

    public void onInvalidLogin() {
        Logger.getInstance().logInfo("Login credentials are invalid");
    }

    public void onError(String response) {
        Logger.getInstance().logInfo(response);
    }

    private ResponseType getSoapResponse(Class<ResponseType> clazz, byte[] soapData) {
        return mSoapParser.parseSoapData(clazz, soapData);
    }

    private boolean isError(ResponseType data) {
        if (data == null) {
            throw new IllegalArgumentException("'data' can't ne null");
        }

        return data.isError();
    }

    private boolean isSessionExpired(SiValeData data) {
        if (data == null) {
            throw new IllegalArgumentException("'data' can't ne null");
        }

        return data.isSessionExpired();
    }

    private boolean isInvalidLogin(SiValeData data) {
        if (data == null) {
            throw new IllegalArgumentException("'data' can't ne null");
        }

        return data.isInvalidLogin();
    }

    private String getError(byte[] responseBody, Throwable error) {
        if(error == null) {
            FaultData soapError = mSoapParser.parseSoapData(FaultData.class, responseBody);

            Logger.getInstance().logError(soapError.toString());

            if(isInvalidLogin(soapError)) {
                onInvalidLogin();
                return null;
            }
            else if(isSessionExpired(soapError)) {
                onSessionExpired();
                return null;
            }

            return soapError.getError();
        }

        Logger.getInstance().logError(error.toString());

        String faultString = Util.isStringEmptyOrNull(error.getMessage()) ?
                "Error con SiVale":
                error.getMessage();

        if(error instanceof HttpResponseException) {
            SOAP11Fault fault = mSoapParser.parseSoapData(SOAP11Fault.class, responseBody);

            if(fault != null &&
               fault.getFaultString() != null &&
               fault.getFaultString().contains("NullPointerException")) {
                //It seems that when a SiVale session is very old and you try to use it
                //the server will throw a NullPointerException. This check will allow me
                //to retry the operation(s) by doing a login first.
                onSessionExpired();
                return null;
            }
        }
        else if(error instanceof ConnectTimeoutException ||
                error instanceof SocketTimeoutException ||
                error.getCause() instanceof ConnectTimeoutException ||
                error.getCause() instanceof SocketTimeoutException){
            return "No se pudo establecer una conexión con SiVale";
        }
        else if(error instanceof SocketException || error.getCause() instanceof SocketException){
            return "Error de conexión. Revise su conexión a internet.";
        }

        return faultString;
    }
}
