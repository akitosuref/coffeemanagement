package com.group13.coffeemanagement.crypto;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCryptography {

    private static final String ALGORITHM = "AES";
    private static final String secret = "quanlycoffeekey1";

    // Hàm mã hóa mật khẩu
    public static String encryptPassword(String password) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromString(secret));
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Hàm giải mã mật khẩu
    public static String decryptPassword(String encryptedPassword) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,getKeyFromString(secret));
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static SecretKey getKeyFromString(String secretKeyStr) {
        // Ensure the key is exactly 16 bytes (128-bit AES)
        byte[] keyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytesPadded = new byte[16]; // 128-bit
        System.arraycopy(keyBytes, 0, keyBytesPadded, 0, Math.min(keyBytes.length, 16));
        return new SecretKeySpec(keyBytesPadded, ALGORITHM);
    }

    // Convert a SecretKey to Base64 string for storage
    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
