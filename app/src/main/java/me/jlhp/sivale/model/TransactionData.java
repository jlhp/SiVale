package me.jlhp.sivale.model;

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

                if (transactionDetails.length > 0 && !isStringEmptyOrNull(transactionDetails[0])) {
                    Transactions.add(new Transaction(transactionDetails));
                }
            }
        }

        return Transactions;
    }

    private boolean isStringEmptyOrNull(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * The SiVale API has a bug in which even if you send a session that has been expired or is not
     * correct it will send you the same amount of transactions you have registered throughout the
     * card usage, the only catch is that they will contain no data.
     */
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

            if (!isStringEmptyOrNull(data[0])) setTransactionId(new BigInteger(data[0].trim()));
            if (!isStringEmptyOrNull(data[1])) setCardNumber(data[1].trim());
            if (!isStringEmptyOrNull(data[2])) setTransactionDate(data[2].trim());
            if (!isStringEmptyOrNull(data[3])) setAmount(Double.parseDouble(data[3].trim()));
            if (!isStringEmptyOrNull(data[4])) setCommerce(data[4].trim());
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

        public String getSpacedCommerce() {
            String commerce = Commerce;

            //Contains double space? It's not correctly spaced
            if (!isStringEmptyOrNull(commerce) && commerce.contains("  ")) {
                String[] words = commerce.split(" ");

                if (words.length > 0) {
                    StringBuilder b = new StringBuilder();

                    for (String word : words) {
                        if (!isStringEmptyOrNull(word)) {
                            b.append(word);
                            b.append(" ");
                        }
                    }

                    b.deleteCharAt(b.length() - 1);
                    commerce = b.toString();
                }
            }

            return commerce;
        }
    }
}
