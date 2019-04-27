package com.linkinghack.criminalquerymodel.data_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {
    private Integer id;
    private Integer supervisorId;
    private Integer[] path;
    private Integer level;
    private String name;
    private String province;
    private String city;
    private String county;
}
