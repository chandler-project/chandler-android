package com.chandlersystem.chandler.data.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderCount {
    @SerializedName("count")
    @Expose
    private int count;

    public OrderCount() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
