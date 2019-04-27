package com.linkinghack.criminalquerybase.controller;

import com.linkinghack.criminalquerybase.service.UserService;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.LoginRequest;
import com.linkinghack.criminalquerymodel.transfer_model.RegisterRequest;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    /**
     *  登录鉴权接口 /auth
     * @param loginRequest {"userID": "id", "password": "password", "remember": true}
     * @param session session
     * @return
     * {
     *     "status": 200,
     *     "data": {
     *         "id": 1,
     *         "userID": "admin",
     *         "password": null,
     *         "email": "linkinghack@outlook.com",
     *         "realName": "刘磊",
     *         "role": 1,
     *         "departmentID": 1,
     *         "activated": true,
     *         "phone": "18235101905",
     *         "department": {
     *             "id": 1,
     *             "departmentName": "公安部",
     *             "supervisorID": 0,
     *             "level": 1,
     *             "districtID": 100000,
     *             "value": null,
     *             "label": null
     *         }
     *     },
     *     "msg": "成功"
     * }
     */
    @PostMapping("/auth")
    public UniversalResponse login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        String fn = "<POST>[/user/auth]";
        logger.info("@request{/user/auth} userID: {}", loginRequest.getUserID());

        if (loginRequest.getUserID() == null || loginRequest.getUserID().length() > 32) {
            return UniversalResponse.UserFail("用户名长度不合法");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().length() < 6 ){
            return UniversalResponse.UserFail("密码不合法");
        }
        UniversalResponse universalResponse = userService.login(loginRequest, session );

        logger.info("@response{/user/auth} {}", universalResponse);
        return universalResponse;
    }

    @PostMapping("/logout")
    public UniversalResponse logout(HttpSession session) {
        session.invalidate();
        return UniversalResponse.Ok("已登出");
    }

    @PostMapping("/register")
    public UniversalResponse register(@RequestBody RegisterRequest registerRequest) {
        String fn = "<POST>[/user/register]";
        logger.info("@request {}", fn);
        if (!registerRequest.getPassword().equals(registerRequest.getConfirm()))
            return UniversalResponse.UserFail("两密码不匹配");
        User user = new User();
        user.setUserID( registerRequest.getUserID());
        user.setPassword(registerRequest.getPassword());
        user.setDepartmentID(registerRequest.getDepartmentID());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRealName(registerRequest.getRealName());
        user.setActivated(false); // 默认不激活
        user.setRole(0); //默认非管理员
        UniversalResponse response = userService.register(user);
        logger.info("@response {} {}", fn, response);
        return response;
    }

    @GetMapping("/ofDepartment/{UID}")
    public UniversalResponse getDepartmentBelongsTo(@PathVariable("UID") Integer UID) {
        String fn = "<GET>[/user/ofDepartment/{UID}]";
        logger.info("@Request{} UID:{}", fn, UID);
        UniversalResponse response = userService.getDepartmentBelongsTo(UID);
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }


    @GetMapping("/all")
    public UniversalResponse allUsers(HttpSession session) {
        return userService.getAllUsers(session);
    }
}
