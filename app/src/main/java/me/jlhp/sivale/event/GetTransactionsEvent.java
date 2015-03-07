package me.jlhp.sivale.event;

import me.jlhp.sivale.model.TransactionData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class GetTransactionsEvent {
    private TransactionData TransactionData;

    public GetTransactionsEvent(TransactionData transactionData) {
        TransactionData = transactionData;
    }

    public TransactionData getTransactionData() {
        return TransactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        TransactionData = transactionData;
    }
}
