package com.linkinghack.criminalquerybase.controller;

import com.linkinghack.criminalquerybase.service.CriminalService;
import com.linkinghack.criminalquerymodel.data_model.Clue;
import com.linkinghack.criminalquerymodel.data_model.Criminal;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.SearchCriminalRequest;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * 非直接面对用户：查询某个特定id的逃犯详细信息，用于系统展示详细信息
     * @param criminalID 逃犯id
     * @return
     */
    @GetMapping("/detailByID/{id}")
    public UniversalResponse criminalDetailById(@PathVariable("id")Integer criminalID){
        String fn = "<GET>[/criminal/basicInfoByID/{ID}]";
        logger.info("@Request{} criminalID:{}", fn, criminalID);
        UniversalResponse response = criminalService.getCriminalDetail(criminalID);
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }

    /**
     * 精确查找：查询某个身份证号对应的逃犯信息
     * @param idCardID 身份证号
     * @return
     */
    @GetMapping("/basicInfoByIDCard/{idCardID}")
    public UniversalResponse criminalInfoByIdCard(@PathVariable("idCardID")String idCardID) {
        return  UniversalResponse.Ok("");
    }

    /**
     * 精确查找：电话号码
     * @param phone 电话号
     * @return
     */
    @GetMapping("/basicInfoByPhone/{phone}")
    public UniversalResponse criminalInfoByPhone(@PathVariable("phone")String phone){
        return UniversalResponse.Ok("ok");
    }

    /**
     * 模糊查找：按逃犯查询的多条件高级检索方法
     * @param searchCriminalRequest
     * @param request
     * @param request
     * @return
     */
    @GetMapping("/criminals")
    public UniversalResponse criminalInfo(@RequestBody SearchCriminalRequest searchCriminalRequest,
                                          HttpServletRequest request) {
        // TODO
        return UniversalResponse.Ok("ok");
    }

    /**
     * 创建新的逃犯基本信息，若新建通缉令时库中无对应逃犯信息应该先调用此接口
     * @param criminal 逃犯基本信息
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
     * @param request HttpServletRequest 用于获取当前用户，来自Interceptor
     * @return 生成的逃犯信息，其中id已返回库中i_id
     */
    @PostMapping("/basicInfo")
    @CrossOrigin(origins = "https://tyut.life")
    public UniversalResponse createCriminal(@RequestBody Criminal criminal, HttpServletRequest request) {
        String fn = "<POST>[/criminal/basicInfo]";
        User user = (User) request.getAttribute("user");

        logger.info("@Request{} user:{} | criminalInfo: {}", fn, user, criminal);
        criminal.setCreatedBy(user.getId());
        criminal.setUpdatedBy(user.getId());
        if (criminal.getName() == null || criminal.getSex() == null){
            logger.info("必要参数:姓名,性别未满足");
            return UniversalResponse.UserFail("必要参数:姓名,性别未满足");
        }
        UniversalResponse response = criminalService.createCriminal(criminal);
        logger.info("@Response{} response: {}", fn, response);
        return response;
    }

    /**
     * 更新逃犯基本信息，选择性更新，仅criminalID 为必要参数
     * @param criminal 待更新逃犯信息
     * @param request 用于获取当前用户
     * @return 更新结果
     */
    @PutMapping("/basicInfo")
    public UniversalResponse updateCriminal(@RequestBody Criminal criminal, HttpServletRequest request) {
        String fn = "<PUT>[/criminal/basicInfo]";
        UniversalResponse response ;
        User user = (User) request.getAttribute("user");
        logger.info("@Request{}  criminal:{}, User:{}", fn, criminal, user);
        if (criminal.getId() == null){
            response = UniversalResponse.UserFail("逃犯ID为必要参数");
        }

        response = criminalService.updateCriminal(criminal, user);

        logger.info("@Response{} response:{}", fn, response);
        return response;
    }

    /**
     * 添加逃犯线索
     * @param clue 线索对象, 必要参数：criminalID
     * @param request 用于获取当前用户
     * @return 添加结果
     */
    @PostMapping("/clue")
    public UniversalResponse addClue(Clue clue, HttpServletRequest request){
        String fn = "<POST>[/criminal/clue]";
        User user = (User) request.getAttribute("user");
        UniversalResponse response;
        logger.info("@Request{} clue:{}, User:{}", fn, clue, user);
        if (clue.getCriminalID() == null){
            response = UniversalResponse.UserFail("criminalID是必要参数");
        }

        response = criminalService.addClue(clue);
        return response;
    }

}
