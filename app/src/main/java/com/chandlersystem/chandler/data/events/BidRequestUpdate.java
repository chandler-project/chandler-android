package com.chandlersystem.chandler.data.events;

import com.chandlersystem.chandler.data.models.pojo.Bidder;
import com.chandlersystem.chandler.data.models.pojo.Owner;

import java.util.List;

public class BidRequestUpdate {
    public List<Bidder> bidders;

    public BidRequestUpdate(List<Bidder> bidders) {
        this.bidders = bidders;
    }
}
