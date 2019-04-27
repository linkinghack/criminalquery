package com.linkinghack.criminalquerybase.dao.mapper;
import com.linkinghack.criminalquerymodel.data_model.Department;
import com.linkinghack.criminalquerymodel.data_model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    Integer createUser(User user);

    Integer updateUser(User user);

    User selectUserByID(Integer id);

    User selectUserByUserID(String userID);

    Integer updatePassword(String userID, String newPassword);

    List<User> users();

    Department getUserDepartment(Integer UID);
}
