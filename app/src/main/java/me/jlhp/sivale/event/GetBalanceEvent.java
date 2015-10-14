package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.model.server.BalanceData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class GetBalanceEvent implements CallerIdRequired, NextOperations {
    private BalanceData BalanceData;
    private int CallerId;
    private SiValeOperation[] NextOperations;

    public GetBalanceEvent(BalanceData balanceData) {
        this(balanceData, 0);
    }

    public GetBalanceEvent(BalanceData balanceData, int callerId) {
        this(balanceData, callerId, (SiValeOperation) null);
    }

    public GetBalanceEvent(BalanceData balanceData, int callerId, SiValeOperation... nextOperations) {
        BalanceData = balanceData;
        CallerId = callerId;
        NextOperations = nextOperations;
    }

    public BalanceData getBalanceData() {
        return BalanceData;
    }

    public void setBalanceData(BalanceData transactionData) {
        BalanceData = transactionData;
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
