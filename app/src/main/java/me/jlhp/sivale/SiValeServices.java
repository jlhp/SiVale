package me.jlhp.sivale;

/**
 * Created by JOSELUIS on 10/9/2016.
 */

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import me.jlhp.sivale.Constants;
import me.jlhp.sivale.Uses;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;

public class SiValeServices
{
    private String OPERATION_NAME = "";
    private String SOAP_ACTION = "";
    private final String SOAP_ADDRESS = "https://wls-wsprod.sivale.mx/sivale/Integracion/Servicios/AppSiVale";
    private final String WSDL_TARGET_NAMESPACE = "http://mx.com.sivale.ws/exposition/servicios/appsivale";
    private Activity actualActivity;
    private String actualServiceText;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE httpTransport;

    private boolean isNetworkConnected()
    {
        return true;
    }

    public String[] consultarSaldoRequest(String identificador, String tarjeta, String alias) {
        this.actualServiceText = "Cargando ...";
        this.OPERATION_NAME = "consultarSaldoRequest";
        this.SOAP_ACTION = "http://mx.com.sivale.ws/exposition/servicios/appsivale/" + this.OPERATION_NAME;
        if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        if (isNetworkConnected()) {
            String siValeKey = Uses.getKeyServicesCards();
            SoapObject request = new SoapObject(Constants.NameSpace, this.OPERATION_NAME);
            Element[] header = new Element[]{new Element().createElement(Constants.NameSpace, "identificacion")};
            Element usuario = new Element().createElement(Constants.NameSpace, "usuario");
            usuario.addChild(4, Constants.usuarioSV);
            header[0].addChild(2, usuario);
            Element password = new Element().createElement(Constants.NameSpace, "password");
            password.addChild(4, siValeKey);
            header[0].addChild(2, password);
            Element solicitante = new Element().createElement(Constants.NameSpace, "solicitante");
            solicitante.addChild(4, Constants.solicitanteSV);
            header[0].addChild(2, solicitante);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("identificador");
            pi.setValue(identificador);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("tarjeta");
            pi.setValue(tarjeta);
            request.addProperty(pi);
            if (alias != null) {
                if (!alias.equals(XmlPullParser.NO_NAMESPACE)) {
                    pi = new PropertyInfo();
                    pi.setName("alias");
                    pi.setValue(alias);
                    request.addProperty(pi);
                }
            }
            this.envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            this.envelope.headerOut = header;
            this.envelope.setOutputSoapObject(request);
            this.httpTransport = new HttpTransportSE("https://wls-wsprod.sivale.mx/sivale/Integracion/Servicios/AppSiVale");
            this.httpTransport.debug = true;
            this.httpTransport.setXmlVersionTag("<?xml version=\"1.1\" encoding=\"utf-8\"?>");
            try {
                String[] messageResult = new String[2];
                this.httpTransport.call(this.SOAP_ACTION, this.envelope);
                Vector<SoapObject> result = (Vector) this.envelope.getResponse();
                SoapObject so = (SoapObject) result.get(0);
                if ((so.getProperty(0) + XmlPullParser.NO_NAMESPACE).equals("0")) {
                    messageResult[0] = "Consulta exitosa";
                    messageResult[1] = result.get(2) + XmlPullParser.NO_NAMESPACE;
                    messageResult[1] = format(result.get(2) + XmlPullParser.NO_NAMESPACE);
                    return messageResult;
                }
                messageResult[0] = so.getProperty(1) + XmlPullParser.NO_NAMESPACE;
                return messageResult;
            } catch (Exception e) {
                e.printStackTrace();
                return new String[]{"ERROR"};
            }
        }
        return new String[]{"Por favor revisa tu conexi\u00f3n a internet"};
    }

    public String format(String number) {
        double amount = Double.parseDouble(number);
        String amountFormt = new DecimalFormat("#,###.00").format(amount) + XmlPullParser.NO_NAMESPACE;
        if (amountFormt.length() > 1 && String.valueOf(amountFormt.charAt(0)).compareTo(".") == 0) {
            return "0" + amountFormt;
        }
        if (amountFormt.length() > 1 && String.valueOf(amountFormt.charAt(0)).compareTo("-") == 0 && String.valueOf(amountFormt.charAt(1)).compareTo(".") == 0) {
            return "-0" + amountFormt.substring(1);
        }
        return amountFormt;
    }
}
