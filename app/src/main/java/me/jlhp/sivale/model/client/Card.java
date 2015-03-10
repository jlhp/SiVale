package me.jlhp.sivale.model.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.orhanobut.hawk.Hawk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import me.jlhp.sivale.utility.Util;

/**
 * Created by jjherrer on 06/03/2015.
 */
@DatabaseTable
public class Card implements Parcelable {

    @DatabaseField(id = true)
    private String mNumber;

    @DatabaseField(canBeNull = false)
    private BigDecimal mBalance;

    @DatabaseField
    private String mAlias;

    @DatabaseField
    private Date mLastUpdateDate;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Collection<Transaction> mTransactions;

    private String mPassword;

    private int mSessionId;

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

    public Collection<Transaction> getTransactions() {
        return mTransactions;
    }

    public void setTransactions(Collection<Transaction> mTransactions) {
        this.mTransactions = mTransactions;
    }

    public String getPassword() {
        mPassword = mPassword == null && Hawk.contains(getPasswordKey()) ?
                    Hawk.<String>get(getPasswordKey()) : mPassword;
        return mPassword;
    }

    public void setPassword(String password) {
        Hawk.put(getPasswordKey(), password);
        mPassword = password;
    }

    public int getSessionId() {
        mSessionId = mSessionId == 0 && Hawk.contains(getSessionKey()) ?
                     (int) Hawk.get(getSessionKey()) : mSessionId;
        return mSessionId;
    }

    public void setSessionId(int sessionId) {
        Hawk.put(getSessionKey(), sessionId);
        mSessionId = sessionId;
    }

    private String getPasswordKey() {
        if(Util.isStringEmptyOrNull(this.mNumber)) throw new IllegalStateException("'mNumber' must be set first");
        return this.mNumber + "_password";
    }

    private String getSessionKey() {
        if(Util.isStringEmptyOrNull(this.mNumber)) throw new IllegalStateException("'mNumber' must be set first");
        return this.mNumber + "_session";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mBalance);
        dest.writeString(this.mAlias);
        dest.writeLong(mLastUpdateDate != null ? mLastUpdateDate.getTime() : -1);
        dest.writeString(this.mNumber);
        dest.writeString(this.mPassword);
        dest.writeList(Util.collection2List(mTransactions));
    }

    public Card() {
    }

    public Card(String cardNumber) {
        this.mNumber = cardNumber;
    }


    private Card(Parcel in) {
        this.mBalance = (BigDecimal) in.readSerializable();
        this.mAlias = in.readString();
        long tmpMLastUpdateDate = in.readLong();
        this.mLastUpdateDate = tmpMLastUpdateDate == -1 ? null : new Date(tmpMLastUpdateDate);
        this.mNumber = in.readString();
        this.mPassword = in.readString();
        this.mTransactions = new ArrayList<>();
        in.readTypedList((List<Transaction>) this.mTransactions, Transaction.CREATOR);
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
