package com.example.cart.dao;

import com.example.cart.entity.Goods;
import com.example.cart.mapper.GoodsMapper;
import com.example.cart.utils.GetSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class GoodsService {
    // 获取已有商品信息
    public static List<Goods> getAllGoods(){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);

        return goodsMapper.selectAllGoods();
    }

    // 获取商品总数
    public static Integer getGoodsCount(){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        return goodsMapper.getTotalGoodsCount();
    }

    // 获取分页商品信息
    public static List<Goods> getPageGoods(Integer offset){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        return goodsMapper.getPageGoods(offset);
    }

    // 根据id删除指定商品
    public static void deleteGoods(String id){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        try{
            goodsMapper.deleteGoodsById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 根据id更新指定商品
    public static void updateGoods(Goods goods){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        try{
            goodsMapper.updateGoodsById(goods);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 添加单个商品
    public static void addGoods(Goods goods){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        try{
            goodsMapper.insertGoods(goods);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 关键词匹配查询
    public static List<Goods> selectGoodsByKeyword(String keyword){
        SqlSession sqlSession = GetSqlSession.createSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);

        return goodsMapper.selectGoodsByKeyword(keyword);
    }
}
