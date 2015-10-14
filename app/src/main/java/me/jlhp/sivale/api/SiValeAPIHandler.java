package me.jlhp.sivale.api;

import android.content.Context;
import android.system.ErrnoException;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.R;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.event.FaultEvent;
import me.jlhp.sivale.event.GetBalanceEvent;
import me.jlhp.sivale.event.GetTransactionsEvent;
import me.jlhp.sivale.event.LoginEvent;
import me.jlhp.sivale.model.server.BalanceData;
import me.jlhp.sivale.model.server.SessionData;
import me.jlhp.sivale.model.server.SiValeData;
import me.jlhp.sivale.model.server.TransactionData;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 4/5/2015.
 */
public class SiValeAPIHandler {
    private final SiValeParser SoapParser = new SiValeParser();
    private final SiValeAPI API = new SiValeAPI();
    private boolean mRequestInProgress = false;

    public void login(Context context, String cardNumber, String password,
                      final int callerId, final SiValeOperation... nextOperations) {
        API.login(context, cardNumber, password, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                setRequestInProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                setRequestInProgress(false);
                SessionData sessionData = getSoapResponse(SessionData.class, responseBody);
                if (isError(sessionData))
                    postEvent(new ErrorEvent(sessionData.getError(),
                            callerId,
                            SiValeOperation.LOGIN,
                            nextOperations));
                else postEvent(new LoginEvent(sessionData,
                        callerId,
                        nextOperations));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                setRequestInProgress(false);
                SOAP11Fault soap11Fault = getSoapFault(responseBody, error);
                postEvent(new FaultEvent(soap11Fault, callerId, SiValeOperation.LOGIN, nextOperations));
            }
        });
    }

    public void getBalance(Context context, int sessionId,
                           final int callerId, final SiValeOperation... nextOperations) {
        API.getBalance(context, sessionId, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                setRequestInProgress(true);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                setRequestInProgress(false);
                BalanceData balanceData = getSoapResponse(BalanceData.class, responseBody);
                if (isError(balanceData))
                    postEvent(new ErrorEvent(balanceData.getError(),
                            callerId,
                            SiValeOperation.GET_BALANCE,
                            nextOperations));
                else
                    postEvent(new GetBalanceEvent(balanceData,
                            callerId,
                            nextOperations));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                setRequestInProgress(false);
                SOAP11Fault soap11Fault = getSoapFault(responseBody, error);
                postEvent(new FaultEvent(soap11Fault, callerId, SiValeOperation.GET_BALANCE, nextOperations));
            }
        });
    }

    public void getTransactions(Context context, int sessionId,
                                final int callerId, final SiValeOperation... nextOperations) {
        API.getTransactions(context, sessionId, new AsyncHttpResponseHandler() {
            @Override
            public void onStart(){
                setRequestInProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                setRequestInProgress(false);
                TransactionData transactionData = getSoapResponse(TransactionData.class,
                        responseBody);
                if (isError(transactionData))
                    postEvent(new ErrorEvent(transactionData.getError(),
                            callerId,
                            SiValeOperation.GET_TRANSACTIONS,
                            nextOperations));
                else
                    postEvent(new GetTransactionsEvent(transactionData,
                            callerId,
                            nextOperations));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                setRequestInProgress(false);
                SOAP11Fault soap11Fault = getSoapFault(responseBody, error);
                postEvent(new FaultEvent(soap11Fault, callerId, SiValeOperation.GET_TRANSACTIONS, nextOperations));
            }
        });
    }

    public boolean isRequestInProgress() {
        return mRequestInProgress;
    }

    private void postEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    private <T> T getSoapResponse(Class<T> clazz, byte[] soapData) {
        return SoapParser.parseSoapData(clazz, soapData);
    }

    private SOAP11Fault getSoapFault(byte[] responseBody, Throwable error) {
        if(error == null) {
            return SoapParser.parseSoapData(SOAP11Fault.class, responseBody);
        }
        else if(error instanceof HttpResponseException) {
            SOAP11Fault fault = SoapParser.parseSoapData(SOAP11Fault.class, responseBody);
            String faultString = Util.isStringEmptyOrNull(error.getMessage()) ?
                    "Error con SiVale":
                    error.getMessage();

            if(fault != null &&
               fault.getFaultString() != null &&
               fault.getFaultString().contains("NullPointerException")) {
                //It seems that when a SiVale session is very old and you try to use it
                //the server will throw a NullPointerException. This check will allow me
                //to retry the operation(s) by doing a login first.
                return new SOAP11Fault(null, "NO EXISTE SESION", null);
            }

            return new SOAP11Fault(null, faultString, null);
        }
        else if(error instanceof ConnectTimeoutException ||
                error instanceof SocketTimeoutException ||
                error.getCause() instanceof ConnectTimeoutException ||
                error.getCause() instanceof SocketTimeoutException){
            return new SOAP11Fault(null, "No se pudo establecer una conexi�n con SiVale", null);
        }
        else if(error instanceof SocketException || error.getCause() instanceof SocketException){
            return new SOAP11Fault(null, "Error de conexi�n. Revise su conexi�n a internet.", null);
        }
        else {
            String faultString = Util.isStringEmptyOrNull(error.getMessage()) ?
                                    "Error con SiVale":
                                    error.getMessage();
            return new SOAP11Fault(null, faultString, null);
        }
    }

    private boolean isError(SiValeData data) {
        if (data == null) {
            throw new IllegalArgumentException("'data' can't ne null");
        }

        return data.isError();
    }

    private void setRequestInProgress(boolean requestInProgress) {
        mRequestInProgress = requestInProgress;
    }
}
