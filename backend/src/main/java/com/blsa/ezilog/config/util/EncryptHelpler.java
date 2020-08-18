package com.blsa.ezilog.config.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface EncryptHelpler {
    String encrypt(String password);
    
    boolean isMatch(String password, String hashed);
    
    default String sha256(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plain.getBytes());
            byte[] bytes = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ignored) {
            return "";
        }
    }
}
