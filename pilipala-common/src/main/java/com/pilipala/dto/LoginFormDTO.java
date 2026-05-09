package com.pilipala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginFormDTO {
    private String email;
    private String password;
    private String captchaKey;
    private String captcha;
}
