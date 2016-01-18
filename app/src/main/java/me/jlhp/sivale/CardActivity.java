package me.jlhp.sivale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import me.jlhp.sivale.api.SiValeAPIHandler;
import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.database.SiValeDataHandler;
import me.jlhp.sivale.event.CardClickEvent;
import me.jlhp.sivale.event.CardOperation;
import me.jlhp.sivale.event.CardOperationEvent;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.event.SyncFrequencyChangeEvent;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.updater.SiValeAlarm;
import me.jlhp.sivale.utility.Util;

public
    class
        CardActivity
    extends
        ActionBarActivity
    implements
        View.OnClickListener,
        CardDataDialog.OnCardDataListener,
        CardDeleteDialog.OnCardDeleteListener,
        CardAdapter.OnCardClickListener {

    private SiValeAPIHandler mSiValeAPIHandler = new SiValeAPIHandler();

    private static final String PARAMETER_CARDS = "cards";

    private SiValeDataHandler mSiValeDataHandler;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Card> mCards;

    private RecyclerView vRecList;
    private FloatingActionButton vFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        init();
        setViews();

        if(savedInstanceState == null) {
            scheduleAutoUpdater();
            setAdapters(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu myMenu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, myMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList(PARAMETER_CARDS, mCards);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        mCards = savedInstanceState.getParcelableArrayList(PARAMETER_CARDS);
        setAdapters(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Util.registerEventBus(this);
    }

    @Override
    public void onStop() {
        Util.unregisterEventBus(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab) {
            CardDataDialog cardDataDialog = new CardDataDialog();
            cardDataDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onCardData(Card card, CardOperation cardOperation) {
        CardOperationEvent cardOperationEvent = new CardOperationEvent(card, cardOperation);
        onEventMainThread(cardOperationEvent);

        if(cardOperation == CardOperation.ADD_DATA) {
            addCard(card);
            updateCard(card);
        }
    }

    @Override
    public void onCardDelete(Card card) {
        CardOperationEvent cardOperationEvent = new CardOperationEvent(card, CardOperation.DELETE_CARD);
        onEventMainThread(cardOperationEvent);
        deleteCard(card);
    }

    @Override
    public void onCardClick(Card card, CardOperation cardOperation) {
        switch (cardOperation) {
            case EDIT_CARD :
                openEditCardDialog(card);
                break;
            case UPDATE_DATA :
                updateCard(card);
                break;
            case SHOW_TRANSACTIONS :
                showCardTransactions(card);
                break;
            case DELETE_CARD :
                openDeleteCardDialog(card);
                break;
        }
    }

    public void onEventMainThread(@NonNull CardOperationEvent cardOperationEvent) {
        Util.unregisterStickyEvent(cardOperationEvent);

        Card card = cardOperationEvent.getCard();
        CardOperation cardOperation = cardOperationEvent.getCardOperation();

        if(card == null || cardOperation == null) return;

        CardAdapter cardAdapter = (CardAdapter) vRecList.getAdapter();

        switch(cardOperation) {
            case ADD_DATA:
                cardAdapter.add(card);
                break;
            case UPDATE_DATA:
                cardAdapter.update(card);
                break;
            case DELETE_CARD:
                cardAdapter.delete(card);
        }

        mCards = (ArrayList<Card>) cardAdapter.getAll();
    }

    public void onEventMainThread(@NonNull ErrorEvent errorEvent) {
        Util.unregisterStickyEvent(errorEvent);
        Util.showToast(this,
                       errorEvent.showToUser() ? errorEvent.getError() :
                                                 getString(R.string.unknown_error));

        vRecList.getAdapter().notifyDataSetChanged();
    }

    private void openEditCardDialog(Card card) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CardDataDialog.PARAMETER_CARD, card);

        CardDataDialog cardDataDialog = new CardDataDialog();
        cardDataDialog.setArguments(bundle);
        cardDataDialog.show(getSupportFragmentManager(), "");
    }

    private void addCard(Card card) {
        mSiValeDataHandler.createCard(this, card);
    }

    private void updateCard(Card card) {
        mSiValeAPIHandler.updateCard(this, card);
    }

    private void deleteCard(Card card) {
        mSiValeDataHandler.deleteCard(this, card);
    }

    private void showCardTransactions(Card card) {
        Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
        intent.putExtra(TransactionActivity.PARAMETER_CARD, card);

        startActivity(intent);
    }

    private void openDeleteCardDialog(Card card) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CardDataDialog.PARAMETER_CARD, card);

        CardDeleteDialog cardDeleteDialog = new CardDeleteDialog();
        cardDeleteDialog.setArguments(bundle);
        cardDeleteDialog.show(getSupportFragmentManager(), "");
    }

    private void init() {
        mSiValeDataHandler = new SiValeDataHandler();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void setViews() {
        vRecList = (RecyclerView) findViewById(R.id.list);
        vRecList.setHasFixedSize(true);
        vRecList.setLayoutManager(mLinearLayoutManager);

        vFab = (FloatingActionButton) findViewById(R.id.fab);
        vFab.setOnClickListener(this);
        vFab.attachToRecyclerView(vRecList);
    }

    private void setAdapters(boolean refresh) {
        initCards(refresh);
        vRecList.setAdapter(new CardAdapter(mCards, R.layout.card_sivale, this));
    }

    private void initCards(boolean refresh) {
        if(refresh) {
            mCards = (ArrayList<Card>) mSiValeDataHandler.getAllCards(this, refresh);
        }
        else if(mCards == null) {
            mCards = new ArrayList<>();
        }
    }

    private void scheduleAutoUpdater(){
        int syncFrequency = PreferenceManager
                            .getDefaultSharedPreferences(this)
                            .getInt(getString(R.string.pref_key_sync_frequency), 1);

        Util.setSiValeSynchronizationAlarm(this, syncFrequency);
    }
}
