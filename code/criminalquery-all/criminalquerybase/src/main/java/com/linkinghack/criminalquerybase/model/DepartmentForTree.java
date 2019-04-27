package com.linkinghack.criminalquerybase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentForTree {
    private Integer id;
    @JsonProperty("pId")
    private Integer pId;
    private String label;
    private String value;

    public void setValue(Integer value) {
        this.value = value.toString();
    }
}
