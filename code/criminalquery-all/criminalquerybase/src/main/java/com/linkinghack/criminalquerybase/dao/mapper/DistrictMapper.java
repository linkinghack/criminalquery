package com.linkinghack.criminalquerybase.dao.mapper;

import com.linkinghack.criminalquerymodel.data_model.District;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DistrictMapper {
    District selectDistrictById(Integer id);
    List<District> selectDistrictsByLevel(Integer level);
    List<District> findSubDistricts(Integer supervisorID);

}
