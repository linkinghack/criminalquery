package com.linkinghack.criminalquery.controller;

import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.service.CriminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/wanted")
public class WantedOrderController {
    private final CriminalService criminalService;

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

    @PostMapping("/order")
    public UniversalResponse createWantedOrder(@RequestParam("reason")String reason,
                                               @RequestParam("level") Integer level,
                                               @RequestParam(value = "status", required = false) Integer status,
                                               @RequestParam("districtID") Integer districtID,
                                               @RequestParam("criminalID") Integer criminalID,
                                               HttpSession session){

        return criminalService.createWantedOrder(criminalID, reason, level, status, districtID);
    }
}
