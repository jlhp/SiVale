package me.jlhp.sivale.model;

import com.alexgilleran.icesoap.annotation.XMLField;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class SoapData {
    @XMLField("//objEstatus/item")
    private SessionData SessionData;

    public SessionData getSessionData() {
        return SessionData;
    }

    public boolean isError() {
        return SessionData != null && SessionData.isError();
    }
}
