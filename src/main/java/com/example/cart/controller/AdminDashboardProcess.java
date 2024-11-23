package com.example.cart.controller;

import com.example.cart.thymeleaf.ThymeleafBaseServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/AdminDashboard")
public class AdminDashboardProcess extends ThymeleafBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 跳转到后台管理首页
        super.processTemplate("admin", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}