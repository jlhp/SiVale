package me.jlhp.sivale;

/**
 * Created by JOSELUIS on 10/9/2016.
 */

public class Uses
{
    private String category;
    private String direction;
    private String distance;
    private boolean onPromo;
    private String title;

    public Uses(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
    {
        this.title = paramString1;
        this.direction = paramString2;
        this.category = paramString3;
        this.distance = paramString4;
        this.onPromo = paramBoolean;
    }

    public static String getKeyServices()
    {
        String str2 = SiValeSingleton.getInstance().getKeyServices();
        String str1 = str2;
        if (str2 == null)
        {
            str1 = Constants.encryptPass("S1v4l3_2016");
            SiValeSingleton.getInstance().setKeyServices(str1);
        }
        return str1;
    }

    public static String getKeyServicesCards()
    {
        return Constants.encryptPass("4ppS1V4l3_2016");
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getDirection()
    {
        return this.direction;
    }

    public String getDistance()
    {
        return this.distance;
    }

    public String getTitle()
    {
        return this.title;
    }

    public boolean isOnPromo()
    {
        return this.onPromo;
    }
}