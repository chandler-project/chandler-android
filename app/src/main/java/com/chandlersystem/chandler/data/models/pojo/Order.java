package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable {
    @SerializedName("requester")
    @Expose
    private Owner owner;

    @SerializedName("shipper")
    @Expose
    private Shipper shipper;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("item")
    @Expose
    private Item item;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("productPrice")
    @Expose
    private double productPrice;

    @SerializedName("shippingPrice")
    @Expose
    private double shippingPrice;

    @SerializedName("spend")
    @Expose
    private int spend;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("requestId")
    @Expose
    private String requestId;

    @SerializedName("dealId")
    @Expose
    private String dealId;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("address")
    @Expose
    private String address;

    private int buttonId;

    public Order() {
    }

    protected Order(Parcel in) {
        owner = in.readParcelable(Owner.class.getClassLoader());
        shipper = in.readParcelable(Shipper.class.getClassLoader());
        title = in.readString();
        status = in.readString();
        item = in.readParcelable(Item.class.getClassLoader());
        id = in.readString();
        productPrice = in.readDouble();
        shippingPrice = in.readDouble();
        spend = in.readInt();
        reason = in.readString();
        requestId = in.readString();
        dealId = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public int getSpend() {
        return spend;
    }

    public void setSpend(int spend) {
        this.spend = spend;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(shipper, flags);
        dest.writeString(title);
        dest.writeString(status);
        dest.writeParcelable(item, flags);
        dest.writeString(id);
        dest.writeDouble(productPrice);
        dest.writeDouble(shippingPrice);
        dest.writeInt(spend);
        dest.writeString(reason);
        dest.writeString(requestId);
        dest.writeString(dealId);
        dest.writeString(phoneNumber);
        dest.writeString(address);
    }
}
