package com.example.cart.service;

import com.example.cart.entity.User;
import com.example.cart.dao.UserService;
import com.example.cart.thymeleaf.ThymeleafBaseServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/SignServlet")
public class SignServlet extends ThymeleafBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取注册表单提交的信息
        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        Integer age = Integer.parseInt(request.getParameter("age"));
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        User newUser = new User(account,name, gender, age,  password);

        try{
            UserService.signUp(newUser);
            HttpSession session = request.getSession();
            session.setAttribute("msg", "注册成功");
            // 重定向会登录页面
            response.sendRedirect("LoginView");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
