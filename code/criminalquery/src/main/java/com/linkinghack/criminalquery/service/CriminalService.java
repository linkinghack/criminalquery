package com.linkinghack.criminalquery.service;

import com.linkinghack.criminalquery.dao.mapper.CriminalMapper;
import com.linkinghack.criminalquery.model.Criminal;
import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.model.WantedOrder;
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
            return UniversalResponse.ServerFail("更新失败");
        }
    }

    public UniversalResponse createWantedOrder(Integer criminalID, String reason, Integer level, Integer status, Integer districtID) {
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
        Integer insertStatus =  mapper.createWantedOrder(order);
        if (insertStatus == 1)
            return UniversalResponse.Ok(order);
        else
            return UniversalResponse.ServerFail("插入失败");
    }
}
