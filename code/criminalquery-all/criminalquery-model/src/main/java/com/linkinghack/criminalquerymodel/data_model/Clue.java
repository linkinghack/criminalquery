package com.linkinghack.criminalquerymodel.data_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clue {
    private Integer id;
    private Integer criminalID;

    private String fileIDs;
    private String description;

    private LocalDateTime createdAt;
}
