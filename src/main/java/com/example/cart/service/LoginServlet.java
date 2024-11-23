package com.example.cart.service;

import com.example.cart.entity.User;
import com.example.cart.dao.UserService;
import com.example.cart.thymeleaf.ThymeleafBaseServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends ThymeleafBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        try{
            User user = UserService.UserCheck(account, password);
            if (user != null) {
                // 登录成功，设置用户信息到 session
                HttpSession session = request.getSession();
                // 将用户的账户与姓名存储到会话中
                session.setAttribute("userAccount", account);
                session.setAttribute("username", user.getUserName());
                // 判断是管理员还是普通用户
                if (account.startsWith("A")) {
                    // 跳转到管理界面
                    response.sendRedirect("AdminDashboard");
                }else{
                    // 跳转到处理首页的servlet程序
                    response.sendRedirect("UserHome");
                }
            } else {
                // 登录失败，设置错误信息到 session
                HttpSession session = request.getSession();
                session.setAttribute("msg", "用户名或密码错误");
                // 重定向回登录页面
                response.sendRedirect("LoginView");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
