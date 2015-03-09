package me.jlhp.sivale;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.jlhp.sivale.model.CardData;

/**
 * Created by jjherrer on 06/03/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<CardData> mCards;
    private int mRowLayout;
    private Context mContext;

    public CardAdapter(List<CardData> cards, int rowLayout, Context context) {
        mCards = cards;
        mRowLayout = rowLayout;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CardData card = mCards.get(i);

        viewHolder.setCardBalance(card.getBalance());
        viewHolder.setCardAlias(card.getAlias());
        viewHolder.setCardLastUpdateDate(card.getLastUpdateDate());
        viewHolder.setCardNumber(card.getNumber());
    }

    @Override
    public int getItemCount() {
        return mCards == null ? 0 : mCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final DecimalFormat mFormatter = new DecimalFormat("###,###.##");
        private static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, hh:mm:ss a", new Locale("es", "MX"));

        private TextView vCardBalance;
        private TextView vCardAlias;
        private TextView vCardLastUpdateDate;
        private TextView vCardNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            vCardBalance = (TextView) itemView.findViewById(R.id.card_balance);
            vCardAlias = (TextView) itemView.findViewById(R.id.card_alias);
            vCardLastUpdateDate = (TextView) itemView.findViewById(R.id.card_last_update_date);
            vCardNumber = (TextView) itemView.findViewById(R.id.card_number);
        }

        public void setCardBalance(BigDecimal balance) {
            if (balance == null) {
                throw new IllegalArgumentException("'balance' should not be null");
            }

            this.vCardBalance.setText("$" + mFormatter.format(balance.doubleValue()));
        }

        public void setCardAlias(String cardAlias) {
            this.vCardAlias.setText(cardAlias);
        }

        public void setCardLastUpdateDate(Date date) {
            if (date == null) {
                throw new IllegalArgumentException("'date' should not be null");
            }

            String d = sdf.format(date);
            d = String.valueOf(d.charAt(0)).toUpperCase() + d.substring(1);

            this.vCardLastUpdateDate.setText(d);
        }

        public void setCardNumber(String cardNumber) {
            if (cardNumber == null) {
                throw new IllegalArgumentException("'cardNumber' should not be null");
            }

            if (cardNumber.length() != 16) {
                throw new IllegalArgumentException("'cardNumber' should contain 16 numbers");
            }

            this.vCardNumber.setText("**** - **** - **** - " + cardNumber.substring(12));
        }
    }
}
