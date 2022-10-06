package org.example.dto;

import lombok.Data;

/** Запрос на подтверждение заявки */
@Data
public class ConfirmRequestDto {
    /** Номер телефона */
    private String tel;
    /** otp-пароль */
    private String otp;
}
