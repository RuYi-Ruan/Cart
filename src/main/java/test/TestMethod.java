package test;

import com.example.cart.dao.SaleBillService;
import com.example.cart.dao.UserService;
import com.example.cart.entity.Goods;
import com.example.cart.entity.SaleBill;
import com.example.cart.entity.User;
import com.example.cart.mapper.GoodsMapper;
import com.example.cart.dao.GoodsService;
import com.example.cart.utils.GetSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TestMethod {

    public static void main(String[] args) {

//        User user = new User();
//        user.setCardNo("S202400043");
//        user.setUserName("崔俊豪");
//        user.setPassword("123456");
//        user.setSex("男");
//        user.setAge(20);

//        UserService.updateUser(user);

//        Goods goods = new Goods();
//        goods.setGoodsNo("G0000000001");
//        goods.setGoodsName("小苹果");
//        goods.setInPrice(5.0);
//        goods.setSalePrice(12.0);
//        goods.setNumber(100);
//        goods.setImgUrl("http://localhost:8080/cart/assets/images/apple.jpg");
//
//        GoodsService.updateGoods(goods);

//        SaleBill saleBill = new SaleBill();
//        saleBill.setGoodsNo("G0000000004");
//        saleBill.setCardNo("S202400034");
//        saleBill.setNumber(2);
//        saleBill.setTotalPrice(52.5);
//        saleBill.setBillDate("2024-11-20 15:25:24");
//
//        SaleBillService.addSaleBill(saleBill);

//        List<User> userList =  UserService.selectByKeyword("a");
//        System.out.println(userList);

//        List<Goods> goodsList = GoodsService.selectGoodsByKeyword("苹果");
//        for (Goods goods : goodsList) {
//            System.out.println(goods);
//        }

        List<SaleBill> billList = SaleBillService.selectSaleBillByKeyword("2");
        for (SaleBill bill : billList) {
            System.out.println(bill);
        }
    }
}
