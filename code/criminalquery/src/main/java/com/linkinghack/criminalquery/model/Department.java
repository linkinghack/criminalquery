package com.linkinghack.criminalquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("departments")
public class Department {
    private Integer id;
    private String departmentName;
    private Integer supervisorID;
    private Integer level;
    private Integer districtID;

    private String districtName;
    private Integer membersCount;
}
