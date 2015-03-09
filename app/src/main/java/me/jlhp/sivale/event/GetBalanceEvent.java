package me.jlhp.sivale.event;

import me.jlhp.sivale.model.server.BalanceData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class GetBalanceEvent {
    private BalanceData BalanceData;

    public GetBalanceEvent(BalanceData balanceData) {
        BalanceData = balanceData;
    }

    public BalanceData getBalanceData() {
        return BalanceData;
    }

    public void setBalanceData(BalanceData transactionData) {
        BalanceData = transactionData;
    }
}
