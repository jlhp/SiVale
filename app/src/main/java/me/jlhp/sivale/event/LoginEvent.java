package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.model.server.SessionData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class LoginEvent implements CallerIdRequired, me.jlhp.sivale.event.NextOperations {
    private SessionData SessionData;
    private int CallerId;
    private SiValeOperation[] NextOperations;

    public LoginEvent(SessionData sessionData) {
        this(sessionData, (SiValeOperation) null);
    }

    public LoginEvent(SessionData sessionData, SiValeOperation... operations) {
        this(sessionData, 0, operations);
    }

    public LoginEvent(SessionData sessionData, int callerId, SiValeOperation... nextOperations) {
        SessionData = sessionData;
        CallerId = callerId;
        NextOperations = nextOperations;
    }

    public SessionData getSessionData() {
        return SessionData;
    }

    public void setSessionData(SessionData sessionData) {
        SessionData = sessionData;
    }

    @Override
    public int getCallerId() {
        return CallerId;
    }

    @Override
    public void setCallerId(int callerId) {
        CallerId = callerId;
    }

    @Override
    public SiValeOperation[] getNextOperations() {
        return NextOperations;
    }

    @Override
    public void setNextOperations(SiValeOperation... nextOperations) {
        NextOperations = nextOperations;
    }

    @Override
    public boolean hasNextOperation(){
        return NextOperations != null && NextOperations.length > 0;
    }
}