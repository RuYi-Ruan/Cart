package com.example.cart.service;

import com.example.cart.entity.Bill;
import com.example.cart.dao.SaleBillService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.Map;

@WebServlet("/SettleBill")
public class SettleBillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取当前会话
        HttpSession session = request.getSession();

        // 从会话中获取账单数据
        Map<String, Bill> billMap = (Map<String, Bill>) session.getAttribute("bills");
        // 从会话中获取用户的账户
        String userAccount = (String) session.getAttribute("userAccount");
        // 检查账单是否为空，并打印账单信息
        if (billMap != null) {
            for (Map.Entry<String, Bill> entry : billMap.entrySet()) {
                Bill bill = entry.getValue();
                // 打印每个商品的详细信息
                System.out.println("商品ID: " + bill.getId() +
                        ", 商品名称: " + bill.getGoodsName() +
                        ", 数量: " + bill.getQuantity() +
                        ", 单价: " + bill.getSalePrice() +
                        ", 总价: " + bill.getTotalPrice());
            }

            // 将账单写入数据库
            try{
                SaleBillService.addSaleBill(billMap, userAccount);
            }catch(Exception e){
                e.printStackTrace();
            }

            // 结算后，应清除会话中的购物记录，然后跳转回首页
            session.removeAttribute("bills");
            response.sendRedirect("UserHome");
        } else {
            System.out.println("当前购物车为空！");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
