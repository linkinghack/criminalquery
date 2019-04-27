package com.linkinghack.criminalquerybase.service;

import com.linkinghack.criminalquerybase.dao.mapper.CriminalMapper;
import com.linkinghack.criminalquerymodel.data_model.Criminal;
import com.linkinghack.criminalquerymodel.data_model.WantedOrder;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriminalService {
    private CriminalMapper mapper;

    @Autowired
    public CriminalService(CriminalMapper mapper){
        this.mapper = mapper;
    }

    public UniversalResponse createCriminal(Criminal criminal){
        Integer updateLines = mapper.createCriminal(criminal);
        if (updateLines == 1)
            return UniversalResponse.Ok(criminal);
        else {
            return UniversalResponse.ServerFail("新建逃犯信息失败");
        }
    }

    public UniversalResponse createWantedOrder(Integer criminalID, String reason, Integer level, Integer status, Integer districtID, Integer createdBy) {
        WantedOrder order = new WantedOrder();
        order.setCriminalID(criminalID);
        order.setArrestReason(reason);
        order.setArrestLevel(level);
        if (status != null){
            order.setArrestStatus(status);
        }else{
            order.setArrestStatus(1);
        }
        order.setDistrictID(districtID);
        order.setCreatedBy(createdBy);
        Integer insertStatus =  mapper.createWantedOrder(order);
        if (insertStatus == 1)
            return UniversalResponse.Ok(order);
        else
            return UniversalResponse.ServerFail("插入失败");
    }
}
