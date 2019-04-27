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
     * @param wantedOrder
     * @param request
     * @return
     */
    @PostMapping("/order")
    public UniversalResponse createWantedOrder(@RequestBody WantedOrder wantedOrder,
                                               HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        String fn = "<POST>[/wanted/order]";
        logger.info("@Request{} request:{}", fn, wantedOrder);
        UniversalResponse response = criminalService.createWantedOrder(wantedOrder.getCriminalID(), wantedOrder.getArrestReason(), wantedOrder.getArrestLevel(),
                wantedOrder.getArrestStatus(), wantedOrder.getDistrictID(), user.getId());
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }
}
