package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by jjherrer on 26/02/2015.
 */
@XMLObject("//item")
public class SessionData implements SiValeData {

    @XMLField("tipoTarjeta")
    private String CardType;

    @XMLField("descError")
    private String ErrorDescription;

    @XMLField("nomTarjeta")
    private String CardholderName;

    @XMLField("numTarjeta")
    private String CardNumber;

    @XMLField("estatusSesion")
    private int SessionStatus;

    @XMLField("cveError")
    private int CVEError;

    @XMLField("customerNumber")
    private String CustomerNumber;

    @XMLField("tiempoDispSeg")
    private int SecondsAvailable;

    @XMLField("idSesion")
    private int SessionId;

    @XMLField("idNivelSeguridad")
    private String SecurityLevel;

    @XMLField("numCuenta")
    private String AccountNumber;

    @XMLField("cveEmisor")
    private String CVEEmisor;

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getErrorDescription() {
        return ErrorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        ErrorDescription = errorDescription;
    }

    public String getCardholderName() {
        return CardholderName;
    }

    public void setCardholderName(String cardholderName) {
        CardholderName = cardholderName;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public int getSessionStatus() {
        return SessionStatus;
    }

    public void setSessionStatus(int sessionStatus) {
        SessionStatus = sessionStatus;
    }

    public int getCVEError() {
        return CVEError;
    }

    public void setCVEError(int CVEError) {
        this.CVEError = CVEError;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public int getSecondsAvailable() {
        return SecondsAvailable;
    }

    public void setSecondsAvailable(int secondsAvailable) {
        SecondsAvailable = secondsAvailable;
    }

    public int getSessionId() {
        return SessionId;
    }

    public void setSessionId(int sessionId) {
        SessionId = sessionId;
    }

    public String getSecurityLevel() {
        return SecurityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        SecurityLevel = securityLevel;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getCVEEmisor() {
        return CVEEmisor;
    }

    public void setCVEEmisor(String CVEEmisor) {
        this.CVEEmisor = CVEEmisor;
    }

    @Override
    public boolean isError() {
        return ErrorDescription != null && !ErrorDescription.equalsIgnoreCase("SIN ERROR");
    }

    @Override
    public String getError() {
        return ErrorDescription;
    }

    @Override
    public boolean isSessionExpired() {
        return "NO EXISTE SESION".equalsIgnoreCase(getError());
    }
}
