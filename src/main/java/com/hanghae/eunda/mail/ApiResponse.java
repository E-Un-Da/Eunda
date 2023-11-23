package com.hanghae.eunda.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "Success");
    }

    public static ApiResponse<com.hanghae.eunda.mail.EmailCertificationResponse> success(EmailCertificationResponse data) {
        return new ApiResponse<>(200, "Success", data);
    }

    public static ApiResponse<Void> error(String failedToSendInvitationMail) {
        return new ApiResponse<>(400, "error", null);
    }
}
