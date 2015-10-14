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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.event.CardClickEvent;
import me.jlhp.sivale.model.client.Card;

/**
 * Created by jjherrer on 06/03/2015.
 */
public
    class
        CardAdapter
    extends
        RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> mCards;
    private int mRowLayout;
    private Context mContext;

    public CardAdapter(List<Card> cards, int rowLayout, Context context) {
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
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Card card = mCards.get(i);

        viewHolder.setCardBalance(card.getBalance());
        viewHolder.setCardAlias(card.getAlias());
        viewHolder.setCardLastUpdateDate(card.getLastUpdateDate());
        viewHolder.setCardNumber(card.getNumber());

        viewHolder.setListener(new ViewHolder.OnCardClickListener() {
            @Override
            public void onCardClick(View v) {
                EventBus.getDefault().post(new CardClickEvent(getOperation(v), card));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards == null ? 0 : mCards.size();
    }

    public void add(Card card) {
        mCards = mCards == null ? new ArrayList<Card>() : mCards;
        mCards.add(card);

        notifyDataSetChanged();
    }

    public void delete(Card card) {
        mCards = mCards == null ? new ArrayList<Card>() : mCards;
        mCards.remove(card);

        notifyDataSetChanged();
    }

    public void update(Card card) {
        mCards = mCards == null ? new ArrayList<Card>() : mCards;

        if(!mCards.isEmpty()) {
            for(Card c : mCards) {
                if(card.equals(c)) {
                    card.update(c);
                    break;
                }
            }
        }

        notifyDataSetChanged();
    }

    public List<Card> getAll(){
        return mCards;
    }

    private CardClickEvent.CardOperation getOperation(@NonNull View v) {
        if(v.getId() == R.id.card_edit) {
            return CardClickEvent.CardOperation.EDIT_CARD;
        }
        else if(v.getId() == R.id.card_update) {
            return CardClickEvent.CardOperation.UPDATE_DATA;
        }
        else if(v.getId() == R.id.card_delete) {
            return CardClickEvent.CardOperation.DELETE_CARD;
        }

        return CardClickEvent.CardOperation.SHOW_TRANSACTIONS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final DecimalFormat mFormatter = new DecimalFormat("###,###.##");
        private static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, hh:mm:ss a", new Locale("es", "MX"));

        private TextView vCardBalance;
        private TextView vCardAlias;
        private TextView vCardLastUpdateDate;
        private TextView vCardNumber;

        private ImageView vCardEdit;
        private ImageView vCardUpdate;
        private ImageView vCardTransactions;
        private ImageView vCardDelete;

        private OnCardClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);

            vCardBalance = (TextView) itemView.findViewById(R.id.card_balance);
            vCardAlias = (TextView) itemView.findViewById(R.id.card_alias);
            vCardLastUpdateDate = (TextView) itemView.findViewById(R.id.card_last_update_date);
            vCardNumber = (TextView) itemView.findViewById(R.id.card_number);
            vCardEdit = (ImageView) itemView.findViewById(R.id.card_edit);
            vCardUpdate = (ImageView) itemView.findViewById(R.id.card_update);
            vCardTransactions = (ImageView) itemView.findViewById(R.id.card_transactions);
            vCardDelete = (ImageView) itemView.findViewById(R.id.card_delete);

            vCardTransactions.setOnClickListener(this);
            vCardEdit.setOnClickListener(this);
            vCardUpdate.setOnClickListener(this);
            vCardDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == vCardUpdate.getId()) {
                Animation rotation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate);
                rotation.setRepeatCount(Animation.INFINITE);
                vCardUpdate.startAnimation(rotation);
            }

            mListener.onCardClick(v);
        }

        public void setCardBalance(BigDecimal balance) {
            if (balance != null) {
                this.vCardBalance.setText("$" + mFormatter.format(balance.doubleValue()));
            }
        }

        public void setCardAlias(String cardAlias) {
            this.vCardAlias.setText(cardAlias);
        }

        public void setCardLastUpdateDate(Date date) {
            if (date == null) {
                this.vCardLastUpdateDate.setText("Sin actualizar");
            }
            else{
                String d = sdf.format(date);
                d = String.valueOf(d.charAt(0)).toUpperCase() + d.substring(1);

                this.vCardLastUpdateDate.setText(d);
            }
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

        public void setListener(OnCardClickListener l) {
            mListener = l;
        }

        public interface OnCardClickListener {
            void onCardClick(View v);
        }
    }
}
