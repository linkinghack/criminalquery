package com.linkinghack.criminalquery.controller;

import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public UniversalResponse allError(Exception e){
        return UniversalResponse.UserFail(e.getClass().getTypeName() + " | " + e.getMessage());
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    @ResponseBody
    public UniversalResponse missingParameter(){
        return UniversalResponse.UserFail("参数不足");
    }
}
