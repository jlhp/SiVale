package me.jlhp.sivale;

/**
 * Created by JOSELUIS on 10/9/2016.
 */

import java.util.List;

public class SiValeSingleton
{
    private static SiValeSingleton ourInstance = new SiValeSingleton();
    private String CantidadSaldo;
    private int CardPosition = -1;
    private List<String> gasolinera;
    private String keyServices;
    private String keyServicesCards;
    private List<List<String>> top5Result;

    public static SiValeSingleton getInstance()
    {
        return ourInstance;
    }

    public static SiValeSingleton getOurInstance()
    {
        return ourInstance;
    }

    public static void setOurInstance(SiValeSingleton paramSiValeSingleton)
    {
        ourInstance = paramSiValeSingleton;
    }

    public String getCantidadSaldo()
    {
        return this.CantidadSaldo;
    }

    public int getCardPosition()
    {
        return this.CardPosition;
    }

    public List<String> getGasolinera()
    {
        return this.gasolinera;
    }

    public String getKeyServices()
    {
        return this.keyServices;
    }

    public String getKeyServicesCards()
    {
        return this.keyServicesCards;
    }

    public List<List<String>> getTop5Result()
    {
        return this.top5Result;
    }

    public void setCantidadSaldo(String paramString)
    {
        this.CantidadSaldo = paramString;
    }

    public void setCardPosition(int paramInt)
    {
        this.CardPosition = paramInt;
    }

    public void setGasolinera(List<String> paramList)
    {
        this.gasolinera = paramList;
    }

    public void setKeyServices(String paramString)
    {
        this.keyServices = paramString;
    }

    public void setKeyServicesCards(String paramString)
    {
        this.keyServicesCards = paramString;
    }

    public void setTop5Result(List<List<String>> paramList)
    {
        this.top5Result = paramList;
    }
}

