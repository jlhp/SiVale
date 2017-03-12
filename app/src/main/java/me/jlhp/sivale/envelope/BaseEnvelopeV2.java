package me.jlhp.sivale.envelope;

import com.alexgilleran.icesoap.envelope.impl.BaseSOAP11Envelope;
import com.alexgilleran.icesoap.xml.XMLParentNode;
import com.alexgilleran.icesoap.xml.XMLTextNode;
import com.alexgilleran.icesoap.xml.impl.XMLTextNodeImpl;

/**
 * Created by jjherrer on 26/02/2015.
 */
public class BaseEnvelopeV2 extends BaseSOAP11Envelope implements BaseEnv {

    private final static String NAMESPACE = "AppSiVale";

    private BaseEnvelopeV2() {
        super();
        declarePrefix("n0", getSiValeNamespace());
    }

    protected static String getSiValeNamespace() {
        return NAMESPACE;
    }

    public static class Builder {

        private BaseEnvelopeV2 BaseEnvelope = null;
        private XMLParentNode HeaderNode = null;
        private XMLParentNode BodyNode = null;
        private String SoapOperation = null;

        public Builder() {

        }

        public Builder setSoapOperation(String soapOperation) {
            if (soapOperation == null) {
                throw new IllegalArgumentException("'SoapOperation' must not be null");
            }

            SoapOperation = soapOperation;
            BaseEnvelope = new BaseEnvelopeV2();

            HeaderNode = BaseEnvelope.getHeader().addNode("http://mx.com.sivale.ws/exposition/servicios/appsivale", "identificacion");
            XMLTextNode user = new XMLTextNodeImpl(null, "usuario", "app_sivale");
            XMLTextNode password = new XMLTextNodeImpl(null, "password", "kc6A+lTX1U2/Ga5id2kN5Lm7GDM7eijT0EZf7NztIc+e7i7uI58fBAxhaz7dAGIfPByrNGg3kzZJQRWQDvV17hqEvRSywdNK8FxJhPz3/SVD631IXWaheoYjW2M2jgWhamV9ZSZL18nwIf8pNsb4lV8WmILr+Ru9jBZWtUZ8Oxs=");
            XMLTextNode solicitant = new XMLTextNodeImpl(null, "solicitante", "ws_appsivale");

            HeaderNode.addElement(user);
            HeaderNode.addElement(password);
            HeaderNode.addElement(solicitant);

            BodyNode = BaseEnvelope.getBody().addNode("http://mx.com.sivale.ws/exposition/servicios/appsivale", soapOperation);
            BodyNode.addAttribute("soapenv", "encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");

            return this;
        }

        public Builder addParameter(EnvelopeParameter param) {
            if (SoapOperation == null) {
                throw new IllegalStateException("Call 'setSoapOperation' method first");
            }

            if (param == null) {
                throw new IllegalArgumentException("'param' must not be null");
            }

            XMLTextNode node = new XMLTextNodeImpl(null, param.getKey(), param.getValue());
            node.addAttribute(null, "xsi:type", param.getType());

            BodyNode.addElement(node);

            return this;
        }

        public BaseEnvelopeV2 create() {
            SoapOperation = null;
            BodyNode = null;

            return BaseEnvelope == null ? new BaseEnvelopeV2() : BaseEnvelope;
        }
    }
}
