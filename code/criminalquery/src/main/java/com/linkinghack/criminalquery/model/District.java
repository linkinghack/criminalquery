package com.linkinghack.criminalquery.model;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("districts") // for mongodb
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
