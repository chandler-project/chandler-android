package com.chandlersystem.chandler.data.models.request;

import java.util.List;

public class CreateRequestBody {
    private String productName;
    private String reference;
    private List<String> productPics;
    private String description;
    private BudgetBody budget;
    private String currency;
    private String deadline;
    private String address;
    private float price;
    private int amount;

    public CreateRequestBody() {
    }

    public static CreateRequestBody valueOf(CreateDealBody createDealBody) {
        CreateRequestBody createRequestBody = new CreateRequestBody();
        createRequestBody.setProductName(createDealBody.getProductName());
        createRequestBody.setPrice(createDealBody.getPrice());
        createRequestBody.setReference(createDealBody.getReference());
        createRequestBody.setDescription(createDealBody.getProductDesc());
        createRequestBody.setCurrency("VND");
        createRequestBody.setDeadline(createDealBody.getShippingTime());
        return createRequestBody;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public BudgetBody getBudget() {
        return budget;
    }

    public void setBudget(BudgetBody budget) {
        this.budget = budget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
