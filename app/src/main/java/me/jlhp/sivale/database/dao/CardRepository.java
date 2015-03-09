package me.jlhp.sivale.database.dao;

import android.content.Context;

import me.jlhp.sivale.model.client.Card;

/**
 * Created by JOSELUIS on 3/8/2015.
 */
public class CardRepository extends BaseRepository<Card> {
    public CardRepository(Context ctx){
        super(ctx, Card.class);
    }

    public Card get(String cardNumber){
        Card c = new Card();
        c.setNumber(cardNumber);

        return getDao().queryForSameId(c);
    }
}
