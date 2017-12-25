package com.chandlersystem.chandler.data.models.request;

import java.util.List;

public class SelectCategoryRequest {
    List<String> categoryIds;

    public SelectCategoryRequest(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
