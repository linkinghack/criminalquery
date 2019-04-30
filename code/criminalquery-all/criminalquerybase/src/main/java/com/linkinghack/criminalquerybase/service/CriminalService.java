package com.linkinghack.criminalquerybase.service;

import com.linkinghack.criminalquerybase.Constants;
import com.linkinghack.criminalquerybase.dao.mapper.CriminalMapper;
import com.linkinghack.criminalquerybase.dao.mapper.DistrictMapper;
import com.linkinghack.criminalquerybase.dao.mapper.UserMapper;
import com.linkinghack.criminalquerymodel.data_model.Clue;
import com.linkinghack.criminalquerymodel.data_model.Criminal;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.data_model.WantedOrder;
import com.linkinghack.criminalquerymodel.transfer_model.CriminalDetailResponse;
import com.linkinghack.criminalquerymodel.transfer_model.SearchCriminalRequest;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class CriminalService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CriminalMapper mapper;
    private FileService fileService;
    private DistrictMapper districtMapper;
    private UserMapper userMapper;

    @Autowired
    public CriminalService(CriminalMapper mapper, FileService fileService, DistrictMapper districtMapper, UserMapper userMapper) {
        this.fileService = fileService;
        this.mapper = mapper;
        this.districtMapper = districtMapper;
        this.userMapper = userMapper;
    }

    public UniversalResponse createCriminal(Criminal criminal) {
        Integer updateLines = mapper.createCriminal(criminal);
        if (updateLines == 1)
            return UniversalResponse.Ok(criminal);
        else {
            return UniversalResponse.ServerFail("新建逃犯信息失败");
        }
    }

    public UniversalResponse updateCriminal(Criminal criminal, User user) {
        criminal.setUpdatedBy(user.getId());
        int rowsAffected = mapper.updateCriminal(criminal);
        return UniversalResponse.Ok("更新"+rowsAffected+"条记录");
    }

    public UniversalResponse createWantedOrder(Integer criminalID, String reason, Integer level, Integer status, Integer districtID, Integer createdBy) {
        WantedOrder order = new WantedOrder();
        order.setCriminalID(criminalID);
        order.setArrestReason(reason);
        order.setArrestLevel(level);
        if (status != null) {
            order.setArrestStatus(status);
        } else {
            order.setArrestStatus(1);
        }
        order.setDistrictID(districtID);
        order.setCreatedBy(createdBy);
        Integer insertStatus = mapper.createWantedOrder(order);
        if (insertStatus == 1)
            return UniversalResponse.Ok(order);
        else
            return UniversalResponse.ServerFail("插入失败");
    }

    public UniversalResponse addClue(Clue clue) {
        int rowsAffected = mapper.addClue(clue);
        if (rowsAffected != 1){
            return UniversalResponse.ServerFail("无法添加线索 - " + rowsAffected);
        }
        return UniversalResponse.Ok(clue);
    }

    public UniversalResponse updateWantedOrder(WantedOrder order) {
        int rowsAffected = mapper.updateWantedOrder(order);
        if (rowsAffected == 1) {
            logger.info("更新通缉令成功 {}", order);
            return UniversalResponse.Ok("更新成功");
        } else {
            logger.info("更新通缉令失败, rowsAffected={}", rowsAffected);
            return UniversalResponse.ServerFail("更新失败");
        }
    }

    public UniversalResponse deleteWantedOrder(Integer id) {
        int rowsAffected = mapper.deleteWantedOrder(id);
        if (rowsAffected != 1) {
            UniversalResponse.UserFail("ID错误");
        }
        return UniversalResponse.Ok("删除成功");
    }

    public UniversalResponse getCriminalDetail(Integer criminalID) {
        CriminalDetailResponse detail = new CriminalDetailResponse();
        Criminal basic = mapper.getCriminalInfo(criminalID);
        // 获取portraitFile URL
        if (basic.getPortraitFileID() != null) {
            basic.setPortraitFileURL(
                    fileService.getTempraryURL(basic.getPortraitFileID(), null));
        }
        // 创建者和更新者
        basic.setUserCreatedBy(userMapper.selectUserByID(basic.getCreatedBy()));
        basic.setUserUpdatedBy(userMapper.selectUserByID(basic.getUpdatedBy()));

        detail.setCriminalBasicInfo(basic);

        // 相关联系人
        detail.setCriminalContacts(mapper.getContactsOfACriminal(criminalID));

        // 通缉令
        List<WantedOrder> wantedOrders = mapper.getWantedOrdersOfACriminal(criminalID);
        for(WantedOrder order : wantedOrders) {
            System.out.println(order);
            order.setDistrict(districtMapper.selectDistrictById(order.getDistrictID()));
        }
        detail.setWantedOrders(wantedOrders);

        // 线索
        List<Clue> clueList = mapper.getCluesOfACriminal(criminalID);
        for (Clue clue : clueList) {
            // 从OSS获取每个线索中提供的照片URL
            if (clue.getFileIDs() != null) {
                ArrayList<String> fileURLs = new ArrayList<>();
                String[] fileIDS = clue.getFileIDs().split(",");
                for (String fileID : fileIDS) {
                    fileURLs.add(fileService.getTempraryURL(fileID, null));
                }
                clue.setFileURLs(fileURLs);
            }
        }
        detail.setClues(clueList);
        return UniversalResponse.Ok(detail);
    }

    /**
     * 多条件模糊查找逃犯
     * pageSize默认为10, page传递页码从0开始，将自动转换为offset
     * @param search 搜索条件
     * @return 搜索结果列表
     */
    public UniversalResponse searchCriminals(SearchCriminalRequest search) {
        HashMap<String, Object> result = new HashMap<>();
        // 处理日期条件前端传递不包含时间导致不能搜索精确一天的问题
        if (search.getArrestCreateTimeEnd() != null){
            search.setArrestCreateTimeEnd(search.getArrestCreateTimeStart().plusHours(23).plusMinutes(59).plusMinutes(59));
        }

        Integer totalCount = mapper.searchResultCount(search);
        // 默认页大小
        if (search.getPageSize() == null)
            search.setPageSize(Constants.DefaultPageSize);
        // page 转换为offSet
        if (search.getPage() == null)
            search.setPage(0); // 默认第一页
        else
            search.setPage(search.getPage() - 1); // 前端返回页码从1开始
        // 计算offset
        search.setOffset(search.getPage() * search.getPageSize());

        List<Criminal> criminals = mapper.searchCriminals(search);
        // 获取照片临时url
        for (Criminal criminal : criminals) {
            if (criminal.getPortraitFileID() != null)
                criminal.setPortraitFileURL( fileService.getTempraryURL(criminal.getPortraitFileID(), null));
        }

        result.put("totalCount", totalCount);
        result.put("currentPage", search.getPage());
        result.put("criminals", criminals);

        return UniversalResponse.Ok(result);
    }

    public UniversalResponse searchCriminalByIDCardID(String idCardID) {
        List<Criminal> result = mapper.searchCriminalByIDCard(idCardID);
        return UniversalResponse.Ok(result);
    }

}
