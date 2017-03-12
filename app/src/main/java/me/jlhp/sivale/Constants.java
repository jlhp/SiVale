package me.jlhp.sivale;

/**
 * Created by JOSELUIS on 10/9/2016.
 */

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.Window;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public final class Constants
{
    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static final int FAILURE_RESULT = 1;
    public static final String LOCATION_DATA_EXTRA = "com.google.android.gms.location.sample.locationaddress.LOCATION_DATA_EXTRA";
    public static final String NameSpace = "http://mx.com.sivale.ws/exposition/servicios/appsivale";
    public static final String OS = "Android";
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = "com.google.android.gms.location.sample.locationaddress.RECEIVER";
    public static final String RESULT_DATA_KEY = "com.google.android.gms.location.sample.locationaddress.RESULT_DATA_KEY";
    public static final int SUCCESS_RESULT = 0;
    static EncryptionUtil encrypter = new EncryptionUtil();
    public static final String endPoint = "http://200.53.144.123:8888/sivale/Integracion/TPRE3I/Servicios/AppSiVale";
    public static final String keyRSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgrN/zVxTJ3LQYO4wAkO+RoMhP+u1PS3xzv7Jc1hsTRtADnLI8xDJ2rZa4QTeunccmac5fGxn04jZsjYSeHrWZ/0RI23bpD9gaNICidLAiAWvmwUE2layWEZ/DISq20R4m9UJDK6wFwABkV8aa9rYdf+aBQ3578jGTx0lqiH0xUwIDAQAB";
    public static final String passSV = "4ppS1V4l3_2016";
    public static final String passSVcms = "S1v4l3_2016";
    public static final String registrarUsuario = "registrarUsuarioRequest";
    public static final String solicitanteSV = "ws_appsivale";
    public static final String solicitanteSVcms = "ws_cms_app";
    public static final String usuarioSV = "app_sivale";
    public static final String usuarioSVcms = "cms_app";

    public static void colorBar(Activity paramActivity)
    {
        Window localWindow = paramActivity.getWindow();
        localWindow.addFlags(-2147483648);
        localWindow.clearFlags(67108864);

    }

    public static String encryptPass(String param)
    {
        try
        {
            param = encrypter.encryptData(param);
            return param;
        }
        catch (NoSuchAlgorithmException paramString)
        {
            paramString.printStackTrace();
            return null;
        }
        catch (NoSuchPaddingException paramString)
        {
            paramString.printStackTrace();
            return null;
        }
        catch (InvalidKeyException paramString)
        {
            paramString.printStackTrace();
            return null;
        }
        catch (IllegalBlockSizeException paramString)
        {
            paramString.printStackTrace();
            return null;
        }
        catch (BadPaddingException paramString)
        {
            paramString.printStackTrace();
        }
        return null;
    }

    public static String getNumberMonth(String paramString)
    {
        return "12";
    }

    public static String getNumberState(String paramString)
    {
        return "09";
    }

    public static String getStringState(String paramString)
    {
        return "CIUDAD DE MÉXICO";
    }
}
