package com.linkinghack.criminalquerybase.controller;

import com.linkinghack.criminalquerybase.service.CriminalService;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.data_model.WantedOrder;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/wanted")
public class WantedOrderController {
    private final CriminalService criminalService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WantedOrderController(CriminalService service) {
        this.criminalService = service;
    }

    /**
     * /wanted/orders 提供通缉令的多条件查询方式，默认按通缉令创建时间由近及远排列
     * @param districts 查找地区
     * @param reason 过滤统计原因
     * @param status 过滤通缉令状态
     * @param dateFrom 开始日期
     * @param dateTo 结束日期
     * @param session 会话对象
     * @return
     */
    @GetMapping("/orders")
    @CrossOrigin(origins = "http://localhost:8080")
    public UniversalResponse wantedOrdersOfDistricts(
            @RequestParam(value = "districts",required = false)List<Integer> districts,
            @RequestParam(value = "reason", required = false) String reason,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "page", required = false) Integer page,
            HttpSession session) {

        LocalDate startDate = LocalDate.parse(dateFrom);
        LocalDate endDate = LocalDate.parse(dateTo);

        return UniversalResponse.Ok(districts);
    }

    @GetMapping("/order/criminal/{id}")
    public UniversalResponse wantedOrderOfACriminal(@PathVariable("id") Integer criminalID, HttpSession session){
        return UniversalResponse.Ok("ok");
    }

    /**
     * 发起通缉
     * @param wantedOrder 新通缉令对象
     * @param request 用于获取User
     * @return 包含成功插入后的通缉令, id generated
     */
    @PostMapping("/order")
    public UniversalResponse createWantedOrder(@RequestBody WantedOrder wantedOrder,
                                               HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        String fn = "<POST>[/wanted/order]";
        logger.info("@Request{} request:{}", fn, wantedOrder);
        if(wantedOrder.getArrestReason() == null || wantedOrder.getArrestLevel()==null){
            logger.warn("发起通缉参数不足");
            return UniversalResponse.UserFail("参数不足");
        }
        UniversalResponse response = criminalService.createWantedOrder(wantedOrder.getCriminalID(), wantedOrder.getArrestReason(), wantedOrder.getArrestLevel(),
                wantedOrder.getArrestStatus(), wantedOrder.getDistrictID(), user.getId());
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }

    /**
     * 更新通缉令统一接口，支持仅更新部分字段,不更新部分传null即可
     * @param order 待更新的通缉令对象，唯一必要参数 通缉令ID
     * @return 更新结果
     */
    @PutMapping("/order")
    public UniversalResponse updateWantedOrder(@RequestBody WantedOrder order) {
        String fn = "<PUT>[/wanted/order]";
        logger.info("@Request{} wantedOrder:{}", fn ,order);
        if (order.getId() == null)
            return UniversalResponse.UserFail("参数不足");
        UniversalResponse response = criminalService.updateWantedOrder(order);
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }

    /**
     * 删除通缉令
     * @param orderID 通缉令id
     * @return 删除结果
     */
    @DeleteMapping("/order/{id}")
    public UniversalResponse deleteWantedOrder (@PathVariable("id") Integer orderID) {
        String fn = "<DELETE>[/wanted/order/{id}]";
        logger.info("@Request{} orderID:{}", fn, orderID);
        UniversalResponse response = criminalService.deleteWantedOrder(orderID);
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }
}
