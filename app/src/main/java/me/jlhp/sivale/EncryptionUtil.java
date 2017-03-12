package me.jlhp.sivale;

/**
 * Created by JOSELUIS on 10/9/2016.
 */

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    private static String ENCRYPTION_ALGORITHM;
    Cipher cipher;
    byte[] encryptedBytes;
    public String keyRSA;
    PublicKey publicKey;

    public EncryptionUtil() {
        this.keyRSA = Constants.keyRSA;
    }

    static {
        ENCRYPTION_ALGORITHM = "RSA";
    }

    public String encryptData(String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            this.publicKey = KeyFactory.getInstance(ENCRYPTION_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(this.keyRSA.getBytes(), 2)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        this.cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        this.cipher.init(1, this.publicKey);
        try {
            this.encryptedBytes = this.cipher.doFinal(plain.getBytes());
            return new String(Base64.encode(this.encryptedBytes, 2), "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return "ERROR";
        }
    }

    public static byte[] encrypt(Key publicKey, byte[] toBeCiphred) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SC");
            rsaCipher.init(1, publicKey);
            return rsaCipher.doFinal(toBeCiphred);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptToBase64(String keyString, String toBeCiphred) {
        byte[] encodedKey = Base64.decode(keyString, 0);
        return Base64.encodeToString(encrypt(new SecretKeySpec(encodedKey, 0, encodedKey.length, "RSA"), toBeCiphred.getBytes()), 0);
    }
}