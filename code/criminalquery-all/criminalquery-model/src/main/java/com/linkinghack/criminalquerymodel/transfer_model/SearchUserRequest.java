package com.linkinghack.criminalquerymodel.transfer_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest {
    private String userID;
    private String realName;
    private String nameOrID;

    private Integer pageSize;
    private Integer page;
    private Integer offset;
}
