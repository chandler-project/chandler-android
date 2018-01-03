
package com.chandlersystem.chandler.data.models.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Files {

    @SerializedName("fileUpload")
    @Expose
    private List<FileUpload> fileUpload = null;

    public List<FileUpload> getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(List<FileUpload> fileUpload) {
        this.fileUpload = fileUpload;
    }

}
