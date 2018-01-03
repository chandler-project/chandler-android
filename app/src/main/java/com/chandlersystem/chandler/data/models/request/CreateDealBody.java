package com.chandlersystem.chandler.data.models.request;

import java.util.List;

public class CreateDealBody {
    private String productName;
    private String reference;
    private float price;
    private float shippingPrice;
    private String currency;
    private String remainTime;
    private String productDesc;
    private List<String> productPics;

    public CreateDealBody() {
    }

    public CreateDealBody(String productName, String reference, float price, float shippingPrice, String currency, String remainTime, String productDesc, List<String> productPics) {
        this.productName = productName;
        this.reference = reference;
        this.price = price;
        this.shippingPrice = shippingPrice;
        this.currency = currency;
        this.remainTime = remainTime;
        this.productDesc = productDesc;
        this.productPics = productPics;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(float shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public List<String> getProductPics() {
        return productPics;
    }

    public void setProductPics(List<String> productPics) {
        this.productPics = productPics;
    }
}
