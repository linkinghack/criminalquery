package com.linkinghack.criminalquery.dao.mapper;

import com.linkinghack.criminalquery.model.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentMapper {
    List<Department> getDepartmentsByLevel(Integer level);

    Department getDepartmentByID(Integer ID);

    List<Department> getSubDepartments(Integer rootID);

    List<Department> allDepartments();

    Integer addDepartment(Department department);

    List<Department> getDepartmentByLevelForTree (Integer level);

    List<Department> getSubDepartmentsForTree (Integer ID);

    List<Department> allDepartmentsForTree();

    Integer membersCount(Integer ID);

    List<Department> departmentsOfDistrict(Integer districtID);

    Integer deleteDepartment(Integer ID);
}
