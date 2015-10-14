package me.jlhp.sivale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 3/19/154.
 */
public class CardDataDialogExperiment extends DialogFragment implements View.OnClickListener {
    public static final String PARAMETER_CARD = "card";
    private static final String PARAMETER_CARD_NUMBER = "card_number";
    private static final String PARAMETER_CARD_PASSWORD = "card_password";
    private static final String PARAMETER_CARD_ALIAS = "card_alias";

    private OnCardDataListener mListener;
    private Card mCard;
    private boolean mUpdating;

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

            mListener.onCardData(mCard, mUpdating);
            dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getDialog() != null) {
            getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    /*
        @Override
        public void onResume() {
            super.onResume();

            AlertDialog dialog = (AlertDialog)getDialog();

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if(positiveButton != null) {
                positiveButton.setOnClickListener(this);
            }
        }
    */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        mUpdating = false;
        mCard = args != null ? (Card) args.getParcelable(PARAMETER_CARD) : null;
        mListener = (OnCardDataListener) getActivity();

        vCardLayout = layoutInflater.inflate(R.layout.dialog_card_data, container);
        vCardNumber = (EditText) vCardLayout.findViewById(R.id.card_number);
        vCardPassword = (EditText) vCardLayout.findViewById(R.id.card_password);
        vCardAlias = (EditText) vCardLayout.findViewById(R.id.card_alias);

        if(mCard != null) {
            mUpdating = true;
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

        return vCardLayout;
    }

    //@NonNull
    //@Override
    //public Dialog onCreateDialog(Bundle savedInstanceState) {
    //    Dialog dialog = super.onCreateDialog(savedInstanceState);
    //    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //    return dialog;
    //}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARAMETER_CARD, mCard);
        outState.putString(PARAMETER_CARD_NUMBER, vCardNumber.getText().toString());
        outState.putString(PARAMETER_CARD_PASSWORD, vCardPassword.getText().toString());
        outState.putString(PARAMETER_CARD_ALIAS, vCardAlias.getText().toString());
    }

    public interface OnCardDataListener {
        void onCardData(Card card, boolean updating);
    }
}

