package me.jlhp.sivale.envelope;

/**
 * Created by JOSELUIS on 3/1/2015.
 */
public class EnvelopeParameter {
    private String Key;
    private String Value;
    private String Type;

    public EnvelopeParameter(String key, String value, String type) {
        this.Key = key;
        this.Value = value;
        this.Type = type;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
