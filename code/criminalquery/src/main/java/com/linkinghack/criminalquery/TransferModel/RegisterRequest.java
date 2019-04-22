package com.linkinghack.criminalquery.TransferModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String userID;
    private String password;
    private String confirm;
    private Integer departmentID;
    private String phone;
    private String email;
    private String prefix;
    private String realName;
    private String captcha;
}
