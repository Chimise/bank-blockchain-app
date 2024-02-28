package com.firstacademy.firstblock.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecretKeyUtils {
    public static SecretKey generateSecretKey(String secretKey) {
        byte[] decodedKey = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
        return key;
    }
}
