package me.jlhp.sivale;

import android.content.Context;

import com.alexgilleran.icesoap.exception.XMLParsingException;
import com.alexgilleran.icesoap.parser.impl.IceSoapParserImpl;
import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import me.jlhp.sivale.envelope.BaseEnvelope;
import me.jlhp.sivale.envelope.EnvelopeParameter;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class SiValeClientAPI {

    private final BaseEnvelope.Builder SoapEnvelopeBuilder = new BaseEnvelope.Builder();

    public void login(Context context, String cardNumber, String password) {

        BaseEnvelope loginEnvelope = SoapEnvelopeBuilder
            .setSoapOperation("login")
            .addParameter(new EnvelopeParameter("huella_aleatoria", "", "xsd:string"))
            .addParameter(new EnvelopeParameter("num_tarjeta", cardNumber, "xsd:string"))
            .addParameter(new EnvelopeParameter("passwd", password, "xsd:string"))
            .addParameter(new EnvelopeParameter("securityLevel", "M", "xsd:string"))
            .build();

        //LoginEnvelope loginEnvelope = new LoginEnvelope("", cardNumber, password, "M");

        StringEntity entity = null;

        try {
            entity = new StringEntity(loginEnvelope.toString());
            entity.setContentType("text/xml");
            entity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SiValeClient.post(context, entity, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                IceSoapParserImpl<SessionData> parser = new IceSoapParserImpl<SessionData>(SessionData.class);
                ByteArrayInputStream stream = new ByteArrayInputStream(responseBody);
                SessionData sessionData = null;

                try {
                    sessionData = parser.parse(stream);
                } catch (XMLParsingException e) {
                    e.printStackTrace();
                }

                System.out.println();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                IceSoapParserImpl<SOAP11Fault> parser = new IceSoapParserImpl<SOAP11Fault>(SOAP11Fault.class);
                ByteArrayInputStream stream = new ByteArrayInputStream(responseBody);
                SOAP11Fault soap11Fault = null;

                try {
                    soap11Fault = parser.parse(stream);
                } catch (XMLParsingException e) {
                    e.printStackTrace();
                }

                System.out.println();
            }
        });
    }
}
