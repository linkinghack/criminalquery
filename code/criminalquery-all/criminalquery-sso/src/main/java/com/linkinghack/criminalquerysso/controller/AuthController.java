package com.linkinghack.criminalquerysso.controller;

import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.LoginRequest;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import com.linkinghack.criminalquerysso.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AuthService authService;

    @Autowired
    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/login")
    public UniversalResponse login(@RequestBody LoginRequest loginRequest) {
        String fn = "<POST>[/user/login]";
        logger.info("@request{} userID: {} {}", fn, loginRequest.getUserID(), loginRequest);

        if (loginRequest.getUserID() == null || loginRequest.getUserID().length() > 32 || loginRequest.getUserID().contains(" ")) {
            return UniversalResponse.UserFail("用户名不合法");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().length() < 6) {
            return UniversalResponse.UserFail("密码不合法");
        }
        if (loginRequest.getRemember() == null) {
            loginRequest.setRemember(false);
        }
        UniversalResponse response = authService.login(loginRequest.getUserID(), loginRequest.getPassword(), loginRequest.getRemember());

        logger.info("@response{/user/auth} {}", response);
        return response;
    }

    @GetMapping("/auth/{token}")
    public User auth(@PathVariable(value = "token", required = true) String token) {
        String fn = "<Get>[/auth/{token}]";
        logger.info("@request{} {}", fn, token);
        User user = authService.auth(token);
        logger.info("@response{} {}", fn, user);
        return user;
    }

    @PutMapping("/logout/{token}")
    @PostMapping("/logout/{token}")
    public UniversalResponse logout(@PathVariable(value = "token", required = true) String token) {
        String fn = "<PUT | POST>[/logout/{token}]";
        logger.info("@Request{} token:{}", fn, token);
        return authService.logout(token);
    }

}
