
package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deal implements Parcelable {

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
    private double shippingPrice;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("shippingTime")
    @Expose
    private String shippingTime;
    @SerializedName("productDesc")
    @Expose
    private String productDesc;
    @SerializedName("productPics")
    @Expose
    private List<String> productPics = null;
    @SerializedName("upVoters")
    @Expose
    private List<String> upVoters;
    @SerializedName("downVoters")
    @Expose
    private List<String> downVoters;
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
    @SerializedName("requesters")
    @Expose
    private List<Owner> requesters = null;
    @SerializedName("voters")
    @Expose
    private List<Owner> voters = null;
    @SerializedName("upvote")
    @Expose
    private int upvote;
    @SerializedName("downvote")
    @Expose
    private int downvote;
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
    @SerializedName("category")
    @Expose
    private Category category;

    private boolean isUpvoted;

    private boolean isDownvoted;

    public Deal() {
    }

    protected Deal(Parcel in) {
        productName = in.readString();
        reference = in.readString();
        price = in.readDouble();
        shippingPrice = in.readDouble();
        currency = in.readString();
        shippingTime = in.readString();
        productDesc = in.readString();
        productPics = in.createStringArrayList();
        upVoters = in.createStringArrayList();
        downVoters = in.createStringArrayList();
        shipper = in.readParcelable(Shipper.class.getClassLoader());
        requesters = in.createTypedArrayList(Owner.CREATOR);
        voters = in.createTypedArrayList(Owner.CREATOR);
        upvote = in.readInt();
        downvote = in.readInt();
        id = in.readString();
        memberId = in.readString();
        created = in.readString();
        modified = in.readString();
        categoryId = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Deal> CREATOR = new Creator<Deal>() {
        @Override
        public Deal createFromParcel(Parcel in) {
            return new Deal(in);
        }

        @Override
        public Deal[] newArray(int size) {
            return new Deal[size];
        }
    };

    public boolean isUpvoted() {
        return isUpvoted;
    }

    public void setUpvoted(boolean upvoted) {
        isUpvoted = upvoted;
    }

    public boolean isDownvoted() {
        return isDownvoted;
    }

    public void setDownvoted(boolean downvoted) {
        isDownvoted = downvoted;
    }

    public List<String> getUpVoters() {
        return upVoters;
    }

    public void setUpVoters(List<String> upVoters) {
        this.upVoters = upVoters;
    }

    public List<String> getDownVoters() {
        return downVoters;
    }

    public void setDownVoters(List<String> downVoters) {
        this.downVoters = downVoters;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
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

    public List<Owner> getRequesters() {
        return requesters;
    }

    public void setRequesters(List<Owner> requesters) {
        this.requesters = requesters;
    }

    public List<Owner> getVoters() {
        return voters;
    }

    public void setVoters(List<Owner> voters) {
        this.voters = voters;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(reference);
        dest.writeDouble(price);
        dest.writeDouble(shippingPrice);
        dest.writeString(currency);
        dest.writeString(shippingTime);
        dest.writeString(productDesc);
        dest.writeStringList(productPics);
        dest.writeStringList(upVoters);
        dest.writeStringList(downVoters);
        dest.writeParcelable(shipper, flags);
        dest.writeTypedList(requesters);
        dest.writeTypedList(voters);
        dest.writeInt(upvote);
        dest.writeInt(downvote);
        dest.writeString(id);
        dest.writeString(memberId);
        dest.writeString(created);
        dest.writeString(modified);
        dest.writeString(categoryId);
        dest.writeParcelable(category, flags);
    }
}
