package com.linkinghack.criminalquerybase.service;

import com.linkinghack.criminalquerybase.dao.mapper.DepartmentMapper;
import com.linkinghack.criminalquerybase.exception.AddDepartmentFailedException;
import com.linkinghack.criminalquerymodel.data_model.Department;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
public class DepartmentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentService(DepartmentMapper mapper) {
        this.departmentMapper = mapper;
    }

    public UniversalResponse allDepartments(){
        return UniversalResponse.Ok(departmentMapper.allDepartments());
    }

    public UniversalResponse getMembersCountOfDepartment(Integer ID) {
        return UniversalResponse.Ok(departmentMapper.membersCount(ID));
    }

    public UniversalResponse allDepartmentsForTree() {
        return UniversalResponse.Ok(departmentMapper.allDepartmentsForTree());
    }

    public UniversalResponse getSubDepartmentsForTree(Integer ID) {
        return UniversalResponse.Ok(
                departmentMapper.getSubDepartmentsForTree(ID)
        );
    }

    public UniversalResponse getDepartmentsByLevelForTree(Integer level) {
        List<Department> departments = departmentMapper.getDepartmentByLevelForTree(level);
        return UniversalResponse.Ok(departments);
    }

    public UniversalResponse getDepartmentsOfDistrict(Integer districtID) {
        List<Department> departments = departmentMapper.departmentsOfDistrict(districtID);
        return UniversalResponse.Ok(departments);
    }

    public UniversalResponse getSubDepartments(Integer rootID) {
        List<Department> subDepartments = departmentMapper.getSubDepartments(rootID);
        return UniversalResponse.Ok(subDepartments);
    }

    public UniversalResponse getDepartmentDetailByID(Integer ID) {
        Department department = departmentMapper.getDepartmentByID(ID);
        if (department == null)
            return UniversalResponse.UserFail("departmentID 不存在");
        else
            return UniversalResponse.Ok(department);
    }

    public UniversalResponse getDepartmentsByLevel(Integer level) {
        List<Department> departments = departmentMapper.getDepartmentsByLevel(level);
        return UniversalResponse.Ok(departments);
    }

    public UniversalResponse addDepartment(Department department) {
        try {
            Integer rowsAffected = departmentMapper.addDepartment(department);
            if (rowsAffected != 1) {
                logger.error("@Service[DepartmentService.addDepartment] 插入部门数据失败, 插入数据{}", department);
                throw new AddDepartmentFailedException("插入数据失败");
            }
            return UniversalResponse.Ok(department);
        } catch (Exception e) {
            logger.error("@Service[DepartmentService.addDepartment] Exception: {}", e.getMessage());
            return UniversalResponse.ServerFail(e.getMessage());
        }
    }

    public UniversalResponse deleteDepartment(Integer departmentID, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Department department = departmentMapper.getDepartmentByID(departmentID);
        if (user != null && user.getRole() == 1) {
            int rows = departmentMapper.deleteDepartment(departmentID);
            if (rows == 1){
                logger.warn("用户{}{} 成功删除部门数据: {}", user.getUserID(), user.getRealName(), department);
                return UniversalResponse.Ok("删除成功");
            }
            else {
                logger.warn("用户{}{} 尝试删除部门数据失败: {}", user.getUserID(), user.getRealName(), department);
                return UniversalResponse.UserFail("删除失败");
            }
        }else {
            return UniversalResponse.UserFail("没有权限");
        }
    }

}
