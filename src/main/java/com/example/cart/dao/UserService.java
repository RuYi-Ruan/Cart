package com.example.cart.dao;

import com.example.cart.entity.User;
import com.example.cart.mapper.UserMapper;
import com.example.cart.utils.GetSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UserService {

    // 用户校验
    public static User UserCheck(String account, String password) {
        SqlSession session = GetSqlSession.createSqlSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);

        // 根据用户名查询用户
        User user = userMapper.getUserByCardNo(account);
        if (user == null) {
            // 用户名错误
            return null;
        } else if (!user.getPassword().equals(password)) {
            // 密码错误
            return null;
        }

        // 返回用户对象（登录成功）
        return user;
    }

    // 注册/添加新的用户到数据库
    public static void signUp(User user) {
        SqlSession session = GetSqlSession.createSqlSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        try{
            userMapper.insertUser(user);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取所有用户
    public static List<User> selectAll() {
        SqlSession session = GetSqlSession.createSqlSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> userList = userMapper.selectUsers();

        return userList;
    }

    // 根据id删除指定用户
    public static void deleteUser(String card_no){
        SqlSession session = GetSqlSession.createSqlSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        try{
            userMapper.deleteUserByCardNo(card_no);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据id更新指定用户
    public static void updateUser(User user) {
        SqlSession session = GetSqlSession.createSqlSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        try{
            userMapper.updateUserByCardNo(user);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据关键词查询用户
    public static List<User> selectByKeyword(String keyword) {
        SqlSession session = GetSqlSession.createSqlSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> userList = null;
        userList = userMapper.selectUsersByKeyword(keyword);

        return userList;
    }
}
