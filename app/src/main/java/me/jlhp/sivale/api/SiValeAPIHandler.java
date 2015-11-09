package me.jlhp.sivale.api;

import android.content.Context;

import java.util.Arrays;

import de.greenrobot.event.EventBus;
import me.jlhp.sivale.database.SiValeDataHandler;
import me.jlhp.sivale.event.CardOperation;
import me.jlhp.sivale.event.CardOperationEvent;
import me.jlhp.sivale.event.ErrorEvent;
import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.server.BalanceData;
import me.jlhp.sivale.model.server.SessionData;
import me.jlhp.sivale.model.server.SiValeData;
import me.jlhp.sivale.model.server.TransactionData;
import me.jlhp.sivale.utility.Logger;
import me.jlhp.sivale.utility.Util;

/**
 * Created by JOSELUIS on 4/5/2015.
 */

/*
TODO Maybe I should be calling the API, and the API the handler, and not backwards... I don't know.
*/
public class SiValeAPIHandler {
    private final SiValeAPI API = new SiValeAPI();
    private boolean mRequestInProgress = false;
    private SiValeDataHandler mSiValeData = new SiValeDataHandler();

    public SiValeAPIHandler() {

    }

    public SiValeAPIHandler(boolean syncMode) {
        API.setSyncMode(syncMode);
    }

    public void login(Context context,
                      String cardNumber,
                      String password,
                      int callerId,
                      SiValeOperation... nextOperations) {
        API.login(context,
                cardNumber,
                password,
                new SiValeResponseHandlerImpl<>(SessionData.class,
                        context,
                        callerId,
                        SiValeOperation.LOGIN,
                        nextOperations));
    }

    public void getBalance(Context context,
                           int sessionId,
                           int callerId,
                           SiValeOperation... nextOperations) {
        API.getBalance(context,
                sessionId,
                new SiValeResponseHandlerImpl<>(BalanceData.class,
                        context,
                        callerId,
                        SiValeOperation.GET_BALANCE,
                        nextOperations));
    }

    public void getTransactions(Context context,
                                int sessionId,
                                int callerId,
                                SiValeOperation... nextOperations) {
        API.getTransactions(context,
                sessionId,
                new SiValeResponseHandlerImpl<>(BalanceData.class,
                        context,
                        callerId,
                        SiValeOperation.GET_TRANSACTIONS,
                        nextOperations));
    }

    public void updateCard(Context context, Card card) {
        if(context == null || card == null) throw new IllegalArgumentException("'context' and 'card' should not be null");

        executeOperation(context, card, SiValeOperation.GET_BALANCE, SiValeOperation.GET_TRANSACTIONS);
    }

    private void executeOperation(Context context,
                                  Card card,
                                  SiValeOperation... operations) {
        if(card == null || operations == null || operations.length == 0) {
            return;
        }

        Logger.getInstance().logInfo(card.toString());
        Logger.getInstance().logInfo(Arrays.toString(operations));

        if(isRequestInProgress()) {
            postErrorEvent("Favor de esperar un momento",
                           card.getId(),
                           true,
                           operations[0],
                           Util.removeFirstItemFromArray(operations));
            return;
        }
        else if(!card.hasValidPassword()) {
            postErrorEvent("Favor de ingresar una contraseña para la tarjeta: " + card.getAlias(),
                    card.getId(),
                    true,
                    operations[0],
                    Util.removeFirstItemFromArray(operations));
            return;
        }
        else if(!card.hasValidSessionId()) {
            if(operations[0] != SiValeOperation.LOGIN) {
                login(context, card.getNumber(), card.getPassword(), card.getId(), Util.addItemsToArray(SiValeOperation.LOGIN, operations));
            }
            else {
                postErrorEvent("Error con SiVale. Por favor intenta más tarde.",
                        card.getId(),
                        true,
                        operations[0],
                        Util.removeFirstItemFromArray(operations));
            }

            return;
        }

        SiValeOperation executeOperation = operations[0];
        operations = Util.removeFirstItemFromArray(operations);

        switch (executeOperation) {
            case LOGIN:
                login(context, card.getNumber(), card.getPassword(), card.getId(), operations);
                break;
            case GET_BALANCE:
                getBalance(context, card.getSessionId(), card.getId(), operations);
                break;
            case GET_TRANSACTIONS:
                getTransactions(context, card.getSessionId(), card.getId(), operations);
                break;
        }
    }

    private void loginAndRetry(Context context, int callerId, SiValeOperation currentOperation, SiValeOperation... nextOperations) {
        nextOperations = Util.addItemsToArray(currentOperation, nextOperations);

        executeOperation(context,
                mSiValeData.getCard(context, callerId),
                Util.addItemsToArray(SiValeOperation.LOGIN, nextOperations));
    }

    public boolean isRequestInProgress() {
        return mRequestInProgress;
    }

    private void setRequestInProgress(boolean requestInProgress) {
        mRequestInProgress = requestInProgress;
    }

    private void postEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    private void postErrorEvent(String error,
                                  int callerId,
                                  boolean showToUser,
                                  SiValeOperation currentOperation,
                                  SiValeOperation... nextOperations) {
        EventBus.getDefault().postSticky(new ErrorEvent(error,
                callerId,
                showToUser,
                currentOperation,
                nextOperations));
    }

    private class SiValeResponseHandlerImpl<ResponseType extends SiValeData> extends SiValeResponseHandler<ResponseType> {

        private Context mContext;
        private int mCallerId;
        private SiValeOperation mCurrentOperation;
        private SiValeOperation[] mNextOperations;

        public SiValeResponseHandlerImpl(Class<ResponseType> responseClass,
                                         Context context,
                                         int callerId,
                                         SiValeOperation currentOperation,
                                         SiValeOperation... nextOperations) {
            super(responseClass);

            this.mContext = context;
            this.mCallerId = callerId;
            this.mCurrentOperation = currentOperation;
            this.mNextOperations = nextOperations;
        }

        @Override
        public void onStart() {
            setRequestInProgress(true);
        }

        @Override
        public void onSuccess(ResponseType response) {
            setRequestInProgress(false);

            Card card;

            if(response instanceof SessionData) {
                SessionData sessionData = (SessionData) response;

                card = mSiValeData.updateCardProperty(mContext,
                        mCallerId,
                        Card.SiValeCardProperty.SESSION_ID,
                        sessionData.getSessionId());
            } else if(response instanceof BalanceData) {
                BalanceData balanceData = (BalanceData) response;

                card = mSiValeData.updateCardProperty(mContext,
                        mCallerId,
                        Card.SiValeCardProperty.BALANCE,
                        balanceData.getBalance());
            } else if(response instanceof TransactionData) {
                TransactionData transactionData = (TransactionData) response;

                card = mSiValeData.updateCardProperty(mContext,
                        mCallerId,
                        Card.SiValeCardProperty.TRANSACTIONS,
                        transactionData.getTransactions());
            } else {
                throw new IllegalStateException("Response type is unknown");
            }

            if (mNextOperations == null || mNextOperations.length == 0) {
                postEvent(new CardOperationEvent(card,
                        CardOperation.UPDATE_DATA,
                        mNextOperations));
            } else {
                executeOperation(mContext, card, mNextOperations);
            }
        }

        @Override
        public void onInvalidLogin() {
            super.onInvalidLogin();

            setRequestInProgress(false);

            postErrorEvent("La contraseña o la tarjeta son incorrectos",
                    mCallerId,
                    true,
                    mCurrentOperation,
                    mNextOperations);
        }

        @Override
        public void onSessionExpired() {
            super.onSessionExpired();

            setRequestInProgress(false);

            loginAndRetry(mContext, mCallerId, mCurrentOperation, mNextOperations);
        }

        @Override
        public void onError(String response) {
            super.onError(response);

            setRequestInProgress(false);

            postErrorEvent(response,
                    mCallerId,
                    false,
                    mCurrentOperation,
                    mNextOperations);
        }
    }
}
