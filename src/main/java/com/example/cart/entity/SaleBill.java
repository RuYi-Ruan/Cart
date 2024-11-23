package com.example.cart.entity;

public class SaleBill {
    private String goodsNo;
    private String cardNo;
    private int number;
    private double totalPrice;
    private String billDate;

    public SaleBill(){}

    public SaleBill(String goodsNo, String cardNo, int number, double totalPrice, String billDate) {
        this.goodsNo = goodsNo;
        this.cardNo = cardNo;
        this.number = number;
        this.totalPrice = totalPrice;
        this.billDate = billDate;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
