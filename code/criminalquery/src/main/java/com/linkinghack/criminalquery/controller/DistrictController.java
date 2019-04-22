package com.linkinghack.criminalquery.controller;

import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.dao.mongo.DepartmentMongoOp;
import com.linkinghack.criminalquery.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/districts")
public class DistrictController {
    private final DistrictService districtService;
    private final DepartmentMongoOp mongoOp;
    @Autowired
    public DistrictController(DistrictService service, DepartmentMongoOp mongoOp) {
        this.mongoOp = mongoOp;
        this.districtService = service;
    }

    @GetMapping("/{id}")
    public UniversalResponse district(@PathVariable("id") Integer id) {
        return UniversalResponse.Ok(districtService.getDistrict(id));
    }

    @GetMapping("/byLevel/{level}")
    public UniversalResponse districtsByLevel(@PathVariable("level") Integer level) {
        return districtService.districtsByLevel(level);
    }

    @GetMapping("/subDistricts/{id}")
    public UniversalResponse subDistricts(@PathVariable("id") Integer id) {
        return districtService.getSubDistricts(id);
    }

    @GetMapping("/aggregated")
    public UniversalResponse aggregatedDistricts() {
        return UniversalResponse.Ok(mongoOp.aggregateDistricts());
    }
}
