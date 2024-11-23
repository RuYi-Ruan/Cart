package com.example.cart.dao;

import com.example.cart.entity.Bill;
import com.example.cart.entity.SaleBill;
import com.example.cart.mapper.SaleBillMapper;
import com.example.cart.utils.GetSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaleBillService {

    // 添加账单记录
    public static void addSaleBill(Map<String, Bill> bills, String userAccount) {
        List<SaleBill> saleBills = new ArrayList<SaleBill>();

        for(Map.Entry<String, Bill> entry:bills.entrySet()){
            Bill bill = entry.getValue();  // 获取Bill对象

            // 创建SaleBill对象
            SaleBill saleBill = new SaleBill();

            // 填充SaleBill对象的字段
            saleBill.setGoodsNo(bill.getId());
            saleBill.setCardNo(userAccount);
            saleBill.setNumber(bill.getQuantity());
            saleBill.setTotalPrice(bill.getTotalPrice());

            // 将SaleBill对象加入到列表中
            saleBills.add(saleBill);
        }

        SqlSession session = null;
        try{
            session = GetSqlSession.createSqlSession();
            SaleBillMapper saleBillMapper = session.getMapper(SaleBillMapper.class);
            saleBillMapper.insertSaleBill(saleBills);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    // 获取所有账单记录
    public static List<SaleBill> selectAllSaleBill() {
        SqlSession session = null;
        List<SaleBill> saleBills = null;
        try{
            session = GetSqlSession.createSqlSession();
            SaleBillMapper saleBillMapper = session.getMapper(SaleBillMapper.class);
            saleBills = saleBillMapper.getAllSaleBill();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return saleBills;
    }

    // 根据id删除指定账单记录
    public static void deleteSaleBill(String id) {
        SqlSession session = GetSqlSession.createSqlSession();
        SaleBillMapper saleBillMapper = session.getMapper(SaleBillMapper.class);
        try{
            saleBillMapper.deleteSaleBillById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 根据id更新指定账单记录
    public static void updateSaleBill(SaleBill saleBill) {
        SqlSession session = GetSqlSession.createSqlSession();
        SaleBillMapper saleBillMapper = session.getMapper(SaleBillMapper.class);
        try{
            saleBillMapper.updateSaleBillById(saleBill);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 添加单个账单
    public static void addSaleBill(SaleBill saleBill) {
        SqlSession session = GetSqlSession.createSqlSession();
        SaleBillMapper saleBillMapper = session.getMapper(SaleBillMapper.class);
        try{
            saleBillMapper.insertNewSaleBill(saleBill);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 关键次查询商品
    public static List<SaleBill> selectSaleBillByKeyword(String keyword) {
        SqlSession session = GetSqlSession.createSqlSession();
        SaleBillMapper saleBillMapper = session.getMapper(SaleBillMapper.class);

        return saleBillMapper.getSaleBillByKeyword(keyword);
    }
}
