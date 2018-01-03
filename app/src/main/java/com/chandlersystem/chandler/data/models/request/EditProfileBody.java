package com.chandlersystem.chandler.data.models.request;

public class EditProfileBody {
    private String bio;
    private String name;
    private String avatar;

    public EditProfileBody() {
    }

    public EditProfileBody(String bio, String name, String avatar) {
        this.bio = bio;
        this.name = name;
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
