package com.chandlersystem.chandler.data.models.pojo;

import com.chandlersystem.chandler.database.ChandlerDatabase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel;


@Table(database = ChandlerDatabase.class)
public class User extends BaseRXModel {
    @Column
    @PrimaryKey
    @SerializedName("gender")
    @Expose
    private int gender;

    @SerializedName("bio")
    @Expose
    @Column
    private String bio;

    @SerializedName("avatar")
    @Expose
    @Column
    private String avatar;

    @SerializedName("points")
    @Expose
    @Column
    private int points;

    @SerializedName("email")
    @Expose
    @Column
    private String email;

    @SerializedName("id")
    @Expose
    @Column
    private String id;

    @SerializedName("created")
    @Expose
    @Column
    private String created;

    @SerializedName("modified")
    @Expose
    @Column
    private String modified;

    @SerializedName("fullName")
    @Expose
    @Column
    private String fullName;

    @SerializedName("isNewAccount")
    @Expose
    @Column
    private boolean isNewAccount;

    @SerializedName("lastLogin")
    @Expose
    @Column
    private String lastLogin;

    @SerializedName("isShipper")
    @Expose
    @Column
    private boolean isShipper;

    @SerializedName("phoneNumber")
    @Expose
    @Column
    private String phoneNumber;

    @SerializedName("address")
    @Expose
    @Column
    private String address;

    @Column
    private String authorization;

    @Column
    private boolean isFirstLogin;

    public User() {
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isNewAccount() {
        return isNewAccount;
    }

    public void setNewAccount(boolean newAccount) {
        isNewAccount = newAccount;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public boolean isShipper() {
        return isShipper;
    }

    public void setShipper(boolean shipper) {
        isShipper = shipper;
    }

    @Override
    public String toString() {
        return "User{" +
                "gender=" + gender +
                ", bio='" + bio + '\'' +
                ", avatar='" + avatar + '\'' +
                ", points=" + points +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isNewAccount=" + isNewAccount +
                ", lastLogin='" + lastLogin + '\'' +
                ", isShipper=" + isShipper +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", authorization='" + authorization + '\'' +
                ", isFirstLogin=" + isFirstLogin +
                '}';
    }
}