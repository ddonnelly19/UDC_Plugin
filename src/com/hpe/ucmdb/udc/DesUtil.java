package com.hpe.ucmdb.udc;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

class DesUtil {
    // --Commented out by Inspection (3/1/2017 3:04 PM):private static final String DES = "DES";
    // --Commented out by Inspection (3/1/2017 3:04 PM):private static final String KEY = "23lj42l3rn,anljfalf&*AS&*fs7d8f9a797fd89!@#$%";

    static String encrypt(final String data) throws Exception {
        final byte[] bt = encrypt(data.getBytes(), "23lj42l3rn,anljfalf&*AS&*fs7d8f9a797fd89!@#$%".getBytes());
        return new BASE64Encoder().encode(bt);
    }

    static String decrypt(final String data) throws Exception {
        if (data == null) {
            return null;
        }
        final BASE64Decoder decoder = new BASE64Decoder();
        final byte[] buf = decoder.decodeBuffer(data);
        final byte[] bt = decrypt(buf, "23lj42l3rn,anljfalf&*AS&*fs7d8f9a797fd89!@#$%".getBytes());
        return new String(bt);
    }


    private static byte[] encrypt(final byte[] data, final byte[] key) throws Exception {
        final SecureRandom sr = new SecureRandom();
        final DESKeySpec dks = new DESKeySpec(key);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, securekey, sr);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(final byte[] data, final byte[] key) throws Exception {
        final SecureRandom sr = new SecureRandom();
        final DESKeySpec dks = new DESKeySpec(key);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, sr);
        return cipher.doFinal(data);
    }
}