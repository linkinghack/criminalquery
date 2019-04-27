package com.linkinghack.criminalquerysso.model;

import com.linkinghack.criminalquerymodel.data_model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于redis记录的用户状态
 */
@Data
@AllArgsConstructor
public class UserStatus {
    private User user;
    private Boolean remember;
}
