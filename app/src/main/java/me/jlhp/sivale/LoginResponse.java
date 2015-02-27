package me.jlhp.sivale;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by jjherrer on 26/02/2015.
 */
@XMLObject("//item")
public class LoginResponse {

    @XMLField("tipoTarjeta")
    public String CardType;

    @XMLField("descError")
    public String ErrorDescription;

    @XMLField("nomTarjeta")
    public String CardholderName;

    @XMLField("numTarjeta")
    public String CardNumber;

    @XMLField("estatusSesion")
    public int SessionStatus;

    @XMLField("cveError")
    public int CVEError;

    @XMLField("customerNumber")
    public String CustomerNumber;

    @XMLField("tiempoDispSeg")
    public int SecondsAvailable;

    @XMLField("idSesion")
    public int SessionId;

    @XMLField("idNivelSeguridad")
    public String SecurityLevel;

    @XMLField("numCuenta")
    public String AccountNumber;

    @XMLField("cveEmisor")
    public String CVEEmisor;

}
