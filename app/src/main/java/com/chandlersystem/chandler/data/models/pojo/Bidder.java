package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bidder implements Parcelable{
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("points")
    @Expose
    private int points;

    @SerializedName("price")
    @Expose
    private float price;

    @SerializedName("sentence")
    @Expose
    private String sentence;

    @SerializedName("spend")
    @Expose
    private int spend;

    public Bidder() {
    }

    protected Bidder(Parcel in) {
        id = in.readString();
        avatar = in.readString();
        fullName = in.readString();
        points = in.readInt();
        price = in.readFloat();
        sentence = in.readString();
        spend = in.readInt();
    }

    public static final Creator<Bidder> CREATOR = new Creator<Bidder>() {
        @Override
        public Bidder createFromParcel(Parcel in) {
            return new Bidder(in);
        }

        @Override
        public Bidder[] newArray(int size) {
            return new Bidder[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public int getSpend() {
        return spend;
    }

    public void setSpend(int spend) {
        this.spend = spend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(avatar);
        dest.writeString(fullName);
        dest.writeInt(points);
        dest.writeFloat(price);
        dest.writeString(sentence);
        dest.writeInt(spend);
    }
}
