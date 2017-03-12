package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by JOSELUIS on 2/5/2017.
 */

@XMLObject("transaccion")
public class Transaction {

    @XMLField("authorizationNumber")
    private String mAuthorizationNumber;

    @XMLField("numberCard")
    private String mCardNumber;

    @XMLField("transactionDate")
    private String mTransactionDate;

    @XMLField("amount")
    private String mAmount;

    @XMLField("acceptorName")
    private String mAcceptorName;

    @XMLField("messageType")
    private String mMessageType;

    @XMLField("responseCode")
    private String mResponseCode;

    @XMLField("transactionType")
    private String mTransactionType;

    @XMLField("transactionOrder")
    private String mTransactionOrder;

    public Transaction() {

    }
}
