package me.jlhp.sivale;

import com.alexgilleran.icesoap.envelope.impl.BaseSOAP11Envelope;

/**
 * Created by jjherrer on 26/02/2015.
 */
public abstract class BaseEnvelope extends BaseSOAP11Envelope {
    /**
     * The namespace of the AonAware services
     */
    private final static String NAMESPACE = "http://com.sivale.bancamovil.proto/IWebMethods.xsd";

    public BaseEnvelope() {
        declarePrefix("web", NAMESPACE);
    }

    protected String getSiValeNamespace() {
        return NAMESPACE;
    }
}
