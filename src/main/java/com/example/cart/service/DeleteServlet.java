package com.example.cart.service;

import com.example.cart.dao.GoodsService;
import com.example.cart.dao.SaleBillService;
import com.example.cart.dao.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/delete/*")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取路径信息
        String pathInfo = request.getPathInfo();  // /users或者/goods等
        // 获取携带参数：要删除记录的id
        String id = request.getParameter("id");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonResponse = new JSONObject();

        if (pathInfo != null && pathInfo.length() > 1) {
            String target = pathInfo.substring(1);  // 去掉前导的斜杠，获取路径内容
            if (target.equals("user")) {
                try {
                    // 执行对应的删除方法
                    UserService.deleteUser(id);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "用户已成功删除。");
                } catch (Exception e) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "删除用户时发生错误。");
                }
            } else if(target.equals("goods")) {
                try {
                    // 执行对应的删除方法
                    GoodsService.deleteGoods(id);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "商品已成功删除。");
                } catch (Exception e) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "删除商品时发生错误。");
                }
            }else if(target.equals("salebills")) {
                try {
                    // 执行对应的删除方法
                    SaleBillService.deleteSaleBill(id);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "账单已成功删除。");
                } catch (Exception e) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "删除账单时发生错误。");
                }
            }
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "请求路径无效。");
        }

        response.getWriter().write(jsonResponse.toString());
    }
}