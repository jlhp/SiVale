package me.jlhp.sivale;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by jjherrer on 26/02/2015.
 */
@XMLObject("//login")
public class Login {

    @XMLField("huella_aleatoria")
    private String RandomString;

    @XMLField("num_tarjeta")
    private String CardNumber;

    @XMLField("passwd")
    private String Password;

    @XMLField("securityLevel")
    private String SecurityLevel;

    public String getRandomString() {
        return RandomString;
    }

    public void setRandomString(String randomString) {
        RandomString = randomString;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSecurityLevel() {
        return SecurityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        SecurityLevel = securityLevel;
    }
}
