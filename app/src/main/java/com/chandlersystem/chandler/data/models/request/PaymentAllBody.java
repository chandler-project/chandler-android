package com.chandlersystem.chandler.data.models.request;

public class PaymentAllBody {
    private String address;
    private String phoneNumber;
    private String method = "VISA";
    private String expireOn = "27/10";
    private String firstName = "Phat";
    private String lastName = "Nguyen";
    private String cardNumber = "1234-5678-9101-1234";

    public PaymentAllBody() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
