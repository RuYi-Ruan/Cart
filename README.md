> JavaWeb练习项目-实现一个简易的购物车

前端：购物车界面、后台管理界面

后端：实现了基本登录、注册、购物、生成订单、图片上传以及与数据库的交互(对数据表的增删改查)等功能。

- JDK17
- Tomcat10

引入依赖

```xml
<!-- json依赖 -->
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20210307</version>
</dependency>
<!-- mybatis核心依赖 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
<!-- mysql -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>
<!-- Thymeleaf模板 -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf</artifactId>
    <version>3.1.2.RELEASE</version>
</dependency>
<!-- log4j2 -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.12.4</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.12.4</version>
</dependency>
<!-- SLF4J-Log4j2 桥接依赖 -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.12.4</version>
</dependency>
```

前端使用`Bootstrap`和`layui`的上传组件。

```html
<!-- 引入bootstrap.css 及其图标库 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

<!-- 引入 layui.css -->
<link href="//unpkg.com/layui@2.9.18/dist/css/layui.css" rel="stylesheet">

<!-- 引入 bootstrap.js -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<!-- 引入 layui.js -->
<script src="//unpkg.com/layui@2.9.18/dist/layui.js"></script>
```



**数据库设计**

```sql
CREATE TABLE `tb_user` (
  `card_no` char(11) NOT NULL,
  `username` varchar(10) NOT NULL,
  `sex` char(1) NOT NULL,
  `age` int DEFAULT NULL,
  `password` char(11) NOT NULL,
  PRIMARY KEY (`card_no`),
  CONSTRAINT `tb_user_chk_1` CHECK (((`age` > 0) and (`sex` in (_utf8mb4'男',_utf8mb4'女'))))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tb_goods` (
  `goods_no` char(11) NOT NULL,
  `goods_name` varchar(15) NOT NULL,
  `in_price` double NOT NULL,
  `sale_price` double NOT NULL,
  `number` int DEFAULT NULL,
  PRIMARY KEY (`goods_no`),
  CONSTRAINT `tb_goods_chk_1` CHECK (((`number` > 0) and (`in_price` > 0) and (`sale_price` > 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE salebill (
  `goods_no` char(11) NOT NULL,
  `card_no` char(11) NOT NULL,
  `number`  int,
  `total_price` decimal(10, 2),
  `bill_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (`goods_no`) REFERENCES `tb_goods` (`goods_no`),
   FOREIGN KEY (`card_no`) REFERENCES `tb_user` (`card_no`)
);

DELIMITER $$

CREATE TRIGGER before_goods_delete
BEFORE DELETE ON tb_goods
FOR EACH ROW
BEGIN
  DELETE FROM salebill WHERE goods_no = OLD.goods_no;
END $$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER before_user_delete
BEFORE DELETE ON tb_user
FOR EACH ROW
BEGIN
  DELETE FROM salebill WHERE card_no = OLD.card_no;
END $$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER after_sale_insert
AFTER INSERT ON salebill
FOR EACH ROW
BEGIN
  UPDATE tb_goods
  SET number = number - NEW.number
  WHERE goods_no = NEW.goods_no;
END $$

DELIMITER ;
```



**界面展示**
需要用到的静态资源在webapp/assets下

登录界面
<img src="https://cdn.jsdelivr.net/gh/RuYi-Ruan/images@main/images/202411231116767.png" alt="image-20241123111614754" style="zoom: 25%;" />

注册界面

<img src="C:/Users/86134/AppData/Roaming/Typora/typora-user-images/image-20241123111739285.png" alt="image-20241123111739285" style="zoom: 25%;" />  



购物首页

<img src="https://cdn.jsdelivr.net/gh/RuYi-Ruan/images@main/images/202411231118887.png" alt="image-20241123111840869" style="zoom:25%;" />  



订单界面
<img src="https://cdn.jsdelivr.net/gh/RuYi-Ruan/images@main/images/202411231120459.png" alt="image-20241123112015468" style="zoom:25%;" />



鼠标经过头像可触发提示框
<img src="https://cdn.jsdelivr.net/gh/RuYi-Ruan/images@main/images/202411231121374.png" alt="image-20241123112118268" style="zoom:25%;" />  



后台管理首页

<img src="C:/Users/86134/AppData/Roaming/Typora/typora-user-images/image-20241123112156263.png" alt="image-20241123112156263" style="zoom:25%;" />  



各模块页面(基本一致)

<img src="https://cdn.jsdelivr.net/gh/RuYi-Ruan/images@main/images/202411231122368.png" alt="image-20241123112241189" style="zoom:25%;" />  

