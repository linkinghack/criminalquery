package com.linkinghack.criminalquerymodel.data_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private Integer id;
    private String departmentName;
    private Integer supervisorID;
    private Integer level;
    private Integer districtID;

    private String districtName;
    private Integer membersCount;
}
