package com.linkinghack.criminalquery.config;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static Integer STATUS_OK = 200;
    public static Integer STATUS_REQUEST_ERR = 400;
    public static Integer STATUS_SERVER_ERR = 500;

    public static Map<Integer,String> statusExplain = new HashMap<>(){};

    static {
        statusExplain.put(STATUS_OK, "成功");
        statusExplain.put(STATUS_REQUEST_ERR, "非法请求");
        statusExplain.put(STATUS_SERVER_ERR, "服务端异常");
    }
}
