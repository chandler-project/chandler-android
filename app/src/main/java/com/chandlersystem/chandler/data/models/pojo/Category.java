package com.chandlersystem.chandler.data.models.pojo;

public class Category {
    public String imageUrl;
    public String name;
    public String subCategory;
    public boolean isSelected;

    public Category() {
    }

    public Category(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public Category(String imageUrl, String name, String subCategory) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.subCategory = subCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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
