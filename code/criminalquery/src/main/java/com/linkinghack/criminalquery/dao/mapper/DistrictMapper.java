package com.linkinghack.criminalquery.dao.mapper;

import com.linkinghack.criminalquery.model.District;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DistrictMapper {
    @Select("select * from b_districts where i_id = #{id}")
    @ResultMap("oneDistrict")
    District selectDistrictById(Integer id);
}
