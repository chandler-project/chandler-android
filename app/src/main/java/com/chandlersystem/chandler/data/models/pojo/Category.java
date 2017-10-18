package com.chandlersystem.chandler.data.models.pojo;

public class Category {
    public String imageUrl;
    public String name;
    public boolean isSelected;

    public Category() {
    }

    public Category(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggleSelectedValue() {
        setSelected(!isSelected);
    }
}
