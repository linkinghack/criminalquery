package com.linkinghack.criminalquerybase.controller;

import com.linkinghack.criminalquerybase.service.DistrictService;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/districts")
public class DistrictController {
    private final DistrictService districtService;
    @Autowired
    public DistrictController(DistrictService service) {
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
}
