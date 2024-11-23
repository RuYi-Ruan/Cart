package com.example.cart.entity;

public class Goods {
    private String goodsNo;
    private String goodsName;
    private double salePrice;
    private double inPrice;
    private int number;
    private String imgUrl;

    public Goods(){}

    public Goods(String goodsNo, String goodsName, double salePrice, double inPrice, int number, String imgUrl) {
        this.goodsNo = goodsNo;
        this.goodsName = goodsName;
        this.salePrice = salePrice;
        this.inPrice = inPrice;
        this.number = number;
        this.imgUrl = imgUrl;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getInPrice() {
        return inPrice;
    }

    public void setInPrice(double inPrice) {
        this.inPrice = inPrice;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Goods [goodsNo=" + goodsNo + ", goodsName=" + goodsName + ", " +
                "salePrice=" + salePrice + ", inPrice=" + inPrice + ", number=" +
                number + ", imgUrl=" + imgUrl + "]";
    }
}
