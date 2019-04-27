package com.linkinghack.criminalquerymodel.transfer_model;

import com.linkinghack.criminalquerymodel.config.StatusDefinition;
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
        response.status = StatusDefinition.NormalSuccess;
        response.msg = StatusDefinition.explain.get(StatusDefinition.NormalSuccess);
        response.data = data;
        return response;
    }

    public static UniversalResponse ServerFail(Object data) {
        UniversalResponse response = new UniversalResponse();
        response.status = StatusDefinition.ServerError;
        response.msg = StatusDefinition.explain.get(StatusDefinition.ServerError);
        response.data = data;
        return response;
    }

    public static UniversalResponse UserFail(Object data) {
        UniversalResponse response = new UniversalResponse();
        response.status = StatusDefinition.UserError;
        response.msg = StatusDefinition.explain.get(StatusDefinition.UserError);
        response.data = data;
        return response;
    }
}
