package me.jlhp.sivale;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import me.jlhp.sivale.model.client.Card;


public class CardActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        RecyclerView recList = (RecyclerView) findViewById(R.id.list);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        final Card c1 = new Card();
        c1.setAlias("SiVale: Compras");
        c1.setBalance(new BigDecimal("3232.21"));
        c1.setLastUpdateDate(new Date());
        c1.setNumber("5203652185635941");

        final Card c2 = new Card();
        c2.setAlias("SiVale: Restaurante");
        c2.setBalance(new BigDecimal("592.33"));
        c2.setLastUpdateDate(new Date());
        c2.setNumber("5203652185638563");

        final Card c3 = new Card();
        c3.setAlias("SiVale: Stuff");
        c3.setBalance(new BigDecimal("8.21"));
        c3.setLastUpdateDate(new Date());
        c3.setNumber("5203652185638956");

        final Card c4 = new Card();
        c4.setAlias("SiVale: Super Troll");
        c4.setBalance(new BigDecimal("109563.99"));
        c4.setLastUpdateDate(new Date());
        c4.setNumber("5203652185635231");

        CardAdapter cardAdapter = new CardAdapter(new ArrayList<Card>() {{
            add(c1);
            add(c2);
            add(c3);
            add(c4);
        }}, R.layout.card_sivale, this);

        recList.setLayoutManager(llm);
        recList.setAdapter(cardAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
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
