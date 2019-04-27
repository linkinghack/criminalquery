package com.linkinghack.criminalquerymodel.data_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WantedOrder {
    private Integer id;
    private Integer criminalID;
    private String criminalName;

    private String arrestReason;
    private Integer arrestStatus;
    private Integer arrestLevel;

    private Integer districtID; // 通缉区域id
    private District district;

    private Integer createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
