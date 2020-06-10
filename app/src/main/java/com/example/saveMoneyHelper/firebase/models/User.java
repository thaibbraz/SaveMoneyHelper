package com.example.saveMoneyHelper.firebase.models;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public Currency currency = new Currency("$", true, true);
    public UserSettings userSettings = new UserSettings();
    public Wallet wallet = new Wallet();
    public Map<String, WalletEntryCategory> customCategories = new HashMap<>();

    public User() {

    }
}
