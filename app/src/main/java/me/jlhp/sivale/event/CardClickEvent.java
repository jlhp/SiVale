package me.jlhp.sivale.event;

import android.view.View;

import me.jlhp.sivale.model.client.Card;

/**
 * Created by JOSELUIS on 3/23/2015.
 */
public
    class
        CardClickEvent {

    private CardOperation mCardOperation;
    private Card mCard;

    public CardClickEvent(CardOperation cardOperation, Card card) {
        setCardOperation(cardOperation);
        setCard(card);
    }

    public CardOperation getCardOperation() {
        return mCardOperation;
    }

    public void setCardOperation(CardOperation vView) {
        this.mCardOperation = vView;
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card mCard) {
        this.mCard = mCard;
    }

    public enum CardOperation {
        UPDATE_DATA, SHOW_TRANSACTIONS, EDIT_CARD, DELETE_CARD
    }
}
