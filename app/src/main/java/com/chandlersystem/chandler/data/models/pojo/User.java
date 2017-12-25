package com.chandlersystem.chandler.data.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.chandlersystem.chandler.database.ChandlerDatabase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel;

import java.util.ArrayList;
import java.util.List;


@Table(database = ChandlerDatabase.class)
public class User extends BaseRXModel implements Parcelable {

    @Column
    @PrimaryKey
    @SerializedName("gender")
    @Expose
    private int gender;

    @Column
    @SerializedName("invalidAttempts")
    @Expose
    private int invalidAttempts;

    @SerializedName("lang")
    @Expose
    @Column
    private String lang;

    @SerializedName("fbAccessToken")
    @Expose
    @Column
    private String fbAccessToken;

    @SerializedName("email")
    @Expose
    @Column
    private String email;

    @SerializedName("emailVerified")
    @Expose
    @Column
    private boolean emailVerified;

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

    public User() {
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getInvalidAttempts() {
        return invalidAttempts;
    }

    public void setInvalidAttempts(int invalidAttempts) {
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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
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

    protected User(Parcel in) {
        gender = in.readInt();
        invalidAttempts = in.readInt();
        lang = in.readString();
        fbAccessToken = in.readString();
        email = in.readString();
        emailVerified = in.readByte() != 0;
        id = in.readString();
        created = in.readString();
        modified = in.readString();
        fullName = in.readString();
        isNewAccount = in.readByte() != 0;
        lastLogin = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(gender);
        parcel.writeInt(invalidAttempts);
        parcel.writeString(lang);
        parcel.writeString(fbAccessToken);
        parcel.writeString(email);
        parcel.writeByte((byte) (emailVerified ? 1 : 0));
        parcel.writeString(id);
        parcel.writeString(created);
        parcel.writeString(modified);
        parcel.writeString(fullName);
        parcel.writeByte((byte) (isNewAccount ? 1 : 0));
        parcel.writeString(lastLogin);
    }
}