package me.jlhp.sivale.api;

import android.content.Context;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.envelope.BaseEnv;
import me.jlhp.sivale.envelope.BaseEnvelope;
import me.jlhp.sivale.envelope.BaseEnvelopeV2;
import me.jlhp.sivale.envelope.EnvelopeParameter;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.event.FaultEvent;
import me.jlhp.sivale.event.GetBalanceEvent;
import me.jlhp.sivale.event.GetTransactionsEvent;
import me.jlhp.sivale.event.LoginEvent;
import me.jlhp.sivale.model.server.BalanceData;
import me.jlhp.sivale.model.server.SessionData;
import me.jlhp.sivale.model.server.SiValeData;
import me.jlhp.sivale.model.server.TransactionData;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class SiValeAPI {

    private final BaseEnvelope.Builder SoapEnvelopeBuilder = new BaseEnvelope.Builder();
    private final BaseEnvelopeV2.Builder SoapEnvelopeBuilderV2 = new BaseEnvelopeV2.Builder();
    private boolean mSyncMode = false;

    public void login(Context context, String cardNumber, String password, AsyncHttpResponseHandler handler) {
        BaseEnvelope loginEnvelope = SoapEnvelopeBuilder
                .setSoapOperation("login")
                .addParameter(new EnvelopeParameter("huella_aleatoria", "", "xsd:string"))
                .addParameter(new EnvelopeParameter("num_tarjeta", cardNumber, "xsd:string"))
                .addParameter(new EnvelopeParameter("passwd", password, "xsd:string"))
                .addParameter(new EnvelopeParameter("securityLevel", "M", "xsd:string"))
                .create();

        postEnvelope(context, loginEnvelope, handler);
    }

    public void getBalance(Context context, int sessionId, AsyncHttpResponseHandler handler) {
        BaseEnvelope getBalanceEnvelope = SoapEnvelopeBuilder
                .setSoapOperation("getSaldo")
                .addParameter(new EnvelopeParameter("P_ID_SESION", String.valueOf(sessionId), "xsd:decimal"))
                .create();

        postEnvelope(context, getBalanceEnvelope, handler);
    }

    public void getTransactions(Context context, int sessionId, AsyncHttpResponseHandler handler) {
        BaseEnvelope getTransactionsEnvelope = SoapEnvelopeBuilder
                .setSoapOperation("getMovimientos")
                .addParameter(new EnvelopeParameter("P_ID_SESION",
                        String.valueOf(sessionId),
                        "xsd:decimal"))
                .create();

        postEnvelope(context, getTransactionsEnvelope, handler);
    }

    public void getTransactionsV2(Context context, String cardNumber, AsyncHttpResponseHandler handler) {
        BaseEnvelopeV2 getTransactionsEnvelope = SoapEnvelopeBuilderV2
                .setSoapOperation("consultarMovimientosRequest")
                .addParameter(new EnvelopeParameter("identificador", "1", "d:string"))
                .addParameter(new EnvelopeParameter("tarjeta", cardNumber, "d:string"))
                .create();

        postEnvelope(context, getTransactionsEnvelope, handler);
    }

    public void setSyncMode(boolean syncMode) {
        mSyncMode = syncMode;
    }

    private void postEnvelope(Context context, BaseEnv envelope, AsyncHttpResponseHandler responseHandler) {
        SiValeClient.post(context, envelope, responseHandler, mSyncMode);
    }
}
