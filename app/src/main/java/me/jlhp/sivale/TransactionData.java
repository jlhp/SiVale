package me.jlhp.sivale;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jjherrer on 03/03/2015.
 */
@XMLObject("//return")
public class TransactionData {

    @XMLField(value = "//objMovimientos/item")
    private List<String> StringTransactions;

    @XMLField("//objEstatus/item")
    private SessionData SessionData;

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
                Transactions.add(new Transaction(s.split("\\|")));
            }
        }

        return Transactions;
    }

    public SessionData getSessionData() {
        return SessionData;
    }

    public void setSessionData(SessionData sessionData) {
        SessionData = sessionData;
    }

    private class Transaction {
        private BigInteger TransactionId;
        private String CardNumber;
        private Date TransactionDate;
        private Double Amount;
        private String Commerce;

        public Transaction(String[] data) {
            if (data == null || data.length < 5) {
                throw new IllegalArgumentException("'data' must not be null and should contain at least 5 elements");
            }

            setTransactionId(new BigInteger(data[0].trim()));
            setCardNumber(data[1].trim());
            setTransactionDate(data[2].trim());
            setAmount(Double.parseDouble(data[3].trim()));
            setCommerce(data[4].trim());
        }

        public BigInteger getTransactionId() {
            return TransactionId;
        }

        public void setTransactionId(BigInteger transactionId) {
            TransactionId = transactionId;
        }

        public String getCardNumber() {
            return CardNumber;
        }

        public void setCardNumber(String cardNumber) {
            CardNumber = cardNumber;
        }

        public Date getTransactionDate() {
            return TransactionDate;
        }

        public void setTransactionDate(Date transactionDate) {
            TransactionDate = transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            try {
                TransactionDate = simpleDateFormat.parse(transactionDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public Double getAmount() {
            return Amount;
        }

        public void setAmount(Double amount) {
            Amount = amount;
        }

        public String getCommerce() {
            return Commerce;
        }

        public void setCommerce(String commerce) {
            Commerce = commerce;
        }
    }
}
