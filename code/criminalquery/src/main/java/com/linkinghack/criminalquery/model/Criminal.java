package com.linkinghack.criminalquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Criminal {
    private Integer id;
    private String name;
    private Integer sex;
    private Integer height;
    private LocalDate birthday;
    private String bornPlace;
    private String idCardID;
    private String otherFeatures;

    private String portraitFileID;
    private String eduBackground;
    private String job;
    private String workFor;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer createdBy;
    private Integer updatedBy;
}
