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

    @SerializedName("total")
    @Expose
    private long total;

    public Order() {
    }

    protected Order(Parcel in) {
        owner = in.readParcelable(Owner.class.getClassLoader());
        shipper = in.readParcelable(Shipper.class.getClassLoader());
        title = in.readString();
        status = in.readString();
        item = in.readParcelable(Item.class.getClassLoader());
        id = in.readString();
        total = in.readLong();
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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
        dest.writeLong(total);
    }
}
