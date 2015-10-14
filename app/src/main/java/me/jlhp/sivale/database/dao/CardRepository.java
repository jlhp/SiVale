package me.jlhp.sivale.database.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Transaction;

/**
 * Created by JOSELUIS on 3/8/2015.
 */
public class CardRepository extends BaseRepository<Card> {
    public CardRepository(Context ctx){
        super(ctx, Card.class);
    }

    @Override
    public int create(Card card) {
        if(card == null) return 0;

        card.setLastUpdateDate(new Date());

        int rows = super.create(card);

        rows += new TransactionRepository(context).create(card.getTransactions(), card.getId());

        return rows;
    }

    @Override
    public int update(Card card) {
        if(card == null) return 0;

        int rows = super.update(card);

        rows += new TransactionRepository(context).update(card.getTransactions(), card.getId());

        return rows;
    }

    public Card get(String cardNumber){
        Card c = new Card();
        c.setNumber(cardNumber);

        return getDao().queryForSameId(c);
    }
}
