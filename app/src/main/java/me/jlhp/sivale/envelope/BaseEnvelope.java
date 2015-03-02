package me.jlhp.sivale.envelope;

import com.alexgilleran.icesoap.envelope.impl.BaseSOAP11Envelope;
import com.alexgilleran.icesoap.xml.XMLParentNode;
import com.alexgilleran.icesoap.xml.XMLTextNode;
import com.alexgilleran.icesoap.xml.impl.XMLTextNodeImpl;

/**
 * Created by jjherrer on 26/02/2015.
 */
public class BaseEnvelope extends BaseSOAP11Envelope {
    /**
     * The namespace of the AonAware services
     */
    private final static String NAMESPACE = "WebMethods";

    private BaseEnvelope() {
        super();
        declarePrefix("web", getSiValeNamespace());
    }

    protected static String getSiValeNamespace() {
        return NAMESPACE;
    }

    public static class Builder {

        private BaseEnvelope BaseEnvelope = null;
        private XMLParentNode ParentNode = null;
        private String SoapOperation = null;

        public Builder() {

        }

        public Builder setSoapOperation(String soapOperation){
            if(soapOperation == null) {
                throw new IllegalArgumentException("'SoapOperation' must not be null");
            }

            SoapOperation = soapOperation;
            BaseEnvelope = new BaseEnvelope();

            ParentNode = BaseEnvelope.getBody().addNode(NS_PREFIX_XSI, soapOperation);
            ParentNode.addAttribute("soapenv", "encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");

            return this;
        }

        public Builder addParameter(EnvelopeParameter param){
            if(SoapOperation == null){
                throw new IllegalStateException("Call 'setSoapOperation' method first");
            }

            if(param == null) {
                throw new IllegalArgumentException("'param' must not be null");
            }

            XMLTextNode node = new XMLTextNodeImpl(getSiValeNamespace(), param.getKey(), param.getValue());
            node.addAttribute(null, "xsi:type", param.getType());

            ParentNode.addElement(node);

            return this;
        }

        public BaseEnvelope build() {
            SoapOperation = null;
            ParentNode = null;

            return BaseEnvelope;
        }
    }
}
