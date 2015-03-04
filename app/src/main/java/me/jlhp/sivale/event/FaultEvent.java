package me.jlhp.sivale.event;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;

import me.jlhp.sivale.api.SiValeOperation;

/**
 * Created by jjherrer on 03/03/2015.
 */
public class FaultEvent {
    private SOAP11Fault SoapFault;
    private SiValeOperation Operation;

    public FaultEvent(SOAP11Fault soapFault, SiValeOperation operation) {
        SoapFault = soapFault;
        Operation = operation;
    }

    public SOAP11Fault getSoapFault() {
        return SoapFault;
    }

    public void setSoapFault(SOAP11Fault soapFault) {
        SoapFault = soapFault;
    }

    public SiValeOperation getOperation() {
        return Operation;
    }

    public void setOperation(SiValeOperation operation) {
        Operation = operation;
    }
}
