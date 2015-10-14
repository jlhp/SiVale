package me.jlhp.sivale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import me.jlhp.sivale.api.SiValeAPIHandler;
import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.database.dao.CardRepository;
import me.jlhp.sivale.event.CardClickEvent;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.event.FaultEvent;
import me.jlhp.sivale.event.GetBalanceEvent;
import me.jlhp.sivale.event.GetTransactionsEvent;
import me.jlhp.sivale.event.LoginEvent;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Card.SiValeCardProperty;
import me.jlhp.sivale.model.client.Transaction;
import me.jlhp.sivale.utility.Util;

public
    class
        CardActivity
    extends
        ActionBarActivity
    implements
        View.OnClickListener,
        CardDataDialog.OnCardDataListener,
        CardDeleteDialog.OnCardDeleteListener {

    private SiValeAPIHandler mSiValeAPIHandler = new SiValeAPIHandler();

    private static final String PARAMETER_CARDS = "cards";

    private CardRepository mCardRepository;
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
            setAdapters(true);
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
            //cardDataDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CardDataDialogStyle);
            cardDataDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onCardData(Card card, boolean updating) {
        updateLocalCard(card, updating, false);
    }

    @Override
    public void onCardDelete(Card card) {
        deleteCard(card);
    }

    public void onEvent(@NonNull CardClickEvent event) {
        switch (event.getCardOperation()) {
            case EDIT_CARD :
                openEditCardDialog(event.getCard());
                break;
            case UPDATE_DATA :
                updateCardFromServer(event.getCard());
                break;
            case SHOW_TRANSACTIONS :
                showCardTransactions(event.getCard());
                break;
            case DELETE_CARD :
                openDeleteCardDialog(event.getCard());
                break;
        }
    }

    public void onEvent(@NonNull LoginEvent loginEvent) {
        Util.unregisterStickyEvent(loginEvent);

        Card card = getCard(loginEvent.getCallerId());

        if(updateCardProperty(card,
                !loginEvent.hasNextOperation(),
                SiValeCardProperty.SESSION_ID,
                loginEvent.getSessionData().getSessionId()))
        {
            executeOperation(card, loginEvent.getNextOperations());
        }
    }

    public void onEvent(@NonNull GetBalanceEvent getBalanceEvent) {
        Util.unregisterStickyEvent(getBalanceEvent);

        Card card = getCard(getBalanceEvent.getCallerId());

        if(updateCardProperty(card,
                !getBalanceEvent.hasNextOperation(),
                SiValeCardProperty.BALANCE,
                getBalanceEvent.getBalanceData().getBalance()))
        {
            executeOperation(card, getBalanceEvent.getNextOperations());
        }
    }

    public void onEvent(@NonNull GetTransactionsEvent getTransactionsEvent) {
        Util.unregisterStickyEvent(getTransactionsEvent);

        Card card = getCard(getTransactionsEvent.getCallerId());

        if(updateCardProperty(card,
                !getTransactionsEvent.hasNextOperation(),
                SiValeCardProperty.TRANSACTIONS,
                getTransactionsEvent.getTransactionData().getTransactions()))
        {
            executeOperation(card, getTransactionsEvent.getNextOperations());
        }
    }

    public void onEvent(@NonNull ErrorEvent errorEvent) {
        Util.unregisterStickyEvent(errorEvent);

        if (getString(R.string.invalid_session).equalsIgnoreCase(errorEvent.getError())) {
            loginAndRetry(errorEvent.getCallerId(),
                          Util.addItemsToArray(errorEvent.getCurrentOperation(),
                                               errorEvent.getNextOperations()));
        } else {
            Util.showToast(this, getString(R.string.unknown_error));
            vRecList.getAdapter().notifyDataSetChanged();
        }
    }

    public void onEvent(@NonNull FaultEvent faultEvent) {
        Util.unregisterStickyEvent(faultEvent);

        if (getString(R.string.invalid_session).equalsIgnoreCase(faultEvent.getSoapFault().getFaultString())) {
            loginAndRetry(faultEvent.getCallerId(),
                          Util.addItemsToArray(faultEvent.getCurrentOperation(),
                                               faultEvent.getNextOperations()));
        } else {
            Util.showToast(this, faultEvent.getSoapFault().getFaultString());
            vRecList.getAdapter().notifyDataSetChanged();
        }
    }


    private <T> boolean updateCardProperty(Card card,
                                           boolean setLastUpdateDate,
                                           SiValeCardProperty propertyId,
                                           T propertyValue) {

        if(card == null || !card.cardValueTypeMatch(propertyId, propertyValue)) return false;

        switch(propertyId) {
            case BALANCE :
                 card.setBalance((Double) propertyValue);
                 break;
            case SESSION_ID :
                 card.setSessionId((Integer) propertyValue);
                 break;
            case TRANSACTIONS :
                 card.setTransactions((Collection<Transaction>) propertyValue);
                 break;
        }

        updateLocalCard(card, true, setLastUpdateDate);

        return true;
    }

    private void loginAndRetry(int callerId, SiValeOperation... nextOperations) {
       loginAndRetry(getCard(callerId), nextOperations);
    }

    private void loginAndRetry(Card card, SiValeOperation... nextOperations) {
        if(card == null) return;

        executeOperation(card, Util.addItemsToArray(SiValeOperation.LOGIN, nextOperations));
    }

    @Nullable private Card getCard(int id) {
        initCards(mCards == null || mCards.isEmpty());

        for(Card c : mCards) {
            if(c.getId() == id) return c;
        }

        return null;
    }

    private void openEditCardDialog(Card card) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CardDataDialog.PARAMETER_CARD, card);

        CardDataDialog cardDataDialog = new CardDataDialog();
        cardDataDialog.setArguments(bundle);
        cardDataDialog.show(getSupportFragmentManager(), "");
    }

    private void updateLocalCard(Card card, boolean updating, boolean setLastUpdateDate) {
        if(card == null) return;

        CardAdapter cardAdapter = (CardAdapter) vRecList.getAdapter();

        if(setLastUpdateDate) card.setLastUpdateDate(new Date());

        if(updating) {
            mCardRepository.update(card);
            cardAdapter.update(card);
        }
        else {
            mCardRepository.create(card);
            cardAdapter.add(card);
            executeOperation(card, SiValeOperation.GET_BALANCE, SiValeOperation.GET_TRANSACTIONS);
        }

        mCards = (ArrayList<Card>) cardAdapter.getAll();
    }

    private void updateCardFromServer(Card card) {
        if(card.getSessionId() > 0) {
            executeOperation(card, SiValeOperation.GET_BALANCE, SiValeOperation.GET_TRANSACTIONS);
        }
        else {
            loginAndRetry(card, SiValeOperation.GET_BALANCE, SiValeOperation.GET_TRANSACTIONS);
        }
    }

    private void showCardTransactions(Card card) {
        Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
        intent.putExtra(TransactionActivity.PARAMETER_CARD, card);

        startActivity(intent);
    }

    private void deleteCard(Card card) {
        if(card == null) return;

        CardAdapter cardAdapter = (CardAdapter) vRecList.getAdapter();

        mCardRepository.delete(card);
        cardAdapter.delete(card);

        mCards = (ArrayList<Card>) cardAdapter.getAll();
    }

    private void openDeleteCardDialog(Card card) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CardDataDialog.PARAMETER_CARD, card);

        CardDeleteDialog cardDeleteDialog = new CardDeleteDialog();
        cardDeleteDialog.setArguments(bundle);
        cardDeleteDialog.show(getSupportFragmentManager(), "");
    }

    private void init() {
        mCardRepository = new CardRepository(this);
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
            mCardRepository = mCardRepository == null ? new CardRepository(this) : mCardRepository;
            mCards = (ArrayList<Card>) mCardRepository.getAll();
        }
        else if(mCards == null) {
            mCards = new ArrayList<>();
        }
    }

    private void executeOperation(Card card,
                                  SiValeOperation... operations) {

        if(mSiValeAPIHandler.isRequestInProgress()) {
            Util.showToast(this, "Favor de esperar un momento");
            return;
        }
        else if(card == null || operations == null || operations.length == 0) {
            return;
        }

        SiValeOperation executeOperation = operations[0];
        operations = operations.length > 1 ?
                        Arrays.copyOfRange(operations, 1, operations.length) :
                        null;

        switch (executeOperation) {
            case LOGIN:
                mSiValeAPIHandler.login(this, card.getNumber(), card.getPassword(), card.getId(),
                        operations);
                break;
            case GET_BALANCE:
                mSiValeAPIHandler.getBalance(this, card.getSessionId(), card.getId(),
                        operations);
                break;
            case GET_TRANSACTIONS:
                mSiValeAPIHandler.getTransactions(this, card.getSessionId(), card.getId(),
                        operations);
                break;
        }
    }
}
