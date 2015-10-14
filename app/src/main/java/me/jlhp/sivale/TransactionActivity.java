package me.jlhp.sivale;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.jlhp.sivale.database.dao.TransactionRepository;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Transaction;

public class TransactionActivity extends ActionBarActivity {
    public static final String PARAMETER_CARD = "card";
    private static final String PARAMETER_TRANSACTIONS = "transactions";
    
    private TransactionRepository mTransactionRepository;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView vRecList;
    private Card mCard;
    private ArrayList<Transaction> mTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setViews();

        if(savedInstanceState == null) {
            setAdapters();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList(PARAMETER_TRANSACTIONS, mTransactions);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        mTransactions = savedInstanceState.getParcelableArrayList(PARAMETER_TRANSACTIONS);
        setAdapters();
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
        mCard = getIntent().getParcelableExtra(PARAMETER_CARD);

        mTransactionRepository = new TransactionRepository(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void setViews() {
        vRecList = (RecyclerView) findViewById(R.id.list);
        vRecList.setHasFixedSize(true);
        vRecList.setLayoutManager(mLinearLayoutManager);
    }

    private void setAdapters() {
        mTransactions = new ArrayList<>(mTransactionRepository.getAllTransactionsFromCard(mCard));

        vRecList.setAdapter(new TransactionAdapter(mTransactions,
                                                   R.layout.list_item_sivale_transaction,
                                                   this));
    }
}
