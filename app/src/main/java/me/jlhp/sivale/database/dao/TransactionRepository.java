package me.jlhp.sivale.database.dao;

import android.content.Context;

import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Transaction;

/**
 * Created by JOSELUIS on 3/11/2015.
 */
public class TransactionRepository extends BaseRepository<Transaction> {
    public TransactionRepository(Context ctx) {
        super(ctx, Transaction.class);
    }

    public int create(Collection<Transaction> transactions, int cardId) {
        if(transactions == null || transactions.isEmpty()) return 0;

        int rows = 0;

        for(Transaction transaction : transactions) {
            if(cardId > 0) transaction.setCard(cardId);
            rows += create(transaction);
        }

        return rows;
    }

    public int update(Collection<Transaction> transactions, int cardId) {
        if(transactions == null || transactions.isEmpty()) return 0;

        int rows = 0;

        for(Transaction transaction : transactions) {
            if(cardId > 0) transaction.setCard(cardId);
            rows += createOrUpdate(transaction);
        }

        return rows;
    }

    public Collection<Transaction> getAllTransactionsFromCard(Card card) {
        if(card == null) return null;

        Transaction transaction = new Transaction();
        transaction.setCard(card);

        try {
            return getDao().queryBuilder()
                   .orderBy("mTransactionDate", false)
                   .where().eq("mCard_id", card.getId())
                   .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
