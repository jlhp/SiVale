package me.jlhp.sivale.model.client;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import me.jlhp.sivale.model.server.TransactionData;

/**
 * Created by jjherrer on 06/03/2015.
 */
public class Card {
    private BigDecimal mBalance;
    private String mAlias;
    private Date mLastUpdateDate;
    private String mNumber;
    private String mPassword;
    private List<TransactionData.Transaction> mTransactions;

    public BigDecimal getBalance() {
        return mBalance;
    }

    public void setBalance(BigDecimal mBalance) {
        this.mBalance = mBalance;
    }

    public String getAlias() {
        return mAlias;
    }

    public void setAlias(String mAlias) {
        this.mAlias = mAlias;
    }

    public Date getLastUpdateDate() {
        return mLastUpdateDate;
    }

    public void setLastUpdateDate(Date mLastUpdateDate) {
        this.mLastUpdateDate = mLastUpdateDate;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public List<TransactionData.Transaction> getTransactions() {
        return mTransactions;
    }

    public void setTransactions(List<TransactionData.Transaction> mTransactions) {
        this.mTransactions = mTransactions;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
