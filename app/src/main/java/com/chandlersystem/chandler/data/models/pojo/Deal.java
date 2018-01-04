
package com.chandlersystem.chandler.data.models.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deal {

    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("shippingPrice")
    @Expose
    private int shippingPrice;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("productDesc")
    @Expose
    private String productDesc;
    @SerializedName("productPics")
    @Expose
    private List<String> productPics = null;
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
    @SerializedName("requesters")
    @Expose
    private List<Object> requesters = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("remainTime")
    @Expose
    private String remainTime;
    @SerializedName("budget")
    @Expose
    private String budget;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public List<Object> getRequesters() {
        return requesters;
    }

    public void setRequesters(List<Object> requesters) {
        this.requesters = requesters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

}
