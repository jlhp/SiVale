package me.jlhp.sivale.event;

import me.jlhp.sivale.model.SessionData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class LoginEvent {
    private SessionData SessionData;

    public LoginEvent(SessionData sessionData) {
        SessionData = sessionData;
    }

    public SessionData getSessionData() {
        return SessionData;
    }

    public void setSessionData(SessionData sessionData) {
        SessionData = sessionData;
    }
}
