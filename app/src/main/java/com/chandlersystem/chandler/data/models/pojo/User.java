package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("invalidAttempts")
    @Expose
    private Integer invalidAttempts;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("fbAccessToken")
    @Expose
    private String fbAccessToken;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("type")
    @Expose
    private List<Integer> type = null;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("isNewAccount")
    @Expose
    private Boolean isNewAccount;
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getInvalidAttempts() {
        return invalidAttempts;
    }

    public void setInvalidAttempts(Integer invalidAttempts) {
        this.invalidAttempts = invalidAttempts;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
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

    public List<Integer> getType() {
        return type;
    }

    public void setType(List<Integer> type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getIsNewAccount() {
        return isNewAccount;
    }

    public void setIsNewAccount(Boolean isNewAccount) {
        this.isNewAccount = isNewAccount;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.gender);
        dest.writeValue(this.invalidAttempts);
        dest.writeString(this.lang);
        dest.writeString(this.fbAccessToken);
        dest.writeString(this.email);
        dest.writeValue(this.emailVerified);
        dest.writeString(this.id);
        dest.writeString(this.created);
        dest.writeString(this.modified);
        dest.writeList(this.type);
        dest.writeString(this.fullName);
        dest.writeValue(this.isNewAccount);
        dest.writeString(this.lastLogin);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.invalidAttempts = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lang = in.readString();
        this.fbAccessToken = in.readString();
        this.email = in.readString();
        this.emailVerified = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.id = in.readString();
        this.created = in.readString();
        this.modified = in.readString();
        this.type = new ArrayList<Integer>();
        in.readList(this.type, Integer.class.getClassLoader());
        this.fullName = in.readString();
        this.isNewAccount = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.lastLogin = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}