package me.jlhp.sivale.model.client;

/**
 * Created by JOSELUIS on 3/8/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.jlhp.sivale.utility.Util;

/**
 * The SiVale API has a bug in which even if you send a session that has been expired or is not
 * correct it will send you the same amount of transactions you have registered throughout the
 * card usage, the only catch is that they will contain no data.
 */
public class Transaction implements Parcelable {

    @DatabaseField(id = true)
    private BigInteger TransactionId;

    @DatabaseField
    private String CardNumber;

    @DatabaseField
    private Date TransactionDate;

    @DatabaseField
    private BigDecimal Amount;

    @DatabaseField
    private String Commerce;

    private String SpacedCommerce;

    public Transaction(String[] data) {
        if (data == null || data.length < 5) {
            throw new IllegalArgumentException("'data' must not be null and should contain at least 5 elements");
        }

        if (!Util.isStringEmptyOrNull(data[0])) setTransactionId(data[0]);
        if (!Util.isStringEmptyOrNull(data[1])) setCardNumber(data[1].trim());
        if (!Util.isStringEmptyOrNull(data[2])) setTransactionDate(data[2]);
        if (!Util.isStringEmptyOrNull(data[3])) setAmount(data[3]);
        if (!Util.isStringEmptyOrNull(data[4])) setCommerce(data[4].trim());
    }

    public BigInteger getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        TransactionId = transactionId;
    }

    public void setTransactionId(String transactionId) {
        BigInteger b = null;

        try {
            b = Util.isStringEmptyOrNull(transactionId) ? new BigInteger("-1") :
                    new BigInteger(transactionId.trim());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        TransactionId = b;
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
            TransactionDate = simpleDateFormat.parse(transactionDate.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public void setAmount(String amount) {
        BigDecimal d = null;

        try {
            d = Util.isStringEmptyOrNull(amount) ? BigDecimal.valueOf(-1) :
                    new BigDecimal(amount.trim());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        Amount = d;
    }

    public String getCommerce() {
        return Commerce;
    }

    public void setCommerce(String commerce) {
        Commerce = commerce;
    }

    public String getSpacedCommerce() {
        if (SpacedCommerce != null) return SpacedCommerce;

        SpacedCommerce = Commerce;

        //Contains double space? Then it isn't correctly spaced
        if (!Util.isStringEmptyOrNull(SpacedCommerce) && SpacedCommerce.contains("  ")) {
            String[] words = SpacedCommerce.split(" ");

            if (words.length > 0) {
                StringBuilder b = new StringBuilder();

                for (String word : words) {
                    if (!Util.isStringEmptyOrNull(word)) {
                        b.append(word);
                        b.append(" ");
                    }
                }

                b.deleteCharAt(b.length() - 1);
                SpacedCommerce = b.toString();
            }
        }

        return SpacedCommerce;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.TransactionId);
        dest.writeString(this.CardNumber);
        dest.writeLong(TransactionDate != null ? TransactionDate.getTime() : -1);
        dest.writeSerializable(this.Amount);
        dest.writeString(this.Commerce);
        dest.writeString(this.SpacedCommerce);
    }

    private Transaction(Parcel in) {
        this.TransactionId = (BigInteger) in.readSerializable();
        this.CardNumber = in.readString();
        long tmpTransactionDate = in.readLong();
        this.TransactionDate = tmpTransactionDate == -1 ? null : new Date(tmpTransactionDate);
        this.Amount = (BigDecimal) in.readSerializable();
        this.Commerce = in.readString();
        this.SpacedCommerce = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
