package com.linkinghack.criminalquerybase.dao.mapper;

import com.linkinghack.criminalquerymodel.data_model.Clue;
import com.linkinghack.criminalquerymodel.data_model.Criminal;
import com.linkinghack.criminalquerymodel.data_model.CriminalContact;
import com.linkinghack.criminalquerymodel.data_model.WantedOrder;
import com.linkinghack.criminalquerymodel.transfer_model.SearchCriminalRequest;
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
