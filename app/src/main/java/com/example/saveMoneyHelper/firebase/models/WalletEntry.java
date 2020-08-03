package com.example.saveMoneyHelper.firebase.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class WalletEntry {

    public String categoryID;
    public String name;
    public String type;
    public long timestamp;
    public long balanceDifference;

    public WalletEntry() {

    }

    public WalletEntry(String categoryID, String name, long timestamp, long balanceDifference, String type) {
        this.categoryID = categoryID;
        this.name = name;
        this.type = type;
        this.timestamp = -timestamp;
        this.balanceDifference = balanceDifference;
    }

}