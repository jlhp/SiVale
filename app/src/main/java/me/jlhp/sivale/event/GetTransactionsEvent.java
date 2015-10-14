package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.model.server.TransactionData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class GetTransactionsEvent implements CallerIdRequired, NextOperations {
    private TransactionData TransactionData;
    private int CallerId;
    private SiValeOperation[] NextOperations;

    public GetTransactionsEvent(TransactionData transactionData) {
        this(transactionData, 0);
    }

    public GetTransactionsEvent(TransactionData transactionData, int callerId) {
        this(transactionData, callerId, (SiValeOperation) null);
    }

    public GetTransactionsEvent(TransactionData transactionData, int callerId, SiValeOperation... nextOperations) {
        TransactionData = transactionData;
        CallerId = callerId;
        NextOperations = nextOperations;
    }

    public TransactionData getTransactionData() {
        return TransactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        TransactionData = transactionData;
    }

    @Override
    public int getCallerId() {
        return CallerId;
    }

    @Override
    public void setCallerId(int callerId) {
        CallerId = callerId;
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
