package com.chandlersystem.chandler.data.events;

import com.chandlersystem.chandler.data.models.pojo.Owner;

import java.util.List;

public class BuyDealUpdate {
    public List<Owner> requesters;

    public BuyDealUpdate(List<Owner> requesters) {
        this.requesters = requesters;
    }
}
