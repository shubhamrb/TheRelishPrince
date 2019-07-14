package com.therelishprince.model;

public class Category {
    private String name;
    private String image;
    private String price;
    private String dis;
    private String id;
    private String discount;

    public Category() {
    }

    public Category(String name, String image, String price, String dis, String id, String discount) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.dis = dis;
        this.id = id;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}