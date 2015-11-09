package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.soapfault.SOAP11Fault;

/**
 * Created by JOSELUIS on 10/18/2015.
 */
public class FaultData extends SOAP11Fault implements SiValeData {

    public FaultData(String faultCode, String faultString, String faultActor) {
        super(faultCode, faultString, faultActor);
    }

    @Override
    public boolean isSessionExpired() {
        return "NO EXISTE SESION".equalsIgnoreCase(getFaultString());
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public String getError() {
        return getFaultString();
    }

    @Override
    public boolean isInvalidLogin() {
        return "PASSWORD INCORRECTO".equalsIgnoreCase(getError())
                || "DATOS DESCONOCIDOS DE TARJETA".equalsIgnoreCase(getError()) ;
    }
}
