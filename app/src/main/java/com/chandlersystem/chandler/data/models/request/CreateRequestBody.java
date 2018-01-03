package com.chandlersystem.chandler.data.models.request;

import java.util.List;

public class CreateRequestBody {
    private String productName;
    private String reference;
    private List<String> productPics;
    private String description;
    private String budget;
    private String deadline;
    private String address;

    public CreateRequestBody() {
    }

    public CreateRequestBody(String productName, String reference, List<String> productPics, String description, String budget, String deadline, String address) {
        this.productName = productName;
        this.reference = reference;
        this.productPics = productPics;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getProductPics() {
        return productPics;
    }

    public void setProductPics(List<String> productPics) {
        this.productPics = productPics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
