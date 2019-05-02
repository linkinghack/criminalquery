package com.linkinghack.criminalquerysso.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import com.linkinghack.criminalquerysso.dao.UserMapper;
import com.linkinghack.criminalquerysso.model.UserStatus;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "auth")
public class AuthService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private StringRedisTemplate redis;
    private UserMapper userMapper;
    private GsonBuilder gsonBuilder;
    @Setter
    private long loginTime;
    @Setter
    private long rememberTime;

    @Autowired
    public AuthService(StringRedisTemplate template, UserMapper userMapper) {
        this.redis = template;
        this.userMapper = userMapper;
        this.gsonBuilder = new GsonBuilder();
    }

    /**
     * 登录功能，通常应该由客户端发起
     *
     * @param userID      用户ID
     * @param rawPassword 用户明文密码
     * @param remember    是否保持会话较长时间
     * @return 登录成功时返回中心验证 token 和 用户信息
     * TODO 用户信息存进redis前加密，AES-256及以上安全性
     */
    public UniversalResponse login(String userID, String rawPassword, boolean remember) {
        User user = userMapper.selectUserByUserID(userID);
        if (user == null) {
            return UniversalResponse.UserFail("用户名不存在");
        }
        if (!user.getActivated()) {
            return UniversalResponse.UserFail("用户被锁定");
        }
        if (!checkAuth(userID, rawPassword)) {
            return UniversalResponse.UserFail("密码错误");
        }

        // 验证通过, 生成全局会话token
        user.setPassword(null); // 脱敏
        String token = Md5Crypt.md5Crypt((UUID.randomUUID().toString() + userID).getBytes());
        token = Base64.getUrlEncoder().encodeToString(token.getBytes());

        // 保存用户id到redis
        long hold = loginTime;
        if (remember)
            hold = rememberTime;
        redis.opsForValue().set(token, userStatusToJson(new UserStatus(user, remember)), Duration.ofMinutes(hold));

        // 返回用户信息和token
        Map<String, Object> respData = new HashMap<>();
        respData.put("token", token);
        respData.put("user", user);
        return UniversalResponse.Ok(respData);
    }

    /**
     * 由token判断登录状态, 供系统其他微服务调用判断登录状态
     *
     * @param token 会话 token
     * @return 验证结果, 验证通过时返回用户数据
     */
    public User auth(String token) {
        UserStatus userStatus = userStatusFromJson(redis.opsForValue().get(token));
        if (userStatus == null) { // token无效或登录过期
            return null;
        }

        // 刷新redis可用时间
        if (userStatus.getRemember())
            redis.opsForValue().set(token, userStatusToJson(userStatus), Duration.ofMinutes(rememberTime));
        else
            redis.opsForValue().set(token, userStatusToJson(userStatus), Duration.ofMinutes(loginTime));

        return userStatus.getUser();
    }

    /**
     * 登出
     *
     * @param token 用户token
     * @return 成功
     */
    public UniversalResponse logout(String token) {
        redis.delete(token);
        return UniversalResponse.Ok("登出成功");
    }

    /**
     * 由用户名和用户明文密码验证是否匹配数据库中记录
     * 验证密码将计算密码的 SHA3-256 哈希值
     *
     * @param userID      用户id
     * @param rawPassword 用户密码
     * @return true: 通过, false: 失败
     */
    private boolean checkAuth(String userID, String rawPassword) {
        User user = userMapper.selectUserByUserID(userID);
        String passwordHash = DigestUtils.sha3_256Hex(rawPassword);
        return (user != null && user.getPassword().equals(passwordHash));
    }

    private String userStatusToJson(UserStatus userStatus) {
        Gson gson = gsonBuilder.create();
        logger.info(gson.toJson(userStatus));
        return gson.toJson(userStatus);
    }

    private UserStatus userStatusFromJson(String userStatusString) {
        logger.info("userStatusFromJson: {}", userStatusString);
        if (userStatusString == null)
            return null;

        Gson gson = gsonBuilder.create();
        return gson.fromJson(userStatusString, UserStatus.class);
    }
}
