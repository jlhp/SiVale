package me.jlhp.sivale;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by jjherrer on 03/03/2015.
 */
@XMLObject("//return")
public class TransactionData {
    @XMLField("saldo")
    private double Balance;

    @XMLField("//objEstatus/item")
    private SessionData SessionData;

}
