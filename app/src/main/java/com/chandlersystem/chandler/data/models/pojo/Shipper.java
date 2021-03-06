
package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shipper implements Parcelable{
    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("points")
    @Expose
    private int points;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    protected Shipper(Parcel in) {
        avatar = in.readString();
        id = in.readString();
        points = in.readInt();
        fullName = in.readString();
    }

    public static final Creator<Shipper> CREATOR = new Creator<Shipper>() {
        @Override
        public Shipper createFromParcel(Parcel in) {
            return new Shipper(in);
        }

        @Override
        public Shipper[] newArray(int size) {
            return new Shipper[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(avatar);
        parcel.writeString(id);
        parcel.writeInt(points);
        parcel.writeString(fullName);
    }
}
