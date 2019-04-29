package com.linkinghack.criminalquerybase.exception;

import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public UniversalResponse allError(Exception e) {
        e.printStackTrace();
        return UniversalResponse.UserFail(e.getMessage());
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    @ResponseBody
    public UniversalResponse missingParameter() {
        return UniversalResponse.UserFail("参数不足");
    }
}
