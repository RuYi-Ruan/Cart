package com.example.cart.service;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/find/*")
public class FindServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取路径信息
        String pathInfo = request.getPathInfo();  // /users或者/goods等
        // 获取页码以及页面能显示的最多记录数
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        if (pathInfo != null && pathInfo.length() > 1) {
            String target = pathInfo.substring(1);  // 去掉前导的斜杠，获取路径内容

            if (target.equals("users")) {

            }else if(target.equals("goods")) {

            }else if(target.equals("salebill")) {

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
