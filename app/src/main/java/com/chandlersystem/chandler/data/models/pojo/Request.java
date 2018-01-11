
package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request implements Parcelable {

    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("productPics")
    @Expose
    private List<String> productPics = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("budget")
    @Expose
    private Budget budget;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("amount")
    @Expose
    private int amount;

    protected Request(Parcel in) {
        productName = in.readString();
        reference = in.readString();
        productPics = in.createStringArrayList();
        description = in.readString();
        budget = in.readParcelable(Budget.class.getClassLoader());
        currency = in.readString();
        deadline = in.readString();
        address = in.readString();
        owner = in.readParcelable(Owner.class.getClassLoader());
        id = in.readString();
        categoryId = in.readString();
        created = in.readString();
        modified = in.readString();
        price = in.readFloat();
        amount = in.readInt();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public List<String> getProductPics() {
        return productPics;
    }

    public void setProductPics(List<String> productPics) {
        this.productPics = productPics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(reference);
        parcel.writeStringList(productPics);
        parcel.writeString(description);
        parcel.writeParcelable(budget, i);
        parcel.writeString(currency);
        parcel.writeString(deadline);
        parcel.writeString(address);
        parcel.writeParcelable(owner, i);
        parcel.writeString(id);
        parcel.writeString(categoryId);
        parcel.writeString(created);
        parcel.writeString(modified);
        parcel.writeFloat(price);
        parcel.writeInt(amount);
    }
}
