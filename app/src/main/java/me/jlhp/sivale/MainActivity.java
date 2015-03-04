package me.jlhp.sivale;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.api.SiValeClientAPI;
import me.jlhp.sivale.event.FaultEvent;
import me.jlhp.sivale.event.GetBalanceEvent;
import me.jlhp.sivale.event.GetTransactionsEvent;
import me.jlhp.sivale.event.LoginEvent;


public class MainActivity extends ActionBarActivity {

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private SiValeClientAPI client = new SiValeClientAPI();

    Button b;
    TextView t1;
    TextView t2;
    EditText e1;
    EditText e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.button);
        t1 = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView2);
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.login(MainActivity.this, e2.getText().toString(), e1.getText().toString());
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onStop() {
        unregisterEventBus();
        super.onStop();
    }

    public void onEvent(GetBalanceEvent getBalanceEvent) {
        t1.setText("$" + getBalanceEvent.getBalanceData().getBalance());
        t2.setText(getBalanceEvent.getBalanceData().getSessionData().getCardNumber());

        showToast(getBalanceEvent.getBalanceData().getBalance() + "");
        unregisterStickyEvent(getBalanceEvent);
    }

    public void onEvent(GetTransactionsEvent getTransactionsEvent) {

    }

    public void onEvent(LoginEvent loginEvent) {
        client.getBalance(this, loginEvent.getSessionData().getSessionId());
        unregisterStickyEvent(loginEvent);
    }

    public void onEvent(FaultEvent faultEvent) {
        switch (faultEvent.getOperation()) {
            case GET_BALANCE:
            case GET_TRANSACTIONS:
            case LOGIN:
                showToast("Error de conexión. Favor de intentar más tarde");
        }

        unregisterStickyEvent(faultEvent);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void registerEventBus() {
        EventBus.getDefault().registerSticky(this);
    }

    private void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    private void unregisterStickyEvent(Object o) {
        EventBus.getDefault().removeStickyEvent(o);
    }
}
