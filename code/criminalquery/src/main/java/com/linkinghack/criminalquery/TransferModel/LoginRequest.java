package com.linkinghack.criminalquery.TransferModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String userID;
    private String password;
    private Boolean remember;
}
