package com.chandlersystem.chandler.data.models.request;

public class PaymentBody {
   private PaymentAllBody payment;

    public PaymentBody() {
    }

    public PaymentAllBody getPayment() {
        return payment;
    }

    public void setPayment(PaymentAllBody payment) {
        this.payment = payment;
    }
}
