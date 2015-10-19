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

import me.jlhp.sivale.event.CardOperation;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 3/19/154.
 */
public class CardDataDialog extends DialogFragment implements View.OnClickListener {
    public static final String PARAMETER_CARD = "card";
    private static final String PARAMETER_CARD_NUMBER = "card_number";
    private static final String PARAMETER_CARD_PASSWORD = "card_password";
    private static final String PARAMETER_CARD_ALIAS = "card_alias";

    private OnCardDataListener mListener;
    private Card mCard;
    private CardOperation mCardOperation;

    private View vCardLayout;
    private EditText vCardNumber;
    private EditText vCardPassword;
    private EditText vCardAlias;

    @Override
    public void onClick(View v) {
        int errors = 0;
        String cardNumber = Util.getStringFromTextView(vCardNumber);
        String cardPassword = Util.getStringFromTextView(vCardPassword);
        String cardAlias = Util.getStringFromTextView(vCardAlias);

        if(Util.isStringInteger(cardNumber) && cardNumber.length() != 16) {
            errors++;
            vCardNumber.setError("Ingrese los 16 dígitos de la tarjeta");
        }

        if(Util.isStringEmptyOrNull(cardPassword)) {
            errors++;
            vCardPassword.setError("La contraseña no puede estar vacía");
        }

        if(errors == 0) {
            mCard = mCard == null ? new Card() : mCard;
            mCard.setNumber(cardNumber);
            mCard.setPassword(cardPassword);
            mCard.setAlias(cardAlias);

            mListener.onCardData(mCard, mCardOperation);
            dismiss();
        }
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

        mCardOperation = CardOperation.ADD_DATA;
        mCard = args != null ? (Card) args.getParcelable(PARAMETER_CARD) : null;
        mListener = (OnCardDataListener) getActivity();

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        vCardLayout = layoutInflater.inflate(R.layout.dialog_card_data, null);
        vCardNumber = (EditText) vCardLayout.findViewById(R.id.card_number);
        vCardPassword = (EditText) vCardLayout.findViewById(R.id.card_password);
        vCardAlias = (EditText) vCardLayout.findViewById(R.id.card_alias);

        if(mCard != null) {
            mCardOperation = CardOperation.UPDATE_DATA;
            vCardNumber.setText(mCard.getNumber());
            vCardPassword.setText(mCard.getPassword());
            vCardAlias.setText(mCard.getAlias());
        }

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(PARAMETER_CARD_NUMBER)) {
                vCardNumber.setText(savedInstanceState.getString(PARAMETER_CARD_NUMBER));
            }
            if(savedInstanceState.containsKey(PARAMETER_CARD_PASSWORD)) {
                vCardPassword.setText(savedInstanceState.getString(PARAMETER_CARD_PASSWORD));
            }
            if(savedInstanceState.containsKey(PARAMETER_CARD_ALIAS)) {
                vCardAlias.setText(savedInstanceState.getString(PARAMETER_CARD_ALIAS));
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Ingrese los datos de la tarjeta")
                .setView(vCardLayout)
                .setPositiveButton(mCardOperation == CardOperation.UPDATE_DATA ? "Editar" : "Crear", null)
                .setNegativeButton("Cancelar", null);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARAMETER_CARD, mCard);
        outState.putString(PARAMETER_CARD_NUMBER, vCardNumber.getText().toString());
        outState.putString(PARAMETER_CARD_PASSWORD, vCardPassword.getText().toString());
        outState.putString(PARAMETER_CARD_ALIAS, vCardAlias.getText().toString());
    }

    public interface OnCardDataListener {
        void onCardData(Card card, CardOperation cardOperation);
    }
}
