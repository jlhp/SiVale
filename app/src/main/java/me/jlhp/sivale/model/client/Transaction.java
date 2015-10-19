package me.jlhp.sivale.model.client;

/**
 * Created by JOSELUIS on 3/8/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.jlhp.sivale.utility.Util;

/**
 * The SiVale API has a bug in which even if you send a session that has been expired or is not
 * correct it will send you the same amount of transactions you have registered throughout the
 * card usage, the only catch is that they will contain no data.
 */
@DatabaseTable
public class Transaction implements Parcelable {

    @DatabaseField(id = true)
    private String mTransactionId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Card mCard;

    @DatabaseField
    private Date mTransactionDate;

    @DatabaseField
    private BigDecimal mAmount;

    @DatabaseField
    private String mCommerce;

    private String mSpacedCommerce;

    public Transaction(String[] data) {
        if (data == null || data.length < 5) {
            throw new IllegalArgumentException("'data' must not be null and should contain at least 5 elements");
        }

        if (!Util.isStringEmptyNullOrStringNull(data[0])) setTransactionId(data[0]);
        if (!Util.isStringEmptyOrNull(data[1])) setCard(data[1].trim());
        if (!Util.isStringEmptyOrNull(data[2])) setTransactionDate(data[2]);
        if (!Util.isStringEmptyOrNull(data[3])) setAmount(data[3]);
        if (!Util.isStringEmptyOrNull(data[4])) setCommerce(data[4].trim());
        if(Util.isStringEmptyOrNull(mTransactionId)) setDefaultTransactionId();
    }

    public Transaction() {}

    public String getTransactionId() {
        return mTransactionId;
    }

    public void setTransactionId(String transactionId) {
        mTransactionId = transactionId;
    }

    public void setDefaultTransactionId() {
        mTransactionId = "";

        if(mTransactionDate != null) {
            mTransactionId += mTransactionDate.toString();
        }

        if(mAmount.toString() != null) {
            mTransactionId += mAmount.toString();
        }

        if(mCommerce != null) {
            mTransactionId += mCommerce;
        }
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        mCard = card;
    }

    public void setCard(String cardNumber) {
        this.mCard = new Card(cardNumber);
    }

    public void setCard(int cardId) {
        this.mCard = new Card(cardId);
    }

    public Date getTransactionDate() {
        return mTransactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        mTransactionDate = transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

        try {
            mTransactionDate = sdf.parse(transactionDate.trim());
        } catch (ParseException e) {
            if(e.getMessage().contains("Unparseable date")) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.US);

                try {
                    mTransactionDate = sdf2.parse(transactionDate.trim());
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }
            else {
                e.printStackTrace();
            }
        }
    }

    public BigDecimal getAmount() {
        return mAmount;
    }

    public void setAmount(BigDecimal amount) {
        mAmount = amount;
    }

    public void setAmount(String amount) {
        BigDecimal d = new BigDecimal(-1);

        try {
            d = Util.isStringEmptyOrNull(amount) ? BigDecimal.valueOf(-1) :
                    new BigDecimal(amount.trim());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        mAmount = d.compareTo(BigDecimal.valueOf(0)) < 0 ?
                  d.multiply(BigDecimal.valueOf(-1)) : d;
    }

    public String getCommerce() {
        return mCommerce;
    }

    public void setCommerce(String commerce) {
        mCommerce = commerce;
    }

    public String getSpacedCommerce() {
        if (mSpacedCommerce != null) return mSpacedCommerce;

        mSpacedCommerce = mCommerce;

        //Contains double space? Then it isn't correctly spaced
        if (!Util.isStringEmptyOrNull(mSpacedCommerce) && mSpacedCommerce.contains("  ")) {
            String[] words = mSpacedCommerce.split(" ");

            if (words.length > 0) {
                StringBuilder b = new StringBuilder();

                for (String word : words) {
                    if (!Util.isStringEmptyOrNull(word)) {
                        b.append(word);
                        b.append(" ");
                    }
                }

                b.deleteCharAt(b.length() - 1);
                mSpacedCommerce = b.toString();
            }
        }

        return mSpacedCommerce;
    }

    public boolean isValid() {
        return mTransactionDate != null && mAmount != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(mCard != null) mCard.setTransactions(null);
        dest.writeString(this.mTransactionId);
        dest.writeParcelable(this.mCard, 0);
        dest.writeLong(mTransactionDate != null ? mTransactionDate.getTime() : -1);
        dest.writeDouble(this.mAmount.doubleValue());
        dest.writeString(this.mCommerce);
        dest.writeString(this.mSpacedCommerce);
    }

    private Transaction(Parcel in) {
        this.mTransactionId = in.readString();
        this.mCard = in.readParcelable(Card.class.getClassLoader());
        long tmpTransactionDate = in.readLong();
        this.mTransactionDate = tmpTransactionDate == -1 ? null : new Date(tmpTransactionDate);
        this.mAmount = new BigDecimal(in.readDouble());
        this.mCommerce = in.readString();
        this.mSpacedCommerce = in.readString();
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
