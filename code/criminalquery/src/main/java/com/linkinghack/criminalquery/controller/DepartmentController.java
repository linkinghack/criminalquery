package com.linkinghack.criminalquery.controller;
import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.model.Department;
import com.linkinghack.criminalquery.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/departments", method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class DepartmentController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService service) {
        this.departmentService = service;
    }

    @GetMapping("/sub/{id}")
    public UniversalResponse subDepartments(@PathVariable("id") Integer rootID) {
        return departmentService.getSubDepartments(rootID);
    }

    @GetMapping("/ofDistrict/{ID}")
    public UniversalResponse departmentsOfDistrict(@PathVariable("ID") Integer ID) {
        return departmentService.getDepartmentsOfDistrict(ID);
    }

    @GetMapping("/detailByID/{id}")
    public UniversalResponse departmentDetailByID(@PathVariable("id") Integer ID) {
        return departmentService.getDepartmentDetailByID(ID);
    }

    @GetMapping(value = {"/all", "/ofDistrict/"})
    public UniversalResponse allDepartments(){
        return departmentService.allDepartments();
    }

    @GetMapping("/membersCount/{ID}")
    public UniversalResponse membersCountOfDepartment(@PathVariable("ID") Integer ID) {
        return departmentService.getMembersCountOfDepartment(ID);
    }

    /**
     * 按部门级别查找部门
     *
     * @param level 部门等级，0-国家部级,1-省厅级,2-市局级,3-县区级
     * @return {
     * "status": 200,
     * "data": [
     * {
     * "id": 1,
     * "departmentName": "公安部",
     * "supervisorID": 0,
     * "level": 1,
     * "districtID": 100000
     * }
     * ],
     * "msg": "成功"
     * }
     */
    @GetMapping("/byLevel/{level}")
    public UniversalResponse departmentsByLevel(@PathVariable("level") Integer level) {
        return departmentService.getDepartmentsByLevel(level);
    }

    /**
     * 新建部门
     *
     * @param department {supervisorID: 上级部门ID, departmentName: 部门名称, level: 部门级别:1-国家级,2-省厅级,3-市局级,4-县区级, districtID: 管辖区域ID}
     * @return
     */
    @PostMapping("/department")
    public UniversalResponse addDepartment(@RequestBody Department department) {
        String fn = "<POST>[/safe/departments/department]";
        logger.info("@request {} requestBody: {} ", fn, department);
        try {
            if (department.getSupervisorID() == null || department.getDepartmentName() == null || department.getDistrictID() == null) {
                throw new Exception("参数不足");
            }
            return departmentService.addDepartment(department);
        } catch (Exception e) {
            return UniversalResponse.UserFail(e.getMessage());
        }
    }

    @GetMapping(value = "/department/{ID}")
    public UniversalResponse deleteDepartment(@PathVariable("ID") Integer ID, HttpSession session) {
        return departmentService.deleteDepartment(ID, session);
    }

    @GetMapping("/tree/nodes/{level}")
    public UniversalResponse departmentsByLevelForTree(@PathVariable("level") Integer level) {
        return departmentService.getDepartmentsByLevelForTree(level);
    }

    @GetMapping("/tree/subNodes/{ID}")
    public UniversalResponse subDepartmentsForTree(@PathVariable("ID") Integer ID) {
        return departmentService.getSubDepartmentsForTree(ID);
    }

    @GetMapping("/tree/nodes")
    public UniversalResponse allDepartmentsForTree() {
        return departmentService.allDepartmentsForTree();
    }
}
