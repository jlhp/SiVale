package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;

/**
 * Created by jjherrer on 04/03/2015.
 */
public class ErrorEvent {
    private String Error;
    private SiValeOperation Operation;

    public ErrorEvent(String error, SiValeOperation operation) {
        Error = error;
        Operation = operation;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public SiValeOperation getOperation() {
        return Operation;
    }

    public void setOperation(SiValeOperation operation) {
        Operation = operation;
    }
}
