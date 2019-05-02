package com.linkinghack.criminalquerybase.dao.mapper;
import com.linkinghack.criminalquerymodel.data_model.Department;
import com.linkinghack.criminalquerymodel.data_model.User;
import com.linkinghack.criminalquerymodel.transfer_model.SearchUserRequest;
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

    List<User> users(SearchUserRequest request);

    Department getUserDepartment(Integer UID);

    Integer deleteUser(Integer id);

    List<User> inactivatedUsers(SearchUserRequest request);

    Integer activateUser(Integer id);
}
