package com.example.cart.service;

import com.example.cart.entity.Bill;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

@WebServlet("/deleteCartBill")
public class DeleteCartBillServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodsId = request.getParameter("goodsId");
        HttpSession session = request.getSession();
        Map<String, Bill> bills = (Map<String, Bill>) session.getAttribute("bills");

        response.setContentType("application/json");

        if (goodsId == null || bills == null || !bills.containsKey(goodsId)) {
            response.getWriter().write("{\"ok\": false, \"message\": \"删除失败：商品不存在\"}");
            return;
        }

        // 删除商品并更新会话中的数据
        bills.remove(goodsId);
        session.setAttribute("bills", bills);

        // 将 bills 转换为 JSON 数组
        JSONArray billsArray = new JSONArray();
        for (Map.Entry<String, Bill> entry : bills.entrySet()) {
            JSONObject billJson = new JSONObject();
            billJson.put("key", entry.getKey());
            billJson.put("goodsName", entry.getValue().getGoodsName());
            billJson.put("quantity", entry.getValue().getQuantity());
            billJson.put("salePrice", entry.getValue().getSalePrice());
            billJson.put("totalPrice", entry.getValue().getTotalPrice());
            billsArray.put(billJson);
        }

        // 返回成功消息和最新的 bills 列表
        JSONObject result = new JSONObject();
        result.put("ok", true);
        result.put("bills", billsArray);
        response.getWriter().write(result.toString());
    }
}
