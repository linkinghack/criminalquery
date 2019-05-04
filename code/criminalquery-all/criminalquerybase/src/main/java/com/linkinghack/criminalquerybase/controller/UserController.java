package com.linkinghack.criminalquerybase.controller;

import com.linkinghack.criminalquerybase.Constants;
import com.linkinghack.criminalquerybase.service.UserService;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.RegisterRequest;
import com.linkinghack.criminalquerymodel.transfer_model.SearchUserRequest;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * 判断token是否有效。无逻辑，由监听器处理
     *
     * @return ""
     */
    @GetMapping("/alive")
    public UniversalResponse isAlive() {
        return UniversalResponse.Ok("");
    }

    @PostMapping("/register")
    public UniversalResponse register(@RequestBody RegisterRequest registerRequest) {
        String fn = "<POST>[/user/register]";
        logger.info("@request {}", fn);
        if (!registerRequest.getPassword().equals(registerRequest.getConfirm()))
            return UniversalResponse.UserFail("两密码不匹配");
        User user = new User();
        user.setUserID(registerRequest.getUserID());
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

    /**
     * 审核用户注册通过,激活账号
     * 需要管理员权限
     *
     * @param uid     uid
     * @param request 用于获取当前用户角色
     * @return 激活结果
     */
    @PatchMapping("/activate/{id}")
    public UniversalResponse activateUser(@PathVariable("id") Integer uid, HttpServletRequest request) {
        String fn = "<PATCH>[/user/active/{id}]";
        User user = (User) request.getAttribute("user");
        UniversalResponse response;
        if (!user.getRole().equals(Constants.UserRoleManeger)) {
            response = UniversalResponse.UserFail("权限不足");
        } else {
            response = userService.activateUser(uid);
        }
        logger.info("@Response{} response{}", fn, response);
        return response;
    }

    /**
     * 获取所有已激活用户，用于用户管理页面
     * 需要管理员权限
     *
     * @param searchUserRequest 搜索条件 {userID:<string>, pageSize:Int, page:Int(start from 1)} 可选,模糊搜索用户登录用户名
     * @param request           用于获取当前用户
     * @return
     */
    @GetMapping("/activated")
    public UniversalResponse allUsers(SearchUserRequest searchUserRequest, HttpServletRequest request) {
        String fn = "<GET>[/user/all]";
        User user = (User) request.getAttribute("user");
        logger.info("@Request{} currentUser:{}", fn, user);
        UniversalResponse response;

        if (!user.getRole().equals(Constants.UserRoleManeger)) {
            response = UniversalResponse.UserFail("权限不足");
        } else response = userService.getAllUsers(searchUserRequest, true);

        logger.info("@Response{} response:{}", fn, response);
        return response;
    }

    /**
     * 获取未激活用户列表，用于新用户审批流
     * 需要管理员权限
     *
     * @param searchUserRequest 搜索条件 {realNameOrID:<string>, pageSize:Int, page:Int(from 1)}
     * @param request           用于获取当前用户
     * @return 未激活用户列表
     */
    @GetMapping("/inactivated")
    public UniversalResponse inactivatedUsers(SearchUserRequest searchUserRequest, HttpServletRequest request) {
        String fn = "<GET>[/user/inactivated]";
        User user = (User) request.getAttribute("user");
        logger.info("@Request{} currentUser:{}", fn, user);
        UniversalResponse response;

        if (!user.getRole().equals(Constants.UserRoleManeger)) {
            response = UniversalResponse.UserFail("权限不足");
        } else {
            response = userService.getAllUsers(searchUserRequest, false);
        }
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }

    /**
     * 删除用户, 需要管理员权限
     *
     * @param uid     用户id
     * @param request 用于获取当前用户
     * @return 删除结果
     */
    @DeleteMapping("/{uid}")
    public UniversalResponse deleteUser(@PathVariable("uid") Integer uid, HttpServletRequest request) {
        String fn = "<DELETE>[/user/{uid}]";
        User user = (User) request.getAttribute("user");
        UniversalResponse response;
        logger.info("@Request{}  param:uid={}, currentUser:{}", fn, uid, user);
        if (!user.getRole().equals(Constants.UserRoleManeger)) {
            response = UniversalResponse.UserFail("权限不足");
        } else {
            response = userService.deleteUser(uid);
        }
        logger.info("@Response{} response:{}", fn, response);
        return response;
    }
}
