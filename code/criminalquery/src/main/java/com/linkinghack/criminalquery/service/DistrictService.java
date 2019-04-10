package com.linkinghack.criminalquery.service;

import com.linkinghack.criminalquery.dao.mapper.DistrictMapper;
import com.linkinghack.criminalquery.model.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {
    private final DistrictMapper mapper;

    @Autowired
    public DistrictService(DistrictMapper mapper) {
        this.mapper = mapper;
    }

    public District getDistrict(Integer id){
        return mapper.selectDistrictById(id);
    }

}
