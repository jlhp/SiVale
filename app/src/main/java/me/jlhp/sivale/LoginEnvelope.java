package me.jlhp.sivale;

import com.alexgilleran.icesoap.xml.XMLParentNode;
import com.alexgilleran.icesoap.xml.XMLTextNode;
import com.alexgilleran.icesoap.xml.impl.XMLTextNodeImpl;

/**
 * Created by jjherrer on 26/02/2015.
 */
public class LoginEnvelope extends BaseEnvelope {

    public LoginEnvelope(String randomString, String cardNumber,
                         String password, String securityLevel) {
        XMLParentNode loginNode = getBody().addNode(NS_PREFIX_XSI, "login");
        loginNode.addAttribute("soapenv", "encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");

        XMLTextNode randomStringNode = new XMLTextNodeImpl(getSiValeNamespace(), "huella_aleatoria", randomString);
        randomStringNode.addAttribute(null, "xsi:type", "xsd:string");

        XMLTextNode cardNumberNode = new XMLTextNodeImpl(getSiValeNamespace(), "num_tarjeta", cardNumber);
        cardNumberNode.addAttribute(null, "xsi:type", "xsd:string");

        XMLTextNode passwordNode = new XMLTextNodeImpl(getSiValeNamespace(), "passwd", password);
        passwordNode.addAttribute(null, "xsi:type", "xsd:string");

        XMLTextNode securityLevelNode = new XMLTextNodeImpl(getSiValeNamespace(), "securityLevel", securityLevel);
        securityLevelNode.addAttribute(null, "xsi:type", "xsd:string");

        loginNode.addElement(randomStringNode);
        loginNode.addElement(cardNumberNode);
        loginNode.addElement(passwordNode);
        loginNode.addElement(securityLevelNode);
    }
}