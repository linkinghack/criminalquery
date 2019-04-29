package com.linkinghack.criminalquerymodel.transfer_model;

import com.linkinghack.criminalquerymodel.data_model.Clue;
import com.linkinghack.criminalquerymodel.data_model.Criminal;
import com.linkinghack.criminalquerymodel.data_model.CriminalContact;
import com.linkinghack.criminalquerymodel.data_model.WantedOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriminalDetailResponse {
    private Criminal criminalBasicInfo;
    private List<CriminalContact> criminalContacts;
    private List<WantedOrder> wantedOrders;
    private List<Clue> clues;
}
