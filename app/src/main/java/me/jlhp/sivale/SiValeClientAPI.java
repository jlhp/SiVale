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

    public void login(Context context, String cardNumber, String password, AsyncHttpResponseHandler test) {

        BaseEnvelope loginEnvelope = SoapEnvelopeBuilder
            .setSoapOperation("login")
            .addParameter(new EnvelopeParameter("huella_aleatoria", "", "xsd:string"))
            .addParameter(new EnvelopeParameter("num_tarjeta", cardNumber, "xsd:string"))
            .addParameter(new EnvelopeParameter("passwd", password, "xsd:string"))
            .addParameter(new EnvelopeParameter("securityLevel", "M", "xsd:string"))
            .build();


        StringEntity entity = null;

        try {
            entity = new StringEntity(loginEnvelope.toString());
            entity.setContentType("text/xml");
            entity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SiValeClient.post(context, entity, test);
    }
}
