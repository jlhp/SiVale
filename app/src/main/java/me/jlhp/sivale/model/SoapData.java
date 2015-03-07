package me.jlhp.sivale.model;

import com.alexgilleran.icesoap.annotation.XMLField;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class SoapData implements SiValeData {
    @XMLField("//objEstatus/item")
    private SessionData SessionData;

    public SessionData getSessionData() {
        return SessionData;
    }

    @Override
    public boolean isError() {
        return SessionData != null && SessionData.isError();
    }

    @Override
    public String getError() {
        return SessionData != null ? SessionData.getError() : null;
    }
}