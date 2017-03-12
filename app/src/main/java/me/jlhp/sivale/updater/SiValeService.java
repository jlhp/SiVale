package me.jlhp.sivale.updater;

import android.app.IntentService;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.jlhp.sivale.SiValeServices;
import me.jlhp.sivale.api.SiValeAPI;
import me.jlhp.sivale.api.SiValeAPIHandler;
import me.jlhp.sivale.database.SiValeDataHandler;
import me.jlhp.sivale.model.client.Card;

/**
 * Created by JOSELUIS on 10/18/2015.
 */
public class SiValeService extends IntentService {

    private SiValeAPIHandler mSiValeAPIHandler = new SiValeAPIHandler(true);
    private SiValeDataHandler mSiValeDataHandler = new SiValeDataHandler();

    public SiValeService() {
        super("SiValeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Card> cards = mSiValeDataHandler.getAllCards(this, true);

        if(cards == null || cards.isEmpty()) return;

        for(Card c : cards) {
            mSiValeAPIHandler.updateCard(this, c);
        }
        //new SiValeServices().consultarSaldoRequest("1", "5230130100509138", "");
/*
        AsyncHttpResponseHandler a = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.print("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.print("");
            }
        };
        a.setUseSynchronousMode(true);

        SiValeAPI b = new SiValeAPI();
        b.setSyncMode(true);

        b.getBalance(this, cards.get(0).getSessionId(), a);*/
    }
}
