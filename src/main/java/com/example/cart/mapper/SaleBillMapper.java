package com.example.cart.mapper;

import com.example.cart.entity.SaleBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SaleBillMapper {
    void insertSaleBill(@Param("saleBillList") List<SaleBill> saleBillList);

    List<SaleBill> getAllSaleBill();

    void deleteSaleBillById(@Param("saleBillId") String saleBillId);

    // 根据id更新指定账单记录
    void updateSaleBillById(@Param("salebill") SaleBill saleBill);

    // 添加新的记录
    void insertNewSaleBill(@Param("salebill") SaleBill saleBill);

    // 关键词匹配查询
    List<SaleBill> getSaleBillByKeyword(@Param("keyword") String keyword);
}
