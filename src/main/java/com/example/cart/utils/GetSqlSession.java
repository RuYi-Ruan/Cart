package com.example.cart.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class GetSqlSession {
    public static SqlSession createSqlSession() {
        SqlSessionFactory sqlSessionFactory = null;
        InputStream input = null;
        SqlSession session = null;

        try {
            // 获得mybatis的环境配置文件
            String resource = "mybatis-config.xml";  // 使用类路径下的相对路径
            // 以流的方式获取resource(mybatis的环境配置文件)
            input = Resources.getResourceAsStream(resource);
            // 创建会话工厂
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
            // 通过工厂得到SqlSession,传入参数true自动提交事务
            session = sqlSessionFactory.openSession(true);
            return session;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
