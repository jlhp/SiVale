package me.jlhp.sivale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 3/19/154.
 */
public class CardDeleteDialog extends DialogFragment implements View.OnClickListener {
    public static final String PARAMETER_CARD = "card";

    private OnCardDeleteListener mListener;
    private Card mCard;

    @Override
    public void onClick(View v) {
        mListener.onCardDelete(mCard);
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

        AlertDialog dialog = (AlertDialog)getDialog();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if(positiveButton != null) {
            positiveButton.setOnClickListener(this);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        mCard = args != null ? (Card) args.getParcelable(PARAMETER_CARD) : null;
        mListener = (OnCardDeleteListener) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setIcon(R.mipmap.ic_remove_big)
            .setTitle("Borrar tarjeta")
            .setMessage("Deseas borrar la tarjeta: '" + mCard.getAlias() + "'?")
            .setPositiveButton("Sí", null)
            .setNegativeButton("No", null);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARAMETER_CARD, mCard);
    }

    public interface OnCardDeleteListener {
        void onCardDelete(Card card);
    }
}

