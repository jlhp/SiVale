package me.jlhp.sivale;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by jjherrer on 02/03/2015.
 */
@XMLObject("//return")
public class BalanceData {
    @XMLField("saldo")
    private double Balance;

    @XMLField("//objEstatus/item")
    private SessionData SessionData;

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public SessionData getSessionData() {
        return SessionData;
    }

    public void setSessionData(SessionData sessionData) {
        SessionData = sessionData;
    }
}
