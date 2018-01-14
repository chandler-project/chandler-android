package com.chandlersystem.chandler.data.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feedback {
    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("owner")
    @Expose
    private Owner owner;

    public Feedback() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
