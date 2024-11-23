package com.example.cart.mapper;

import com.example.cart.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    // 根据卡号获取指定用户
    User getUserByCardNo(@Param("card_no") String card_no);

    // 添加用户
    void insertUser(User user);

    // 获取所有用户
    List<User> selectUsers();

    // 根据id删除指定用户
    void deleteUserByCardNo(@Param("card_no") String card_no);

    // 根据id更新指定用户
    void updateUserByCardNo(@Param("user") User user);

    List<User> selectUsersByKeyword(@Param("keyword") String keyword);
}
