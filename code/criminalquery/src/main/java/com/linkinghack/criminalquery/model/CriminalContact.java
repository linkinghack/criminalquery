package com.linkinghack.criminalquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriminalContact {
    private Integer id;
    private Integer criminalID;
    private String name;
    private Integer sex;
    private LocalDate birthday;
    private String relation;
    private String phone;
    private String address;

}
