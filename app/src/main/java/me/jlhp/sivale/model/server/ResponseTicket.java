package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by JOSELUIS on 2/5/2017.
 */
@XMLObject("//consultarMovimientosResponse/responseTicket")
public class ResponseTicket {

    public ResponseTicket() {

    }

    @XMLField("ticket")
    private String mTicketCode;

    @XMLField("inicio")
    private String mRequestStart;

    @XMLField("termino")
    private String mRequestEnd;
}
