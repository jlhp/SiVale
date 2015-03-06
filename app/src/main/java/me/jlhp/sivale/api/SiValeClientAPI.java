package me.jlhp.sivale.api;

import android.content.Context;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.envelope.BaseEnvelope;
import me.jlhp.sivale.envelope.EnvelopeParameter;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.event.FaultEvent;
import me.jlhp.sivale.event.GetBalanceEvent;
import me.jlhp.sivale.event.GetTransactionsEvent;
import me.jlhp.sivale.event.LoginEvent;
import me.jlhp.sivale.model.BalanceData;
import me.jlhp.sivale.model.SessionData;
import me.jlhp.sivale.model.SiValeData;
import me.jlhp.sivale.model.TransactionData;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class SiValeClientAPI {

    private final BaseEnvelope.Builder SoapEnvelopeBuilder = new BaseEnvelope.Builder();
    private final SiValeParser SoapParser = new SiValeParser();

    public void login(Context context, String cardNumber, String password) {
        login(context, cardNumber, password, null);
    }

    public void login(Context context, String cardNumber, String password, final SiValeOperation retryOperation) {
        BaseEnvelope loginEnvelope = SoapEnvelopeBuilder
                .setSoapOperation("login")
                .addParameter(new EnvelopeParameter("huella_aleatoria", "", "xsd:string"))
                .addParameter(new EnvelopeParameter("num_tarjeta", cardNumber, "xsd:string"))
                .addParameter(new EnvelopeParameter("passwd", password, "xsd:string"))
                .addParameter(new EnvelopeParameter("securityLevel", "M", "xsd:string"))
                .build();

        postEnvelope(context, loginEnvelope, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                SessionData sessionData = getSoapResponse(SessionData.class, responseBody);
                if (isError(sessionData))
                    postEvent(new ErrorEvent(sessionData.getError(), SiValeOperation.LOGIN));
                else if (retryOperation == null) postEvent(new LoginEvent(sessionData));
                else postEvent(new LoginEvent(sessionData, retryOperation));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SOAP11Fault soap11Fault = getSoapFault(responseBody, error);
                postEvent(new FaultEvent(soap11Fault, SiValeOperation.LOGIN));
            }
        });
    }

    public void getBalance(Context context, int sessionId) {
        BaseEnvelope getBalanceEnvelope = SoapEnvelopeBuilder
                .setSoapOperation("getSaldo")
                .addParameter(new EnvelopeParameter("P_ID_SESION", String.valueOf(sessionId), "xsd:decimal"))
                .build();

        postEnvelope(context, getBalanceEnvelope, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                BalanceData balanceData = getSoapResponse(BalanceData.class, responseBody);
                if (!isError(balanceData)) postEvent(new GetBalanceEvent(balanceData));
                else postEvent(new ErrorEvent(balanceData.getError(), SiValeOperation.GET_BALANCE));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SOAP11Fault soap11Fault = getSoapFault(responseBody, error);
                postEvent(new FaultEvent(soap11Fault, SiValeOperation.GET_BALANCE));
            }
        });
    }

    public void getTransactions(Context context, int sessionId) {
        BaseEnvelope getTransactionsEnvelope = SoapEnvelopeBuilder
                .setSoapOperation("getMovimientos")
                .addParameter(new EnvelopeParameter("P_ID_SESION", String.valueOf(sessionId), "xsd:decimal"))
                .build();

        postEnvelope(context, getTransactionsEnvelope, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                TransactionData transactionData = getSoapResponse(TransactionData.class, responseBody);
                if (!isError(transactionData)) postEvent(new GetTransactionsEvent(transactionData));
                else
                    postEvent(new ErrorEvent(transactionData.getError(), SiValeOperation.GET_TRANSACTIONS));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SOAP11Fault soap11Fault = getSoapFault(responseBody, error);
                postEvent(new FaultEvent(soap11Fault, SiValeOperation.GET_TRANSACTIONS));
            }
        });
    }

    private void postEnvelope(Context context, BaseEnvelope envelope, AsyncHttpResponseHandler responseHandler) {
        SiValeClient.post(context, envelope, responseHandler);
    }

    private void postEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    private <T> T getSoapResponse(Class<T> clazz, byte[] soapData) {
        return SoapParser.parseSoapData(clazz, soapData);
    }

    private SOAP11Fault getSoapFault(byte[] responseBody, Throwable error) {
        return responseBody != null && error == null ?
                SoapParser.parseSoapData(SOAP11Fault.class, responseBody) :
                new SOAP11Fault(null, error.getMessage(), null);
    }

    private boolean isError(SiValeData data) {
        if (data == null) {
            throw new IllegalArgumentException("'data' can't ne null");
        }

        return data.isError();
    }
}
