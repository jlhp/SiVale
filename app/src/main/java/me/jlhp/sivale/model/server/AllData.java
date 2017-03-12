package me.jlhp.sivale.model.server;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

import java.util.List;

/**
 * Created by JOSELUIS on 10/15/2016.
 */
@XMLObject("//consultarMovimientosResponse")
public class AllData implements SiValeData {

    @XMLField("responseError")
    ResponseError mResponseError;

    @XMLField("responseTicket")
    ResponseTicket mResponseTicket;

    @XMLField("saldo")
    double mBalance;

    @XMLField("transacciones/transaccion")
    List<Transaction> mTransactions;

    public AllData() {

    }

    @Override
    public boolean isInvalidLogin() {
        return false;
    }

    @Override
    public boolean isSessionExpired() {
        return false;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String getError() {
        return null;
    }

}
