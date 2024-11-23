package com.example.cart.utils;

import com.example.cart.entity.Goods;
import com.example.cart.entity.SaleBill;
import com.example.cart.entity.User;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTargetObject {
    // 从json数据中提取数据到指定对象

    public static User getTargetUser(JSONObject jsonObject) {
        String card_no = jsonObject.getString("card_no");
        String username = jsonObject.getString("username");
        String sex = jsonObject.getString("sex");
        Integer age = jsonObject.getInt("age");
        String password = jsonObject.getString("password");

        return new User(card_no, username, sex, age, password);
    }

    public static Goods getTargetGoods(JSONObject jsonObject) {
        String goods_no = jsonObject.getString("goods_no");
        String goods_name = jsonObject.getString("goods_name");
        double in_price = jsonObject.getDouble(("in_price"));
        double sale_price = jsonObject.getDouble(("sale_price"));
        int number = jsonObject.getInt("number");
        String img_url = jsonObject.getString("img_url");

        return new Goods(goods_no, goods_name, in_price, sale_price, number, img_url);
    }

    public static SaleBill getTargetSaleBill(JSONObject jsonObject) {
        String goods_no = jsonObject.getString("goods_no");
        String card_no = jsonObject.getString("card_no");
        int number = jsonObject.getInt("number");
        double total_price = jsonObject.getDouble("total_price");
        String bill_date = jsonObject.getString("bill_date");

        // 获取当前时间的 HH:mm:ss 格式
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(new Date());

        // 将当前时间附加到 bill_date 后面
        String formattedBillDate = bill_date + " " + currentTime;

        System.out.println(formattedBillDate);
        return new SaleBill(goods_no, card_no, number, total_price, formattedBillDate);
    }
}
