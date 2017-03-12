package me.jlhp.sivale.api;

import com.alexgilleran.icesoap.exception.XMLParsingException;
import com.alexgilleran.icesoap.parser.IceSoapParser;
import com.alexgilleran.icesoap.parser.impl.IceSoapParserImpl;

import java.io.ByteArrayInputStream;

/**
 * Created by jjherrer on 02/03/2015.
 */
public class SiValeParser {
    public <T> T parseSoapData(Class<T> clazz, byte[] soapData) {
        if (clazz == null) {
            throw new IllegalArgumentException("'clazz' must not be null");
        }

        if (soapData == null || soapData.length == 0) {
            throw new IllegalArgumentException("'soapData' must not be null nor empty");
        }

        IceSoapParser<T> parser = getSoapParser(clazz);
        ByteArrayInputStream stream = new ByteArrayInputStream(soapData);
        T parsedData = null;

        try {
            parsedData = parser.parse(stream);
        } catch (XMLParsingException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return parsedData;
    }

    private <T> IceSoapParser<T> getSoapParser(Class<T> clazz) {
        return new IceSoapParserImpl<>(clazz);
    }
}
