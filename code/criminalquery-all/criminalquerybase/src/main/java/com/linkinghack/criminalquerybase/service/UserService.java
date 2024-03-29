package com.linkinghack.criminalquerybase.service;

import com.linkinghack.criminalquerybase.Constants;
import com.linkinghack.criminalquerybase.dao.mapper.UserMapper;
import com.linkinghack.criminalquerybase.exception.RegisterFailedException;
import com.linkinghack.criminalquerymodel.data_model.Department;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.LoginRequest;
import com.linkinghack.criminalquerymodel.transfer_model.SearchUserRequest;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper mapper) {
        this.userMapper = mapper;
    }

    public UniversalResponse register(User user) {
        user.setPassword(DigestUtils.sha3_256Hex(user.getPassword())); // hash password
        try {
            int lineAffected = userMapper.createUser(user);
            if (lineAffected == 1)
                return UniversalResponse.Ok("注册成功,在审批通过前您无法登录。");
            else {
                throw new RegisterFailedException("插入新用户行数为0");
            }
        } catch (RegisterFailedException e) {
            return UniversalResponse.ServerFail(e.getMessage());
        } catch (Exception e) {
            return UniversalResponse.ServerFail("未知错误");
        }
    }

    public UniversalResponse getDepartmentBelongsTo(Integer UID) {
        Department department = userMapper.getUserDepartment(UID);
        return UniversalResponse.Ok(department);
    }

    public UniversalResponse login(LoginRequest request, HttpSession session) {
        User user = userMapper.selectUserByUserID(request.getUserID());
        if (user == null) {
            return UniversalResponse.UserFail("用户名不存在");
        }
        if (!user.getActivated()) {
            return UniversalResponse.UserFail("用户被锁定");
        }
        if (!checkAuth(request.getUserID(), request.getPassword())) {
            return UniversalResponse.UserFail("密码错误");
        }
        // 验证通过
        session.setAttribute("user", user);
        if (request.getRemember()) {
            session.setMaxInactiveInterval(3600); // 登录状态最多维持1小时
        }
        System.out.println(session.getAttribute("user"));
        user.setPassword(null);
        return UniversalResponse.Ok(user);
    }

    public UniversalResponse updateUser(User user) {
        if (checkAuth(user.getUserID(), user.getPassword())) { // 此时传入的user对象中密码还是明文
            userMapper.updateUser(user);
            return UniversalResponse.Ok(user);
        } else {
            return UniversalResponse.UserFail("鉴权失败");
        }
    }

    public UniversalResponse updatePassword(String userID, String oldPassword, String newPassword) {
        if (!checkAuth(userID, oldPassword)) {
            return UniversalResponse.UserFail("原密码错误");
        }
        if (newPassword.length() < 6) {
            return UniversalResponse.UserFail("密码最少6位");
        }

        int rowsAffected = userMapper.updatePassword(userID, newPassword);
        if (rowsAffected == 1) {
            return UniversalResponse.Ok("修改成功");
        } else {
            String errID = UUID.randomUUID().toString();
            logger.error("更改密码失败, userID={}, errID= {} ", userID, errID);
            return UniversalResponse.ServerFail("未知错误,errID = " + errID);
        }
    }

    public UniversalResponse getAllUsers(SearchUserRequest request, boolean isActivatedUser) {
        // 处理pageSize
        if (request.getPageSize() == null) {
            request.setPageSize(Constants.DefaultPageSize);
        }
        if (request.getPage() == null) {
            request.setPage(1);
        }
        // 计算offset
        request.setOffset(request.getPageSize() * (request.getPage() - 1));

        // 处理筛选条件
        if (request.getNameOrID()!= null && request.getNameOrID().length() < 1){
            request.setNameOrID(null);
        }

        List<User> userList;
        if (isActivatedUser) {
            userList = userMapper.users(request);
        } else {
            userList = userMapper.inactivatedUsers(request);
        }
        if (userList.size() > 0) {
            for (User user : userList) {
                user.setPassword(null); // 脱敏
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("users", userList);
        result.put("pageSize", request.getPageSize());
        result.put("page", request.getPage());
        return UniversalResponse.Ok(result);
    }

    public UniversalResponse deleteUser(Integer uid) {
        User user = userMapper.selectUserByID(uid);
        int rowsAffected = userMapper.deleteUser(uid);
        if (rowsAffected == 1) {
            logger.warn("删除用户: {}", user);
            return UniversalResponse.Ok("删除成功");
        } else {
            logger.warn("删除用户异常: rowsAffected:{}, userToDelete:{}", rowsAffected, user);
            return UniversalResponse.ServerFail("删除数据异常,影响行数:" + rowsAffected);
        }
    }

    public UniversalResponse activateUser(Integer uid) {
        int rowsAffected = userMapper.activateUser(uid);
        if (rowsAffected == 1) {
            // TODO 发送激活邮件
            return UniversalResponse.Ok("已激活");
        } else {
            logger.warn("激活用户异常, uid:{}, rowsAffected:{}", uid, rowsAffected);
            return UniversalResponse.ServerFail("激活异常,影响行数:" + rowsAffected);
        }

    }

    private boolean checkAuth(String userID, String rawPassword) {
        User user = userMapper.selectUserByUserID(userID);
        String passwordHash = DigestUtils.sha3_256Hex(rawPassword);
        return (user != null && user.getPassword().equals(passwordHash));
    }
}
