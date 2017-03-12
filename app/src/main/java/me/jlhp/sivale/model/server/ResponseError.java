package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

/**
 * Created by JOSELUIS on 2/5/2017.
 */

@XMLObject("//consultarMovimientosResponse/responseError")
public class ResponseError {

    public ResponseError() {

    }

    @XMLField("codigo")
    private int mErrorCode;
}
