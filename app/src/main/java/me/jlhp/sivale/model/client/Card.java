package me.jlhp.sivale.model.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.orhanobut.hawk.Hawk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.jlhp.sivale.utility.Util;

/**
 * Created by jjherrer on 06/03/2015.
 */
@DatabaseTable
public class Card implements Parcelable {

    @DatabaseField(generatedId = true)
    private int mId;

    @DatabaseField(canBeNull = false, unique = true)
    private String mNumber;

    @DatabaseField
    private BigDecimal mBalance;

    @DatabaseField
    private String mAlias;

    @DatabaseField
    private Date mLastUpdateDate;

    @ForeignCollectionField(eager = false)
    private Collection<Transaction> mTransactions;

    private List<Transaction> mTransactionList;

    private String mPassword;

    private Integer mSessionId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public void setBalance(BigDecimal mBalance) {
        this.mBalance = mBalance;
    }

    public void setBalance(double mBalance) {
        this.mBalance = new BigDecimal(mBalance);
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

    public List<Transaction> getTransactionList() {
        return mTransactionList == null ? new ArrayList<>(getTransactions()) : mTransactionList;
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

    public boolean unsetPassword() {
        return Hawk.remove(getPasswordKey());
    }

    public Integer getSessionId() {
        mSessionId = (mSessionId == null || mSessionId == 0) && Hawk.contains(getSessionKey()) ?
                     Hawk.<Integer>get(getSessionKey()) : mSessionId;
        return mSessionId;
    }

    public void setSessionId(Integer sessionId) {
        Hawk.put(getSessionKey(), sessionId);
        mSessionId = sessionId;
    }

    public void unsetSessionId() {
        Hawk.remove(getSessionKey());
    }

    public boolean hasValidSessionId() {
        getSessionId();
        return mSessionId != null && mSessionId > 0;
    }

    public boolean hasValidPassword() {
        getPassword();
        return mPassword != null && !Util.isStringEmptyNullOrStringNull(mPassword);
    }

    private String getPasswordKey() {
        if(Util.isStringEmptyOrNull(this.mNumber)) throw new IllegalStateException("'mNumber' must be set first");
        return this.mNumber + "_password";
    }

    private String getSessionKey() {
        if(Util.isStringEmptyOrNull(this.mNumber)) throw new IllegalStateException("'mNumber' must be set first");
        return this.mNumber + "_session";
    }

    public boolean hasValidData() {
        return hasValidSessionId() && hasValidPassword();
    }

    public void update(Card card) {
        if(card == null) return;

        setId(card.getId());
        setAlias(card.getAlias());
        setNumber(card.getNumber());
        setPassword(card.getPassword());
        setBalance(card.getBalance());
        setLastUpdateDate(card.getLastUpdateDate());
        setSessionId(card.getSessionId());
        setTransactions(card.getTransactions());
    }

    public <T> boolean cardValueTypeMatch(SiValeCardProperty propertyId, T propertyValue) {
        if(propertyId == null || propertyValue == null) {
            return false;
        }

        switch(propertyId) {
            case BALANCE :
                return propertyValue instanceof BigDecimal || propertyValue instanceof Double;
            case SESSION_ID :
                return propertyValue instanceof Integer;
            case TRANSACTIONS :
                if(propertyValue instanceof Collection<?>) {
                    for(Object o : (Collection<?>) propertyValue) {
                        if(!(o instanceof Transaction))  return false;
                    }
                    return true;
                }
                return false;
            default :
                return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeSerializable(this.mBalance);
        dest.writeString(this.mAlias);
        dest.writeLong(mLastUpdateDate != null ? mLastUpdateDate.getTime() : -1);
        dest.writeString(this.mNumber);
        dest.writeString(this.mPassword);
        dest.writeTypedList(Util.collection2List(this.mTransactions));
    }

    public Card() {
    }

    public Card(String cardNumber) {
        this.mNumber = cardNumber;
    }

    public Card(int cardId) {
        this.mId = cardId;
    }

    private Card(Parcel in) {
        this.mId = in.readInt();
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

    @Override
    public String toString() {
        return "Card{" +
                "mAlias='" + mAlias + '\'' +
                ", mLastUpdateDate=" + mLastUpdateDate +
                ", mSessionId=" + mSessionId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        if(mNumber == null) return false;

        Card card = (Card) o;

        return mNumber.equals(card.getNumber());
    }

    @Override
    public int hashCode() {
        int result = mNumber != null ? mNumber.hashCode() : 0;
        result = 31 * result + mId;
        result = 31 * result + (mBalance != null ? mBalance.hashCode() : 0);
        result = 31 * result + (mAlias != null ? mAlias.hashCode() : 0);
        result = 31 * result + (mLastUpdateDate != null ? mLastUpdateDate.hashCode() : 0);
        result = 31 * result + (mTransactions != null ? mTransactions.hashCode() : 0);
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        result = 31 * result + mSessionId;
        return result;
    }

    public enum SiValeCardProperty {
        ID, NUMBER, BALANCE, ALIAS, LAST_UPDATE_DATE, TRANSACTIONS, PASSWORD, SESSION_ID
    }
}
