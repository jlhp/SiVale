package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;

/**
 * Created by jjherrer on 04/03/2015.
 */
public class ErrorEvent implements CallerIdRequired, CurrentOperation, NextOperations {
    private String Error;
    private SiValeOperation CurrentOperation;
    private SiValeOperation[] NextOperations;
    private int CallerId;

    public ErrorEvent(String error, int callerId) {
        this(error, callerId, null);
    }

    public ErrorEvent(String error, int callerId, SiValeOperation currentOperation) {
        this(error, callerId, currentOperation, (SiValeOperation) null);
    }

    public ErrorEvent(String error, int callerId,
                      SiValeOperation currentOperation, SiValeOperation... nextOperations) {
        Error = error;
        CallerId = callerId;
        CurrentOperation = currentOperation;
        NextOperations = nextOperations;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    @Override
    public SiValeOperation getCurrentOperation() {
        return CurrentOperation;
    }

    @Override
    public void setCurrentOperation(SiValeOperation currentOperation) {
        CurrentOperation = currentOperation;
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

    @Override
    public int getCallerId() {
        return CallerId;
    }

    @Override
    public void setCallerId(int callerId) {
        CallerId = callerId;
    }
}
