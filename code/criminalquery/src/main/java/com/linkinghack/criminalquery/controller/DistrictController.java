package com.linkinghack.criminalquery.controller;

import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService service) {
        this.districtService = service;
    }

    @GetMapping("/district/{id}")
    public UniversalResponse district(@PathVariable("id") Integer id){
        return UniversalResponse.Ok(districtService.getDistrict(id));
    }

}
