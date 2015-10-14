package me.jlhp.sivale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.event.CardClickEvent;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Transaction;

/**
 * Created by jjherrer on 06/03/2015.
 */
public
    class
        TransactionAdapter
    extends
        RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> mTransactions;
    private int mRowLayout;
    private Context mContext;

    public TransactionAdapter(List<Transaction> transactions, int rowLayout, Context context) {
        mTransactions = transactions;
        mRowLayout = rowLayout;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Transaction transaction = mTransactions.get(i);

        viewHolder.setAmount(transaction.getAmount());
        viewHolder.setDate(transaction.getTransactionDate());
        viewHolder.setCommerce(transaction.getSpacedCommerce());
    }

    @Override
    public int getItemCount() {
        return mTransactions == null ? 0 : mTransactions.size();
    }

    public List<Transaction> getAll(){
        return mTransactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final DecimalFormat mFormatter = new DecimalFormat("###,###.##");
        private static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, hh:mm:ss a", new Locale("es", "MX"));

        private TextView vAmount;
        private TextView vDate;
        private TextView vCommerce;

        public ViewHolder(View itemView) {
            super(itemView);

            vAmount = (TextView) itemView.findViewById(R.id.amount);
            vDate = (TextView) itemView.findViewById(R.id.date);
            vCommerce = (TextView) itemView.findViewById(R.id.commerce);
        }

        public void setAmount(BigDecimal amount) {
            if (amount != null) {
                this.vAmount.setText("$" + mFormatter.format(amount.doubleValue()));
            }
        }

        public void setDate(Date date) {
            if (date == null) {
                this.vDate.setText("");
            }
            else{
                String d = sdf.format(date);
                d = String.valueOf(d.charAt(0)).toUpperCase() + d.substring(1);

                this.vDate.setText(d);
            }
        }

        public void setCommerce(String commerce) {
            this.vCommerce.setText(commerce);
        }
    }
}
