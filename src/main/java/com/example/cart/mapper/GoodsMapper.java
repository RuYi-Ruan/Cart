package com.example.cart.mapper;

import com.example.cart.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    // 获取所有商品信息
    List<Goods> selectAllGoods();

    // 获取商品总数
    Integer getTotalGoodsCount();

    // 分页获取商品信息
    List<Goods> getPageGoods(@Param("offset") Integer offset);

    // 根据id删除指定商品
    void deleteGoodsById(@Param("goods_no") String goods_no);

    // 根据id更新指定商品
    void updateGoodsById(@Param("goods") Goods goods);

    // 添加一个新的商品
    void insertGoods(@Param("goods") Goods goods);

    // 关键词匹配查询
    List<Goods> selectGoodsByKeyword(@Param("keyword") String keyword);
}
