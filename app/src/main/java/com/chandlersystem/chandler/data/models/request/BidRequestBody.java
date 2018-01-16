package com.chandlersystem.chandler.data.models.request;

public class BidRequestBody {
    private float price;
    private String sentence;
    private int spend;

    public BidRequestBody() {
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
}
