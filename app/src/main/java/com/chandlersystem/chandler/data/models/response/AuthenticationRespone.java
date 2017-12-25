package com.chandlersystem.chandler.data.models.response;

import com.chandlersystem.chandler.data.models.pojo.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationRespone {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ttl")
    @Expose
    private Integer ttl;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("isFirstLogin")
    @Expose
    private Boolean isFirstLogin;
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;
    @SerializedName("user")
    @Expose
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
