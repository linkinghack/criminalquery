package com.linkinghack.criminalquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userID;
    private String password;
    private String email;
    private String realName;
    private Integer role;
    private Integer departmentID;
    private Boolean activated;
    private String phone;

    private Department department;
}
