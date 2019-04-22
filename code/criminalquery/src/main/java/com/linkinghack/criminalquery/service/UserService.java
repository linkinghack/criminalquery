package com.linkinghack.criminalquery.service;
import com.linkinghack.criminalquery.TransferModel.LoginRequest;
import com.linkinghack.criminalquery.dao.mapper.UserMapper;
import com.linkinghack.criminalquery.exception.RegisterFailedException;
import com.linkinghack.criminalquery.TransferModel.UniversalResponse;
import com.linkinghack.criminalquery.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
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
            else{
                throw new RegisterFailedException("插入新用户行数为0");
            }
        }catch (RegisterFailedException e){
            return UniversalResponse.ServerFail(e.getMessage());
        } catch (Exception e) {
            return UniversalResponse.ServerFail("未知错误");
        }
    }

    public UniversalResponse login(LoginRequest request, HttpSession session) {
        User user = userMapper.selectUserByUserID(request.getUserID());
        if (user == null) {
            return UniversalResponse.UserFail("用户名不存在");
        }
        if (!user.getActivated()){
            return UniversalResponse.UserFail("用户被锁定");
        }
        if (!checkAuth(request.getUserID(), request.getPassword())) {
            return UniversalResponse.UserFail("密码错误");
        }
        // 验证通过
        session.setAttribute("user", user);
        if (request.getRemember()){
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
        }else {
            return UniversalResponse.UserFail("鉴权失败");
        }
    }

    public UniversalResponse updatePassword(String userID, String oldPassword, String newPassword) {
        if (!checkAuth(userID,oldPassword)){
            return UniversalResponse.UserFail("原密码错误");
        }
        if (newPassword.length() < 6) {
            return UniversalResponse.UserFail("密码最少6位");
        }

        int rowsAffected = userMapper.updatePassword(userID, newPassword);
        if (rowsAffected == 1){
            return UniversalResponse.Ok("修改成功");
        } else {
            String errID = UUID.randomUUID().toString();
            logger.error("更改密码失败, userID={}, errID= {} ", userID, errID);
            return UniversalResponse.ServerFail("未知错误,errID = " + errID);
        }
    }

    public UniversalResponse getAllUsers(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole() == 1) {
            return UniversalResponse.Ok(userMapper.users());
        } else {
            return UniversalResponse.UserFail("没有权限");
        }
    }

    private boolean checkAuth(String userID, String rawPassword) {
        User user = userMapper.selectUserByUserID(userID);
        String passwordHash = DigestUtils.sha3_256Hex(rawPassword);
        return (user != null && user.getPassword().equals(passwordHash));
    }
}
