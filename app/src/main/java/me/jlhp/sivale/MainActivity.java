package me.jlhp.sivale;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.alexgilleran.icesoap.exception.SOAPException;
import com.alexgilleran.icesoap.exception.XMLParsingException;
import com.alexgilleran.icesoap.observer.SOAP11Observer;
import com.alexgilleran.icesoap.parser.impl.IceSoapParserImpl;
import com.alexgilleran.icesoap.request.Request;
import com.alexgilleran.icesoap.request.RequestFactory;
import com.alexgilleran.icesoap.request.SOAP11Request;
import com.alexgilleran.icesoap.request.impl.RequestFactoryImpl;
import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity {

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private RequestFactory requestFactory = new RequestFactoryImpl();
    private SOAP11Observer<LoginResponse> AnonymousObserver = new SOAP11Observer<LoginResponse>() {
        @Override
        public void onCompletion(Request<LoginResponse, SOAP11Fault> request) {
            System.out.print("");
        }

        @Override
        public void onException(Request<LoginResponse, SOAP11Fault> request, SOAPException e) {
            System.out.print("");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            LoginEnvelope loginEnvelope = null;

            SOAP11Request<LoginResponse> definitionRequest = requestFactory.buildRequest(
                    "http://148.223.134.18:8888/bancamovil/WebMethods",
                    loginEnvelope,
                    null,
                    LoginResponse.class);

            StringEntity entity = null;

            try {
                entity = new StringEntity(loginEnvelope.toString());
                entity.setContentType("text/xml");
                entity.setContentEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            asyncHttpClient.post(this, "http://148.223.134.18:8888/bancamovil/WebMethods", entity, "text/xml", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    IceSoapParserImpl<LoginResponse> parser = new IceSoapParserImpl<LoginResponse>(LoginResponse.class);
                    ByteArrayInputStream stream = new ByteArrayInputStream(responseBody);
                    LoginResponse loginResponse = null;

                    try {
                        loginResponse = parser.parse(stream);
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

            definitionRequest.registerObserver(AnonymousObserver);
            definitionRequest.execute();
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
}
