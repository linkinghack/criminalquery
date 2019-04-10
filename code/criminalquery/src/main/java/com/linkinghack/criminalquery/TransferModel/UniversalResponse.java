package com.linkinghack.criminalquery.TransferModel;

import com.linkinghack.criminalquery.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversalResponse {
    private Integer status;
    private Object data;
    private String msg;

    public static UniversalResponse Ok(Object data) {
        UniversalResponse response = new UniversalResponse();
        response.status = Constants.STATUS_OK;
        response.msg = Constants.statusExplain.get(Constants.STATUS_OK);
        response.data = data;
        return response;
    }

    public static UniversalResponse ServerFail(Object data) {
        UniversalResponse response = new UniversalResponse();
        response.status = Constants.STATUS_SERVER_ERR;
        response.msg = Constants.statusExplain.get(Constants.STATUS_SERVER_ERR);
        response.data = data;
        return response;
    }

    public static UniversalResponse UserFail(Object data) {
        UniversalResponse response = new UniversalResponse();
        response.status = Constants.STATUS_REQUEST_ERR;
        response.msg = Constants.statusExplain.get(Constants.STATUS_REQUEST_ERR);
        response.data = data;
        return response;
    }
}
