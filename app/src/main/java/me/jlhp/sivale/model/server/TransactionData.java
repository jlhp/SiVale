package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.jlhp.sivale.model.client.Transaction;
import me.jlhp.sivale.utility.Util;

/**
 * Created by jjherrer on 03/03/2015.
 */
@XMLObject("//return")
public class TransactionData extends SoapData {

    @XMLField(value = "//objMovimientos/item")
    private List<String> StringTransactions;

    private List<Transaction> Transactions;

    public void setStringTransactions(List<String> stringTransactions) {
        StringTransactions = stringTransactions;
        Transactions = null;
    }

    public List<Transaction> getTransactions() {
        if (StringTransactions == null || StringTransactions.isEmpty()) {
            throw new IllegalStateException("No StringTransactions available");
        }

        if (Transactions == null) {
            Transactions = new ArrayList<Transaction>();

            for (String s : StringTransactions) {
                String[] transactionDetails = s.split("\\|");

                if (transactionDetails.length > 0 && !Util.isStringEmptyOrNull(transactionDetails[0])) {
                    Transactions.add(new Transaction(transactionDetails));
                }
            }
        }

        return Transactions;
    }
}
