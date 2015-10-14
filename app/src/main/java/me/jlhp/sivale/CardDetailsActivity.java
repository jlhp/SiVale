package me.jlhp.sivale;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import me.jlhp.sivale.model.client.Card;

public class CardDetailsActivity extends ActionBarActivity {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView vRecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setViews();

        if(savedInstanceState == null) {
            setAdapters();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void setViews() {
        vRecList = (RecyclerView) findViewById(R.id.list);
        vRecList.setHasFixedSize(true);
        vRecList.setLayoutManager(mLinearLayoutManager);
    }

    private void setAdapters() {
        //vRecList.setAdapter(new CardAdapter(mCards, R.layout.card_sivale, this));
    }
}
