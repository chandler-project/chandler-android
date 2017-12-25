package com.chandlersystem.chandler.data.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetrofitResponseItem<T> {
    @SerializedName("data")
    @Expose
    public T item;

    @SerializedName("error")
    @Expose
    public String error;
}
