package me.jlhp.sivale;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static final String NAMESPACE = "http://com.sivale.bancamovil.proto/IWebMethods.xsd";
    public static final String BASE_URL = "http://148.223.134.18:8888/bancamovil/WebMethods";
    public static final int DEFAULT_TIMEOUT = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            ArrayList<PropertyInfo> params = new ArrayList<PropertyInfo>();

            String card = "5349260339603952";
            String password = "hepl9106";
            String securityLevel = "M";

            params.add(CreatePropertyInfo("num_tarjeta", card, String.class));
            params.add(CreatePropertyInfo("passwd", password, String.class));
            params.add(CreatePropertyInfo("securityLevel", securityLevel, String.class));

            CallWebService("login", params);

            AsyncHttpClient client = new AsyncHttpClient();
            try {
                StringEntity s = new StringEntity("");

                client.post(this, "http://www.google.com", new StringEntity(""), "text/xml", new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public SoapObject CallWebService(String soapMethod, ArrayList<PropertyInfo> params){
        SoapObject resultsRequestSOAP = new SoapObject();
        SoapObject request = new SoapObject(NAMESPACE, soapMethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        HttpTransportSE aht = new HttpTransportSE(BASE_URL, DEFAULT_TIMEOUT);

        try {
            for (PropertyInfo param : params) {
                request.addProperty(param);
            }

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            aht.call(NAMESPACE, envelope);

            resultsRequestSOAP = (SoapObject) envelope.bodyIn;

        } catch (ConnectException ex) {
            SoapObject error = new SoapObject();
            error.addProperty("errorMessage", "error:Error de conectividad. Revise su conexión a Internet.");
            resultsRequestSOAP.addSoapObject(error);
        } catch (Exception ex) {
            SoapObject error = new SoapObject();
            error.addProperty("errorMessage", "error:Error desconocido. Favor de intentar más tarde.");
            resultsRequestSOAP.addSoapObject(error);
        }

        return resultsRequestSOAP;
    }

    public PropertyInfo CreatePropertyInfo(String name, Object value, Object type){
        PropertyInfo propertyInfo = new PropertyInfo();

        propertyInfo.setName(name);
        propertyInfo.setValue(value);
        propertyInfo.setType(type);

        return propertyInfo;
    }

}
