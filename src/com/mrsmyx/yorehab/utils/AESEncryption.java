package com.mrsmyx.yorehab.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cj on 1/31/16.
 */
public class AESEncryption {
    public static String decrypt(String encData, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        System.out.println("Base64 decoded: "
                + Base64.decode(encData).length);
        byte[] original = cipher
                .doFinal(Base64.decode(encData));
        return new String(original).trim();
    }

    public static String encrypt(String data, String key)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        System.out.println("Base64 encoded: "
                + Base64.encode(data.getBytes()).length());
        byte[] original = Base64.encode(cipher.doFinal(data.getBytes())).getBytes();
        return new String(original);
    }

}
