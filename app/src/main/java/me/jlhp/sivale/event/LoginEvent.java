package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;
import me.jlhp.sivale.model.server.SessionData;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class LoginEvent {
    private SessionData SessionData;
    private SiValeOperation RetryOperation;

    public LoginEvent(SessionData sessionData) {
        SessionData = sessionData;
    }

    public LoginEvent(SessionData sessionData, SiValeOperation operation) {
        SessionData = sessionData;
        RetryOperation = operation;
    }

    public SessionData getSessionData() {
        return SessionData;
    }

    public void setSessionData(SessionData sessionData) {
        SessionData = sessionData;
    }

    public SiValeOperation getRetryOperation() {
        return RetryOperation;
    }

    public void setRetryOperation(SiValeOperation retryOperation) {
        RetryOperation = retryOperation;
    }

    public boolean hasRetryOperation() {
        return RetryOperation != null;
    }
}
