package com.linkinghack.criminalquery.dao.mapper;

import com.linkinghack.criminalquery.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    Integer createUser(User user);

    Integer updateUser(User user);

    User selectUserByID(Integer id);

    User selectUserByUserID(String userID);

    Integer updatePassword(String userID, String newPassword);
}
