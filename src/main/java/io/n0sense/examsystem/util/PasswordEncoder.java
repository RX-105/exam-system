package io.n0sense.examsystem.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
  MessageDigest不是线程安全的，应当考虑每一个线程使用一个单独的实例。
 */
public class PasswordEncoder {
    private static String encrypt(String x, String algorithm) {
        final String uuid = "7d583a8ad2f0403ebd7b6d6e01cc56e6";
        byte[] encryptedBytes = null;
        x = uuid + x;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            encryptedBytes = messageDigest.digest(x.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Incorrect algorithm.");
        }
        StringBuilder buffer = new StringBuilder();
        if (encryptedBytes != null) {
            for (byte b : encryptedBytes) {
                String hex = Integer.toString(b & 0xFF, 16);
                while (hex.length() < 2) {
                    hex = "0" + hex;
                }
                buffer.append(hex);
            }
        }
        return buffer.toString();
    }

    public static String MD5Encode(String x){
        return encrypt(x, "MD5");
    }

    public static String SHA256Encode(String x){
        return encrypt(x, "SHA-256");
    }
}
