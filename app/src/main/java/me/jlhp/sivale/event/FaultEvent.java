package me.jlhp.sivale.event;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;

import me.jlhp.sivale.api.SiValeOperation;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class FaultEvent implements CallerIdRequired, CurrentOperation, NextOperations {
    private SOAP11Fault SoapFault;
    private SiValeOperation CurrentOperation;
    private SiValeOperation[] NextOperations;
    private int CallerId;

    public FaultEvent(SOAP11Fault soapFault, int callerId) {
        this(soapFault, callerId, null);
    }

    public FaultEvent(SOAP11Fault soapFault, int callerId, SiValeOperation currentOperation) {
        SoapFault = soapFault;
        CallerId = callerId;
        CurrentOperation = currentOperation;
    }

    public FaultEvent(SOAP11Fault soapFault, int callerId,
                      SiValeOperation currentOperation, SiValeOperation... nextOperations) {
        SoapFault = soapFault;
        CallerId = callerId;
        CurrentOperation = currentOperation;
        NextOperations = nextOperations;
    }

    public SOAP11Fault getSoapFault() {
        return SoapFault;
    }

    public void setSoapFault(SOAP11Fault soapFault) {
        SoapFault = soapFault;
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
