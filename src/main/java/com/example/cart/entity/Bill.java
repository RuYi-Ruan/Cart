package com.example.cart.entity;

public class Bill {
    private String id;
    private String goodsName;
    private int quantity;
    private double salePrice;
    private double totalPrice;

    public Bill() {}
    public Bill(String id, String goodsName, int quantity, double salePrice, double totalPrice) {
        this.id = id;
        this.goodsName = goodsName;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Bill [id=" + id + ", goodsName=" + goodsName + ", quantity=" + quantity + "]";
    }
}

