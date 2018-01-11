package com.chandlersystem.chandler.data.models.request;

import java.util.List;

public class CommentBody {
    private String content;
    private List<String> images;

    public CommentBody() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
