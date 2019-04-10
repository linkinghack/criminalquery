package com.linkinghack.criminalquery.controller;
import com.linkinghack.criminalquery.TransferModel.LoginRequest;
import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    /**
     *  登录鉴权接口
     * @param loginRequest {"userID": "id", "password": "password", "remember": true}
     * @param session session
     * @return
     */
    @PostMapping("/auth")
    public UniversalResponse login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        logger.info("@request{/auth} userID: {}", loginRequest.getUserID());

        if (loginRequest.getUserID() == null || loginRequest.getUserID().length() > 32) {
            return UniversalResponse.UserFail("用户名长度不合法");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().length() < 6 ){
            return UniversalResponse.UserFail("密码不合法");
        }
        UniversalResponse universalResponse = userService.login(loginRequest, session );

        logger.info("@response{/auth} {}", universalResponse);
        return universalResponse;
    }
}
