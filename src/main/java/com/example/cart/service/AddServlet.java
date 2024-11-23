package com.example.cart.service;

import com.example.cart.dao.GoodsService;
import com.example.cart.dao.SaleBillService;
import com.example.cart.dao.UserService;
import com.example.cart.entity.Goods;
import com.example.cart.entity.SaleBill;
import com.example.cart.entity.User;
import com.example.cart.utils.GetTargetObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/add/*")
public class AddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取路径信息
        String pathInfo = request.getPathInfo();  // /users或者/goods等

        // 读取请求体中的 JSON 数据
        StringBuilder jsonBuffer = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // 使用 org.json.JSONObject 解析 JSON 数据
        JSONObject jsonObject = new JSONObject(jsonBuffer.toString());


        // 设置响应为json格式
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonResponse = new JSONObject();

        if(pathInfo != null && pathInfo.length() > 1){
            String target = pathInfo.substring(1);  // 去掉前导的斜杠，获取路径内容
            // 判断是那种请求，执行不同的操作
            if(target.equals("user")){
                User targetUser = GetTargetObject.getTargetUser(jsonObject);
                try{
                    // 执行dao层对应的方法
                    UserService.signUp(targetUser);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "用户添加成功");
                }catch (Exception e){
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "用户添加失败：" + e.getMessage());
                }
            }else if (target.equals("goods")){
                Goods targetGoods = GetTargetObject.getTargetGoods(jsonObject);
                try{
                    // 执行dao层对应的方法
                    GoodsService.addGoods(targetGoods);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "商品添加成功");
                }catch (Exception e){
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "商品添加失败：" + e.getMessage());
                }

            }else if(target.equals("salebill")){
                // 获取目标对象
                SaleBill targetSaleBill = GetTargetObject.getTargetSaleBill(jsonObject);
                try{
                    // 执行dao层对应的方法
                    SaleBillService.addSaleBill(targetSaleBill);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "账单添加成功");
                }catch (Exception e){
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "账单添加失败：" + e.getMessage());
                }
            }
        }else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "请求路径无效。");
        }

        // 将 JSON 响应返回给前端
        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
