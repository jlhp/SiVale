package me.jlhp.sivale.database;

import android.content.Context;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.database.dao.CardRepository;
import me.jlhp.sivale.event.CardOperation;
import me.jlhp.sivale.event.CardOperationEvent;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Transaction;

/**
 * Created by JOSELUIS on 10/18/2015.
 */
public class SiValeDataHandler {

    private CardRepository mCardRepository;

    public <T> Card updateCardProperty(Context context,
                                          int cardId,
                                          Card.SiValeCardProperty propertyId,
                                          T propertyValue) {

        Card card = getCard(context, cardId);

        if(card == null || !card.cardValueTypeMatch(propertyId, propertyValue)) return null;

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

        card.setLastUpdateDate(new Date());

        updateCard(context, card);

        return card;
    }

    private void updateCard(Context context, Card card) {
        init(context, false);

        if(card == null) return;

        mCardRepository.update(card);
    }

    public Card getCard(Context context, int cardId) {
        init(context, false);

        return mCardRepository.get(cardId);
    }

    public List<Card> getAllCards(Context context, boolean refresh) {
        init(context, refresh);

        return mCardRepository.getAll();
    }

    private void init(Context context, boolean refresh) {
        if(context == null) throw new IllegalArgumentException("'context' should not be null");

        mCardRepository = refresh || mCardRepository == null ? new CardRepository(context) : mCardRepository;
    }
}
