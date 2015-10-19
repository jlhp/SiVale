package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.model.client.Card;

/**
 * Created by JOSELUIS on 10/18/2015.
 */
public class CardOperationEvent implements NextOperations {
    private SiValeOperation[] NextOperations;
    private Card mCard;
    private CardOperation mCardOperation;

    public CardOperationEvent(Card card, CardOperation cardOperation) {
        setCard(card);
        setCardOperation(cardOperation);
    }

    public CardOperationEvent(Card card, CardOperation cardOperation, SiValeOperation... nextOperations) {
        setCard(card);
        setCardOperation(cardOperation);
        setNextOperations(nextOperations);
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card mCard) {
        this.mCard = mCard;
    }

    public CardOperation getCardOperation() {
        return mCardOperation;
    }

    public void setCardOperation(CardOperation mCardOperation) {
        this.mCardOperation = mCardOperation;
    }

    @Override
    public SiValeOperation[] getNextOperations() {
        return NextOperations;
    }

    @Override
    public void setNextOperations(SiValeOperation... nextOperations) {
        NextOperations = nextOperations;
    }

    @Override
    public boolean hasNextOperation(){
        return NextOperations != null && NextOperations.length > 0;
    }
}
