package com.linkinghack.criminalquery.controller;

import com.linkinghack.criminalquery.TransferModel.SearchCriminalRequest;
import com.linkinghack.criminalquery.model.Criminal;
import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.service.CriminalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/criminal")
public class CriminalController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CriminalService criminalService;

    @Autowired
    public CriminalController(CriminalService service){
        this.criminalService = service;
    }

    /**
     * 非直接面对用户：查询某个特定id的逃犯相信信息，用于系统展示详细信息
     * @param criminalID 逃犯id
     * @return
     */
    @GetMapping("/basicInfoByID/{id}")
    public UniversalResponse criminalInfoById(@PathVariable("id")Integer criminalID){
        return UniversalResponse.Ok("");
    }

    /**
     * 精确查找：查询某个身份证号对应的逃犯信息
     * @param idCardID
     * @return
     */
    @GetMapping("/basicInfoByIDCard/{idCardID}")
    public UniversalResponse criminalInfoByIdCard(@PathVariable("idCardID")String idCardID) {
        return  UniversalResponse.Ok("");
    }

    /**
     * 精确查找：电话号码
     * @param phone
     * @return
     */
    @GetMapping("/basicInfoByPhone/{phone}")
    public UniversalResponse criminalInfoByPhone(@PathVariable("phone")String phone){
        return UniversalResponse.Ok("ok");
    }

    /**
     * 模糊查找：按逃犯查询的多条件高级检索方法
     * @param name 姓名
     * @param sex 性别
     * @param heightLow 最小身高
     * @param heightHigh 最大身高
     * @param ageLow 最小年龄
     * @param ageHigh 最大年龄
     * @return
     */
    @GetMapping("/criminals")
    public UniversalResponse criminalInfo(@RequestBody SearchCriminalRequest searchCriminalRequest,
                                          HttpServletRequest request, HttpSession session) {
        return UniversalResponse.Ok("ok");
    }

    /**
     * 创建新的逃犯基本信息，若新建通缉令时库中无对应逃犯信息应该先调用此接口
     * @param criminal
     *   String name
     *   Integer sex
     *   Integer height
     *   String birthday
     *   String bornPlace
     *   String idCardID
     *   String otherFeatures
     *   String portraitFileID
     *   String eduBackground
     *   String job, workFor, phone, address
     * @param session 会话对象
     * @return 生成的逃犯信息，其中id已返回库中i_id
     */
    public @PostMapping("/basicInfo") UniversalResponse createCriminal(Criminal criminal, HttpSession session) {
        criminal.setCreatedBy(1); // TODO 从session中获取当前用户id
        criminal.setUpdatedBy(1); // TODO 同上
        if (criminal.getName() == null || criminal.getSex() == null){
            logger.info("必要参数:姓名,性别未满足");
            return UniversalResponse.UserFail("必要参数:姓名,性别未满足");
        }
        return criminalService.createCriminal(criminal);
    }

}
