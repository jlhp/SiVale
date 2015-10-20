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
import java.util.Arrays;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.R;
import me.jlhp.sivale.database.SiValeDataHandler;
import me.jlhp.sivale.event.CardOperation;
import me.jlhp.sivale.event.CardOperationEvent;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.event.FaultEvent;
import me.jlhp.sivale.event.GetBalanceEvent;
import me.jlhp.sivale.event.GetTransactionsEvent;
import me.jlhp.sivale.event.LoginEvent;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.server.BalanceData;
import me.jlhp.sivale.model.server.FaultData;
import me.jlhp.sivale.model.server.SessionData;
import me.jlhp.sivale.model.server.SiValeData;
import me.jlhp.sivale.model.server.TransactionData;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 4/5/2015.
 */

/*
TODO This class does too much stuff... At least subclass AsyncHttpResponseHandler.
TODO Also, maybe I should be calling the API, and the API the handler, and not backwards... I don't know.
*/
public class SiValeAPIHandler {
    private final SiValeParser SoapParser = new SiValeParser();
    private final SiValeAPI API = new SiValeAPI();
    private boolean mRequestInProgress = false;
    private SiValeDataHandler mSiValeData = new SiValeDataHandler();

    public SiValeAPIHandler() {

    }

    public SiValeAPIHandler(boolean syncMode) {
        API.setSyncMode(syncMode);
    }

    public void login(final Context context, final String cardNumber, String password,
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
                else {
                    Card card = mSiValeData.updateCardProperty(context,
                            callerId,
                            Card.SiValeCardProperty.SESSION_ID,
                            sessionData.getSessionId());

                    if (nextOperations == null || nextOperations.length == 0) {
                        postEvent(new CardOperationEvent(card,
                                CardOperation.UPDATE_DATA,
                                nextOperations));
                    } else {
                        executeOperation(context, card, nextOperations);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                setRequestInProgress(false);
                FaultData faultData = getSoapFault(responseBody, error);

                postEvent(new ErrorEvent(faultData.getError(), callerId, SiValeOperation.GET_TRANSACTIONS, nextOperations));
            }
        });
    }

    public void getBalance(final Context context, int sessionId,
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

                if (isSessionExpired(balanceData)) {
                    loginAndRetry(context, callerId, nextOperations);
                } else if (isError(balanceData))
                    postEvent(new ErrorEvent(balanceData.getError(),
                            callerId,
                            SiValeOperation.GET_BALANCE,
                            nextOperations));
                else {
                    Card card = mSiValeData.updateCardProperty(context,
                            callerId,
                            Card.SiValeCardProperty.BALANCE,
                            balanceData.getBalance());

                    if (nextOperations == null || nextOperations.length == 0) {
                        postEvent(new CardOperationEvent(card,
                                CardOperation.UPDATE_DATA,
                                nextOperations));
                    } else {
                        executeOperation(context, card, nextOperations);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                setRequestInProgress(false);
                FaultData faultData = getSoapFault(responseBody, error);

                if (isSessionExpired(faultData)) {
                    loginAndRetry(context, callerId, nextOperations);
                } else {
                    postEvent(new ErrorEvent(faultData.getError(), callerId, SiValeOperation.GET_TRANSACTIONS, nextOperations));
                }
            }
        });
    }

    public void getTransactions(final Context context, int sessionId,
                                final int callerId, final SiValeOperation... nextOperations) {
        API.getTransactions(context, sessionId, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                setRequestInProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                setRequestInProgress(false);
                TransactionData transactionData = getSoapResponse(TransactionData.class,
                        responseBody);
                if (isSessionExpired(transactionData)) {
                    loginAndRetry(context, callerId, nextOperations);
                } else if (isError(transactionData))
                    postEvent(new ErrorEvent(transactionData.getError(),
                            callerId,
                            SiValeOperation.GET_TRANSACTIONS,
                            nextOperations));
                else {
                    Card card = mSiValeData.updateCardProperty(context,
                            callerId,
                            Card.SiValeCardProperty.TRANSACTIONS,
                            transactionData.getTransactions());

                    if (nextOperations == null || nextOperations.length == 0) {
                        postEvent(new CardOperationEvent(card,
                                CardOperation.UPDATE_DATA,
                                nextOperations));
                    } else {
                        executeOperation(context, card, nextOperations);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                setRequestInProgress(false);
                FaultData faultData = getSoapFault(responseBody, error);

                if (isSessionExpired(faultData)) {
                    loginAndRetry(context, callerId, nextOperations);
                } else {
                    postEvent(new ErrorEvent(faultData.getError(), callerId, SiValeOperation.GET_TRANSACTIONS, nextOperations));
                }
            }
        });
    }

    public void updateCard(Context context, Card card) {
        if(context == null || card == null) throw new IllegalArgumentException("'context' and 'card' should not be null");

        executeOperation(context, card, SiValeOperation.GET_BALANCE, SiValeOperation.GET_TRANSACTIONS);
    }

    public void executeOperation(Context context,
                                 Card card,
                                 SiValeOperation... operations) {

        if(isRequestInProgress()) {
            Util.showToast(context, "Favor de esperar un momento");
            return;
        }
        else if(card == null || operations == null || operations.length == 0) {
            return;
        }
        else if(card.getSessionId() <= 0 && operations[0] != SiValeOperation.LOGIN) {
            login(context, card.getNumber(), card.getPassword(), card.getId(), operations);
            return;
        }

        SiValeOperation executeOperation = operations[0];
        operations = operations.length > 1 ?
                Arrays.copyOfRange(operations, 1, operations.length) :
                null;

        switch (executeOperation) {
            case LOGIN:
                login(context, card.getNumber(), card.getPassword(), card.getId(), operations);
                break;
            case GET_BALANCE:
                getBalance(context, card.getSessionId(), card.getId(), operations);
                break;
            case GET_TRANSACTIONS:
                getTransactions(context, card.getSessionId(), card.getId(), operations);
                break;
        }
    }

    public void loginAndRetry(Context context, int callerId, SiValeOperation... operations) {
        executeOperation(context,
                         mSiValeData.getCard(context, callerId),
                         Util.addItemsToArray(SiValeOperation.LOGIN, operations));
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

    private FaultData getSoapFault(byte[] responseBody, Throwable error) {
        if(error == null) {
            return SoapParser.parseSoapData(FaultData.class, responseBody);
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
                return new FaultData(null, "NO EXISTE SESION", null);
            }

            return new FaultData(null, faultString, null);
        }
        else if(error instanceof ConnectTimeoutException ||
                error instanceof SocketTimeoutException ||
                error.getCause() instanceof ConnectTimeoutException ||
                error.getCause() instanceof SocketTimeoutException){
            return new FaultData(null, "No se pudo establecer una conexi�n con SiVale", null);
        }
        else if(error instanceof SocketException || error.getCause() instanceof SocketException){
            return new FaultData(null, "Error de conexi�n. Revise su conexi�n a internet.", null);
        }
        else {
            String faultString = Util.isStringEmptyOrNull(error.getMessage()) ?
                                    "Error con SiVale":
                                    error.getMessage();
            return new FaultData(null, faultString, null);
        }
    }

    private boolean isError(SiValeData data) {
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

    private void setRequestInProgress(boolean requestInProgress) {
        mRequestInProgress = requestInProgress;
    }
}
