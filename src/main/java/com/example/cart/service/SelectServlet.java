package com.example.cart.service;

import com.example.cart.dao.GoodsService;
import com.example.cart.dao.SaleBillService;
import com.example.cart.dao.UserService;
import com.example.cart.entity.Goods;
import com.example.cart.entity.SaleBill;
import com.example.cart.entity.User;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/select/*")
public class SelectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取路径信息
        String pathInfo = request.getPathInfo();  // /users或者/goods等
        // 获取页码以及页面能显示的最多记录数
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String keyword = request.getParameter("keyword");
        System.out.println("keyword:" + keyword);

        if (pathInfo != null && pathInfo.length() > 1) {
            String target = pathInfo.substring(1);  // 去掉前导的斜杠，获取路径内容

            if (target.equals("users")) {
                List<User> userList = null;
                try {
                    // 如果关键词不为null,则采取关键词查询
                    if(keyword.equals("null")){
                        userList = UserService.selectAll();
                    }else{
                        userList = UserService.selectByKeyword(keyword);
                    }

                    int totalItems = userList.size(); // 总条目数
                    int fromIndex = (page - 1) * pageSize;
                    // 分页
                    int toIndex = Math.min(fromIndex + pageSize, totalItems);
                    List<User> paginatedUsers = userList.subList(fromIndex, toIndex);

                    // 将数据存入JSON数组
                    JSONArray jsonArray = new JSONArray();
                    for (User user : paginatedUsers) {
                        JSONObject userJson = new JSONObject();
                        userJson.put("card_no", user.getCardNo());
                        userJson.put("username", user.getUserName());
                        userJson.put("sex", user.getSex());
                        userJson.put("age", user.getAge());
                        userJson.put("password", user.getPassword());
                        jsonArray.put(userJson);
                    }

                    JSONObject result = new JSONObject();
                    result.put("totalItems", totalItems);
                    result.put("data", jsonArray);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(result.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Error retrieving user data\"}");
                }
            } else if (target.equals("goods")) {
                List<Goods> goodsList = null;
                try {
                    // 如果关键词不为null,则采取关键词查询
                    if(keyword.equals("null")){
                        goodsList = GoodsService.getAllGoods();
                    }else{
                        goodsList = GoodsService.selectGoodsByKeyword(keyword);
                    }

                    int totalItems = goodsList.size();
                    int fromIndex = (page - 1) * pageSize;
                    int toIndex = Math.min(fromIndex + pageSize, totalItems);
                    List<Goods> paginatedGoods = goodsList.subList(fromIndex, toIndex);

                    JSONArray jsonArray = new JSONArray();
                    for (Goods goods : paginatedGoods) {
                        JSONObject goodsJson = new JSONObject();
                        goodsJson.put("goods_no", goods.getGoodsNo());
                        goodsJson.put("goods_name", goods.getGoodsName());
                        goodsJson.put("in_price", goods.getInPrice());
                        goodsJson.put("sale_price", goods.getSalePrice());
                        goodsJson.put("number", goods.getNumber());
                        goodsJson.put("img_url", goods.getImgUrl());
                        jsonArray.put(goodsJson);
                    }

                    JSONObject result = new JSONObject();
                    result.put("totalItems", totalItems);
                    result.put("data", jsonArray);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Error retrieving goods data\"}");
                }
            } else if (target.equals("salebills")) {
                List<SaleBill> billList = null;
                try {
                    if(keyword.equals("null")){
                        billList = SaleBillService.selectAllSaleBill();
                    }else{
                        billList = SaleBillService.selectSaleBillByKeyword(keyword);
                    }
                    int totalItems = billList.size();
                    int fromIndex = (page - 1) * pageSize;
                    int toIndex = Math.min(fromIndex + pageSize, totalItems);
                    List<SaleBill> paginatedBills = billList.subList(fromIndex, toIndex);

                    JSONArray jsonArray = new JSONArray();
                    for (SaleBill saleBill : paginatedBills) {
                        JSONObject billJson = new JSONObject();
                        billJson.put("goods_no", saleBill.getGoodsNo());
                        billJson.put("card_no", saleBill.getCardNo());
                        billJson.put("number", saleBill.getNumber());
                        billJson.put("total_price", saleBill.getTotalPrice());
                        billJson.put("bill_date", saleBill.getBillDate());
                        jsonArray.put(billJson);
                    }

                    JSONObject result = new JSONObject();
                    result.put("totalItems", totalItems);
                    result.put("data", jsonArray);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Error retrieving salebill data\"}");
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid path specified\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
