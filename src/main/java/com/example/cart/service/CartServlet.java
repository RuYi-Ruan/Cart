package com.example.cart.service;

import com.example.cart.entity.Bill;
import com.example.cart.thymeleaf.ThymeleafBaseServlet;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CartServlet")
public class CartServlet extends ThymeleafBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 读取 JSON 数据
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // 将数据转为 JSON 数组
        try {
            JSONArray shoppingCart = new JSONArray(sb.toString());
            Map<String, Bill> billMap = new HashMap<>();  // 使用 Map 存储账单

            // 从会话中获取已存在的购物车账单
            HttpSession session = request.getSession();
            Map<String, Bill> existingBillMap = (Map<String, Bill>) session.getAttribute("bills");
            if (existingBillMap != null) {
                billMap.putAll(existingBillMap);  // 将现有账单加入 Map
            }

            // 遍历购物车数据，更新账单
            for (int i = 0; i < shoppingCart.length(); i++) {
                JSONObject item = shoppingCart.getJSONObject(i);
                String id = item.getString("id");
                String name = item.getString("name");
                int quantity = item.getInt("quantity");
                double salePrice = item.getDouble("salePrice");
                double totalPrice = salePrice * quantity;

                // 检查购物车中是否已有该商品
                Bill existingBill = billMap.get(id);
                if (existingBill != null) {
                    // 如果商品已存在，则更新数量和总价
                    existingBill.setQuantity(existingBill.getQuantity() + quantity);
                    existingBill.setTotalPrice(existingBill.getSalePrice() * existingBill.getQuantity());
                } else {
                    // 如果该商品不在已有账单中，则添加新记录
                    Bill oneBill = new Bill(id, name, quantity, salePrice, totalPrice);
                    billMap.put(id, oneBill);
                }
            }

            // 设置 JSON 响应
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("code", 200);
            jsonResponse.put("message", "接收成功");
            jsonResponse.put("ok", true);

            // 返回 JSON 响应
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());

            // 更新会话中的购物车账单
            session.setAttribute("bills", billMap);
        } catch (JSONException e) {
            // 处理 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"code\": 400, \"message\": \"Invalid JSON format\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
