package com.chandlersystem.chandler.data.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetrofitResponseListItem<T> {
    @SerializedName("data")
    @Expose
    public List<T> items;

    @SerializedName("total")
    public int total;
}
