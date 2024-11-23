package com.example.cart.service;

import com.example.cart.entity.Goods;
import com.example.cart.dao.GoodsService;
import com.example.cart.thymeleaf.ThymeleafBaseServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/UserHome")
public class UserHome extends ThymeleafBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取页码
        int pageNo = 1;
        String pageAttr = request.getParameter("page");
        if (pageAttr != null) {
            pageNo = Integer.parseInt(pageAttr);
        }

        // 获取当前页商品列表
        List<Goods> goodsList = GoodsService.getPageGoods((pageNo - 1) * 6);
        HttpSession session = request.getSession();
        session.setAttribute("hasGoods", goodsList);

        // 计算总页数
        int total = GoodsService.getGoodsCount();
        int totalPages = (int) Math.ceil(total / 6.0);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("currentPage", pageNo);

        // 检查是否为 AJAX 请求
        String requestedWith = request.getHeader("X-Requested-With");
        System.out.println("RequestedWith: " + requestedWith);  // 打印请求头，用于调试

        if ("XMLHttpRequest".equals(requestedWith)) {
            // 如果是 AJAX 请求，返回商品列表的片段内容
            super.processTemplate("goods", request, response);
        } else {
            // 否则，渲染整个页面
            super.processTemplate("home", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
