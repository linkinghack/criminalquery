package com.linkinghack.criminalquery.TransferModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriminalRequest {
    // 逃犯基本信息表相关条件
    private String name;
    private Integer sex;
    private Integer heightStart;
    private Integer heightEnd;
    private Integer ageStart;
    private Integer ageEnd;
    private String job;
    private String workFor;
    private String address;
    private String otherFeatures;

    // 通缉令表相关条件
    private String arrestReason; //通缉原因
    private Integer arrestLevel;
    private Integer arrestStatus;
    private Integer[] districtIDs; // 多范围搜索
    private LocalDateTime arrestCreateTimeStart;
    private LocalDateTime arrestCreateTimeEnd;
    private LocalDateTime arrestUpdateTimeStart;
    private LocalDateTime arrestUpdateTimeEnd;
}
