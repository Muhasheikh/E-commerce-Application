package com.example.ecommerce.Model;

public class Cart {

    private String pid,pname,quantity,price,discount;

    public Cart() {
    }

    public Cart(String pid, String pname, String quantity, String price, String discount) {
        this.pid = pid;
        this.pname = pname;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    ;}
