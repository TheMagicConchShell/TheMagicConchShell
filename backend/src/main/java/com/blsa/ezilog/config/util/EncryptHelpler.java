package com.blsa.ezilog.config.util;

public interface EncryptHelpler {
        String encrypt(String password);
        
        boolean isMatch(String password, String hashed);
}
