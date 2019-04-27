package com.linkinghack.criminalquerybase.service;


import com.linkinghack.criminalquerybase.dao.mapper.DistrictMapper;
import com.linkinghack.criminalquerymodel.data_model.District;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DistrictMapper mapper;

    @Autowired
    public DistrictService(DistrictMapper mapper) {
        this.mapper = mapper;
    }

    public District getDistrict(Integer id){
        return mapper.selectDistrictById(id);
    }

    public UniversalResponse districtsByLevel(Integer level) {
        if (level > 3 || level < 0)
            return UniversalResponse.UserFail("参数错误");
        else
            try{
                List<District> districtList = mapper.selectDistrictsByLevel(level);
                return UniversalResponse.Ok(districtList);
            }catch (Exception e) {
                logger.error("@DistrictService.districtsByLevel: An error occurred. {}", e.getMessage());
                e.printStackTrace();
                return UniversalResponse.ServerFail(e.getMessage());
            }
    }

    public UniversalResponse getSubDistricts(Integer id) {
        try {
            List<District> districts = mapper.findSubDistricts(id);
            return UniversalResponse.Ok(districts);
        }catch (Exception e) {
            logger.error("@DistrictsService.getSubDistricts: An error occurred. {}", e.getMessage());
            e.printStackTrace();
            return UniversalResponse.ServerFail(e.getMessage());
        }
    }
}
