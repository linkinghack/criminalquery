package com.linkinghack.criminalquerymodel.config;

import java.util.HashMap;

public class StatusDefinition {
    public static int NormalSuccess = 200; // 通用请求成功状态
    public static int UnknowError = 500; // 未知错状态
    public static int ServerError = 501; // 服务端异常
    public static int UserError = 400; // 由于用户请求数据错误导致的异常状态

    public static int AuthSuccessStatus = 201; // SSO 验证通过
    public static int AuthFailStatus = 301; // SSO 验证失败

    public static HashMap<Integer, String> explain = new HashMap<Integer, String>();

    static {
        explain.put(NormalSuccess, "成功");
        explain.put(UnknowError, "未知错误");
        explain.put(UserError, "客户端导致错误");
        explain.put(AuthSuccessStatus, "SSO 校验通过");
        explain.put(AuthFailStatus, "SSO 校验失败");
        explain.put(ServerError, "服务器异常");
    }
}
