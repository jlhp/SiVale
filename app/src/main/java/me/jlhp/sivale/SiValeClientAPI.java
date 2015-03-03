package me.jlhp.sivale;

import android.content.Context;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import me.jlhp.sivale.envelope.BaseEnvelope;
import me.jlhp.sivale.envelope.EnvelopeParameter;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class SiValeClientAPI {

    private final BaseEnvelope.Builder SoapEnvelopeBuilder = new BaseEnvelope.Builder();
    private final SiValeParser SoapParser = new SiValeParser();

    public void login(Context context, String cardNumber, String password) {
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
                SessionData sessionData = SoapParser.parseSoapData(SessionData.class, responseBody);

                System.out.println();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SOAP11Fault soap11Fault = SoapParser.parseSoapData(SOAP11Fault.class, responseBody);

                System.out.println();
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
                BalanceData balanceData = SoapParser.parseSoapData(BalanceData.class, responseBody);

                System.out.println();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SOAP11Fault soap11Fault = SoapParser.parseSoapData(SOAP11Fault.class, responseBody);

                System.out.println();
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
                TransactionData transactionData = SoapParser.parseSoapData(TransactionData.class, responseBody);

                System.out.println();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SOAP11Fault soap11Fault = SoapParser.parseSoapData(SOAP11Fault.class, responseBody);

                System.out.println();
            }
        });
    }

    private void postEnvelope(Context context, BaseEnvelope envelope, AsyncHttpResponseHandler responseHandler) {
        SiValeClient.post(context, soapEnvelope2StringEntity(envelope), responseHandler);
    }

    private StringEntity soapEnvelope2StringEntity(BaseEnvelope envelope) {
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
