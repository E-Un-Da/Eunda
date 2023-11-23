package com.hanghae.eunda.redis;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class CertificationGenerator { // 난수 생성 역할
    public String createCertificationNumber() throws NoSuchAlgorithmException {
        String result;

        do {
            int num = SecureRandom.getInstanceStrong().nextInt(999999);
            result = String.valueOf(num);
        } while (result.length() != 6);

        return result;
    }
}