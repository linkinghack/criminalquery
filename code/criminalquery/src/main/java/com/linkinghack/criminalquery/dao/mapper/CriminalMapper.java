package com.linkinghack.criminalquery.dao.mapper;

import com.linkinghack.criminalquery.TransferModel.SearchCriminalRequest;
import com.linkinghack.criminalquery.model.Clue;
import com.linkinghack.criminalquery.model.Criminal;
import com.linkinghack.criminalquery.model.CriminalContact;
import com.linkinghack.criminalquery.model.WantedOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CriminalMapper {
    Criminal getCriminalInfo(Integer id);

    List<WantedOrder> getWantedOrdersOfACriminal(Integer criminalID);

    List<WantedOrder> getWantedOrdersOfADistrict(Integer districtID);

    List<Criminal> searchCriminals(SearchCriminalRequest request);

    Integer createWantedOrder(WantedOrder order);

    Integer createCriminal(Criminal criminal);
    Integer updateCriminal(Criminal criminal);
    Integer updateCriminalDate(Integer id);

    Integer addCriminalContact(CriminalContact contact);
    Integer addClue(Clue clue);
}
