package com.hanghae.eunda.redis.service;

import com.hanghae.eunda.redis.dao.CertificationNumberDao;
import com.hanghae.eunda.redis.dto.ApiResponse;
import com.hanghae.eunda.redis.dto.EmailCertificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailVerifyService {

    private final CertificationNumberDao certificationNumberDao;

    public ApiResponse verifyEmail(String email, String certificationNumber) {
        if (!isVerify(email, certificationNumber)) {
            throw new IllegalArgumentException("InvalidCertificationNumberException : 잘못된 인증번호 예외");
        }
        certificationNumberDao.removeCertificationNumber(email);
        EmailCertificationResponseDto emailResponse = new EmailCertificationResponseDto(email, certificationNumber);
        return new ApiResponse("SUCCESS", emailResponse);
    }

    private boolean isVerify(String email, String certificationNumber) {
        boolean validatedEmail = isEmailExists(email);
        if (!isEmailExists(email)) {
            throw new IllegalArgumentException("EmailNotFoundException : 잘못된 이메일 예외");
        }
        return (validatedEmail &&
                certificationNumberDao.getCertificationNumber(email).equals(certificationNumber));
    }

    private boolean isEmailExists(String email) {
        return certificationNumberDao.hasKey(email);
    }
}