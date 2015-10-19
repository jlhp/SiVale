package me.jlhp.sivale.updater;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

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
    }
}
