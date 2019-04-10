package com.linkinghack.criminalquery.model;

import lombok.*;

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
